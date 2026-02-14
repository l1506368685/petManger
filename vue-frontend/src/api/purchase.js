import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/purchase/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/purchase/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/purchase/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/purchase/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/purchase/detail', method: 'get', params: { id } })
}

export function expiryAlertList(params) {
  return request({ url: '/purchase/expiryAlertList', method: 'get', params })
}
