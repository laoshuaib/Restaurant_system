package com.example.Restaurant.system;

import com.example.Restaurant.system.controller.CuisineController;
import com.example.Restaurant.system.entity.Cuisine;
import com.example.Restaurant.system.mapper.CuisineMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RestaurantSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

}
