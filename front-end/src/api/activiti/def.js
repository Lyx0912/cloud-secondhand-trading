import request from '@/utils/request'

/**
 * 流程列表
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-activiti/resource/list',
    method: 'get',
    params
  })
}

/**
 * 删除
 * @returns {AxiosPromise}
 */
export function deleteDef(id) {
  return request({
    url: '/cloud-activiti/resource/' + id,
    method: 'delete'
  })
}

/**
 * 挂起/激活
 * @returns {AxiosPromise}
 */
export function changeStatus(id) {
  return request({
    url: '/cloud-activiti/resource/changeStatus/' + id,
    method: 'patch'
  })
}
