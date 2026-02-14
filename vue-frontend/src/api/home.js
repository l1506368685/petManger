import request from '@/utils/request'

export function getStatistics() {
  return request({ url: '/home/statistics', method: 'get' })
}
