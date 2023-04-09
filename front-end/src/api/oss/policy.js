import request from '@/utils/request'

/**
 * 查询会员的收获地址列表
 * @returns {AxiosPromise}
 */
export function policy() {
  return request({
    url: '/cloud-oss/oss/policy',
    method: 'get'
  })
}
