import request from '@/utils/request'

/** 日报 date: yyyy-MM-dd */
export function getDailyReport(date) {
  return request({ url: '/report/daily', method: 'get', params: { date } })
}

/** 月报 */
export function getMonthlyReport(year, month) {
  return request({ url: '/report/monthly', method: 'get', params: { year, month } })
}

/** 季报 quarter 1-4 */
export function getQuarterlyReport(year, quarter) {
  return request({ url: '/report/quarterly', method: 'get', params: { year, quarter } })
}

/** 年报 */
export function getYearlyReport(year) {
  return request({ url: '/report/yearly', method: 'get', params: { year } })
}

/** 自定义报表 */
export function getCustomReport(startDate, endDate, compareStart, compareEnd) {
  const params = { startDate, endDate }
  if (compareStart) params.compareStart = compareStart
  if (compareEnd) params.compareEnd = compareEnd
  return request({ url: '/report/custom', method: 'get', params })
}
