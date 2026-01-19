package com._01Blog.backend.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.query.Page;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
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
                u.id, u.user_name, u.first_name, u.last_name, u.bio, u.profile_image, u.role
                COALESCE(flwing.followingCount, 0) AS following,
                COALESCE(flwer.followerCount, 0) AS follower
                FROM user u
                LEFT JOIN (
                    SELECT follower_id, COUNT(*) AS followingCount
                    FROM subscription
                    GROUP BY follower_id
                ) flwing ON flwing.follower_id = u.id
                LEFT JOIN (
                    SELECT following_id, COUNT(*) AS followerCount
                    FROM subscription
                    GROUP BY following_id
                ) flwer ON flwer.following_id = u.id
                WHERE u.id = :id
            """, nativeQuery = true)
    Map<String, Object> findUserData(@Param("id") UUID id);

    @Query(value = """
                SELECT u FROM user u
                WHERE LOWER(u.user_name) LIKE LOWER(CONCAT('%', :name, '%'))
                OR LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :name, '%'))
                OR LOWER(CONCAT(u.last_name, ' ', u.first_name)) LIKE LOWER(CONCAT('%', :name, '%'))
            """, nativeQuery = true)
    List<User> searchUsers(@Param("name") String name);

    @Query(value = """
            SELECT * FROM user ORDER BY created_at OFFSET :offset LIMIT :limit
            """, nativeQuery = true)
    List<User> findAllUsers(@Param("offset") int offset, @Param("limit") int limit);

    @Query(value = """
            UPDATE user SET enabled = :isEnabled WHERE id = :userId
            """, nativeQuery = true)
    void updateEnabledUser(@Param("userId") UUID userId, @Param("isEnabled") boolean isEnabled);

}
