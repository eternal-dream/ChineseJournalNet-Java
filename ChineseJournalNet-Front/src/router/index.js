import Vue from 'vue'
import VueRouter from 'vue-router'

// 进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import store from '@/store/index'
import util from '@/libs/util.js'

// 路由数据
import { MessageBox } from 'element-ui'

import routes from './routes'
import layoutHeaderAside from '@/layout/header-aside'
import { commonRouter, frameInRoutes } from '@/router/routes'

// fix vue-router NavigationDuplicated
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return VueRouterPush.call(this, location).catch(err => err)
}
const VueRouterReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace (location) {
  return VueRouterReplace.call(this, location).catch(err => err)
}

Vue.use(VueRouter)
// 导出路由 在 main.js 里使用
const router = new VueRouter({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: commonRouter
})

let matched = false
/**
 * 路由拦截
 * 权限验证
 */
router.beforeEach(async (to, from, next) => {
  if (to.meta.front === true) {
    await store.dispatch('front/user/load')
    await store.dispatch('common/enums/load')
    if (to.meta.auth === true && !store.state.front.user.info.id) {
      MessageBox.alert('请登录后操作', '警告', { type: 'error' })
        .then(res => {
          next({ name: 'QikanIndex' })
        })
    }
    // todo 前台操作不需要auth，但是有些页面要验证前台用户登录，这里后面补充
    const routes = router.options.routes
    const toPath = routes.filter(item => item.path === to.path)
    if (toPath.length <= 0 && to.path !== '/index') {
      next({ name: '404' })
    } else {
      next()
    }
  } else {
    if (to.meta.auth === false) {
      next()
    } else {
      const token = util.cookies.get(util.cookies.adminTokenSign)
      // 请根据自身业务需要修改
      if (token && token !== 'undefined') {
        // 进度条
        NProgress.start()
        // 关闭搜索面板
        store.commit('d2admin/search/set', false)

        store.dispatch('d2admin/routes/generateRoutes')
          .then(async menu => {
            if (to.path === '/index' || to.matched.length === 0) {
              generateRouters(menu, to)
            }
            if (to.matched.length === 0) {
              next({ ...to, replace: true })
            } else {
              generateMenu(menu, to)
              await store.dispatch('d2admin/account/load')
              // 确认已经加载多标签页数据 https://github.com/d2-projects/d2-admin/issues/201
              await store.dispatch('d2admin/page/isLoaded')
              // 确认已经加载组件尺寸设置 https://github.com/d2-projects/d2-admin/issues/198
              await store.dispatch('d2admin/size/isLoaded')
              next()
            }
          })
      } else {
        // 没有登录的时候跳转到登录界面
        // 携带上登陆成功之后需要跳转的页面完整路径
        MessageBox.confirm('请登录后操作', '警告', {
          type: 'error'
        }).then(() => {
          next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
          NProgress.done()
        }).catch(() => {
          next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
          NProgress.done()
        })
      }
    }
  }
})

router.afterEach(to => {
  // 进度条
  NProgress.done()
  // 多页控制 打开新的页面
  store.dispatch('d2admin/page/open', to)
  // 更改标题
  if (to.meta.front === true){
    util.title(to.meta.title)
  }else {
    window.document.title = "维普中文期刊服务平台管理系统"
  }
})

function generateMenu (menu, to) {
  const headerMenu = JSON.parse(JSON.stringify(menu))
  headerMenu.forEach(item => {
    item.children = undefined
  })
  // 设置菜单信息
  store.commit('d2admin/menu/headerSet', headerMenu)
  store.commit('d2admin/search/init', menu)
  store.commit('d2admin/page/init', menu.concat(frameInRoutes))
  // if (!token && !to.meta.isFront) {
  if (!to.path) { // 第一次进入没有获取到路由信息时不生成侧边栏菜单
    return
  }
  // 侧边栏菜单
  let flag = false
  for (let i = 0; i < menu.length; i++) {
    matched = false
    if (menu[i].children && matchRoute(menu[i].children, to)) {
      store.commit('d2admin/menu/asideSet', getNoHiddenMenu(menu[i].children))
      flag = true
    }
  }
  if (!flag) {
    store.commit('d2admin/menu/asideSet', [])
  }
}

function generateRouters (menu) {
  // 添加路由时去掉没有component的菜单
  const accessRoutes = getRouters(menu, [])
  const routes = [{
    path: '/admin',
    name: 'index',
    component: layoutHeaderAside,
    children: accessRoutes
  }]
  router.addRoutes(routes) // 动态添加可访问路由表
  router.addRoutes([{ path: '*', redirect: '/404', hidden: true }])
}

function getNoHiddenMenu (menuList) {
  return menuList.filter(item => item.hidden !== true)
}

/**
 * 从菜单列表获取正确的路由
 * @param {*} menu
 * @param {系统原有路由菜单} routers
 * @returns
 */
function getRouters (menu, routers) {
  menu.map(item => {
    if (!item.component) {
      getRouters(item.children, routers)
    } else {
      routers.push(item)
    }
  })
  return routers
}

// 获取匹配的侧边栏菜单
function matchRoute (routes, to) {
  if (matched) {
    return true
  }
  routes.forEach(route => {
    if (route.path === to.path) {
      matched = true
      return
    }
    if (route.children) {
      matchRoute(route.children, to)
    }
  })
  return matched
}

export default router
