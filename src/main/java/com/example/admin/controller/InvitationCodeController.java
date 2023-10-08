package com.example.admin.controller;
import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.entity.User.scheduled_invitation;
import com.example.admin.entity.User.InvitationCode;
import com.example.admin.service.InvitationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/invitationCode")
public class InvitationCodeController {
    @Autowired
    private InvitationCodeService invitationCodeService;

    // 管理员后台 生成一个不重复的激活码
    @RequestMapping(value="/generateCodeID", method= RequestMethod.GET)
    public String generateCodeID() {
        return invitationCodeService.generateCodeID();
    }

    // 管理员后台  行事历获得月格式下每一天的预约数量
    @RequestMapping(value="/getScheduledDay", method= RequestMethod.GET)
    public ResponseObject getScheduledDay() {
        return ResponseObject.success(invitationCodeService.getScheduledDay());
    }

    // 管理员后台  行事历获得周格式下每一小时的预约数量
    @RequestMapping(value="/getScheduledHour", method= RequestMethod.GET)
    public ResponseObject getScheduledHour() {
        return ResponseObject.success(invitationCodeService.getScheduledHour());
    }

    // 管理员后台  生成一次邀请记录
    @RequestMapping(value="/createInvitation", method= RequestMethod.POST)
    public ResponseObject createInvitation(@RequestBody InvitationCode invitationCode) {
        invitationCodeService.createInvitation(invitationCode);
        return ResponseObject.success("生成邀请记录成功");
    }

    // 管理员后台  获得所有的邀请记录
    @RequestMapping(value="/getAllInvitation", method= RequestMethod.GET)
    public ResponseObject getAllInvitation() {
        return ResponseObject.success(invitationCodeService.getAllInvitation());
    }




}
