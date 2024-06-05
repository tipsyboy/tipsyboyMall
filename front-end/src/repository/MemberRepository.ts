import { singleton, inject } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type SignupForm from '@/entity/member/SignupForm'
import type LoginForm from '@/entity/member/LoginForm'
import LoginMember from '@/entity/member/LoginMember'

@singleton()
export default class MemberRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public signup(request: SignupForm) {
    return this.httpRepository.post({
      path: '/api/auth/signup',
      body: request,
    })
  }

  public login(request: LoginForm) {
    return this.httpRepository.post({
      path: '/api/auth/login',
      body: request,
    })
  }

  public getProfileByAuth() {
    return this.httpRepository.get<LoginMember>(
      {
        path: `/api/members/me`,
      },
      LoginMember,
    )
  }

  public getProfileByNickname(nickname: string) {
    return this.httpRepository.get<LoginMember>(
      {
        path: `/api/members/profile/${nickname}`,
      },
      LoginMember,
    )
  }
}
