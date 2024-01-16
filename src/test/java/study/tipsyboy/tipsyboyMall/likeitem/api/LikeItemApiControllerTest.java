package study.tipsyboy.tipsyboyMall.likeitem.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.annotation.CustomWithMockUser;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.likeitem.domain.LikeItem;
import study.tipsyboy.tipsyboyMall.likeitem.repository.LikeItemRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LikeItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private LikeItemRepository likeItemRepository;

    @AfterEach
    public void after() {
        likeItemRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품을 관심상품으로 등록한다.")
    public void addLikeItem() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        // expected
        mockMvc.perform(post("/api/likeitem/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("한 개의 상품을 중복으로 관심상품 등록할 수 없다.")
    public void duplicatedLikeItem() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);
        
        // 이미 등록된 관심 상품
        likeItemRepository.save(LikeItem.builder()
                .item(item)
                .member(buyer)
                .build());

        // expected
        mockMvc.perform(post("/api/likeitem/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("로그인 사용자의 관심상품을 조회한다.")
    public void readLikeItemByLoginMember() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0); // 로그인 사용자
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        List<Item> items = IntStream.range(0, 30)
                .mapToObj(i -> Item.builder()
                        .member(seller)
                        .itemName("상품 " + i)
                        .price(i)
                        .stock(i)
                        .description(i + "번 째 상품입니다.")
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        List<LikeItem> likeItems = IntStream.range(0, 30)
                .mapToObj(i -> LikeItem.builder()
                        .item(items.get(i))
                        .member(buyer)
                        .build())
                .collect(Collectors.toList());
        likeItemRepository.saveAll(likeItems);

        // expected
        mockMvc.perform(get("/api/likeitem?page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(10)))
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("관심상품 등록을 해제한다.")
    public void deleteLikeItem() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        LikeItem likeItem = LikeItem.builder()
                .item(item)
                .member(buyer)
                .build();
        likeItemRepository.save(likeItem);

        // expected
        mockMvc.perform(delete("/api/likeitem/{likeItemId}", likeItem.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}