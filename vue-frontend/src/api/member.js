import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/member/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/member/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/member/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/member/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/member/detail', method: 'get', params: { id } })
}
