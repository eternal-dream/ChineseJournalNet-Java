<template>
  <d2-container type="full">
    <template slot="header">
      <h4 style="height:15px">条件过滤</h4>
      <el-form :model="queryParams" ref="form" size="small" :inline="true">
        <el-col :span=12>
            <el-form-item label="时间范围" label-width="80px" >
                <el-date-picker
                    v-model="daterange"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd"
                    :picker-options="pickerOptions">
                </el-date-picker>
            </el-form-item>
        </el-col>
        <el-col :span=12>
            <el-form-item label="页面板块" prop="plate" label-width="80px">
                <el-select v-model="queryParams.plate" placeholder="请选择" :clearable="true">
                    <el-option v-for="item in plates"
                    :key="item"
                    :value="item"
                    :label="item" />
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span=12>
            <el-form-item label="IP地址" prop="startIp" label-width="80px">
                <el-input v-model="queryParams.startIp" placeholder="IP起" style="width:48%" />
                -
                <el-input v-model="queryParams.endIp" placeholder="IP止" style="width:48%" />
            </el-form-item>
        </el-col>
        <el-col :span=6>
            <el-form-item label="用户" prop="userIds" label-width="80px">
                <el-select v-model="queryParams.userIds" size="mini" :filterable="true" :collapse-tags="true" multiple :clearable="true" placeholder="请选择用户">
                    <el-option v-for="item in users"
                      :key="item.id"
                      :value="item.id"
                      :label="item.userName" />
                  </el-select>
            </el-form-item>
        </el-col>
        <el-col :span=6>
            <el-form-item label="用户组" prop="userGroupId" label-width="80px">
                <el-select v-model="queryParams.userGroupId" size="mini" :filterable="true" :collapse-tags="true" :clearable="true" placeholder="请选择用户组">
                    <el-option v-for="item in allGroups"
                      :key="item.id"
                      :value="item.id"
                      :label="item.name" />
                  </el-select>
            </el-form-item>
        </el-col>
        <el-col :span=24>
            <el-form-item label="分析对象" prop="type" label-width="80px">
                <el-radio-group v-model="queryParams.type">
                    <el-radio label="plate">页面模块</el-radio>
                    <el-radio label="user">用户</el-radio>
                    <el-radio label="userGroup">用户组</el-radio>
                    <el-radio label="ipRange">用户IP段</el-radio>
                    <el-radio label="custom">
                        自定义
                        <el-select v-model="queryParams.customType" style="width:150px">
                            <el-option value="all" label="全部汇总" />
                            <el-option value="period" label="周期汇总" />
                        </el-select>
                        粒度:
                        <el-select v-model="queryParams.grainSize" style="width:150px">
                            <el-option value="day" label="日" />
                            <el-option value="week" label="周" />
                            <el-option value="month" label="月" />
                            <el-option value="year" label="年" />
                        </el-select>
                    </el-radio>
                </el-radio-group>
            </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-button type="primary" icon="el-icon-search" size="small" @click="analysis">开始分析</el-button>
          <!-- <el-button type="danger"  size="small" @click="disableAllSelection()"><d2-icon name="trash" />禁用</el-button> -->
        </el-col>
        <el-col :span="4">
          <el-button v-show="analysisResult.length>1" type="primary" icon="el-icon-tickets" size="small" @click="handleExport">导出EXCEL</el-button>
        </el-col>
      </el-form>

    </template>
    <template slot="default">
      <el-table :data="analysisResult" size="mini" empty-text="未查询到信息" stripe>
        <el-table-column type="index" width="50" label="序号" align="center" />
        <el-table-column prop="VALUE" label="统计名称" min-width='100px' align="center" />
        <el-table-column prop="COUNT" label="次数" min-width='100px' align="center" />
        <el-table-column prop="COUNT" label="所占比例" min-width='100px' align="center" >
          <template slot-scope="scope">
            <el-progress :percentage="analysisResult[analysisResult.length-1].COUNT == 0 ? 100:parseInt(scope.row.COUNT/analysisResult[analysisResult.length-1].COUNT * 100)"></el-progress>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </d2-container>
</template>

<script>
import axios from 'axios'
import { downloadFile } from '@/utils/zipdownload'
export default {
  name: 'TrafficStatistics',
  components: {
  },
  data () {
    return {
      queryParams: {
        type: 'plate',
        customType: 'all',
        grainSize: 'day',
        visitType: 'View'
      },
      multipleSelection: [], // 多选框选中项
      ids: [], // 选中项的id
      allGroups: [],
      total: 0,
      users: [],
      ipReg: /^((25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[0]\d{2}|[0]{2}\d|\d|0\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[1-9]|[0]\d{2}|[0]{2}\d)$/,
      plates: [], // 所有页面板块
      daterange: [],
      analysisResult: [], // 分析结果
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
  created () {
    this.getAllPlates()
    this.getAllUser()
    this.getAllGroups()
  },
  methods: {
    analysis () {
      if (this.queryParams.startIp && !this.ipReg.test(this.queryParams.startIp)) {
        this.$message.error('起始IP不正确,请检查!')
        return
      }
      if (this.queryParams.endIp && !this.ipReg.test(this.queryParams.endIp)) {
        this.$message.error('结束IP不正确,请检查!')
        return
      }
      if (this.daterange) {
        this.queryParams.startTime = this.daterange[0]
        this.queryParams.endTime = this.daterange[1]
      }else{
        this.queryParams.startTime = undefined
        this.queryParams.endTime = undefined
      }
      //   this.$axiosRequest('/system/statistics/trafficAnalysis', this.queryParams, { method: 'get' })
      axios({
        method: 'post',
        url: '/system/statistics/visitAnalysis',
        data: this.queryParams
      })
        .then((res) => {
          this.analysisResult = res.data.result
        })
    },
    getAllPlates () {
      this.$axiosRequest('/system/userLog/getAllPlates', {}, { method: 'get' })
        .then((res) => {
          this.plates = res.plates
        })
    },
    getAllUser () {
      this.$axiosRequest('/system/user/getAllUser', {}, this.$axiosOptions('get'))
        .then(res => {
          this.users = res.users
        })
    },
    handleQuery () {
      this.queryParams.pageNum = 1
      this.searchGroup()
    },
    handlePageChange (val) {
      this.queryParams.pageNum = val
      this.searchGroup()
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
    handleExport () {
      if (this.queryParams.startIp && !this.ipReg.test(this.queryParams.startIp)) {
        this.$message.error('起始IP不正确,请检查!')
        return
      }
      if (this.queryParams.endIp && !this.ipReg.test(this.queryParams.endIp)) {
        this.$message.error('结束IP不正确,请检查!')
        return
      }
      if (this.daterange) {
        this.queryParams.startTime = this.daterange[0]
        this.queryParams.endTime = this.daterange[1]
      }
      axios({
        method: 'post',
        url: '/system/statistics/visitExport',
        data: this.queryParams,
        responseType: 'blob'
      })
      // this.$axiosRequest('/system/statistics/export', this.queryParams, this.$axiosOptions('post', null, 'blob'))
        .then((res) => {
          downloadFile(res)
        })
    }
  }

}
</script>
