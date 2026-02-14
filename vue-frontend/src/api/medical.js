import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/medical/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/medical/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/medical/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/medical/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/medical/detail', method: 'get', params: { id } })
}
