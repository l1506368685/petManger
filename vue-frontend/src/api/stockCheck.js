import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/stockCheck/pageList', method: 'get', params })
}

export function detail(id) {
  return request({ url: '/stockCheck/detail', method: 'get', params: { id } })
}

export function add(data) {
  return request({ url: '/stockCheck/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/stockCheck/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/stockCheck/delete', method: 'delete', params: { id } })
}

export function confirm(id) {
  return request({ url: '/stockCheck/confirm', method: 'put', params: { id } })
}
