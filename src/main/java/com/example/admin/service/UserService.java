package com.example.admin.service;
// import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> findAll();
    List<User> findbyNamePage(int limit, int page, String name);
    User findById(int id);
    Login verifylogin(String code);
    Login templogin(String code);
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    int alive(int loginid, String alive_timestamp);
    String getLanguage() throws IOException;
    List<DayUserVisit> getUserbyDate(Long startDate, Long endDate);
    int getVisitbyDate(Long startDate, Long endDate);
    int getNewUserbyDate(String startday, String endday);

}

