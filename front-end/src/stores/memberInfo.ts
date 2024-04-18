import { defineStore } from "pinia";
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

const useMemberStore = defineStore('memberStore', () => {
    const userInfo = ref({ email: '', nickname: '' });
    const isLogin = ref(false);

    const login = (form: any) => {
        axios
        .post('http://localhost:8080/auth/login', 
        {
          email: form.value.email,
          password: form.value.password
        },
        {
          withCredentials: true, 
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
            userInfo.value =
            { 
                email: response.data.email, 
                nickname: response.data.nickname 
            };
            isLogin.value = true;
            ElMessage({
              message: '로그인 되었습니다.',
              type: 'success',
            })
            router.push({ name: "home" })
        }) 
        .catch(() => {
          ElMessage({
            message: '로그인 실패, 이메일과 비밀번호를 확인해주세요.',
            type: 'error',
          })
        })
    }

    const logout = () => {
        // TODO: server logout 구현.
        userInfo.value = { email: '', nickname: '' };
        isLogin.value = false;

        router.push({ name: "login" });
    }
    
    return { userInfo, isLogin, login, logout }

}, {persist:true})

export default useMemberStore
