import axios, { AxiosError, type AxiosInstance, type AxiosResponse } from 'axios'
import HttpError from '@/http/HttpError'
import { singleton } from 'tsyringe'

export type HttpRequestConfig = {
  headers?: Record<string, string>
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  path: string
  params?: any
  body?: any
}

@singleton()
export default class AxiosHttpClient {
  private readonly client: AxiosInstance = axios.create({
    timeout: 3000,
    timeoutErrorMessage: 'timeout... 왜 안됨..?'
  })

  public async request(config: HttpRequestConfig) {
    return this.client
      .request({
        headers: config.headers,
        method: config.method,
        url: config.path,
        params: config.params,
        data: config.body
      })
      .then((response: AxiosResponse) => {
        return response.data
      })
      .catch((e: AxiosError) => {
        // const errorMessage = e.response?.data.message ?? e.message
        return Promise.reject(new HttpError(e))
      })
  }
}
