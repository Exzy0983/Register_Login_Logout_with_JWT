import { defineStore } from "pinia";
import {
  loginAPI,
  registerAPI,
  refreshTokenAPI,
  logoutAPI,
  getUserProfileAPI,
} from "@/api/auth";
import router from "@/router";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: JSON.parse(localStorage.getItem("user")) || null,
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
    isLoading: false,
  }),

  getters: {
    // 인증 상태 확인
    isAuthenticated: (state) => !!state.accessToken,

    // 사용자 역할
    userRole: (state) => state.user?.role || null,

    // 관리자 권한 확인
    isAdmin: (state) => state.user?.role === "ADMIN",

    // 매니저 이상 권한 확인
    isManagerOrAdmin: (state) =>
      state.user?.role == "MANAGER" || state.user?.role === "ADMIN",

    // 사용자 정보
    userName: (state) => state.user?.name || "",
    userLoginId: (state) => state.user?.loginId || "",
  },

  actions: {
    // 로그인
    async login(credentials) {
      try {
        this.isLoading = true;
        const response = await loginAPI(credentials);
        const { loginId, name, accessToken, refreshToken, role } =
          response.data;

        // 상태 업데이트
        this.user = { loginId, name, role };
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        // 로컬 스토리지에 저장
        localStorage.setItem("user", JSON.stringify(this.user));
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);

        return response.data;
      } catch (error) {
        console.error("로그인 실패: ", error);
        throw error;
      } finally {
        this.isLoading = false;
      }
    },

    // 회원가입
    async register(userData) {
      try {
        this.isLoading = true;
        const response = await registerAPI(userData);
        return response.data;
      } catch (error) {
        console.log("회원가입 실패: ", error);
        throw error;
      } finally {
        this.isLoading = false;
      }
    },

    //  로그아웃
    async logout() {
      try {
        // 서버에 로그아웃 요청 (리프레시 토큰 무효화)
        if (this.refreshToken) {
          await logoutAPI(this.refreshToken);
        }
      } catch (error) {
        console.log("로그아웃 API 요청 실패: ", error);
        // API 실패해도 로컬 데이터는 삭제
      } finally {
        // 로컬 상태 및 스토리지 정리
        this.clearAuthData();
        router.push("/login");
      }
    },

    // 토큰 갱신
    async refreshAccessToken() {
      try {
        if (!this.refreshToken) {
          throw new Error("Refresh Token이 없습니다.");
        }
        const response = await refreshTokenAPI(this.refreshToken);
        const { accessToken } = response.data;

        this.accessToken = accessToken;
        localStorage.setItem("accessToken", accessToken);

        return accessToken;
      } catch (error) {
        console.error("토큰 갱신 실패 : ", error);
        this.clearAuthData();
        router.push("/login");
        throw error;
      }
    },

    // 사용자 프로필 로드
    async loadUserProfile() {
      try {
        const response = await getUserProfileAPI();
        this.user = { ...this.user, ...response.data };
        localStorage.setItem("user", JSON.stringify(this.user));
        return response.data;
      } catch (error) {
        console.log("사용자 프로필 로드 실패 : ", error);
        throw error;
      }
    },

    clearAuthData() {
      this.user = null;
      this.accessToken = null;
      this.refreshToken = null;
      localStorage.removeItem("user");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
    },

    // 앱 시작 시 인증 상태 초기화
    initializeAuth() {
      const user = localStorage.getItem("user");
      const accessToken = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");

      if (user && accessToken && refreshToken) {
        this.user = JSON.parse(user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
      } else {
        this.clearAuthData();
      }
    },
  },
});
