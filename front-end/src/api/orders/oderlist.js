import request from '@/utils/request'

/**
 * 分页查询商品
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-order/order/list',
    method: 'get',
    params
  })
}
