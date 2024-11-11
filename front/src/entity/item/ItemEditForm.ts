import type ItemImage from './ItemImage'

export default class ItemEditForm {
  public itemName: string = ''

  public price: number = 0

  public stock: number = 0

  public description: string = ''

  public storedImages: ItemImage[] = []
}
