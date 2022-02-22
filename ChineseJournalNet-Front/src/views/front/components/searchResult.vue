<template>
  <div id="body">
    <div class="l-width">
        <div class="row search-list">
        <div class="search-left">
            <div class="layui-form search-cluster">
            <div class="layui-collapse" lay-filter="test">
                <div class="loading" v-show="loadingFacet">
                    <div class="loader-m1"></div>
                </div>
                <div v-if="!loadingFacet">
                    <div class="layui-colla-item search-refine">
                    <h2 class="layui-colla-title">二次检索</h2>
                    <div class="layui-colla-content ">
                        <ul class="cluster-term">
                        <li v-for="(param,i) in secondParams" :key="param.displayValue">
                            <a class="cluster-close" @click="removeSecondParam(i)" href="javascript:void(0)"><i class="icon-close"></i></a>
                            <span class="cluster-name">{{param.displayValue}}</span>
                        </li>
                        </ul>
                        <div class="refine-line">
                        <div class="refine-sel">
                            <el-select id="Select11" v-model="secondSearchModel.searchField" size="mini">
                                <!-- <el-option value="T" label="题名"></el-option>
                                <el-option value="K" label="关键词"></el-option>
                                <el-option value="R" label="文摘"></el-option>
                                <el-option value="A" label="作者"></el-option>
                                <el-option value="F" label="第一作者"></el-option>
                                <el-option value="S" label="机构"></el-option>
                                <el-option value="J" label="刊名"></el-option>
                                <el-option value="C" label="分类号"></el-option>
                                <el-option value="I" label="基金资助"></el-option>
                                <el-option value="L" label="栏目信息"></el-option> -->
                                <el-option v-for="item in params.searchFields"
                                    :key="item.value"
                                    :value="item.value"
                                    :label="item.label" />
                            </el-select>
                        </div>
                        <div class="refine-input">
                            <el-input width="100%" placeholder="请输入检索词" size="mini" v-model="secondSearchModel.value" />
                        </div>
                        </div>
                        <div class="refine-submit">
                        <button class="layui-btn" @click="addSecondSearchModel('AND')">在结果中检索</button>
                        <button class="layui-btn" @click="addSecondSearchModel('NOT')">在结果中去除</button>
                        </div>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.yearFacet && facet.yearFacet.length>0">
                    <h2 class="layui-colla-title" @click="$set(facet.yearFacet,'showAll',facet.yearFacet.showAll? false:true)">年份</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.yearFacet">
                            <li v-show="isSelected(item.label)" v-for="item in facet.yearFacet" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.key}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || facet.yearFacet.showAll) && !isSelected(item.label)" v-for="(item,index) in facet.yearFacet" :key="item.key">
                            <a href="javascript:void(0)" @click="addFacetParam(item,'Y')">{{item.key}}<tt>{{formatNumber(item.value)}}</tt></a>
                        </li>
                        <span v-if="facet.yearFacet.length>5">
                        <i v-if="!facet.yearFacet.showAll" @click="$set(facet.yearFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        <i v-else @click="$set(facet.yearFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.classFacet">
                    <h2 class="layui-colla-title" @click="$set(facet.classFacet,'showAll',facet.classFacet.showAll? false:true)">学科</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.classFacet">
                            <li v-show="item.searchField == 'CID'" v-for="item in facetParams" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.displayValue}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || facet.classFacet.showAll) && !isSelected(cls.key)" v-for="(cls,index) in facet.classFacet" :key="cls.key">
                            <a href="javascript:void(0)" :title="cls.label" @click="addFacetParam(cls,'CID')">
                                <i v-show="!cls.showSubFacet && !cls.loading"  class="icon-plus" @click.stop="getClassSubFacet(cls)"></i>
                                <i class="el-icon-loading" v-show="cls.loading"></i>
                                <i class="icon-minus" v-show="cls.showSubFacet" @click.stop="cls.showSubFacet = false"></i>
                                {{cls.label}}<tt>{{formatNumber(cls.value)}}</tt>
                            </a>
                            <ul class="cluster-list" v-if="cls.showSubFacet">
                                <li class="cluster-item" v-show="!isSelected(subCls.key)" v-for="subCls in cls.subFacet" :key="subCls.key">
                                    <a href="javascript:void(0)" @click="addFacetParam(subCls,'CID')" :title="subCls.label">{{subCls.label}}<tt>{{formatNumber(subCls.value)}}</tt></a>
                                </li>
                            </ul>
                        </li>
                        <span v-if="facet.classFacet.length>5">
                            <i v-if="!facet.classFacet.showAll" @click="$set(facet.classFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.classFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.rangeFacet && facet.rangeFacet.length>0">
                    <h2 class="layui-colla-title" @click="$set(facet.rangeFacet,'showAll',facet.rangeFacet.showAll? false:true)">期刊收录</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.rangeFacet">
                            <li v-show="item.searchField == 'RN'" v-for="item in facetParams" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.displayValue}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || facet.rangeFacet.showAll) && !isSelected(item.key) && index<15" v-for="(item,index) in facet.rangeFacet" :key="item.key">
                            <a href="javascript:void(0)" :title="item.label" @click="addFacetParam(item,'RN')">
                                <i v-show="!item.showSubFacet && !item.loading && item.children.length>0"  class="icon-plus" @click.stop="$set(item,'showSubFacet',true)"></i>
                                <i class="el-icon-loading" v-show="item.loading"></i>
                                <i class="icon-minus" v-show="item.showSubFacet" @click.stop="$set(item,'showSubFacet',false)"></i>
                                {{item.label}}<tt>{{formatNumber(item.value)}}</tt>
                            </a>
                            <ul class="cluster-list" v-if="item.showSubFacet">
                                <li class="cluster-item" v-show="!isSelected(child.key)" v-for="child in item.children" :key="child.key">
                                    <a href="javascript:void(0)" @click="addFacetParam(child,'RN')" :title="child.label">{{child.label}}<tt>{{formatNumber(child.value)}}</tt></a>
                                </li>
                            </ul>
                        </li>
                        <span v-if="facet.rangeFacet.length>5">
                            <i v-if="!facet.rangeFacet.showAll" @click="$set(facet.rangeFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.rangeFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.subjectFacet">
                    <h2 class="layui-colla-title" @click="$set(facet.subjectFacet,'showAll',facet.subjectFacet.showAll? false:true)">主题</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.subjectFacet">
                            <li v-show="isSelected(item.key)" v-for="item in facet.subjectFacet" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.label}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || (facet.subjectFacet.showAll  && index<20)) && !isSelected(item.key)" v-for="(item,index) in facet.subjectFacet" :key="item.key">
                            <a href="javascript:void(0)"  @click="addFacetParam(item,'SUB')" :title="item.label">{{item.label}}<tt>{{formatNumber(item.value)}}</tt></a>
                        </li>
                        <span v-if="facet.subjectFacet.length>5">
                            <i v-if="!facet.subjectFacet.showAll" @click="$set(facet.subjectFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.subjectFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.mediaFacet">
                    <h2 class="layui-colla-title" @click="$set(facet.mediaFacet,'showAll',facet.mediaFacet.showAll? false:true)">期刊</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.mediaFacet">
                            <li v-show="isSelected(item.key)" v-for="item in facet.mediaFacet" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.label}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || (facet.mediaFacet.showAll  && index<15)) && !isSelected(item.key)" v-for="(item,index) in facet.mediaFacet" :key="item.key">
                            <a href="javascript:void(0)" @click="addFacetParam(item,'GCH')" :title="item.label">{{item.label}}<tt>{{formatNumber(item.value)}}</tt></a>
                        </li>
                        <span v-if="facet.mediaFacet.length>5">
                            <i v-if="!facet.mediaFacet.showAll" @click="$set(facet.mediaFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.mediaFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.writerFacet">
                    <h2 class="layui-colla-title" @click="$set(facet.writerFacet,'showAll',facet.writerFacet.showAll? false:true)">作者</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term" v-if="facet.writerFacet">
                            <li v-show="isSelected(item.key)" v-for="item in facet.writerFacet" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.label}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || (facet.writerFacet.showAll  && index<20)) && !isSelected(item.key)" v-for="(item,index) in facet.writerFacet" :key="item.key">
                            <a href="javascript:void(0)" @click="addFacetParam(item,'AID')" :title="item.label">{{item.label}}<tt>{{formatNumber(item.value)}}</tt></a>
                        </li>
                        <span v-if="facet.writerFacet.length>5">
                            <i v-if="!facet.writerFacet.showAll" @click="$set(facet.writerFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.writerFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                    <div class="layui-colla-item" v-if="facet.organFacet">
                    <h2 class="layui-colla-title" @click="$set(facet.organFacet,'showAll',facet.organFacet.showAll? false:true)">机构</h2>
                    <div class="layui-colla-content">
                        <ul class="cluster-term">
                            <li v-show="item.searchField == 'SID'" v-for="item in facetParams" :key="item.key+item.value">
                                <a class="cluster-close" href="javascript:void(0)" @click="removeFacetParam(item)"><i class="icon-close"></i></a>
                                <span class="cluster-name">{{item.displayValue}}</span>
                            </li>
                        </ul>
                        <ul class="cluster-list">
                        <li class="cluster-item" v-show="(index<5 || (facet.organFacet.showAll  && index<20)) && !isSelected(organ.key)" v-for="(organ,index) in facet.organFacet" :key="organ.key">
                            <a href="javascript:void(0)"  @click="addFacetParam(organ,'SID')" :title="organ.label">
                                <i v-show="!organ.showSubFacet && !organ.loading"  class="icon-plus" @click.stop="getOrganSubFacet(organ)"></i>
                                <i class="el-icon-loading" v-show="organ.loading"></i>
                                <i class="icon-minus" v-show="organ.showSubFacet" @click.stop="organ.showSubFacet = false"></i>
                                {{organ.label}}<tt>{{formatNumber(organ.value)}}</tt>
                            </a>
                            <ul class="cluster-list" v-if="organ.showSubFacet">
                                <li class="cluster-item" v-show="!isSelected(subOrgan.key)" v-for="subOrgan in organ.subFacet" :key="subOrgan.key">
                                    <a href="javascript:void(0)" @click="addFacetParam(subOrgan,'SID')" :title="subOrgan.label">{{subOrgan.label}}<tt>{{formatNumber(subOrgan.value)}}</tt></a>
                                </li>
                            </ul>
                        </li>
                        <span v-if="facet.organFacet.length>5">
                            <i v-if="!facet.organFacet.showAll" @click="$set(facet.organFacet,'showAll',true)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                            <i v-else @click="$set(facet.organFacet,'showAll',false)" class="layui-icon layui-colla-icon" align="center" style="cursor:pointer"></i>
                        </span>
                        </ul>
                    </div>
                    </div>
                </div>
            </div>
            </div>
        </div>
        <div class="layui-form search-main">
            <div class="search-object" v-if="medias && medias.length>0">
            <h2 class="search-title">为您找到了以下期刊：</h2>
            <ul class="author-list">
                <li v-for="(item,index) in medias" :key="item.key" v-show="index<4"><router-link :to="'/journalsDetail/index?gch5='+item.key.substring(0,5)"><span class="name">{{item.label}}</span></router-link></li>
                <li class="more" v-if="medias.length>=4"><router-link :to="'/journal/index?keyword='+medias[0].keyword">查看更多</router-link></li>
            </ul>
            </div>
            <div class="row search-top">
            <div class="layui-col-xs5 search-result">共找到<span>{{formatNumber(data.total)}}</span>篇文章</div>
            <div class="layui-col-xs7">
                <div id="demo1" class="page">
                    <el-pagination
                        layout="prev, pager, next"
                        :current-page="searchParam.pageNum"
                        :page-size="searchParam.pageSize"
                        :total="data.total && data.total > 5000 ? 5000 : (data.total || 0)"
                        :pager-count="5"
                        size="mini"
                        style="margin-top:2%"
                        @current-change="handlePageChange"
                    ></el-pagination>
                </div>
                <div class="page-view">
                    每页显示
                    <a :class="searchParam.pageSize==20?'active':''" href="javascript:void(0)" @click="searchParam.pageNum=1;searchParam.pageSize=20;searchParamChange()">20</a>
                    <a :class="searchParam.pageSize==50?'active':''" href="javascript:void(0)" @click="searchParam.pageNum=1;searchParam.pageSize=50;searchParamChange()">50</a>
                    <a :class="searchParam.pageSize==100?'active':''" href="javascript:void(0)" @click="searchParam.pageNum=1;searchParam.pageSize=100;searchParamChange()">100</a>
                </div>
            </div>
            </div>
            <div class="row search-op">
            <div class="layui-col-xs5">
                <div class="selection"> <span class="select-all" title="全选">
                <el-checkbox style="z-index:100" v-model="checkAll" @change="(val)=>{checkAllChange(val)}"></el-checkbox>
                </span> <span class="selected-count"><a href="javascript:void(0);" @click="showSelections">已选择<span>{{selections.length}}</span>条</a> <a href="javascript:void(0);" @click="confirmRemoveSelections"><i class="icon-del"></i></a></span> </div>
                <div class="selection-tool">
                <div class="layui-btn-group"> <a class="layui-btn layui-btn-xs" @click="toExport()">导出题录</a> </div>
                </div>
            </div>
            <div class="layui-col-xs7">
                <div class="btn-switch"> 显示方式：
                <div class="layui-btn-group">
                    <a :class="'layui-btn layui-btn-xs ' + (displayMode=='simple-list'?'active':'')" data-key="2" data-type="set" @click="displayMode='simple-list'" href="javascript:void(0)"><i class="icon-simple"></i>文摘</a>
                    <a :class="'layui-btn layui-btn-xs ' + (displayMode=='full-list'?'active':'')" data-key="3" data-type="set" @click="displayMode='full-list'" href="javascript:void(0)"><i class="icon-full"></i>详细</a>
                    <a :class="'layui-btn layui-btn-xs ' + (displayMode=='table-list'?'active':'')" data-key="1" data-type="set" @click="displayMode='table-list'" href="javascript:void(0)"><i class="icon-table"></i>列表</a>
                </div>
                </div>
                <div class="btn-switch">
                    <div class="layui-btn-group">
                        <a :class="'layui-btn layui-btn-xs '+ (!searchParam.sortField?'active':'')" data-key="indicator" data-type="set" @click="searchParam.sortField='';searchParamChange()" href="javascript:void(0)"><i class="icon-sort"></i>相关度排序</a>
                        <a :class="'layui-btn layui-btn-xs '+ (searchParam.sortField == 'zkbycount'?'active':'')" style="" data-key="indicator" data-type="set" @click="searchParam.sortField='zkbycount';searchParamChange()" href="javascript:void(0)"><i class="icon-sort"></i>被引量排序</a>
                        <a :class="'layui-btn layui-btn-xs '+ (searchParam.sortField == 'publishdate'?'active':'')" style="" data-key="indicator" data-type="set" @click="searchParam.sortField='publishdate';searchParamChange()" href="javascript:void(0)"><i class="icon-sort"></i>时效排序</a>
                    </div>
                </div>
            </div>
            </div>
            <div class="no-data"  v-if="!data || !data.rows || !data.rows.length">
                <div>抱歉，暂无相关结果。请调整您的检索策略，或者查看<router-link to="/search/help" target="_blank">使用帮助</router-link>。</div>
            </div>
            <div class="search-result-list" id="layerDemo">
            <div class="simple-list" id="1" v-if="displayMode == 'simple-list' && data.rows" style="display:block">
                <dl class="" v-for="(article,index) in data.rows" :key="article.id">
                <dt>
                    <router-link :to="'/detail/index?lngid='+article.ndo_content.lngid" target="_blank" v-html="article.ndo_content.title_c || article.ndo_content.title_e"></router-link>
                    <!-- <span class="pre-view"><a method="notice" href="#"><i class="icon-view"></i>预览</a></span> -->
                    <span style="margin-left:10px" class="cited" v-if="article.ndo_content.zkbycount>0">被引量：{{article.ndo_content.zkbycount}}</span>
                    <!-- <span class="downloaded">下载量：111</span> -->
                </dt>
                <dd class="no">{{(searchParam.pageNum-1)*searchParam.pageSize+index+1}}</dd>
                <dd class="sel">
                    <el-checkbox style="z-index:100" v-model="article.checked" @change="(val)=>{checkedChange(val,article)}"></el-checkbox>
                </dd>
                <dd><span class="author" v-if="article.ndo_content.showwriter">
                  <span class="label">作者</span>
                    <span v-show="(article.showAllWriter || i<2)" v-for="(author,i) in article.ndo_content.showwriter.split(';')" :key="author">
                        <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(author))" :title="author" v-html="formatAuthor(author)"></a>
                    </span>
                    <span class="more hide" v-if="article.ndo_content.showwriter.split(';').length>3 && !article.showAllWriter">
                        <a class="num" href="javascript:void(0)" @click=" $set(article,'showAllWriter',true)">+{{article.ndo_content.showwriter.split(';').length-3}} 位作者</a>
                    </span>
                    <span v-if="(!article.showAllWriter && article.ndo_content.showwriter.split(';').length>3 )">
                        <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1]))" v-html="formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1])"></a>
                    </span>
                    </span>
                    <span class="from"><router-link :to="'/journalsDetail/index?gch5='+article.ndo_content.gch5" :title="article.ndo_content.media_c || article.ndo_content.media_e">《<span v-html="article.ndo_content.media_c || article.ndo_content.media_e"></span>》</router-link>
                        <span class="medal">
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('SCIE') != -1">SCIE</span>
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('EI') != -1">EI</span>
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CAS') != -1">CAS</span>
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSSCI') != -1">CSSCI</span>
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSCD') != -1">CSCD</span>
                            <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('BDHX') != -1">北大核心</span>
                        </span>
                    </span>
                    <span class="vol" v-if="article.ndo_content.num"><span v-html="article.ndo_content.years"></span>年第{{article.ndo_content.num}}期{{article.ndo_content.beginpage+'-'+article.ndo_content.endpage}},{{article.ndo_content.jumppage || ''}}共{{article.ndo_content.pagecount}}页</span>
                </dd>
                <dd>
                    <span class="abstract" v-if="article.ndo_content.remark_c">
                    <span class="" v-html="article.ndo_content.remark_c.length>200 && !article.showAllRemark ? (article.ndo_content.remark_c.substring(0,200)+'...') : article.ndo_content.remark_c">
                    </span>
                    <a v-if="article.ndo_content.remark_c.length>200 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                    </span>

                    <span class="abstract" v-else-if="article.ndo_content.remark_e">
                    <span class="" v-html="article.ndo_content.remark_e.length>300 && !article.showAllRemark ? (article.ndo_content.remark_e.substring(0,300)+'...') : article.ndo_content.remark_e">
                    </span>
                    <a v-if="article.ndo_content.remark_e.length>300 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                    </span>
                </dd>
                <dd>
                    <span v-if="article.ndo_content.keyword_c" class="subject"><span class="label">关键词</span>
                        <span v-for="keyword in article.ndo_content.keyword_c.split(';')" :key="keyword">
                            <a href="javascript:void(0)" @click="changeSearchModel('K',formatKeyword(keyword))" :title="keyword" v-html="formatKeyword(keyword)"></a>
                        </span>
                    </span>
                    <span v-else-if="article.ndo_content.keyword_e" class="subject"><span class="label">关键词</span>
                        <span v-for="keyword in article.ndo_content.keyword_e.split(';')" :key="keyword">
                            <a href="javascript:void(0)" @click="changeSearchModel('K',formatKeyword(keyword))" :title="keyword" v-html="formatKeyword(keyword)"></a>
                        </span>
                    </span>
                </dd>
                <dd class="source" v-if="currentUser&&currentUser.paymentType=='Full_text'">
                    <a v-if="!article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)" @click="readPdf(article.ndo_content.lngid)"><i class="icon-read"></i>在线阅读</a>
                    <a v-if="!article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)" @click="downloadPdf(article.ndo_content.lngid)"><i class="icon-down"></i>下载PDF</a>
                    <a v-if="article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)"><i class="el-icon-warning"></i>暂无订购</a>
                    <!-- <a href="#"><i class="icon-down"></i>免费下载</a><a href="#"><i class="icon-send"></i>原文传递</a><a href="#"><i class="icon-oa"></i>OA链接</a><a href="#"><i class="icon-site"></i>网络资源外链</a><a href="#"><i class="icon-rar"></i>职称评审资料打包下载</a> -->
                </dd>
                </dl>

                <!-- <dl class="declare">
                    <dt>由于版权政策及相关保密法规原因，部分结果未予显示。</dt>
                </dl> -->
            </div>
            <div class="full-list" id="2" v-if="displayMode == 'full-list' && data.rows" style="display:block" >
                <dl class="" v-for="(article,index) in data.rows" :key="article.id">
                <dt><span class="label">题名</span>
                    <router-link :to="'/detail/index?lngid='+article.ndo_content.lngid" target="_blank" index="0" v-html="article.ndo_content.title_c || article.ndo_content.title_e"></router-link>
                    <span class="cited">被引量：{{article.ndo_content.zkbycount}}</span></dt>
                <dd class="sel">
                    <el-checkbox style="z-index:100" v-model="article.checked" @change="(val)=>{checkedChange(val,article)}"></el-checkbox>
                </dd>
                <dd class="no">{{(searchParam.pageNum-1)*searchParam.pageSize+index+1}}</dd>
                <dd v-if="article.ndo_content.showwriter">
                    <span class="label">作者</span>
                    <span class="author">
                        <span v-show="(article.showAllWriter || i<2)" v-for="(author,i) in article.ndo_content.showwriter.split(';')" :key="author">
                            <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(author))" :title="author" v-html="formatAuthor(author)"></a>
                        </span>
                        <span class="more hide" v-if="article.ndo_content.showwriter.split(';').length>3 && !article.showAllWriter">
                            <a class="num" href="javascript:void(0)" @click=" $set(article,'showAllWriter',true)">+{{article.ndo_content.showwriter.split(';').length-3}} 位作者</a>
                        </span>
                        <span v-if="(!article.showAllWriter && article.ndo_content.showwriter.split(';').length>3 )">
                            <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1]))" v-html="formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1])"></a>
                        </span>
                    </span>
                </dd>
                <dd v-if="article.ndo_content.showorgan"><span class="label">机构</span><span class="organ">
                    <span v-for="organ in article.ndo_content.showorgan.split(';')" :key="organ"><a href="javascript:void(0)" @click="changeSearchModel('S',formatOrgan(organ))" v-html="formatOrgan(organ)"></a></span>
                </span></dd>
                <dd><span class="label">出处</span>
                <span class="from">
                  <router-link :to="'/journalsDetail/index?gch5='+article.ndo_content.gch5" :title="article.ndo_content.media_c || article.ndo_content.media_e" v-html="'《'+(article.ndo_content.media_c || article.ndo_content.media_e)+'》'"></router-link>
                  <span class="medal">
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('SCIE') != -1">SCIE</span>
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('EI') != -1">EI</span>
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CAS') != -1">CAS</span>
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSSCI') != -1">CSSCI</span>
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSCD') != -1">CSCD</span>
                    <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('BDHX') != -1">北大核心</span>
                  </span>
                </span>
                  <span class="vol" v-if="article.ndo_content.num">{{article.ndo_content.years}}年第{{article.ndo_content.num}}期{{article.ndo_content.beginpage+'-'+article.ndo_content.endpage}},{{article.ndo_content.jumppage || ''}}共{{article.ndo_content.pagecount}}页</span>
                </dd>
                <dd v-if="article.ndo_content.imburse"><span class="label">基金</span><span class="fund">
                    <span v-for="fund in article.ndo_content.imburse.split(';')" :key="fund"><span  @click="changeSearchModel('I',fund)" v-html="fund"></span></span>
                </span></dd>
                <dd v-if="article.ndo_content.remark_c || article.ndo_content.remark_e"><span class="label">文摘</span><span class="abstract">
                    <span class="abstract" v-if="article.ndo_content.remark_c">
                    <span class="" v-html="article.ndo_content.remark_c.length>200 && !article.showAllRemark ? (article.ndo_content.remark_c.substring(0,200)+'...') : article.ndo_content.remark_c">
                    </span>
                    <a v-if="article.ndo_content.remark_c.length>200 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                    </span>

                    <span class="abstract" v-else-if="article.ndo_content.remark_e">
                    <span class="" v-html="article.ndo_content.remark_e.length>300 && !article.showAllRemark ? (article.ndo_content.remark_e.substring(0,300)+'...') : article.ndo_content.remark_e">
                    </span>
                    <a v-if="article.ndo_content.remark_e.length>300 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                    </span>
                </span></dd>
                <dd>
                    <span v-if="article.ndo_content.keyword_c" class="subject"><span class="label">关键词</span>
                        <span v-for="keyword in article.ndo_content.keyword_c.split(';')" :key="keyword">
                            <a href="javascript:void(0)" @click="changeSearchModel('K',formatKeyword(keyword))" :title="keyword" v-html="formatKeyword(keyword)"></a>
                        </span>
                    </span>
                </dd>
                <dd class="en" v-if="article.ndo_content.keyword_e"><span class="label">Keywords</span>
                    <span class="subject">
                        <span v-for="keyword in article.ndo_content.keyword_e.split(';')" :key="keyword">
                            <a href="javascript:void(0)" @click="changeSearchModel('K',formatKeyword(keyword))" :title="keyword" v-html="formatKeyword(keyword)"></a>
                        </span>
                </span></dd>
                <dd v-if=" article.ndo_content.class"><span class="label">分类号</span>
                <span class="class">
                    <span v-for="(cls) in article.ndo_content.class.split(';')" :key="cls">
                        <a href="javascript:void(0)" @click="changeSearchModel('C',cls)" :title="cls" v-html="cls"></a>
                    </span>
                    <!-- [<span><a href="/domain/article.aspx?id=490&amp;from=article_detail">医药卫生—心血管疾病</a></span>
                    <span><a href="/domain/article.aspx?id=481&amp;from=article_detail">医药卫生—临床医学</a></span>
                    <span><a href="/domain/article.aspx?id=489&amp;from=article_detail">医药卫生—内科学</a></span>]  -->
                </span></dd>
                  <dd class="source" v-if="currentUser&&currentUser.paymentType=='Full_text'">
                    <a v-if="!article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)" @click="readPdf(article.ndo_content.lngid)"><i class="icon-read"></i>在线阅读</a>
                    <a v-if="!article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)" @click="downloadPdf(article.ndo_content.lngid)"><i class="icon-down"></i>下载PDF</a>
                    <a v-if="article.ndo_content.userDatabaseInfoInvalid" href="javascript:void(0)"><i class="el-icon-warning"></i>暂无订购</a>
                  </dd>
                </dl>
            </div>
            <div class="table-list" id="3" v-if="displayMode == 'table-list' && data.rows" style="display:block">
                <table>
                <colgroup>
                <col width="25">
                <col>
                <col width="180">
                <col width="150">
                <col width="80">
                <col width="100">
                </colgroup>
                <thead>
                    <tr>
                    <th></th>
                    <th>题名</th>
                    <th>作者</th>
                    <th>出处</th>
                    <th>发文年</th>
                    <th>被引量</th>
                    <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(article,index) in data.rows" :key="article.id">
                    <td><el-checkbox  style="z-index:100" v-model="article.checked" @change="(val)=>{checkedChange(val,article)}"></el-checkbox><div class="table-no"><span class="no">{{(searchParam.pageNum-1)*searchParam.pageSize+index+1}}</span></div></td>
                    <td class="title"><router-link :to="'/detail/index?lngid='+article.ndo_content.lngid" target="_blank" v-html="article.ndo_content.title_c || article.ndo_content.title_e"></router-link></td>
                    <td class="t-left">
                        <div v-if="article.ndo_content.showwriter">
                            <span v-for="(author,i) in article.ndo_content.showwriter.split(';')" :key="author" style="margin-right:5px">
                                <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(author))" :title="author" v-html="formatAuthor(author)"></a>
                            </span>
                        </div>
                    </td>
                    <td class="t-left">
                        <router-link :to="'/journalsDetail/index?gch5='+article.ndo_content.gch5" :title="article.ndo_content.media_c || article.ndo_content.media_e">《<span v-html="article.ndo_content.media_c || article.ndo_content.media_e"></span>》</router-link>
                        <span class="medal">
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('SCIE') != -1">SCIE</span>
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('EI') != -1">EI</span>
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CAS') != -1">CAS</span>
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSSCI') != -1">CSSCI</span>
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('CSCD') != -1">CSCD</span>
                          <span v-show="article.ndo_content.range && article.ndo_content.range.indexOf('BDHX') != -1">北大核心</span>
                        </span>
                        <!-- <span v-if="article.ndo_content.num">{{article.ndo_content.years}}年第{{article.ndo_content.num}}期</span> -->
                    </td>
                    <td class="cited">{{article.ndo_content.years}}</td>
                    <td class="cited">{{article.ndo_content.zkbycount}}</td>
                    <dd class="source" v-if="currentUser&&currentUser.paymentType=='Full_text'">
                      <el-button size="mini" href="javascript:void(0)" icon="icon-read" circle v-if="!article.ndo_content.userDatabaseInfoInvalid" @click="readPdf(article.ndo_content.lngid)"></el-button>
                      <el-button size="mini" href="javascript:void(0)" icon="icon-down" circle v-if="!article.ndo_content.userDatabaseInfoInvalid" @click="downloadPdf(article.ndo_content.lngid)"></el-button>
                      <el-button size="mini" href="javascript:void(0)" icon="el-icon-warning" v-if="article.ndo_content.userDatabaseInfoInvalid">暂无订购</el-button>
                    </dd>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
            <div class="row search-bottom">
            <div class="layui-col-xs5">
                <div class="selection"> <span class="select-all" title="全选">
                <el-checkbox style="z-index:100" v-model="checkAll" @change="(val)=>{checkAllChange(val)}"></el-checkbox>
                </span> <span class="selected-count"><a href="javascript:void(0);">已选择<span>{{selections.length}}</span>条</a></span> <a href="javascript:void(0);" @click="confirmRemoveSelections"><i class="icon-del"></i></a></div>
                <div class="selection-tool">
                <div class="layui-btn-group"> <a class="layui-btn layui-btn-xs" @click="toExport()">导出题录</a> </div>
                </div>
            </div>
            <div class="layui-col-xs7">
                <div id="demo7" class="page">
                    <div class="layui-box layui-laypage layui-laypage-default" id="layui-laypage-10">
                        <!-- <a href="javascript:;" class="layui-laypage-prev layui-disabled" data-page="0">上一页</a><span class="layui-laypage-curr"><em class="layui-laypage-em"></em><em>1</em></span><a href="javascript:;" data-page="2">2</a><a href="javascript:;" data-page="3">3</a><a href="javascript:;" data-page="4">4</a><a href="javascript:;" data-page="5">5</a><span class="layui-laypage-spr">…</span><a href="javascript:;" class="layui-laypage-last" title="尾页" data-page="10">10</a><a href="javascript:;" class="layui-laypage-next" data-page="2">下一页</a><span class="layui-laypage-skip">到第
                        <input type="text" min="1" value="1" class="layui-input">
                        页
                        <button type="button" class="layui-laypage-btn">确定</button>
                        </span> -->
                        <el-pagination
                            layout="prev, pager, next, jumper"
                            :current-page="searchParam.pageNum"
                            :page-size="searchParam.pageSize"
                            :total="data.total && data.total > 5000 ? 5000 : (data.total || 0)"
                            :pager-count="5"
                            size="mini"
                            style="margin-top:2%"
                            @current-change="handlePageChange"
                        ></el-pagination>
                    </div>
                </div>
            </div>
            </div>
        </div>
    </div>
  </div>
  <el-dialog  :visible.sync="selectionDialogVisible" width="50%" :close-on-click-modal="false" :inline="true">
    <div class="layui-layer layui-layer-page layui-layer-rim" id="layui-layer4" type="page" times="4" showtime="0" contype="object" style=" width: 100%;"><div class="layui-layer-title" style="cursor: move;"> </div><div id="" class="layui-layer-content" style="height: 311px;"><div id="selection" class="selection-box layui-layer-wrap" style="display: block;"><div class="title">已选择<span id="selectArticleCount">{{selections.length}}</span>条</div>
      <div class="content">
          <div class="row list-op">
              <div class="layui-col-xs3"><a @click="confirmRemoveSelections" class="layui-btn layui-btn-xs" id="clearSelectArticle" href="javascript:void(0)">清空</a></div>
              <div class="layui-col-xs9">
                  <div class="layui-btn-group">
                      <a class="layui-btn layui-btn-xs behavior-exporttitle" data-key="export" @click="toExport()" href="javascript:void(0)">导出题录</a>
                  </div>
              </div>
          </div>
          <div class="selection-list">
            <dl class="" v-for="(article,i) in selectionVals" :key="article.id">
                <dt><router-link :to="'/detail/index?lngid='+article.ndo_content.lngid" target="_blank" v-html="article.ndo_content.title_c || article.ndo_content.title_e"></router-link></dt>
                <dd>
                    <span class="author" v-if="article.ndo_content.showwriter">
                      <span class="label">作者</span>
                      <span v-show="(article.showAllWriter || i<2)" v-for="(author,i) in article.ndo_content.showwriter.split(';')" :key="author">
                          <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(author))" :title="author" v-html="formatAuthor(author)"></a>
                      </span>
                      <span class="more hide" v-if="article.ndo_content.showwriter.split(';').length>3 && !article.showAllWriter">
                          <a class="num" href="javascript:void(0)" @click=" $set(article,'showAllWriter',true)">+{{article.ndo_content.showwriter.split(';').length-3}} 位作者</a>
                      </span>
                      <span v-if="(!article.showAllWriter && article.ndo_content.showwriter.split(';').length>3 )">
                          <a href="javascript:void(0)" @click="changeSearchModel('A',formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1]))" v-html="formatAuthor(article.ndo_content.showwriter.split(';')[article.ndo_content.showwriter.split(';').length-1])"></a>
                      </span>
                    </span>
                    <span class="from">
                      <router-link :to="'/journalsDetail/index?gch5='+article.ndo_content.gch5" :title="article.ndo_content.media_c || article.ndo_content.media_e">《<span v-html="article.ndo_content.media_c || article.ndo_content.media_e"></span>》</router-link>
                      <span class="vol" v-if="article.ndo_content.num">{{article.ndo_content.years}}年第{{article.ndo_content.num}}期{{article.ndo_content.beginpage+'-'+article.ndo_content.endpage}},{{article.ndo_content.jumppage || ''}}共{{article.ndo_content.pagecount}}页</span>
                    </span>
                </dd>
                <dd>
                  <span class="abstract" v-if="article.ndo_content.remark_c">
                    <span class="" v-html="article.ndo_content.remark_c.length>200 && !article.showAllRemark ? (article.ndo_content.remark_c.substring(0,200)+'...') : article.ndo_content.remark_c">
                    </span>
                    <a v-if="article.ndo_content.remark_c.length>200 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                  </span>

                  <span class="abstract" v-else-if="article.ndo_content.remark_e">
                    <span class="" v-html="article.ndo_content.remark_e.length>300 && !article.showAllRemark ? (article.ndo_content.remark_e.substring(0,300)+'...') : article.ndo_content.remark_e">
                    </span>
                    <a v-if="article.ndo_content.remark_e.length>300 && !article.showAllRemark" href="javascript:void(0)" @click="$set(article,'showAllRemark',true)">展开更多</a>
                  </span>
                </dd>
                <dd class="op">
                    <a class="layui-btn" @click="removeSelection(article,i)" href="javascript:void(0)"><i class="icon-del"></i></a>
                </dd>
            </dl>
          </div>
      </div>
      </div></div></div>
    </el-dialog>
</div>
</template>
<script>
import axios from 'axios'
import params from '@/api/common/params'
import { MessageBox } from 'element-ui'
import { downloadPdf, readPdf } from '@/utils/zipdownload'
export default {
  name: 'SearchResult',
  props: [
    'data',
    'facet',
    'getSubFacet',
    'search',
    'medias'
  ],
  data () {
    return {
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
      displayMode: 'simple-list', // 显示方式
      loadingFacet: true,
      loading: true,
      secondParams: [], // 已添加的二次检索条件,仅用于展示
      secondSearchModel: { // 二次检索条件,仅用于展示
        searchField: 'T',
        value: '',
        exact: false, // 是否精确检索
        logicOperator: 'AND',
        displayValue: ''
      },
      params: params,
      facetParams: [], // 聚类检索条件,仅用于展示
      selectedYearFacet: [], // 选中的年份聚类条件，用于展示
      selections: [], // 多选框选中项
      checkAll: false, // 全选
      selectionDialogVisible: false,
      selectionVals: [] // 选中项去掉高亮的结果
    }
  },
  mounted () {
    // if (this.$route.query && this.$route.query.searchModels) {
    //   this.searchParam.searchModels = JSON.parse(this.$route.query.searchModels)
    // }
  },
  methods: {
    downloadPdf: downloadPdf,
    readPdf: readPdf,
    handlePageChange (val) {
      this.searchParam.pageNum = val
      this.searchParamChange()
    },
    // 去掉作者的[1]
    formatAuthor (author) {
      if (!author) {
        return ''
      }
      if (author.indexOf('[') === -1) {
        return author
      }
      return author.substring(0, author.indexOf('['))
    },
    formatOrgan (organ) {
      if (!organ) {
        return ''
      }
      if (organ.indexOf(']') !== -1) {
        organ = organ.substring(organ.indexOf(']') + 1, organ.length)
      }
      if (organ.indexOf(',') !== -1) {
        organ = organ.substring(0, organ.indexOf(','))
      }
      return organ
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
      // this.$router.replace({ path: '/search/index', query: { searchModels: JSON.stringify(this.searchParam.searchModels) } })
      // setTimeout(() => {
      //   this.$router.replace('/refresh')
      // }, 10)
      this.$emit('changeSearchModel',this.searchParam.searchModels)
    //   this.searchParamChange()
    },
    /**
     * 添加检索条件
     * params:要添加检索条件的数组
     * logicOperator: 操作符
     */
    addSecondSearchModel (logicOperator) {
      if (!this.secondSearchModel.value) {
        this.$message.error('请输入关键字!')
        return
      }
      this.secondSearchModel.logicOperator = logicOperator || 'AND'
      this.secondSearchModel.displayValue = this.defineDisplayValue(this.secondSearchModel)
      this.secondParams.push(JSON.parse(JSON.stringify(this.secondSearchModel)))
      this.generateSearchParam()
      this.secondSearchModel.value = ''
      this.searchParamChange()
    },
    // 删除二次检索条件
    removeSecondParam (i) {
      this.secondParams.splice(i, 1)
      this.generateSearchParam()
      this.searchParamChange()
    },
    generateSearchParam () {
    //   重组检索条件,第一条为输入框简单检索,需保留
    //   this.searchParam.searchModels = [this.searchParam.searchModels[0]]
      this.searchParam.searchModels = []
      this.searchParam.searchModels = this.searchParam.searchModels.concat(this.secondParams)
      this.searchParam.searchModels = this.searchParam.searchModels.concat(this.facetParams)
    },
    removeSelection (article, i) {
      this.$confirm('您确认要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.selections.splice(i, 1)
        this.selectionVals.splice(i, 1)
        this.data.rows.forEach(item => {
          if (item.id === article.id) { item.checked = false }
        })
        this.checkAll = false
      })
    },
    defineDisplayValue (searchModel) {
      let displayValue = searchModel.logicOperator
      params.searchFields.forEach(item => {
        if (item.value === searchModel.searchField) {
          displayValue += ' ' + item.label + '=' + searchModel.value
        }
      })
      return displayValue
    },
    getClassSubFacet (cls) {
      if (cls.subFacet) {
        this.$set(cls, 'showSubFacet', true)
        return
      }
      this.$set(cls, 'loading', true)
      this.getSubFacet(this.searchParam, 'classids_s:' + cls.key, 'view/search/getClassSubFacet')
        .then(facet => {
          this.$set(cls, 'subFacet', facet)
          this.$set(cls, 'loading', false)
          this.$set(cls, 'showSubFacet', true)
        })
    },
    getOrganSubFacet (organ) {
      if (organ.subFacet) {
        this.$set(organ, 'showSubFacet', true)
        return
      }
      this.$set(organ, 'loading', true)
      this.getSubFacet(this.searchParam, 'organids_s:' + organ.key, 'view/search/getOrganSubFacet')
        .then(facet => {
          this.$set(organ, 'subFacet', facet)
          this.$set(organ, 'loading', false)
          this.$set(organ, 'showSubFacet', true)
        })
    },
    // 添加聚类查询条件
    addFacetParam (item, searchField) {
      const param = {
        searchField: searchField,
        value: item.key,
        exact: true, // 是否精确检索
        logicOperator: 'AND',
        displayValue: item.label
      }
      this.facetParams.push(param)
      //   console.log(this.facetParams)
      this.generateSearchParam()
      this.searchParamChange()
    },
    removeFacetParam (item) {
      let index = -1
      this.facetParams.forEach((param, i) => {
        if (param.displayValue === item.label) {
          index = i
        }
      })
      this.facetParams.splice(index, 1)
      this.generateSearchParam()
      this.searchParamChange()
    },
    searchParamChange () {
      this.loadingFacet = true
      this.loading = true
      this.search(this.searchParam)
    },
    isSelected (key) {
      let selected = false
      this.facetParams.forEach(item => {
        if (item.value === key) {
          selected = true
          return selected
        }
      })
      return selected
    },
    checkedChange (val, article) {
      if (this.selections.length >= 500) {
        this.$message.warning('最多只能选择500条')
        this.$set(article, 'checked', false)
        return
      }
      const index = this.getCheckedIndex(article)
      if (val && index === -1) {
        this.selections.push(article)
      } else if (!val && index !== -1) {
        this.selections.splice(this.selections.indexOf(article), 1)
      }
      this.$set(article, 'checked', val)
    },
    checkAllChange (val) {
      for (let i = 0; i < this.data.rows.length; i++) {
        const item = this.data.rows[i]
        if (this.checkAll && this.selections.length >= 500) {
          this.$message.warning('最多只能选择500条')
          this.checkAll = false
          return
        }
        const index = this.getCheckedIndex(item)
        if (val && index === -1) {
          this.selections.push(item)
          this.$set(item, 'checked', val)
        } else if (!val && index !== -1) {
          this.selections.splice(index, 1)
          this.$set(item, 'checked', val)
        }
      }
    },
    // 获取item在选中项中的下标，未选中返回-1
    getCheckedIndex (item) {
      let index = -1
      this.selections.forEach((selection, i) => {
        if (selection.id === item.id) {
          index = i
        }
      })
      return index
    },
    showSelections () {
      if (this.selections.length == 0) {
        this.$message.warning('请选择要导出的文献!')
        return
      }
      const ids = []
      this.selections.forEach(item => {
        ids.push(item.ndo_content.lngid)
      })
      const loading = this.$loading({
        lock: true,
        text: '加载中...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.5)'
      })
      axios({
        method: 'post',
        url: 'view/search/getArticlesByIds',
        data: ids
      }).then(res => {
        if (!res.data.success) {
          this.$message.error(res.data.msg)
          return
        }
        loading.close()
        this.selectionVals = res.data.pageBean.rows
        this.selectionDialogVisible = true
        // this.selections = []
      }).catch(e => { loading.close() })
    },
    confirmRemoveSelections () {
      this.$confirm('确认要清空已选文章吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.selections = []
        this.data.rows.forEach(item => {
          item.checked = false
        })
        this.checkAll = false
        this.selectionDialogVisible = false
      })
    },
    /**
     * 导出题录
     */
    toExport () {
      const arr = this.selections
      if (!arr || arr.length <= 0) {
        MessageBox.confirm('请选择需要导出的数据', '警告', {
          type: 'error'
        }).then(() => {}).catch(() => {})
      } else {
        const ids = []
        arr.forEach(item => { ids.push(item.ndo_content.lngid) })
        const routePath = this.$router.resolve({
          path: '/export/index',
          query: {
            ids: ids
          }
        })
        window.open(routePath.href, '_blank')
      }
    },
    formatNumber (num) {
      if (!num) {
        return 0
      }
      return num.toString().replace(/\d+/, function (n) {
        return n.replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      })
    },
    getUserInfo () {
      this.$axiosRequest('view/user/getLoginUserFromDb', {}, this.$axiosOptions('get')).then(res => {
        this.currentUser = res.data
      }).finally(e => {

      })
    }
  },
  watch: {
    facet () {
      if (!this.facet ) {
        this.loadingFacet = true
      } else {
        this.loadingFacet = false
      }
    },
    data () {
      const displayMode = this.displayMode + ''
      const loadingFacet = this.loadingFacet
      const selections = this.selections
      const pageSize = this.searchParam.pageSize
      if (!this.loading) {
        Object.assign(this.$data, this.$options.data())
      }
      this.selections = selections
      this.checkAll = false
      this.displayMode = displayMode
      this.loadingFacet = loadingFacet
      this.searchParam.pageSize = pageSize
      this.loading = false
      if (this.data && this.data.rows) {
        this.data.rows.forEach(item => {
          if (this.getCheckedIndex(item) != -1) {
            item.checked = true
          }
        })
      }
      this.getUserInfo()
    }

  }
}
</script>
<style scoped src="@/assets/css/search.css"></style>
<style scoped>
>>> .el-dialog{
  height: auto !important;
}
</style>
