package com.example.admin.controller;

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

    @RequestMapping(value="/generateCodeID", method= RequestMethod.GET)
    public String generateCodeID() {
        return invitationCodeService.generateCodeID();
    }

    @RequestMapping(value="/getScheduledDay", method= RequestMethod.GET)
    public List<scheduled_invitation> getScheduledDay() {
        return invitationCodeService.getScheduledDay();
    }

    @RequestMapping(value="/getScheduledHour", method= RequestMethod.GET)
    public List<scheduled_invitation> getScheduledHour() {
        return invitationCodeService.getScheduledHour();
    }

    @RequestMapping(value="/createInvitation", method= RequestMethod.POST)
    public void createInvitation(@RequestBody InvitationCode invitationCode) {
        invitationCodeService.createInvitation(invitationCode);
    }

}
