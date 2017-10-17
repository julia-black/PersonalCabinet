package com.ssu.chernousovaya.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class User {

    private Integer id;

    private String login;

    private String password;

    private List<Good> cart;

    public User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.cart = new ArrayList<>();
    }

    public User(Integer id, String login, String password, List<Good> cart) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.cart = cart;
    }

    public void addGoodToCart(Integer id, String name, Integer price){
        Good good = new Good(id, name, price);
        cart.add(good);
    }


    public User() {

    }

    public void addToCart(Good good){
        cart.add(good);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Good> getCart() {
        return cart;
    }

    public void setCart(List<Good> cart) {
        this.cart = cart;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean buyGoodById(Integer id) {
        for (int i = 0; i < cart.size() ; i++) {
            if(cart.get(i).getId() == id){
                System.out.println("Buying");
                cart.remove(i);
                return true;
            }
        }
        return false;
    }

    public Good findById(Integer id){
        Good good = null;
        for (int i = 0; i < cart.size() ; i++) {
            if(cart.get(i).getId() == id){
               good = cart.get(i);
            }
        }
        return good;
    }

    public boolean deleteGoodById(Integer id) {
        for (int i = 0; i < cart.size() ; i++) {
            if(cart.get(i).getId() == id){
                cart.remove(i);
                return true;
            }
        }
        return false;
    }
}