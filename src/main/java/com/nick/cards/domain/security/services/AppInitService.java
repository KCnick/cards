package com.nick.cards.domain.security.services;

import com.nick.cards.domain.security.repositories.RoleRepository;
import com.nick.cards.entities.Role;
import com.nick.cards.enums.ERole;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class AppInitService {
    private final RoleRepository roleRepository;

    public AppInitService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void setup(){
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_MEMBER);
        if(role.isEmpty()){
            roleRepository.save(Role.builder()
                            .name(ERole.ROLE_MEMBER)
                    .build());
            roleRepository.save(Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build());
        }
    }
}
