package com.lanai.lanjourney.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lanai.lanjourney.dto.CreateUserRequest;
import com.lanai.lanjourney.entity.AppUser;
import com.lanai.lanjourney.repository.AppUserRepository;

@Service
public class UserService {

    private final AppUserRepository userRepo;

    public UserService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public AppUser createUser(CreateUserRequest req) {
        AppUser u = new AppUser();
        u.setDisplayName(req.displayName);
        u.setEmail(req.email);
        u.setEmailVerified(req.emailVerified);
        u.setAuthProvider(req.authProvider);
        u.setAuthProviderUserId(req.authProviderUserId);
        u.setAvatarUrl(req.avatarUrl);
        u.setLocale(req.locale);

        try {
            return userRepo.save(u);
        } catch (DataIntegrityViolationException e) {
            // could be email unique or (provider, provider_user_id) unique
            throw new IllegalArgumentException("User already exists (email or provider identity is not unique).");
        }
    }
}
