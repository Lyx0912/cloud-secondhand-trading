import request from '@/utils/request'

// 查询出勤信息列表
export function listOverwork(query) {
  return request({
    url: '/cloud-attendance/overwork',
    method: 'get',
    params: query
  })
}

// 查询出勤信息详细
export function getOverwork(id) {
  return request({
    url: '/cloud-attendance/overwork/info/' + id,
    method: 'get'
  })
}

// 新增出勤信息
export function addOverwork(data) {
  return request({
    url: '/cloud-attendance/overwork',
    method: 'post',
    data: data
  })
}

// 修改出勤信息
export function updateOverwork(data) {
  return request({
    url: '/cloud-attendance/overwork',
    method: 'put',
    data: data
  })
}

// 删除出勤信息
export function delOverwork(id) {
  return request({
    url: '/cloud-attendance/overwork/' + id,
    method: 'delete'
  })
}
