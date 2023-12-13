package study.tipsyboy.tipsyboyMall.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberApiControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberApiController memberController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(
            username = "tipsyboy@gmail.com",
            roles = {"MEMBER"}
    )
    @DisplayName("Id 값을 통해 사용자 프로필 정보를 가져온다.")
    public void memberProfile() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .password("1234")
                .build();
        memberRepository.save(member);

        // expected
        mockMvc.perform(get("/members/profile/{memberId}", member.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.email").value("tipsyboy@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("간술맨"))
                .andExpect(jsonPath("$.role").value(MemberRole.MEMBER.getRole()))
                .andDo(print());
    }
}