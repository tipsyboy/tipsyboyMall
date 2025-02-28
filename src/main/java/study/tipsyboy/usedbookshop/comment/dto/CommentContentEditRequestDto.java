package study.tipsyboy.usedbookshop.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentContentEditRequestDto {

    private String content;

    @Builder
    public CommentContentEditRequestDto(String content) {
        this.content = content;
    }
}

