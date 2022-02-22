<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px" size="small">
    <el-form-item label="用户名" prop="loginName">
      <el-input v-model="user.userName" />
    </el-form-item>
    <el-form-item label="真实姓名" prop="realName">
      <el-input v-model="user.realName" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" />
    </el-form-item>
    <el-form-item label="工号" prop="number">
      <el-input v-model="user.number" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phoneNumber">
      <el-input v-model="user.phoneNumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="user.remark" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="closeBtn">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { Message } from 'element-ui'
import { updateInfoWithOutOther } from '@api/system/admin.api'
import { validEmail } from '@/utils/validate'
import { mapActions } from 'vuex'

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    const validateEmail= (rule,value,callback) =>{
      if (!value){
        callback(new Error('密码不能为空'));
      }else {
        let b = validEmail(value)
        if (!b){
          callback(new Error("密码最少6位，包括至少1个大写字母，1个小写字母，1个数字"))
        }
        callback()
      }
    };
    return {
      // 表单校验
      rules: {
        loginName: [
          { required: true, message: "登录名称不能为空", trigger: "blur" }
        ],
        phoneNumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        email:[
          {required: true, validator: validateEmail, trigger: "blur"}
        ],
      }
    };
  },
  methods: {
    ...mapActions('d2admin/page', [
      'close',
    ]),
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateInfoWithOutOther(this.user).then(response => {
            Message.success("修改成功");
          });
        }
      });
    },
    closeBtn() {
      this.close({tagName:this.$route.path})
    }
  }
};
</script>
