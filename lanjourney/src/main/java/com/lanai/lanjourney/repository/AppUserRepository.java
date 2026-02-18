package com.lanai.lanjourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lanai.lanjourney.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByAuthProviderAndAuthProviderUserId(String authProvider, String authProviderUserId);
}
