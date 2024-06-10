import { inject, singleton } from 'tsyringe'
import type OrderCreateForm from '@/entity/order/OrderCreateForm'
import Order from '@/entity/order/Order'
import HttpRepository from './HttpRepository'
import type Paging from '@/entity/data/Paging'

@singleton()
export default class OrderRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public createOrder(request: OrderCreateForm): Promise<number> {
    return this.httpRepository.post(
      {
        path: `/api/orders`,
        body: request,
      },
      Number,
    )
  }

  public getOrder(orderId: number): Promise<Order> {
    return this.httpRepository.get(
      {
        path: `/api/orders/${orderId}`,
      },
      Order,
    )
  }

  public getMyOrderList(page: number): Promise<Paging<Order>> {
    return this.httpRepository.getList(
      {
        path: `/api/orders?page=${page}&size=5`,
      },
      Order,
    )
  }

  public cancelORder(orderId: number) {
    return this.httpRepository.delete({
      path: `/api/orders/${orderId}`,
    })
  }
}
