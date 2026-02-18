package com.lanai.lanjourney.dto;

import java.time.OffsetDateTime;

public class UserResponse {

    public Long id;
    public String displayName;
    public String email;
    public boolean emailVerified;
    public String authProvider;
    public String authProviderUserId;
    public String avatarUrl;
    public String locale;
    public OffsetDateTime createdAt;
}
