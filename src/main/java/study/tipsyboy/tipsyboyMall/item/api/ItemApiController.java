package study.tipsyboy.tipsyboyMall.item.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.item.dto.ItemCreateDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemResponseDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;
import study.tipsyboy.tipsyboyMall.item.dto.ItemUpdateDto;
import study.tipsyboy.tipsyboyMall.item.service.ItemService;
import study.tipsyboy.tipsyboyMall.response.PagingResponse;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping(value = "/items", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    public ResponseEntity<Long> saveItem(@RequestPart(value = "itemCreateDto") @Valid ItemCreateDto itemCreateDto,
                                         @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                         @AuthenticationPrincipal LoginMember loginMember) throws IOException {
        return ResponseEntity.ok(itemService.saveItem(itemCreateDto, loginMember.getMemberId(), imageFiles));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemOne(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping("/items")
    public ResponseEntity<PagingResponse<ItemResponseDto>> getItems(@ModelAttribute ItemSearchReqDto pagingRequestDto) {
        return ResponseEntity.ok(itemService.getItemsForPage(pagingRequestDto));
    }

    @GetMapping("/items/my-items")
    public ResponseEntity<PagingResponse<ItemResponseDto>> getMyItems(
            @ModelAttribute ItemSearchReqDto pagingRequestDto,
            @AuthenticationPrincipal LoginMember loginMember) {

        return ResponseEntity.ok(itemService.getMyItemForPage(loginMember.getMemberId(), pagingRequestDto));
    }

    @PatchMapping(value = "/items/{itemId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#itemId, 'ITEM', 'PATCH'))")
    public ResponseEntity<Void> editItem(@PathVariable Long itemId,
                                         @RequestPart(value = "itemUpdateDto") @Valid ItemUpdateDto itemUpdateDto,
                                         @RequestPart(value = "newImageFiles", required = false) List<MultipartFile> newImageFiles) throws IOException {

        itemService.edit(itemId, itemUpdateDto, newImageFiles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#itemId, 'ITEM', 'DELETE'))")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }
}
