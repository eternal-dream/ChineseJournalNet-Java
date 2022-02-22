<template>
    <div>
        <vip-header ref="header"/>
        <searcher ref="searcher"
          @showMedias="showMedias"
          @loadingFacet="facet = []"
          @completeSearch="completeSearch"
          @completeGetFacet="completeGetFacet"></searcher>
        <search-result ref="searchResult"
          :facet="facet"
          :data="pageBean"
          :search="search"
          :medias="medias"
          @changeSearchModel="changeSearchModel"
          :getSubFacet="getSubFacet" />
    </div>
</template>

<script>
// import header from '@/views/front/components/header.vue'
import SearchResult from '@/views/front/components/searchResult.vue'
export default {
  name: 'SearchIndex',
  components: {
    // viewHeader: header,
    SearchResult
  },
  data () {
    return {
      pageBean: {},
      facet: [],
      loading: false,
      medias: []
    }
  },
  methods: {
    // 检索成功调用的回调函数
    completeSearch (res) {
      this.pageBean = res.data.pageBean
    },
    completeGetFacet (res) {
      this.facet = res.data.facet
    },
    // 简单检索条件更改调用的回调函数
    changeSearchModel (searchModels) {
      this.$refs.searcher.searchParam.searchModels = searchModels
      this.search()
    },
    // 检索条件更改调用的回调函数
    search (searchParam) {
    //   this.$refs.searcher.searchParam = searchParam
      this.$refs.searcher.search(searchParam)
    },
    // 二次聚类调用函数
    getSubFacet (searchParam, fq, url) {
      return new Promise(resolve => {
        this.$refs.searcher.getSubFacet(searchParam, fq, url)
          .then(res => {
            resolve(res.data.facet)
          })
      })
    },
    showMedias (medias) {
      this.medias = medias
    }

  }
}
</script>
