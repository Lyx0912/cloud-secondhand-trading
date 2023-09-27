import request from '@/utils/request'

/**
 * 分页查询商品
 * @returns {AxiosPromise}
 */
export function list() {
  return request({
    url: '/cloud-order/order/dashboard',
    method: 'get'
  })
}
