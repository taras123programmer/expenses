package com.expenses.services;

import com.expenses.entities.User;
import com.expenses.entities.UserDetailsImpl;
import com.expenses.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to load user" );
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getBalance().floatValue());
    }

    public void updateBalance(int userId, BigDecimal number){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        user.setBalance(user.getBalance().add(number));
        userRepository.save(user);
    }

    public BigDecimal getBalance(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        return user.getBalance();
    }

}
