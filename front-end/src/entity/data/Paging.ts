export default class Paging<T> {
  public page: number = 0
  public size: number = 0
  public totalCount: number = 0
  public contents: T[] = []

  setContents(contents: T[]) {
    this.contents = contents
  }
}
