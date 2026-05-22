package com.taskmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbdrassulayevYerzatAuthResponse {

    private String accessToken;
    private String tokenType;
    private String username;
    private String email;
    private String role;
}