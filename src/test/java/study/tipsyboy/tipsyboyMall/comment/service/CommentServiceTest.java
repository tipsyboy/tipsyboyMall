package study.tipsyboy.tipsyboyMall.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    public void after() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("상품에 댓글을 작성한다.")
    public void addCommentToItem() throws Exception {
        // given
        Member author = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(author);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(author)
                .build();
        itemRepository.save(item);

        // when
        CommentCreateRequestDto requestDto = CommentCreateRequestDto.builder()
                .itemId(item.getId())
                .parentCommentId(null)
                .content("댓글 작성")
                .build();
        Long savedCommentId = commentService.create(author.getId(), requestDto);

        // then
        Comment comment = commentRepository.findAll().get(0);

        assertEquals(1L, commentRepository.count());
        assertEquals(savedCommentId, comment.getId());
        assertEquals("댓글 작성", comment.getContent());
        assertEquals(item.getId(), comment.getItem().getId());
        assertEquals(author.getId(), comment.getAuthor().getId());
        assertNull(comment.getParentComment());
    }

    @Test
    @DisplayName("상품 댓글에 대댓글을 작성한다.")
    public void addCommentToParentComment() throws Exception {
        // given
        Member author = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(author);

        Item item = Item.builder()
                .itemName("객체 지향의 사실과 오해")
                .price(20000)
                .stock(10)
                .description("역할, 책임, 협력 관점에서 본 객체지향")
                .member(author)
                .build();
        itemRepository.save(item);

        Comment parentComment = Comment.builder()
                .item(item)
                .author(author)
                .parentComment(null)
                .content("부모 댓글")
                .build();
        commentRepository.save(parentComment);

        // when
        CommentCreateRequestDto requestDto = CommentCreateRequestDto.builder()
                .itemId(item.getId())
                .parentCommentId(parentComment.getId())
                .content("자식 댓글")
                .build();
        Long savedCommentId = commentService.create(author.getId(), requestDto);

        Comment childComment = commentRepository.findById(savedCommentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        // then
        assertEquals(2L, commentRepository.count());
        assertEquals(parentComment.getId(), childComment.getParentComment().getId());
    }

//    @Test
//    @DisplayName("상품에 댓글을 가져온다.")
//    public void readCommentByItem() throws Exception {
//        // given
//        Member author = Member.builder()
//                .email("tipsyboy@gmail.com")
//                .password("1234")
//                .nickname("간술맨")
//                .memberRole(MemberRole.MEMBER)
//                .build();
//        memberRepository.save(author);
//
//        Item item = Item.builder()
//                .itemName("객체 지향의 사실과 오해")
//                .price(20000)
//                .stock(10)
//                .description("역할, 책임, 협력 관점에서 본 객체지향")
//                .member(author)
//                .build();
//        itemRepository.save(item);
//
//        List<Comment> comments = IntStream.range(0, 10)
//                .mapToObj(i -> Comment.builder()
//                        .author(author)
//                        .item(item)
//                        .content("댓글 " + i)
//                        .parentComment(null)
//                        .build())
//                .collect(Collectors.toList());
//        List<Comment> children = IntStream.range(0, 5)
//                .mapToObj(i -> Comment.builder()
//                        .author(author)
//                        .item(item)
//                        .content("댓글 " + i + " 의 자식 댓글")
//                        .parentComment(comments.get(i))
//                        .build())
//                .collect(Collectors.toList());
//        commentRepository.saveAll(comments);
//        commentRepository.saveAll(children);
//
//        // when
//        List<CommentResponseDto> responseDtos = commentService.readCommentByItem(item.getId());
//
//        // then
//        assertEquals(15L, commentRepository.count());
//        assertEquals(15L, responseDtos.size());
//    }

    @Test
    @DisplayName("댓글의 내용을 변경한다.")
    public void editCommentContent() throws Exception {
        // given
        Member author = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(author);

        Item item = Item.builder()
                .itemName("파전에 막걸리")
                .price(20000)
                .stock(10)
                .description("비오는 날엔 파전에 막걸리입니다.")
                .member(author)
                .build();
        itemRepository.save(item);

        Comment comment = Comment.builder()
                .item(item)
                .author(author)
                .parentComment(null)
                .content("ㅇㅈ하는 부분적분")
                .build();
        commentRepository.save(comment);

        // when
        CommentContentEditRequestDto requestDto = CommentContentEditRequestDto.builder()
                .content("ㄴㄴ 사실 파전 별로 안좋아함.")
                .build();
        commentService.editCommentContent(comment.getId(), requestDto);

        // then
        Comment editedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        assertEquals(comment.getId(), editedComment.getId());
        assertEquals("ㄴㄴ 사실 파전 별로 안좋아함.", editedComment.getContent());
    }

    @Test
    @DisplayName("댓글을 삭제한다. -> 상태 변경")
    public void deleteComment() throws Exception {
        // given
        Member author = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(author);

        Item item = Item.builder()
                .itemName("상품")
                .price(1)
                .stock(1)
                .description("상품임")
                .member(author)
                .build();
        itemRepository.save(item);

        Comment comment = Comment.builder()
                .item(item)
                .author(author)
                .parentComment(null)
                .content("성의없게 만드네")
                .build();
        commentRepository.save(comment);

        // when
        Long commentId = commentService.deleteComment(comment.getId());

        // then
        Comment deleteComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
        assertTrue(deleteComment.isDeleted());
    }



}