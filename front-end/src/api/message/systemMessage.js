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
