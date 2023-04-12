import request from '@/utils/request'

/**
 * 查询分类
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-goods/category/list',
    method: 'get',
    params
  })
}

/**
 * 删除分类
 * @returns {AxiosPromise}
 */
export function remove(id) {
  return request({
    url: '/cloud-goods/category/' + id,
    method: 'delete'
  })
}

/**
 * 添加分类
 * @returns {AxiosPromise}
 */
export function save(data) {
  return request({
    url: '/cloud-goods/category/',
    method: 'put',
    data
  })
}

/**
 * 批量删除
 * @returns {AxiosPromise}
 */
export function batchDelete(ids) {
  return request({
    url: '/cloud-goods/category/batchDelete/' + ids,
    method: 'delete'
  })
}
