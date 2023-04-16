import request from '@/utils/request'

/**
 * 查询商品推荐列表
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-goods/recommend/list',
    method: 'get',
    params
  })
}

/**
 * 同步商品推荐信息
 * @returns {AxiosPromise}
 */
export function corret(id) {
  return request({
    url: '/cloud-goods/recommend/corret/' + id,
    method: 'put'
  })
}

/**
 * 删除
 * @returns {AxiosPromise}
 */
export function deletes(ids) {
  return request({
    url: '/cloud-goods/recommend/' + ids,
    method: 'delete'
  })
}

/**
 * 设置推荐的商品
 * @returns {AxiosPromise}
 */
export function setRecommed(ids) {
  return request({
    url: '/cloud-goods/recommend/' + ids,
    method: 'post'
  })
}

