package study.tipsyboy.tipsyboyMall.likeitem.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.likeitem.domain.LikeItem;
import study.tipsyboy.tipsyboyMall.likeitem.dto.LikeItemResponseDto;
import study.tipsyboy.tipsyboyMall.likeitem.exception.LikeItemException;
import study.tipsyboy.tipsyboyMall.likeitem.exception.LikeItemExceptionType;
import study.tipsyboy.tipsyboyMall.likeitem.repository.LikeItemRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeItemServiceTest {

    @Autowired
    private LikeItemService likeItemService;

    @Autowired
    private LikeItemRepository likeItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
        likeItemRepository.deleteAll();
        memberRepository.deleteAll();
        em.clear();
    }
    
    @Test
    @DisplayName("상품을 관심상품으로 등록한다.")
    public void saveLikeItem() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(member)
                .build();
        itemRepository.save(item);

        // when
        Long likeItemId = likeItemService.addLikeItem(member.getId(), item.getId());
        LikeItem likeItem = likeItemRepository.findById(likeItemId)
                .orElseThrow(() -> new LikeItemException(LikeItemExceptionType.NOT_FOUND_LIKE_ITEM));

        // then
        assertEquals(1, likeItemRepository.count());
        assertEquals(member.getId(), likeItem.getMember().getId());
        assertEquals(item.getId(), likeItem.getItem().getId());
    }

    @Test
    @DisplayName("이미 관심상품으로 등록한 상품을 또 관심상품으로 등록할 수 없다.")
    public void duplicatedLikeItem() throws Exception {
        // given
        Member member1 = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member member2 = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(member1)
                .build();
        itemRepository.save(item);

        LikeItem likeItem1 = LikeItem.builder()
                .member(member1)
                .item(item)
                .build();
        LikeItem likeItem2 = LikeItem.builder()
                .member(member2)
                .item(item)
                .build();
        likeItemRepository.save(likeItem1);
        likeItemRepository.save(likeItem2);

        // then
        // 관심상품 중복 등록시 예외 발생
        LikeItemException likeItemException = assertThrows(LikeItemException.class,
                () -> likeItemService.addLikeItem(member1.getId(), item.getId()));
        // 예외 타입이 일치
        assertEquals(LikeItemExceptionType.DUPLICATE_LIKE_ITEM, likeItemException.getExceptionType());
        // 중복 등록 안되니까 그대로 2
        assertEquals(2, likeItemRepository.count());
    }

    @Test
    @DisplayName("관심상품 등록을 해제한다.")
    public void deleteLikeItem() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(member)
                .build();
        itemRepository.save(item);

        LikeItem likeItem = LikeItem.builder()
                .member(member)
                .item(item)
                .build();
        likeItemRepository.save(likeItem);

        assertEquals(1, likeItemRepository.count());
        assertTrue(likeItemRepository.existsByMemberIdAndItemId(member.getId(), item.getId()));

        // when
        likeItemService.deleteLikeItem(likeItem.getId());

        // then
        assertEquals(0, likeItemRepository.count());
        assertFalse(likeItemRepository.existsByMemberIdAndItemId(member.getId(), item.getId()));
    }

    @Test
    @DisplayName("사용자의 관심상품들을 조회한다.")
    public void findLikeItemListByMember() throws Exception {
        // given
        Member seller = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);
        memberRepository.save(buyer);

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

        // when
        int page = 1;
        Page<LikeItemResponseDto> likeItemsByMember
                = likeItemService.readLikeItem(buyer.getId(), page);
        // then
        assertEquals(30L, likeItemRepository.count());
        assertEquals(3L, likeItemsByMember.getTotalPages());
        assertEquals(30L, likeItemsByMember.getTotalElements());
        assertEquals(10L, likeItemsByMember.getSize());
        assertEquals("상품 19", likeItemsByMember.getContent().get(0).getItemName());
        assertEquals("상품 10", likeItemsByMember.getContent().get(9).getItemName());
    }

}