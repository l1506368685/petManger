import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/memberCard/pageList', method: 'get', params })
}

export function add(data) {
  return request({ url: '/memberCard/add', method: 'post', data })
}

export function update(data) {
  return request({ url: '/memberCard/update', method: 'put', data })
}

export function remove(id) {
  return request({ url: '/memberCard/delete', method: 'delete', params: { id } })
}

export function detail(id) {
  return request({ url: '/memberCard/detail', method: 'get', params: { id } })
}

export function buy(data) {
  return request({ url: '/memberCard/buy', method: 'post', data })
}

export function recharge(data) {
  return request({ url: '/memberCard/recharge', method: 'post', data })
}

export function consume(data) {
  return request({ url: '/memberCard/consume', method: 'post', data })
}
