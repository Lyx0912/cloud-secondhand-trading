import request from '@/utils/request'

/**
 * 角色select
 * @returns {AxiosPromise}
 */
export function roleSelect() {
  return request({
    url: '/cloud-admin/role',
    method: 'get'
  })
}

/**
 * 角色分页列表
 */
export function listPage(params) {
  return request({
    url: '/cloud-admin/role/list',
    method: 'get',
    params
  })
}
/**
 * 更新角色信息
 */
export function createRole(data) {
  return request({
    url: '/cloud-admin/role',
    method: 'post',
    data
  })
}
/**
 * 创建角色信息
 */
export function updateRole(data) {
  return request({
    url: '/cloud-admin/role',
    method: 'put',
    data
  })
}
/**
 * 删除角色信息
 */
export function deletes(ids) {
  return request({
    url: `/cloud-admin/role/${ids}`,
    method: 'delete'
  })
}
/**
 * 更新角色状态
 */
export function updateStatus(id, status) {
  return request({
    url: `/cloud-admin/role/${id}/${status}`,
    method: 'patch'
  })
}
