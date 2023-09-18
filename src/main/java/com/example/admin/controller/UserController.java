// UserController.java
package com.example.admin.controller;

import com.example.admin.entity.User.*;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public Login login(String code) {
        return userService.verifylogin(code);
    }


    @RequestMapping(value="/findAll", method= RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value="/findbyNamePage", method= RequestMethod.GET)
    public List<User> findbyNamePage(int limit, int page, String name) {
        return userService.findbyNamePage(limit, page, name);
    }

    @RequestMapping(value="/findById/{id}", method= RequestMethod.GET)
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @RequestMapping(value="/createUser", method= RequestMethod.POST)
    public void createUser(@RequestBody User user) {

        userService.createUser(user);
    }

    @RequestMapping(value="/updateUser", method= RequestMethod.PUT)
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @RequestMapping(value="/deleteUser", method= RequestMethod.DELETE)
    public void deleteUser(int id) {
        userService.deleteUser(id);
    }

}