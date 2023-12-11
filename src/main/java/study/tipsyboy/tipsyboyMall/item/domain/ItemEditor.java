package study.tipsyboy.tipsyboyMall.item.domain;

import lombok.Getter;

@Getter
public class ItemEditor {

    private final String itemName;
    private final Integer price;
    private final Integer stock;
    private final String description;


    public ItemEditor(String itemName, Integer price, Integer stock, String description) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public static ItemEditor.ItemEditorBuilder builder() {
        return new ItemEditor.ItemEditorBuilder();
    }

    public static class ItemEditorBuilder {
        private String itemName;
        private int price;
        private int stock;
        private String description;

        ItemEditorBuilder() {
        }

        public ItemEditor.ItemEditorBuilder itemName(final String itemName) {
            if (itemName != null) {
                this.itemName = itemName;
            }
            return this;
        }

        public ItemEditor.ItemEditorBuilder price(final Integer price) {
            if (price != null) {
                this.price = price;
            }
            return this;
        }

        public ItemEditor.ItemEditorBuilder stock(final Integer stock) {
            if (stock != null) {
                this.stock = stock;
            }
            return this;
        }

        public ItemEditor.ItemEditorBuilder description(final String description) {
            if (description != null) {
                this.description = description;
            }
            return this;
        }

        public ItemEditor build() {
            return new ItemEditor(this.itemName, this.price, this.stock, this.description);
        }

    }
}