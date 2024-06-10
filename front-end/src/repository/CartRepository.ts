import { inject, singleton } from 'tsyringe'
import HttpRepository from './HttpRepository'
import type CartItemCreateForm from '@/entity/cart/CartItemCreateForm'
import CartItem from '@/entity/cart/CartItem'
import type Paging from '@/entity/data/Paging'
import type CartItemEditForm from '@/entity/cart/CartItemEditForm'

@singleton()
export default class CartRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public addToCart(request: CartItemCreateForm) {
    return this.httpRepository.post(
      {
        path: `/api/cartItem`,
        body: request,
      },
      CartItem,
    )
  }

  public getCartItem(request): Promise<CartItem[]> {
    const queryParams = request.map((id) => `ids=${id}`).join('&')
    return this.httpRepository.get(
      {
        path: `/api/cartItem/byIds?${queryParams}`,
      },
      CartItem,
    )
  }

  public getCartItemList(page: number): Promise<Paging<CartItem>> {
    return this.httpRepository.getList(
      {
        path: `/api/cartItem?page=${page}&size=5`,
      },
      CartItem,
    )
  }

  public editCartItemCount(request: CartItemEditForm, cartItemId: number) {
    return this.httpRepository.patch({
      path: `/api/cartItem/${cartItemId}`,
      body: request,
    })
  }

  public deleteCartItem(cartItemId: number) {
    return this.httpRepository.delete({
      path: `/api/cartItem/${cartItemId}`,
    })
  }
}
