import { axiosRequest, axiosOptions } from '@/api/service'
const sm2 = require('sm-crypto').sm2
const publicKey = require('@api/service').publicKey
export default {
  namespaced: true,
  state: {
    // 用户信息
    info: {},
    // 标记是否已退出
    logoutSign: false,
    // 标记是否登录
    loginSign: false
  },
  actions: {
    /**
       * @description 设置用户数据
       * @param {Object} context
       * @param {*} info info
       */
    async set ({ state, dispatch }, info) {
      // store 赋值
      state.info = info
      // 持久化
      await dispatch('d2admin/db/set', {
        dbName: 'front',
        path: 'user.info',
        value: info,
        user: true
      }, { root: true })
    },
    /**
       * @description 从数据库取用户数据
       * @param {Object} context
       */
    async load ({ state, dispatch }) {
      // store 赋值
      state.info = await dispatch('d2admin/db/get', {
        dbName: 'front',
        path: 'user.info',
        defaultValue: {},
        user: true
      }, { root: true })
    },
    /**
       * @description 尝试Ip登录
       * @param {Object} context
       */
    async tryIpLogin ({ state, dispatch }) {
      return new Promise(resolve => {
        axiosRequest('/view/user/tryIpLogin', {}, axiosOptions('post'))
          .then(res => {
            dispatch('set', res.user)
            state.loginSign = true
            resolve(res)
          })
      })
    },
    /**
       * @description 退出登录
       * @param {Object} context
       */
    async logout ({ state, dispatch }) {
      axiosRequest('/view/user/logout', {}, axiosOptions('post'))
        .then(res => {
          dispatch('set', {})
          state.logoutSign = true
        })
    },
    /**
     * 通过用户名密码登录
     * @param {*} param0
     * @param {*} userInfo 用户信息，包括userName,password,code
     */
    async login ({ state, dispatch }, userInfo) {
      const user = JSON.parse(JSON.stringify(userInfo))
      user.password = '04' + sm2.doEncrypt(userInfo.password, publicKey, 1)
      return new Promise(resolve => {
        axiosRequest('/view/user/login', user, axiosOptions('post'))
          .then(res => {
            dispatch('set', res.user)
            resolve(res)
          })
      })
    }
  }
}
