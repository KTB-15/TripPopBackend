package com.kakaotech.back.dto.oauth;

import com.kakaotech.back.entity.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final UserDto userDto;
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        for (Authority authority : userDto.getAuthorities()) {
            collection.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return authority.getAuthorityName(); // Authority 엔티티에서 권한 이름을 반환
                }
            });
        }

        return collection;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public String getMemberId() {
        return userDto.getMemberId();
    }
}
