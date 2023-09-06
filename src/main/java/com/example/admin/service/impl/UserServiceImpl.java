// UserServiceImpl.java
package com.example.admin.service.impl;

import com.example.admin.entity.User;
import com.example.admin.service.UserService;
import com.example.admin.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        List<User>users = userMapper.findAll();
        return users;
    }

    @Override
    public List<User> findbyNamePage(int limit, int page, String name) {

        return userMapper.findbyNamePage(limit, 20*page-20, name);
    }

    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }


    @Override
    public void createUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void updateUser(User user) {
            userMapper.update(user);
    }


    @Override
    public void deleteUser(int id) {
        System.out.println("delete user"+id);
        userMapper.delete(id);
    }

}