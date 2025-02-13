//package com.example.Library.model;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class UserPrincipal implements UserDetails {
//    private Optional<User> user;
//
//    public UserPrincipal(Optional<User> user) {
//        this.user = user;
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }
//
//    @Override
//    public String getPassword() {
//        return user.map(User::getPassword)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    @Override
//    public String getUsername() {
//        return user.map(User::getUsername)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return user.get().isEnabled(); // Add a field in your User entity
//    }
//
//    public Optional<User> getUser() {
//        return user;
//    }
//}
