package com.rockhardy.lovable.repository;

import com.rockhardy.lovable.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query(""" 
            Select p from Project p
            where p.deletedAt IS NULL
            AND EXISTS (
            Select 1 from ProjectMember pm
            where pm.id.userId=:userId
            AND pm.id.userId=p.id
            )
            ORDER BY p.updatedAt DESC
            """)
    List<Project>   findAllAccessibleByUser(@Param("userId") Long userId);

    @Query("""
            Select p from Project p
            where p.id = :projectId
            and p.deletedAt is null
            and exists(
            select 1 from ProjectMember pm
            where pm.id.userId =: userId
            and pm.id.projectId=:projectId  
            )
            """)
    Optional<Project> findAccessibleProjectById(@Param("projectId") Long projectId, @Param("userId")Long userId);
}
