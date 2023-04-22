import request from '@/utils/request'

/**
 * 分页查询商品
 * @returns {AxiosPromise}
 */
export function list(params) {
  return request({
    url: '/cloud-goods/audit/list',
    method: 'get',
    params
  })
}

/**
 * 删除申请记录
 * @returns {AxiosPromise}
 */
export function remove(id) {
  return request({
    url: '/cloud-goods/audit/' + id,
    method: 'delete'
  })
}

/**
 * 修改上架状态
 * @returns {AxiosPromise}
 */
export function changeStatus(goodsId, isOnSell) {
  return request({
    url: '/cloud-goods/audit/' + goodsId,
    method: 'patch',
    params: {
      isOnSell
    }
  })
}

/**
 * 更新商品审核信息
 * @returns {AxiosPromise}
 */
export function update(data) {
  return request({
    url: '/cloud-goods/audit/',
    method: 'put',
    data
  })
}
