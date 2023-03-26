import request from '@/utils/request'

/**
 * 权限列表
 * @returns {AxiosPromise}
 */
export function permissionList(menuId) {
  return request({
    url: `/cloud-admin/permission/list/${menuId}`,
    method: 'get'
  })
}

/**
 * 权限列表
 * @returns {AxiosPromise}
 */
export function servicesSelect() {
  return request({
    url: `/cloud-admin/permission/services`,
    method: 'get'
  })
}

/**
 * 更新权限
 * @returns {AxiosPromise}
 */
export function updatePermission(data) {
  return request({
    url: `/cloud-admin/permission`,
    method: 'put',
    data
  })
}

/**
 * 新增权限
 * @returns {AxiosPromise}
 */
export function createPermission(data) {
  return request({
    url: `/cloud-admin/permission`,
    method: 'post',
    data
  })
}

/**
 * 删除权限
 * @returns {AxiosPromise}
 */
export function deletePermissions(id) {
  return request({
    url: `/cloud-admin/permission/${id}`,
    method: 'delete'
  })
}

/**
 * 查询角色绑定的权限
 * @returns {AxiosPromise}
 */
export function rolePermissions(roleId) {
  return request({
    url: `/cloud-admin/permission/role/${roleId}`,
    method: 'get'
  })
}

/**
 * 保存角色绑定的权限
 * @returns {AxiosPromise}
 */
export function saveRolePermissions(roleId, data) {
  return request({
    url: `/cloud-admin/permission/role/${roleId}`,
    method: 'put',
    data
  })
}
