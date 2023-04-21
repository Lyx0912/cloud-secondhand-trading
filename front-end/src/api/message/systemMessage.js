import request from '@/utils/request'

/**
 * 系统消息
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-message/systemMessage/list',
    method: 'get',
    params
  })
}

/**
 * 系统消息
 * @returns {AxiosPromise}
 */
export function info(id) {
  return request({
    url: '/cloud-message/systemMessage/' + id,
    method: 'get'
  })
}

/**
 * 更新系统消息
 * @returns {AxiosPromise}
 */
export function update(data) {
  return request({
    url: '/cloud-message/systemMessage',
    method: 'put',
    data
  })
}

/**
 * 添加系统消息
 * @returns {AxiosPromise}
 */
export function save(data) {
  return request({
    url: '/cloud-message/systemMessage',
    method: 'post',
    data
  })
}

/**
 * 删除系统消息
 * @returns {AxiosPromise}
 */
export function deletes(ids) {
  return request({
    url: '/cloud-message/systemMessage/' + ids,
    method: 'delete',
  })
}
