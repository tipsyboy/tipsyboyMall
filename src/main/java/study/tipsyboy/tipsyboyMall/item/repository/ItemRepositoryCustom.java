package study.tipsyboy.tipsyboyMall.item.repository;

import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemPagingRequestDto;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getItems(ItemPagingRequestDto pagingRequestDto);
}
