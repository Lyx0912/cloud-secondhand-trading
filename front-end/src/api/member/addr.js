import request from '@/utils/request'

/**
 * 查询会员的收获地址列表
 * @returns {AxiosPromise}
 */
export function queryMemberAddr(params) {
  return request({
    url: '/cloud-member/memberAddr/list',
    method: 'get',
    params
  })
}

/**
 * 删除会员的收获地址列表
 * @returns {AxiosPromise}
 */
export function deletes(ids) {
  return request({
    url: '/cloud-member/memberAddr/' + ids,
    method: 'delete'
  })
}

