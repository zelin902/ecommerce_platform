// src/api/address.js
import http from './index'

export const getAddress = () => http.get('/api/address')
  .then(res => {
    console.log('地址接口响应:', res.data); // 打印响应，确认数据格式
    return res.data; // 直接返回数组（后端返回的是数组）
  })
  .catch(err => {
    console.error('地址接口请求失败:', err); // 打印错误详情
    throw err; // 抛出错误，让上层 catch 处理
  });
export const saveAddress  = (data)    => http.post('/api/address', data)
export const delAddress   = (id)      => http.delete(`/api/address/${id}`)
export const setDefault   = (id)      => http.put(`/api/address/default/${id}`)