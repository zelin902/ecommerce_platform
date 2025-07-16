// src/api/index.js
import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8081',
  withCredentials: true
})

// 添加请求拦截器：自动携带 userId
instance.interceptors.request.use(
  (config) => {
    const userId = localStorage.getItem('userId') || '';
    if (userId) {
      // ✅ 关键修改：对 PUT 请求也添加 userId 到 query 参数
      if (config.method === 'get' || config.method === 'put' || config.method === 'delete') {
        config.params = { ...config.params, userId: userId };
      } else {
        config.data = { ...config.data, userId: userId };
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default instance