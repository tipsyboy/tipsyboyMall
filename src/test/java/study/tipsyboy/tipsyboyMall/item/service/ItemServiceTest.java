package study.tipsyboy.tipsyboyMall.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.domain.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;

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


    @AfterEach
    public void after() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("상품을 등록한다.")
    public void saveItem() {
        // given
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();

        // when
        itemService.saveItem(itemCreateDto);

        // then
        Item findItem = itemRepository.findAll().get(0);
        assertEquals(1L, itemRepository.count());
        assertEquals("상품", findItem.getItemName());
        assertEquals(2000, findItem.getPrice());
        assertEquals(10, findItem.getStock());
        assertEquals("상품 설명입니다.", findItem.getDescription());
    }

    @Test
    @DisplayName("상품을 단건 조회한다.")
    public void getItemById() throws Exception {
        // given
        Item item = Item.builder()
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

    @Test
    @DisplayName("전체 상품을 조회한다.")
    public void getItemAll() throws Exception {
        // given
        List<Item> items = IntStream.range(0, 20)
                .mapToObj(i -> Item.builder()
                        .itemName("상품 " + i)
                        .price(10000 + i)
                        .stock(i)
                        .description("상품 설명 " + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        // when
        List<ItemResponseDto> findItems = itemService.getAllItems();

        // then
        assertEquals(20, itemRepository.count());
        assertEquals(20, findItems.size());
    }

    @Test
    @DisplayName("상품 단건 조회에 실패한다. - 존재 하지 않는 상품")
    public void getOneItemFail() throws Exception {
        // given
        Item item = Item.builder()
                .itemName("상품")
                .price(2000)
                .stock(10)
                .description("상품 설명입니다.")
                .build();
        itemRepository.save(item);

        // expected
        assertThrows(IllegalArgumentException.class, ()
                        -> itemService.getItemById(item.getId() + 1L));
    }

    @Test
    @DisplayName("상품의 정보를 변경한다.")
    public void updateItem() throws Exception {
        // given
        Item item = Item.builder()
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

        // when
        itemService.edit(item.getId(), itemUpdateDto);

        Item findItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

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
        Item item = Item.builder()
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

}
