<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="40%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="groupForm" :model="group" >
                <el-form-item label="用户组名称" prop="name" label-width="100px"
                    :rules="{ required: true, message: '请输入用户组名称!', trigger: 'blur'}">
                    <el-input v-model="group.name" placeholder="请输入用户组名称" />
                </el-form-item>
                <el-card class="box-card" style="margin-top:5px">
                    <div slot="header" class="clearfix">
                        <span>文献下载限制规则>></span>
                         <el-button style="float: right; padding: 3px 0" type="text" @click="addRule()">新增规则</el-button>
                    </div>
                    <el-form-item label="" prop="downloadRules" >
                        <el-col :span="10">限制类型：</el-col>
                        <el-col :span="10">最大下载量：</el-col>
                        <el-row :gutter="5" style="margin-bottom:5px" v-for="(item,i) in group.downloadRules" :key="i">
                            <el-col :span="10">
                                <el-select v-model="group.downloadRules[i].rule">
                                    <el-option v-for="item in $enums.value.downloadRule"
                                    :value="item.value"
                                    :label="item.label"
                                    :key="item.value"/>
                                </el-select>
                            </el-col>
                            <el-col :span="10">
                                <el-input-number :step="1" :min="0" v-model="group.downloadRules[i].limit" />
                            </el-col>
                            <el-col :span="4">
                                <el-button @click="removeRule(i)"><d2-icon name="minus"></d2-icon></el-button>
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
  props: [
    'allGroups'
  ],
  data () {
    return {
      visible: false,
      title: '',
      group: {}
    }
  },
  created () {
  },
  methods: {
    show (group) {
      this.group = JSON.parse(JSON.stringify(group))
      if (group && group.id) {
        this.title = '编辑用户组'
        this.getGroupDownloadRules()
      } else {
        this.title = '新增用户组'
      }
      if (!this.group.downloadRules) {
        this.group.downloadRules = []
      }
      this.visible = true
    },
    getGroupDownloadRules () {
      this.$axiosRequest('system/downloadRules/getRules', { groupId: this.group.id }, this.$axiosOptions('get'))
        .then(res => {
          this.group.downloadRules = res.downloadRules
        })
    },
    close () {
      this.$refs.groupForm.resetFields()
      this.visible = false
    },
    removeRule (index) {
      this.group.downloadRules.splice(index, 1)
    },
    addRule () {
      const rule = {
        rule: this.$enums.value.downloadRule[0].value,
        limit: 1
      }
      this.group.downloadRules.push(rule)
      this.group = JSON.parse(JSON.stringify(this.group))
    },
    submit () {
      this.$refs.groupForm.validate(valid => {
        if (valid) {
        //   this.$axiosRequest('/system/group/addOrModify', this.group, this.$axiosOptions('post', 'application/json'))
          axios({
            method: 'post',
            url: 'system/userGroup/addOrModify',
            data: this.group
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
