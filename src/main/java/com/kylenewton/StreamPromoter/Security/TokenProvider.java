/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Security;

import com.kylenewton.StreamPromoter.Config.AppProperties;
import com.kylenewton.StreamPromoter.Util.JwtUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Manages JWT Token
 */
@Service
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Creates a token
     * @param authentication    Authentication object to get current user
     * @return  String of the JWT token
     */
    public String createToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtUtils.jwtExpirationMs);

        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(new Date()).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtUtils.jwtSecret).compact();
    }

    /**
     * Validates an Authenication Token
     * @param authToken Token to authenticate
     * @return  valid or not valid
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtUtils.jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
