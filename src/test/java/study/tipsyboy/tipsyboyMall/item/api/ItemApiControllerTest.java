package study.tipsyboy.tipsyboyMall.item.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.annotation.CustomWithMockUser;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;

import java.nio.charset.StandardCharsets;
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
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();

        String json = objectMapper.writeValueAsString(itemCreateDto);

        MockMultipartFile jsonToMultipart = new MockMultipartFile(
                "itemCreateDto",
                "itemCreateDto",
                "application/json",
                json.getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(multipart("/api/items")
                        .file(jsonToMultipart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andDo(print());

//        // expected
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andDo(print());
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
        mockMvc.perform(get("/api/items/{itemId}", item.getId())
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
        mockMvc.perform(get("/api/items?page=2&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()", is(20)))
                .andExpect(jsonPath("$.contents[0].itemName").value("상품 29"))
                .andExpect(jsonPath("$.contents[19].itemName").value("상품 10"))
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
        mockMvc.perform(get("/api/items?page=-1&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()", is(20)))
                .andExpect(jsonPath("$.contents[0].itemName").value("상품 49"))
                .andExpect(jsonPath("$.contents[19].itemName").value("상품 30"))
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
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("존재 하지 않는 상품을 조회한다.")
    public void readNotFoundItem() throws Exception {
        // expected
        mockMvc.perform(get("/api/items/{itemId}", 1L)
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

        // JSON 데이터를 MultipartFile로 변환 (컨트롤러의 @RequestPart("itemUpdateDto")와 이름 일치)
        MockMultipartFile itemUpdateDtoPart = new MockMultipartFile(
                "itemUpdateDto",  // 컨트롤러의 @RequestPart("itemUpdateDto")와 동일해야 함
                "",
                "application/json",
                objectMapper.writeValueAsBytes(itemUpdateDto)  //  JSON을 바이트 배열로 변환
        );

        // expected
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/items/{itemId}", item.getId())
                        .file(itemUpdateDtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        )
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
        mockMvc.perform(delete("/api/items/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("내 상품을 조회한다.")
    public void getMyItems() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Member member2 = Member.builder()
                .email("tipsyboy2@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member2);

        List<Item> items = IntStream.range(0, 30)
                .mapToObj(i -> Item.builder()
                        .member(member)
                        .itemName("상품 " + i)
                        .price(10000 + i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        List<Item> items2 = IntStream.range(30, 50)
                .mapToObj(i -> Item.builder()
                        .member(member2)
                        .itemName("상품 " + i)
                        .price(10000 + i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);
        itemRepository.saveAll(items2);

        // expected
        mockMvc.perform(get("/api/items/my-items?page=0&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()", is(20)))
                .andExpect(jsonPath("$.contents[0].itemName").value("상품 29"))
                .andExpect(jsonPath("$.contents[19].itemName").value("상품 10"))
                .andExpect(jsonPath("$.contents[*].seller", everyItem(equalTo("간술맨"))))
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품 이름으로 검색한다.")
    public void searchByItemName() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        Item item1 = Item.builder().itemName("벤츠 E 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item3 = Item.builder().itemName("포르쉐 파나메라").member(member).price(10000).stock(10).description(".").build();
        Item item5 = Item.builder().itemName("포르쉐 카이엔").member(member).price(10000).stock(10).description(".").build();
        Item item2 = Item.builder().itemName("벤츠 S 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item4 = Item.builder().itemName("포르쉐 박스터").member(member).price(10000).stock(10).description(".").build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);

        mockMvc.perform(get("/api/items?page=1&size=20&title=벤츠")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()", is(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("판매자 이름으로 검색한다")
    public void searchBySeller() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Member member2 = Member.builder()
                .email("tipsyboy2@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member2);

        Item item1 = Item.builder().itemName("벤츠 E 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item3 = Item.builder().itemName("포르쉐 파나메라").member(member).price(10000).stock(10).description(".").build();
        Item item5 = Item.builder().itemName("포르쉐 카이엔").member(member2).price(10000).stock(10).description(".").build();
        Item item2 = Item.builder().itemName("벤츠 S 클래스").member(member2).price(10000).stock(10).description(".").build();
        Item item4 = Item.builder().itemName("포르쉐 박스터").member(member2).price(10000).stock(10).description(".").build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);

        mockMvc.perform(get("/api/items?page=1&size=20&seller=혼술맨")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()", is(3)))
                .andDo(print());
    }
}