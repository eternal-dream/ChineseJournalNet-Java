<template>
    <div>
        <vip-header />
        <searcher />
        <div class="l-width">
            <div class="row web">
                <div class="web-intro">
                    <h1><span class="title">检索规则</span></h1>
                </div>
                <div class="web-content">
                <h2><span class="">基本检索</span></h2>
                <ul>
                    <li>检索框中输入的所有字符均被视为检索词，不支持任何逻辑运算；如果输入逻辑运算符，将被视为检索词或停用词进行处理；</li>
                    <li>有且仅当检索词中包含了运算符号的情况下，可作为特例，使用英文半角引号""将整个检索词组括起来，表示需要对包含符号的整个词组进行匹配命中，如："互联网+"。</li>
                </ul>
                <h2><span class="">高级检索</span></h2>        
                    <ul>
                        <li>检索框中可支持“并且”(AND)、“或者”(OR)、“不包含”(NOT)三种简单逻辑运算；</li>
                        <li>逻辑运算符AND、OR、NOT必须大写，且前后必须空一格；不支持使用()提高检索优先级；</li>
                        <li>精确检索请使用检索框后方的“精确”选项，不支持使用""进行精确检索；</li>
                        <li>有且仅当检索词中包含了运算符号的情况下，可作为特例，使用英文半角引号""将整个检索词组括起来，表示需要对包含符号的整个词组进行匹配命中，如："互联网+"。</li>
                    </ul>
                
                
                <h2><span class="">检索式检索</span></h2>
                <ul>
                <li><strong class="label">检索界面</strong> 您可以在检索框中使用布尔逻辑运算符对多个检索词进行组配检索。执行检索前，还可以选择时间、期刊来源、学科等检索条件对检索范围进行限定。每次调整检索策略并执行检索后，均会在检索区下方生成一个新的检索结果列表，方便您对多个检索策略的结果进行比对分析。</li>
                <li><strong class="label">检索条件限定</strong> 使用检索条件限定，可以进一步缩小检索范围，获得更符合需求的检索结果。您可以根据需要，选择合适的时间范围、学科范围、期刊范围等限制条件。</li>
                <li><strong class="label">检索规则</strong></li>
                <div >
                    <p><strong class="label">3.1 关于逻辑运算符</strong>见下表</p>
                    <div class="table-list">
                        <div class="table-header">
                            逻辑运算符对照表
                        </div>
                        <table>
                            <colgroup>
                            <col width="150">  
                            <col width="150"> 
                            <col width="150"> 
                            <col width="150">                                                      
                            </colgroup>
                            <thead>
                                <tr>
                            <th>逻辑关系</th>
                            <th width="">并且、与</th>
                            <th width="">或者</th>
                            <th width="">不包含、非</th>
                            </tr>
                            </thead>
                        <tbody>                    
                            <tr>
                            <td>运算符</td>
                            <td>AND/*	</td>
                            <td>OR/+	</td>
                            <td>NOT/-</td>
                            </tr>
                        </tbody>
                        </table>
                    </div>
                    <p>书写规则：逻辑运算符AND、OR、NOT必须大写，三种运算逻辑没有优先级，但可以使用英文半角括号()提高检索优先级；所有运算符号必须在英文半角状态下输入，且前后必须空一格；检索词中包含运算符号的，需使用英文半角引号""将整个检索词组括起来，作为一个整体检索，方可命中召回相关结果，如："互联网+"。</p>
                    <p>关于精确检索：将检索词组使用英文半角引号""括起来，即表示对此词组进行精确检索。</p>
                    <p><strong class="label">3.2 关于字段标识符</strong>见下表</p>
                    <div class="table-list">
                        <div class="table-header">
                            检索字段标识符对照表
                        </div>
                        <table>
                            <colgroup>
                            <col width="150">  
                            <col width="150"> 
                            <col width="150"> 
                            <col width="150">                                                      
                            </colgroup>
                            <thead>			
                                <tr>
                            <th>符号</th>
                            <th width="">字段</th>
                            <th width="">符号</th>
                            <th width="">字段</th>
                            </tr>
                            </thead>
                        <tbody>                    
                            <tr>			
                            <td>U</td>
                            <td>任意字段</td>
                            <td>S</td>
                            <td>机构</td>
                            </tr>
                            <tr>						
                            <td>M</td>
                            <td>题名或关键词</td>
                            <td>J</td>
                            <td>刊名</td>
                            </tr>
                            <tr>						
                            <td>K</td>
                            <td>关键词</td>
                            <td>F</td>
                            <td>第一作者</td>
                            </tr>
                            <tr>						
                            <td>A</td>
                            <td>作者</td>
                            <td>T</td>
                            <td>题名</td>
                            </tr>
                            <tr>			
                            <td>C</td>
                            <td>分类号</td>
                            <td>R</td>
                            <td>文摘</td>
                            </tr>
                        </tbody>
                        </table>
                    </div>
                    <p>书写规则：字段标识符必须为大写字母，且每个检索词前，都必须带有字段标识符。</p>
                </div>
                    <li><strong class="label">检索范例</strong></li>
                    <div>
                        <p>范例一：K=维普资讯 AND A=范兴丰</p>
                        <p>此检索式表示：查找关键词中含有“维普资讯”，并且作者为范兴丰的文献。</p>
                        <p>范例二：(K=CAD OR K=CAM OR T=雷达) AND R=机械 NOT K=模具</p>
                        <p>此检索式表示：查找文摘中含有机械，并且关键词中含有CAD或CAM、或者题名中含有“雷达”，但关键词不包含“模具”的文献。 </p>
                        <p>以上两个检索式也可以写作：</p>
                        <p>范例一：K=维普资讯 * A=范兴丰</p>
                        <p>范例二：(K=CAD + K=CAM + T=雷达) * R=机械 - K=模具</p>
                    </div>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>