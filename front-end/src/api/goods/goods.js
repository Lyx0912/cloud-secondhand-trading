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
export function changeStatus(goodsIds, isOnSell) {
  return request({
    url: '/cloud-goods/goods/' + isOnSell,
    method: 'patch',
    data: goodsIds
  })
}

/**
 * 获取商品的详细信息
 * @returns {AxiosPromise}
 */
export function info(goodsId) {
  return request({
    url: '/cloud-goods/goods/' + goodsId,
    method: 'get'
  })
}

/**
 * 更新商品信息
 * @returns {AxiosPromise}
 */
export function update(data) {
  return request({
    url: '/cloud-goods/goods/',
    method: 'put',
    data
  })
}

/**
 * 删除信息
 * @returns {AxiosPromise}
 */
export function deletes(ids) {
  return request({
    url: '/cloud-goods/goods/' + ids,
    method: 'delete'
  })
}
