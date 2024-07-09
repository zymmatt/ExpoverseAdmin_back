package com.example.admin.controller;

import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.entity.User.InvitationCode;
import com.example.admin.service.InvitationCodeService;
import com.example.admin.utils.datetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    // 管理员后台  获得所有的邀请记录(60天没有登录的邀请记录不显示)
    @RequestMapping(value="/getAllInvitation", method= RequestMethod.GET)
    public ResponseObject getAllInvitation() {
        List<InvitationCode> invitationCodeList = invitationCodeService.getAllInvitation();
        for (InvitationCode temp:invitationCodeList){
            long ts = temp.getLast_login();
            if (ts==0){
                temp.setLast_login_str("未參觀");
            } else {
                temp.setLast_login_str(datetime.timestamp2str(ts));
            }
        }
        return ResponseObject.success(invitationCodeList);
    }

    // 管理员后台  删除邀请记录
    @RequestMapping(value="/deleteInvitationCode", method= RequestMethod.DELETE)
    public ResponseObject deleteInvitationCode(String code) {
        invitationCodeService.deleteInvitationCode(code);
        return ResponseObject.success("删除邀请记录成功");
    }


}
