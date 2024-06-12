package study.tipsyboy.tipsyboyMall.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
