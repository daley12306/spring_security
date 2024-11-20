package vn.hcmute.demo3_security.Services.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.hcmute.demo3_security.entity.Role;
import vn.hcmute.demo3_security.entity.Users;
import vn.hcmute.demo3_security.reponsitory.UserRepository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserDetailsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername (String usernameOrEmail) throws UsernameNotFoundException {
        Users user = userRepository.findByUsernameOrEmail (usernameOrEmail, usernameOrEmail) .orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new User (user.getEmail(), user.getPassword(), mapRolesToAuthorities (user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities (Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
