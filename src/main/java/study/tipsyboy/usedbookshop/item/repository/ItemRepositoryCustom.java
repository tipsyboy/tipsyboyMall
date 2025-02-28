package study.tipsyboy.usedbookshop.item.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.dto.ItemSearchReqDto;

public interface ItemRepositoryCustom {

    Page<Item> getItems(ItemSearchReqDto pagingRequestDto);

    Page<Item> getMyItems(ItemSearchReqDto pagingRequestDto, Member member);
}
