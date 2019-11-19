package com.siteparser.repository;

import com.siteparser.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("from Project p where p.domain = :domain")
    List<Project> findAllByDomain(@Param("domain") String domain);

    @Query("from Project p where p.parsingStatus = true")
    List<Project> findAllWithEnableParsing();

    @Query("from Project p where p.domain = :domain")
    Project findByDomain(@Param("domain") String domain);

    @Query("from Project p where p.user.id = :user_id")
    List<Project> getAllByUserId(@Param("user_id") long userId);
}
