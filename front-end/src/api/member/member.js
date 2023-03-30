import request from '@/utils/request'

/**
 * 会员列表
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-member/member/list',
    method: 'get',
    params
  })
}

/**
 * 删除会员
 * @returns {AxiosPromise}
 */
export function remove(ids) {
  return request({
    url: '/cloud-member/member/' + ids,
    method: 'delete'
  })
}

/**
 * 会员详情
 * @returns {AxiosPromise}
 */
export function memberDetail(id) {
  return request({
    url: '/cloud-member/member/' + id,
    method: 'get'
  })
}

