package com.example.admin.mapper;
import com.example.admin.entity.User.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();
    List<User> findbyNamePage(int limit, int offset, String name);
    User findById(int id);
    String findNamebyId(int id);
    void insert(User user);
    void update(User user);
    void delete(int id);
}
