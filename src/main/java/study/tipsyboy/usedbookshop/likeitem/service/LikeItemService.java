package study.tipsyboy.usedbookshop.likeitem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.exception.AuthException;
import study.tipsyboy.usedbookshop.auth.exception.AuthExceptionType;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.likeitem.domain.LikeItem;
import study.tipsyboy.usedbookshop.likeitem.dto.LikeItemResponseDto;
import study.tipsyboy.usedbookshop.likeitem.exception.LikeItemException;
import study.tipsyboy.usedbookshop.likeitem.exception.LikeItemExceptionType;
import study.tipsyboy.usedbookshop.likeitem.repository.LikeItemRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final LikeItemRepository likeItemRepository;
    private static final Integer PER_PAGE = 10;

    @Transactional
    public Long addLikeItem(Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        if (likeItemRepository.existsByMemberIdAndItemId(memberId, itemId)) {
            throw new LikeItemException(LikeItemExceptionType.DUPLICATE_LIKE_ITEM);
        }

        LikeItem likeItem = LikeItem.builder()
                .member(member)
                .item(item)
                .build();
        LikeItem savedLikeItem = likeItemRepository.save(likeItem);
        item.addLikeCount();

        return savedLikeItem.getId();
    }

    public Page<LikeItemResponseDto> readLikeItem(Long memberId, int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        PageRequest pageable = PageRequest.of(page, PER_PAGE, Sort.by(sorts));

        return likeItemRepository.findByMemberId(memberId, pageable)
                .map(LikeItemResponseDto::new);
    }

    @Transactional
    public void deleteLikeItem(Long likeItemId) {
        LikeItem likeItem = likeItemRepository.findById(likeItemId)
                .orElseThrow(() -> new LikeItemException(LikeItemExceptionType.NOT_FOUND_LIKE_ITEM));
        likeItemRepository.delete(likeItem);
    }
}
