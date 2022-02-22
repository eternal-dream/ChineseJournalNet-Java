<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="30%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="userDatabaseForm" :model="userDatabase" size="small">
                <el-form-item  v-if="userDatabase.id" label="用户名" prop="userName" label-width="100px">
                    <el-input disabled :value="userDatabase.userName" />
                </el-form-item>
                <el-form-item label="开始时间" prop="beginTime" label-width="100px">
                  <el-date-picker
                        v-model="userDatabase.beginTime"
                        type="date"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间" prop="endTime" label-width="100px">
                  <el-date-picker
                        v-model="userDatabase.endTime"
                        type="date"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
<!--                <el-form-item label="数据库" prop="databaseId" label-width="100px">-->
<!--                  <el-select v-model="userDatabase.databaseId" placeholder="请选择">-->
<!--                    <el-option v-for="item in databaseList"-->
<!--                      :key="item.id"-->
<!--                      :value="item.id"-->
<!--                      :label="item.name" />-->
<!--                  </el-select>-->
<!--                </el-form-item>-->
                <el-form-item label="分类限制" prop="classify" label-width="100px">
                  <el-select @change="changeClassify" v-model="userDatabase.classifyArray" :collapse-tags="true" :clearable="true" style="width:95%" filterable multiple size="small">
                        <el-option v-for="item in statClasses"
                          :key="item.classNumber"
                          :value="item.classNumber"
                          :label="item.name" />
                      </el-select>
                </el-form-item>
                <el-form-item label="状态" prop="effective" label-width="100px">
                  <el-select v-model="userDatabase.effective" size="small" style="width:95%">
                        <el-option :value="true" label="有效" />
                        <el-option :value="false" label="无效" />
                      </el-select>
                </el-form-item>
                <el-form-item label="数据年限" prop="inceptionYear" label-width="100px">
                  <el-date-picker
                        style="width:40%"
                        v-model="userDatabase.inceptionYear"
                        type="year"
                        value-format="yyyy"
                        format="yyyy"
                        placeholder="开始年限">
                    </el-date-picker>
                    -
                    <el-date-picker
                        style="width:40%"
                        v-model="userDatabase.terminationYear"
                        type="year"
                        value-format="yyyy"
                        format="yyyy"
                        placeholder="结束年限">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="备注" prop="remark" label-width="100px">
                  <el-input type="textarea" v-model="userDatabase.remark" placeholder="请输入备注"/>
                </el-form-item>
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
      userDatabase: {},
      statClasses: [],
      users: []
    }
  },
  created () {
    this.getStatClasses()
  },
  methods: {
    show (userDatabase) {
      this.getCheckedClasses(userDatabase)
      this.userDatabase = JSON.parse(JSON.stringify(userDatabase))
      if (userDatabase && userDatabase.id) {
        this.userDatabase.inceptionYear = this.userDatabase.inceptionYear ? this.userDatabase.inceptionYear + '' : undefined
        this.userDatabase.terminationYear = this.userDatabase.terminationYear ? this.userDatabase.terminationYear + '' : undefined
        this.title = '编辑包库信息'
      } else {
        this.title = '新增包库信息'
      }
      this.visible = true
    },
    close () {
      this.$refs.userDatabaseForm.resetFields()
      this.visible = false
    },
    changeClassify (value) {
      let classify = ''
      this.userDatabase.classifyArray.forEach(item => {
        classify += item + ','
      })
      classify = classify.substring(0, classify.length - 1)
      this.userDatabase.classify = classify
    },
    getStatClasses () {
      this.$axiosRequest('/system/statClass/getStatClasses', {}, this.$axiosOptions('get'))
        .then(res => {
          this.statClasses = res.statClasses
        })
    },
    getCheckedClasses (userDatabase) {
      if(!userDatabase.classify){
        return;
      }
      const checkedClasses = []
      this.statClasses.forEach(item => {
        if (userDatabase.classify.indexOf(item.classNumber) !== -1) {
          checkedClasses.push(item.classNumber)
        }
      })
      userDatabase.classifyArray = checkedClasses
    },
    submit () {
      this.$refs.userDatabaseForm.validate(valid => {
        if (valid) {
          const userDatabase = JSON.parse(JSON.stringify(this.userDatabase))
          this.$axiosRequest('/system/userDatabase/modify', userDatabase, this.$axiosOptions('post'))
            .then(res => {
              if (!res.success) {
                this.$message.error(res.msg)
                return
              }
              this.$message.success('操作成功!')
              this.close()
              this.$emit('callback')
            })
        }
      })
    }
  }
}

</script>
