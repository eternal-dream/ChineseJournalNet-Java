/**
 * 管理员APi
 */

import { SYSTEM_API_PREFIX } from '@api/common/commonUrl'
import { axiosOptions, axiosRequest, publicKey } from '@api/service'

const baseUrl = SYSTEM_API_PREFIX + 'admin/'

/**
 * 登录方法
 * @param name
 * @param passWord
 * @param code
 * @returns {Promise<unknown>}
 */
export function login (userName, password, code) {
  const sm2 = require('sm-crypto').sm2
  password = '04' + sm2.doEncrypt(password, publicKey, 1)
  const data = {
    userName,
    password,
    code
  }
  return axiosRequest(baseUrl + '/login', data)
}

/**
 * 退出登录
 * @returns {Promise | Promise<unknown>}
 */
export function logoutRequest () {
  return axiosRequest(baseUrl + 'logout')
}

/**
 * 获取登录管理员的角色，权限和账户信息
 */
export function getAdminInfo () {
  return axiosRequest(baseUrl + 'getAdminInfo', null, axiosOptions('get'))
}

/**
 * 新增编辑管理员账户
 * @param user
 */
export function addOrModifyAdminUser (user) {
  return axiosRequest(baseUrl + 'addOrModifyAdminUser', user)
}

/**
 * 管理员修改自己的密码
 * @param param
 * @returns {Promise | Promise<unknown>}
 */
export function updateMyselfPassword (param) {
  return axiosRequest(baseUrl + 'updateMyselfPassword', param)
}

/**
 * 更新自身基础信息，不处理密码和权限关系
 * @param user
 * @returns {Promise | Promise<unknown>}
 */
export function updateInfoWithOutOther (user) {
  return axiosRequest(baseUrl + 'updateInfoWithOutOther', user)
}
