package com.example.Restaurant.system.common;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Result<T> {
    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    private int code;//校验码：0为成功非0失败
    private String msg;
    private T data;

    @NotNull
    public static Result<?> success() {
        Result<?> result = new Result<>();
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    @NotNull
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    @NotNull
    public static Result<?> error(int code, String msg) {
        Result<?> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
