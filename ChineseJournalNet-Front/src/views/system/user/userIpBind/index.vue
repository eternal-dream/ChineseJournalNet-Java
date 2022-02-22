<template>
  <d2-container type="full">
    <template slot="header">
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
      <el-form :model="queryParams" ref="form" size="small" :inline="true">
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
              <el-select style="width:200px" v-model="queryParams.status" placeholder="请选择" :clearable="true">
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
            <el-form-item label="备注信息" prop="remark" label-width="80px" >
                <el-input v-model="queryParams.remark" placeholder="请输入备注信息"/>
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
          <el-button type="success"  size="small" @click="handleEdit({})"><d2-icon name="plus" />新增</el-button>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          <!-- <el-button type="danger"  size="small" @click="disableAllSelection()"><d2-icon name="trash" />禁用</el-button> -->
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="userInfoList" size="mini" empty-text="未查询到用户信息" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="userName" label="用户名" min-width='100px' :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="unit" label="单位名" min-width='100px' :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="verificationType" label="登录验证方式" min-width='100px' :show-overflow-tooltip="true" align="center">
          <template slot-scope="scope">
            <span v-for="item in $enums.value.verificationType" v-show="scope.row.verificationType == item.value" :key="item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注信息" min-width='100px' :show-overflow-tooltip="true" align="center" />
        <el-table-column label="操作" width="310px" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="small"
              style="padding:5px"
              @click="handleEdit(scope.row)"
            >编辑IP</el-button>
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
    <user-ip-form ref="userIpForm"></user-ip-form>
    <!-- <user-info-form ref="userIpForm" :allGroups="allGroups" :totalRoles="roles" @callback="searchUserInfo"></user-info-form> -->
  </d2-container>
</template>

<script>
import userIpForm from './userIpForm.vue'
import axios from 'axios'
export default {
  name: 'UserIpBind',
  components: {
    userIpForm
  },
  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        filterByIP: false // 搜索方式（false:条件过滤，true:IP过滤）
      },
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      userInfoList: [],
      total: 0,
      allGroups: [],
      roles: [] // 角色用以授权
    }
  },
  created () {
    this.searchUserInfo()
    this.getAllGroups()
  },
  methods: {
    searchUserInfo () {
      this.$axiosRequest('/system/user/getUserInfoPage', this.queryParams, { method: 'get' })
        .then((res) => {
          this.userInfoList = res.userInfoPage.records
          this.total = res.userInfoPage.total
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchUserInfo()
    },
    handleEdit (userInfo) {
      this.$refs.userIpForm.show(userInfo)
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchUserInfo()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    getAllGroups () {
      this.$axiosRequest('/system/userGroup/getAllGroups', {}, this.$axiosOptions('get'))
        .then(res => {
          this.allGroups = res.allGroups
        })
    },
    deleteAllSelection () {
      this.ids = []
      if (this.multipleSelection.length <= 0) {
        this.$message.error('请选择需要删除的用户！')
        return
      }
      this.multipleSelection.forEach((item) => {
        this.ids.push(item.id)
      })
      this.deleteUserInfo()
    },
    handleDelete (row) {
      this.ids = [row.id]
      this.deleteUserInfo()
    },
    deleteUserInfo () {
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
          url: '/system/user/deleteUserInfo',
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
    }
  }

}
</script>
