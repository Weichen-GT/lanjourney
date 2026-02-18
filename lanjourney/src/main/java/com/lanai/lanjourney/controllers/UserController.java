package com.lanai.lanjourney.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lanai.lanjourney.dto.CreateUserRequest;
import com.lanai.lanjourney.dto.UserResponse;
import com.lanai.lanjourney.entity.AppUser;
import com.lanai.lanjourney.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        AppUser u = userService.createUser(req);
        UserResponse r = new UserResponse();
        r.id = u.getId();
        r.displayName = u.getDisplayName();
        r.email = u.getEmail();
        r.emailVerified = u.isEmailVerified();
        r.authProvider = u.getAuthProvider();
        r.authProviderUserId = u.getAuthProviderUserId();
        r.avatarUrl = u.getAvatarUrl();
        r.locale = u.getLocale();
        r.createdAt = u.getCreatedAt();
        return r;
    }
}
