package study.tipsyboy.usedbookshop.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.comment.domain.Comment;
import study.tipsyboy.usedbookshop.comment.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndMember(Comment comment, Member member);
}
