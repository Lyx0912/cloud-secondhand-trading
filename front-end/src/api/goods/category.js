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
