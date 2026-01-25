package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.Enum.ProjectRole;
import com.rockhardy.lovable.entity.ProjectMember;
import com.rockhardy.lovable.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByProjectId(Long projectId);
    @Query("""
            Select pm.projectRole from ProjectMember pm
            where pm.id.projectId=:projectId
            and pm.id.userId=:userId
            """)
    Optional<ProjectRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId,
                                                     @Param("userId") Long userId);
    @Query("""
            Select count(pm) from ProjectMember pm
            where pm.id.userId=:userId and
            pm.role='OWNER'
            """)
    int countProjectOwnedByUser(@Param("user_id")Long userid);
}
