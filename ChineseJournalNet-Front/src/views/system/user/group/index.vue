<template>
  <d2-container type="full">
    <template slot="header">
      <h4 style="height:15px">用户组查询</h4>
      <el-divider></el-divider>
      <el-form :model="queryParams" ref="form" size="small" :inline="true">
        <el-col :span=12>
        <el-form-item label="用户组名" prop="name" label-width="80px" >
            <el-input v-model="queryParams.name" placeholder="请输入用户组名称"></el-input>
        </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-button type="success"  size="small" @click="handleEdit({})"><d2-icon name="plus" />新增</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" :disabled="multipleSelection.length==0" @click="deleteAllSelection">删除</el-button>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
          <!-- <el-button type="danger"  size="small" @click="disableAllSelection()"><d2-icon name="trash" />禁用</el-button> -->
        </el-col>
        <el-col :span="4">
          <!-- <el-button type="primary" icon="el-icon-tickets" size="small" @click="handleQuery">导出EXCEL</el-button> -->
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="groupList" size="mini" empty-text="未查询到用户组信息" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="name" label="用户组名" min-width='100px' :show-overflow-tooltip="true" align="center" />
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
    <!-- <user-info-form ref="userInfoForm" :totalRoles="roles" @callback="searchGroup"></user-info-form> -->
    <group-form ref="groupForm" :allGroups="allGroups" @callback="searchGroup"></group-form>
  </d2-container>
</template>

<script>
import axios from 'axios'
import groupForm from './groupForm.vue'
export default {
  name: 'Group',
  components: {
    groupForm
  },
  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      groupList: [],
      allGroups: [],
      total: 0
    }
  },
  created () {
    this.searchGroup()
  },
  methods: {
    searchGroup () {
      this.$axiosRequest('/system/userGroup/getGroupPage', this.queryParams, { method: 'get' })
        .then((res) => {
          this.groupList = res.groupPage.records
          this.total = res.groupPage.total
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchGroup()
    },
    handleEdit (group) {
      this.$refs.groupForm.show(group)
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchGroup()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    deleteAllSelection () {
      this.ids = []
      this.multipleSelection.forEach((item) => {
        this.ids.push(item.id)
      })
      this.deleteGroup()
    },
    handleDelete (row) {
      this.ids = [row.id]
      this.deleteGroup()
    },
    deleteGroup () {
      if (this.ids.length === 0) {
        this.$message.error('请选择要删除的用户组!')
        return
      }
      this.$confirm('确定删除选中的用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios({
          method: 'post',
          url: '/system/userGroup/deleteGroup',
          data: this.ids
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          this.$message.success('删除成功!')
          this.searchGroup()
        })
        // this.$axiosRequest('/system/armUserInfo/deleteUser', JSON.stringify({ ids: this.ids }), this.$axiosOptions('post'))
      })
    },
    getAllGroups () {
      this.$axiosRequest('/system/userGroup/getAllGroups', {}, this.$axiosOptions('get'))
        .then(res => {
          this.allGroups = res.allGroups
        })
    }
  }

}
</script>
