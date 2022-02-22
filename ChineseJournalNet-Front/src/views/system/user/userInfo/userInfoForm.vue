<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="70%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="userInfoForm" :model="userInfo" :rules="rules" size="small">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>账号基本信息>></span>
                    </div>
                    <el-col :span="9">
                        <el-form-item label="用户名" prop="userName" label-width="150px">
                            <el-input v-model="userInfo.userName" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="3">
                         <el-link type="primary" :underline="false" @click="checkUserName">验证账号有效性</el-link>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="password" label-width="150px">
                            <el-input type="password" v-model="userInfo.password"
                            :placeholder="title==='新增用户' ? '密码最少8位，必须包含大写字母、小写字母和数字':'不填表示不修改'" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="登录验证方式" prop="verificationType" label-width="150px"
                            :rules="{required: true, message: '验证方式不能为空!', trigger: 'blur'}">
                            <el-select  v-model="userInfo.verificationType" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in $enums.value.verificationType"
                                :key="item.value"
                                :value="item.value"
                                :label="item.label" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="账户状态" prop="status" label-width="150px">
                            <el-select  v-model="userInfo.status" placeholder="请选择" :clearable="true">
                                <el-option :value="true" label="有效"></el-option>
                                <el-option :value="false" label="无效"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="所属用户组" prop="userGroupId" label-width="150px">
                        <el-select v-model="userInfo.userGroupId" placeholder="请选择" :clearable="true">
                            <el-option v-for="item in allGroups"
                            :key="item.id"
                            :value="item.id"
                            :label="item.name" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                </el-card>
                <el-card class="box-card" style="margin-top:5px" v-if="userInfo.verificationType == 'Ip' || userInfo.verificationType == 'All'">
                    <div slot="header" class="clearfix">
                        <span>用户IP信息>></span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="addIp()">新增IP</el-button>
                    </div>
                    <el-form-item label="" prop="userIpInfos" >
                      <el-col :span="5">开始IP:</el-col>
                      <el-col :span="5">结束IP:</el-col>
                      <el-col :span="6">IP绑定单位:</el-col>
                      <el-col :span="6">IP备注:</el-col>
                      <el-row :gutter="5" style="margin-bottom:5px" v-for="(item,i) in userInfo.userIpInfos" :key="i">
                          <el-col :span="5">
                              <el-input v-model="item.startIp" placeholder="请输入开始IP" />
                          </el-col>
                          <el-col :span="5">
                              <el-input v-model="item.finishIp" placeholder="请输入结束IP"/>
                          </el-col>
                          <el-col :span="6">
                              <el-input v-model="item.unit" placeholder="请输入IP绑定单位"/>
                          </el-col>
                          <el-col :span="6">
                              <el-input v-model="item.remark" placeholder="请输入备注"/>
                          </el-col>
                          <el-col :span="2">
                              <el-button @click="removeIp(i)"><d2-icon name="minus"></d2-icon></el-button>
                          </el-col>
                      </el-row>
                    </el-form-item>
                </el-card>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>文献下载限制规则>></span>
                         <el-button style="float: right; padding: 3px 0" type="text" @click="addRule()">新增规则</el-button>
                    </div>
                    <el-form-item label="" prop="downloadRules" >
                        <el-col :span="10">限制类型：</el-col>
                        <el-col :span="10">最大下载量：</el-col>
                        <el-row :gutter="5" style="margin-bottom:5px" v-for="(item,i) in userInfo.downloadRules" :key="i">
                            <el-col :span="10">
                                <el-select v-model="userInfo.downloadRules[i].rule">
                                    <el-option v-for="item in $enums.value.downloadRule"
                                    :value="item.value"
                                    :label="item.label"
                                    :key="item.value"/>
                                </el-select>
                            </el-col>
                            <el-col :span="10">
                                <el-input-number :step="1" :min="0" v-model="userInfo.downloadRules[i].limit" />
                            </el-col>
                            <el-col :span="4">
                                <el-button @click="removeRule(i)"><d2-icon name="minus"></d2-icon></el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-card>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>联系信息>></span>
                    </div>
                    <el-col :span="12">
                        <el-form-item label="联系人" prop="linkMan" label-width="150px">
                            <el-input v-model="userInfo.linkMan" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所在单位" prop="unit" label-width="150px">
                            <el-input v-model="userInfo.unit" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话" prop="phone" label-width="150px">
                            <el-input v-model="userInfo.phone" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系地址" prop="address" label-width="150px">
                            <el-input v-model="userInfo.address" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="电子邮箱" prop="email" label-width="120px">
                            <el-input v-model="userInfo.email" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="邮政编码" prop="postCode" label-width="120px">
                            <el-input v-model="userInfo.postCode" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="传真" prop="fax" label-width="120px">
                            <el-input v-model="userInfo.fax" />
                        </el-form-item>
                    </el-col>
                </el-card>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>分类统计信息>></span>
                    </div>
                    <el-col :span="12">
                        <el-form-item label="所在区域" prop="area" label-width="150px">
                            <el-select v-model="userInfo.area">
                                <el-option value="大陆" label="大陆" />
                                <el-option value="其它" label="其它" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所在省份" prop="province" label-width="150px">
                            <el-select  v-model="userInfo.province" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in commonEnums.provinces"
                                :key="item"
                                :value="item"
                                :label="item" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位类型" prop="unitType" label-width="150px">
                            <el-select  v-model="userInfo.unitType" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in $enums.value.unitType"
                                :key="item.value"
                                :value="item.value"
                                :label="item.label" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所在行业" prop="industry" label-width="150px">
                            <el-select  v-model="userInfo.industry" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in commonEnums.industries"
                                :key="item"
                                :value="item"
                                :label="item" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-card>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>费用信息>></span>
                    </div>
                    <el-col :span="12">
                        <el-form-item label="用户类型" prop="userType" label-width="150px"
                        :rules="{required: true, message: '用户类型不能为空!', trigger: 'blur'}">
                            <el-select  v-model="userInfo.userType" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in $enums.value.userType"
                                :key="item.value"
                                :value="item.value"
                                :label="item.label" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="付费方式" prop="paymentType" label-width="150px"
                        :rules="{required: true, message: '付费方式不能为空!', trigger: 'blur'}">
                            <el-select  v-model="userInfo.paymentType" placeholder="请选择" :clearable="true">
                                <el-option v-for="item in $enums.value.paymentType"
                                :key="item.value"
                                :value="item.value"
                                :label="item.label" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-card>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>备注信息>></span>
                    </div>
                <el-col :span="24">
                    <el-form-item label="" prop="remark">
                        <el-input width="100%" rows="4" type="textarea" v-model="userInfo.remark" placeholder="请输入备注信息" />
                    </el-form-item>
                </el-col>
                </el-card>
            </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="submit();">确 定</el-button>
          </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
import { validPassWord, validUsername, validEmail, validatePhone } from '@/utils/validate'
import commonEnums from '@/api/common/params'
const sm2 = require('sm-crypto').sm2
const publicKey = require('@api/service').publicKey
export default {
  props: [
    'allGroups'
  ],
  data () {
    var validatePassword = (rule, value, callback) => {
      if (this.title !== '新增用户' && !value) {
        callback()
      }
      if (this.userInfo.verificationType == 'Ip') {
        callback()
      }
      if (!value) {
        callback(new Error('密码不能为空!'))
      }
      if (!validPassWord(value)) {
        callback(new Error('密码最少8位，必须包含大写字母、小写字母和数字'))
      }
      callback()
    }
    var validateUserName = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('登陆账号最少6位，包含数字和字母'))
      }
      callback()
    }
    var validatePhoneNumber = (rule, value, callback) => {
      if (!value) {
        callback()
      }
      if (!validatePhone(value)) {
        callback(new Error('手机号码格式不正确'))
      }
      callback()
    }
    var validateEmail = (rule, value, callback) => {
      if (!value) {
        callback()
      }
      if (!validEmail(value)) {
        callback(new Error('邮箱格式不正确,请检查!'))
      }
      callback()
    }
    return {
      title: '',
      visible: false,
      userInfo: {},
      status: true,
      api: process.env.VUE_APP_API,
      commonEnums: commonEnums,
      rules: {
        userName: [
          { required: true, message: '登录名不能为空!', trigger: 'blur' }
        //   { validator: validateUserName, trigger: 'blur' }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhoneNumber, trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '姓名不能为空!', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    show (userInfo) {
      this.userInfo = JSON.parse(JSON.stringify(userInfo))
      if (userInfo && userInfo.id) {
        this.title = '修改用户信息'
        this.getUserIpInfo()
        this.getUserDownloadRules()
      } else {
        this.title = '新增用户'
      }
      if (!this.userInfo.userIpInfos) {
        this.userInfo.userIpInfos = []
      }
      if (!this.userInfo.downloadRules) {
        this.userInfo.downloadRules = []
      }
      this.visible = true
    },
    close () {
      this.visible = false
    },
    removeIp (index) {
      this.userInfo.userIpInfos.splice(index, 1)
      this.userInfo = JSON.parse(JSON.stringify(this.userInfo))
    },
    addIp () {
      this.userInfo.userIpInfos.push({})
      this.userInfo = JSON.parse(JSON.stringify(this.userInfo))
    },
    getUserIpInfo () {
      this.$axiosRequest('system/userIpInfo/getUserIpInfo', { userId: this.userInfo.id }, this.$axiosOptions('get'))
        .then(res => {
          this.userInfo.userIpInfos = res.userIpInfos
        })
    },
    removeRule (index) {
      this.userInfo.downloadRules.splice(index, 1)
    },
    addRule () {
      const rule = {
        rule: this.$enums.value.downloadRule[0].value,
        limit: 1
      }
      this.userInfo.downloadRules.push(rule)
      this.userInfo = JSON.parse(JSON.stringify(this.userInfo))
    },
    getUserDownloadRules () {
      this.$axiosRequest('system/downloadRules/getRules', { userId: this.userInfo.id }, this.$axiosOptions('get'))
        .then(res => {
          this.userInfo.downloadRules = res.downloadRules
          this.userInfo = JSON.parse(JSON.stringify(this.userInfo))
        })
    },
    checkUserName () {
      this.$axiosRequest('system/user/checkUserName', { userName: this.userInfo.userName }, this.$axiosOptions('get'))
        .then(res => {
          if (!res.success) {
            this.$message.error(res.msg)
            return
          }
          this.$message.success('该账号可用!')
        })
    },
    submit () {
      let ipValid = true
      this.userInfo.userIpInfos.forEach((item, index) => {
        const reg = /^((25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[0]\d{2}|[0]{2}\d|\d|0\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[1-9]|[0]\d{2}|[0]{2}\d)$/
        if (!reg.test(item.startIp)) {
          this.$message.error('第' + (index + 1) + '条起始IP不正确,请检查!')
          ipValid = false
          return
        }
        if (!reg.test(item.finishIp)) {
          this.$message.error('第' + (index + 1) + '条结束IP不正确,请检查!')
          ipValid = false
        }
      })
      if (!ipValid) {
        return
      }
      this.$refs.userInfoForm.validate(valid => {
        if (valid) {
          const userInfo = JSON.parse(JSON.stringify(this.userInfo))
          if (userInfo.password) {
            userInfo.password = '04' + sm2.doEncrypt(userInfo.password, publicKey, 1)
          }
          // this.$axiosRequest('/system/user/addOrModify', userInfo, this.$axiosOptions('post', null, null, true))
          axios({
            method: 'post',
            url: 'system/user/addOrModify',
            data: userInfo
          }).then(res => {
            if (!res.data.success) {
              this.$message.error(res.data.msg)
              return
            }
            this.$message.success('操作成功!')
            this.visible = false
            this.$emit('callback')
          })
        } else {
          this.$message.error('表单验证不通过，请检查!')
        }
      })
    }
  }
}

</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 118px;
    height: 118px;
    line-height: 118px;
    text-align: center;
  }
  .avatar {
    width: 118px;
    height: 118px;
    display: inline;
  }
</style>
