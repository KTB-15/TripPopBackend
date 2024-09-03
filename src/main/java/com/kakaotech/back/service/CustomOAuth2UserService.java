package com.kakaotech.back.service;

import com.kakaotech.back.dto.member.MemberDto;
import com.kakaotech.back.dto.oauth.CustomOAuth2User;
import com.kakaotech.back.dto.oauth.GoogleResponse;
import com.kakaotech.back.dto.oauth.OAuth2Response;
import com.kakaotech.back.dto.oauth.UserDto;
import com.kakaotech.back.entity.Authority;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            // TODO: Naver OAuth 추가
            //oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = (OAuth2Response) new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")){
            // TODO: Kakao OAuth 추가
            //oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String memberId = oAuth2Response.getProvider()+ " " + oAuth2Response.getProviderId();
        Optional<Member> existData = memberRepository.findOneWithAuthoritiesByMemberId(memberId);

        UserDto userDto = null;
        if (existData.isEmpty()) {
            Authority authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            Member member = null;

            if(oAuth2Response instanceof GoogleResponse){
                member = Member.builder()
                        .memberId(memberId)
                        .build();

                userDto = UserDto.builder()
                        .name(oAuth2Response.getName())
                        .memberId(memberId)
                        .authorities(Collections.singleton(authority))
                        .build();
            }

            memberRepository.save(member);
        }
        else {
            Member existingMember = existData.get();

            userDto = UserDto.builder()
                    .name(oAuth2Response.getName())
                    .memberId(memberId)
                    .authorities(existingMember.getAuthorities())
                    .build();
        }

        return new CustomOAuth2User(userDto);
    }
}
