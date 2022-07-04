package com.wooim.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.intellij.lang.annotations.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MBR")
@Entity(name = "MBR")
public class Member implements UserDetails {

    @Id
    @Column(name = "MBR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL의 AUTO_INCREMENT를 사용
    private Integer mbrId;

    @Column( name = "MBR_NM", nullable = false )
    private String mbrNm;

    @Column( name = "MBR_PW", nullable = false )
    private String mbrPw;

    @Column( name = "EMAIL", nullable = false, unique = true )
    private String email;

    @Column( name = "MBR_NK_NM", nullable = true )
    private String mbrNkNm;

    @Column( name = "PHONE", nullable = true, unique = true )
    private String phone;

    @Column( name = "REG_DTT", nullable = false)
    private LocalDate regDtt;

    @Column( name = "MOD_DTT", nullable = false)
    private LocalDate modDtt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Collections.singletonList("ROLE_USER");

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return mbrPw;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return mbrId.toString();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
