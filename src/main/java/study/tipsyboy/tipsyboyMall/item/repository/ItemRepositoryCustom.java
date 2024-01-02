package study.tipsyboy.tipsyboyMall.item.repository;

import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemPagingRequestDto;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getItems(ItemPagingRequestDto pagingRequestDto);

    List<Item> getMyItems(ItemPagingRequestDto pagingRequestDto, Member member);
}
