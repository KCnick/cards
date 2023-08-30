package com.nick.cards.domain.security.repositories;

import com.nick.cards.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String emailAddress);

    boolean existsByEmail(String email);

    Optional<User> findByGuid(UUID guid);


}