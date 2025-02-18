package study.tipsyboy.tipsyboyMall.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.comment.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndMember(Comment comment, Member member);
}
