export default class CommentCreateForm {
  public itemId: number = 0

  public parentCommentId: number | null = null

  public content: string = ''

  public setItemId(itemId: number) {
    this.itemId = itemId
  }

  public setParentCommentId(parentCommentId: number) {
    this.parentCommentId = parentCommentId
  }
}
