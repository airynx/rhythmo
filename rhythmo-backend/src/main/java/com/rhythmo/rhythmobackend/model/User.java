package com.rhythmo.rhythmobackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 18, message = "Bad username length. Should be 3-18 symbols.")
    @Column(nullable = false, unique = true)
    @NonNull
    private String username;

    @Email(message = "Email should be well-formed.")
    @Column(nullable = false)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn)
    @Column
    private List<String> roles = new ArrayList<>();

    @URL(message = "Avatar URL should be valid.")
    private String avatarUrl;

    @Size(max = 250, message = "AboutMe should have 250 symbols maximum.")
    private String aboutMe;

    @ElementCollection
    private List<String> socialLinks;

    @URL(message = "Avatar URL should be valid.")
    private String profileCoverUrl;

    private String bgColor;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }
}
