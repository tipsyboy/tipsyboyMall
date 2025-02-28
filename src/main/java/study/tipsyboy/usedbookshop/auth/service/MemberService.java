package study.tipsyboy.usedbookshop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.dto.MemberInfoResponseDto;
import study.tipsyboy.usedbookshop.auth.exception.AuthException;
import study.tipsyboy.usedbookshop.auth.exception.AuthExceptionType;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponseDto findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberInfoResponseDto::of)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));
    }

    public List<MemberInfoResponseDto> findAllMember() {
        return memberRepository.findAll().stream()
                .map(MemberInfoResponseDto::of)
                .collect(Collectors.toList());
    }

    public MemberInfoResponseDto findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(MemberInfoResponseDto::of)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));
    }
}
