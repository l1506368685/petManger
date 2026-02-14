import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/recharge/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/recharge/add', method: 'post', data })
}

export function detail(id) {
  return request({ url: '/recharge/detail', method: 'get', params: { id } })
}
