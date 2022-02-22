<template>
    <div>
        <vip-header ref="header"/>
        <div class="l-width">
            <div class="layui-form advance">
        <div class="layui-tab">
            <ul class="layui-tab-title">
            <li :class="searchParam.searchByExpression? '':'layui-this'" @click="$set(searchParam,'searchByExpression',false)">高级检索</li>
            <li :class="searchParam.searchByExpression? 'layui-this':''" @click="$set(searchParam,'searchByExpression',true)">检索式检索</li>
            </ul>
            <div class="help">
                <router-link to="/search/help" target="_blank"><i class="icon-help"></i> 查看更多规则</router-link>
            </div>
            <div class="layui-tab-content">
            <el-dialog
              title="选择分类号"
              :visible.sync="classDialogOpen"
              width="40%"
              lock-scroll
              :before-close="handleClassDialog">
              <div>
                <el-tree
                  ref="tree"
                  :props="props"
                  :load="loadNode"
                  :data="classifyList"
                  @check="getChooseData"
                  lazy
                  show-checkbox>
                </el-tree>
              </div>
              <span slot="footer" class="dialog-footer">
                <el-button @click="classDialogOpen = false">取 消</el-button>
                <el-button type="primary" @click="submitClassify">确 定</el-button>
              </span>
            </el-dialog>
            <div class="layui-tab-item layui-show">
                <div class="tip" v-show="searchParam.searchByExpression">
                <h2>检索说明</h2>
                <p><span class="label">逻辑运算符</span> AND（逻辑“与”）、OR（逻辑“或”）、NOT（逻辑“非”）；</p>
                <p><span class="label">字段标识符</span> U=任意字段、M=题名或关键词、K=关键词、A=作者、C=分类号、S=机构、J=刊名、F=第一作者、T=题名、R=文摘；</p>
                <p><span class="label">范例</span> (K=CAD OR K=CAM OR T=雷达) AND R=机械 NOT K=模具 </p>

                </div>
                <div class="formula-input" v-show="searchParam.searchByExpression">
                <textarea v-model="searchParam.searchExpression" placeholder="请输入检索式" class="layui-textarea"></textarea>
                </div>
                <div class="advance-input" v-show="!searchParam.searchByExpression">
                <div class="input-group" v-for="(item,index) in searchParam.searchModels" :key="index">
                    <div class="inline-sel sel-l" v-if="index>0">
                        <el-select id="Select11" value="T" datau="rel1" v-model="item.logicOperator">
                            <el-option value="AND" label="与"></el-option>
                            <el-option value="OR" label="或"></el-option>
                            <el-option value="NOT" label="非"></el-option>
                        </el-select>
                    </div>
                    <div class="inline-sel sel-k">
                        <el-select id="Select11" v-model="item.searchField" >
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
                    <div class="inline-input">
                      <el-input type="text" autocomplete="off" placeholder="请输入标题" v-model="item.value" >
                        <el-button style="background-color: #0033a0;color: whitesmoke;" v-if="item.searchField=='C'" slot="append" @click="showClassifyDialog(index)"><i class="icon-plus"></i>查看分类表</el-button>
                      </el-input>
                    </div>
                    <div class="inline-sel sel-c">
                        <el-select id="Select11" value="T" datau="rel1" v-model="item.exact">
                            <el-option :value="false" label="模糊"></el-option>
                            <el-option :value="true" label="精确"></el-option>
                        </el-select>
                    </div>
                    <div class="inline-btn">
                        <div class="layui-btn-group" v-if="index>1">
                            <button class="layui-btn" @click="addSeachCondition"><i class="icon-plus"></i></button>
                            <button class="layui-btn" @click="removeSeachCondition(index)"><i class="icon-minus"></i></button>
                        </div>
                    </div>
                </div>

                <div class="input-group sel-extra">
                    <el-checkbox :label="true" v-model="searchParam.englishExtension">中英文扩展</el-checkbox>
                    <el-checkbox :label="true" v-model="searchParam.synonymExpansion">同义词扩展</el-checkbox>
                </div>
                </div>

                <div class="layui-collapse advance-filter">
                <div class="layui-colla-item">
                    <div class="layui-colla-title">
                    <h2>时间限定</h2>
                    </div>
                    <div class="layui-colla-content layui-show">
                        <div class="input-group">
                            <el-col :span="12">
                          <el-radio v-model="dateLimitType" :label="0">
                                年份：
                          </el-radio>
                                <el-date-picker
                                    v-model="searchParam.beginYear"
                                    type="year"
                                    value-format="yyyy"
                                    style="width:37%"
                                    placeholder="收录起始年">
                                </el-date-picker>
                                -
                                <el-date-picker
                                    v-model="searchParam.endYear"
                                    type="year"
                                    value-format="yyyy"
                                    style="width:37%"
                                    placeholder="选择年份">
                                </el-date-picker>
                            </el-col>
                              <el-col :span="12">
                          <el-radio v-model="dateLimitType" :label="1">
                            更新时间：
                          </el-radio>
                                  <el-select v-model="searchParam.timeLimitType" @change="changePubDate" >
                                      <el-option value="0" label="一个月内" />
                                      <el-option value="1" label="三个月内" />
                                      <el-option value="2" label="半年内" />
                                      <el-option value="3" label="一年内" />
                                      <el-option value="4" label="当年内" />
                                  </el-select>
                              </el-col>
                        </div>
                    </div>
                </div>
                <div class="layui-colla-item">
                    <div class="layui-colla-title">
                    <h2>期刊范围</h2>
                    </div>
                    <div class="layui-colla-content layui-show">
                        <el-checkbox-group v-model="searchParam.journalRange">
                            <el-checkbox label="核心刊">核心期刊</el-checkbox>
                            <el-checkbox label="ei">EI来源期刊</el-checkbox>
                            <el-checkbox label="sci">SCI来源期刊</el-checkbox>
                            <el-checkbox label="cas">CAS来源期刊</el-checkbox>
                            <el-checkbox label="cscd">CSCD来源期刊</el-checkbox>
                            <el-checkbox label="cssci">CSSCI来源期刊</el-checkbox>
                        </el-checkbox-group>
                    </div>
                </div>
                <div class="layui-colla-item">
                    <div class="layui-colla-title">
                    <h2>学科限定</h2>
                    </div>
                    <span class="sel-all">
                    <input type="checkbox" name="sel[all]" title="全选" checked>
                    </span>
                    <div class="layui-colla-content layui-show">
                    <ul class="cluster-list list-scroll" >
                        <li class="cluster-item" @click="showMore($event)" id="30">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'30')}" v-model="searchParam.subjects" label="30" title="医药卫生" ><span>医药卫生</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="441" title="公共卫生与预防医学" >公共卫生与预防医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="442" title="卫生毒理学" >卫生毒理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="443" title="环境卫生学" >环境卫生学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="444" title="劳动卫生" >劳动卫生</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="445" title="军事预防医学" >军事预防医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="446" title="营养与食品卫生学" >营养与食品卫生学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="447" title="妇幼卫生保健" >妇幼卫生保健</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="448" title="流行病学" >流行病学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="449" title="卫生事业管理" >卫生事业管理</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="450" title="卫生统计学" >卫生统计学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="451" title="中医学" >中医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="452" title="中医基础理论" >中医基础理论</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="453" title="中医临床基础" >中医临床基础</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="454" title="中医诊断学" >中医诊断学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="455" title="针灸推拿学" >针灸推拿学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="456" title="中医内科学" >中医内科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="457" title="中医外科学" >中医外科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="458" title="中医妇科学" >中医妇科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="459" title="中医儿科学" >中医儿科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="460" title="中医肿瘤科" >中医肿瘤科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="461" title="中医骨伤科学" >中医骨伤科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="462" title="中医皮科" >中医皮科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="463" title="中医五官科学" >中医五官科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="464" title="中药学" >中药学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="465" title="方剂学" >方剂学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="466" title="民族医学" >民族医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="467" title="中西医结合" >中西医结合</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="468" title="基础医学" >基础医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="469" title="生物医学工程" >生物医学工程</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="470" title="人体解剖和组织胚胎学" >人体解剖和组织胚胎学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="471" title="人体生理学" >人体生理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="472" title="病理学" >病理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="473" title="病原生物学" >病原生物学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="474" title="医学寄生虫学" >医学寄生虫学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="475" title="免疫学" >免疫学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="476" title="医学遗传学" >医学遗传学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="477" title="医学心理学" >医学心理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="478" title="法医学" >法医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="479" title="放射医学" >放射医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="480" title="航空、航天与航海医学" >航空、航天与航海医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="481" title="临床医学" >临床医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="482" title="诊断学" >诊断学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="483" title="影像医学与核医学" >影像医学与核医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="484" title="治疗学" >治疗学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="485" title="运动医学" >运动医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="486" title="护理学" >护理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="487" title="康复医学" >康复医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="488" title="急诊医学" >急诊医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="489" title="内科学" >内科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="490" title="心血管疾病" >心血管疾病</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="491" title="血液循环系统疾病" >血液循环系统疾病</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="492" title="呼吸系统" >呼吸系统</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="493" title="消化系统" >消化系统</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="494" title="内分泌" >内分泌</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="495" title="老年医学" >老年医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="496" title="外科学" >外科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="497" title="麻醉学" >麻醉学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="498" title="整形外科" >整形外科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="499" title="骨科学" >骨科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="500" title="泌尿科学" >泌尿科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="501" title="妇产科学" >妇产科学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="502" title="儿科" >儿科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="503" title="肿瘤" >肿瘤</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="504" title="神经病学与精神病学" >神经病学与精神病学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="505" title="皮肤病学与性病学" >皮肤病学与性病学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="506" title="耳鼻咽喉科" >耳鼻咽喉科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="507" title="眼科" >眼科</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="508" title="口腔医学" >口腔医学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="509" title="药学" >药学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="510" title="药物化学" >药物化学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="511" title="药物分析学" >药物分析学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="512" title="生药学" >生药学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="513" title="药剂学" >药剂学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="514" title="药理学" >药理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="515" title="药品" >药品</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="516" title="毒理学" >毒理学</el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('30') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="517" title="微生物与生化药学" >微生物与生化药学</el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="35">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'35')}" v-model="searchParam.subjects" label="35" title="农业科学" ><span>农业科学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="518" title="农业基础科学" ><span>农业基础科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="519" title="肥料学" ><span>肥料学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="520" title="土壤学" ><span>土壤学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="521" title="农业气象学" ><span>农业气象学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="522" title="农业工程" ><span>农业工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="523" title="农业机械化工程" ><span>农业机械化工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="524" title="农业电气化与自动化" ><span>农业电气化与自动化</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="525" title="农业水土工程" ><span>农业水土工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="526" title="农艺学" ><span>农艺学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="527" title="作物栽培与耕作技术" ><span>作物栽培与耕作技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="528" title="作物遗传育种" ><span>作物遗传育种</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="529" title="农产品加工" ><span>农产品加工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="530" title="植物保护" ><span>植物保护</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="531" title="植物病理学" ><span>植物病理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="532" title="农业昆虫与害虫防治" ><span>农业昆虫与害虫防治</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="533" title="农药学" ><span>农药学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="534" title="作物学" ><span>作物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="535" title="茶叶生产加工" ><span>茶叶生产加工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="536" title="中草药栽培" ><span>中草药栽培</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="537" title="烟草工业" ><span>烟草工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="538" title="园艺学" ><span>园艺学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="539" title="果树学" ><span>果树学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="540" title="蔬菜学" ><span>蔬菜学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="541" title="观赏园艺" ><span>观赏园艺</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="542" title="林学" ><span>林学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="543" title="林木遗传育种" ><span>林木遗传育种</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="544" title="森林经理学" ><span>森林经理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="545" title="森林保护学" ><span>森林保护学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="546" title="森林工程" ><span>森林工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="547" title="木材科学与技术" ><span>木材科学与技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="548" title="畜牧兽医" ><span>畜牧兽医</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="549" title="畜牧学" ><span>畜牧学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="550" title="饲料科学" ><span>饲料科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="551" title="草业科学" ><span>草业科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="552" title="兽医学" ><span>兽医学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="553" title="基础兽医学" ><span>基础兽医学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="554" title="预防兽医学" ><span>预防兽医学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="555" title="临床兽医学" ><span>临床兽医学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="556" title="野生动物驯养" ><span>野生动物驯养</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="557" title="特种经济动物饲养" ><span>特种经济动物饲养</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="558" title="水产科学" ><span>水产科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="559" title="渔业资源" ><span>渔业资源</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="560" title="水产养殖" ><span>水产养殖</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('35') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="561" title="捕捞与储运" ><span>捕捞与储运</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="257">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'257')}" v-model="searchParam.subjects" label="257" title="一般工业技术" ><span>一般工业技术</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="562" title="工程设计测绘" ><span>工程设计测绘</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="563" title="材料科学与工程" ><span>材料科学与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="564" title="工业设计" ><span>工业设计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="565" title="包装工程" ><span>包装工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="566" title="制冷工程" ><span>制冷工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="567" title="真空技术" ><span>真空技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="568" title="摄影技术" ><span>摄影技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('257') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="569" title="计量学" ><span>计量学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="13">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'13')}" v-model="searchParam.subjects" label="13" title="矿业工程" ><span>矿业工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="570" title="矿山地质测量" ><span>矿山地质测量</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="571" title="矿井建设" ><span>矿井建设</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="572" title="矿山机电" ><span>矿山机电</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="573" title="矿井通风与安全" ><span>矿井通风与安全</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="574" title="矿山开采" ><span>矿山开采</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="575" title="煤矿开采" ><span>煤矿开采</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="576" title="金属矿开采" ><span>金属矿开采</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="577" title="非金属矿开采" ><span>非金属矿开采</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('13') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="578" title="选矿" ><span>选矿</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="14">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'14')}" v-model="searchParam.subjects" label="14" title="石油与天然气工程" ><span>石油与天然气工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="137" title="油气勘探" ><span>油气勘探</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="138" title="油气井工程" ><span>油气井工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="139" title="油气田开发工程" ><span>油气田开发工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="140" title="油气加工工程" ><span>油气加工工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="579" title="油气储运工程" ><span>油气储运工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('14') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="580" title="石油机械设备" ><span>石油机械设备</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="15">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'15')}" v-model="searchParam.subjects" label="15" title="冶金工程" ><span>冶金工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('15') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="141" title="冶金物理化学" ><span>冶金物理化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('15') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="143" title="粉末冶金" ><span>粉末冶金</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('15') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="144" title="冶金机械及自动化" ><span>冶金机械及自动化</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('15') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="145" title="钢铁冶金" ><span>钢铁冶金</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('15') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="146" title="有色金属冶金" ><span>有色金属冶金</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="16">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'16')}" v-model="searchParam.subjects" label="16" title="金属学及工艺" ><span>金属学及工艺</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="147" title="金属学" ><span>金属学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="148" title="物理冶金" ><span>物理冶金</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="149" title="合金" ><span>合金</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="627" title="金属材料" ><span>金属材料</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="150" title="热处理" ><span>热处理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="151" title="金属表面处理" ><span>金属表面处理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="152" title="铸造" ><span>铸造</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="153" title="金属压力加工" ><span>金属压力加工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="154" title="焊接" ><span>焊接</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="155" title="金属切削加工及机床" ><span>金属切削加工及机床</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="156" title="刀具与模具" ><span>刀具与模具</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="157" title="公差测量技术" ><span>公差测量技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('16') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="158" title="钳工工艺" ><span>钳工工艺</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="17">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'17')}" v-model="searchParam.subjects" label="17" title="机械工程" ><span>机械工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="159" title="机械设计及理论" ><span>机械设计及理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="160" title="机械制造及自动化" ><span>机械制造及自动化</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="161" title="仪器科学与技术" ><span>仪器科学与技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="581" title="精密仪器及机械" ><span>精密仪器及机械</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="582" title="测试计量技术及仪器" ><span>测试计量技术及仪器</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="583" title="车辆工程" ><span>车辆工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('17') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="584" title="光学工程" ><span>光学工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="268">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'268')}" v-model="searchParam.subjects" label="268" title="兵器科学与技术" ><span>兵器科学与技术</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('268') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="585" title="兵器发射理论与技术" ><span>兵器发射理论与技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('268') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="586" title="武器系统与运用工程" ><span>武器系统与运用工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('268') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="587" title="火炮、自动武器与弹药工程" ><span>火炮、自动武器与弹药工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('268') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="588" title="军事化学与烟火技术" ><span>军事化学与烟火技术</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="18">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'18')}" v-model="searchParam.subjects" label="18" title="动力工程及工程热物理" ><span>动力工程及工程热物理</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('18') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="167" title="热能工程" ><span>热能工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('18') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="168" title="工程热物理" ><span>工程热物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('18') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="169" title="动力机械及工程" ><span>动力机械及工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('18') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="170" title="生物能" ><span>生物能</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('18') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="171" title="流体机械及工程" ><span>流体机械及工程</span></el-checkbox></el-col>
                            </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="29">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'29')}" v-model="searchParam.subjects" label="29" title="核科学技术" ><span>核科学技术</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('29') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="589" title="核能科学" ><span>核能科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('29') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="590" title="核燃料循环与材料" ><span>核燃料循环与材料</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('29') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="591" title="核技术及应用" ><span>核技术及应用</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('29') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="592" title="辐射防护及环境保护" ><span>辐射防护及环境保护</span></el-checkbox></el-col>
                            </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="20">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'20')}" v-model="searchParam.subjects" label="20" title="电气工程" ><span>电气工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="180" title="电工理论与新技术" ><span>电工理论与新技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="181" title="电机" ><span>电机</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="182" title="电力系统及自动化" ><span>电力系统及自动化</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="186" title="电器" ><span>电器</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="184" title="高电压与绝缘技术" ><span>高电压与绝缘技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('20') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="185" title="电力电子与电力传动" ><span>电力电子与电力传动</span></el-checkbox></el-col>
                            </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="19">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'19')}" v-model="searchParam.subjects" label="19" title="电子电信" ><span>电子电信</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="173" title="物理电子学" ><span>物理电子学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="174" title="微电子学与固体电子学" ><span>微电子学与固体电子学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="175" title="电路与系统" ><span>电路与系统</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="176" title="信息与通信工程" ><span>信息与通信工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="177" title="通信与信息系统" ><span>通信与信息系统</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('19') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="178" title="信号与信息处理" ><span>信号与信息处理</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="21">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'21')}" v-model="searchParam.subjects" label="21" title="自动化与计算机技术" ><span>自动化与计算机技术</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="593" title="控制科学与工程" ><span>控制科学与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="594" title="控制理论与控制工程" ><span>控制理论与控制工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="595" title="检测技术与自动化装置" ><span>检测技术与自动化装置</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="596" title="计算机科学与技术" ><span>计算机科学与技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="597" title="计算机系统结构" ><span>计算机系统结构</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="598" title="计算机软件与理论" ><span>计算机软件与理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('21') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="599" title="计算机应用技术" ><span>计算机应用技术</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="12">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'12')}" v-model="searchParam.subjects" label="12" title="化学工程" ><span>化学工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="123" title="无机化工" ><span>无机化工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="124" title="电化学工业" ><span>电化学工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="122" title="高温制品工业" ><span>高温制品工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="125" title="硅酸盐工业" ><span>硅酸盐工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="600" title="玻璃工业" ><span>玻璃工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="601" title="水泥工业" ><span>水泥工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="602" title="搪瓷工业" ><span>搪瓷工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="603" title="陶瓷工业" ><span>陶瓷工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="126" title="有机化工" ><span>有机化工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="127" title="高聚物工业" ><span>高聚物工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="604" title="合成树脂塑料工业" ><span>合成树脂塑料工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="605" title="橡胶工业" ><span>橡胶工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="606" title="化纤工业" ><span>化纤工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="128" title="化学肥料工业" ><span>化学肥料工业</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="129" title="农药化工" ><span>农药化工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="130" title="制药化工" ><span>制药化工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="131" title="煤化学工程" ><span>煤化学工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="132" title="炸药化工" ><span>炸药化工</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('12') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="133" title="精细化工" ><span>精细化工</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="25">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'25')}" v-model="searchParam.subjects" label="25" title="轻工技术与工程" ><span>轻工技术与工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="607" title="纺织科学与工程" ><span>纺织科学与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="608" title="纺织工程" ><span>纺织工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="609" title="纺织材料与纺织品设计" ><span>纺织材料与纺织品设计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="610" title="纺织化学与染整工程" ><span>纺织化学与染整工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="611" title="服装设计与工程" ><span>服装设计与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="612" title="食品科学与工程" ><span>食品科学与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="613" title="食品科学" ><span>食品科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="614" title="粮食、油脂及植物蛋白工程" ><span>粮食、油脂及植物蛋白工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="615" title="制糖工程" ><span>制糖工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="616" title="农产品加工及贮藏工程" ><span>农产品加工及贮藏工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="617" title="水产品加工及贮藏工程" ><span>水产品加工及贮藏工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="618" title="发酵工程" ><span>发酵工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="619" title="皮革化学与工程" ><span>皮革化学与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('25') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="620" title="制浆造纸工程" ><span>制浆造纸工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="22">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'22')}" v-model="searchParam.subjects" label="22" title="建筑科学" ><span>建筑科学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="195" title="建筑理论" ><span>建筑理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="196" title="建筑设计及理论" ><span>建筑设计及理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="197" title="建筑技术科学" ><span>建筑技术科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="198" title="土木工程" ><span>土木工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="199" title="岩土工程" ><span>岩土工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="622" title="结构工程" ><span>结构工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="623" title="城市规划与设计" ><span>城市规划与设计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="624" title="市政工程" ><span>市政工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="625" title="供热、供燃气、通风及空调工程" ><span>供热、供燃气、通风及空调工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('22') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="626" title="桥梁与隧道工程" ><span>桥梁与隧道工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="23">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'23')}" v-model="searchParam.subjects" label="23" title="水利工程" ><span>水利工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('23') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="204" title="水文学及水资源" ><span>水文学及水资源</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('23') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="205" title="水力学及河流动力学" ><span>水力学及河流动力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('23') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="206" title="水工结构工程" ><span>水工结构工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('23') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="207" title="水利水电工程" ><span>水利水电工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="26">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'26')}" v-model="searchParam.subjects" label="26" title="交通运输工程" ><span>交通运输工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="208" title="道路与铁道工程" ><span>道路与铁道工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="209" title="交通信息工程及控制" ><span>交通信息工程及控制</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="210" title="交通运输规划与管理" ><span>交通运输规划与管理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="211" title="载运工具运用工程" ><span>载运工具运用工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="212" title="船舶与海洋工程" ><span>船舶与海洋工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="213" title="船舶及航道工程" ><span>船舶及航道工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="214" title="轮机工程" ><span>轮机工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="215" title="水声工程" ><span>水声工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('26') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="621" title="港口、海岸及近海工程" ><span>港口、海岸及近海工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="27">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'27')}" v-model="searchParam.subjects" label="27" title="航空宇航科学技术" ><span>航空宇航科学技术</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('27') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="216" title="飞行器设计" ><span>飞行器设计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('27') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="217" title="航空宇航推进理论与工程" ><span>航空宇航推进理论与工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('27') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="218" title="航空宇航制造工程" ><span>航空宇航制造工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('27') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="219" title="人机与环境工程" ><span>人机与环境工程</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="28">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'28')}" v-model="searchParam.subjects" label="28" title="环境科学与工程" ><span>环境科学与工程</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('28') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="221" title="环境科学" ><span>环境科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('28') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="222" title="环境工程" ><span>环境工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('28') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="223" title="灾害防治" ><span>灾害防治</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('28') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="226" title="安全科学" ><span>安全科学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="266">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'266')}" v-model="searchParam.subjects" label="266" title="自然科学总论" ><span>自然科学总论</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('266') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="376" title="系统科学" ><span>系统科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('266') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="377" title="科学技术哲学" ><span>科学技术哲学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="267">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'267')}" v-model="searchParam.subjects" label="267" title="理学" ><span>理学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="378" title="数学" ><span>数学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="379" title="基础数学" ><span>基础数学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="380" title="计算数学" ><span>计算数学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="381" title="概率论与数理统计" ><span>概率论与数理统计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="382" title="应用数学" ><span>应用数学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="383" title="运筹学与控制论" ><span>运筹学与控制论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="384" title="力学" ><span>力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="385" title="一般力学与力学基础" ><span>一般力学与力学基础</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="386" title="固体力学" ><span>固体力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="387" title="流体力学" ><span>流体力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="388" title="工程力学" ><span>工程力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="389" title="物理" ><span>物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="390" title="理论物理" ><span>理论物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="391" title="声学" ><span>声学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="392" title="光学" ><span>光学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="393" title="电磁学" ><span>电磁学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="394" title="无线电物理" ><span>无线电物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="395" title="电子物理学" ><span>电子物理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="396" title="凝聚态物理" ><span>凝聚态物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="397" title="半导体物理" ><span>半导体物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="398" title="固体物理" ><span>固体物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="399" title="低温物理" ><span>低温物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="400" title="高压高温物理" ><span>高压高温物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="401" title="等离子体物理" ><span>等离子体物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="402" title="热学与物质分子运动论" ><span>热学与物质分子运动论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="403" title="原子与分子物理" ><span>原子与分子物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="404" title="粒子物理与原子核物理" ><span>粒子物理与原子核物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="405" title="应用物理" ><span>应用物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="406" title="化学" ><span>化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="407" title="无机化学" ><span>无机化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="408" title="有机化学" ><span>有机化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="409" title="高分子化学" ><span>高分子化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="410" title="物理化学" ><span>物理化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="411" title="分析化学" ><span>分析化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('267') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="412" title="晶体学" ><span>晶体学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="10">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'10')}" v-model="searchParam.subjects" label="10" title="天文地球" ><span>天文地球</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="115" title="天文学" ><span>天文学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="413" title="天体物理" ><span>天体物理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="414" title="天体测量" ><span>天体测量</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="415" title="天体力学" ><span>天体力学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="116" title="测绘科学与技术" ><span>测绘科学与技术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="416" title="大地测量学与测量工程" ><span>大地测量学与测量工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="417" title="摄影测量与遥感" ><span>摄影测量与遥感</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="418" title="地图制图学与地理信息工程" ><span>地图制图学与地理信息工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="117" title="地球物理学" ><span>地球物理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="419" title="固体地球物理学" ><span>固体地球物理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="420" title="地震学" ><span>地震学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="421" title="水文科学" ><span>水文科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="422" title="空间物理学" ><span>空间物理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="118" title="大气科学及气象学" ><span>大气科学及气象学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="423" title="大气物理学与大气环境" ><span>大气物理学与大气环境</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="424" title="地质学" ><span>地质学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="425" title="矿物学" ><span>矿物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="426" title="岩石学" ><span>岩石学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="427" title="矿床学" ><span>矿床学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="428" title="地质矿产勘探" ><span>地质矿产勘探</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="429" title="工程地质学" ><span>工程地质学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="430" title="地球化学" ><span>地球化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="431" title="古生物学与地层学" ><span>古生物学与地层学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="432" title="构造地质学" ><span>构造地质学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="433" title="第四纪地质学" ><span>第四纪地质学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="120" title="海洋科学" ><span>海洋科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="434" title="物理海洋学" ><span>物理海洋学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="435" title="海洋化学" ><span>海洋化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="436" title="海洋生物学" ><span>海洋生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="437" title="海洋地质" ><span>海洋地质</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('10') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="121" title="自然地理学" ><span>自然地理学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="9">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'9')}" v-model="searchParam.subjects" label="9" title="生物学" ><span>生物学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="101" title="普通生物学" ><span>普通生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="438" title="生态学" ><span>生态学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="439" title="水生生物学" ><span>水生生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="440" title="神经生物学" ><span>神经生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="102" title="细胞生物学" ><span>细胞生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="103" title="遗传学" ><span>遗传学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="104" title="生理学" ><span>生理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="105" title="生物化学" ><span>生物化学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="106" title="生物物理学" ><span>生物物理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="107" title="分子生物学" ><span>分子生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="112" title="生物工程" ><span>生物工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="113" title="古生物学" ><span>古生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="111" title="微生物学" ><span>微生物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="108" title="植物学" ><span>植物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="109" title="动物学" ><span>动物学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="110" title="昆虫学" ><span>昆虫学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('9') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="114" title="人类学" ><span>人类学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="261">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'261')}" v-model="searchParam.subjects" label="261" title="文化科学" ><span>文化科学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="329" title="传播学" ><span>传播学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="330" title="新闻学" ><span>新闻学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="331" title="图书馆学" ><span>图书馆学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="332" title="档案学" ><span>档案学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="333" title="情报学" ><span>情报学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="334" title="教育学" ><span>教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="335" title="教育技术学" ><span>教育技术学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="336" title="教育学原理" ><span>教育学原理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="337" title="课程与教学论" ><span>课程与教学论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="338" title="学前教育学" ><span>学前教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="339" title="高等教育学" ><span>高等教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="340" title="成人教育学" ><span>成人教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="341" title="职业技术教育学" ><span>职业技术教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="342" title="特殊教育学" ><span>特殊教育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="343" title="体育学" ><span>体育学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="344" title="运动人体科学" ><span>运动人体科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="345" title="体育训练" ><span>体育训练</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('261') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="346" title="民族体育" ><span>民族体育</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="2">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'2')}" v-model="searchParam.subjects" label="2" title="经济管理" ><span>经济管理</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="313" title="管理学" ><span>管理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="314" title="人力资源管理" ><span>人力资源管理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="315" title="政治经济学" ><span>政治经济学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="316" title="世界经济" ><span>世界经济</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="317" title="国民经济" ><span>国民经济</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="318" title="会计学" ><span>会计学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="319" title="企业管理" ><span>企业管理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="320" title="产业经济" ><span>产业经济</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="321" title="市场营销" ><span>市场营销</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="322" title="广告" ><span>广告</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="323" title="旅游管理" ><span>旅游管理</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="324" title="国际贸易" ><span>国际贸易</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="325" title="财政学" ><span>财政学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="326" title="金融学" ><span>金融学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="327" title="保险" ><span>保险</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('2') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="328" title="劳动经济" ><span>劳动经济</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="259">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'259')}" v-model="searchParam.subjects" label="259" title="政治法律" ><span>政治法律</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="286" title="政治学" ><span>政治学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="287" title="国际共产主义运动" ><span>国际共产主义运动</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="288" title="中共党史" ><span>中共党史</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="289" title="国际政治" ><span>国际政治</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="290" title="外交学" ><span>外交学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="291" title="国际关系" ><span>国际关系</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="292" title="中外政治制度" ><span>中外政治制度</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="293" title="法学" ><span>法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="294" title="法学理论" ><span>法学理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="295" title="宪法学与行政法学" ><span>宪法学与行政法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="296" title="刑法学" ><span>刑法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="297" title="民商法学" ><span>民商法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="298" title="诉讼法学" ><span>诉讼法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="299" title="经济法学" ><span>经济法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="300" title="环境与资源保护法学" ><span>环境与资源保护法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="301" title="国际法学" ><span>国际法学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('259') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="302" title="军事法学" ><span>军事法学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="8">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'8')}" v-model="searchParam.subjects" label="8" title="哲学宗教" ><span>哲学宗教</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="269" title="哲学理论" ><span>哲学理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="270" title="马克思主义哲学" ><span>马克思主义哲学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="271" title="世界哲学" ><span>世界哲学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="272" title="中国哲学" ><span>中国哲学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="273" title="外国哲学" ><span>外国哲学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="274" title="思维科学" ><span>思维科学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="275" title="逻辑学" ><span>逻辑学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="276" title="伦理学" ><span>伦理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="277" title="美学" ><span>美学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="278" title="宗教学" ><span>宗教学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="279" title="心理学" ><span>心理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="280" title="基础心理学" ><span>基础心理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="281" title="发展与教育心理学" ><span>发展与教育心理学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('8') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="281" title="应用心理学" ><span>应用心理学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="258">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'258')}" v-model="searchParam.subjects" label="258" title="社会学" ><span>社会学</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('258') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="283" title="统计学" ><span>统计学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('258') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="284" title="人口学" ><span>人口学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('258') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="285" title="民族学" ><span>民族学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="260">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'260')}" v-model="searchParam.subjects" label="260" title="军事" ><span>军事</span></el-checkbox></span>
                            <ul class="cluster-list">
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="303" title="军事理论" ><span>军事理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="304" title="战略学" ><span>战略学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="305" title="战役学" ><span>战役学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="306" title="战术学" ><span>战术学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="307" title="军队指挥学" ><span>军队指挥学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="308" title="军事通信学" ><span>军事通信学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="309" title="军事情报学" ><span>军事情报学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="310" title="军事装备学" ><span>军事装备学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="311" title="军事工程" ><span>军事工程</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('260') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="312" title="军事地形" ><span>军事地形</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="262">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'262')}" v-model="searchParam.subjects" label="262" title="语言文字" ><span>语言文字</span></el-checkbox></span>
                            <ul class="cluster-list" >
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="347" title="语言学" ><span>语言学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="348" title="汉语" ><span>汉语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="349" title="少数民族语言" ><span>少数民族语言</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="350" title="英语" ><span>英语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="351" title="法语" ><span>法语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="352" title="德语" ><span>德语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="353" title="西班牙语" ><span>西班牙语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="354" title="俄语" ><span>俄语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="355" title="日语" ><span>日语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="356" title="阿拉伯语" ><span>阿拉伯语</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('262') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="357" title="世界语" ><span>世界语</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="263">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'263')}" v-model="searchParam.subjects" label="263" title="文学" ><span>文学</span></el-checkbox></span>
                            <ul class="cluster-list">
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('263') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="358" title="文学理论" ><span>文学理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('263') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="359" title="世界文学" ><span>世界文学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('263') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="360" title="中国文学" ><span>中国文学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('263') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="361" title="其他各国文学" ><span>其他各国文学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="264">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'264')}" v-model="searchParam.subjects" label="264" title="艺术" ><span>艺术</span></el-checkbox></span>
                            <ul class="cluster-list">
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="362" title="艺术理论" ><span>艺术理论</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="363" title="美术" ><span>美术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="364" title="摄影艺术" ><span>摄影艺术</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="365" title="艺术设计" ><span>艺术设计</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="366" title="音乐" ><span>音乐</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="367" title="舞蹈" ><span>舞蹈</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="368" title="戏剧戏曲" ><span>戏剧戏曲</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('264') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="369" title="电影电视艺术" ><span>电影电视艺术</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                        <li class="cluster-item" @click="showMore($event)" id="265">
                            <span class="title"><i class="icon-arrow-right"></i><el-checkbox @change="(val)=>{checkedChange(val,'265')}" v-model="searchParam.subjects" label="265" title="历史地理" ><span>历史地理</span></el-checkbox></span>
                            <ul class="cluster-list">
                                <el-checkbox-group v-model="searchParam.subjects">
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="370" title="历史学" ><span>历史学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="371" title="中国史" ><span>中国史</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="372" title="世界史" ><span>世界史</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="373" title="考古学及博物馆学" ><span>考古学及博物馆学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="374" title="民俗学" ><span>民俗学</span></el-checkbox></el-col>
                                <el-col style="padding:3px" :span="6"><el-checkbox :disabled="searchParam.subjects.indexOf('265') != -1" @change="(val,$event)=>subjectCheck(val,$event)" label="375" title="人文地理学"><span>人文地理学</span></el-checkbox></el-col>
                                </el-checkbox-group>
                            </ul>
                        </li>
                    </ul>
                    </div>
                </div>
                </div>
                <div class="advance-submit" ref="searchButton">
                <button @click="search()" class="layui-btn"><i class="icon-search"></i>检索</button>
                <button @click="resetData" class="layui-btn layui-btn-primary">清空</button>
                </div>
            </div>
            </div>
        </div>
        </div>
            <div class="sel-tab">
            <ul>
                <li v-show="index>=searchHistory.length-5" v-for="(item,index) in searchHistory" :class="index == activeIndex ? 'tab-this':''">
                  <a class="sel-close" href="javascript:void(0)" @click="removeHistory(index)"><i class="icon-close"></i></a><a href="javascript:void(0)" @click="historySearch(item,index)" :title="item.displayValue || '空检索'">{{item.displayValue || '空检索'}}</a>
                </li>
            </ul>
            </div>
        </div>
        <search-result
          v-if="pageBean"
          @changeSearchModel="changeSearchModel"
          ref="searchResult"
          :facet="facet"
          :data="pageBean"
          :search="search"
          :getSubFacet="getSubFacet" />
    </div>
</template>
<script>
import axios from 'axios'
import SearchResult from '@/views/front/components/searchResult.vue'
import { scrollTo } from '@/utils/scroll-to'
export default {
  name: 'AdvanceSearch',
  components: {
    SearchResult
  },
  data () {
    return {
      currentInput: null,
      classifyVal: null,
      // 分类
      classifyList: null,
      props: {
        children: 'children',
        label: 'label'
      },
      loading: false,
      classDialogOpen: false,
      searchParam: {
        pageNum: 1,
        pageSize: 20,
        sortField: '',
        fq: '', // 用于二次聚类的条件过滤
        searchExpression: '',
        searchByExpression: false,
        englishExtension: false, // 中英文扩展
        synonymExtension: false, // 同义词扩展
        beginYear: undefined,
        endYear: this.$moment(new Date()).format('yyyy'),
        pubDate: new Date().getFullYear() + '-' + new Date().getMonth() + '-' + new Date().getDate(),
        journalRange: [],
        subjects: [], // 学科限定
        displayValue: '', // 检索式显示内容
        timeLimitType: '0', // 发布时间类型限定
        subjectNames: [], // 学科限定中文
        searchModels: [// 检索条件数组
          {
            searchField: 'M',
            value: '',
            exact: false, // 是否精确检索
            logicOperator: 'AND',
            displayValue: ''
          },
          {
            searchField: 'R',
            value: '',
            exact: false, // 是否精确检索
            logicOperator: 'AND',
            displayValue: ''
          },
          {
            searchField: 'A',
            value: '',
            exact: false, // 是否精确检索
            logicOperator: 'AND',
            displayValue: ''
          }
        ]
      },
      searchHistory: [], // 检索历史
      facet: [], // 检索聚类结果
      pageBean: undefined, // 检索结果
      activeIndex: 0,
      dateLimitType: 0 // 时间限定类型
    }
  },
  created () {
    window.addEventListener('keydown', this.handleKeyDown, true) // 开启监听键盘按下事件
  },
  destroyed () {
    window.removeEventListener('keydown', this.handleKeyDown, true)
  },
  methods: {
    submitClassify () {
      this.searchParam.searchModels[this.currentInput].value = this.classifyVal
      this.classDialogOpen = false
    },
    getSimpleCheckedNodes (store) {
      const checkedNodes = []
      const traverse = function (node) {
        const childNodes = node.root ? node.root.childNodes : node.childNodes
        // childNodes.forEach(child => {
        //   if (child.checked) {
        //     checkedNodes.push(child.data)
        //   }
        //   if (child.indeterminate) {
        //     traverse(child)
        //   }
        // })
        for (let i = 0; i < childNodes.length; i++) {
          const child = childNodes[i]
          if (child.checked && child.parent.childNodes.length == 1) {
            checkedNodes.splice(child.parent.data, 1)
            checkedNodes.push(child.data)
          } else if (child.checked) {
            checkedNodes.push(child.data)
          }
          if (child.indeterminate || (child.checked && child.childNodes.length == 1)) {
            traverse(child)
          }
        }
      }
      traverse(store)
      return checkedNodes
    },
    getChooseData () {
      const simpleCheckedNodes = this.getSimpleCheckedNodes(this.$refs.tree.store)

      const tempArr = []
      simpleCheckedNodes.forEach(item => {
        tempArr.push(item.strClass)
      })

      const classStrs = tempArr.join(',')
      this.classifyVal = classStrs
    },
    loadNode (node, resolve) {
      if (node.data) {
        this.$axiosRequest('view/classify/getSubClassifyDataByParentId', { parentId: node.data.id }, this.$axiosOptions('get')).then(res => {
          const data = res.data.map(item => {
            this.$set(item, 'label', '[' + item.strClass + ']' + item.strName)
            return item
          })
          resolve(data)
        })
      }
    },
    showClassifyDialog (index) {
      this.currentInput = index
      this.classifyVal = null
      this.classDialogOpen = true
      this.getSuperClassify()
    },
    getSuperClassify () {
      this.$axiosRequest('view/classify/getSuperClassifyData', null, this.$axiosOptions('get')).then(res => {
        this.classifyList = res.data.map(item => {
          this.$set(item, 'label', '[' + item.strClass + ']' + item.strName)
          return item
        })
      })
    },
    search (param) {
      this.facet = []
      this.defineDisplayValue()
      const searchParam = JSON.parse(JSON.stringify(this.searchParam))
      if (param) {
        searchParam.pageNum = param.pageNum
        searchParam.pageSize = param.pageSize
        searchParam.sortField = param.sortField
        searchParam.searchModels = searchParam.searchModels.concat(param.searchModels)
      }
      if (this.dateLimitType === 0) {
        searchParam.timeLimitType = ''
        searchParam.pubDate = undefined
      } else if (this.dateLimitType === 1) {
        searchParam.beginYear = undefined
        searchParam.endYear = undefined
      }

      let exists = false
      this.searchHistory.forEach(item => {
        const history = JSON.stringify(item)
        if (history == JSON.stringify(searchParam)) {
          exists = true
        }
      })
      if (!exists) {
        this.searchHistory.push(searchParam)
        this.activeIndex = this.searchHistory.length - 1
      } else {
        this.searchHistory.forEach((item, i) => {
          if (JSON.stringify(item) == JSON.stringify(searchParam)) {
            this.activeIndex = i
          }
        })
      }

      const loading = this.$loading({
        lock: true,
        text: '加载中...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.5)'
      })
      axios({
        method: 'post',
        url: 'view/search/advanceSearch',
        data: searchParam
      }).then(res => {
        if (!res.data.success) {
          this.$message.error(res.data.msg)
          return
        }
        loading.close()
        this.pageBean = res.data.pageBean
        scrollTo(750, 700)
        // this.$refs.searchButton.$el.scrollIntoView(true)
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
        this.facet = res.data.facet
      })
    },
    showMore (event) {
      var el = event.currentTarget
      if (el.className !== 'cluster-item show-more') {
        el.className = 'cluster-item show-more'
      } else {
        el.className = 'cluster-item'
      }
    },
    historySearch (value, index) {
      this.activeIndex = index
      this.searchParam = JSON.parse(JSON.stringify(value))
      this.search()
    },
    removeHistory (index) {
      this.searchHistory.splice(index, 1)
      if (index < this.activeIndex) {
        this.activeIndex--
      }
    },
    resetData () {
      Object.assign(this.searchParam, this.$options.data().searchParam)
    },
    getSubFacet (param, fq, url) {
      const searchParam = JSON.parse(JSON.stringify(this.searchParam))
      if (param) {
        searchParam.pageNum = param.pageNum
        searchParam.pageSize = param.pageSize
        searchParam.searchModels = searchParam.searchModels.concat(param.searchModels)
      }
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
    addSeachCondition () {
      if (this.searchParam.searchModels.length === 5) {
        return
      }
      this.searchParam.searchModels.push({
        searchField: 'U',
        value: '',
        exact: false, // 是否精确检索
        logicOperator: 'AND',
        displayValue: ''
      })
    },
    checkedChange (val, id) {
      const dom = document.getElementById(id)
      const checkboxes = dom.querySelectorAll('.el-checkbox__original')
      //   if (this.searchParam.subjects.indexOf(id) === -1 && val) {
      //     this.searchParam.subjects.push(id)
      //   } else if (this.searchParam.subjects.indexOf(id) !== -1 && !val) {
      //     this.searchParam.subjects.splice(this.searchParam.subjects.indexOf(id), 1)
      //   }
      checkboxes.forEach((box, index) => {
        const title = box.parentNode.parentElement.title
        if (this.searchParam.subjects.indexOf(box.value) === -1 && val) { // val为true 且subjects中没有该value
          this.searchParam.subjects.push(box.value)
        } else if (this.searchParam.subjects.indexOf(box.value) !== -1 && !val) { // val为false 且subjects中有该value
          this.searchParam.subjects.splice(this.searchParam.subjects.indexOf(box.value), 1)
        } else if (val && this.searchParam.subjectNames.indexOf(title) !== -1) {
          this.searchParam.subjectNames.splice(this.searchParam.subjectNames.indexOf(title), 1)
        }
        if (val && index !== 0) {
          box.disabled = true
        } else if (!val && index !== 0) {
          box.disabled = false
        }
      })
      if (val) {
        this.searchParam.subjectNames.push(checkboxes[0].parentNode.parentElement.title)
      } else if (!val) {
        this.searchParam.subjectNames.splice(this.searchParam.subjectNames.indexOf(checkboxes[0].parentNode.parentElement.title), 1)
      }
    },
    removeSeachCondition (index) {
      if (this.searchParam.searchModels.length === 3) {
        return
      }
      this.searchParam.searchModels.splice(index, 1)
    },
    changePubDate () {
      const date = new Date()
      switch (this.searchParam.timeLimitType) {
        case '0':// 一个月内
          date.setMonth(date.getMonth() - 1)
          break
        case '1':// 三个月内
          date.setMonth(date.getMonth() - 3)
          break
        case '2':// 半年内
          date.setMonth(date.getMonth() - 6)
          break
        case '3':// 一年内
          date.setFullYear(date.getFullYear() - 1)
          break
        case '4':// 当年内
          date.setMonth(0)
          date.setDate(1)
          break
      }
      this.searchParam.pubDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()
    },
    subjectCheck (val, event) {
      var el = event.currentTarget
      const node = document.querySelector('input[value="' + el.value + '"]')
      const title = node.parentNode.parentElement.title
      if (val && this.searchParam.subjectNames.indexOf(title) === -1) {
        this.searchParam.subjectNames.push(title)
      } else if (!val && this.searchParam.subjectNames.indexOf(title) !== -1) {
        this.searchParam.subjectNames.splice(this.searchParam.subjectNames.indexOf(title), 1)
      }
    },
    // 组装检索中文
    defineDisplayValue () {
      let displayValue = ''
      if (this.searchParam.searchByExpression) {
        displayValue = this.searchParam.searchExpression
      } else {
        this.searchParam.searchModels.forEach(item => {
          if (item.value) {
            let label = ''
            this.$enums.value.searchField.forEach(f => {
              if (f.value === item.searchField) {
                label = f.label
              }
            })
            if (!displayValue) {
              displayValue += label + '=' + item.value
            } else {
              displayValue += ' ' + item.logicOperator + ' ' + label + '=' + item.value
            }
          }
        })
      }
      if (!this.dateLimitType) {
        if (this.searchParam.beginYear && this.searchParam.endYear) {
          displayValue += ' AND 年份:' + this.searchParam.beginYear + '-' + this.searchParam.endYear
        } else if (this.searchParam.beginYear) {
          displayValue += ' AND 年份:' + this.searchParam.beginYear + '-不限'
        } else if (this.searchParam.endYear) {
          displayValue += ' AND 年份:' + '收录起始年-' + this.searchParam.endYear
        }
      } else if (this.dateLimitType === 1) {
        switch (this.searchParam.timeLimitType) {
          case '0':// 一个月内
            displayValue += ' AND 更新时间: 一个月内'
            break
          case '1':// 三个月内
            displayValue += ' AND 更新时间: 三个月内'
            break
          case '2':// 半年内
            displayValue += ' AND 更新时间: 半年内'
            break
          case '3':// 一年内
            displayValue += ' AND 更新时间: 一年内'
            break
          case '4':// 当年内
            displayValue += ' AND 更新时间: 当年内'
            break
        }
      }
      switch (this.searchParam.journalRange && this.searchParam.journalRange.length > 0) {
        case '核心刊':
          displayValue += ' AND 期刊范围: 核心期刊'
          break
        default:
          const range = this.searchParam.journalRange.map(e => {
            return e.toLocaleUpperCase() + '来源期刊'
          })
          if (range && range.length > 0) {
            displayValue += ' AND 期刊范围: ' + range
          }
          break
      }

      if (this.searchParam.subjectNames && this.searchParam.subjectNames.length > 0) {
        displayValue += ' AND 学科限定: ' + this.searchParam.subjectNames
      }
      if (displayValue.indexOf(' AND') == 0) {
        displayValue = displayValue.replace(' AND ', '')
      } else if (displayValue.indexOf(' OR') == 0) {
        displayValue = displayValue.replace(' OR ', '')
      }
      console.log(displayValue)
      this.searchParam.displayValue = displayValue
    },
    handleClassDialog () {
      this.classDialogOpen = false
    },
    // 简单检索条件更改调用的回调函数
    changeSearchModel (searchModels) {
      this.$router.push({ path: '/search/index', query: { searchModels: JSON.stringify(searchModels) } })
      // this.$refs.searcher.searchParam.searchModels = searchModels
      // this.search()
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
        this.search()
      }
    }

  },
  mounted () {
  }
}
</script>
<style scoped>
  >>>.el-dialog {
    height: 71%;
  }
  >>>.el-dialog__body {
    overflow-y: auto;
    height: 70%;
  }
</style>
<style scoped src="@/assets/css/advance.css"></style>
