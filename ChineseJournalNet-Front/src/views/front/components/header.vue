<template>
    <div id="header">
        <div class="l-width">
            <div class="logo"><router-link to="/qikan/index"><img src="@/assets/images/logo.png" alt="维普期刊服务平台"></router-link></div>
            <ul class="head-nav">
                <li><a href="javasript:void(0)" @click="jumpJournalIndex">期刊导航</a></li>
            </ul>
            <ul class="user-nav" id="user-nav">
                <li v-if="!info.id"><a href="javascript:void(0)" @click="$refs.login.show()" id="login-btn"><i class="icon-login"></i>登录</a></li>
                <li v-else><a href="javascript:void(0)"><i class="icon-login"></i>{{info.userName}}</a></li>
                <li v-if="info.id"><a href="javascript:void(0)" @click="$store.dispatch('front/user/logout')"><d2-icon name="sign-out"></d2-icon>退出</a></li>
            </ul>
        </div>
        <login ref="login" />
    </div>
</template>
<script>
import login from '@/views/front/components/login.vue'
import { mapState } from 'vuex'
export default {
  name: 'Header',
  components: {
    login
  },
  data () {
    return {
      userInfo: {},
      codeUrl: '',
      searchParams: []
    }
  },
  computed: {
    ...mapState('front/user', [
      'info'
    ])
  },
  mounted () {
    if (!this.info.id && this.logoutSign === false) {
      this.$store.dispatch('front/user/tryIpLogin')
    }
  },
  methods: {
    jumpJournalIndex () {
      if (this.$route.path === '/journal/index') {
        this.$router.replace('/refresh')
      } else {
        this.$router.push({ path: '/journal/index' })
      }
    }
  }
}

</script>
