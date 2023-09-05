// UserController.java
package com.example.admin.controller;
import com.example.admin.entity.User;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/findAll", method= RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value="/findbyNamePage", method= RequestMethod.GET)
    public List<User> findbyNamePage(int limit, int page, String name) {
        return userService.findbyNamePage(limit, page, name);
    }

    @RequestMapping(value="/findById/{id}", method= RequestMethod.GET)
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @RequestMapping(value="/createUser", method= RequestMethod.POST)
    public void createUser(@RequestBody User user) {
        //System.out.println(user.getName());
        //System.out.println(user.getCompany());
        //System.out.println(user.getJob());
        //System.out.println(user.getPhone());
        userService.createUser(user);
    }

    @RequestMapping(value="/updateUser", method= RequestMethod.PUT)
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @RequestMapping(value="/deleteUser", method= RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}