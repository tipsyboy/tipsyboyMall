import { singleton, inject } from 'tsyringe';
import { plainToInstance } from 'class-transformer';
import HttpRepository from "@/repository/HttpRepository";
import type ItemCreateForm from '@/entity/item/ItemCreateForm';
import type FilesForm  from '@/entity/file/FilesForm';
import Item from '@/entity/item/Item';

@singleton()
export default class ItemRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public itemCreate(request: ItemCreateForm, images: FilesForm) {
        const formData = new FormData();
        formData.append('itemCreateDto', new Blob([JSON.stringify(request)], {type: 'application/json'}))
        
        images.imageFiles.forEach((image: any) => {
            formData.append('imageFiles', image.raw)
        })

        const headers = {
            'Content-Type': 'multipart/form-data'
        };
        
        return this.httpRepository.post({
            path: '/api/items',
            body: formData,
            headers: headers
        })
    }

    public getItem(itemId: number) {
        return this.httpRepository.get({
            path: `/api/items/${itemId}`
        })
        .then((response) => {
            return plainToInstance(Item, response)
        })
    }

    // // TODO: 흠...
    // public getImage(storedName: string) {
    //     return this.httpRepository.get({
    //         path: `/api/images/${storedName}`
    //     })
    // }
}