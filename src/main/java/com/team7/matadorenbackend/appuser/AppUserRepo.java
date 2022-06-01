package com.team7.matadorenbackend.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser,String> {


    Optional<AppUser> findByUsername(String username);

   Optional <?> deleteByUsername(String username);

   boolean existsByUsername(String username);

   @Query("SELECT a FROM AppUser a WHERE LOWER(a.username) LIKE LOWER(CONCAT('%',:keyword,'%'))" +
           " OR LOWER(a.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))" +
           "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%',:keyword,'%')) ")
    List<AppUser> search(@Param("keyword")String keyword);
}
