import DeliveryRequest from './DeliveryRequest'

export default class OrderCreateForm {
  public orderer: string = ''
  public email: string = ''

  public cartItemIds: number[] = []

  public delivery: DeliveryRequest = new DeliveryRequest()
}
