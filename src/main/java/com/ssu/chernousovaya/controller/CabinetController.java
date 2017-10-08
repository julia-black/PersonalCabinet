package com.ssu.chernousovaya.controller;

import com.ssu.chernousovaya.model.Good;
import com.ssu.chernousovaya.model.User;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CabinetController {

    @Autowired
    User currentUser;

    @Autowired
    RestTemplate restTemplate;

  // @RequestMapping(value = "/getter", method = RequestMethod.GET, params = {"name"})
  // public String method(@RequestParam("name") String name) {
  //     String forObject = restTemplate.getForObject("http://localhost:8080/get_all_goods", String.class);
  //     return name + " " + forObject;
  // }


    @RequestMapping(value = "/set_user")
    public String setUser(Integer id, String login, String password){
        currentUser = new User(id, login, password);
        return "My Cabinet. Login: " + currentUser.getLogin();
    }

    @RequestMapping(value = "/get_user")
    public User getUser(){
          return currentUser;
    }


    @RequestMapping(value = "/add_to_cart_by_id")
    public String addToCartById(Integer id){
        if(currentUser!= null) {
            String str = restTemplate.getForObject("http://localhost:8080/get_all_goods_string", String.class);

            List<Good> goods = new ArrayList<>();
            String[] infoOfGood = str.split("-");
            Good good = null;
            for (int i = 0; i < infoOfGood.length; i++) {
                String[] goodStr = infoOfGood[i].split("&");
                good = new Good(Integer.parseInt(goodStr[0]), goodStr[1], Integer.parseInt(goodStr[2]));
                goods.add(good);
            }

            for (int i = 0; i < goods.size(); i++) {
                if(goods.get(i).getId() ==id){
                    good = goods.get(i);
                }
            }
           // good = ;
            currentUser.addToCart(good);

            return "Good " + good.getName() + " add to cart of " + currentUser.getLogin();
        }
        return "Please, registration or log in";
    }


    @RequestMapping(value = "/show_cart")
    public List<Good> showCart(){
        if(currentUser != null)
            return currentUser.getCart();
        else
            return null;
    }


    @RequestMapping(value = "/buy_good_id")
    public String buyGoodInCart(Integer id){
        if(currentUser != null) {
            try {
                String name = currentUser.findById(id).getName();
                if (currentUser.buyGoodById(id)) {
                    return name + " successfully buy from cart of user " + currentUser.getLogin();
                } else
                    return "Error buying. Good with this id not found";
            }
            catch (Exception e) {
                return "Error buying";
            }
        }
        return "Please, registration or log in";
    }

    @RequestMapping(value = "/delete_in_cart")
    public String deleteGoodInCart(Integer id){
        if(currentUser != null) {
            try{
                String name = currentUser.findById(id).getName();
                if(currentUser.deleteGoodById(id)) {
                    return name + " successfully delete from cart of user " + currentUser.getLogin();
                }
                else
                    return "Error deleting. Good with this id not found";
            }
            catch (Exception e){
                return "Error deleting";
            }
        }
        return "Please, registration or log in";
    }

    @RequestMapping(value = "/exit_cabinet")
    public String exitCabinet(){
        currentUser = null;
        return "Good bye!";
    }

}
