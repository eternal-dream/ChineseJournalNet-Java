<template>
  <div>
    <vip-header></vip-header>
    <searcher></searcher>
    <div id="body">
      <div class="l-width" v-loading="loading">
        <div class="row article">
          <div class="article-main">
            <div class="article-title">
              <h1>{{detail.title_c}}
                <span class="pre-view">
               <!-- <a method="notice" href="#">
                <i class="icon-view"></i>预览
                </a>-->
              </span>
                <span v-if="detail.zkbycount>0" class="cited">被引量：{{detail.zkbycount}}</span></h1>
              <em>{{detail.title_e}}</em>
            </div>
            <div class="article-source" v-if="currentUser&&currentUser.paymentType=='Full_text'">
              <a v-if="!userDatabaseInfoInvalid" href="javaScript:void(0);" @click="readPdf(detail.lngid)"><i class="icon-read"></i>在线阅读</a>
              <a v-if="!userDatabaseInfoInvalid" href="javaScript:void(0);" @click="downloadPdf(detail.lngid)"><i class="icon-down"></i>下载PDF</a>
              <a v-if="userDatabaseInfoInvalid" href="javaScript:void(0);"><i class="el-icon-warning"></i>暂无订购</a>
              <!--<a href="#"><i class="icon-down"></i>免费下载</a>
              <a href="#"><i class="icon-send"></i>原文传递</a>
              <a href="#"><i class="icon-oa"></i>OA链接</a>
              <a href="#"><i class="icon-site"></i>网络资源外链</a>
              <a href="#"><i class="icon-rar"></i>职称评审资料打包下载</a>-->
            </div>

            <div class="article-op">
              <!--<a href="#"><i class="icon-favorite"></i>收藏</a>
              <a href="#"><i class="icon-share"></i>分享</a>-->
              <a href="javaScript:void(0);" @click="toExportIndex"><i class="icon-export"></i>导出</a>
            </div>
            <div class="article-detail" v-if="detail.remark_c">
              <div class="abstract" v-if="detail.remark_c">
                <span class="label">摘要</span>

                <span class="abstract"  v-html="detail.remark_c">

                   <!-- <span class=""
                          v-html="detail.remark_c.length>220 && !detail.showRemarkC ? (detail.remark_c.substring(0,220)+'...') : detail.remark_c">
                    </span>
                    <a v-if="detail.remark_c.length>220 && !detail.showRemarkC" href="javascript:void(0)"
                       @click="$set(detail,'showRemarkC',true)">展开更多</a>-->
                </span>

                <em>
                  <span class="abstract" v-if="detail.remark_e" v-html="detail.remark_e">

                    <!--<span class=""
                          v-html="detail.remark_e.length>300 && !detail.showRemarkE ? (detail.remark_e.substring(0,244)+'...') : detail.remark_e">
                    </span>
                    <a v-if="detail.remark_e.length>300 && !detail.showRemarkE" href="javascript:void(0)"
                       @click="$set(detail,'showRemarkE',true)">展开更多</a>-->
                    </span>
                </em>
              </div>
              <div class="author" v-if="detail.showwriter">
                <span class="label">作者</span>
                <span style="margin-left: 5px;" v-show="(detail.showAllWriter || i<2)"
                      v-for="(author,i) in detail.showwriter.split(';')"
                      :key="author">
                            <a href="javascript:void(0)" @click="changeSearchModel('A',removeBlankAndNum(author))"
                               :title="author"><span v-html="removeBlankAndNum(author)"></span></a>
                        </span>


                <span style="margin-left: 5px;" class="more hide"
                      v-if="detail.showwriter.split(';').length>3 && !detail.showAllWriter">
                            <a class="num" href="javascript:void(0)" @click=" $set(detail,'showAllWriter',true)">+{{detail.showwriter.split(';').length-3}} 位作者</a>
                </span>
                <em><span>{{detail.author_e}}</span></em>
                <span style="margin-left: 5px;"
                      v-if="(!detail.showAllWriter && detail.showwriter && detail.showwriter.split(';').length>3 )">
                            <a href="javascript:void(0)"
                               @click="changeSearchModel('A',removeBlankAndNum(detail.showwriter.split(';')[detail.showwriter.split(';').length-1]))"
                               v-html="removeBlankAndNum(detail.showwriter.split(';')[detail.showwriter.split(';').length-1])"></a>
                </span>
              </div>
              <div class="organ" v-if="detail.organ_text">
                <span class="label">机构地区</span>
                <!--<span v-for="org in detail.showorgan.split(';')"><a @click="changeSearchModel('S',removeBlankAndNum(org),false)" href="javascript:void(0);"
                                                                    v-html="removeBlankAndNum(org)"></a></span>-->
                <span><a @click="changeSearchModel('S',detail.organ_text.split('#')[0],false)"
                         href="javascript:void(0);"
                         v-html="detail.organ_text.split('#')[0]"></a></span>
              </div>
              <div class="journal">
                <span class="label">出处</span>
                <span class="from">
                  <a href="javascript:void(0);" @click="toJournal(detail.gch5)" :title="detail.media_c">《{{detail.media_c}}》</a>
                  <span class="medal">
                    <span v-show="detail.range.indexOf('EI') != -1">EI</span>
                    <span v-show="detail.range.indexOf('CAS') != -1">CAS</span>
                    <span v-show="detail.range.indexOf('CSSCI') != -1">CSSCI</span>
                    <span v-show="detail.range.indexOf('CSCD') != -1">CSCD</span>
                    <span v-show="detail.range.indexOf('BDHX') != -1">北大核心</span>
                  </span>
                </span>
                <span class="vol">{{detail.years}}年第{{detail.num}}期{{detail.beginpage}}-{{detail.endpage}},{{detail.jumppage}}共{{detail.pagecount}}页</span>
                <em>{{detail.media_e}}</em>
              </div>
              <div class="fund" v-if="detail.imburse">
                <span class="label">基金</span>
                <span v-for="fund in detail.imburse"><a href="#">{{fund}}</a></span>
              </div>

              <div class="subject" v-if="detail.keyword_c">
                <span class="label">关键词</span>
                <span style="margin-left: 5px;" v-if="detail.keyword_c" v-for="keyword in detail.keyword_c.split(';')"
                      :key="keyword">
                            <a href="javascript:void(0)" @click="changeSearchModel('K',formatKeyword(keyword))"
                               :title="keyword" v-html="formatKeyword(keyword)"></a>
                        </span>
                <em v-if="detail.keyword_e">
                  <span v-for="keyword in detail.keyword_e.split(';')" :key="keyword" v-html="formatKeyword(keyword)">LI Guang-da</span>
                </em>
              </div>
              <div class="class" v-if="detail.class">
                <span class="label">分类号</span>
                <span v-for="(cls) in detail.class.split(';')" :key="cls">
                        <a href="javascript:void(0)" @click="changeSearchModel('C',cls)" :title="cls" v-html="cls"></a>
                </span>

              </div>
              <!--<div class="others"><span class="label">作者简介</span>李光达，在读博士，研究方向为可视化管理，邮箱为guangdali@163．com。</div>-->
            </div>
            <div class="article-relate-list">
              <div class="layui-tab" lay-filter="">
                <!--<div class="relate-title">
                  <ul class="layui-tab-title">
                    <li class="layui-this">引文网络</li>
                    <li>相关文献</li>
                  </ul>
                </div>-->
                <div class="layui-tab-content relate-content">
                  <div class="layui-tab-item content layui-show">
                    <!--<div class="relate "> &lt;!&ndash; relate-fixed 固定悬浮 &ndash;&gt;

                      <div class="article-map">
                        <ul>
                          <li class="core"><a href="#">节点文献</a></li>
                          <li class="ckwx2"><a href="#">二级参考文献<span>11</span></a></li>
                          <li class="ckwx"><a href="#">参考文献<span>61</span></a></li>
                          <li class="gywx"><a href="#">共引文献<span>0</span></a></li>
                          <li class="tbywx"><a href="#">同被引文献<span>1</span></a></li>
                          <li class="yzwx"><a href="#">引证文献<span>11</span></a></li>
                          <li class="yzwx2"><a href="#">二级引证文献<span>6</span></a></li>
                        </ul>
                      </div>
                    </div>-->
                    <div class="relate" v-if="refList.length>0">
                      <div class="title"><h2>参考文献<span>{{refList.length}}</span></h2></div>

                      <div class="article-list">
                        <ul class="referenceInfo" id="referenceRelateInfo">
                          <li v-for="(item,index) in refList">
                            <span>{{index+1}}</span>{{item.writer_text}}<a href="javascript:void(0)"
                                                                           @click="toDetail(item.lngid)">{{item.title_c}}</a>.{{item.media_c}},{{item.years}},{{item.vol}}({{item.num}}):{{item.beginpage}}-{{item.endpage}}.
                          </li>
                        </ul>
                      </div>

                    </div>
                    <!--<div class="relate">
                      <div class="title"><h2>二级参考文献<span>15</span></h2></div>
                      <div class="article-list">
                        <ul>
                          <li><span>1</span>吴伟定,姚金刚,周振兴编著.网站运营直通车 网络整合营销[M].北京:清华大学出版社,2014:258.</li>
                          <li><span>2</span>赵国庆[1],黄荣怀[1],陆志坚[1].<a href="#">知识可视化的理论与方法</a>[J].开放教育研究,2005,11(1):23-27.
                            <a href="#" class="yellow">被引量：203</a></li>
                          <li><span>3</span>王朝云[1],刘玉龙[2].<a href="#">知识可视化的理论与应用</a>[J].现代教育技术,2007,17(6):18-20. <a
                            href="#" class="yellow">被引量：56</a></li>
                          <li><span>4</span>谭章禄[1],方毅芳[1],吕明[1],张长鲁[1].<a href="#">信息可视化的理论发展与框架体系构建</a>[J].情报理论与实践,2013,36(1):16-19.
                            <a href="#" class="yellow">被引量：20</a></li>
                          <li><span>5</span>史忠植编著.认知科学[M],2008:591.</li>
                          <li><span>6</span>连榕主编.认知心理学[M].北京:高等教育出版社,2010:338.</li>
                          <li><span>7</span>赵慧臣[1].<a href="#">知识可视化视觉表征的设计方法</a>[J].开放教育研究,2012,18(5):25-30. <a
                            href="#"
                            class="yellow">被引量：11</a>
                          </li>
                          <li><span>8</span>Ferreira de O M C,Levkowitz H.From visual data exploration to visual data
                            mining:A survey[J].IEEETransactions on Visualization&amp;Computer Graphics,2003,(3):378-394.
                          </li>
                          <li><span>9</span>Chen K,Xu H,Tian F,et al.Cloud Vista:Visual cluster exploration for extreme
                            scale data in the cloud.Cushing B,French J,Bowers S.Scientific and statistical database
                            management conference[C].Berlin:Springer,2011:332-350.
                          </li>
                          <li><span>10</span>张龙飞[1],姚中华[1],宋汉辰[1],吴玲达[2].<a href="#">基于ThemeRiver的可视化技术发展综述</a>[J].系统仿真学报,2013,25(9):2091-2096.
                            <a href="#" class="yellow">被引量：8</a></li>
                          <li style="display:none"><span>11</span>赵慧臣[1].<a href="#">知识可视化视觉表征的形式分析</a>[J].现代教育技术,2012,22(2):21-27.
                            <a href="#" class="yellow">被引量：9</a></li>
                        </ul>
                        <div class="page">
                          <div class="layui-box layui-laypage layui-laypage-default" id="layui-laypage-1"><a
                            href="javascript:;" class="layui-laypage-prev layui-disabled" data-page="0"><</a><span
                            class="layui-laypage-curr"><em class="layui-laypage-em"></em><em>1</em></span><a
                            href="javascript:;" data-page="2">2</a><a href="javascript:;" data-page="3">3</a><a
                            href="javascript:;" data-page="4">4</a><a href="javascript:;" data-page="5">5</a><a
                            href="javascript:;" class="layui-laypage-next" data-page="2">></a></div>
                        </div>
                      </div>
                    </div>-->
                  </div>
                  <div class="layui-tab-item content">
                    <div class="relate">

                      <div class="article-list">
                        <ul>
                          <li><span>1</span>吴伟定,姚金刚,周振兴编著.网站运营直通车 网络整合营销[M].北京:清华大学出版社,2014:258.</li>
                          <li><span>2</span>赵国庆[1],黄荣怀[1],陆志坚[1].<a href="#">知识可视化的理论与方法</a>[J].开放教育研究,2005,11(1):23-27.
                            <a href="#" class="yellow">被引量：203</a></li>
                          <li><span>3</span>王朝云[1],刘玉龙[2].<a href="#">知识可视化的理论与应用</a>[J].现代教育技术,2007,17(6):18-20. <a
                            href="#" class="yellow">被引量：56</a></li>
                          <li><span>4</span>谭章禄[1],方毅芳[1],吕明[1],张长鲁[1].<a href="#">信息可视化的理论发展与框架体系构建</a>[J].情报理论与实践,2013,36(1):16-19.
                            <a href="#" class="yellow">被引量：20</a></li>
                          <li><span>5</span>史忠植编著.认知科学[M],2008:591.</li>
                          <li><span>6</span>连榕主编.认知心理学[M].北京:高等教育出版社,2010:338.</li>
                          <li><span>7</span>赵慧臣[1].<a href="#">知识可视化视觉表征的设计方法</a>[J].开放教育研究,2012,18(5):25-30. <a
                            href="#"
                            class="yellow">被引量：11</a>
                          </li>
                          <li><span>8</span>Ferreira de O M C,Levkowitz H.From visual data exploration to visual data
                            mining:A survey[J].IEEETransactions on Visualization&amp;Computer Graphics,2003,(3):378-394.
                          </li>
                          <li><span>9</span>Chen K,Xu H,Tian F,et al.Cloud Vista:Visual cluster exploration for extreme
                            scale data in the cloud.Cushing B,French J,Bowers S.Scientific and statistical database
                            management conference[C].Berlin:Springer,2011:332-350.
                          </li>
                          <li><span>10</span>张龙飞[1],姚中华[1],宋汉辰[1],吴玲达[2].<a href="#">基于ThemeRiver的可视化技术发展综述</a>[J].系统仿真学报,2013,25(9):2091-2096.
                            <a href="#" class="yellow">被引量：8</a></li>
                          <li style="display:none"><span>11</span>赵慧臣[1].<a href="#">知识可视化视觉表征的形式分析</a>[J].现代教育技术,2012,22(2):21-27.
                            <a href="#" class="yellow">被引量：9</a></li>
                          <li><span>1</span>吴伟定,姚金刚,周振兴编著.网站运营直通车 网络整合营销[M].北京:清华大学出版社,2014:258.</li>
                          <li><span>2</span>赵国庆[1],黄荣怀[1],陆志坚[1].<a href="#">知识可视化的理论与方法</a>[J].开放教育研究,2005,11(1):23-27.
                            <a href="#" class="yellow">被引量：203</a></li>
                          <li><span>3</span>王朝云[1],刘玉龙[2].<a href="#">知识可视化的理论与应用</a>[J].现代教育技术,2007,17(6):18-20. <a
                            href="#" class="yellow">被引量：56</a></li>
                          <li><span>4</span>谭章禄[1],方毅芳[1],吕明[1],张长鲁[1].<a href="#">信息可视化的理论发展与框架体系构建</a>[J].情报理论与实践,2013,36(1):16-19.
                            <a href="#" class="yellow">被引量：20</a></li>
                          <li><span>5</span>史忠植编著.认知科学[M],2008:591.</li>
                          <li><span>6</span>连榕主编.认知心理学[M].北京:高等教育出版社,2010:338.</li>
                          <li><span>7</span>赵慧臣[1].<a href="#">知识可视化视觉表征的设计方法</a>[J].开放教育研究,2012,18(5):25-30. <a
                            href="#"
                            class="yellow">被引量：11</a>
                          </li>
                          <li><span>8</span>Ferreira de O M C,Levkowitz H.From visual data exploration to visual data
                            mining:A survey[J].IEEETransactions on Visualization&amp;Computer Graphics,2003,(3):378-394.
                          </li>
                          <li><span>9</span>Chen K,Xu H,Tian F,et al.Cloud Vista:Visual cluster exploration for extreme
                            scale data in the cloud.Cushing B,French J,Bowers S.Scientific and statistical database
                            management conference[C].Berlin:Springer,2011:332-350.
                          </li>
                          <li><span>10</span>张龙飞[1],姚中华[1],宋汉辰[1],吴玲达[2].<a href="#">基于ThemeRiver的可视化技术发展综述</a>[J].系统仿真学报,2013,25(9):2091-2096.
                            <a href="#" class="yellow">被引量：8</a></li>
                          <li style="display:none"><span>11</span>赵慧臣[1].<a href="#">知识可视化视觉表征的形式分析</a>[J].现代教育技术,2012,22(2):21-27.
                            <a href="#" class="yellow">被引量：9</a></li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>
          <div class="article-right">
            <div class="article-journal">
              <div class="cover"><a href="#"><img
                src="@/assets/images/default_qk.jpg" alt=""/></a></div>
              <div class="name"><a href="javascript:void(0)" @click="toJournal(detail.gch5)">《{{detail.media_c}}》</a>
              </div>
              <div class="vol"><a href="javascript:void(0);"
                                  @click="toJournalWithYearAndNum(detail.gch5,detail.years,detail.num)">{{detail.years}}年
                第{{detail.num}}期</a></div>
              <!--<div class="op"><a href="#"><i class="icon-rar"></i>职称评审资料打包下载</a></div>-->
            </div>
            <!--<div class="article-object">
              <div class="object-box">
                <h2 class="object-title">相关作者</h2>
                <div class="object-content">
                  <ul class="b-list">
                    <li><a href="#">张宏晨</a></li>
                    <li><a href="#">杨志健</a></li>
                    <li><a href="#">曹克将</a></li>
                    <li><a href="#">李春坚</a></li>
                    <li><a href="#">崔建国</a></li>
                    <li><a href="#">张宏晨</a></li>
                    <li><a href="#">杨志健</a></li>
                    <li><a href="#">曹克将</a></li>
                    <li><a href="#">李春坚</a></li>
                  </ul>
                </div>
              </div>
              <div class="object-box">
                <h2 class="object-title">相关作者</h2>
                <div class="object-content">
                  <ul class="l-list">
                    <li><a href="#">河南大学护理学院</a></li>
                    <li><a href="#">南京医科大学公共卫生学院</a></li>
                    <li><a href="#">广东省人民医院广东省心血管病研究所</a></li>
                    <li><a href="#">新疆医科大学护理学院</a></li>
                    <li><a href="#">台州学院医学院</a></li>
                  </ul>
                </div>
              </div>
              <div class="object-box">
                <h2 class="object-title">相关主题</h2>
                <div class="object-content">
                  <ul class="b-list">
                    <li><a href="#">张宏晨</a></li>
                    <li><a href="#">杨志健</a></li>
                    <li><a href="#">曹克将</a></li>
                    <li><a href="#">李春坚</a></li>
                    <li><a href="#">崔建国</a></li>
                    <li><a href="#">张宏晨</a></li>
                    <li><a href="#">杨志健</a></li>
                    <li><a href="#">曹克将</a></li>
                    <li><a href="#">李春坚</a></li>
                  </ul>
                </div>
              </div>
            </div>-->
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
  import { downloadPdf, readPdf } from '@/utils/zipdownload'

  export default {
    name: 'index',
    data () {
      return {
        //无效
        userDatabaseInfoInvalid: false,
        currentUser: null,

        searchParam: {
          pageNum: 1,
          pageSize: 20,
          sortField: '',
          isAsc: false, // 是否正序
          searchExpression: '',
          searchByExpression: false,
          searchModels: []
        },

        refList: [],
        detail: {},
        loading: false
      }
    },
    methods: {
      toJournal (gch5) {
        if (gch5) {
          this.$router.push({
            path: '/journalsDetail/index',
            query: { gch5: gch5 }
          })
        }
      },
      toJournalWithYearAndNum (gch5, year, num) {
        if (gch5 && year && num) {
          this.$router.push({
            path: '/journalsDetail/index',
            query: {
              gch5: gch5,
              year: year,
              num: num
            }
          })
        }
      },
      toDetail (lngid) {
        if (lngid) {
          this.getDetail(lngid)
        }
      },
      // 修改简单检索条件(去掉其它检索条件，只保留简单检索)
      changeSearchModel (searchField, value, exact) {
        const searchModel = {
          searchField: searchField,
          value: value,
          exact: exact || false, // 是否精确检索
          logicOperator: 'AND',
          weight: 2.0
        }
        this.searchParam.searchModels = [searchModel]
        this.$router.replace({
          path: '/search/index',
          query: { searchModels: JSON.stringify(this.searchParam.searchModels) }
        })
      },
      // 去掉作者的[1]
      removeBlankAndNum (val) {
        if (val) {
          var reg = /\[([0-9]*)\]/g
          val = val.replace(reg, '')
        }
        return val
      },
      getDetail (lngid) {
        if (lngid) {
          this.loading = true
          let param = {
            index: 'title_info',
            query: 'lngid:' + lngid
          }
          this.$axiosRequest('view/detail/getDetail', param, this.$axiosOptions('get')).then(res => {
            console.log('getDetail', res)
            this.detail = res.data
            this.refList = res.refList.map(item => {
              return JSON.parse(item.ndo_content)
            })
            // this.$route.meta.title = this.detail.title_c
            console.log('this.refList', this.refList)
          }).finally(e => {
            this.loading = false
          })
        }
      },
      formatKeyword (keyword) {
        if (!keyword) {
          return ''
        }
        if (keyword.indexOf(']') === -1) {
          return keyword
        }
        return keyword.substring(keyword.indexOf(']') + 1, keyword.length)
      },
      toExportIndex () {
        let params = []
        params.push(this.detail.lngid)
        this.$router.push({
            path: '/export/index',
            query: {
              ids: params
            }
          }
        )
        // let routeUrl = this.$router.resolve({
        //   path: '/export/index',
        //   query: {
        //     ids: params
        //   }
        // })
        // window.open(routeUrl.href, '_blank')
      },
      downloadPdf: downloadPdf,
      readPdf: readPdf,
      getUserInfo () {
        this.$axiosRequest('view/user/getLoginUserFromDb', {}, this.$axiosOptions('get')).then(res => {
          console.log('getLoginUserFromDb', res)
          this.currentUser = res.data
        }).finally(e => {

        })
      },
      readLimitCheck (lngid) {
        debugger
        if(lngid){
          this.$axiosRequest('/view/detail/readLimitCheck', { lngid: lngid }, this.$axiosOptions('get')).then(res => {
            console.log('res', res)
            this.userDatabaseInfoInvalid = res.data.limited
          })
        }

      }
    },
    created () {
      // 接收参数
      let query = this.$route.query
      if (query && query.lngid) {
        this.$axiosRequest('/view/shieldInfo/checkArticleDetail', { lngid: query.lngid }, this.$axiosOptions('get')).then(res => {
          if(res.shielded){
            // 跳转首页
            location.href = '/qikan/index'
          }else{
            this.getDetail(query.lngid.toLowerCase())
            this.readLimitCheck(query.lngid.toLowerCase())
            this.getUserInfo()
          }
        })
      }

    }
  }
</script>
<style scoped src="@/assets/css/article.css"></style>
<style scoped src="@/assets/css/global.css"></style>
<style scoped>

</style>
