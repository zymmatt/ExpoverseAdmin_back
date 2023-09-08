package com.example.admin.mapper;
import com.example.admin.entity.User.InvitationCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvitationCodeMapper {
    void insertInvitationCode(InvitationCode invitationCode);
    List<InvitationCode> findCodebyId(String codeId);
    List<InvitationCode> getAllInvitation();
}
