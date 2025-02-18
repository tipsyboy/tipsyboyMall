import { inject, singleton } from 'tsyringe'
import HttpRepository from './HttpRepository'
import type CommentCreateForm from '@/entity/comment/CommentCreateForm'
import type CommentEditForm from '@/entity/comment/CommentEditForm'
import Comment from '@/entity/comment/Comment'
import type Paging from '@/entity/data/Paging'

@singleton()
export default class CommentRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public createComment(request: CommentCreateForm) {
    return this.httpRepository.post(
      {
        path: `/api/comments`,
        body: request,
      },
      Comment,
    )
  }

  public getCommentList(itemId: number, page: number): Promise<Paging<Comment>> {
    return this.httpRepository.getList(
      {
        path: `/api/comments/${itemId}?page=${page}&size=20`,
      },
      Comment,
    )
  }

  public editComment(commentId: number, request: CommentEditForm) {
    return this.httpRepository.patch(
      {
        path: `/api/comments/${commentId}`,
        body: request,
      },
      Comment,
    )
  }

  public deleteComment(commentId: number) {
    return this.httpRepository.delete({
      path: `/api/comments/${commentId}`,
    })
  }

  public likeComment(commentId: number) {
    return this.httpRepository.post({
      path: `/api/comments/${commentId}/like`,
    })
  }

  public dislikeComment(commentId: number) {
    return this.httpRepository.post({
      path: `/api/comments/${commentId}/dislike`,
    })
  }
}
