export default {
  namespaced: true,
  state: {
    // 用户信息
    info: {},
    roles:[],
    permissions:[]
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
        dbName: 'sys',
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
        dbName: 'sys',
        path: 'user.info',
        defaultValue: {},
        user: true
      }, { root: true })
    },
    /**
     * 设置用户角色
     * @param state
     * @param dispatch
     * @param info
     * @returns {Promise<void>}
     */
    async setRoles ({state,dispatch},info) {
      state.roles = info
    },
    /**
     * 设置用户权限
     * @param state
     * @param dispatch
     * @param info
     * @returns {Promise<void>}
     */
    async setPermissions({state,dispatch},info) {
      state.permissions = info
    }
  }
}
