/*
 * Copyright (c) 2020.
 * All Rights Reserved
 * Kyle Newton
 */

package com.kylenewton.StreamPromoter;

import com.kylenewton.StreamPromoter.Integration.Twitch.TwitchIntegration;
import com.kylenewton.StreamPromoter.Objects.ERole;
import com.kylenewton.StreamPromoter.Objects.Role;
import com.kylenewton.StreamPromoter.Objects.User;
import com.kylenewton.StreamPromoter.Repository.IRoleRepository;
import com.kylenewton.StreamPromoter.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StreamersOfColor {

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	IUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	TwitchIntegration twitchIntegration;

	@Value("${soc.auth.new_run")
	String first;

	boolean firstRun = Boolean.getBoolean(first);

	public static void main(String[] args) {
		SpringApplication.run(StreamersOfColor.class, args);
	}

	/**
	 * Sets up roles in database if database does not exist
	 */
	@Bean
	public void setupRoles() {
		if(firstRun){
			roleRepository.deleteAll();
			roleRepository.save(new Role(ERole.ROLE_USER));
			roleRepository.save(new Role(ERole.ROLE_MODERATOR));
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
		}
	}

	/**
	 * Sets up BURN admin account if necessary
	 */
	@Bean
	public void setupAccounts() {
		if(firstRun){
			userRepository.deleteAll();
			User user = new User("BURN447", passwordEncoder.encode("${auth.burn.password}"));
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found")));
			roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role not found")));
			roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role not found")));

			user.setRoles(roles);
			userRepository.save(user);
		}
	}

	@Bean
	public TwitchIntegration getTwitchIntegration() {
		return new TwitchIntegration();
	}

}
