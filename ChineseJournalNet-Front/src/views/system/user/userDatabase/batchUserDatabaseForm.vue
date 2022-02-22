<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="80%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="userDatabaseForm" :model="content" size="small">
                <el-form-item label="用户" prop="userIds" label-width="100px">
                  <el-select v-model="content.userIds" multiple placeholder="请选择用户">
                    <el-option v-for="item in users"
                      :key="item.id"
                      :value="item.id"
                      :label="item.userName" />
                  </el-select>
                </el-form-item>
                <el-card class="box-card" style="margin-top:5px">
<!--                  <div slot="header" class="clearfix">-->
<!--                      <span>包库信息>></span>-->
<!--                      <el-button style="float: right; padding: 3px 0" type="text" @click="addUserDatabase()">增加数据库</el-button>-->
<!--                  </div>-->
                  <el-col :span="3" align="center">开始时间</el-col>
                  <el-col :span="3" align="center">结束时间</el-col>
<!--                  <el-col :span="3" align="center">数据库</el-col>-->
                  <el-col :span="7" align="center">分类限制</el-col>
                  <el-col :span="5" align="center">数据年限</el-col>
                  <el-col :span="2" align="center">状态</el-col>
                  <el-col :span="3" align="center">备注</el-col>
                  <el-divider></el-divider>
                  <el-row v-for="(database,i) in content.userDatabase" :key="i" style="margin-bottom:5px">
                    <el-col :span="3">
                      <el-date-picker
                            v-model="database.beginTime"
                            type="date"
                            size="small"
                            style="width:95%"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期">
                        </el-date-picker>
                    </el-col>
                    <el-col :span="3">
                      <el-date-picker
                            v-model="database.endTime"
                            type="date"
                            size="small"
                            style="width:95%"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期">
                        </el-date-picker>
                    </el-col>
<!--                    <el-col :span="3">-->
<!--                      <el-select v-model="database.databaseId" placeholder="请选择" size="small" style="width:95%">-->
<!--                        <el-option v-for="item in databaseList"-->
<!--                          :key="item.id"-->
<!--                          :value="item.id"-->
<!--                          :label="item.name" />-->
<!--                      </el-select>-->
<!--                    </el-col>-->
                    <el-col :span="7">
                      <el-select @change="(val)=>{changeClassify(database,val)}" v-model="database.classifyArray" :collapse-tags="true" style="width:95%" :clearable="true" filterable multiple size="small">
                        <el-option v-for="item in statClasses"
                          :key="item.classNumber"
                          :value="item.classNumber"
                          :label="item.name" />
                      </el-select>
                    </el-col>
                    <el-col :span="5">
                      <el-date-picker
                            v-model="database.inceptionYear"
                            type="year"
                            size="small"
                            value-format="yyyy"
                            style="width:45%"
                            placeholder="开始年份">
                        </el-date-picker>
                        -
                        <el-date-picker
                            v-model="database.terminationYear"
                            type="year"
                            size="small"
                            value-format="yyyy"
                            style="width:45%"
                            placeholder="结束年份">
                        </el-date-picker>
                    </el-col>
                    <el-col :span="2">
                      <el-select v-model="database.effective" size="small" style="width:95%">
                        <el-option :value="true" label="有效" />
                        <el-option :value="false" label="无效" />
                      </el-select>
                    </el-col>
                    <el-col :span="4">
                      <el-input v-model="database.remark" placeholder="备注信息" size="small" />
                    </el-col>
<!--                    <el-col :span="1">-->
<!--                      <el-button @click="removeUserDatabase(i)" size="mini"><d2-icon name="minus"></d2-icon></el-button>-->
<!--                    </el-col>-->
                  </el-row>
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
    'databaseList'
  ],
  data () {
    return {
      visible: false,
      title: '',
      content: {
        userIds: [], // 用户id
        userDatabase: [
          {
            beginTime: this.$moment(new Date()).format('yyyy-MM-DD HH:mm:ss'),
            classifyArray: []
          }
        ]
      },
      statClasses: [],
      users: [],
      selectedClassify: []
    }
  },
  created () {
    this.getStatClasses()
    this.getUnsetUsers()
  },
  methods: {
    show () {
      this.getUnsetUsers()
      this.title = '新增包库信息'
      this.visible = true
      this.statClasses.forEach(item => {
        this.content.userDatabase[0].classifyArray.push(item.classNumber)
      })
      // console.log(this.content.userDatabase[0].classifyArray)
      // console.log(this.statClasses)
    },
    close () {
      this.content = {
        userIds: [], // 用户id
        userDatabase: [
          {
            beginTime: this.$moment(new Date()).format('yyyy-MM-DD HH:mm:ss'),
            classifyArray: []
          }
        ]
      }
      this.visible = false
    },
    getStatClasses () {
      this.$axiosRequest('/system/statClass/getStatClasses', {}, this.$axiosOptions('get'))
        .then(res => {
          this.statClasses = res.statClasses
        })
    },
    changeClassify (userDatabase, value) {
      let classify = ''
      userDatabase.classifyArray.forEach(item => {
        classify += item + ','
      })
      classify = classify.substring(0, classify.length - 1)
      userDatabase.classify = classify
    },
    addUserDatabase () {
      this.content.userDatabase.push({})
    },
    removeUserDatabase (index) {
      this.content.userDatabase.splice(index, 1)
    },
    getUnsetUsers () {
      this.$axiosRequest('/system/userDatabase/getUnsetUsers', {}, this.$axiosOptions('get'))
        .then(res => {
          this.users = res.users
        })
    },
    submit () {
      let flag = true
      this.content.userDatabase.forEach((item, index) => {
        // if (!item.databaseId) {
        //   this.$message.error('第' + (index + 1) + '条包库信息未选择数据库!')
        //   flag = false
        //   return
        // }
        if (!item.beginTime || !item.endTime) {
          this.$message.error('包库信息开始/结束时间不能为空!')
          flag = false
        }
      })
      if (this.content.userIds.length === 0) {
        this.$message.error('请选择用户！')
        return
      }
      if (!flag) {
        return
      }
      this.$refs.userDatabaseForm.validate(valid => {
        if (valid) {
          const content = JSON.parse(JSON.stringify(this.content))
          content.userDatabase.forEach(item => {
            item.classify = item.classifyArray.join(',')
          })
          //   this.$axiosRequest('/system/group/addOrModify', this.group, this.$axiosOptions('post', 'application/json'))
          axios({
            method: 'post',
            url: 'system/userDatabase/batchAddUserDatabase',
            data: content
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
