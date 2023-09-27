import request from '@/utils/request'

/**
 * 分页查询商品
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-order/pay/list',
    method: 'get',
    params
  })
}
