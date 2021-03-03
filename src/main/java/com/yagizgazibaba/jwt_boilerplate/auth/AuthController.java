package com.yagizgazibaba.jwt_boilerplate.auth;

import com.yagizgazibaba.jwt_boilerplate.security.JWTToken;
import com.yagizgazibaba.jwt_boilerplate.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(LoginDTO loginDTO) {
        JWTToken jwtToken = userService.createJWTToken(loginDTO);
        return ResponseEntity.ok(jwtToken);
    }

    /**
     * Warning: Method is build for testing purposes of token and user credentials!
     * TODO: Delete it in production!
     * @param authentication
     * @return
     */
    @GetMapping("user")
    public UserDetails getUser(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> attributes = token.getTokenAttributes();
        return userDetailsService.loadUserByUsername(attributes.get("username").toString());
    }
}
