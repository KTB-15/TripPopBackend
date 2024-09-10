package com.kakaotech.back.service;

import com.kakaotech.back.dto.oauth.CustomOAuth2User;
import com.kakaotech.back.dto.oauth.GoogleResponse;
import com.kakaotech.back.dto.oauth.OAuth2Response;
import com.kakaotech.back.dto.oauth.UserDto;
import com.kakaotech.back.entity.Authority;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        logger.info("oAuth2User check : " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            // TODO: Naver OAuth 추가
            //oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            logger.info("goolge input");
            oAuth2Response = (OAuth2Response) new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")){
            // TODO: Kakao OAuth 추가
            //oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {

            logger.info("not exists oauth");
            return null;
        }

        String memberId = oAuth2Response.getProvider()+ " " + oAuth2Response.getProviderId();
        logger.info("OAuth member Id : " + memberId);
        Optional<Member> existData = memberRepository.findOneWithAuthoritiesByMemberId(memberId);

        UserDto userDto = null;
        logger.info("userDto : " + userDto);
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
