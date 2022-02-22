<template>
  <div>
    <vip-header></vip-header>
    <div id="body">

      <div class="l-width">
        <div class="row web-intro">
          <h1><span class="title">导出题录</span></h1>

        </div>
        <div class="row export">

          <div class="layui-tab">
            <div class="export-type">
              <div class="label">文献导出格式</div>
              <ul class="layui-tab-title">
                <li v-for="(type,indexT) in showType" :class="{'layui-this':typeIndex == indexT}"
                    @click="changeSHowType(indexT)">{{ type }}
                </li>
              </ul>
            </div>
            <div class="export-op" v-if="typeIndex !== 8">
              <a class="layui-btn layui-btn-primary" @click="doCopy()">
                <i class="icon-copy"></i>复制
              </a>
<!--              <a class="layui-btn layui-btn-primary" @click="printFile()">-->
<!--                <i class="icon-print"></i>打印-->
<!--              </a>-->
              <a class="layui-btn layui-btn-primary" @click="exportFile()">
                <i class="icon-export"></i>导出
              </a>
            </div>
            <div class="layui-tab-content export-content">
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 0 || typeIndex == 9}">
                <p v-for="(item,indexA) in articleList" :key="indexA" >
                  [{{ (indexA + 1) }}]
                  <font style="color: #008000">{{ item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '').replace(";",",")+'.' : '' }}</font>
                  <font style="color: #034481">{{ item.title_c }}.</font>
                  <font style="color: #008000">{{ item.media_c }},
                    {{item.years }}({{ item.num }}):{{ item.beginpage }}-{{ item.endpage }}.</font>
                  <br>
                  {{ item.remark_c }}<br>
                </p>
              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 1}">
                <p v-for="(item,indexA) in articleList" :key="indexA">
                  [{{ (indexA + 1) }}]
                  <font style="color: #008000">{{ item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '').replace(";",",")+'.' : '' }}</font>
                  <font style="color: #034481">{{ item.title_c }}.</font>
                  <font style="color: #008000">{{ item.media_c }},
                    {{item.years }}({{ item.num }}):{{ item.beginpage }}-{{ item.endpage }}.</font>
                  <br>
                  机构: {{ item.showorgan ? item.showorgan.replace(/\[.*?\]/g, '') : '' }}<br>
                  摘要: {{ item.remark_c }}<br>
                </p>
              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 2}">
                <p v-for="(item,indexA) in articleList" :key="indexA">
                  [{{ (indexA + 1) }}]{{ item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '').replace(";",",")+'.' : ''  }}
                  {{ item.title_c + '[J]'}}.{{ item.media_c }},{{item.years }}({{ item.num }}):{{ item.beginpage }}-{{ item.endpage }}.<br>
                </p>

              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 3}">

                &lt;?xml version="1.0" encoding="utf-8"?&gt;
                <br>&lt;ResourceList&gt;
                <template v-for="(item,indexA) in articleList">
                  <br>
                  <br> &lt;PeriodicalPaper xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
                  <br> &lt;Type&gt;1&lt;/Type&gt;
                  <br> &lt;ID&gt;{{ item.lngid }}&lt;/ID&gt;
                  <br> &lt;Titles&gt;
                  <br> &lt;Title&gt;
                  <br> &lt;Language&gt;chi&lt;/Language&gt;
                  <br> &lt;Text&gt;&lt;![CDATA[{{ item.title_c }}]]&gt;&lt;/Text&gt;
                  <br> &lt;/Title&gt;
                  <br> &lt;/Titles&gt;
                  <br> &lt;Creators&gt;
                  <template v-if="item.showwriter">
                    <template v-for="(w,indexW) in item.showwriter.split(';')">
                      <br> &lt;Creator&gt;
                      <br> &lt;Name&gt;&lt;![CDATA[{{ w }},]]&gt;&lt;/Name&gt;
                      <br> &lt;/Creator&gt;
                    </template>
                  </template>
                  <br> &lt;/Creators&gt;
                  <br> &lt;PublishDate&gt;&lt;![CDATA[{{ item.publishdate }}]]&gt;&lt;/PublishDate&gt;
                  <br> &lt;CLCs&gt;
                  <template v-if="item.class">
                    <template v-for="(c,indexC) in item.class.split(';')">
                      <br> &lt;CLC&gt;
                      <br> &lt;Code&gt;&lt;![CDATA[{{ c }}]]&gt;&lt;/Code&gt;
                      <br> &lt;/CLC&gt;
                    </template>
                  </template>
                  <br> &lt;/CLCs&gt;
                  <br> &lt;HasOriginalDoc&gt;true&lt;/HasOriginalDoc&gt;
                  <br> &lt;DataSource&gt;&lt;/DataSource&gt;
                  <br> &lt;Keywords&gt;
                  <template v-if="item.keyword_c">
                    <template v-for="(k,indexK) in item.keyword_c.split(';')">
                      <br> &lt;Keyword&gt;&lt;![CDATA[[{{ k.replace(/\[.*?\]/g, '') }}]]&gt;&lt;/Keyword&gt;
                    </template>
                  </template>
                  <br> &lt;/Keywords&gt;
                  <br> &lt;Abstracts&gt;
                  <br> &lt;Abstract&gt;
                  <br> &lt;Text&gt;&lt;![CDATA[{{ item.remark_c }}]]&gt;&lt;/Text&gt;
                  <br> &lt;/Abstract&gt;
                  <br> &lt;/Abstracts&gt;
                  <br> &lt;Periodical&gt;
                  <br> &lt;ID&gt;94688A&lt;/ID&gt;
                  <br> &lt;Name&gt;&lt;![CDATA[{{ item.media_c }}]]&gt;&lt;/Name&gt;
                  <br> &lt;ISSN&gt;{{ item.issn }}&lt;/ISSN&gt;
                  <br> &lt;/Periodical&gt;
                  <br> &lt;Column&gt;&lt;/Column&gt;
                  <template v-if="item.vol ">
                    <br> &lt;Volume&gt;&lt;![CDATA[{{ item.vol }}]]&gt;&lt;/Volum&gt;
                  </template>
                  <br> &lt;Issue&gt;&lt;![CDATA[{{ item.num }}]]&gt;&lt;/Issue&gt;
                  <br> &lt;Page&gt;{{ item.beginpage }}-{{ item.endpage }}&lt;/Page&gt;
                  <br> &lt;ServiceMode&gt;1&lt;/ServiceMode&gt;
                  <br> &lt;Organs&gt;
                  <template v-if="item.showorgan">
                    <template v-for="(o,indexO) in item.showorgan.split(';')">
                      <br> &lt;Organ&gt;&lt;![CDATA[{{ o.replace(/\[.*?\]/g, '') }}]]&gt;&lt;/Organ&gt;
                    </template>
                  </template>
                  <br> &lt;/Organs&gt;
                  <br> &lt;/PeriodicalPaper&gt;
                </template>
                <br>&lt;/ResourceList&gt;
              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 4}">
                <p v-for="(item,indexA) in articleList" :key="indexA">
                  {Reference Type}: Journal Article<br>
                  {Title}: {{ item.title_c }}<br>
                  <template v-if="item.showwriter">
                    <template v-for="(w,indexW) in item.showwriter.split(';')">
                      {Author}: {{ w.replace(/\[.*?\]/g, '') }}<br>
                    </template>
                  </template>
                  <template v-if="item.showorgan">
                    <template v-for="(o,indexO) in item.showorgan.split(';')">
                      {Author Address}: {{o.replace(/\[.*?\]/g, '')}}<br>
                    </template>
                  </template>
                  {Journal}: {{ item.media_c }}<br>
                  {Year}: {{ item.years }}<br>
                  <template v-if="item.vol">
                    {Volume}: {{ item.vol }}<br>
                  </template>
                  {Issue}: {{ item.num }}<br>
                  <template v-if="item.keyword_c">
                    <template v-for="(k,indexK) in item.keyword_c.split(';')">
                      {Keywords}: {{ k.replace(/\[.*?\]/g, '') }}<br>
                    </template>
                  </template>
                  {Abstract}: {{item.remark_c}}<br>
                  {URL}: {{  windowPath(item)}}<br>
                  {Database Provider}: 重庆维普资讯有限公司<br>
                </p>

              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 5}">
                <p v-for="(item,indexA) in articleList" :key="indexA">
                  RT Journal<br>
                  T1 {{ item.title_c }}<br>
                  JF {{ item.media_c }}<br>
                  YR {{ item.years }}<br>
                  VO {{ item.vol }}<br>
                  IS {{ item.num }}<br>
                  AB {{ item.remark_c }}<br>
                  LK {{ windowPath(item)}}<br>
                  A1 {{ item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '') : ''}}<br>
                  <template v-if="item.showorgan">
                    <template v-for="(o,indexO) in item.showorgan.split(';')">
                      AD {{o.replace(/\[.*?\]/g, '')}}<br>
                    </template>
                  </template>
                  CL {{ item.class}}<br>
                  K1 {{ item.keyword_c ? item.keyword_c.replace(/\[.*?\]/g, '') : ''}} <br>
                  PP 中国<br>
                  DS 重庆维普<br>
                  NO 作者个数:{{ item.showwriter ? item.showwriter.split(";").length : 0}}; 第一作者:{{ item.firstwriter }} <br>
                  <br>
                </p>

              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 6}">
                <p v-for="(item,indexA) in articleList" :key="indexA">
                  %0 Journal Article<br>
                  %A {{ item.showwriter }}<br>
                  %+: {{ item.showorgan ? item.showorgan.replace(/\[.*?\]/g, '') :''}}<br>
                  %T {{ item.title_c }}<br>
                  %J {{ item.media_c }}<br>
                  %D {{ item.years }}<br>
                  %N {{ item.num }}<br>
                  %V {{ item.vol }}<br>
                  %K {{ item.keyword_c ? item.keyword_c.replace(/\[.*?\]/g, '') : ''}} <br>
                  %X {{ item.remark_c }}<br>
                  %U {{ windowPath(item)}}<br>
                  %W 重庆维普资讯有限公司
                </p>

              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 7}">
                &lt;?xml version="1.0" encoding="utf-8"?&gt; <br>
                &lt;Bibliographies&gt; <br>
                &lt;BibliographiesCount&gt;{{ articleList.length }}&lt;/BibliographiesCount&gt; <br>
                <template v-for="(item,indexA) in articleList">
                  <br>
                  &lt;Bibliography&gt; <br>
                  &lt;Type&gt;JournalArticle&lt;/Type&gt; <br>
                  &lt;Language&gt;zh-CHS&lt;/Language&gt; <br>
                  &lt;PrimaryTitle&gt; &lt;Title Lang="zh-CHS"&gt;&lt;![CDATA[{{ item.title_c }}]]&gt;&lt;/Title&gt;&lt;/PrimaryTitle&gt;
                  <br>
                  &lt;Authors&gt;&lt;Author&gt;&lt;Info lang="zh-CHS"&gt; <br>
                  &lt;FullName&gt;&lt;![CDATA[{{ item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '') : '' }}]]&gt;&lt;/FullName&gt; <br>
                  &lt;Organization&gt;&lt;![CDATA[{{ item.showorgan }}]]&gt;&lt;/Organization&gt;
                  <br>
                  &lt;/Info&gt;&lt;/Author&gt;&lt;/Authors&gt; <br>
                  &lt;Medium&gt;&lt;Media Lang="zh-CHS"&gt;&lt;![CDATA[{{ item.media_c }}]]&gt;&lt;/Media&gt;&lt;/Medium&gt; <br>
                  &lt;Year&gt;&lt;![CDATA[{{ item.years }}]]&gt;&lt;/Year&gt; <br>
                  &lt;Volume&gt;&lt;![CDATA[{{ item.volumn }}]]&gt;&lt;/Volume&gt; <br>
                  &lt;Issue&gt;&lt;![CDATA[{{ item.num }}]]&gt;&lt;/Issue&gt; <br>
                  &lt;Keywords&gt; <br>
                  &lt;Keyword Lang="zh-CHS"&gt;&lt;![CDATA[[{{ item.keyword_c ? item.keyword_c.replace(/\[.*?\]/g, '') : ''}}]]&gt;&lt;/Keyword&gt;
                  <br>
                  &lt;Keyword Lang="en"&gt;&lt;![CDATA[ {{item.keyword_e ? item.keyword_e.replace(/\[.*?\]/g, '') : ''}} ]]&gt;&lt;/Keyword&gt; <br>
                  &lt;/Keywords&gt; <br>
                  &lt;Abstracts&gt;&lt;Abstract Lang="zh-CHS"&gt; <br>
                  &lt;![CDATA[{{ item.remark_c }}]]&gt;
                  <br>
                  &lt;/Abstract&gt; &lt;/Abstracts&gt; <br>
                  &lt;PageScope&gt;&lt;![CDATA[{{ item.beginpage }}-{{ item.endpage }}]]&gt;&lt;/PageScope&gt; <br>
                  &lt;Publisher&gt;&lt;![CDATA[{{item.publisher}}]]&gt;&lt;/Publisher&gt; <br>
                  &lt;Code&gt;&lt;![CDATA[{{ item.class }}]]&gt;&lt;/Code&gt; <br>
                  &lt;/Bibliography&gt; <br>
                </template>
                &lt;/Bibliographies&gt; <br>

              </div>
              <div class="layui-tab-item" :class="{'layui-show':typeIndex == 8}">
                <div class="export-filter" style="margin-top: -5em;">
                  <div class="label" >已选类型及字段</div>
                  <ul>
                    <el-checkbox-group v-model="checked">
                      <li v-for="(item,indexC) in checkBoxes">
                        <el-checkbox :label="item" border="" :key="indexC"></el-checkbox>
                      </li>
                    </el-checkbox-group>
                  </ul>

                </div>
                <div class="submit">
                  <div class="advance-submit">
                    <button @click="exportFile()" class="layui-btn"><i class="icon-export"></i>导出</button>
                  </div>
                </div>
              </div>
              <div class="layui-tab-item " id="outTable">
                <el-table :data = "articleList" border>
                  <el-table-column type="index" label="序号"></el-table-column>
                  <el-table-column prop="title_c" label="题名"></el-table-column>
                  <el-table-column prop="showwriter" label="作者"></el-table-column>
                  <el-table-column prop="showorgan" label="机构"></el-table-column>
                  <el-table-column prop="imburse" label="基金"></el-table-column>
                  <el-table-column prop="media_c" label="刊名"></el-table-column>
                  <el-table-column prop="years" label="年"></el-table-column>
                  <el-table-column prop="vol" label="卷"></el-table-column>
                  <el-table-column prop="num" label="期"></el-table-column>
                  <el-table-column prop="issn" label="ISSN号"></el-table-column>
                  <el-table-column prop="cn" label="CN号"></el-table-column>
                  <el-table-column label="页码">
                    <template slot-scope="scope">
                      {{ scope.row.beginpage }} - {{ scope.row.endpage }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="keyword_c" label="关键词"></el-table-column>
                  <el-table-column prop="class" label="分类号"></el-table-column>
                  <el-table-column prop="remark_c" label="文摘"></el-table-column>
                  <el-table-column label="网址">
                    <template slot-scope="scope">
                      {{ windowPath(scope.row) }}
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>
<style scoped src="@/assets/css/export.css"></style>
<script src="./export.js"></script>
