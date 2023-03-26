import request from '@/utils/request'

/**
 * 菜单列表
 * @returns {AxiosPromise}
 */
export function list() {
  return request({
    url: '/cloud-admin/menu/list',
    method: 'get'
  })
}

/**
 * 菜单详情
 * @returns {AxiosPromise}
 */
export function menuDetail(id) {
  return request({
    url: `/cloud-admin/menu/info/${id}`,
    method: 'get'
  })
}

/**
 * 菜单select
 * @returns {AxiosPromise}
 */
export function menuSelect() {
  return request({
    url: `/cloud-admin/menu/select`,
    method: 'get'
  })
}

/**
 * 更新菜单
 * @returns {AxiosPromise}
 */
export function updateMenu(data) {
  return request({
    url: `/cloud-admin/menu`,
    method: 'put',
    data
  })
}

/**
 * 新增菜单
 * @returns {AxiosPromise}
 */
export function createMenu(data) {
  return request({
    url: `/cloud-admin/menu`,
    method: 'post',
    data
  })
}

/**
 * 删除菜单
 * @returns {AxiosPromise}
 */
export function deleteMenu(menuId) {
  return request({
    url: `/cloud-admin/menu/${menuId}`,
    method: 'delete'
  })
}

/**
 * 更新菜单状态
 * @returns {AxiosPromise}
 */
export function updateMenuStatus(id, status) {
  return request({
    url: `/cloud-admin/menu/${id}/${status}`,
    method: 'patch'
  })
}

/**
 * 查询角色绑定的菜单
 * @returns {AxiosPromise}
 */
export function roleMenus(roleId) {
  return request({
    url: `/cloud-admin/menu/role/${roleId}`,
    method: 'get'
  })
}

/**
 * 保存角色绑定的菜单
 * @returns {AxiosPromise}
 */
export function saveRoleMenus(roleId, data) {
  return request({
    url: `/cloud-admin/menu/role/${roleId}`,
    method: 'put',
    data
  })
}

/**
 * 菜单树
 * @returns {AxiosPromise}
 */
export function menuRoutes() {
  return request({
    url: `/cloud-admin/menu/tree`,
    method: 'get'
  })
}

