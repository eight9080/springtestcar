package com.dg.security.domain.repositories;

import com.dg.security.domain.entities.AutoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoUserRepository extends JpaRepository<AutoUser, Long> {

    AutoUser findByUsername(String username);

}
