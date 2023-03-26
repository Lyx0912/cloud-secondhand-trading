import request from '@/utils/request'

/**
 * 模型列表
 * @returns {AxiosPromise}
 */
export function getModelList(params) {
  return request({
    url: '/cloud-activiti/model/list',
    method: 'get',
    params
  })
}

/**
 * 新增模型
 * @returns {AxiosPromise}
 */
export function saveModelInfo(data) {
  return request({
    url: '/cloud-activiti/model',
    method: 'post',
    data
  })
}

/**
 * 新增模型
 * @returns {AxiosPromise}
 */
export function deleteModel(id) {
  return request({
    url: '/cloud-activiti/model/' + id,
    method: 'delete'
  })
}

/**
 * 获取xml文件
 * @returns {AxiosPromise}
 */
export function loadModelXml(id) {
  return request({
    url: '/cloud-activiti/model//loadXml/' + id,
    method: 'get'
  })
}

/**
 * 获取xml文件
 * @returns {AxiosPromise}
 */
export function updateModel(id, xml) {
  return request({
    url: '/cloud-activiti/model//updateModel/' + id,
    method: 'put',
    data: {
      id: id,
      xml: xml
    }
  })
}

/**
 * 获取xml文件
 * @returns {AxiosPromise}
 */
export function downloadResource(id) {
  return request({
    url: '/cloud-activiti/model/downModel/' + id,
    method: 'get'
  })
}

/**
 * 部署模型
 * @returns {AxiosPromise}
 */
export function deployment(modelId) {
  return request({
    url: '/cloud-activiti/model/deployment/' + modelId,
    method: 'post'
  })
}

