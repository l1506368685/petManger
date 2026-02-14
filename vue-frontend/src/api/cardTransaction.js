import request from '@/utils/request'

export function pageList(params) {
  return request({ url: '/cardTransaction/pageList', method: 'get', params })
}

export function detail(id) {
  return request({ url: '/cardTransaction/detail', method: 'get', params: { id } })
}
