<template>
  <div id="app"
       v-if="!$route.meta.front">
    <router-view />
  </div>
  <div v-else>
    <router-view />
    <div class="web-tools">
      <a href="/help/index" target="_blank"><span><i class="icon-help"></i>使用帮助</span></a>
      <a href="#top"><span><i class="icon-top"></i>返回顶部</span></a>
    </div>
    <vip-footer v-if="$route.path != '/journal/index'"></vip-footer>
  </div>
</template>

<script>
import util from '@/libs/util'
export default {
  name: 'app',
  watch: {
    '$i18n.locale': 'i18nHandle'
  },
  created () {
    this.i18nHandle(this.$i18n.locale)
  },
  methods: {
    i18nHandle (val, oldVal) {
      util.cookies.set('lang', val)
      document.querySelector('html').setAttribute('lang', val)
    }
  }
}
</script>

