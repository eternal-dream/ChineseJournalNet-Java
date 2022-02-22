import { axiosOptions, axiosRequest } from '@api/service'
export default {
  namespaced: true,
  state: {
    // 枚举值列表
    value: []
  },
  actions: {
    async set ({ state, dispatch }, info) {
      // store 赋值
      state.info = info
    },
    async get ({ state, dispatch }) {
      // store 赋值
      return state.info
    },
    load ({ state, dispatch }) {
      axiosRequest('common/getCommonEnums', {}, axiosOptions('get')).then(res => {
        if (res.success) {
          state.value = res.enums
        }
      })
    }
  }
}
