import request from '@/utils/request'

/**
 * 分页查询商品
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-goods/goods/list',
    method: 'get',
    params
  })
}

/**
 * 更新商品上架状态
 * @returns {AxiosPromise}
 */
export function changeStatus(goodsId, isOnSell) {
  return request({
    url: '/cloud-goods/goods/' + goodsId,
    method: 'patch',
    params: {
      isOnSell
    }
  })
}

/**
 * 更新商品上架状态
 * @returns {AxiosPromise}
 */
export function info(goodsId) {
  return request({
    url: '/cloud-goods/goods/' + goodsId,
    method: 'get'
  })
}
