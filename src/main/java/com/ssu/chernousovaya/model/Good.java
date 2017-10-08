package com.ssu.chernousovaya.model;

import org.springframework.stereotype.Component;

@Component
public class Good {

    private Integer id;
    private String name;
    private Integer price;

    public Good(Integer id, String name, Integer price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Good(String name, Integer price){
        this.id = 0;
        this.name = name;
        this.price = price;
    }

    public Good(){

    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
