package com.yagizgazibaba.jwt_boilerplate.auth;


import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String username;

    private String password;
}
