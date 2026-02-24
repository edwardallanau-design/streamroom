package com.streamroom.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String displayName;
    private String bio;
    private String profileImage;
    private String bannerImage;
    private String twitchUsername;
}
