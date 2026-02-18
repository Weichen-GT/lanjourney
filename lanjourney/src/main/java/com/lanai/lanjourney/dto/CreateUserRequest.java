package com.lanai.lanjourney.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    public String displayName;
    public String email;
    public boolean emailVerified;

    @NotBlank
    public String authProvider;        // "google"
    @NotBlank
    public String authProviderUserId;  // google sub

    public String avatarUrl;
    public String locale;
}
