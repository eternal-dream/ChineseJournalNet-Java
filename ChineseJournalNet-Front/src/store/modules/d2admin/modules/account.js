import { Message, MessageBox } from 'element-ui'
import util from '@/libs/util.js'
import router from '@/router'
import { getAdminInfo, login, logoutRequest } from '@api/system/admin.api'
import store from '@/store'

export default {
  namespaced: true,
  actions: {
    /**
     * @description 登录
     * @param {Object} context
     * @param {Object} payload username {String} 用户账号
     * @param {Object} payload password {String} 密码
     * @param {Object} payload route {Object} 登录成功后定向的路由对象 任何 vue-router 支持的格式
     */

    Login ({ dispatch }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo.userName, userInfo.password, userInfo.code).then(res => {
          if (res.success) {
            util.cookies.set(util.cookies.adminTokenSign, res.token)
            dispatch('d2admin/user/set', res.admin, { root: true })
            resolve()
          } else {
            return Promise.reject(res.msg)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    /**
     * @description 注销用户并返回登录页面
     * @param {Object} context
     * @param {Object} payload confirm {Boolean} 是否需要确认
     */
    Logout ({ commit, dispatch }, { confirm = false } = {}) {
      /**
       * @description 注销
       */
      function logout () {
        logoutRequest().then(res => {
          if (res.success) {
            // 删除cookie
            util.cookies.remove(util.cookies.adminTokenSign)
            // 清空 vuex 用户信息
            dispatch('d2admin/user/set', {}, { root: true })
            dispatch('d2admin/user/setRoles', [], { root: true })
            dispatch('d2admin/user/setPermissions', [], { root: true })
            // 清空路由菜单
            store.state.d2admin.routes.accessedRoutes = []
            // 跳转路由
            router.push({ name: 'AdminLogin' })
          }
        })
      }
      // 判断是否需要确认
      if (confirm) {
        commit('d2admin/gray/set', true, { root: true })
        MessageBox.confirm('确定要注销当前用户吗', '注销用户', { type: 'warning' })
          .then(() => {
            commit('d2admin/gray/set', false, { root: true })
            logout()
          })
          .catch(() => {
            commit('d2admin/gray/set', false, { root: true })
            Message({ message: '取消注销操作' })
          })
      } else {
        logout()
      }
    },

    // 前端 登出
    FedLogOut ({ dispatch }) {
      return new Promise(resolve => {
        dispatch('d2admin/user/set', {}, { root: true })
        dispatch('d2admin/user/setRoles', [], { root: true })
        dispatch('d2admin/user/setPermissions', [], { root: true })
        util.cookies.remove(util.cookies.adminTokenSign)
        resolve()
      })
    },
    /**
     * 获取登录管理员的账户信息，角色，权限等信息
     * @param dispatch
     * @constructor
     */
    AdminInfo ({ dispatch }) {
      return new Promise((resolve, reject) => {
        getAdminInfo().then(res => {
          if (res.success) {
            dispatch('d2admin/user/set', res.admin, { root: true })
            dispatch('d2admin/user/setRoles', res.roles, { root: true })
            dispatch('d2admin/user/setPermissions', res.per, { root: true })
            resolve()
          } else {
            return Promise.reject(res.msg)
          }
        })
      }).catch(error => {
        reject(error)
      })
    },
    /**
     * @description 用户登录后从持久化数据加载一系列的设置
     * @param {Object} context
     */
    async load ({ dispatch }) {
      // 加载用户名
      await dispatch('d2admin/user/load', null, { root: true })
      // 加载主题
      await dispatch('d2admin/theme/load', null, { root: true })
      // 加载页面过渡效果设置
      await dispatch('d2admin/transition/load', null, { root: true })
      // 持久化数据加载上次退出时的多页列表
      await dispatch('d2admin/page/openedLoad', null, { root: true })
      // 持久化数据加载侧边栏配置
      await dispatch('d2admin/menu/asideLoad', null, { root: true })
      // 持久化数据加载全局尺寸
      await dispatch('d2admin/size/load', null, { root: true })
      // 持久化数据加载颜色设置
      await dispatch('d2admin/color/load', null, { root: true })
      // 从服务器获取枚举值
      await dispatch('common/enums/load', null, { root: true })
    }
  }
}
