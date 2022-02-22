<template>
  <d2-container type="full">
    <template slot="header">
      <h4 >日志搜索</h4>
      <el-form :model="queryParams" size="small" ref="form" :inline="true">
        <el-row>
          <el-col :span=7>
            <el-form-item label="操作人用户名" prop="userName" label-width="100px">
                <el-input v-model="queryParams.userName" placeholder="请输入操作人用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span=7>
            <el-form-item label="操作类型" prop="operateType" label-width="100px">
                <el-select v-model="queryParams.operateType" placeholder="请选择" :clearable="true">
                    <el-option v-for="item in $enums.value.operateType"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label" />
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span=10 >
            <el-form-item  label="操作时间" prop="operateType" label-width="100px">
                <el-date-picker
                  v-model="timeRange"
                  type="daterange"
                  :picker-options="pickerOptions"
                  range-separator="至"
                  value-format="yyyy-MM-dd"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  align="left">
                </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span=7>
            <el-form-item label="功能模块" prop="resourceName" label-width="100px">
                <el-select v-model="queryParams.resourceName" placeholder="请选择" :clearable="true">
                    <el-option v-for="item in resources"
                    :key="item.name"
                    :value="item.name"
                    :label="item.name" />
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span=3>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="operlogList" size="mini" empty-text="未查询到用户信息" stripe @selection-change="handleSelectionChange">
        <!-- <el-table-column type="selection" width="55" align="center" fixed /> -->
        <!-- <el-table-column type="index" width="50" label="序号" align="center" /> -->
        <el-table-column prop="title" label="操作信息" :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="createTime" label="操作时间" :show-overflow-tooltip="true" align="center" />
        <el-table-column prop="ipAddress" label="IP" align="center" />
        <el-table-column prop="operateType" label="操作类型" align="center" >
          <template slot-scope="scope">
            <span v-for="item in $enums.value.operateType"
              v-show="item.value === scope.row.operateType" :key="item.value">{{item.label}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="操作人" align="center" />
         <el-table-column prop="resourceName" label="功能模块" align="center" />
        <el-table-column prop="errMessage" label="异常信息" :show-overflow-tooltip="true" align="center" />
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
  </d2-container>
</template>

<script>
export default {
  name: 'AdminUserLog',
  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      operlogList: [],
      total: 0,
      operateType: [],
      timeRange: '',
      resources: [], // 所有功能模块
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  async created () {
    this.searchOperlog()
    this.getAllResources()
  },
  methods: {
    searchOperlog () {
      if (this.timeRange) {
        this.queryParams.startTime = this.timeRange[0]
        this.queryParams.endTime = this.timeRange[1] + ' 23:59:59'
      }else{
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      this.$axiosRequest('/system/operlog/getLogPage', this.queryParams, { method: 'get' })
        .then((res) => {
          this.operlogList = res.page.records
          this.total = res.page.total
        })
    },
    getAllResources () {
      this.$axiosRequest('/system/resource/getAllMenu', {}, { method: 'get' })
        .then((res) => {
          this.resources = res.resources
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchOperlog()
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchOperlog()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    }
  }

}
</script>
