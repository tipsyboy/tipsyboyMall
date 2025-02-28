package study.tipsyboy.usedbookshop.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.usedbookshop.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
