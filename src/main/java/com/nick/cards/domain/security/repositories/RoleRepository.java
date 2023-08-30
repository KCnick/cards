package com.nick.cards.domain.security.repositories;

import com.nick.cards.entities.Role;
import com.nick.cards.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}