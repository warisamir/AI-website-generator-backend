package com.rockhardy.lovable.security;

import com.rockhardy.lovable.Enum.ProjectPermission;
import com.rockhardy.lovable.Enum.ProjectRole;
import com.rockhardy.lovable.repository.ProjectMemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("security")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class SecurityExpression {
     ProjectMemberRepository projectMemberRepository;
     AuthUtils authUtils;
     public boolean hasPermission(Long projectId,ProjectPermission projectPermission){
         Long userId= authUtils.getCurrentUserId();
         return projectMemberRepository.findRoleByProjectIdAndUserId(projectId,userId)
                 .map(projectRole ->projectRole.getPermissions()
                         .contains(projectPermission)).orElse(false);
     }
     public Boolean canViewProject(Long projectId){
         return hasPermission(projectId,ProjectPermission.VIEW);
     }
    public Boolean canEditProject(Long projectId){
         return hasPermission(projectId,ProjectPermission.EDIT);
    }
    public boolean canDeleteProject(Long projectId){
         return hasPermission(projectId,ProjectPermission.DELETE);
    }
    public boolean canViewMembers(Long projectId){
        return hasPermission(projectId,ProjectPermission.VIEW);
    }
    public boolean canEditMembers(Long projectId){ return hasPermission(projectId,ProjectPermission.EDIT);}
    public boolean canManageMembers(Long projectId){
        return hasPermission(projectId,ProjectPermission.DELETE);
    }
}
