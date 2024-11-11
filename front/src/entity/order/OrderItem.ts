import type ItemImage from '../item/ItemImage'

export default class OrderItem {
  public itemId: number = 0

  public itemName: string = ''

  public count: number = 0

  public orderPrice: number = 0

  public itemImages: ItemImage[] = []
}
