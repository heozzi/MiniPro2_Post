package com.example.minipro2_post.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private int uid;

    private String username;
    private String password;
    private String userdetail;
}
