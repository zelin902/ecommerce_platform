import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8081/api',
  timeout: 5000,
  withCredentials: false          // 如需携带 cookie 改为 true
})

export const register = (params) => instance.post('/register', params)
export const login    = (params) => instance.post('/login',    params)