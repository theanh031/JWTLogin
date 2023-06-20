package com.example.apitoregisterphone.repositories;

import com.example.apitoregisterphone.models.tokens.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface tokenRepo extends JpaRepository<Token, Integer> {

    @Query(value = """
      select * from token as t inner join user u on t.user_id = u.id where u.id = 1 and (t.expired = false or t.revoked = false);
      """, nativeQuery = true)
    List<Token> findAllValidTokenByUser(Integer id);
    Optional<Token> findByToken(String token);
}
