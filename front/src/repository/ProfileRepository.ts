import LoginMember from '@/entity/member/LoginMember'
import { instanceToPlain } from 'class-transformer'
import { singleton } from 'tsyringe'

@singleton()
export default class ProfileRepository {
  public setProfile(profile: LoginMember) {
    const json = instanceToPlain(profile)
    localStorage.setItem('profile', JSON.stringify(json))
  }

  public getProfile() {
    return localStorage.getItem('profile')
  }

  public clearProfile() {
    localStorage.clear()
  }
}
