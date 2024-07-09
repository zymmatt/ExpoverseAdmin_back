package com.example.admin.mapper;
import com.example.admin.entity.User.InvitationCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvitationCodeMapper {
    void insertInvitationCode(InvitationCode invitationCode);
    List<InvitationCode> getLatestCodeForEach();
    List<InvitationCode> findCodebyId(String codeId);
    List<InvitationCode> getAllInvitation();
    void deleteInvitationCode(String code);
    void setLastLogin(int id, long currentTimestamp);

    void setdelete(int userid);
}
