package com.ourhome.taehyeong.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Otp {

    @Id
    private String username;

    @Column
    private String code;

}