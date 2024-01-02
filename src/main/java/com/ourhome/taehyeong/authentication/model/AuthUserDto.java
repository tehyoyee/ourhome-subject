package com.ourhome.taehyeong.authentication.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class AuthUserDto {

    private String username;
    private String password;
    private String code;

}