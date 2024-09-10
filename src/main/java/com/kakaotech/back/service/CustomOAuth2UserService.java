package com.kakaotech.back.service;

import com.kakaotech.back.dto.oauth.*;
import com.kakaotech.back.entity.Authority;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.AuthorityRepository;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = (OAuth2Response) new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes(), oAuth2User.getAttributes());
        } else {

            logger.error("not exists oauth");
            return null;
        }

        String memberId = oAuth2Response.getProvider()+ " " + oAuth2Response.getProviderId();
        Optional<Member> existData = memberRepository.findOneWithAuthoritiesByMemberId(memberId);

        UserDto userDto = null;
        if (existData.isEmpty()) {

            Authority authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            if(!authorityRepository.existsByAuthorityName("ROLE_USER")) authorityRepository.save(authority);

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
            } else if(oAuth2Response instanceof NaverResponse){
                member = Member.builder()
                        .memberId(memberId)
                        .gender(((NaverResponse) oAuth2Response).getGender())
                        .age(((NaverResponse) oAuth2Response).getAge())
                        .authorities(Collections.singleton(authority))
                        .activated(true)
                        .build();

                userDto = UserDto.builder()
                        .name(oAuth2Response.getName())
                        .memberId(memberId)
                        .authorities(Collections.singleton(authority))
                        .build();
            } else if(oAuth2Response instanceof KakaoResponse) {
                member = Member.builder()
                        .memberId(memberId)
                        .gender(((KakaoResponse) oAuth2Response).getGender())
                        .age(((KakaoResponse) oAuth2Response).getAge())
                        .authorities(Collections.singleton(authority))
                        .activated(true)
                        .build();

                userDto = UserDto.builder()
                        .name(oAuth2Response.getName())
                        .memberId(memberId)
                        .authorities(Collections.singleton(authority))
                        .build();
            }

            logger.info("member save: " + member);
            memberRepository.save(member);
        }
        else {
            Member existingMember = existData.get();

            userDto = UserDto.builder()
                    .name(oAuth2Response.getName())
                    .memberId(existingMember.getMemberId())
                    .authorities(existingMember.getAuthorities())
                    .build();
        }

        return new CustomOAuth2User(userDto);
    }
}
