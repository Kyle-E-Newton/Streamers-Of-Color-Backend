/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter.Security;

import com.kylenewton.StreamPromoter.Objects.User;
import com.kylenewton.StreamPromoter.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom implementation of the User Details Service
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    IUserRepository userRepository;

    /**
     * Gets a user details object from a username
     * @param username  Username to find
     * @return  UserDetails user
     * @throws UsernameNotFoundException    User does not exist in the userRepository
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            return UserDetailsImpl.build(user);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(String.format("Username %s was not found", username));
        }
    }
}
