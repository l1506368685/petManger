import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/goods/pageList', method: 'get', params })
}

export function listAll() {
  return request({ url: '/goods/listAll', method: 'get' })
}

export function add(data) {
  return request({ url: '/goods/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/goods/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/goods/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/goods/detail', method: 'get', params: { id } })
}
