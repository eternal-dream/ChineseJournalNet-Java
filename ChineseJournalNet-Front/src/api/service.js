import axios from 'axios'
import { MessageBox } from 'element-ui'
import store from '@/store'
import Qs from 'qs'
import util from '@/libs/util'
import router from '@/router'
import { FRONT_API_PREFIX } from '@api/common/commonUrl'

axios.defaults.withCredentials = true// 允许跨域携带cookie
axios.defaults.headers['X-Requested-With'] = 'XMLHttpRequest'
axios.defaults.baseURL = process.env.VUE_APP_API// 添加请求基础路径
// axios.defaults.timeout = 10000;

export const publicKey = '040289a3360e5acefc9d7158e06c36294095a40a7cdbe1717c8c442ef29ec97a59403a9c324' +
  'ae12f6a1c9b5d3cb457801221cbfa53c3e2f520079626fd930a2452'

/**
 * 常用的几种contentType
 * @type {{json: string, formData: string}}
 */
export const commonContentType = {
  json: 'application/json',
  formData: 'multipart/form-data'
}

/*
 *  todo 配置请求拦截器
 */
axios.interceptors.request.use(
  config => {
    // 后续的加密写在这里
    const user = store.state.front.user.info
    config.headers.user_id = user ? user.id : null

    // 携带一个sessionId与后端shiro交互
    if (config.url.indexOf(FRONT_API_PREFIX.substring(1)) == -1) {
      const session = sessionStorage.getItem('session')
      if (session) {
        config.headers.sessionId = session
      }
    }
    // 携带token
    if (util.cookies.get(util.cookies.adminTokenSign)) {
      config.headers.Authorization = 'Bearer ' + util.cookies.get(util.cookies.adminTokenSign)
    }
    return config
  },

  error => {
    return Promise.reject(error.response)
  })

/*
 * todo 响应拦截器
 */
axios.interceptors.response.use(
  function (response) {
    // IE 8-9
    if (response.data == null && response.config.responseType === 'json' && response.request.responseText != null) {
      try {
        // eslint-disable-next-line no-param-reassign
        response.data = JSON.parse(response.request.responseText)
      } catch (e) {
        // ignored
      }
    }
    // 响应后的统一处理处理
    return response
  },
  function (error) {
    // 响应错误的情况，后续的所有拦截写在这里
    if (error) {
      const res = error.response.data
      if (res.status == 4011) {
        MessageBox.alert('登录状态已过期，请重新登录后再试!', '系统提示', {
          type: 'warning'
        }
        ).then(() => {
          location.href = '/qikan/index'
          store.dispatch('front/user/set', {})
        })
      }
      if (res.status == 401 && res.error == 'Unauthorized') {
        MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
        ).then(() => {
          store.dispatch('d2admin/account/FedLogOut').then(() => {
            location.href = '/admin/login?redirect=' + router.currentRoute.fullPath
          })
        })
      }
      if (res.status == 618 && res.message == 'sensitive_error') {
        MessageBox.confirm('您的输入存在系统敏感词汇，请重新输入！', '系统提示', {
          confirmButtonText: '重新输入',
          cancelButtonText: '取消',
          type: 'warning'
        }
        ).then(() => {}).catch(() => {})
      }
      if (res.status == 403 && res.message == 'noauth') {
        MessageBox.alert('权限不足!', '系统提示', {
          type: 'warning'
        })
      }
      if (res.status == 500) {
        MessageBox.confirm('服务器异常，请联系管理员', '警告', {
          type: 'error'
        }).then(() => {}).catch(() => {})
      }
      return Promise.reject(error.response)
    }
  }
)

/** '
 * axios配置参数
 * @param method 请求方式 （Get Or Post）
 * @param contentType
 * @param responseType
 * @param isEncryption 是否添加加密标识（默认否，当前时间戳加密后的密文作为加密标识，可用于拦截器验证请求的合法性）
 * @returns {Object}
 */
export const axiosOptions = (method, contentType, responseType, isEncryption) => {
  var object = new Object()
  object.method = method
  object.contentType = contentType
  object.responseType = responseType
  object.isEncryption = isEncryption
  return object
}

/*
  todo 暴露请求对象
 */
export const axiosRequest = (url, postData = {}, options = {}) => {
  return new Promise((resolve, reject) => {
    axios({
      method: (options && options.method) || 'post',
      url: isGetUrl(url, options.method),
      params: isGet(postData, options.method),
      data: postData,
      /* 处理request数据 */
      transformRequest: [function (data) {
        if (options && options.contentType) {
          return JSON.stringify(data)
        } else {
          return Qs.stringify(data, { indices: false })
        }
      }],
      headers: {
        'Content-Type': (options && options.contentType) || 'application/x-www-form-urlencoded;charset=UTF-8',
        'Encrypt-Sign': (options && options.isEncryption) ? addEncryptSign() : ''
      },
      responseType: (options && options.responseType) || 'json'
    }).then((res) => {
      if (options.responseType) {
        resolve(res)
      } else {
        const response = res.data
        resolve(response)
      }
    }).catch((error) => {
      reject(error)
    })
  })
}

function isGet (data, method) {
  if (method && method == 'get') {
    return data
  } else {
    return null
  }
}

function isGetUrl (url, method) {
  if (method && method == 'get') {
    return url + '?_t=' + Date.parse(new Date()) / 1000
  } else {
    return url
  }
}

/**
 * 添加加密签名(逻辑应修改为后台可识别签名正确性,当前逻辑签名任何非法请求添加上都能正常通过)
 * @returns {string | ArrayBuffer | PromiseLike<ArrayBuffer>}
 */
function addEncryptSign () {
  const sign = 'chinesejournal-' + Date.parse(new Date())
  const sm2 = require('sm-crypto').sm2
  return '04' + sm2.doEncrypt(sign, publicKey, 1)
}
