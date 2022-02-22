<template>
  <d2-container>
    <vxe-toolbar :refresh="{query: reload}" export print custom>
      <template #buttons>
        <vxe-button @click="openAddResource">新增</vxe-button>
        <vxe-button @click="deleteResourcesByIds">删除</vxe-button>
      </template>
    </vxe-toolbar>

    <vxe-table
      border
      show-overflow
      show-footer
      height="100%"
      keep-source
      highlight-hover-row
      highlight-hover-column
      highlight-current-row
      ref="xTree"
      row-id="id"
      :print-config="{}"
      :export-config="{}"
      :loading="loading"
      :tree-config="treeConfig"
      :edit-config="{trigger: 'click', mode: 'row', showStatus: true}"
      :data="tableData">
      <vxe-column type="checkbox" width="60"></vxe-column>
      <vxe-column  width="15%" field="title" title="菜单名称" tree-node></vxe-column>
      <vxe-column align="center"  width="35%" field="path" title="路由"></vxe-column>
      <!--<vxe-column field="component" title="组件路径" :edit-render="{name: 'input'}"></vxe-column>-->
      <vxe-column align="center"  width="30%" field="permission" title="Permission"></vxe-column>
      <vxe-column align="center"  title="操作">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="primary"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>

          <el-button
            size="small"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>

      </vxe-column>
    </vxe-table>

    <el-dialog @close="refreshData" :title="formData.id==null?'新增菜单信息':'修改菜单信息'" :visible.sync="dialogOpen" width="43%"
               append-to-body
               :close-on-click-modal="false">
      <el-form ref="form" :model="formData" :rules="rules" label-width="80px">
        <el-row>

          <el-col :span="12">
            <el-form-item label="菜单类型" prop="menuType">
              <el-select v-model="formData.menuType" placeholder="请选择">
                <el-option
                  v-for="item in menuTypeList"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item label="上级菜单" prop="parentId">

              <treeselect
                v-model="formData.parentId"
                :options="treeData"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="选择上级菜单"
              />

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="title">
              <el-input v-model="formData.title" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由" prop="path">
              <el-input v-model="formData.path" placeholder="请输入路由" type="text"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="组件路径" :prop="componentValue">
              <el-input v-model="formData.component" placeholder="请输入组件路径" type="text"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="序号" prop="sort">
              <el-input v-model="formData.sort" placeholder="序号" type="number" max="999999" min="0"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label-width="100px" label="Permission" prop="permission">
              <el-input v-model="formData.permission" placeholder="请输入Permission" type="text"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label-width="90px" label="图标" prop="menuIcon">
              <d2-container>
                <div class="title-group">
                  <p class="sub-title">
                    <template v-if="formData.icon">
                      <!-- 选择的图标 {{formData.menuUrl}}-->
                      <!--<i :class="'fa fa-' + formData.menuUrl"></i>-->
                    </template>
                    <!--<template v-else>未选择</template>-->
                  </p>
                </div>
                <d2-icon-select v-model="formData.icon"/>
              </d2-container>

            </el-form-item>
          </el-col>

        </el-row>
        <el-row>

          <el-col :span="12">
            <el-form-item>
              <el-switch
                v-model="formData.isCache"
                active-text="缓存"
                inactive-text="不缓存">
              </el-switch>
            </el-form-item>

          </el-col>

          <el-col :span="12">
            <el-form-item>
              <el-switch
                v-model="formData.isHidden"
                active-text="隐藏"
                inactive-text="不隐藏">
              </el-switch>
            </el-form-item>

          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelSubmit">取 消</el-button>
      </div>
    </el-dialog>
  </d2-container>

</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import axios from 'axios/index'

export default {
  components: { Treeselect },
  data () {
    return {
      componentValue: 'component',
      loading: false,
      tableData: [],
      removeList: [],
      isShowSelect: false,
      treeData: [],
      treeConfig: {
        children: 'children',
        expandAll: true
      },
      // 弹出dialog
      dialogOpen: false,
      formData: {},
      rules: {
        menuType: [
          { required: true, message: '菜单类型不能为空', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '菜单序号不能为空', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '路由不能为空', trigger: 'blur' }
        ],
        component: [
          { required: true, message: '组件路径不能为空', trigger: 'blur' }
        ],
        permission: [
          { required: true, message: '权限标识不能为空', trigger: 'blur' }
        ]
      },
      menuTypeList: [
        {
          name: '目录',
          value: 'M'
        },
        {
          name: '菜单',
          value: 'C'
        },
        {
          name: '按钮',
          value: 'F'
        }
      ]
    }
  },
  created () {
    this.getResourcesByPage()
  },
  watch: {
    'formData.menuType' () {
      if (this.formData.menuType === 'M') {
        this.componentValue = ''
      } else {
        this.componentValue = 'component'
      }
    }
  },
  methods: {
    handleDelete (row) {
      this.deleteResources([row.id])
    },
    refreshData () {
      this.cancelSubmit()
    },
    cancelSubmit () {
      this.formData = {}
      this.dialogOpen = false
    },
    submitForm () {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 由于d2admin的组装菜单的util所用字段与resource表字段不一致，此处数据转换一下
          // TODO优化 修改resource表字段或更改d2admin的组装菜单的util所用字段
          this.formData.name = this.formData.title
          this.formData.menuUrl = this.formData.path
          this.formData.menuIcon = this.formData.icon
          this.$axiosRequest('/system/resource/saveOrEditResource', this.formData, this.$axiosOptions('post'))
            .then((res) => {
              this.$confirm(res.msg, '提示', {
                showCancelButton: false,
                type: res.success ? 'success' : 'warning'
              })
              if (res.success) {
                this.formData = {}
                this.dialogOpen = false
                this.getResourcesByPage()
              }
            })
        }
      })
    },
    /** 转换菜单数据结构 */
    normalizer (node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.title,
        children: node.children
      }
    },

    handleUpdate (row) {
      console.log(row)
      this.formData = JSON.parse(JSON.stringify(row))
      this.dialogOpen = true
    },

    openAddResource () {
      this.dialogOpen = true
    },
    deleteResourcesByIds () {
      const $table = this.$refs.xTree
      const selectRecords = $table.getCheckboxRecords()
      if (selectRecords.length <= 0) {
        this.$confirm('请选择要删除的数据 ', '提示', {
          showCancelButton: false,
          type: 'warning'
        })
        return
      }
      const ids = selectRecords.map(item => {
        return item.id
      })

      this.deleteResources(ids)
    },
    deleteResources (ids) {
      this.$confirm('是否确认删除这些数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除
        axios({
          method: 'post',
          url: '/system/resource/deleteResourceByIds',
          data: JSON.stringify(ids),
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          },
          timeout: 30000,
          responseType: 'json'
        }).then((res) => {
          console.log('res', res)
          this.$confirm(res.data.msg, '提示', {
            showCancelButton: false,
            type: res.data.code === '200' ? 'success' : 'warning'
          })
          if (res.data.success) {
            this.getResourcesByPage()
          }
        })
      })
    },
    getResourcesByPage () {
      this.loading = true
      this.$axiosRequest('/system/resource/getAllResources', null, this.$axiosOptions('get'))
        .then((res) => {
          this.tableData = res.resources
          this.loading = false
          this.treeData = JSON.parse(JSON.stringify(this.tableData))
          // this.treeData.unshift({
          //  id:null,
          //  name:'无',
          //  children:[]
          // })
        })
    },

    reload () {
      // 清除所有状态
      this.$refs.xTree.clearAll()
      return this.getResourcesByPage()
    }

  }
}
</script>

<style scoped>

</style>
