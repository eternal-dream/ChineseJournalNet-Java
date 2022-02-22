<template>
    <div>
        <el-dialog title='用户登录' :visible.sync="visible" width="25%" :before-close="close" :close-on-click-modal="false"
                   :inline="true" :modal-append-to-body="false" :append-to-body="true">
            <el-form ref="loginForm" :model="userInfo" >
                <el-form-item label="用户名" prop="userName" label-width="80px"
                    :rules="{ required: true, message: '请输入用户名!', trigger: 'blur'}">
                    <el-input v-model="userInfo.userName" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="密码" prop="password" label-width="80px"
                    :rules="{ required: true, message: '请输入密码!', trigger: 'blur'}">
                    <el-input type="password" v-model="userInfo.password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item prop="code" label="验证码" label-width="80px"
                :rules="{ required: true, message: '请输入验证码!', trigger: 'blur'}">
                  <el-input
                    type="text"
                    v-model="userInfo.code"
                    placeholder="验证码">
                    <template slot="append">
                      <img :src="codeUrl" @click="getCode" class="login-code"/>
                    </template>
                  </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary"  @click="submit" style="width:48%">登录</el-button>
                <el-button type="success"  @click="tryIpLogin" style="width:48%">Ip登录</el-button>
            </span>

        </el-dialog>
    </div>
</template>

<script>
import { getCodeImg } from '@api/common/commonApi'
import axios from 'axios'
import { mapState } from 'vuex'
export default {
  data () {
    return {
      visible: false,
      userInfo: {},
      codeUrl: ''
    }
  },
  created () {
  },
  computed: {
    ...mapState('front/user', [
      'info'
    ])
  },
  methods: {
    show () {
      this.visible = true
      this.getCode()
    },
    close () {
      this.$refs.loginForm.resetFields()
      this.visible = false
    },
    getCode () {
      getCodeImg().then(res => {
        this.codeUrl = 'data:image/gif;base64,' + res.img
      })
    },
    tryIpLogin () {
      this.$store.dispatch('front/user/tryIpLogin')
        .then(res => {
          if (!res.success) {
            this.$message.error(res.msg)
            return
          }
          this.close()
        })
    },
    submit () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$store.dispatch('front/user/login', this.userInfo)
            .then(res => {
              if (!res.success) {
                this.$message.error(res.msg)
                return
              }
              this.close()
            })
        }
      })
    }
  }
}

</script>
