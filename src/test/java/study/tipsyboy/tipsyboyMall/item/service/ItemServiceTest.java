package study.tipsyboy.tipsyboyMall.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.files.UploadFile;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.response.PagingResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("상품을 등록한다.")
    public void saveItem() throws IOException {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("testImage1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("testImage2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );

        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();

        // when
        Long itemId = itemService.saveItem(itemCreateDto, member.getId(), imageFiles);
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        // then
        assertEquals(1L, itemRepository.count());
        assertEquals("상품", findItem.getItemName());
        assertEquals(2000, findItem.getPrice());
        assertEquals(10, findItem.getStock());
        assertEquals("상품 설명입니다.", findItem.getDescription());

        // imageFile
        assertNotNull(findItem.getItemImages());
        assertEquals(2, findItem.getItemImages().size());
        List<String> expectedFileNames = List.of("test1.PNG", "test2.PNG");
        for (UploadFile uploadFile : findItem.getItemImages()) {
            assertTrue(expectedFileNames.contains(uploadFile.getUploadName()));
        }
    }

    @Test
    @DisplayName("상품을 단건 조회한다.")
    public void getItemById() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        // when
        ItemResponseDto findItem = itemService.getItemById(item.getId());

        // then
        assertEquals(item.getItemName(), findItem.getItemName());
        assertEquals(item.getPrice(), findItem.getPrice());
        assertEquals(item.getStock(), findItem.getStock());
        assertEquals(item.getDescription(), findItem.getDescription());
    }
//
//    @Test
//    @DisplayName("전체 상품을 조회한다.")
//    public void getItemAll() throws Exception {
//        // given
//        Member member = Member.builder()
//                .email("tipsyboy@gmail.com")
//                .password("1234")
//                .nickname("간술맨")
//                .memberRole(MemberRole.MEMBER)
//                .build();
//        memberRepository.save(member);
//
//        List<Item> items = IntStream.range(0, 20)
//                .mapToObj(i -> Item.builder()
//                        .member(member)
//                        .itemName("상품 " + i)
//                        .price(10000 + i)
//                        .stock(i)
//                        .description("상품 설명 " + i)
//                        .build())
//                .collect(Collectors.toList());
//        itemRepository.saveAll(items);
//
//        // when
//        List<ItemResponseDto> findItems = itemService.getAllItems();
//
//        // then
//        assertEquals(20, itemRepository.count());
//        assertEquals(20, findItems.size());
//    }

    @Test
    @DisplayName("상품 단건 조회에 실패한다. - 존재 하지 않는 상품")
    public void getOneItemFail() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        // expected
        ItemException itemException = assertThrows(ItemException.class, ()
                -> itemService.getItemById(item.getId() + 1L));
        assertEquals(ItemExceptionType.ITEM_NOT_FOUND, itemException.getExceptionType());
    }

    @Test
    @DisplayName("상품의 정보를 변경한다.")
    public void updateItem() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .itemName("변경 상품")
                .price(3000)
                .stock(null)
                .description("상품 설명 변경")
                .build();

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("testImage1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("testImage2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );

        // when
        itemService.edit(item.getId(), itemUpdateDto, imageFiles);

        Item findItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        // then
        assertEquals("변경 상품", findItem.getItemName());
        assertEquals(3000, findItem.getPrice());
        assertEquals(10, findItem.getStock());
        assertEquals("상품 설명 변경", findItem.getDescription());
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    public void deleteItem() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        // when
        itemService.delete(item.getId());

        // then
        assertEquals(0L, itemRepository.count());
    }


    @Test
    @DisplayName("페이징된 상품 목록을 조회한다.")
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
                        .price(10000 + i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        // when
        ItemSearchReqDto paging = ItemSearchReqDto.builder()
                .page(2)
                .build();

        PagingResponse<ItemResponseDto> findItems = itemService.getItemsForPage(paging);
        findItems.getContents();

        // then
        assertEquals(50, itemRepository.count());
        assertEquals(20, findItems.getContents().size());
        assertEquals("상품 29", findItems.getContents().get(0).getItemName());
        assertEquals("상품 10", findItems.getContents().get(findItems.getContents().size() - 1).getItemName());
    }

//    @Test
//    @DisplayName("내 상품 목록을 조회한다.")
//    public void getMyItems() throws Exception {
//        // given
//        Member member = Member.builder()
//                .email("tipsyboy@gmail.com")
//                .nickname("간술맨")
//                .password("1234")
//                .memberRole(MemberRole.MEMBER)
//                .build();
//
//        Member member2 = Member.builder()
//                .email("tipsyboy2@gmail.com")
//                .nickname("혼술맨")
//                .password("1234")
//                .memberRole(MemberRole.MEMBER)
//                .build();
//        memberRepository.save(member);
//        memberRepository.save(member2);
//
//        List<Item> items = IntStream.range(0, 30)
//                .mapToObj(i -> Item.builder()
//                        .member(member)
//                        .itemName("상품 " + i)
//                        .price(10000 + i)
//                        .stock(i)
//                        .description("상품 설명 " + i)
//                        .build())
//                .collect(Collectors.toList());
//        itemRepository.saveAll(items);
//
//        List<Item> items2 = IntStream.range(30, 50)
//                .mapToObj(i -> Item.builder()
//                        .member(member2)
//                        .itemName("상품 " + i)
//                        .price(10000 + i)
//                        .stock(i)
//                        .description("상품 설명 " + i)
//                        .build())
//                .collect(Collectors.toList());
//        itemRepository.saveAll(items2);
//
//        // when - 첫 번째 MEMBER 간술맨의 상품만 2번째 페이지 내림차순 조회한다.
//        ItemSearchReqDto paging = ItemSearchReqDto.builder()
//                .page(2)
//                .build();
//        List<ItemResponseDto> findItems = itemService.getMyItemForPage(member.getId(), paging);
//
//        // then
//        assertEquals(50, itemRepository.count());
//        assertEquals(10, findItems.size());
//        assertEquals("상품 9", findItems.get(0).getItemName());
//        assertEquals("상품 0", findItems.get(findItems.size() - 1).getItemName());
//    }

    @Test
    @DisplayName("상품 제목으로 검색")
    public void searchByItemName() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .nickname("간술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item1 = Item.builder().itemName("벤츠 E 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item2 = Item.builder().itemName("벤츠 S 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item3 = Item.builder().itemName("포르쉐 파나메라").member(member).price(10000).stock(10).description(".").build();
        Item item4 = Item.builder().itemName("포르쉐 박스터").member(member).price(10000).stock(10).description(".").build();
        Item item5 = Item.builder().itemName("포르쉐 카이엔").member(member).price(10000).stock(10).description(".").build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);


        // when - 제목에 '벤츠'가 포함된 상품 검색
        ItemSearchReqDto searchReqDto = ItemSearchReqDto.builder()
                .page(1)
                .title("벤츠")
                .build();
        PagingResponse<ItemResponseDto> searchItems = itemService.getItemsForPage(searchReqDto);

        // then
        assertEquals(5, itemRepository.count());
        assertEquals(2, searchItems.getContents().size());
        for (ItemResponseDto searchedItem : searchItems.getContents()) {
            assertTrue(searchedItem.getItemName().contains("벤츠"));
        }
    }

    @Test
    @DisplayName("판매자 이름으로 검색")
    public void searchBySeller() throws Exception {
        // given
        Member member = Member.builder()
                .nickname("간술맨")
                .email("tipsyboy@gmail.com")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member member2 = Member.builder()
                .nickname("혼술맨")
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);

        Item item1 = Item.builder().itemName("벤츠 E 클래스").member(member).price(10000).stock(10).description(".").build();
        Item item3 = Item.builder().itemName("포르쉐 파나메라").member(member).price(10000).stock(10).description(".").build();
        Item item5 = Item.builder().itemName("포르쉐 카이엔").member(member).price(10000).stock(10).description(".").build();
        Item item2 = Item.builder().itemName("벤츠 S 클래스").member(member2).price(10000).stock(10).description(".").build();
        Item item4 = Item.builder().itemName("포르쉐 박스터").member(member2).price(10000).stock(10).description(".").build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);


        // when - 판매자 이름에 '혼술맨'이 포함된 상품 검색
        ItemSearchReqDto searchReqDto = ItemSearchReqDto.builder()
                .page(1)
                .seller("혼술맨")
                .build();
        PagingResponse<ItemResponseDto> searchItems = itemService.getItemsForPage(searchReqDto);

        // then
        assertEquals(5, itemRepository.count());
        assertEquals(2, searchItems.getContents().size());
        for (ItemResponseDto searchedItem : searchItems.getContents()) {
            assertTrue(searchedItem.getSeller().contains("혼술맨"));
        }
    }
}
