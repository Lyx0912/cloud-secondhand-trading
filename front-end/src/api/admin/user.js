import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/cloud-admin/user/list',
    method: 'get',
    params
  })
}

export function updateUserInfo(userId, param) {
  return request({
    url: '/cloud-admin/user/' + userId,
    method: 'put',
    data: param
  })
}

export function updateStatus(userId, status) {
  return request({
    url: '/cloud-admin/user/' + userId,
    method: 'patch',
    params: {
      'status': status
    }
  })
}

export function login(data) {
  return request({
    url: '/cloud-auth/oauth/token',
    method: 'post',
    params: data,
    headers: {
      'Authorization': 'Basic YW1zOmFtcw=='
    }
  })
}

export function getInfo() {
  return request({
    url: '/cloud-admin/user/current',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}

export function userDetail(userId) {
  return request({
    url: '/cloud-admin/user/' + userId,
    method: 'get'
  })
}

/**
 * 更新用户信息
 * @returns {AxiosPromise}
 */
export function deletes(userIds) {
  return request({
    url: `/cloud-admin/user/${userIds.join(',')}`,
    method: 'delete'
  })
}

/**
 * 新增用户信息
 * @returns {AxiosPromise}
 */
export function createUserInfo(data) {
  return request({
    url: `/cloud-admin/user`,
    method: 'post',
    data: data
  })
}
