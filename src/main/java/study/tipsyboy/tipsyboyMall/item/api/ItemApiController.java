package study.tipsyboy.tipsyboyMall.item.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.service.ItemService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    public ResponseEntity<Void> saveItem(@RequestBody @Valid ItemCreateDto itemCreateDto,
                                         @AuthenticationPrincipal LoginMember loginMember) {
        itemService.saveItem(itemCreateDto, loginMember.getMemberId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemOne(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping("/items")
    public ResponseEntity<Page<ItemResponseDto>> getItems(@ModelAttribute ItemSearchReqDto pagingRequestDto) {
        return ResponseEntity.ok(itemService.getItemsForPage(pagingRequestDto));
    }

    @GetMapping("/items/my-items")
    public ResponseEntity<List<ItemResponseDto>> getMyItems(
            @ModelAttribute ItemSearchReqDto pagingRequestDto,
            @AuthenticationPrincipal LoginMember loginMember) {

        return ResponseEntity.ok(itemService.getMyItemForPage(loginMember.getMemberId(), pagingRequestDto));
    }

    @PatchMapping("/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#itemId, 'ITEM', 'PATCH'))")
    public ResponseEntity<Void> editItem(@PathVariable Long itemId,
                                         @RequestBody ItemUpdateDto itemUpdateDto) {

        itemService.edit(itemId, itemUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#itemId, 'ITEM', 'DELETE'))")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }
}
