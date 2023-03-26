import request from '@/utils/request'

/**
 * 流程列表
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-activiti/task/taskByRoles',
    method: 'get',
    params
  })
}

/**
 * 流程进度
 * @returns {AxiosPromise}
 */
export function processHistory(instantId) {
  return request({
    url: '/cloud-activiti/task/taskHistory/' + instantId,
    method: 'get'
  })
}

/**
 * 流程进度
 * @returns {AxiosPromise}
 */
export function handle(data) {
  return request({
    url: '/cloud-activiti/task/handle',
    method: 'post',
    data
  })
}

