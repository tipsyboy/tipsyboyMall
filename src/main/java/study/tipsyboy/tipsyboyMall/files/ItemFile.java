package study.tipsyboy.tipsyboyMall.files;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ItemFile extends UploadFile {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemFile(String uploadName, String storedName, Item item) {
        super(uploadName, storedName);
        mappingItem(item);
    }

    private void mappingItem(Item item) {
        this.item = item;
        item.getItemImages().add(this);
    }

    public void unMapping(Item item) {
        this.item = null;
        item.getItemImages().remove(this);
    }

}
