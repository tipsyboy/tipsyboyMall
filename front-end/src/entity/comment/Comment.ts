import { DateTimeFormatter, LocalDateTime, ChronoUnit } from '@js-joda/core'
import { Transform, Type } from 'class-transformer'

export default class Comment {
  public commentId: number = 0

  public itemId: number = 0

  public author: string = ''

  public content: string = ''

  public parentCommentId = null

  @Type(() => Comment)
  public children: Comment[] = []

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME), {
    toClassOnly: true,
  })
  public createdDate: LocalDateTime = LocalDateTime.now()

  public getFormattedDateTime(): string {
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
