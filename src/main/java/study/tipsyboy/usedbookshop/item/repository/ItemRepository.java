package study.tipsyboy.usedbookshop.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.usedbookshop.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
