package study.tipsyboy.usedbookshop.auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.domain.MemberRole;
import study.tipsyboy.usedbookshop.auth.dto.MemberInfoResponseDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자를 id 값으로 조회한다.")
    public void findByMemberId() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .password("1234")
                .build();
        memberRepository.save(member);

        // when
        MemberInfoResponseDto responseDto = memberService.findByMemberId(member.getId());

        // then
        assertEquals(responseDto.getId(), member.getId());
        assertEquals(responseDto.getEmail(), member.getEmail());
        assertEquals(responseDto.getNickname(), member.getNickname());
        assertEquals(responseDto.getRole(), member.getMemberRole().getRole());
    }

    @Test
    @DisplayName("등록된 사용자를 조회한다.")
    public void findAllMember() throws Exception {
        // given
        List<Member> members = IntStream.range(0, 5)
                .mapToObj(i -> Member.builder()
                        .email("user" + i + "@gmail.com")
                        .nickname("user" + i)
                        .memberRole(MemberRole.MEMBER)
                        .password(String.valueOf(i))
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        // when
        List<MemberInfoResponseDto> memberDtos = memberService.findAllMember();

        // then
        assertEquals(memberDtos.size(), memberRepository.count());
        for (int i = 0; i < 5; i++) {
            assertEquals(memberDtos.get(i).getId(), members.get(i).getId());
            assertEquals(memberDtos.get(i).getEmail(), members.get(i).getEmail());
            assertEquals(memberDtos.get(i).getNickname(), members.get(i).getNickname());
            assertEquals(memberDtos.get(i).getRole(), members.get(i).getMemberRole().getRole());
        }
    }

}