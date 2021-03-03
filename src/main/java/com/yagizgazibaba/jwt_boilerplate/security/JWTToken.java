package com.yagizgazibaba.jwt_boilerplate.security;


import lombok.Data;

import java.io.Serializable;

@Data
public class JWTToken implements Serializable {
    private String token;
}
