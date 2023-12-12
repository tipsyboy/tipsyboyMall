package study.tipsyboy.tipsyboyMall.item.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.service.ItemService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;
    // merge Test 중입니다.
    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(@RequestBody @Valid ItemCreateDto itemCreateDto) {
        itemService.saveItem(itemCreateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemOne(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> editItem(@PathVariable Long itemId,
                                         @RequestBody ItemUpdateDto itemUpdateDto) {

        itemService.edit(itemId, itemUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }
}
