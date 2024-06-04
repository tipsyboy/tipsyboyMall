import Null from '@/entity/data/Null'
import Paging from '@/entity/data/Paging'
import AxiosHttpClient, { type HttpRequestConfig } from '@/http/AxiosHttpClient'
import { plainToInstance } from 'class-transformer'
import { inject, singleton } from 'tsyringe'

@singleton()
export default class HttpRepository {
  constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {
    this.httpClient = httpClient
  }

  public async get<T>(config: HttpRequestConfig, clazz: { new (...args: any[]) }): Promise<T> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => plainToInstance(clazz, response))
  }

  public async getList<T>(config: HttpRequestConfig, clazz: { new (...args: any[]) }): Promise<Paging<T>> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => {
      console.log('>>>>', response)
      const paging = plainToInstance<Paging<T>, any>(Paging, response)
      const contents = plainToInstance(clazz, response.contents)
      paging.setContents(contents)
      console.log('>>>>', response)
      return paging
    })
  }

  public async post<T>(config: HttpRequestConfig, clazz: { new (...args: any[]) } | null = null): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'POST' })
      .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }

  public async patch<T>(config: HttpRequestConfig, clazz: { new (...args: any[]) } | null = null): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'PATCH' })
      .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }

  public async delete<T>(config: HttpRequestConfig, clazz: { new (...args: any[]) } | null = null): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'DELETE' })
      .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }
}
