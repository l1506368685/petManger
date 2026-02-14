import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/petType/pageList', method: 'get', params })
}

export function listAll() {
  return request({ url: '/petType/listAll', method: 'get' })
}

export function add(data) {
  return request({ url: '/petType/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/petType/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/petType/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/petType/detail', method: 'get', params: { id } })
}
