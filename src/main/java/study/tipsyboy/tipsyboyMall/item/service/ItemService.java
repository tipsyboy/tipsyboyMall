package study.tipsyboy.tipsyboyMall.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.domain.ItemEditor;
import study.tipsyboy.tipsyboyMall.item.domain.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(ItemCreateDto itemCreateDto) {
        Item item = Item.builder()
                .itemName(itemCreateDto.getItemName())
                .price(itemCreateDto.getPrice())
                .stock(itemCreateDto.getStock())
                .description(itemCreateDto.getDescription())
                .build();
        itemRepository.save(item);
    }

    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        return ItemResponseDto.builder()
                .itemName(item.getItemName())
                .price(item.getPrice())
                .stock(item.getStock())
                .description(item.getDescription())
                .build();
    }

    public List<ItemResponseDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long itemId, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        ItemEditor.ItemEditorBuilder editorBuilder = item.toEditor();
        ItemEditor itemEditor = editorBuilder
                .itemName(itemUpdateDto.getItemName())
                .price(itemUpdateDto.getPrice())
                .stock(itemUpdateDto.getStock())
                .description(itemUpdateDto.getDescription())
                .build();

        item.update(itemEditor);
    }

    @Transactional
    public void delete(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        itemRepository.delete(item);
    }

}