<template>
    <div id="searcher">
        <div class="layui-form l-width">
            <div v-if="$route.path != '/qikan/index'" class="search-label">期刊文献<sup>+</sup></div>
            <div class="search">
                <div class="search-sel">
                    <el-select id="Select11" v-model="searchParam.searchModels[0].searchField" >
                        <el-option value="U" label="任意字段"></el-option>
                        <el-option value="M" label="题名或关键词"></el-option>
                        <el-option value="T" label="题名"></el-option>
                        <el-option value="K" label="关键词"></el-option>
                        <el-option value="R" label="文摘"></el-option>
                        <el-option value="A" label="作者"></el-option>
                        <el-option value="F" label="第一作者"></el-option>
                        <el-option value="S" label="机构"></el-option>
                        <el-option value="J" label="刊名"></el-option>
                        <el-option value="C" label="分类号"></el-option>
                        <!-- <el-option value="Y" label="参考文献"></el-option>
                        <el-option value="Z" label="作者简介"></el-option> -->
                        <el-option value="I" label="基金资助"></el-option>
                        <el-option value="L" label="栏目信息"></el-option>
                    </el-select>
                </div>
                <div class="search-input">
                    <el-autocomplete
                      class="inline-input"
                      style="width:100%"
                      v-model="searchParam.searchModels[0].value"
                      :fetch-suggestions="autoComplete"
                      :trigger-on-focus="false"
                      @blur="inputTime=new Date().getTime()"
                      @select="handleSeachClick()"
                    ></el-autocomplete>
                </div>
                <div class="search-btn">
                    <button @click="handleSeachClick()"  class="layui-btn">检索</button>
                </div>
                <ul class="search-tool">
                    <li><router-link to="/search/advance" target="_blank">高级检索</router-link></li>
                    <li><a v-if=" $route.path != '/qikan/index'" href="javasrcipt:void(0)" @click="jumpJournalIndex">期刊导航</a></li>
                    <!-- <li><a href="history.html">检索历史</a></li> -->
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
import axios from 'axios'
export default {
  name: 'Searcher',
  data () {
    return {
      searchParam: {
        pageNum: 1,
        pageSize: 20,
        sortField: '',
        fq: '', // 用于二次聚类的条件过滤
        searchExpression: '',
        searchByExpression: false,
        searchModels: [// 检索条件数组，默认第一条为输入框检索条件
          {
            searchField: 'U',
            value: '',
            exact: false, // 是否精确检索
            logicOperator: 'AND',
            displayValue: ''
          }
        ],
        inputTime: 0 // 记录上一次输入内容的时间
      }
    }
  },
  methods: {
    search (param) {
      let searchParam = this.searchParam
      if (param) {
        const simpleSearchModel = this.searchParam.searchModels[0]
        searchParam = param
        searchParam.searchModels.unshift(simpleSearchModel)
      }
      this.$emit('loadingFacet')
      const loading = this.$loading({
        lock: true,
        text: '加载中...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.5)'
      })
      axios({
        method: 'post',
        url: 'view/search/search',
        data: searchParam
      }).then(res => {
        if (!res.data.success) {
          this.$message.error(res.data.msg)
          return
        }
        this.$emit('completeSearch', res)
        loading.close()
      }).catch(e => { loading.close() })
      axios({
        method: 'post',
        url: 'view/search/getFacet',
        data: searchParam
      }).then(res => {
        if (!res.data.success) {
          this.$message.error(res.data.msg)
          return
        }
        if (searchParam.searchModels[0].searchField === 'J' && searchParam.searchModels[0].value && res.data.facet.mediaFacet && res.data.facet.mediaFacet.length > 0) {
          res.data.facet.mediaFacet[0].keyword = searchParam.searchModels[0].value
          this.$emit('showMedias', res.data.facet.mediaFacet)
        }
        this.$emit('completeGetFacet', res)
      })

      // axios({
      //   method: 'post',
      //   url: 'view/search/test',
      //   data: searchParam
      // }).then(res => {
      //   console.log(res.data)
      // })
    },
    handleSeachClick () {
      if (this.$route.path !== '/search/index') { // 如果当前页面不是在检索页则进行跳转
        this.$router.push({ path: '/search/index', query: { searchModels: JSON.stringify(this.searchParam.searchModels) } })
      } else {
        // this.$router.replace('/refresh')
        this.search()
      }
    },
    autoComplete (value, callback) {
      this.inputTime = new Date().getTime()
      setTimeout(() => {
        const currentTime = new Date().getTime()
        if (currentTime - this.inputTime >= 680 && this.searchParam.searchModels[0].value) {
          this.$axiosRequest('view/search/autoComplete', { value: this.searchParam.searchModels[0].value }, this.$axiosOptions('get'))
            .then(res => {
              const suggests = []
              res.suggests.forEach(item => {
                suggests.push({ value: item })
              })
              callback(suggests)
            })
        } else {
          callback([])
        }
      }, 700)
    },
    getSubFacet (param, fq, url) {
      let searchParam = this.searchParam
      if (param) {
        const simpleSearchModel = this.searchParam.searchModels[0]
        searchParam = param
        searchParam.searchModels.unshift(simpleSearchModel)
      }
      searchParam.fq = fq
      return new Promise(resolve => {
        axios({
          method: 'post',
          url: url,
          data: searchParam
        }).then(res => {
          resolve(res)
        })
      })
    },
    handleKeyDown (e) {
      let key = null
      if (window.event === undefined) {
        key = e.keyCode
      } else {
        key = window.event.keyCode
      }
      if (key === 13) {
        // 触发的事件
        this.handleSeachClick()
      }
    },
    jumpJournalIndex () {
      if (this.$route.path === '/journal/index') {
        this.$router.replace('/refresh')
      } else {
        this.$router.push({ path: '/journal/index' })
      }
    }
  },
  mounted () {
    if (this.$route.query && this.$route.query.searchModels) {
      this.searchParam.searchModels = JSON.parse(this.$route.query.searchModels)
    } else {
      this.searchParam.searchModels = [{
        searchField: 'U',
        value: '',
        exact: false, // 是否精确检索
        logicOperator: 'AND',
        displayValue: ''
      }]
    }
    if (this.$route.path === '/search/index') {
      this.search()
    }
  },
  created () {
    window.addEventListener('keydown', this.handleKeyDown, true) // 开启监听键盘按下事件
  },
  destroyed () {
    window.removeEventListener('keydown', this.handleKeyDown, true)
  }
}
</script>
