<template>
  <d2-container type="full">
    <template slot="header">
      <h4 >角色搜索</h4>
      <el-form :model="queryParams" ref="form" :inline="true">
        <el-col :span=7>
        <el-form-item label="角色名称" prop="name" label-width="100px">
            <el-input v-model="queryParams.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        </el-col>
        <el-col>
          <el-button type="success"  size="small" @click="handleEdit({})"><d2-icon name="plus" />添加</el-button>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="roleList" size="mini" empty-text="未查询到角色信息" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="name" label="角色名" :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="description" label="描述" :show-overflow-tooltip="true" align="center" />
        <el-table-column label="操作" width="310px" align="center" fixed="right">
          <template slot-scope="scope">
              <el-button
              type="warning"
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
    <role-form ref="roleForm" :searchRole="searchRole"></role-form>
  </d2-container>
</template>

<script>
import { commonEnums } from '@/api/common/params'
import axios from 'axios'
import roleForm from './roleForm.vue'
export default {
  name: 'Role',
  components: {
    roleForm
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
      roleList: [],
      total: 0,
      roles: []// 角色用以授权

    }
  },
  created () {
    this.getRoles()
    this.searchRole()
  },
  methods: {
    searchRole () {
      this.$axiosRequest('/system/role/getRoles', this.queryParams, { method: 'get' })
        .then((res) => {
          this.roleList = res.roles
          this.total = res.total
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchRole()
    },
    handleEdit (role) {
      this.$refs.roleForm.show(role)
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchRole()
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
    handleDelete (row) {
      this.ids = [row.id]
      this.deleteRole()
    },
    deleteRole () {
      if (this.ids.length === 0) {
        this.$message.error('请选择要删除的角色!')
        return
      }
      this.$confirm('确定删除选中的角色吗？删除后可能导致某些功能无法使用', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios({
          method: 'post',
          url: '/system/role/deleteRole',
          data: this.ids
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          this.$message.success('删除成功!')
          this.searchRole()
        })
      })
    }
  }

}
</script>
