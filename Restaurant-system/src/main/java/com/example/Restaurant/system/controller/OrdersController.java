package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.*;
import com.example.Restaurant.system.service.*;
import com.example.Restaurant.system.untils.CheckUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Resource
    private OrdersServiceImpl ordersService;
    @Resource
    private OrdersContainerServiceImpl ordersContainerService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private CuisineServiceImpl cuisineService;
    @Resource
    private CuisineUsingServiceImpl cuisineUsingService;
    @Resource
    private IngredientServiceImpl ingredientService;

    @SuppressWarnings("SuspiciousToArrayCall")//屏蔽警告
    @PostMapping("/save")
    public Result<?> save(@NotNull @RequestBody Map<String, Object> input) {
        //JSON解析
        Orders orders = JSONUtil.toBean(JSONUtil.toJsonStr(input.get("orders")), Orders.class);
        OrdersContainer[] ordersContainers = JSONUtil.parseArray(
                JSONUtil.toJsonStr(input.get("ordersContainer"))).toArray(new OrdersContainer[0]);
        //储量检测
        HashMap<BigInteger, Double> reservesTestMap = new HashMap<>();
        if (orders.getForcing() == 0) {
            for (OrdersContainer ordersContainer : ordersContainers) {
                List<CuisineUsing> cuisineUsingList = cuisineUsingService.getListByCuisineIDWithIngredientName
                        (ordersContainer.getCuisineID());
                for (CuisineUsing cuisineUsing : cuisineUsingList) {
                    Double origin = reservesTestMap.put(cuisineUsing.getIngredientID(),
                            cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001);
                    if (origin != null)
                        reservesTestMap.put(cuisineUsing.getIngredientID(),
                                cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001 + origin);
                }
            }
            for (BigInteger ingredientID : reservesTestMap.keySet()) {
                if (ingredientService.getById(ingredientID).getReserves() < reservesTestMap.get(ingredientID))
                    return Result.error(-1, ingredientService.getNameByID(ingredientID)
                            + "储量不足以支持本次订单，如需要请使用强制下单");
            }
        }
        //orders存储
        if (Objects.equals(orders.getCustomer(), ""))
            return Result.error(-1, "顾客名未填写");
        if (orders.getType() == null)
            return Result.error(-1, "类型不能为空");
        if (!(orders.getType() == 0 || orders.getType() == 1))
            return Result.error(-1, "类型错误");
        if (!userService.userIDCheck(orders.getUserID()))
            return Result.error(-1, "处理人错误");
        double price = 0;
        for (OrdersContainer ordersContainer : ordersContainers) {
            price += ordersContainer.getAmount() * cuisineService.getPriceByID(ordersContainer.getCuisineID());
        }
        orders.setPrice(price);
        ordersService.save(orders);//mybatis已自动对id赋值
        //获取ordersID
        BigInteger ordersID = orders.getID();
        //对ordersContainer数组批量处理ID
        for (OrdersContainer ordersContainer : ordersContainers) {
            ordersContainer.setOrdersID(ordersID);
        }
        //ordersContainer存储
        List<OrdersContainer> data = Arrays.asList(ordersContainers);
        ordersContainerService.saveBatch(data);
        if (orders.getForcing() == 0) {
            //储量修改
            for (BigInteger ingredientID : reservesTestMap.keySet()) {
                Ingredient update = ingredientService.getById(ingredientID);
                update.setReserves(update.getReserves() - reservesTestMap.get(ingredientID));
                ingredientService.saveOrUpdate(update);
            }
        }
        return Result.success();
    }

    //find只使用ID
    @GetMapping("/find")
    public Result<?> find(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String search) {
        List<Orders> ordersList = ordersService.getListWithUserName();
        if (StrUtil.isNotBlank(search) && CheckUtil.isNum(search)) {
            ordersList = ordersService.getListByIDWithUserName(new BigInteger(search));
        }
        return getResPage(pageNum, pageSize, ordersList);
    }

    @GetMapping("/view")
    public Result<?> viewPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Orders> ordersList = ordersService.getListWithUserName();
        return getResPage(pageNum, pageSize, ordersList);
    }

    @GetMapping("/viewEatIn")
    public Result<?> viewEatInPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Orders> ordersList = ordersService.getEatInListWithUserName();
        return getResPage(pageNum, pageSize, ordersList);
    }

    @GetMapping("/viewTakeOut")
    public Result<?> viewTakeOutPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Orders> ordersList = ordersService.getTakeOutListWithUserName();
        return getResPage(pageNum, pageSize, ordersList);
    }

    @GetMapping("/viewContainerList")
    public Result<?> viewContainerList(@RequestParam String ordersID) {
        BigInteger input = new BigInteger(ordersID);
        List<OrdersContainer> resList = ordersContainerService.getListByOrdersIDWithCuisineName(input);
        return Result.success(resList);
    }

    @GetMapping("/delete")
    public Result<?> delete(@RequestParam String ordersID) {
        BigInteger input = new BigInteger(ordersID);
        //耗材获取
        OrdersContainer[] ordersContainers = ordersContainerService.getListByOrdersIDWithCuisineName(input)
                .toArray(new OrdersContainer[0]);
        HashMap<BigInteger, Double> reservesTestMap = new HashMap<>();
        for (OrdersContainer ordersContainer : ordersContainers) {
            List<CuisineUsing> cuisineUsingList = cuisineUsingService.getListByCuisineIDWithIngredientName
                    (ordersContainer.getCuisineID());
            for (CuisineUsing cuisineUsing : cuisineUsingList) {
                Double origin = reservesTestMap.put(cuisineUsing.getIngredientID(),
                        cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001);
                if (origin != null)
                    reservesTestMap.put(cuisineUsing.getIngredientID(),
                            cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001 + origin);
            }
        }
        if (ordersService.getById(input).getForcing() == 0) {
            //储量回退
            for (BigInteger ingredientID : reservesTestMap.keySet()) {
                Ingredient ingredient = ingredientService.getById(ingredientID);
                ingredient.setReserves(ingredient.getReserves() + reservesTestMap.get(ingredientID));
                ingredientService.updateById(ingredient);
            }
        }
        //删除container内容
        List<BigInteger> IDList = ordersContainerService.getIDListByOrdersID(input);
        ordersContainerService.removeBatchByIds(IDList);
        //删除order
        ordersService.removeById(input);
        return Result.success();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @PostMapping("/update")
    public Result<?> update(@NotNull @RequestBody Map<String, Object> input) {
        //JSON解析
        OrdersContainer[] newOrdersContainers = JSONUtil.parseArray(
                JSONUtil.toJsonStr(input.get("ordersContainer"))).toArray(new OrdersContainer[0]);
        BigInteger ordersID = newOrdersContainers[0].getOrdersID();
        //旧耗材获取
        HashMap<BigInteger, Double> reservesOldMap = new HashMap<>();
        OrdersContainer[] oldOrdersContainers = ordersContainerService.getListByOrdersIDWithCuisineName(ordersID).
                toArray(new OrdersContainer[0]);
        for (OrdersContainer ordersContainer : oldOrdersContainers) {
            List<CuisineUsing> cuisineUsingList = cuisineUsingService.getListByCuisineIDWithIngredientName
                    (ordersContainer.getCuisineID());
            for (CuisineUsing cuisineUsing : cuisineUsingList) {
                Double origin = reservesOldMap.put(cuisineUsing.getIngredientID(),
                        cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001);
                if (origin != null)
                    reservesOldMap.put(cuisineUsing.getIngredientID(),
                            cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001 + origin);
            }
        }
        //新耗材获取
        HashMap<BigInteger, Double> reservesNewMap = new HashMap<>();
        for (OrdersContainer ordersContainer : newOrdersContainers) {
            List<CuisineUsing> cuisineUsingList = cuisineUsingService.getListByCuisineIDWithIngredientName
                    (ordersContainer.getCuisineID());
            for (CuisineUsing cuisineUsing : cuisineUsingList) {
                Double origin = reservesNewMap.put(cuisineUsing.getIngredientID(),
                        cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001);
                if (origin != null)
                    reservesNewMap.put(cuisineUsing.getIngredientID(),
                            cuisineUsing.getDosage() * ordersContainer.getAmount() * 0.001 + origin);
            }
        }
        //储量修改
        if (ordersService.getById(ordersID).getForcing() == 0) {
            //储量回退
            for (BigInteger ingredientID : reservesOldMap.keySet()) {
                Ingredient ingredient = ingredientService.getById(ingredientID);
                ingredient.setReserves(ingredient.getReserves() + reservesOldMap.get(ingredientID));
                ingredientService.updateById(ingredient);
            }
            //储量检测
            for (BigInteger ingredientID : reservesNewMap.keySet()) {
                if (ingredientService.getById(ingredientID).getReserves() < reservesNewMap.get(ingredientID)){
                    //无法完成要求，储量再回退
                    for (BigInteger ingredientId : reservesOldMap.keySet()) {
                        Ingredient ingredient = ingredientService.getById(ingredientId);
                        ingredient.setReserves(ingredient.getReserves() - reservesOldMap.get(ingredientId));
                        ingredientService.updateById(ingredient);
                    }
                    return Result.error(-1, ingredientService.getNameByID(ingredientID)
                            + "储量不足以支持本次订单，如需要请使用强制下单");
                }
            }
            //储量使用
            for (BigInteger ingredientID : reservesNewMap.keySet()) {
                Ingredient update = ingredientService.getById(ingredientID);
                update.setReserves(update.getReserves() - reservesNewMap.get(ingredientID));
                ingredientService.saveOrUpdate(update);
            }
        }
        //删除container内容
        List<BigInteger> IDList = ordersContainerService.getIDListByOrdersID(ordersID);
        ordersContainerService.removeBatchByIds(IDList);
        //对ordersContainer数组批量处理ID
        for (OrdersContainer ordersContainer : newOrdersContainers) {
            ordersContainer.setOrdersID(ordersID);
        }
        //ordersContainer存储
        List<OrdersContainer> data = Arrays.asList(newOrdersContainers);
        ordersContainerService.saveBatch(data);
        return Result.success();
    }

    /**
     * 分页函数
     *
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     */
    @Nullable
    private Result<?> getResPage(Integer currentPage, Integer pageSize, @NotNull List<Orders> list) {
        Page<Orders> resPage = new Page<>();
        int size = list.size();
        if (size == 0) {
            return null;
        }
        if (pageSize > size) {
            pageSize = size;
        }
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (currentPage > maxPage) {
            currentPage = maxPage;
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List<Orders> pageList = new ArrayList<>();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }
        resPage.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return Result.success(resPage);
    }
}
