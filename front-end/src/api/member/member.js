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
