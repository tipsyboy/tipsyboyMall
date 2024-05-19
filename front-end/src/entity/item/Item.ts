import { DateTimeFormatter, LocalDateTime } from "@js-joda/core";
import { Transform } from "class-transformer";
import type ItemImage from "@/entity/item/ItemImage";

export default class Item {
    
    public itemId: number = 0;
    public itemName: string = ""
    public status: string = ""
    public seller: string = ""
    public price: number = 0
    public stock: number = 0
    public description: string = ""
    
    @Transform(({value}) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME), {toClassOnly:true})
    public createdDate: LocalDateTime = LocalDateTime.now()

    public itemImages: ItemImage[] = []
    // public itemImages: ItemImages[]


    public getFormattedDateTime() {
        return this.createdDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm'))
    }
}