package org.example.ver1.Member;

import java.util.List;

public interface MemberDao {

    boolean insertMember(Member member);

    boolean deleteMember(Member member);

    boolean updateMember(Member member);
//
    List<Member> fetchMembers();
//
    Member fetchSpecificMember(int id);
}
