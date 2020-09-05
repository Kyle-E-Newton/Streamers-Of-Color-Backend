/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Integration.Twitch;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.kylenewton.StreamersOfColor.Util.References.TWITCH_CALLBACK_URL;
import static com.kylenewton.StreamersOfColor.Util.References.TWITCH_SUBSCRIBE_STREAM_TOPIC_URL;

/**
 * Twitch Integration
 * Handles getting and managing Twitch tokens
 * Will handle webhook subscriptions
 */
@Component
public class TwitchIntegration {

    private static final String TWITCH_OAUTH_URL = "${soc.url.TWITCH_OAUTH_URL}";
    private static final String TWITCH_WEBHOOK_URL = "${soc.url.TWITCH_WEBHOOK_URL}";

    private static final String TWITCH_USER_URL = "${soc.url.TWITCH_USER_URL}";

    @Value(value = "${twitch.client.client-id}")
    private String clientId;

    @Value("${twitch.client.client-secret}")
    private String clientSecret;

    private OauthToken oauthToken;

    @Value("${twitch.webhook.secret}")
    private String SECRET;

    Logger logger = LoggerFactory.getLogger(TwitchIntegration.class);

    /**
     * Gets an Oauth token from Twitch to use in authentication
     * @return  New Oauth Token
     */
    private OauthToken getOauthToken() {
        HttpResponse<JsonNode> response = Unirest.post(TWITCH_OAUTH_URL).queryString("client_id", clientId).queryString("client_secret", clientSecret).queryString("grant_type", "client_credentials").asJson();
        return new OauthToken(response.getBody().getObject().getString("access_token"), response.getBody().getObject().getString("token_type"), LocalDateTime.now().plusSeconds(response.getBody().getObject().getInt("expires_in")));
    }

    /**
     * Check to see if the token does not exist or is going to expire
     * If so, refreshes the token using the getOauthToken method
     */
    public void refreshOauthToken() {
        if(oauthToken == null || oauthToken.getExpires_at().minusSeconds(10).isBefore(LocalDateTime.now())) {
            oauthToken = getOauthToken();
            logger.info("Refreshed Twitch OAuth Token");
        }
    }

    /**
     * Gets a user id from a username by querying the Twitch Database
     * @param username  Streamer Username
     * @return  String of the userid
     */
    public String getUserIdFromUsername(String username) {
        refreshOauthToken();
        HttpResponse<JsonNode> response = Unirest.get(TWITCH_USER_URL).header("Authorization", getAuthorizationHeader()).header("Client-ID", clientId).queryString("login", username).asJson();
        JSONObject obj = new JSONObject(response.getBody().getObject().toString());
        JSONArray array = obj.getJSONArray("data");
        return (String) array.getJSONObject(0).get("id");
    }

    /**
     * Formats an authorization header
     * @return  Formatted Authorization Header
     */
    private String getAuthorizationHeader() {
        return StringUtils.capitalize(oauthToken.getToken_type()) + " " + oauthToken.getAccess_token();
    }

    /**
     * Subscribes to a twitch webhook that will notify when a stream changes state
     * @param username Streamer Username
     */
    public void subscribeToWebhook(String username) {
        refreshOauthToken();
        JSONObject body = new JSONObject();
        body.append("hub.callback", TWITCH_CALLBACK_URL);
        body.append("hub.mode", "subscribe");
        body.append("hub.topic", TWITCH_SUBSCRIBE_STREAM_TOPIC_URL + "?user_id=" + getUserIdFromUsername(username));
        body.append("hub.lease_seconds", 864000);
        body.append("hub.secret", SECRET);
        //Subscribes to Event
        Unirest.post(TWITCH_WEBHOOK_URL).header("Authorization", getAuthorizationHeader()).header("Client-ID", clientId).body(body);
    }

    /**
     * Oauth Object that is returned by the getOauthToken method
     */
    private static class OauthToken {
        public String access_token;
        public String token_type;
        public LocalDateTime expires_at;

        public OauthToken(String access_token, String token_type, LocalDateTime expires_at) {
            this.access_token = access_token;
            this.token_type = token_type;
            this.expires_at = expires_at;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public LocalDateTime getExpires_at() {
            return expires_at;
        }

        public void setExpires_at(LocalDateTime expires_at) {
            this.expires_at = expires_at;
        }
    }
}
