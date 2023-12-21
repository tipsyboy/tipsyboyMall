import { defineStore } from "pinia";
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'

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
            router.push({ name: "home" })
        }) 
        .catch((error) => {
            console.log(error);
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
