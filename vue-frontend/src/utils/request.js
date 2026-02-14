import axios from 'axios'
import { Notification } from 'element-ui'
import store from '@/store'
import router from '@/router'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

function showError(title, message) {
  Notification.error({
    title: title || '提示',
    message: message || '请求失败',
    position: 'top-right',
    duration: 4500
  })
}

service.interceptors.request.use(
  config => {
    const token = store.state.token
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 401) {
      showError('登录已过期', '请重新登录')
      store.commit('logout')
      router.push('/login')
      return Promise.reject(new Error('未授权'))
    }
    if (res.code !== 200) {
      const msg = res.msg || '请求失败'
      const displayMsg = msg.indexOf('库存不足') !== -1 ? msg + '。请先在【库存管理】中为该商品补货后再操作。' : msg
      showError('操作失败', displayMsg)
      return Promise.reject(new Error(msg))
    }
    return res
  },
  error => {
    showError('网络错误', error.message || '请检查网络后重试')
    return Promise.reject(error)
  }
)

export default service
