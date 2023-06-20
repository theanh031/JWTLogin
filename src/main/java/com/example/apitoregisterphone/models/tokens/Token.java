package com.example.apitoregisterphone.models.tokens;

import com.example.apitoregisterphone.models.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(unique = true)
    private String token;
    
    @Enumerated(EnumType.STRING)    
    private TokenType tokenType = TokenType.BEARER;
    
    private boolean revoked;
    private boolean expired;
    
    private int userId;
    
    @ManyToOne()
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    @JsonBackReference
    private User users;
}
