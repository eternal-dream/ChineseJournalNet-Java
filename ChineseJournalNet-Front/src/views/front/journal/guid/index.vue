<template>
  <div v-loading.fullscreen.lock="loading"
       element-loading-text="拼命加载中"
       element-loading-background="rgba(255, 255, 255, 0.5)">
    <vip-header></vip-header>
    <searcher></searcher>
    <div id="body">
      <div class="l-width">
        <div class="row web-intro"
             v-if="classOrMedia">
          <h1><span class="title">期刊导航</span></h1>
          <div class="intro-content">
            《中文科技期刊数据库》诞生于1989年，累计收录期刊16000余种，现刊9000余种，文献总量6000余万篇。
            是我国数字图书馆建设的核心资源之一，是高校图书馆文献保障系统的重要组成部分，也是科研工作者进行科技查证和科技查新的必备数据库。
          </div>
        </div>
        <div class="row journal-guid">
          <div class="search-left">
            <div class="layui-form search-cluster">
              <div class="layui-collapse"
                   lay-filter="test">
                <div class="layui-colla-item search-refine">
                  <h2 class="layui-colla-title">期刊检索</h2>
                  <div class="layui-colla-content ">
                    <div class="refine-line">
                      <div class="refine-sel">
                        <el-select v-model="commonSearchParams.searchField"
                                   placeholder="请选择"
                                   size="mini">
                          <el-option v-for="item in selectOptions"
                                     :key="item.value"
                                     :label="item.label"
                                     :value="item.value">
                          </el-option>
                        </el-select>
                      </div>
                      <div class="refine-input">
                        <el-input size="mini"
                                  placeholder="请输入检索词"
                                  clearable
                                  auto-complete="off"
                                  v-model="commonSearchParams.value" />
                      </div>
                    </div>
                    <div class="refine-submit">
                      <button class="layui-btn"
                              @click="submitSearch">检索</button>
                    </div>
                  </div>
                </div>
                <div class="layui-colla-item">
                  <h2 class="layui-colla-title"
                      @click="providerFacets.showAll = !providerFacets.showAll">核心期刊</h2>
                  <i class="layui-icon layui-colla-icon"
                     align="center"
                     style="cursor:pointer"
                     :class="{'layui-colla-icon-rotate':providerFacets.showAll == true}"
                     @click="providerFacets.showAll = !providerFacets.showAll"></i>
                  <div class="layui-colla-content layui-show">
                    <ul class="cluster-term">
                      <li v-for="(facet,indexFP) in providerFacetsList">
                        <a class="cluster-close"
                           @click="removeFacet(facet,'P')"><i class="icon-close"></i></a>
                        <span class="cluster-name">{{ facet.displayName }}</span>
                      </li>
                    </ul>
                    <ul class="cluster-list">
                      <li class="cluster-item"
                          v-for="(item,idnexP) in providerFacets.value.slice(0,providerFacets.limit)">
                        <a @click="clickFacetItem(item,'P')"
                           style="cursor:pointer"
                           v-if="item.show !== false">{{ item.label }}<tt>{{ formatNumber(item.value) }}</tt></a>
                      </li>
                    </ul>

                  </div>
                </div>
                <div class="layui-colla-item">
                  <h2 class="layui-colla-title"
                      @click="outsideFacets.showAll = !outsideFacets.showAll">国内外数据库收录</h2>
                  <i class="layui-icon layui-colla-icon"
                     align="center"
                     style="cursor:pointer"
                     :class="{'layui-colla-icon-rotate':outsideFacets.showAll == true}"
                     @click="outsideFacets.showAll = !outsideFacets.showAll"></i>
                  <div class="layui-colla-content layui-show">
                    <ul class="cluster-term">
                      <li v-for="(facet,indexFO) in outsideFacetsList">
                        <a class="cluster-close"
                           @click="removeFacet(facet,'O')"><i class="icon-close"></i></a>
                        <span class="cluster-name">{{ facet.displayName }}</span>
                      </li>
                    </ul>
                    <ul class="cluster-list">
                      <li class="cluster-item"
                          v-for="(item,indexO) in outsideFacets.value.slice(0,outsideFacets.limit)">
                        <a @click="clickFacetItem(item,'O')"
                           style="cursor:pointer"
                           v-if="item.show !== false">{{ item.label }}<tt>{{ formatNumber(item.value) }}</tt></a>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="layui-colla-item">
                  <h2 class="layui-colla-title"
                      @click="subjectFacets.showAll = !subjectFacets.showAll">主题</h2>
                  <i class="layui-icon layui-colla-icon"
                     align="center"
                     style="cursor:pointer"
                     :class="{'layui-colla-icon-rotate':subjectFacets.showAll == true}"
                     @click="subjectFacets.showAll = !subjectFacets.showAll"></i>
                  <div class="layui-colla-content layui-show">
                    <ul class="cluster-term">
                      <li v-for="(facet,indexFS) in subjectFacetsList">
                        <a class="cluster-close"
                           @click="removeFacet(facet,'S')"><i class="icon-close"></i></a>
                        <span class="cluster-name">{{ facet.displayName }}</span>
                      </li>
                    </ul>
                    <ul class="cluster-list">
                      <li class="cluster-item"
                          v-for="(item,indexS) in subjectFacets.value.slice(0,subjectFacets.limit)">
                        <a @click="clickFacetItem(item,'S')"
                           style="cursor:pointer"
                           v-if="item.show !== false">{{ item.label }}<tt>{{ formatNumber(item.value) }}</tt></a>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="layui-colla-item">
                  <h2 class="layui-colla-title"
                      @click="areaFacets.showAll = !areaFacets.showAll">地区</h2>
                  <i class="layui-icon layui-colla-icon"
                     align="center"
                     style="cursor:pointer"
                     :class="{'layui-colla-icon-rotate':areaFacets.showAll == true}"
                     @click="areaFacets.showAll = !areaFacets.showAll"></i>
                  <div class="layui-colla-content layui-show">
                    <ul class="cluster-term">
                      <li v-for="(facet,indexFA) in areaFacetsList">
                        <a class="cluster-close"
                           @click="removeFacet(facet,'A')"><i class="icon-close"></i></a>
                        <span class="cluster-name">{{ facet.displayName }}</span>
                      </li>
                    </ul>
                    <ul class="cluster-list">
                      <li class="cluster-item"
                          v-for="(item,indexA) in areaFacets.value.slice(0,areaFacets.limit)">
                        <a @click="clickFacetItem(item,'A')"
                           style="cursor:pointer"
                           v-if="item.show !== false">
                          <i :class="{'icon-plus':item.children,'icon-minus':item.children && item.isOpen}"
                             @click.stop="changeAreaChildShow(item)"></i>
                          {{ item.label }}<tt>{{ formatNumber(item.value) }}</tt>
                        </a>
                        <ul class="cluster-list"
                            v-if="item.children && item.isOpen">
                          <li class="cluster-item"
                              v-for="(child,indexC) in item.children">
                            <a @click="clickFacetItem(child,'A')"
                               style="cursor:pointer"
                               v-if="child.show !== false">{{ child.label }}<tt>{{ formatNumber(child.value) }}</tt></a>
                          </li>
                        </ul>
                      </li>
                    </ul>
                  </div>
                </div>

                <div class="layui-colla-item"
                     v-if="!classOrMedia">
                  <h2 class="layui-colla-title"
                      @click="classTypeFacets.showAll = !classTypeFacets.showAll">学科</h2>
                  <i class="layui-icon layui-colla-icon"
                     align="center"
                     style="cursor:pointer"
                     :class="{'layui-colla-icon-rotate':classTypeFacets.showAll == true}"
                     @click="classTypeFacets.showAll = !classTypeFacets.showAll"></i>
                  <div class="layui-colla-content layui-show">
                    <ul class="cluster-term">
                      <li v-for="(facet,indexFC) in classTypeFacetsList">
                        <a class="cluster-close"
                           @click="removeFacet(facet,'C')"
                           style="cursor:pointer"><i class="icon-close"></i></a>
                        <span class="cluster-name">{{ facet.displayName }}</span>
                      </li>
                    </ul>
                    <ul class="cluster-list">
                      <li class="cluster-item"
                          v-for="(item,indexA) in classTypeFacets.value.slice(0,classTypeFacets.limit)">
                        <a @click="clickFacetItem(item,'C')"
                           style="cursor:pointer"
                           v-if="item.show !== false ">
                          <i :class="{'icon-plus':item.children,'icon-minus':item.children && item.isOpen}"
                             @click.stop="changeClassTypeChildShow(item)"></i>
                          {{ item.label }}<tt>{{ formatNumber(item.value) }}</tt>
                        </a>
                        <ul class="cluster-list"
                            v-if="item.children && item.isOpen">
                          <li class="cluster-item"
                              v-for="(child,indexC) in item.children">
                            <a @click="clickFacetItem(child,'C')"
                               style="cursor:pointer"
                               v-if="child.show !== false">{{ child.label }}<tt>{{ formatNumber(child.value) }}</tt></a>
                          </li>
                        </ul>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="layui-form search-main">
            <template v-if="classOrMedia">
              <div class="row search-top">
                <div class="layui-col-xs6 search-result">目前期刊总计<span>{{ formatNumber(total) }}</span>种</div>
              </div>
              <div class="search-filter">
                <div class="label">按首字母查找</div>
                <ul class="list-abc">
                  <li v-for="(letter ,indexL) in theLetters"><a @click="clickTheLetter(letter)"
                       style="cursor: pointer">{{ letter }}</a></li>
                </ul>
              </div>
              <div class="journal-class-list">
                <div v-for="(fatherClass,indexF) in classTypeFacets.value">
                  <h2>
                    <span class="title"><a style="cursor: pointer"
                         @click="clickFacetItem(fatherClass,'C',false)">{{ fatherClass.label }}</a>({{
                    fatherClass.value
                  }})</span>
                  </h2>
                  <ul>
                    <li v-for="(sonClass,indexS) in fatherClass.children">
                      <a @click="clickFacetItem(sonClass,'C',false)"
                         style="cursor: pointer">
                        {{splitClassName(sonClass.label) }}</a>({{ sonClass.value }})
                    </li>
                  </ul>
                </div>
              </div>
            </template>
            <template v-else>
              <div class="layui-form search-main">
                <div class="row search-top">
                  <div class="layui-col-xs6 search-result">共找到<span>{{ formatNumber(total) }}</span>种期刊
                  </div>
                  <div class="layui-col-xs6">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page.sync="queryParams.pageNum"
                                   :page-sizes="[20, 50, 100, 200]"
                                   :page-size="queryParams.pageSize"
                                   layout="sizes, prev, pager, next"
                                   :total="total">
                    </el-pagination>
                  </div>
                </div>

                <div class="row search-op">

                  <div class="layui-col-xs6 layui-col-md-offset6">
                    <div class="btn-switch">
                      显示方式：
                      <div class="layui-btn-group">
                        <a class="layui-btn layui-btn-xs"
                           :class="{ 'active' : !coverOrTable}"
                           data-key="list"
                           data-type="set"
                           @click="coverOrTable = false"><i class="icon-table icon-cover"></i>列表</a>
                        <a class="layui-btn layui-btn-xs "
                           :class="{ 'active' : coverOrTable}"
                           data-key="coverlist"
                           data-type="set"
                           @click="coverOrTable = true"><i class="icon-table icon-cover"></i>封面</a>
                      </div>
                    </div>
                    <div class="btn-switch">
                      <div class="layui-btn-group">
                        <a class="layui-btn layui-btn-xs"
                           :class="{'active':queryParams.sortField == 'zkbycount'}"
                           data-key="1"
                           data-type="order"
                           @click="changeSort('zkbycount')"><i class="icon-sort"></i>被引量排序</a>
                        <a class="layui-btn layui-btn-xs "
                           :class="{'active':queryParams.sortField == 'zkfwcount'}"
                           data-key="0"
                           data-type="order"
                           @click="changeSort('zkfwcount')"><i class="icon-sort"></i>作品数排序</a>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="search-result-list no-sel-list">
                  <div class="cover-list"
                       id="coverlist"
                       v-if="coverOrTable">
                    <div class="row"
                         v-for=" (row,idnexW ) in (queryParams.pageSize/5)">
                      <dl v-for="(media ,indexM) in mediaList.slice(idnexW*5,(idnexW+1)*5)">
                        <dt>
                          <a @click="toMediaDetail(media.ndo_content.gch5)"
                             style="cursor: pointer"
                             target="_blank"
                             v-html="media.ndo_content.media"></a>
                        </dt>
                        <dd class="cover">
                          <a @click="toMediaDetail(media.ndo_content.gch5)"
                             target="_blank"
                             style="cursor: pointer">
                            <img :src=buildImgUrl(media.ndo_content.gch) :onerror="loadDefaultImg">
                            <i v-if="media.ndo_content.accessmode=='OA'" class="icon-oa-pic"></i>
                          </a>

                        </dd>
                        <dd class="medal">
                          <span v-for="it in buildProvider(media.ndo_content.lngrangeid)">{{ it }}</span>
                        </dd>
                      </dl>
                    </div>
                  </div>
                  <div class="table-list" id="list" v-else>
                    <table>
                      <colgroup>
                        <col width="300">
                        <col>
                        <col width="200">
                        <col width="100">
                        <col width="100">
                      </colgroup>
                      <thead>
                        <tr>
                          <th>期刊名称</th>
                          <th>主办单位</th>
                          <th>收录情况</th>
                          <th>发文量</th>
                          <th>被引量</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(mediaL,indexML) in mediaList">
                          <td class="title">
                            <a @click="toMediaDetail(mediaL.ndo_content.gch5)"
                               target="_blank"
                               style="text-decoration: none;cursor: pointer"
                               v-html="mediaL.ndo_content.media"></a>
                            <i v-if="mediaL.ndo_content.accessmode=='OA'" class="icon-oa-text"></i>
                          </td>
                          <td class="title">
                            <span v-html="mediaL.ndo_content.publisher"></span>
                          </td>
                          <td class="medal">
                            <span v-for="it in buildProvider(mediaL.ndo_content.lngrangeid)">{{ it }}</span>
                          </td>
                          <td>{{ mediaL.zkfwcount }}</td>
                          <td>{{ mediaL.zkbycount}}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
                <div class="row search-bottom">
                  <div class="page">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="queryParams.pageNum"
                                   :page-sizes="[20, 50, 100, 200]"
                                   layout="total, sizes, prev, pager, next, jumper"
                                   :total="total">
                    </el-pagination>

                  </div>
                </div>
              </div>

            </template>

          </div>
        </div>
      </div>
    </div>
    <vip-footer></vip-footer>

  </div>

</template>

<script src="./guid.js"></script>
<style scoped src="@/assets/css/guid.css"></style>
<style scoped src="@/assets/css/search.css"></style>
<style scoped lang="scss" >
.el-input--suffix .el-input__inner {
  padding-right: 15px;
}

.layui-colla-icon-rotate {
  transform: rotate(180deg);
}
</style>
