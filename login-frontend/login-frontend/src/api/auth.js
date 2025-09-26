import api from './index.js'

// 회원가입
export const registerAPI = userData => api.post('/register', userData)

// 로그인
export const loginAPI = credentials => api.post('/login', credentials)

// 액세스 토큰 갱신
export const refreshTokenAPI = refreshToken => api.post('/refresh', {refreshToken})

// 로그아웃
export const logoutAPI = refreshToken => api.post('/logout', {refreshToken})

// 사용자 프로필 조회
export const getUserProfileAPI = () => api.get('/user/profile')

// 관리자 - 전체 사용자 조회
export const getAllUsersAPI = () => api.get('/admin/users')

