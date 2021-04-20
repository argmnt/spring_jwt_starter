package com.yagizgazibaba.jwt_boilerplate.user;

import com.yagizgazibaba.jwt_boilerplate.auth.LoginDTO;
import com.yagizgazibaba.jwt_boilerplate.security.JWTService;
import com.yagizgazibaba.jwt_boilerplate.security.JWTToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private User mockUser;

    public UserService(PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Creates mock new User(UserDetails) for testing purposes!!!
     * TODO: Delete it in production!!!
     * @return
     */
    @PostConstruct
    private void createMockUser() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        String password = this.passwordEncoder.encode("1234");
        mockUser = User.builder()
                .id(1L)
                .username("user")
                .grantedAuthorities(grantedAuthorities)
                .password(password).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mockUser;
    }

    /**
     * Creates JWT Token for users.
     * @param loginDTO
     * @return
     */
    public JWTToken createJWTToken(LoginDTO loginDTO) {
        Optional<UserDetails> optionalUserDetails = Optional.of(this.loadUserByUsername(loginDTO.getUsername()));
        UserDetails userDetails = optionalUserDetails.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User credentials are wrong"));
        if (passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            Map<String, String> claims = jwtService.getClaims(loginDTO, userDetails);
            String jwt = jwtService.createJwtForClaims(loginDTO.getUsername(), claims);
            JWTToken jwtToken = new JWTToken();
            jwtToken.setToken(jwt);
            return jwtToken;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User credentials are wrong");
    }

    public <T> Optional<T> getClaim(JwtAuthenticationToken jwtAuthenticationToken, String key, Class<T> clazz) {
        return jwtService.getClaim(jwtAuthenticationToken, key, clazz);
    }

}
