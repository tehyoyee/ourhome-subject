package com.ourhome.taehyeong.entities;

import com.ourhome.taehyeong.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Data
public class User {

    @Id
    private String username;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}