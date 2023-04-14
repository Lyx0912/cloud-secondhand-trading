import request from '@/utils/request'

/**
 * 查询轮播图
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-goods/banner/list',
    method: 'get',
    params
  })
}

/**
 * 查询轮播图
 * @returns {AxiosPromise}
 */
export function info(id) {
  return request({
    url: '/cloud-goods/banner/' + id,
    method: 'get'
  })
}

/**
 * 更新轮播图
 * @returns {AxiosPromise}
 */
export function update(id, data) {
  return request({
    url: '/cloud-goods/banner/' + id,
    method: 'put',
    data
  })
}
