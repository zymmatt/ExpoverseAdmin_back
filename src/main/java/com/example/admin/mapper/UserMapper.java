package com.example.admin.mapper;
import com.example.admin.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    List<User> findAll();
    List<User> findbyNamePage(int limit, int offset, String name);
    User findById(Long id);
    void insert(User user);
    void update(User user);
    void delete(Long id);
}
