package study.tipsyboy.tipsyboyMall.comment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import study.tipsyboy.tipsyboyMall.annotation.CustomWithMockUser;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentContentEditRequestDto;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentCreateRequestDto;
import study.tipsyboy.tipsyboyMall.comment.exception.CommentException;
import study.tipsyboy.tipsyboyMall.comment.exception.CommentExceptionType;
import study.tipsyboy.tipsyboyMall.comment.repository.CommentRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.likeitem.repository.LikeItemRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품에 댓글을 작성한다.")
    public void createComment() throws Exception {
        // given
        Member commentAuthor = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        CommentCreateRequestDto requestDto = CommentCreateRequestDto.builder()
                .itemId(item.getId())
                .content("구매 희망")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품 댓글을 불러온다.")
    public void readComment() throws Exception {
        // given
        Member commentAuthor = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        List<Comment> comments = IntStream.range(0, 20)
                .mapToObj(i -> Comment.builder()
                        .parentComment(null)
                        .item(item)
                        .author(commentAuthor)
                        .content("댓글 내용 " + i)
                        .build())
                .collect(Collectors.toList());
        commentRepository.saveAll(comments);

        // expected
        mockMvc.perform(get("/api/comment/{itemId}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("댓글 내용을 수정한다.")
    public void editCommentContent() throws Exception {
        // given
        Member commentAuthor = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        Comment comment = Comment.builder()
                .item(item)
                .author(commentAuthor)
                .parentComment(null)
                .content("수정 전 댓글 내용")
                .build();
        commentRepository.save(comment);

        CommentContentEditRequestDto requestDto = CommentContentEditRequestDto.builder()
                .commentId(comment.getId())
                .content("수정 후 댓글 내용")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(patch("/api/comment/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Comment editedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
        assertEquals("수정 후 댓글 내용", editedComment.getContent());
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("댓글을 삭제한다 -> 상태 변경")
    public void deleteComment() throws Exception {
        // given
        Member commentAuthor = memberRepository.findAll().get(0);
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(seller)
                .build();
        itemRepository.save(item);

        Comment comment = Comment.builder()
                .item(item)
                .author(commentAuthor)
                .parentComment(null)
                .content("삭제할 댓글")
                .build();
        commentRepository.save(comment);


        // expected
        assertFalse(comment.isDeleted());

        mockMvc.perform(delete("/api/comment/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        Comment deletedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        assertTrue(deletedComment.isDeleted());
    }
}