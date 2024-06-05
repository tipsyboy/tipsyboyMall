package study.tipsyboy.tipsyboyMall.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<Item> getItems(ItemSearchReqDto pagingRequestDto);

    Page<Item> getMyItems(ItemSearchReqDto pagingRequestDto, Member member);
}
