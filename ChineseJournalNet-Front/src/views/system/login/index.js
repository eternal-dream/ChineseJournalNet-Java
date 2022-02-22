import dayjs from 'dayjs'
import { mapActions } from 'vuex'
import localeMixin from '@/locales/mixin.js'
import { getCodeImg } from '@api/common/commonApi'
import { MessageBox } from 'element-ui'
export default {
  mixins: [
    localeMixin
  ],
  data () {
    return {
      codeUrl: undefined,
      timeInterval: null,
      time: dayjs().format('HH:mm:ss'),
      // 表单
      formLogin: {
        userName: undefined,
        password: undefined,
        code: undefined
      },
      // 表单校验
      rules: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }
        ],
        code: [
          {
            required: true,
            message: '请输入验证码',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted () {
    this.getCode()
    this.timeInterval = setInterval(() => {
      this.refreshTime()
    }, 1000)
  },
  beforeDestroy () {
    clearInterval(this.timeInterval)
  },
  methods: {
    ...mapActions('d2admin/account', [
      'Login'
    ]),
    refreshTime () {
      this.time = dayjs().format('HH:mm:ss')
    },
    /**
     * @description 提交表单
     */
    // 提交登录信息
    submit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          // 登录
          // 注意 这里的演示没有传验证码
          // 具体需要传递的数据请自行修改代码
          this.Login(this.formLogin)
            .then(() => {
              this.$router.replace(this.$route.query.redirect || '/index')
            })
            .catch((err) => {
              if (err) {
                MessageBox.confirm(err, '警告', {
                  type: 'error'
                }).then(() => {}).catch(() => {})
              }
              this.getCode()
            })
        } else {
          // 登录表单校验失败
          this.$message.error('表单校验失败，请检查')
        }
      })
    },
    getCode () {
      getCodeImg().then(res => {
        this.codeUrl = 'data:image/gif;base64,' + res.img
        sessionStorage.setItem('session', res.sessionId)
      })
    }
  }
}
