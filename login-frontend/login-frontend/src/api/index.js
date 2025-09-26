import axios from "axios";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";

const API_BASE_URL = "http://localhost:9000/api";

// Axios 인스턴스 생성
const api = axios.create({
  baseURL : API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 요청 인터셉터 - Authroization 헤더 자동 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터 - 토큰 만료 시 자동 갱신 시도
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    // 401 에러이고 아직 토큰 갱신을 시도하지 않앗다면
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem("refreshToken");

      if (refreshToken) {
        try {
          // 토큰 갱신 시도
          const response = await axios.post(`${API_BASE_URL}/refresh`, {
            refreshToken: refreshToken,
          });

          const { accessToken } = response.data;
          localStorage.setItem("accessToken", accessToken);

          // 원래 요청 재시도
          originRequest.headers.Authorization = `Bearer ${accessToken}`;
          return api(originalResult);
        } catch (refreshError) {
          // 토큰 갱신 실패 시 로그아웃 처리
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          localStorage.removeItem("user");
          router.push("/login");
          return Promise.reject(refreshError);
        }
      } else {
        // 리프레시 토큰이 없으면 로그인 페이지로
        router.push("/login");
      }
    }
    return Promise.reject(error);
  }
);
export default api;
