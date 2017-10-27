package com.ssu.chernousovaya.controller;

import com.ssu.chernousovaya.model.Good;
import com.ssu.chernousovaya.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CabinetController {

   @Autowired
    List<User> currentsUsers;

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<User> getCurrentUsers(){
        return currentsUsers;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public String addUser(Integer id, String login, String password){

        User user = new User(id, login, password);
        //проверяем, есть ли такой в БД
        String str = restTemplate.postForObject("http://localhost:8083/users/string", user, String.class);
        String[] infoOfUser = str.split("-");

        for (int i = 0; i < infoOfUser.length; i++) {
            String[] userStr = infoOfUser[i].split("&");

            if(Integer.parseInt(userStr[0]) == id && userStr[1].equals(login) && userStr[2].equals(password)){
                currentsUsers.add(new User(id, login, password));
                return "Hello, " + login + " (Added user in current session)";
            }
        }
        return "Wrong data, this user not found in base";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        for (User u: currentsUsers) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/{userId}/cart/{goodId}")
    public String addToCartById(@PathVariable Integer goodId, @PathVariable Integer userId){
        User user = null;
        for (User u: currentsUsers) {
            if (u.getId() == userId) {
                user = u;
            }
        }
        if(user != null){
            String str = restTemplate.getForObject("http://localhost:8080/goods/string", String.class);

            List<Good> goods = new ArrayList<>();
            String[] infoOfGood = str.split("-");
            Good good = null;
            for (int i = 0; i < infoOfGood.length; i++) {
                String[] goodStr = infoOfGood[i].split("&");
                good = new Good(Integer.parseInt(goodStr[0]), goodStr[1], Integer.parseInt(goodStr[2]));
                goods.add(good);
            }

            for (int i = 0; i < goods.size(); i++) {
                if(goods.get(i).getId() == goodId){
                    good = goods.get(i);
                }
            }
            user.addToCart(good);
            currentsUsers.set(currentsUsers.indexOf(user), user);
            return "Good " + good.getName() + " add to cart of " + user.getLogin();
        }
        return "Please, registration or log in";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/cart")
    public List<Good> showCart(@PathVariable Integer userId){
        for (User u: currentsUsers) {
            if (u.getId() == userId) {
                return u.getCart();
            }
        }
        return null;
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/users/{userId}/cart/{goodId}")
    public String buyGoodInCart(@PathVariable Integer goodId, @PathVariable Integer userId){
        User user = null;
        for (User u: currentsUsers) {
            if (u.getId() == userId) {
                user = u;
            }
        }
        if(user!= null) {
            try {
                String name = user.findById(goodId).getName();
                if (user.buyGoodById(goodId)) {
                    currentsUsers.set(currentsUsers.indexOf(user), user);
                    return name + " successfully buy from cart of user " + user.getLogin();
                } else
                    return "Error buying. Good with this id not found";
            }
            catch (Exception e) {
                return "Error buying";
            }
        }
        return "Please, registration or log in";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}/cart/{goodId}")
    public String deleteGoodInCart(@PathVariable Integer goodId, @PathVariable Integer userId){
        User user = null;
        for (User u: currentsUsers) {
            if (u.getId() == userId) {
                user = u;
            }
        }
        if(user!= null) {
            try{
                String name = user.findById(goodId).getName();
                if(user.deleteGoodById(goodId)) {
                    currentsUsers.set(currentsUsers.indexOf(user), user);
                    return name + " successfully delete from cart of user " + user.getLogin();
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

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}")
    public String exitCabinet(@PathVariable Integer userId){
        User user = null;
        for (User u: currentsUsers) {
            if (u.getId() == userId) {
                user = u;
            }
        }
        if(user != null){
            currentsUsers.remove(user); //удалить пользователя из подключенных пользователей
        }
        return "Good bye!";
    }
}
