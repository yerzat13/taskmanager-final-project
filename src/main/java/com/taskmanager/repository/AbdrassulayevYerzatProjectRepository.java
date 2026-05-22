package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatProject;
import com.taskmanager.entity.AbdrassulayevYerzatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbdrassulayevYerzatProjectRepository extends JpaRepository<AbdrassulayevYerzatProject, Long> {

    List<AbdrassulayevYerzatProject> findByOwner(AbdrassulayevYerzatUser owner);

    Page<AbdrassulayevYerzatProject> findByOwner(AbdrassulayevYerzatUser owner, Pageable pageable);

    Optional<AbdrassulayevYerzatProject> findByIdAndOwner(Long id, AbdrassulayevYerzatUser owner);

    @Query("SELECT p FROM AbdrassulayevYerzatProject p WHERE p.owner = :owner AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AbdrassulayevYerzatProject> searchByOwnerAndKeyword(@Param("owner") AbdrassulayevYerzatUser owner,
                                                             @Param("search") String search,
                                                             Pageable pageable);
}