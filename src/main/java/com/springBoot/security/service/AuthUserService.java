package com.springBoot.security.service;

import com.springBoot.security.model.AuthRole;
import com.springBoot.security.model.AuthUser;
import com.springBoot.security.repository.AuthRoleRepository;
import com.springBoot.security.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final AuthUserRepository userRepository;
    private final AuthRoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AuthUser user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    public AuthUser saveUser(AuthUser user) {
        return userRepository.save(user);
    }

    public AuthUser registerUser(AuthUser user) {
        AuthRole role = roleRepository.findByRole("ROLE_USER");
        if (role != null) {
            List<AuthRole> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            return userRepository.save(user);
        }
        return null;
    }
}