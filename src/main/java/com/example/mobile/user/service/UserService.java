package com.example.mobile.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mobile.user.entity.UserEntity;
import com.example.mobile.user.model.UserModel;
import com.example.mobile.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    // 
    @Autowired // used for dependency injection
    private UserRepository userRepository;
    // get all users 
    public Page<UserModel> getAllUsers(@org.springframework.lang.NonNull Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(entity -> new UserModel(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getRoles(),
            entity.isEnabled()
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles() != null ? userEntity.getRoles().split(",") : new String[]{})
                .disabled(!userEntity.isEnabled())
                .build();
    }
}
