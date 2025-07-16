import axios from 'axios'

const service = axios.create({
  baseURL: 'http://localhost:8081',
  timeout: 8000
})

export default service