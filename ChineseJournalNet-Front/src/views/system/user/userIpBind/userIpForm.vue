<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="70%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="userIpForm" :model="user" >
                <el-form-item label="用户名" prop="userName" label-width="100px">
                    <el-input disabled :value="user.userName" />
                </el-form-item>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>用户IP信息>></span>
                         <el-button style="float: right; padding: 3px 0" type="text" @click="addIp()">新增IP</el-button>
                    </div>
                    <el-form-item label="" prop="userIpInfos" >
                        <el-col :span="5">开始IP:</el-col>
                        <el-col :span="5">结束IP:</el-col>
                        <el-col :span="6">IP绑定单位:</el-col>
                        <el-col :span="6">IP备注:</el-col>
                        <el-row :gutter="5" style="margin-bottom:5px" v-for="(item,i) in user.userIpInfos" :key="i">
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
            </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="close">取 消</el-button>
                    <el-button  el-button type="primary"  @click="submit">确定</el-button>
                </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
export default {
  components: {
  },
  data () {
    return {
      visible: false,
      title: '',
      user: {}
    }
  },
  created () {
  },
  methods: {
    show (user) {
      this.user = JSON.parse(JSON.stringify(user))
      if (user && user.id) {
        this.title = '编辑用户组'
        this.getUserIpInfo()
      } else {
        this.title = '新增用户组'
      }
      if (!this.user.userIpInfos) {
        this.user.userIpInfos = []
      }
      this.visible = true
    },
    getUserIpInfo () {
      this.$axiosRequest('system/userIpInfo/getUserIpInfo', { userId: this.user.id }, this.$axiosOptions('get'))
        .then(res => {
          this.user.userIpInfos = res.userIpInfos
        })
    },
    close () {
      this.$refs.userIpForm.resetFields()
      this.visible = false
    },
    removeIp (index) {
      this.user.userIpInfos.splice(index, 1)
    },
    addIp () {
      this.user.userIpInfos.push({})
      // this.user=JSON.parse(JSON.stringify(this.user))
      // console.log(this.user)
    },
    submit () {
      let ipValid = true
      this.user.userIpInfos.forEach((item, index) => {
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
      this.$refs.userIpForm.validate(valid => {
        if (valid) {
          axios({
            method: 'post',
            url: 'system/userIpInfo/addOrModify',
            data: this.user
          }).then(res => {
            if (!res.data.success) {
              this.$message.error(res.data.msg)
              return
            }
            this.$message.success('操作成功!')
            this.visible = false
            this.close()
            this.$emit('callback')
          })
        }
      })
    }
  }
}

</script>
