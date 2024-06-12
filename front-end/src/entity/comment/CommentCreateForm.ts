export default class CommentCreateForm {
  public itemId: number = 0

  public parentCommentId = null

  public content: string = ''

  public setItemId(itemId: number) {
    this.itemId = itemId
  }
}
