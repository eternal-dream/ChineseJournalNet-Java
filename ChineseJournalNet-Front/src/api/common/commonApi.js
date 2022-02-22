/**
 * 通用请求接口，包括上传，获取验证码等
 */

import { axiosOptions, axiosRequest, commonContentType } from '@api/service'

const baseUrl = '/common/'

/**
 * 获取验证码
 * @returns {Promise<unknown>}
 */
export function getCodeImg () {
  const option = axiosOptions('get')
  return axiosRequest(baseUrl + '/verifyCode', null, option)
}

/**
 * 根据文件路径下载文件
 * @param path
 */
export function downloadByPath (path) {
  return axiosRequest(baseUrl + 'downloadByPath', { path: path }, axiosOptions(null, null, 'blob'))
}

/**
 * 根据类名获取枚举值
 * @param {*} name 类名
 * @returns {value:'xx',label:'xx'}
 */
export async function getCommonEnum (name) {
  let enums = null
  await axiosRequest(baseUrl + 'getCommonEnum', { name: name }, { method: 'get' })
    .then(async (res) => {
      if (!res.success) {
        return
      }
      enums = res.enums
    })
  return enums
}
