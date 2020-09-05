/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamersOfColor.Controllers;

import com.kylenewton.StreamersOfColor.Annotation.CurrentUser;
import com.kylenewton.StreamersOfColor.Objects.ERole;
import com.kylenewton.StreamersOfColor.Objects.Role;
import com.kylenewton.StreamersOfColor.Objects.User;
import com.kylenewton.StreamersOfColor.Repository.IUserRepository;
import com.kylenewton.StreamersOfColor.Repository.IRoleRepository;
import com.kylenewton.StreamersOfColor.Request.LoginRequest;
import com.kylenewton.StreamersOfColor.Request.SignupRequest;
import com.kylenewton.StreamersOfColor.Response.ApiResponse;
import com.kylenewton.StreamersOfColor.Response.JwtResponse;
import com.kylenewton.StreamersOfColor.Security.TokenProvider;
import com.kylenewton.StreamersOfColor.Security.UserDetailsImpl;
import com.kylenewton.StreamersOfColor.Util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kylenewton.StreamersOfColor.Util.References.CROSS_ORIGIN_URL;
import static com.kylenewton.StreamersOfColor.Util.Utils.print;

/**
 * Authentication Controller
 * Handles Moderation Account Setup and Login
 * Currently the signup route is restricted to moderators and admins, and will likley be changed to just admins
 */
@CrossOrigin(origins = CROSS_ORIGIN_URL)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * Logs in a given user
     * @param loginRequest  Request containing username/password/other info
     * @return JWTResponse that contains the user token
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    }

    /**
     * Signs up a new user account
     * @param signupRequest Request containing username/password/roles
     * @return  APIResponse with success/failure
     */
    @PostMapping("/signup")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error: Username Already Taken"));
        }

        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        print(strRoles);

        if(strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch(role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User Registered Successfully"));
    }

    /**
     * Gets current user
     * @param userPrincipal Logged in user
     * @return  Logged in user object or a Resource not found exception
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetailsImpl userPrincipal) {
        try {
            return userRepository.findByUsername(userPrincipal.getUsername());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new ResourceNotFoundException("User not found");
        }
    }
}
