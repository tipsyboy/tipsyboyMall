import { singleton, inject } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type ItemCreateForm from '@/entity/item/ItemCreateForm'
import type FilesForm from '@/entity/file/FilesForm'
import Item from '@/entity/item/Item'
import type ItemEditForm from '@/entity/item/ItemEditForm'
import type Paging from '@/entity/data/Paging'

@singleton()
export default class ItemRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public createItem(request: ItemCreateForm, images: FilesForm) {
    const formData = new FormData()
    formData.append('itemCreateDto', new Blob([JSON.stringify(request)], { type: 'application/json' }))

    images.imageFiles.forEach((image: any) => {
      formData.append('imageFiles', image.raw)
    })

    const headers = {
      'Content-Type': 'multipart/form-data',
    }

    return this.httpRepository.post(
      {
        path: '/api/items',
        body: formData,
        headers: headers,
      },
      Item,
    )
  }

  public getItem(itemId: number): Promise<Item> {
    return this.httpRepository.get(
      {
        path: `/api/items/${itemId}`,
      },
      Item,
    )
  }

  public getItemList(page: number): Promise<Paging<Item>> {
    return this.httpRepository.getList(
      {
        path: `/api/items?page=${page}&size=10`,
      },
      Item,
    )
  }

  public editItem(request: ItemEditForm, itemId: number, storedImages: any[]) {
    const formData = new FormData()

    storedImages.forEach((storedImage) => {
      if (storedImage.status === 'ready') {
        formData.append('newImageFiles', storedImage.raw)
      } else if (storedImage.status === 'finished') {
        request.storedImages.push(storedImage.storedName)
      }
    })

    formData.append('itemUpdateDto', new Blob([JSON.stringify(request)], { type: 'application/json' }))

    const headers = {
      'Content-Type': 'multipart/form-data',
    }

    return this.httpRepository.patch(
      {
        path: `/api/items/${itemId}`,
        body: formData,
        headers: headers,
      },
      Item,
    )
  }

  public deleteItem(itemId: number) {
    return this.httpRepository.delete({ path: `/api/items/${itemId}` })
  }

  public getMyItems(page: number): Promise<Paging<Item>> {
    return this.httpRepository.getList(
      {
        path: `/api/items/my-items?page=${page}&size=5`,
      },
      Item,
    )
  }
}
