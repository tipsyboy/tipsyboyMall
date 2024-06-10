import { DateTimeFormatter, LocalDateTime, ChronoUnit } from '@js-joda/core'
import { Transform } from 'class-transformer'
import type ItemImage from '@/entity/item/ItemImage'

export default class Item {
  public itemId: number = 0

  public itemName: string = ''

  public status: string = ''

  public seller: string = ''

  public price: number = 0

  public stock: number = 0

  public description: string = ''

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME), {
    toClassOnly: true,
  })
  public createdDate: LocalDateTime = LocalDateTime.now()

  public itemImages: ItemImage[] = []

  public getFormattedDateTime() {
    const now = LocalDateTime.now()
    const diff = ChronoUnit.SECONDS.between(this.createdDate, now)

    if (diff < 60) {
      return '방금 전'
    } else if (diff < 3600) {
      const minutes = Math.floor(diff / 60)
      return `${minutes}분 전`
    } else if (diff < 86400) {
      const hours = Math.floor(diff / 3600)
      return `${hours}시간 전`
    } else if (diff < 2592000) {
      const days = Math.floor(diff / 86400)
      return `${days}일 전`
    } else {
      return this.createdDate.format(DateTimeFormatter.ofPattern('yyyy.MM.dd'))
    }
  }
}
