package study.tipsyboy.tipsyboyMall.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
