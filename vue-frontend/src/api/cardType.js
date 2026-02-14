import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/cardType/pageList', method: 'get', params })
}

export function listAll(params) {
  return request({ url: '/cardType/listAll', method: 'get', params })
}

export function add(data) {
  return request({ url: '/cardType/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/cardType/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/cardType/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/cardType/detail', method: 'get', params: { id } })
}
