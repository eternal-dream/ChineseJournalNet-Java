<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password" />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="user.newPassword" placeholder="请输入新密码" type="password" />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="请确认密码" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="closeBtn">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateMyselfPassword } from "@/api/system/admin.api";
import { Message } from 'element-ui'
import { mapActions } from 'vuex'

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      test: "1test",
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: "旧密码不能为空", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
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
          let param = {}
          param.oldPassword = this.user.oldPassword
          param.newPassword = this.user.newPassword
          updateMyselfPassword(param)
            .then(res => {
              if (res.success==true){
                Message.success("修改成功");
              }else {
                Message.error(res.msg);
              }
            }
          );
        }
      });
    },
    closeBtn() {
      this.close({tagName:this.$route.path})
    }
  }
};
</script>
