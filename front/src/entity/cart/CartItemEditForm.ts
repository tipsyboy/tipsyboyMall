export default class CartItemEditForm {
  public cartItemId: number = 0

  public count: number = 0

  public setCartItemId(cartItemId: number) {
    this.cartItemId = cartItemId
  }

  public setCount(count: number) {
    this.count = count
  }
}
