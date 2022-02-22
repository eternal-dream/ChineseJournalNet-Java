<template>
  <el-dropdown size="small" class="d2-mr">
    <span class="btn-text">{{ info.userName ? `你好 ${info.userName}` : '未登录' }}
    <!-- <img :src="info.avatar ? imgUrl+this.info.avatar : require('@/assets/image/head-pic.png')" class="user-avatar"> -->
    </span>
    <el-dropdown-menu slot="dropdown">
      <!-- <el-dropdown-item @click.native="toProfile">
        <d2-icon name="address-card-o" class="d2-mr-5"/>个人中心
      </el-dropdown-item> -->
      <el-dropdown-item @click.native="logOff">
        <d2-icon name="power-off" class="d2-mr-5"/>注销
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data(){
    return{
      imgUrl:process.env.VUE_APP_API
    }
  },
  methods: {
    ...mapActions('d2admin/account', [
      'Logout'
    ]),
    /**
     * @description 登出
     */
    logOff() {
      this.Logout({
        confirm: true
      })
    },
    /**
     * 跳转个人中心
     */
    toProfile(){
      this.$router.push({
        name:"Profile"
      })
    }
  }
}
</script>
<style scoped>
.user-avatar{
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  vertical-align: middle;
  margin-left: 10px;
}
</style>
