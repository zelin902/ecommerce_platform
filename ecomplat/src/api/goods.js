// src/api/goods.js
import request from '@/utils/request'

export function addGoods(data) {
  return request.post('api/goods/add', data)
}

export function uploadImage(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}