package com.streamroom.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String displayName;
    private String bio;
    private String profileImage;
    private String bannerImage;
    private String twitchUsername;
    private String twitchUserId;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(nullable = false)
    private String passwordHash;

    private String tagline;
    private String twitchUrl;
    private String discordUrl;
    private String twitterUrl;

    @Column(columnDefinition = "TEXT")
    private String schedule;

    private Integer followerCount;
    private Integer streamCount;
    private Integer hoursStreamed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getBannerImage() { return bannerImage; }
    public void setBannerImage(String bannerImage) { this.bannerImage = bannerImage; }

    public String getTwitchUsername() { return twitchUsername; }
    public void setTwitchUsername(String twitchUsername) { this.twitchUsername = twitchUsername; }

    public String getTwitchUserId() { return twitchUserId; }
    public void setTwitchUserId(String twitchUserId) { this.twitchUserId = twitchUserId; }

    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getTwitchUrl() { return twitchUrl; }
    public void setTwitchUrl(String twitchUrl) { this.twitchUrl = twitchUrl; }

    public String getDiscordUrl() { return discordUrl; }
    public void setDiscordUrl(String discordUrl) { this.discordUrl = discordUrl; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public Integer getFollowerCount() { return followerCount; }
    public void setFollowerCount(Integer followerCount) { this.followerCount = followerCount; }

    public Integer getStreamCount() { return streamCount; }
    public void setStreamCount(Integer streamCount) { this.streamCount = streamCount; }

    public Integer getHoursStreamed() { return hoursStreamed; }
    public void setHoursStreamed(Integer hoursStreamed) { this.hoursStreamed = hoursStreamed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
