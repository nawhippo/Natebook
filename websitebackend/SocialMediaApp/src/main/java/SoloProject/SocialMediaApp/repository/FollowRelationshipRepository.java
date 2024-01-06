package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.FollowRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {

    @Query(value = "SELECT COUNT(f) FROM FollowRelationship as f WHERE f.followerId = :appUserID")
    int getFollowerCountById(@Param("appUserID") Long appUserID);

    List<FollowRelationship> getFollowRelationshipByFollowerId(Long appUserID);
}
