import axios from "axios";

const api = axios.create({
  baseURL : 'http://localhost:9000/api', // 백엔드 서버 주소
  timeout : 5000
})

export default api