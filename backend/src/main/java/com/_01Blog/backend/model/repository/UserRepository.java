package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._01Blog.backend.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email); // Query Derivation Rules : find : select, By : where, Email : value

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    @Query(value = """
            SELECT
                u.id, u.userName, u.firstName, u.lastName, u.bio, u.profileImage, u.role
                COALESCE(flwing.followingCount, 0) AS following,
                COALESCE(flwer.followerCount, 0) AS follower
                FROM User u
                LEFT JOIN (
                    SELECT followerId, COUNT(*) AS followingCount
                    FROM Subscription
                    GROUP BY followerId
                ) flwing ON flwing.followerId = u.id
                LEFT JOIN (
                    SELECT followingId, COUNT(*) AS followerCount
                    FROM Subscription
                    GROUP BY followingId
                ) flwer ON flwer.followingId = u.id
                WHERE u.id = :id
            """)
    Map<String, Object> findUserData(@Param("id") UUID id);

    @Query(value = """
            SELECT u FROM User AS user
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(CONCAT(u.firstName, " ", u.lastName)) LIKE LOWER (CONCAT('%', :name, '%'))
            OR LOWER(CONCAT(u.lastName, " ", u.firstName)) LIKE LOWER (CONCAT('%', :name, '%'))
            """)
    List<User> searchUsers(@Param("name") String name);

}
