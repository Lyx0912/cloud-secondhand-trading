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
export function update(data) {
  return request({
    url: '/cloud-goods/banner',
    method: 'put',
    data
  })
}

/**
 * 更新轮播图
 * @returns {AxiosPromise}
 */
export function deletes(ids) {
  return request({
    url: '/cloud-goods/banner/' + ids,
    method: 'delete'
  })
}

/**
 * 添加轮播图
 * @returns {AxiosPromise}
 */
export function save(data) {
  return request({
    url: '/cloud-goods/banner',
    method: 'post',
    data
  })
}

/**
 * 更新激活状态
 * @returns {AxiosPromise}
 */
export function changeStatus(id, isActive) {
  return request({
    url: '/cloud-goods/banner',
    method: 'patch',
    params: {
      id: id,
      status: isActive
    }
  })
}
