package study.tipsyboy.usedbookshop.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.exception.AuthException;
import study.tipsyboy.usedbookshop.auth.exception.AuthExceptionType;
import study.tipsyboy.usedbookshop.files.FileStore;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.domain.ItemEditor;
import study.tipsyboy.usedbookshop.item.dto.ItemCreateDto;
import study.tipsyboy.usedbookshop.item.dto.ItemResponseDto;
import study.tipsyboy.usedbookshop.item.dto.ItemSearchReqDto;
import study.tipsyboy.usedbookshop.item.dto.ItemUpdateDto;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.response.PagingResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    @Transactional
    public Long saveItem(ItemCreateDto itemCreateDto, Long memberId,
                         List<MultipartFile> imageFiles) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        Item item = Item.builder()
                .member(member)
                .itemName(itemCreateDto.getItemName())
                .price(itemCreateDto.getPrice())
                .stock(itemCreateDto.getStock())
                .description(itemCreateDto.getDescription())
                .build();

        if (imageFiles != null) {
            saveItemImages(item, imageFiles);
        }

        return itemRepository.save(item).getId();
    }

    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        return new ItemResponseDto(item);
    }


    public PagingResponse<ItemResponseDto> getItemsForPage(ItemSearchReqDto requestDto) {
        Page<Item> resultPage = itemRepository.getItems(requestDto);

        PagingResponse<ItemResponseDto> itemList = new PagingResponse<>(resultPage, ItemResponseDto.class);

        return itemList;
    }


    public PagingResponse<ItemResponseDto> getMyItemForPage(Long memberId, ItemSearchReqDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        Page<Item> myItemsPage = itemRepository.getMyItems(requestDto, member);

        return new PagingResponse<>(myItemsPage, ItemResponseDto.class);
    }

    @Transactional
    public void edit(Long itemId, ItemUpdateDto itemUpdateDto, List<MultipartFile> newImageFiles) throws IOException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        ItemEditor.ItemEditorBuilder editorBuilder = item.toEditor();
        ItemEditor itemEditor = editorBuilder
                .itemName(itemUpdateDto.getItemName())
                .price(itemUpdateDto.getPrice())
                .stock(itemUpdateDto.getStock())
                .description(itemUpdateDto.getDescription())
                .build();

        if (itemUpdateDto.getStoredImages() != null) {
            item.imageUpdate(itemUpdateDto.getStoredImages());
        }
        if (newImageFiles != null) {
            saveItemImages(item, newImageFiles);
        }
        item.update(itemEditor);
    }

    @Transactional
    public void delete(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        itemRepository.delete(item);
    }

    private Pageable createPageable(ItemSearchReqDto pagingRequestDto) {
        return PageRequest.of(pagingRequestDto.getPage() - 1, pagingRequestDto.getSize());
    }

    private void saveItemImages(Item item, List<MultipartFile> itemFiles) throws IOException {
        for (MultipartFile itemFile : itemFiles) {
            fileStore.createFileForItem(item, itemFile);
        }
    }
}