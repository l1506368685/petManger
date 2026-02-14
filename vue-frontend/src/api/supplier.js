import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/supplier/pageList', method: 'get', params })
}

export function listAll(params) {
  return request({ url: '/supplier/listAll', method: 'get', params })
}

export function add(data) {
  return request({ url: '/supplier/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/supplier/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/supplier/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/supplier/detail', method: 'get', params: { id } })
}
