import request from '@/utils/request'

export function login(data) {
  return request({ url: '/admin/login', method: 'post', data })
}

export function pageList(params) {
  return request({ url: '/admin/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/admin/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/admin/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/admin/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/admin/detail', method: 'get', params: { id } })
}

export function resetPwd(id, newPassword) {
  return request({ url: '/admin/resetPwd', method: 'put', params: { id, newPassword } })
}
