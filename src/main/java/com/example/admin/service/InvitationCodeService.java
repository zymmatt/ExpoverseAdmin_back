package com.example.admin.service;
import com.example.admin.entity.User.InvitationCode;
import com.example.admin.entity.User.scheduled_invitation;
import java.util.List;

public interface InvitationCodeService {
    String generateCodeID();
    List<scheduled_invitation> getScheduledDay();
    List<scheduled_invitation> getScheduledHour();
    List<InvitationCode> getAllInvitation();
    void createInvitation(InvitationCode invitation);
    void deleteInvitationCode(String code);
}

