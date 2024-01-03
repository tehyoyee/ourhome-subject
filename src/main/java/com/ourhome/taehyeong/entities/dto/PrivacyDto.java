package com.ourhome.taehyeong.entities.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class PrivacyDto {

    String username;

    String name;

    String university;

    Date birth;

    String address;

}
