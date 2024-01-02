package study.tipsyboy.tipsyboyMall.item.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @CustomWithMockUser
    @DisplayName("상품을 등록한다.")
    public void saveItem() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();

        String json = objectMapper.writeValueAsString(itemCreateDto);

        // expected
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("한 개의 상품을 조회한다.")
    public void getItemOne() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .member(member)
                .build();
        itemRepository.save(item);

        // expected
        mockMvc.perform(get("/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("상품"))
                .andExpect(jsonPath("$.price").value(2000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.description").value("상품 설명입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("페이징된 상품들을 조회한다.")
    public void getItemsForPage() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        List<Item> items = IntStream.range(0, 50)
                .mapToObj(i -> Item.builder()
                        .member(member)
                        .itemName("상품 " + i)
                        .price(i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        // expected
        mockMvc.perform(get("/items?page=2&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(20)))
                .andExpect(jsonPath("$[0].itemName").value("상품 29"))
                .andExpect(jsonPath("$[19].itemName").value("상품 10"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지가 0과 같거나 작은 경우, 첫 페이지를 가져온다.")
    public void getItemsForMinusPage() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        List<Item> items = IntStream.range(0, 50)
                .mapToObj(i -> Item.builder()
                        .member(member)
                        .itemName("상품 " + i)
                        .price(i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        // expected
        mockMvc.perform(get("/items?page=-1&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(20)))
                .andExpect(jsonPath("$[0].itemName").value("상품 49"))
                .andExpect(jsonPath("$[19].itemName").value("상품 30"))
                .andDo(print());
    }

    @Test
    @CustomWithMockUser
    @DisplayName("상품 등록시 Bean-Validation을 수행한다.")
    public void itemCreateBeanValidationFail() throws Exception {
        // TODO: 좀 더 자세한 테스트 작성
        // given
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        String json = objectMapper.writeValueAsString(itemCreateDto);

        // expected
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("존재 하지 않는 상품을 조회한다.")
    public void readNotFoundItem() throws Exception {
        // expected
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품 정보를 수정한다.")
    public void updateItem() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .itemName("상품 이름 변경")
                .description("상품 설명 변경")
                .build();
        String json = objectMapper.writeValueAsString(itemUpdateDto);

        // expected
        mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품을 삭제한다.")
    public void deleteItem() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        // expected
        mockMvc.perform(delete("/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}