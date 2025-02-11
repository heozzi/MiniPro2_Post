package com.example.minipro2_post.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * insert into user(username, password,userdetail) values("kimkangbin","1234","11");
 * insert into user(username, password,userdetail) values("leekangbin","1234","11");
 * insert into user(username, password,userdetail) values("parkkangbin","1234","11");
 */

@Entity
@Data
@Table(name="User")
@NoArgsConstructor
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String username;
    private String password;
    private String userdetail;
}
