import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import type OrderItem from './OrderItem'
import { Transform } from 'class-transformer'
import OrderInfo from './OrderInfo'

export default class Order {
  public id: number = 0

  public orderStatus: string = ''

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME), {
    toClassOnly: true,
  })
  public orderedDate: LocalDateTime = LocalDateTime.now()

  public orderItemList: OrderItem[] = []

  public orderInfo: OrderInfo = new OrderInfo()

  public getFormattedDateTime() {
    return this.orderedDate.format(DateTimeFormatter.ofPattern('yyyy.MM.dd'))
  }
}
