package com.example.admin.service;
import com.example.admin.entity.InvitationCode;
import com.example.admin.entity.scheduled_invitation;
import java.util.List;

public interface InvitationCodeService {
    String generateCodeID();
    List<scheduled_invitation> getScheduledDay();
    void createInvitation(InvitationCode invitation);
    List<String> getAllCodeID();
}

