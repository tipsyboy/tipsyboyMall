package study.tipsyboy.tipsyboyMall.likeitem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.likeitem.domain.LikeItem;

public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {
    boolean existsByMemberIdAndItemId(Long memberId, Long itemId);

    Page<LikeItem> findByMemberId(Long memberId, Pageable pageable);
}
