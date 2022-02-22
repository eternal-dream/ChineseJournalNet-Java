<template>
    <div>
        <el-dialog title='新增IP' :visible.sync="visible" width="25%" :before-close="close" :close-on-click-modal="false" :inline="true">
            <el-form :model="ipInfo" >
                <el-form-item label="开始IP" prop="startIp" label-width="100px">
                    <el-input v-model="ipInfo.startIp" />
                </el-form-item>
                <el-form-item label="结束IP" prop="finishIp" label-width="100px">
                    <el-input v-model="ipInfo.finishIp" />
                </el-form-item>
                <el-form-item label="IP绑定单位" prop="unit" label-width="100px">
                    <el-input v-model="ipInfo.unit" />
                </el-form-item>
                <el-form-item label="IP备注" prop="remark" label-width="100px">
                    <el-input type="textarea" v-model="ipInfo.remark" />
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="close">取 消</el-button>
                <el-button  el-button type="primary"  @click="addIp">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
  props: [
    'user'
  ],
  data () {
    return {
      visible: false,
      ipInfo: {}
    }
  },
  methods: {
    close () {
      this.visible = false
    },
    show () {
      this.ipInfo = {}
      this.visible = true
    },
    addIp () {
      this.$emit('submit', this.ipInfo)
      this.visible = false
    }
  }
}

</script>
