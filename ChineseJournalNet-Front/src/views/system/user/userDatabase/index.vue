<template>
  <d2-container type="full">
    <template slot="header">
      <el-form :model="queryParams" ref="form" size="small" :inline="true">
          <el-row>
            <el-col :span=9>
                <el-form-item label="有效开始日期" prop="effectStartTime1" label-width="100px" >
                    <el-date-picker
                        v-model="queryParams.effectStartTime1"
                        type="date"
                        size="mini"
                        align="left"
                        style="width:35%"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>
                    -
                    <el-date-picker
                        v-model="queryParams.effectStartTime2"
                        type="date"
                        size="mini"
                        align="left"
                        style="width:35%"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
            </el-col>
            <el-col :span=10>
                <el-form-item label="有效结束日期" prop="effectEndTime1" label-width="100px" >
                    <el-date-picker
                        v-model="queryParams.effectEndTime1"
                        type="date"
                        size="mini"
                        align="left"
                        style="width:25%"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>
                    -
                    <el-date-picker
                        v-model="queryParams.effectEndTime2"
                        type="date"
                        size="mini"
                        align="left"
                        style="width:25%"
                        value-format="yyyy-MM-dd"
                        placeholder="选择日期">
                    </el-date-picker>
                    <el-button type="primary" style="margin-left:8px" @click="getNextMonthDate">下月到期</el-button>
                </el-form-item>
            </el-col>
            <el-col :span=5>
                <el-form-item label="是否过期" prop="expired" label-width="100px" >
                    <el-select v-model="queryParams.expired" :clearable="true">
                        <el-option :value="false" label="未过期"/>
                        <el-option :value="true" label="已过期"/>
                    </el-select>
                </el-form-item>
            </el-col>
          </el-row>
          <el-row>
              <el-col :span="9">
                <el-form-item label="状态" prop="effective" label-width="100px" >
                    <el-select v-model="queryParams.effective" style="width:100%" :clearable="true">
                        <el-option :value="true" label="有效"/>
                        <el-option :value="false" label="无效"/>
                    </el-select>
                </el-form-item>
              </el-col>
<!--              <el-col :span="4">-->
<!--                <el-form-item label="数据库" prop="databaseId" label-width="60px" >-->
<!--                    <el-select v-model="queryParams.databaseId" style="width:50%" :clearable="true">-->
<!--                        <el-option v-for="item in databaseList"-->
<!--                        :value="item.id"-->
<!--                        :label="item.name"-->
<!--                        :key="item.id"/>-->
<!--                    </el-select>-->
<!--                </el-form-item>-->
<!--              </el-col>-->
              <el-col :span=10>
                <el-form-item label="数据年限" prop="inceptionYear" label-width="100px" >
                        <el-date-picker
                        v-model="queryParams.inceptionYear"
                        type="year"
                        size="mini"
                        value-format="yyyy"
                        style="width:28%"
                        placeholder="选择日期">
                    </el-date-picker>
                    -
                    <el-date-picker
                        v-model="queryParams.terminationYear"
                        type="year"
                        size="mini"
                        value-format="yyyy"
                        style="width:28%"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
            </el-col>
            <el-col :span=5>
                <el-form-item label="备注" prop="remark" label-width="100px" >
                    <el-input v-model="queryParams.remark" />
                </el-form-item>
            </el-col>
          </el-row>
      <h4 style="height:15px">用户搜索
        <el-switch
          style="display: inline;margin-left:50px"
          v-model="queryParams.filterByIP"
          active-color="#13ce66"
          inactive-color="rgb(236, 165, 71)"
          active-text="IP过滤"
          inactive-text="条件过滤">
        </el-switch>
      </h4>
      <el-divider></el-divider>
        <div v-if="!queryParams.filterByIP">
          <el-col :span=6>
          <el-form-item label="用户名" prop="userName" label-width="80px" >
              <el-input v-model="queryParams.userName" placeholder="请输入用户名"></el-input>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="单位" prop="unit" label-width="80px" >
              <el-input v-model="queryParams.unit" placeholder="请输入单位名称"></el-input>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="联系人" prop="linkMan" label-width="80px" >
              <el-input v-model="queryParams.linkMan" placeholder="请输入联系人"></el-input>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="邮箱" prop="email" label-width="80px" >
              <el-input v-model="queryParams.email" placeholder="请输入电子邮箱"></el-input>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="用户类型" prop="userType" label-width="80px" >
              <el-select style="width:200px" v-model="queryParams.userType" placeholder="请选择" :clearable="true">
                <el-option v-for="item in $enums.value.userType"
                :key="item.value"
                :value="item.value"
                :label="item.label" />
              </el-select>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="账户状态" prop="status" label-width="80px" >
              <el-select style="width:200px" v-model="queryParams.status" placeholder="请选择" >
                      <el-option :value="true" label="有效"></el-option>
                      <el-option :value="false" label="无效"></el-option>
                  </el-select>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="用户组" prop="userGroupId" label-width="80px" >
            <el-select style="width:200px" v-model="queryParams.userGroupId" placeholder="请选择" :clearable="true">
              <el-option v-for="item in allGroups"
              :key="item.id"
              :value="item.id"
              :label="item.name"/>
            </el-select>
          </el-form-item>
          </el-col>
          <el-col :span=6>
          <el-form-item label="费用方式" prop="paymentType" label-width="80px" >
              <el-select style="width:200px" v-model="queryParams.paymentType" placeholder="请选择" :clearable="true">
                      <el-option v-for="item in $enums.value.paymentType"
                      :key="item.value"
                      :value="item.value"
                      :label="item.label" />
                  </el-select>
          </el-form-item>
          </el-col>
          <el-col :span=6>
            <el-form-item label="备注信息" prop="userRemark" label-width="80px" >
                <el-input v-model="queryParams.userRemark" placeholder="请输入备注信息"/>
            </el-form-item>
          </el-col>
        </div>
        <div v-else>
          <el-col :span=7>
            <el-form-item label="IP段" prop="ip" label-width="100px" >
              <el-input v-model="queryParams.ip"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span=7>
            <el-form-item label="IP绑定单位" prop="ipUnit" label-width="100px" >
              <el-input v-model="queryParams.ipUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span=7>
            <el-form-item label="IP绑定备注" prop="ipRemark" label-width="100px" >
              <el-input v-model="queryParams.ipRemark"></el-input>
            </el-form-item>
          </el-col>
        </div>
        <el-col :span="20">
        <el-button type="success"  size="small" @click="handleAdd()"><d2-icon name="plus" />新增</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" :disabled="multipleSelection.length==0" @click="deleteAllSelection">删除</el-button>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          <!-- <el-button type="danger"  size="small" @click="disableAllSelection()"><d2-icon name="trash" />禁用</el-button> -->
        </el-col>
        <el-col :span="4">
          <el-button type="primary" icon="el-icon-tickets" size="small" @click="handleExport">导出EXCEL</el-button>
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="userDatabaseList" size="mini" empty-text="未查询到用户信息" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="userName" label="用户名" min-width='100px' :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="unit" label="单位名" min-width='100px' :show-overflow-tooltip="true" align="center" />
<!--        <el-table-column prop="databaseName" label="数据库" min-width='200px' :show-overflow-tooltip="true" align="center" />-->
        <el-table-column prop="classify" label="分类限制" min-width='300px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span v-show="scope.row.classify">{{scope.row.classify}}</span>
            <span v-show="!scope.row.classify">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="inceptionYear" label="数据年限" min-width='100px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span v-if="!scope.row.inceptionYear && !scope.row.terminationYear">不限制</span>
            <span v-else>{{(scope.row.inceptionYear ? scope.row.inceptionYear : '不限') +' 至 '+ (scope.row.terminationYear ? scope.row.terminationYear : '不限')}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="classify" label="状态" min-width='100px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.effective">有效</span>
            <span v-else>无效</span>
          </template>
        </el-table-column>
        <el-table-column prop="beginTime" label="开始时间" min-width='100px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span >{{scope.row.beginTime.substring(0,10)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" min-width='100px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span >{{scope.row.endTime.substring(0,10)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width='100px' :show-overflow-tooltip="true" align="center" />
        <el-table-column label="操作" width="310px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="small"
              style="padding:5px"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              class="red"
              size="small"
              style="padding:5px"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="queryParams.pageNum"
            :page-size="queryParams.pageSize"
            :total="total"
            @current-change="handlePageChange"
        ></el-pagination>
    </div>
    </template>
    <user-database-form ref="userDatabaseForm" :databaseList="databaseList" @callback="searchUserInfo"/>
    <batch-user-database-form ref="batchUserDatabaseForm" :databaseList="databaseList" @callback="searchUserInfo"/>
  </d2-container>
</template>

<script>
import axios from 'axios'
import userDatabaseForm from './userDatabaseForm'
import batchUserDatabaseForm from './batchUserDatabaseForm.vue'
import { downloadFile } from '@/utils/zipdownload'
export default {
  name: 'UserInfo',
  components: {
    userDatabaseForm,
    batchUserDatabaseForm
  },
  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        expired:false,
        filterByIP: false // 搜索方式（false:条件过滤，true:IP过滤）
      },
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      userDatabaseList: [],
      databaseList: [],
      total: 0,
      allGroups: [],
      roles: [] // 角色用以授权
    }
  },
  created () {
    this.searchUserInfo()
    this.getAllGroups()
    this.getAllDatabase()
  },
  methods: {
    searchUserInfo () {
      this.$axiosRequest('/system/userDatabase/searchUserDatabase', this.queryParams, { method: 'get' })
        .then((res) => {
          this.userDatabaseList = res.userDatabasePage.records
          this.total = res.userDatabasePage.total
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchUserInfo()
    },
    handleEdit (userInfo) {
      this.$refs.userDatabaseForm.show(userInfo)
    },
    handleAdd () {
      this.$refs.batchUserDatabaseForm.show()
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchUserInfo()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    deleteAllSelection () {
      this.ids = []
      this.multipleSelection.forEach((item) => {
        this.ids.push(item.id)
      })
      this.deleteUserDatabase()
    },
    handleDelete (row) {
      this.ids = [row.id]
      this.deleteUserDatabase()
    },
    getAllGroups () {
      this.$axiosRequest('/system/userGroup/getAllGroups', {}, this.$axiosOptions('get'))
        .then(res => {
          this.allGroups = res.allGroups
        })
    },
    getAllDatabase () {
      this.$axiosRequest('/system/databaseInfo/getAllDatabase', {}, this.$axiosOptions('get'))
        .then(res => {
          this.databaseList = res.databaseList
        })
    },
    getNextMonthDate () {
      const today = new Date()
      let year = today.getFullYear()
      let month = today.getMonth() + 2
      if (month === 13) {
        month = 1
        year = year + 1
      }
      const firstDay = `${year}-${month}-01`
      const lastDay = new Date(year, month, 0)
      const lastDayOfNextMonth = `${year}-${month}-${lastDay.getDate()}`
      this.queryParams.effectEndTime1 = firstDay
      this.queryParams.effectEndTime2 = lastDayOfNextMonth
      this.queryParams = JSON.parse(JSON.stringify(this.queryParams))
    },
    deleteUserDatabase () {
      if (this.ids.length === 0) {
        this.$message.error('请选择要删除的包库信息!')
        return
      }
      this.$confirm('确定删除选中的包库信息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios({
          method: 'post',
          url: '/system/userDatabase/deleteUserDatabase',
          data: this.ids
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          this.$message.success('删除成功!')
          this.searchUserInfo()
        })
        // this.$axiosRequest('/system/armUserInfo/deleteUser', JSON.stringify({ ids: this.ids }), this.$axiosOptions('post'))
      })
    },
    handleExport () {
      this.$axiosRequest('/system/userDatabase/export', this.queryParams, this.$axiosOptions('post', null, 'blob'))
        .then((res) => {
          downloadFile(res)
        })
    }
  }

}
</script>
<style scoped>
>>> .el-form-item__content{
    position: absolute !important;
}
</style>
