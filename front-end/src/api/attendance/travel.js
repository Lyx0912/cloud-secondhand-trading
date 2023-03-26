import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/cloud-attendance/travel',
    method: 'get',
    params
  })
}

export function saveTravel(data) {
  return request({
    url: '/cloud-attendance/travel',
    method: 'post',
    data: data
  })
}

export function deleteTravel(id,instance) {
  return request({
    url: '/cloud-attendance/travel',
    method: 'delete',
    params: {
      id: id,
      instantId: instance
    }
  })
}

/**
 * 出差信息
 * @returns {AxiosPromise}
 */
export function travelInfo(id) {
  return request({
    url: '/cloud-attendance/travel/info/' + id,
    method: 'get'
  })
}

