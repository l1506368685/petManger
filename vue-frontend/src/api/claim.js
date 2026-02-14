import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/claim/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/claim/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/claim/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/claim/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/claim/detail', method: 'get', params: { id } })
}
