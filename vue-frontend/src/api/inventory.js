import request from '@/utils/request'

export function list(params) {
  return request({ url: '/inventory/list', method: 'get', params })
}

export function updateStock(data) {
  return request({ url: '/inventory/updateStock', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/inventory/delete', method: 'delete', params: { id } })
}

export function flowPageList(params) {
  return request({ url: '/inventory/flow/pageList', method: 'get', params })
}

export function alertList(params) {
  return request({ url: '/inventory/alertList', method: 'get', params })
}
