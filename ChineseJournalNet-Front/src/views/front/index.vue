<template>
    <div>
        <div class="part index-show show-type2"><!--未开放作者机构对象的模式用 type2-->
            <div class="l-width">
                <div class="show-top">
                    <ul class="top-nav">
                        <li class="top-nav-journal"><router-link to="/journal/index"><i class="icon-journal"></i>期刊导航</router-link></li>
                    </ul>
                    <ul class="user-nav">
                        <li v-if="!info.id"><a href="javascript:void(0)" @click="$refs.login.show()" id="login-btn"><i class="icon-login"></i>登录</a></li>
                        <li v-else><a href="javascript:void(0)"><i class="icon-login"></i>{{info.userName}}</a></li>
                        <li v-if="info.id"><a href="javascript:void(0)" @click="$store.dispatch('front/user/logout')"><d2-icon name="sign-out"></d2-icon>退出</a></li>
                    </ul>

                </div>
                <div class="show-logo"><img src="@/assets/images/show-logo.png" alt="维普期刊服务平台" /></div>
                <div class="show-text">已收录<strong>{{formatNumber(articleNumber)}}+</strong>条文献</div>
                <searcher ref="searcher"></searcher>
            </div>
        </div>
        <login ref="login" />
    </div>
</template>

<script>
import login from '@/views/front/components/login.vue'
import { mapState } from 'vuex'
export default {
  name: 'Index',
  components: {
    login
  },
  data () {
    return {
      articleNumber: 71094272
    }
  },
  computed: {
    ...mapState('front/user', [
      'info',
      'logoutSign',
      'loginSign'
    ])
  },
  mounted () {
    this.getArticleNumber()
    if (this.loginSign === false && this.logoutSign === false) {
      this.$store.dispatch('front/user/tryIpLogin')
    }
  },
  methods: {
    getArticleNumber () {
      this.$axiosRequest('view/search/getArticleNumber', {}, this.$axiosOptions('get'))
        .then(res => {
          this.articleNumber = res.number
        })
    },
    formatNumber (num) {
      if (!num) {
        return 0
      }
      return num.toString().replace(/\d+/, function (n) {
        return n.replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      })
    }
  },
  watch: {
    info (oldVal, newVal) {
      if (!this.info.id) {
        this.$refs.login.show()
      }
    }
  }
}
</script>
<style scoped src="@/assets/css/index.css"></style>
