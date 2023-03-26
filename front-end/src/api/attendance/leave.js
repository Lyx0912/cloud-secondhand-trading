import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/cloud-attendance/leave/list',
    method: 'get',
    params
  })
}

/**
 * 请假申请
 * @returns {AxiosPromise}
 */
export function saveLeave(data) {
  return request({
    url: '/cloud-attendance/leave',
    method: 'post',
    data: data
  })
}

/**
 * 流程进度
 * @returns {AxiosPromise}
 */
export function deleteLeave(leaveId, instantId) {
  return request({
    url: '/cloud-attendance/leave/',
    method: 'delete',
    params: {
      id: leaveId,
      instantId: instantId
    }
  })
}

/**
 * 请假信息
 * @returns {AxiosPromise}
 */
export function leaveInfo(id) {
  return request({
    url: '/cloud-attendance/leave/info/' + id,
    method: 'get'
  })
}
