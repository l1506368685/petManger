import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/pet/pageList', method: 'get', params })
}

export function listAll() {
  return request({ url: '/pet/listAll', method: 'get' })
}

export function add(data) {
  return request({ url: '/pet/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/pet/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/pet/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/pet/detail', method: 'get', params: { id } })
}
