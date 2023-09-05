package com.example.admin.service;
// import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    List<User> findbyNamePage(int limit, int page, String name);
    User findById(Long id);
    void createUser(User user);
    void updateUser(Long id, User user);
    void deleteUser(Long id);
}

