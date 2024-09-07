package com.kakaotech.back.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaotech.back.dto.auth.AuthorityDto;
import com.kakaotech.back.entity.Member;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotNull
    private String memberId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    private Set<AuthorityDto> authorityDtoSet;

    public static MemberDto from(Member member) {
        if(member == null) return null;

        return MemberDto.builder()
                .memberId(member.getMemberId())
                .authorityDtoSet(member.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
