<template>
  <d2-container type="full">
    <template slot="header">
      <h4 >用户搜索</h4>
      <el-form :model="queryParams" ref="form" :inline="true">
        <el-col :span=7>
        <el-form-item label="用户名" prop="userName" label-width="100px">
            <el-input v-model="queryParams.userName" placeholder="请输入用户名"></el-input>
        </el-form-item>
        </el-col>
        <el-col :span=7>
        <el-form-item label="真实姓名" prop="realName" label-width="100px">
            <el-input v-model="queryParams.realName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        </el-col>
        <el-col>
          <el-button type="success"  size="small" @click="handleEdit({})"><d2-icon name="plus" />新增</el-button>
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          <!-- <el-button type="danger"  size="small" @click="disableAllSelection()"><d2-icon name="trash" />禁用</el-button> -->
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="adminUserInfoList" size="mini" empty-text="未查询到用户信息" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="userName" label="用户名" :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="realName" label="真实姓名" :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="ipLimit" label="IP限制" :show-overflow-tooltip="true" align="center" >
          <template slot-scope="scope">
            <span v-if="!scope.row.ipLimit">无限制</span>
            <span v-else>{{scope.row.ipLimit}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="effectBeginDate" label="有效日期" :show-overflow-tooltip="true" align="center" >
          <template slot-scope="scope">
            <span v-if="!scope.row.effectBeginDate && !scope.row.effectEndDate">无限制</span>
            <span v-else>{{(scope.row.effectBeginDate ? scope.row.effectBeginDate.substring(0,10) : '不限') +' 至 '+ (scope.row.effectEndDate ? scope.row.effectEndDate.substring(0,10) : '不限')}}</span>
          </template>
        </el-table-column>
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
    <admin-user-form ref="adminUserForm" :totalRoles="roles" :searchAdminUser="searchAdminUser"></admin-user-form>
  </d2-container>
</template>

<script>
import { commonEnums } from '@/api/common/params'
import axios from 'axios'
import adminUserForm from './adminUserForm.vue'
export default {
  name: 'AdminUser',
  components: {
    adminUserForm
  },
  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      param: commonEnums,
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      adminUserInfoList: [],
      total: 0,
      roles: []// 角色用以授权

    }
  },
  created () {
    this.getRoles()
    this.searchAdminUser()
  },
  methods: {
    searchAdminUser () {
      this.$axiosRequest('/system/admin/getAdminUserList', this.queryParams, { method: 'get' })
        .then((res) => {
          this.adminUserInfoList = res.userPage.records
          this.total = res.userPage.total
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchAdminUser()
    },
    handleEdit (adminUser) {
      this.$refs.adminUserForm.show(adminUser)
    },
    handleDisableAdminUser (row) {
      this.ids = [row.id]
      this.disableAdminUser()
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchAdminUser()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    getRoles () {
      this.$axiosRequest('system/role/getTotalRoles', {}, this.$axiosOptions('get'))
        .then(res => {
          this.roles = res.totalRoles
        })
    },
    disableAllSelection () {
      this.ids = []
      if (this.multipleSelection.length <= 0) {
        this.$message.error('请选择需要禁用的用户！')
        return
      }
      this.multipleSelection.forEach((item) => {
        this.ids.push(item.id)
      })
      this.disableAdminUser()
    },
    handleDelete (row) {
      this.ids = [row.id]
      this.deleteAdminUser()
    },
    deleteAdminUser () {
      if (this.ids.length === 0) {
        this.$message.error('请选择要删除的用户!')
        return
      }
      this.$confirm('确定删除选中的用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios({
          method: 'post',
          url: '/system/admin/deleteAdminUser',
          data: this.ids
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          this.$message.success('删除成功!')
          this.searchAdminUser()
        })
        // this.$axiosRequest('/system/armUserInfo/deleteUser', JSON.stringify({ ids: this.ids }), this.$axiosOptions('post'))
      })
    }
  }

}
</script>
