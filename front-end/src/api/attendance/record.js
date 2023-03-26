import request from '@/utils/request'

// 查询考勤信息列表
export function list(query) {
  return request({
    url: '/cloud-attendance/record',
    method: 'get',
    params: query
  })
}

// 查询考勤信息
export function getRecord(id) {
  return request({
    url: '/cloud-attendance/record/info/' + id,
    method: 'get'
  })
}

// 新增考勤信息
export function addRecord(data) {
  return request({
    url: '/cloud-attendance/record',
    method: 'post',
    data: data
  })
}

// 修改考勤信息
export function updateRecord(data) {
  return request({
    url: '/cloud-attendance/record',
    method: 'put',
    data: data
  })
}

// 删除考勤信息
export function delRecord(id) {
  return request({
    url: '/cloud-attendance/record/' + id,
    method: 'delete'
  })
}
