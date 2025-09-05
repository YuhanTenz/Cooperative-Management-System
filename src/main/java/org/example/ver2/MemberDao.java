package org.example.ver2;

import java.util.List;

public interface MemberDao {

    boolean insertMember(Member member);

    boolean deleteMember(Member member);

    boolean updateMember(Member member);

    List<Member> fetchMembers();

    List<Member> getAllMembers();

    Member fetchSpecificMember(int id);
}
