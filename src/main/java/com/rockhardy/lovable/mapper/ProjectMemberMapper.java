package com.rockhardy.lovable.mapper;

import com.rockhardy.lovable.dto.member.MemberResponse;
import com.rockhardy.lovable.entity.ProjectMember;
import com.rockhardy.lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "userId",source="id")
    @Mapping(target="projectRole",constant = "OWNER")
    MemberResponse toMemberResponse(User owner);
    @Mapping(target="userId" ,source="user.id")
    @Mapping(target ="username",source="user.username")
    @Mapping(target="name",source="user.name")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
}
