package study.tipsyboy.usedbookshop.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.domain.MemberRole;
import study.tipsyboy.usedbookshop.auth.dto.SignupRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
    }
    
    @Test
    @DisplayName("회원가입을 성공한다.")
    public void signup() throws Exception {
        // given
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일로 가입하면 이메일 중복예외가 발생한다.")
    public void signupFailDupEmail() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 중복된 닉네임으로 가입하면 닉네임 중복예외가 발생한다.")
    public void signupFailDupNickname() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .password("1234")
                .build();
        memberRepository.save(member);

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    
}