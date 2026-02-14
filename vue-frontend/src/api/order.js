import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/order/pageList', method: 'get', params })
}

export function detail(id) {
  return request({ url: '/order/detail', method: 'get', params: { id } })
}

export function add(data) {
  return request({ url: '/order/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/order/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/order/delete', method: 'delete', params: { id } })
}

export function updateStatus(id, status) {
  return request({ url: '/order/updateStatus', method: 'put', params: { id, status } })
}
