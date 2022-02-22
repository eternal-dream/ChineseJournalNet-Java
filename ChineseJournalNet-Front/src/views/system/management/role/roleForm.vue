<template>
    <div>
        <el-dialog :title='title' :visible.sync="visible" width="30%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form ref="roleForm" :model="role" >
                <el-form-item label="角色名称" prop="name" label-width="100px"
                    :rules="{ required: true, message: '请输入角色名称!', trigger: 'blur'}">
                    <el-input v-model="role.name" placeholder="请输入角色名称" />
                </el-form-item>
                <el-form-item label="描述" prop="description" label-width="100px">
                    <el-input type="textarea" v-model="role.description" placeholder="请描述角色用途" />
                </el-form-item>
                <el-form-item label="权限设置" label-width="100px">
                    <el-tree
                        :data="resources"
                        show-checkbox
                        node-key="id"
                        ref="tree"
                        :highlight-current="true"
                        :props="{ children: 'children', label: 'title' }"
                        @check-change="getChooseData"
                    >
                        <span class="custom-tree-node" slot-scope="{node,data}">
                            <span>
                                {{node.label}}
                                <el-tag v-if="data.menuType==='C'" size="mini" >菜单</el-tag>
                                <el-tag v-if="data.menuType==='M'" size="mini" type="info">目录</el-tag>
                                <el-tag v-if="data.menuType==='F'" size="mini" type="warning">按钮</el-tag>
                            </span>
                        </span>
                    </el-tree>
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
export default {
  props: [
    'searchRole'
  ],
  data () {
    return {
      visible: false,
      role: {
        resourceIds: []
      },
      title: '',
      resources: []// 所有权限用于授权
    }
  },
  created () {
    this.getResources()
  },
  methods: {
    show (role) {
      this.role = JSON.parse(JSON.stringify(role))
      if (role && role.id) {
        this.title = '编辑角色'
        if (role.resourceIds && role.resourceIds.length > 0) {
          role.resourceIds.forEach(id => {
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, true, false)
            })
          })
        }
      } else {
        this.title = '新增角色'
      }
      this.visible = true
    },
    close () {
      this.$refs.roleForm.resetFields()
      this.$refs.tree.setCheckedKeys([])
      this.visible = false
    },
    getResources () {
      this.$axiosRequest('/system/resource/getAllResources', {}, this.$axiosOptions('get'))
        .then(res => {
          this.resources = res.resources
        })
    },
    getChooseData () {
      const elTree = this.$refs.tree
      const nodes = elTree.getCheckedNodes(false, true)
      const arr = []
      nodes.forEach((item) => {
        arr.push(item.id)
      })
      this.role.resourceIds = arr
    },
    submit () {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          this.$axiosRequest('/system/role/addOrModifyRole', JSON.stringify(this.role), this.$axiosOptions('post', 'application/json'))
            .then(res => {
              if (!res.success) {
                this.$message.error(res.msg)
                return
              }
              this.$message.success('操作成功!')
              this.visible = false
              this.close()
              this.searchRole()
            })
        }
      })
    }
  }
}

</script>
