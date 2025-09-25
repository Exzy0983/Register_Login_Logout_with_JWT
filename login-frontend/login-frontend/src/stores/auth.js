import { defineStore } from "pinia";
import { loginAPI, registerAPI, getMeAPI } from "@/stores/auth";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: localStorage.getItem("token") || null,
  }),

  actions: {
    async login(credentials) {
      try {
        const res = await loginAPI(credentials);
        this.token = res.data.token;
        this.user = res.data.user;
        localStorage.setItem("token", this.token);
      } catch (error) {
        console.log("로그인 실패: ", error);
        throw error;
      }
    },

    async register(data) {
      try {
        await registerAPI(data);
      } catch (error) {
        console.log("회원가입 실패: ", error);
        throw error;
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem("token");
    },
  },
});
