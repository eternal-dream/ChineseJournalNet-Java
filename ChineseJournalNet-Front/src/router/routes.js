import layoutHeaderAside from '@/layout/header-aside'

// 由于懒加载页面太多的话会造成webpack热更新太慢，所以开发环境不使用懒加载，只有生产环境使用懒加载
const _import = require('@/libs/util.import.' + process.env.NODE_ENV)

/**
 * 在主框架内显示
 */
const frameIn = [
  {
    path: '/admin',
    redirect: { name: 'index' },
    component: layoutHeaderAside,
    children: [
      // 首页
      {
        path: '/index',
        name: 'index',
        meta: {
          title: '首页',
          auth: true
        },
        component: _import('system/index')
      },
      // 系统 前端日志
      {
        path: 'log',
        name: 'log',
        meta: {
          title: '前端日志',
          auth: true
        },
        component: _import('system/log')
      },
      // 个人中心
      // {
      //   path: '/admin/profile',
      //   name: 'Profile',
      //   meta: {
      //     title: '个人中心',
      //     auth: true
      //   },
      //   component: _import('system/management/adminUser/profile/index.vue')
      // }
    ]
  }
]

/**
 * 在主框架之外显示(前台页面路由)
 */
const frameOut = [
  //登录
  {
    path: '/admin/login',
    name: 'AdminLogin',
    meta: {
      auth: false
    },
    component: _import('system/login/page.vue')
  },
  // 404
  {
    path: '/',
    redirect: { name: 'QikanIndex' }
  },
  {
    path: '/404',
    name: '404',
    meta: {
      auth: false
    },
    component: _import('system/error/404')
  },
  // 刷新页面 必须保留
  {
    path: '/refresh',
    name: 'refresh',
    meta: {
      auth: false
    },
    component: _import('system/function/refresh')
  },
  // 页面重定向 必须保留
  {
    path: '/redirect/:route*',
    name: 'redirect',
    meta: {
      auth: false
    },
    component: _import('system/function/redirect')
  },
  {
    path: '/qikan/index',
    name: 'QikanIndex',
    meta: {
      auth: false,
      front: true
    },
    component: _import('front/index')
  },
  {
    path: '/search/index',
    name: 'SearchIndex',
    meta: {
      auth: false,
      front: true
    },
    component: _import('front/search/index')
  },
  {
    path: '/detail/index',
    name: 'detail',
    meta: {
      auth: false,
      front: true,
      title:null
    },
    component: _import('front/detail/index')
  },
  {
    path: '/journalsDetail/index',
    name: 'JournalsIndex',
    meta: {
      auth: false,
      front: true,
      title:'期刊详情'
    },
    component: _import('front/journalsDetail/index')
  },
  {
    path: '/search/advance',
    name: 'SearchAdvance',
    meta: {
      auth: false,
      front: true
    },
    component: _import('front/search/advance')
  },
  //期刊导航页
  {
    path: '/journal/index',
    name: 'JournalIndex',
    meta: {
      title:'期刊导航',
      auth: false,
      front: true
    },
    component: _import('front/journal/guid/index.vue')
  },
  //文献题录导出
  {
    path: '/export/index',
    name: 'ExportIndex',
    meta: {
      title:'题录导出',
      auth: false,
      front: true
    },
    component: _import('front/export/index.vue')
  },
  // 检索帮助
  {
    path: '/search/help',
    name: 'SearchHelp',
    meta: {
      auth: false,
      front: true
    },
    component: _import('front/search/help.vue')
  },
  {
    path: '/help/index',
    name: 'helpIndex',
    meta: {
      auth: false,
      front: true,
      title:'使用帮助'
    },
    component: _import('front/help/index.vue')
  }
]

// 导出需要显示菜单的
export const frameInRoutes = frameIn

// 重新组织后导出
export const commonRouter = [
  ...frameIn,
  ...frameOut
  // ...errorPage
]
