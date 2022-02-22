<template>
  <div>
    <vip-header/>
    <searcher ref="searcher"
              @completeSearch="completeSearch"
              @completeGetFacet="completeGetFacet"></searcher>
    <div class="l-width" v-loading="loading">
      <div class="row object journal">
        <div class="object-intro">
          <div class="object-title">
            <h1 v-if="journal.media">《{{journal.media}}》
              <span class="medal">
              <!--<span>CSCD</span><span>北大核心期刊</span>-->
              </span>
            </h1>
            <!--<em>Chinese clinician magazine</em>-->
          </div>
          <div class="object-op">
            <!--<a href="#"><i class="icon-favorite"></i></a>
            <a href="#"><i class="icon-share"></i></a>
            <a href="#"><i class="icon-export"></i></a>-->
          </div>
          <div class="object-count">
            <span>
              <span class="label">作品数</span>
              <a href="#">{{journal.zkfwcount}}</a>
            </span>
            <span>
             <span class="label">被引量</span>{{journal.zkbycount}}
            </span>
            <!--<span>
              <span class="label">H指数</span>10
            </span>-->
          </div>
          <div class="object-cover">
            <img  :src="buildImgUrl(journal.gch)" :onerror="loadDefaultImg"
                 :alt="'《'+journal.media+'》'">

            <i v-if="journal.accessmode=='OA'" class="icon-oa-pic"></i>
          </div>
          <div class="object-info-content">
            <span v-if="journal.introduction &&journal.introduction.length>200">
              {{journal.introduction.substring(0,200)}}......
            </span>
            <span v-else-if="journal.introduction &&journal.introduction.length<=200">
               {{journal.introduction}}
            </span>
            <br/>
            <a v-if="journal.introduction" href="javascript:void(0);" @click="changeTab('detail')">
              查看详情>>
            </a>
          </div>
          <ul class="object-intro-list">
            <li v-if="journal.oldname"><span class="label">曾用名</span><span>{{journal.oldname}}</span></li>
            <li v-if="journal.publisher"><span class="label">主办单位</span>{{journal.publisher}}</li>
            <li class="issn" v-if="journal.issn"><span class="label">国际标准连续出版物号</span>{{journal.issn}}</li>
            <li class="cn" v-if="journal.cn"><span class="label">国内统一连续出版物号</span>{{journal.cn}}</li>
            <li v-if="journal.periodtype"><span class="label">出版周期</span>{{journal.periodtype}}</li>
          </ul>
        </div>
        <div class="object-tab">
          <ul>
            <li :class="tabFlag=='detail'?'tab-this':''">
              <a href="javascript:void(0);" @click="changeTab('detail')">期刊详情</a>
            </li>
            <li :class="tabFlag=='summary'?'tab-this':''">
              <a href="javascript:void(0);" @click="changeTab('summary')">收录汇总</a>
            </li>
            <li :class="tabFlag=='content'?'tab-this':''">
              <a href="javascript:void(0);" @click="changeTab('content')">发表作品</a>
            </li>
          </ul>
        </div>
        <div class="object-main">
          <div v-if="tabFlag=='detail'" class="row journal-info">
            <div class="info-left">
              <div class="info-box">
                <div class="title">
                  <h2>期刊信息</h2>
                </div>
                <div class="info-content">
                  <div class="info-list l1">
                    <ul>
                      <li v-if="journal.oldname"><span class="label">曾用名</span>{{journal.oldname}}</li>
                      <li v-if="journal.organizers"><span class="label">主管单位</span>{{journal.organizers}}</li>
                      <li v-if="journal.publisher"><span class="label">主办单位</span>{{journal.publisher}}</li>
                      <li v-if="journal.editor"><span class="label">总编/主编</span>{{journal.editor}}</li>
                      <li v-if="journal.address"><span class="label">地址</span>{{journal.address}}</li>
                      <li v-if="journal.postcode"><span class="label">邮政编码</span>{{journal.postcode}}</li>
                      <li v-if="journal.tel"><span class="label">电话</span>{{journal.tel}}</li>

                      <li v-if="journal.email"><span class="label">电子邮件</span>
                        <a id="emails"
                           href="mailto:jcphysician@sohu.com?from=journal_summary"><span>{{journal.email}}</span></a>
                      </li>

                      <!--<li class="issn" v-if="journal.issn"><span class="label">国际标准连续出版物号</span>{{journal.issn}}</li>
                      <li class="cn" v-if="journal.cn"><span class="label">国内统一连续出版物号</span>{{journal.cn}}</li>-->
                      <li v-if="journal.yfdh"><span class="label">邮发代号</span>{{journal.yfdh}}</li>
                      <li v-if="journal.unitprice"><span class="label">单价</span>{{journal.unitprice}}</li>
                      <li v-if="journal.price"><span class="label">定价</span>{{journal.price}}</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
            <div class="info-main">
              <div class="info-box" v-if="journal.introduction">
                <div class="title">
                  <h2>期刊简介</h2>
                </div>
                <div class="info-content">
                  <p>{{journal.introduction}}</p>
                </div>
              </div>
              <div class="info-box" v-if="journal.awards">
                <div class="title">
                  <h2>获奖情况</h2>
                </div>
                <div class="info-content">
                  <div class="info-list">
                    <ul>
                      <li :title="journal.awards">{{journal.awards}}</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="info-box" v-if="lngrange.length>0">
                <div class="title">
                  <h2>收录情况</h2>
                </div>
                <div class="info-content">
                  <div class="info-list">
                    <ul>
                      <li v-for="item in lngrange" :title="item.databaseName">
                        {{item.nationName}}·{{item.databaseName}}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="tabFlag=='summary'" class="row search-list">
            <div class="search-left">
              <div class="layui-form search-cluster">
                <div class="layui-collapse" lay-filter="test">
                  <!--<div class="layui-colla-item search-refine">
                    <h2 class="layui-colla-title">刊内检索<i class="layui-icon layui-colla-icon"></i></h2>
                    <div class="layui-colla-content ">
                      <ul class="cluster-term">
                        <li><a class="cluster-close" href="#"><i class="icon-close"></i></a><span class="cluster-name">作者=胡大一</span>
                        </li>
                      </ul>
                      <div class="refine-line">
                        <div class="refine-sel">
                          <select id="Select11" value="M">
                            <option value="T" selected="selected">题名</option>
                            <option value="K">关键词</option>
                            <option value="R">文摘</option>
                            <option value="A">作者</option>
                            <option value="F">第一作者</option>
                            <option value="S">机构</option>
                            <option value="J">刊名</option>
                            <option value="C">分类号</option>
                            <option value="Y">参考文献</option>
                            <option value="Z">作者简介</option>
                            <option value="I">基金资助</option>
                            <option value="L">栏目信息</option>
                          </select>
                          <div class="layui-unselect layui-form-select">
                            <div class="layui-select-title"><input type="text" placeholder="请选择" value="题名" readonly=""
                                                                   class="layui-input layui-unselect"><i
                              class="layui-edge"></i></div>
                            <dl class="layui-anim layui-anim-upbit">
                              <dd lay-value="T" class="layui-this">题名</dd>
                              <dd lay-value="K" class="">关键词</dd>
                              <dd lay-value="R" class="">文摘</dd>
                              <dd lay-value="A" class="">作者</dd>
                              <dd lay-value="F" class="">第一作者</dd>
                              <dd lay-value="S" class="">机构</dd>
                              <dd lay-value="J" class="">刊名</dd>
                              <dd lay-value="C" class="">分类号</dd>
                              <dd lay-value="Y" class="">参考文献</dd>
                              <dd lay-value="Z" class="">作者简介</dd>
                              <dd lay-value="I" class="">基金资助</dd>
                              <dd lay-value="L" class="">栏目信息</dd>
                            </dl>
                          </div>
                        </div>
                        <div class="refine-input">
                          <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入标题"
                                 class="layui-input">
                        </div>
                      </div>
                      <div class="refine-submit">
                        <button class="layui-btn">检索</button>
                      </div>
                    </div>
                  </div>-->
                  <div class="layui-colla-item y-vol">
                    <h2 class="layui-colla-title">刊期<i class="layui-icon layui-colla-icon"></i></h2>
                    <div class="layui-colla-content layui-show">
                      <ul class="cluster-list">
                        <li :key="index" v-for="(item,index) in currentPageData" class="cluster-item show-more">
                          <a href="javascript:void(0);" @click="item.open=!item.open">{{item.year}}</a>
                          <ul class="cluster-list " v-show="item.open">
                            <li v-for="val in item.num" class="cluster-item">
                              <a href="javascript:void(0);"
                                 @click="numClick(journal.gch5+item.year+val,item.year,val)">{{val}}</a></li>
                          </ul>
                        </li>
                      </ul>
                      <div class="page">
                        <div id="demo1" class="page">
                          <el-pagination
                            :current-page="pageNum"
                            :page-size="pageSize"
                            :total="total"
                            @current-change="handlePageChange"
                            small
                            layout="prev, pager, next">
                          </el-pagination>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="layui-form search-main">
              <div class="journal-catalog" id="ajaxJournalDiv">
                <h2 class="catalog-vol">{{currentYear}}年 第{{currentNum}}期</h2>
                <template v-for="item in summaryContent">

                  <h6>{{item.muiInfo}}</h6>
                  <ul>
                    <li v-for="subItem in item.summarys"><span class="title">
                      <a href="javascript:void(0);" @click="toDetail(subItem.lngid)">{{subItem.title}}</a></span>
                      <tt>
                        <span class="writer">{{removeBlankAndNum(subItem.writer)}}</span>
                        <span class="pages" v-if="subItem.beginpage&&subItem.endpage">({{subItem.beginpage}}-{{subItem.endpage}})</span>
                        <span class="pages" v-else-if="subItem.endpage">({{subItem.endpage}})</span>
                        <span class="pages" v-else-if="subItem.beginpage">({{subItem.beginpage}})</span>
                      </tt>
                    </li>
                  </ul>
                </template>
              </div>

            </div>
          </div>

          <div v-else-if="tabFlag=='content'">
            <search-result ref="searchResult"
                           :facet="facet"
                           :data="pageBean"
                           :search="search"
                           :getSubFacet="getSubFacet"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import SearchResult from '../components/searchResult'
  import axios from 'axios'

  export default {
    name: 'index',
    components: { SearchResult },
    data () {
      return {
        loadDefaultImg: 'this.src="' + require('@/assets/images/default_qk.jpg') + '"',
        defaultFlag: true,

        tabFlag: 'summary',
        //期刊信息
        journal: {},
        //期刊收录年份及情况
        journalSummaryYear: [],

        //收录汇总年限
        pageNum: 1,
        pageSize: 5,
        total: 0,
        currentPageData: [],
        summaryYearList: [],
        //收录汇总
        summaryContent: [],
        currentYear: null,
        currentNum: null,
        loading: false,
        pageBean: {},
        facet: [],
        baseParam: null,
        searchParam: {
          pageNum: 1,
          pageSize: 20,
          sortField: '',
          // 用于二次聚类的条件过滤
          fq: '',
          searchExpression: '',
          searchByExpression: false,
          // 检索条件数组，默认第一条为输入框检索条件
          searchModels: [],
          // 记录上一次输入内容的时间
          inputTime: 0
        },
        //收录情况
        lngrange: []
      }
    },
    methods: {
      toDetail (lngid) {
        if (lngid) {
          let routeUrl = this.$router.resolve({
            path: '/detail/index',
            query: {
              lngid: lngid
            }
          })
          window.open(routeUrl.href, '_blank')
        }
      },
      // 检索成功调用的回调函数
      completeSearch (res) {
        this.pageBean = res.data.pageBean
      },
      completeGetFacet (res) {
        this.facet = res.data.facet
      },
      // 检索条件更改调用的回调函数
      search (param) {
        let searchParam = param
        // 基础检索，自带馆藏号好
        searchParam.searchModels.unshift(this.baseParam)
        axios({
          method: 'post',
          url: 'view/journalsDetail/search',
          data: searchParam
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          //this.completeSearch(res)
          this.pageBean = res.data.pageBean
        })
        axios({
          method: 'post',
          url: 'view/search/getFacet',
          data: searchParam
        }).then(res => {
          if (!res.data.success) {
            this.$message.error(res.data.msg)
            return
          }
          // this.$emit('completeGetFacet', res)
          this.facet = res.data.facet
        })
      },
      // 二次聚类调用函数
      getSubFacet (param, fq, url) {
        let searchParam = this.searchParam
        if (param) {
          const simpleSearchModel = this.searchParam.searchModels[0]
          searchParam = param
          searchParam.searchModels.unshift(simpleSearchModel)
        }
        searchParam = searchParam || this.searchParam
        searchParam.fq = fq
        return new Promise(resolve => {
          axios({
            method: 'post',
            url: url,
            data: searchParam
          }).then(res => {
            resolve(res.data.facet)
          })
        })
      },
      paginationChange () {

        this.pageData = []
        this.currentPageData = []
        let index = 0
        for (let i = 0; i < this.total; i++) {
          // 可以被 pageSize 整除
          if (i % this.pageSize === 0 && i !== 0) {
            this.pageData.push(this.journalSummaryYear.slice(index, i))
            index = i
          }

          if ((i + 1) === this.total) {
            this.pageData.push(this.journalSummaryYear.slice(index, (i + 1)))
          }
        }
        //默认每一页第一个为open
        this.pageData[this.pageNum - 1].forEach((item, index) => {
          if (index === 0) {
            item.open = true
          } else {
            item.open = false
          }
        })
        if (this.defaultFlag) {
          this.currentYear = this.pageData[this.pageNum - 1][0].year
          this.currentNum = this.pageData[this.pageNum - 1][0].num[this.pageData[this.pageNum - 1][0].num.length - 1]
        }

        this.getSummary(this.journal.gch5 + this.currentYear + this.currentNum)
        this.currentPageData = this.pageData[this.pageNum - 1]

      },
      handlePageChange (val) {
        this.pageNum = val
        this.paginationChange()
      },
      changeTab (flag) {
        this.tabFlag = flag
        if (flag === 'content') {
          this.getJournalContent(this.journal.gch5)
        }
      },
      /**
       * 获取期刊详情
       * @param gch5 期刊gch5
       */
      getJournalsDetail (gch5) {
        if (gch5) {
          this.loading = true
          let param = {
            index: 'media_info',
            // query: 'mediaid:' + mediaId
            query: 'gch5:' + gch5 + ' AND vipnow:1 '
          }
          this.$axiosRequest('view/journalsDetail/getJournalsDetail', param, this.$axiosOptions('get')).then(res => {
            console.log('res.data', res.data)
            this.lngrange = res.data.lngrange
            this.journal = res.data
            this.journalSummaryYear = res.data.gchyearsnum
            this.journalSummaryYear.forEach(item => {
              this.$set(item, 'open', false)
            })
            this.total = res.data.gchyearsnum.length
            this.paginationChange()
          }).finally(e => {
            this.loading = false
          })
        }
      },
      numClick (keyid, year, num) {
        this.currentYear = year
        this.currentNum = num
        this.getSummary(keyid)
      },
      /**
       * 获取收录汇总
       */
      getSummary (keyid) {
        this.loading = true
        let param = {
          index: 'gch_num_info',
          query: 'keyid:' + keyid
        }
        this.$axiosRequest('view/journalsDetail/getSummary', param, this.$axiosOptions('get')).then(res => {
          this.summaryContent = res.data.xml
        }).finally(e => {
          this.loading = false
        })
      },
      /**
       * 格式化文本，清除类似[1]文本
       * @param val
       * @returns {*}
       */
      removeBlankAndNum (val) {
        if (val) {
          var reg = /\[([0-9]*)\]/g
          val = val.replace(reg, '')
        }
        return val
      },
      getJournalContent (gch5) {
        if (gch5) {
          this.baseParam = {
            searchField: 'GCH5',
            value: gch5,
            exact: false, // 是否精确检索
            logicOperator: 'AND',
            displayValue: ''
          }
          this.search(this.searchParam)
        }
      },
      /**
       * 拼接封面路径地址
       * @param gch
       * @returns {string}
       */
      buildImgUrl (gch) {
        if (gch) {
          return 'http://image1.cqvip.com//vip1000/qkclearimg/' + gch.toLowerCase() + '/' + gch.toLowerCase() + '.jpg?site=d68adea8566ee0bb33fc319fa2279e77'
        }
        return '@/assets/images/default_qk.jpg'
      },


    },
    created () {
      // 接收参数
      let query = this.$route.query

      this.$axiosRequest('/view/shieldInfo/checkJournalDetail', { gch5: query.gch5 }, this.$axiosOptions('get')).then(res => {
        if(res.shielded){
          // 跳转首页
          location.href = '/qikan/index'
        }else{
          if (query && query.gch5 && query.num && query.year) {
            this.defaultFlag = false
            this.currentYear = query.year
            this.currentNum = query.num
            this.getJournalsDetail(query.gch5)
            this.changeTab('summary')
          } else if (query && query.gch5) {
            this.getJournalsDetail(query.gch5)
          }
        }
      })

    }

  }
</script>
<style scoped src="@/assets/css/object.css"></style>
<style scoped src="@/assets/css/search.css"></style>
<style scoped>
  .object-info-content {
    font-size: 14px;
    line-height: 22px;
    overflow: hidden;
    margin-bottom: 10px;
    max-height: max-content !important;
  }
</style>

