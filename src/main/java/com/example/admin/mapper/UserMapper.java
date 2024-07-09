package com.example.admin.mapper;
import com.example.admin.entity.User.*;
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
    InvitationCode findCode(String code);
    void insertlogin(Login login);
    List<Integer> getlogin(Login login);
    List<Login> getallLogin();
    void updatealive(int loginid, Long alive_timestamp);
    User getLatestUser();
    int getUseridByloginid(int loginid);
    int getUserLatestloginid(int userid);
    List<Login> getUserVisitbyDate(Long startDate, Long endDate);
    List<User> getNewUserbyDate(String startday, String endday);

    void setLastLogin(int userid, long currentTimestamp);

    List<User> getSurvived();

    void setdelete(int userid);
}
