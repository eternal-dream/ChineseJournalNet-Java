import {axiosRequest,axiosOptions} from '@/api/service'
const _import = require('@/libs/util.import.' + process.env.NODE_ENV)
export default {
  namespaced: true,
  state: {
    accessedRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.accessedRoutes = routes
    }
  },
  actions: {
    // 生成路由
    generateRoutes ({ commit }) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        axiosRequest('system/resource/getUserResource',null, axiosOptions('get')
        ).then(res => {
          let resources = res.resources
          resources = filterAsyncRouter(resources)
          commit('SET_ROUTES', resources)
          resolve(resources)
        })
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter (asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    let component
    if (route.component) {
      component = loadView(route.component)
    }
    route.title = route.meta ? route.meta.title : route.title || ''
    route.component = component

    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }

    return true
  })
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve)
}
