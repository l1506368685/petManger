import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/vaccine/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/vaccine/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/vaccine/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/vaccine/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/vaccine/detail', method: 'get', params: { id } })
}
