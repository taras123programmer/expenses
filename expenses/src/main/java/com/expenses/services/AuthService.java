package com.expenses.services;

import com.expenses.entities.User;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(String username, String password) {
        if(userRepository.findByUsername(username).isEmpty()){
            User user = new User(username, passwordEncoder.encode(password), "USER");
            userRepository.save(user);
            return true;
        }
        else{
            return false;
        }
    }

//    public int getCurrentUserId(){
//        Authentication auth = SecurityContextHolder
//                .getContext()
//                .getAuthentication();
//        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
//
//        Integer userId = user.getId();
//    }

}


