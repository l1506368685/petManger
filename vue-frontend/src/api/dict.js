import request from '@/utils/request'

export function typeList() {
  return request({ url: '/dict/typeList', method: 'get' })
}

export function addType(data) {
  return request({ url: '/dict/type/add', method: 'post', data })
}

export function updateType(data) {
  return request({ url: '/dict/type/update', method: 'put', data })
}

export function deleteType(id) {
  return request({ url: '/dict/type/delete', method: 'delete', params: { id } })
}

export function items(dictType) {
  return request({ url: '/dict/items', method: 'get', params: { dictType } })
}

export function itemPageList(params) {
  return request({ url: '/dict/item/pageList', method: 'get', params })
}

export function addItem(data) {
  return request({ url: '/dict/item/add', method: 'post', data })
}

export function updateItem(data) {
  return request({ url: '/dict/item/update', method: 'put', data })
}

export function removeItem(id) {
  return request({ url: '/dict/item/delete', method: 'delete', params: { id } })
}

export function itemDetail(id) {
  return request({ url: '/dict/item/detail', method: 'get', params: { id } })
}
