package com.rockhardy.lovable.Enum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Set;

import static com.rockhardy.lovable.Enum.ProjectPermission.*;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(EDIT,VIEW,DELETE,VIEW_MEMBERS),
    VIEWER(Set.of(VIEW,VIEW_MEMBERS)),
    OWNER(Set.of(MANAGE_MEMBERS,DELETE,VIEW,EDIT,VIEW_MEMBERS));


    ProjectRole(ProjectPermission... permissions ){
    this.permissions=Set.of(permissions);
    }
    private final Set<ProjectPermission> permissions;
}
