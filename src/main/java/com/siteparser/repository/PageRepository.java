package com.siteparser.repository;

import com.siteparser.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query(nativeQuery = true, value = "select * from page where project_id = :projectId limit :count offset :offset ")
    List<Page> getPagesByProjectIdWithOffset(@Param("projectId") long projectId, @Param("count") long count,
                                             @Param("offset") long offset);

    @Query("from Page p where p.url = :url and p.project.id = :projectId")
    Page findByUrl(@Param("url") String url, @Param("projectId") long projectId);

    @Query("from Page p where p.project.id = :projectId")
    List<Page> findAllByProjectId(@Param("projectId") long projectId);

    @Query("from Page p where p.project.id = :projectId and p.content = null")
    List<Page> findAllUnparsedPages(@Param("projectId") long projectId);

    @Query("from Page p where p.url = :url")
    Page findByUrl(@Param("url") String url);

    @Query("from Page p where p.project.id = :projectId and p.url like %:keyword%")
    List<Page> findKeywordsInUrl(@Param("projectId") long projectId, @Param("keyword") String keyword);

    @Query("from Page p where p.project.id = :projectId and p.title like %:keyword%")
    List<Page> findKeywordsInTitle(@Param("projectId") long projectId, @Param("keyword") String keyword);

    @Query("from Page p where p.project.id = :projectId and p.description like %:keyword%")
    List<Page> findKeywordsInDescription(@Param("projectId") long projectId, @Param("keyword") String keyword);

    @Query("from Page p where p.project.id = :projectId and p.content like %:keyword%")
    List<Page> findKeywordsInContent(@Param("projectId") long projectId, @Param("keyword") String keyword);

    @Query(nativeQuery = true, value = "delete from page where url like %:type and project_id = :projectId")
    @Modifying
    @Transactional
    void deleteAllByTypeInUrl(@Param("type") String type, @Param("projectId") long projectId);

}
