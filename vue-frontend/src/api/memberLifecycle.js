import request from '@/utils/request'

// 等级规则
export function levelRulePageList(params) {
  return request({ url: '/memberLevelRule/pageList', method: 'get', params })
}
export function levelRuleListAll() {
  return request({ url: '/memberLevelRule/listAll', method: 'get' })
}
export function levelRuleAdd(data) {
  return request({ url: '/memberLevelRule/add', method: 'post', data })
}
export function levelRuleUpdate(data) {
  return request({ url: '/memberLevelRule/update', method: 'put', data })
}
export function levelRuleRemove(id) {
  return request({ url: '/memberLevelRule/delete', method: 'delete', params: { id } })
}
export function levelRuleDetail(id) {
  return request({ url: '/memberLevelRule/detail', method: 'get', params: { id } })
}

// 生命周期配置
export function configList() {
  return request({ url: '/memberLifecycleConfig/list', method: 'get' })
}
export function configSave(data) {
  return request({ url: '/memberLifecycleConfig/save', method: 'post', data })
}

// 流失/沉睡预警列表
export function warningPageList(params) {
  return request({ url: '/memberLifecycleWarning/pageList', method: 'get', params })
}

// 手动执行任务
export function runLevelCalc() {
  return request({ url: '/memberLifecycle/runLevelCalc', method: 'post' })
}
export function runChurnScan() {
  return request({ url: '/memberLifecycle/runChurnScan', method: 'post' })
}
export function runSleepingScan() {
  return request({ url: '/memberLifecycle/runSleepingScan', method: 'post' })
}
