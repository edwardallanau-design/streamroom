package com.streamroom.service;

import com.streamroom.config.TwitchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitchService {

    private final TwitchProperties twitchProperties;
    private final WebClient webClient;

    public Map<String, Object> getStreamInfo(String channelName) {
        try {
            log.info("Fetching stream info for channel: {}", channelName);
            // TODO: implement actual Twitch Helix API call using twitchProperties.getClientId()
            Map<String, Object> streamInfo = new HashMap<>();
            streamInfo.put("channel", channelName);
            streamInfo.put("isLive", false);
            streamInfo.put("viewers", 0);
            return streamInfo;
        } catch (Exception e) {
            log.error("Error fetching Twitch stream info for channel {}: {}", channelName, e.getMessage());
            throw new RuntimeException("Failed to fetch stream info from Twitch", e);
        }
    }

    public Map<String, Object> getUserInfo(String username) {
        try {
            log.info("Fetching Twitch user info for: {}", username);
            // TODO: implement actual Twitch Helix /users API call
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("login", username);
            userInfo.put("displayName", username);
            return userInfo;
        } catch (Exception e) {
            log.error("Error fetching Twitch user info for {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to fetch user info from Twitch", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            log.info("Validating Twitch token");
            // TODO: implement actual token validation via Twitch /validate endpoint
            return token != null && !token.isBlank();
        } catch (Exception e) {
            log.error("Error validating Twitch token: {}", e.getMessage());
            return false;
        }
    }
}
