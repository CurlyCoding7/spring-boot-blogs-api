package com.app.blog.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JwtAuthResponse {

    private String token;
}
