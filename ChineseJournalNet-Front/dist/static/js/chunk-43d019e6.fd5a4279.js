(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-43d019e6","chunk-0953b70d"],{1:function(e,t){},2:function(e,t){},3863:function(e,t,a){},"4ce6":function(e,t,a){"use strict";var l=a("3863"),s=a.n(l);s.a},"5ae0":function(e,t,a){},"5b9b":function(e,t,a){"use strict";var l=a("5ae0"),s=a.n(l);s.a},"61f7":function(e,t,a){"use strict";a.d(t,"c",(function(){return s})),a.d(t,"b",(function(){return r})),a.d(t,"a",(function(){return n})),a.d(t,"d",(function(){return o}));a("d3b7"),a("25f0");var l=!0;function s(e){var t=/^.*(?=.{6,})(?=.*\d)(?=.*[a-zA-Z]).*$/;return t.test(e)}function r(e){if(l)return!0;var t=/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*$/;return t.test(e)}function n(e){var t=/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;return t.test(e)}function o(e){var t=/^1(3|4|5|6|7|8|9)\d{9}$/;return t.test(e)}},"9edd":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("d2-container",{attrs:{type:"full"}},[a("template",{slot:"header"},[a("h4",{staticStyle:{height:"15px"}},[e._v("用户搜索 "),a("el-switch",{staticStyle:{display:"inline","margin-left":"50px"},attrs:{"active-color":"#13ce66","inactive-color":"rgb(236, 165, 71)","active-text":"IP过滤","inactive-text":"条件过滤"},model:{value:e.queryParams.filterByIP,callback:function(t){e.$set(e.queryParams,"filterByIP",t)},expression:"queryParams.filterByIP"}})],1),a("el-divider"),a("el-form",{ref:"form",attrs:{model:e.queryParams,size:"small",inline:!0}},[e.queryParams.filterByIP?a("div",[a("el-col",{attrs:{span:7}},[a("el-form-item",{attrs:{label:"IP段",prop:"ip","label-width":"100px"}},[a("el-input",{model:{value:e.queryParams.ip,callback:function(t){e.$set(e.queryParams,"ip",t)},expression:"queryParams.ip"}})],1)],1),a("el-col",{attrs:{span:7}},[a("el-form-item",{attrs:{label:"IP绑定单位",prop:"ipUnit","label-width":"100px"}},[a("el-input",{model:{value:e.queryParams.ipUnit,callback:function(t){e.$set(e.queryParams,"ipUnit",t)},expression:"queryParams.ipUnit"}})],1)],1),a("el-col",{attrs:{span:7}},[a("el-form-item",{attrs:{label:"IP绑定备注",prop:"ipRemark","label-width":"100px"}},[a("el-input",{model:{value:e.queryParams.ipRemark,callback:function(t){e.$set(e.queryParams,"ipRemark",t)},expression:"queryParams.ipRemark"}})],1)],1)],1):a("div",[a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"用户名",prop:"userName","label-width":"80px"}},[a("el-input",{attrs:{placeholder:"请输入用户名"},model:{value:e.queryParams.userName,callback:function(t){e.$set(e.queryParams,"userName",t)},expression:"queryParams.userName"}})],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"单位",prop:"unit","label-width":"80px"}},[a("el-input",{attrs:{placeholder:"请输入单位名称"},model:{value:e.queryParams.unit,callback:function(t){e.$set(e.queryParams,"unit",t)},expression:"queryParams.unit"}})],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"联系人",prop:"linkMan","label-width":"80px"}},[a("el-input",{attrs:{placeholder:"请输入联系人"},model:{value:e.queryParams.linkMan,callback:function(t){e.$set(e.queryParams,"linkMan",t)},expression:"queryParams.linkMan"}})],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"邮箱",prop:"email","label-width":"80px"}},[a("el-input",{attrs:{placeholder:"请输入电子邮箱"},model:{value:e.queryParams.email,callback:function(t){e.$set(e.queryParams,"email",t)},expression:"queryParams.email"}})],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"用户类型",prop:"userType","label-width":"80px"}},[a("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择",clearable:!0},model:{value:e.queryParams.userType,callback:function(t){e.$set(e.queryParams,"userType",t)},expression:"queryParams.userType"}},e._l(e.$enums.value.userType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"账户状态",prop:"status","label-width":"80px"}},[a("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择",clearable:!0},model:{value:e.queryParams.status,callback:function(t){e.$set(e.queryParams,"status",t)},expression:"queryParams.status"}},[a("el-option",{attrs:{value:!0,label:"有效"}}),a("el-option",{attrs:{value:!1,label:"无效"}})],1)],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"用户组",prop:"userGroupId","label-width":"80px"}},[a("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择",clearable:!0},model:{value:e.queryParams.userGroupId,callback:function(t){e.$set(e.queryParams,"userGroupId",t)},expression:"queryParams.userGroupId"}},e._l(e.allGroups,(function(e){return a("el-option",{key:e.id,attrs:{value:e.id,label:e.name}})})),1)],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"费用方式",prop:"paymentType","label-width":"80px"}},[a("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择",clearable:!0},model:{value:e.queryParams.paymentType,callback:function(t){e.$set(e.queryParams,"paymentType",t)},expression:"queryParams.paymentType"}},e._l(e.$enums.value.paymentType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),a("el-col",{attrs:{span:6}},[a("el-form-item",{attrs:{label:"备注信息",prop:"remark","label-width":"80px"}},[a("el-input",{attrs:{placeholder:"请输入备注信息"},model:{value:e.queryParams.remark,callback:function(t){e.$set(e.queryParams,"remark",t)},expression:"queryParams.remark"}})],1)],1)],1),a("el-col",{attrs:{span:20}},[a("el-button",{attrs:{type:"success",size:"small"},on:{click:function(t){return e.handleEdit({})}}},[a("d2-icon",{attrs:{name:"plus"}}),e._v("新增")],1),a("el-button",{attrs:{type:"primary",icon:"el-icon-download",size:"small"},on:{click:e.downloadTemplate}},[e._v("模板下载")]),a("el-button",{attrs:{type:"primary",icon:"el-icon-upload",size:"small"},on:{click:function(t){e.fileUploaderVisible=!0}}},[e._v("模板导入")]),a("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"small",disabled:0==e.multipleSelection.length},on:{click:e.deleteAllSelection}},[e._v("删除")]),a("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"small"},on:{click:e.handleQuery}},[e._v("搜索")])],1),a("el-col",{attrs:{span:4}},[a("el-button",{attrs:{type:"primary",icon:"el-icon-tickets",size:"small"},on:{click:e.handleExport}},[e._v("导出EXCEL")])],1)],1)],1),a("template",{slot:"default"},[a("el-table",{attrs:{data:e.userInfoList,size:"mini","empty-text":"未查询到用户信息",stripe:""},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"55",align:"center",fixed:""}}),a("el-table-column",{attrs:{type:"index",width:"50",label:"序号",align:"center"}}),a("el-table-column",{attrs:{prop:"userName",label:"用户名","min-width":"100px","show-overflow-tooltip":!0,align:"center"}}),a("el-table-column",{attrs:{prop:"unit",label:"单位名","min-width":"100px","show-overflow-tooltip":!0,align:"center"}}),a("el-table-column",{attrs:{prop:"linkMan",label:"联系人","min-width":"100px","show-overflow-tooltip":!0,align:"center"}}),a("el-table-column",{attrs:{prop:"userGroupName",label:"用户组","min-width":"140px","show-overflow-tooltip":!0,align:"center"}}),a("el-table-column",{attrs:{prop:"userType",label:"用户类型","min-width":"100px","show-overflow-tooltip":!0,align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.$enums.value.userType,(function(l){return a("span",{directives:[{name:"show",rawName:"v-show",value:t.row.userType==l.value,expression:"scope.row.userType == item.value"}],key:l.value},[e._v(e._s(l.label))])}))}}])}),a("el-table-column",{attrs:{prop:"status",label:"用户状态","min-width":"100px","show-overflow-tooltip":!0,align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.status?a("el-tag",{attrs:{type:"success"}},[e._v("有效")]):a("el-tag",{attrs:{type:"info"}},[e._v("无效")])]}}])}),a("el-table-column",{attrs:{prop:"paymentType",label:"用户类型","min-width":"100px","show-overflow-tooltip":!0,align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.$enums.value.paymentType,(function(l){return a("span",{directives:[{name:"show",rawName:"v-show",value:t.row.paymentType==l.value,expression:"scope.row.paymentType == item.value"}],key:l.value},[e._v(e._s(l.label))])}))}}])}),a("el-table-column",{attrs:{prop:"remark",label:"备注","min-width":"100px","show-overflow-tooltip":!0,align:"center"}}),a("el-table-column",{attrs:{label:"操作",width:"310px",align:"center",fixed:"right"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{staticStyle:{padding:"5px"},attrs:{type:"primary",icon:"el-icon-edit",size:"small"},on:{click:function(a){return e.handleEdit(t.row)}}},[e._v("编辑")]),a("el-button",{staticClass:"red",staticStyle:{padding:"5px"},attrs:{type:"danger",icon:"el-icon-delete",size:"small"},on:{click:function(a){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),a("div",{staticClass:"pagination"},[a("el-pagination",{attrs:{background:"",layout:"total, prev, pager, next","current-page":e.queryParams.pageNum,"page-size":e.queryParams.pageSize,total:e.total},on:{"current-change":e.handlePageChange}})],1)],1),a("user-info-form",{ref:"userInfoForm",attrs:{allGroups:e.allGroups,totalRoles:e.roles},on:{callback:e.searchUserInfo}}),a("el-dialog",{attrs:{title:"文件上传",visible:e.fileUploaderVisible,width:"20%","close-on-click-modal":!1},on:{"update:visible":function(t){e.fileUploaderVisible=t}}},[a("el-upload",{attrs:{drag:"",accept:".xls","show-file-list":!1,"on-change":e.fileChange,action:"#","http-request":e.importUser}},[a("i",{staticClass:"el-icon-upload"}),a("div",{staticClass:"el-upload__text"},[e._v("将文件拖到此处，或"),a("em",[e._v("点击上传")])])])],1)],2)},s=[],r=(a("4160"),a("159b"),a("cd49")),n=a("bc3a"),o=a.n(n),i=a("d3a4"),u=a("ca3f"),c={name:"UserInfo",components:{userInfoForm:i["default"]},data:function(){return{queryParams:{pageNum:1,pageSize:10,filterByIP:!1},commonEnums:r["commonEnums"],fileUploaderVisible:!1,multipleSelection:[],ids:[],userInfoList:[],total:0,allGroups:[],file:void 0,roles:[]}},created:function(){this.searchUserInfo(),this.getAllGroups()},methods:{searchUserInfo:function(){var e=this;this.$axiosRequest("/system/user/getUserInfoPage",this.queryParams,{method:"get"}).then((function(t){e.userInfoList=t.userInfoPage.records,e.total=t.userInfoPage.total}))},handleQuery:function(){this.queryParams.pageNum=1,this.searchUserInfo()},handleEdit:function(e){this.$refs.userInfoForm.show(e)},handlePageChange:function(e){this.queryParams.pageNum=e,this.searchUserInfo()},handleSelectionChange:function(e){this.multipleSelection=e},deleteAllSelection:function(){var e=this;this.ids=[],this.multipleSelection.length<=0?this.$message.error("请选择需要删除的用户！"):(this.multipleSelection.forEach((function(t){e.ids.push(t.id)})),this.deleteUserInfo())},handleDelete:function(e){this.ids=[e.id],this.deleteUserInfo()},getAllGroups:function(){var e=this;this.$axiosRequest("/system/userGroup/getAllGroups",{},this.$axiosOptions("get")).then((function(t){e.allGroups=t.allGroups}))},fileChange:function(e){this.file=e.raw},deleteUserInfo:function(){var e=this;0!==this.ids.length?this.$confirm("确定删除选中的用户吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){o()({method:"post",url:"/system/user/deleteUserInfo",data:e.ids}).then((function(t){t.data.success?(e.$message.success("删除成功!"),e.searchUserInfo()):e.$message.error(t.data.msg)}))})):this.$message.error("请选择要删除的用户!")},handleExport:function(){this.$axiosRequest("/system/user/export",this.queryParams,this.$axiosOptions("post",null,"blob")).then((function(e){Object(u["a"])(e)}))},downloadTemplate:function(){this.$axiosRequest("/system/user/downloadTemplate",{},this.$axiosOptions("get",null,"blob")).then((function(e){Object(u["a"])(e)}))},importUser:function(){var e=this;this.fileUploaderVisible=!1;var t=new FormData;t.append("file",this.file),o()({method:"post",url:"/system/user/import",headers:{"Content-Type":"multipart/form-data"},data:t}).then((function(t){e.searchUserInfo(),t.data.success?e.$message.success("用户导入成功!"):e.$message.error(t.data.msg)}))}}},p=c,d=(a("4ce6"),a("2877")),f=Object(d["a"])(p,l,s,!1,null,"2b8300dd",null);t["default"]=f.exports},ca3f:function(e,t,a){"use strict";a.d(t,"a",(function(){return l})),a.d(t,"c",(function(){return s})),a.d(t,"b",(function(){return r}));a("d3b7"),a("4d63"),a("ac1f"),a("25f0"),a("3ca3"),a("5319"),a("1276"),a("ddb0"),a("2b3d");function l(e){var t=e.headers["content-disposition"],l=t.split("filename=")[1],s=new Blob([e.data]),r=a("acf9");if(r.skipDecodeWarning=!0,l=r.decode(l,"utf-8"),"msSaveOrOpenBlob"in navigator)window.navigator.msSaveOrOpenBlob(s,decodeURI(l));else{var n=window.URL||window.webkitURL,o=n.createObjectURL(s),i=document.createElement("a");i.href=o,i.download=decodeURI(l),i.click(),n.revokeObjectURL(o)}}function s(e){var t=this;return new Promise((function(a,l){t.$axiosRequest("/view/detail/readLimitCheck",{lngid:e},t.$axiosOptions("get")).then((function(l){if(console.log("res",l),l.data.limited)t.$confirm(l.data.msg,"提示",{showCancelButton:!1,type:"warning"}),a(l);else{e=encodeURIComponent(e);var s="/api/view/detail/readPdf?lngid="+e,r=window.location.origin;window.open(r+"/pdfjs/web/viewer.html?file="+encodeURIComponent(s))}}))}))}function r(e){var t=this,a=this.$loading({lock:!0,text:"下载中...",backgroundColor:"rgba(55,55,55,0.4)",spinner:"el-icon-loading",target:document.querySelector(".el-table__body")});return new Promise((function(s,r){t.$axiosRequest("/view/detail/downloadLimitCheck",{lngid:e},t.$axiosOptions("get")).then((function(n){console.log("res",n),n.data.limited?(t.$confirm(n.data.msg,"提示",{showCancelButton:!1,type:"warning"}),a.close(),s(n)):t.$axiosRequest("/view/detail/downloadPdf",{lngid:e},t.$axiosOptions("get",null,"blob")).then((function(e){if(!e.data.size>0)return t.$confirm("文件不存在,下载失败!","提示",{showCancelButton:!1,type:"warning"}),void s(e);l(e),s(e)})).catch((function(e){r(e)})).finally((function(e){a.close()}))}))}))}},d3a4:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{title:e.title,visible:e.visible,width:"70%","before-close":e.close,"close-on-click-modal":!1,inline:!0},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"userInfoForm",attrs:{model:e.userInfo,rules:e.rules,size:"small"}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("账号基本信息>>")])]),a("el-col",{attrs:{span:9}},[a("el-form-item",{attrs:{label:"用户名",prop:"userName","label-width":"150px"}},[a("el-input",{model:{value:e.userInfo.userName,callback:function(t){e.$set(e.userInfo,"userName",t)},expression:"userInfo.userName"}})],1)],1),a("el-col",{attrs:{span:3}},[a("el-link",{attrs:{type:"primary",underline:!1},on:{click:e.checkUserName}},[e._v("验证账号有效性")])],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"密码",prop:"password","label-width":"150px"}},[a("el-input",{attrs:{type:"password",placeholder:"新增用户"===e.title?"密码最少8位，必须包含大写字母、小写字母和数字":"不填表示不修改"},model:{value:e.userInfo.password,callback:function(t){e.$set(e.userInfo,"password",t)},expression:"userInfo.password"}})],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"登录验证方式",prop:"verificationType","label-width":"150px",rules:{required:!0,message:"验证方式不能为空!",trigger:"blur"}}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.verificationType,callback:function(t){e.$set(e.userInfo,"verificationType",t)},expression:"userInfo.verificationType"}},e._l(e.$enums.value.verificationType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"账户状态",prop:"status","label-width":"150px"}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.status,callback:function(t){e.$set(e.userInfo,"status",t)},expression:"userInfo.status"}},[a("el-option",{attrs:{value:!0,label:"有效"}}),a("el-option",{attrs:{value:!1,label:"无效"}})],1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"所属用户组",prop:"userGroupId","label-width":"150px"}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.userGroupId,callback:function(t){e.$set(e.userInfo,"userGroupId",t)},expression:"userInfo.userGroupId"}},e._l(e.allGroups,(function(e){return a("el-option",{key:e.id,attrs:{value:e.id,label:e.name}})})),1)],1)],1)],1),"Ip"==e.userInfo.verificationType||"All"==e.userInfo.verificationType?a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("用户IP信息>>")]),a("el-button",{staticStyle:{float:"right",padding:"3px 0"},attrs:{type:"text"},on:{click:function(t){return e.addIp()}}},[e._v("新增IP")])],1),a("el-form-item",{attrs:{label:"",prop:"userIpInfos"}},[a("el-col",{attrs:{span:5}},[e._v("开始IP:")]),a("el-col",{attrs:{span:5}},[e._v("结束IP:")]),a("el-col",{attrs:{span:6}},[e._v("IP绑定单位:")]),a("el-col",{attrs:{span:6}},[e._v("IP备注:")]),e._l(e.userInfo.userIpInfos,(function(t,l){return a("el-row",{key:l,staticStyle:{"margin-bottom":"5px"},attrs:{gutter:5}},[a("el-col",{attrs:{span:5}},[a("el-input",{attrs:{placeholder:"请输入开始IP"},model:{value:t.startIp,callback:function(a){e.$set(t,"startIp",a)},expression:"item.startIp"}})],1),a("el-col",{attrs:{span:5}},[a("el-input",{attrs:{placeholder:"请输入结束IP"},model:{value:t.finishIp,callback:function(a){e.$set(t,"finishIp",a)},expression:"item.finishIp"}})],1),a("el-col",{attrs:{span:6}},[a("el-input",{attrs:{placeholder:"请输入IP绑定单位"},model:{value:t.unit,callback:function(a){e.$set(t,"unit",a)},expression:"item.unit"}})],1),a("el-col",{attrs:{span:6}},[a("el-input",{attrs:{placeholder:"请输入备注"},model:{value:t.remark,callback:function(a){e.$set(t,"remark",a)},expression:"item.remark"}})],1),a("el-col",{attrs:{span:2}},[a("el-button",{on:{click:function(t){return e.removeIp(l)}}},[a("d2-icon",{attrs:{name:"minus"}})],1)],1)],1)}))],2)],1):e._e(),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("文献下载限制规则>>")]),a("el-button",{staticStyle:{float:"right",padding:"3px 0"},attrs:{type:"text"},on:{click:function(t){return e.addRule()}}},[e._v("新增规则")])],1),a("el-form-item",{attrs:{label:"",prop:"downloadRules"}},[a("el-col",{attrs:{span:10}},[e._v("限制类型：")]),a("el-col",{attrs:{span:10}},[e._v("最大下载量：")]),e._l(e.userInfo.downloadRules,(function(t,l){return a("el-row",{key:l,staticStyle:{"margin-bottom":"5px"},attrs:{gutter:5}},[a("el-col",{attrs:{span:10}},[a("el-select",{model:{value:e.userInfo.downloadRules[l].rule,callback:function(t){e.$set(e.userInfo.downloadRules[l],"rule",t)},expression:"userInfo.downloadRules[i].rule"}},e._l(e.$enums.value.downloadRule,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1),a("el-col",{attrs:{span:10}},[a("el-input-number",{attrs:{step:1,min:0},model:{value:e.userInfo.downloadRules[l].limit,callback:function(t){e.$set(e.userInfo.downloadRules[l],"limit",t)},expression:"userInfo.downloadRules[i].limit"}})],1),a("el-col",{attrs:{span:4}},[a("el-button",{on:{click:function(t){return e.removeRule(l)}}},[a("d2-icon",{attrs:{name:"minus"}})],1)],1)],1)}))],2)],1),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("联系信息>>")])]),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"联系人",prop:"linkMan","label-width":"150px"}},[a("el-input",{model:{value:e.userInfo.linkMan,callback:function(t){e.$set(e.userInfo,"linkMan",t)},expression:"userInfo.linkMan"}})],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"所在单位",prop:"unit","label-width":"150px"}},[a("el-input",{model:{value:e.userInfo.unit,callback:function(t){e.$set(e.userInfo,"unit",t)},expression:"userInfo.unit"}})],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"联系电话",prop:"phone","label-width":"150px"}},[a("el-input",{model:{value:e.userInfo.phone,callback:function(t){e.$set(e.userInfo,"phone",t)},expression:"userInfo.phone"}})],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"联系地址",prop:"address","label-width":"150px"}},[a("el-input",{model:{value:e.userInfo.address,callback:function(t){e.$set(e.userInfo,"address",t)},expression:"userInfo.address"}})],1)],1),a("el-col",{attrs:{span:8}},[a("el-form-item",{attrs:{label:"电子邮箱",prop:"email","label-width":"120px"}},[a("el-input",{model:{value:e.userInfo.email,callback:function(t){e.$set(e.userInfo,"email",t)},expression:"userInfo.email"}})],1)],1),a("el-col",{attrs:{span:8}},[a("el-form-item",{attrs:{label:"邮政编码",prop:"postCode","label-width":"120px"}},[a("el-input",{model:{value:e.userInfo.postCode,callback:function(t){e.$set(e.userInfo,"postCode",t)},expression:"userInfo.postCode"}})],1)],1),a("el-col",{attrs:{span:8}},[a("el-form-item",{attrs:{label:"传真",prop:"fax","label-width":"120px"}},[a("el-input",{model:{value:e.userInfo.fax,callback:function(t){e.$set(e.userInfo,"fax",t)},expression:"userInfo.fax"}})],1)],1)],1),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("分类统计信息>>")])]),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"所在区域",prop:"area","label-width":"150px"}},[a("el-select",{model:{value:e.userInfo.area,callback:function(t){e.$set(e.userInfo,"area",t)},expression:"userInfo.area"}},[a("el-option",{attrs:{value:"大陆",label:"大陆"}}),a("el-option",{attrs:{value:"其它",label:"其它"}})],1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"所在省份",prop:"province","label-width":"150px"}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.province,callback:function(t){e.$set(e.userInfo,"province",t)},expression:"userInfo.province"}},e._l(e.commonEnums.provinces,(function(e){return a("el-option",{key:e,attrs:{value:e,label:e}})})),1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"单位类型",prop:"unitType","label-width":"150px"}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.unitType,callback:function(t){e.$set(e.userInfo,"unitType",t)},expression:"userInfo.unitType"}},e._l(e.$enums.value.unitType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"所在行业",prop:"industry","label-width":"150px"}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.industry,callback:function(t){e.$set(e.userInfo,"industry",t)},expression:"userInfo.industry"}},e._l(e.commonEnums.industries,(function(e){return a("el-option",{key:e,attrs:{value:e,label:e}})})),1)],1)],1)],1),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("费用信息>>")])]),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"用户类型",prop:"userType","label-width":"150px",rules:{required:!0,message:"用户类型不能为空!",trigger:"blur"}}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.userType,callback:function(t){e.$set(e.userInfo,"userType",t)},expression:"userInfo.userType"}},e._l(e.$enums.value.userType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),a("el-col",{attrs:{span:12}},[a("el-form-item",{attrs:{label:"付费方式",prop:"paymentType","label-width":"150px",rules:{required:!0,message:"付费方式不能为空!",trigger:"blur"}}},[a("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.paymentType,callback:function(t){e.$set(e.userInfo,"paymentType",t)},expression:"userInfo.paymentType"}},e._l(e.$enums.value.paymentType,(function(e){return a("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1)],1),a("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("备注信息>>")])]),a("el-col",{attrs:{span:24}},[a("el-form-item",{attrs:{label:"",prop:"remark"}},[a("el-input",{attrs:{width:"100%",rows:"4",type:"textarea",placeholder:"请输入备注信息"},model:{value:e.userInfo.remark,callback:function(t){e.$set(e.userInfo,"remark",t)},expression:"userInfo.remark"}})],1)],1)],1)],1),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.close}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.submit()}}},[e._v("确 定")])],1)],1)],1)},s=[],r=(a("4160"),a("a434"),a("159b"),a("bc3a")),n=a.n(r),o=a("61f7"),i=a("cd49"),u=a("8060").sm2,c=a("22ce").publicKey,p={props:["allGroups"],data:function(){var e=this,t=function(t,a,l){"新增用户"===e.title||a||l(),"Ip"==e.userInfo.verificationType&&l(),a||l(new Error("密码不能为空!")),Object(o["b"])(a)||l(new Error("密码最少8位，必须包含大写字母、小写字母和数字")),l()},a=function(e,t,a){t||a(),Object(o["d"])(t)||a(new Error("手机号码格式不正确")),a()},l=function(e,t,a){t||a(),Object(o["a"])(t)||a(new Error("邮箱格式不正确,请检查!")),a()};return{title:"",visible:!1,userInfo:{},status:!0,api:"/api/",commonEnums:i["b"],rules:{userName:[{required:!0,message:"登录名不能为空!",trigger:"blur"}],password:[{validator:t,trigger:"blur"}],phone:[{validator:a,trigger:"blur"}],realName:[{required:!0,message:"姓名不能为空!",trigger:"blur"}],email:[{validator:l,trigger:"blur"}]}}},methods:{show:function(e){this.userInfo=JSON.parse(JSON.stringify(e)),e&&e.id?(this.title="修改用户信息",this.getUserIpInfo(),this.getUserDownloadRules()):this.title="新增用户",this.userInfo.userIpInfos||(this.userInfo.userIpInfos=[]),this.userInfo.downloadRules||(this.userInfo.downloadRules=[]),this.visible=!0},close:function(){this.visible=!1},removeIp:function(e){this.userInfo.userIpInfos.splice(e,1),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},addIp:function(){this.userInfo.userIpInfos.push({}),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},getUserIpInfo:function(){var e=this;this.$axiosRequest("system/userIpInfo/getUserIpInfo",{userId:this.userInfo.id},this.$axiosOptions("get")).then((function(t){e.userInfo.userIpInfos=t.userIpInfos}))},removeRule:function(e){this.userInfo.downloadRules.splice(e,1)},addRule:function(){var e={rule:this.$enums.value.downloadRule[0].value,limit:1};this.userInfo.downloadRules.push(e),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},getUserDownloadRules:function(){var e=this;this.$axiosRequest("system/downloadRules/getRules",{userId:this.userInfo.id},this.$axiosOptions("get")).then((function(t){e.userInfo.downloadRules=t.downloadRules,e.userInfo=JSON.parse(JSON.stringify(e.userInfo))}))},checkUserName:function(){var e=this;this.$axiosRequest("system/user/checkUserName",{userName:this.userInfo.userName},this.$axiosOptions("get")).then((function(t){t.success?e.$message.success("该账号可用!"):e.$message.error(t.msg)}))},submit:function(){var e=this,t=!0;this.userInfo.userIpInfos.forEach((function(a,l){var s=/^((25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[0]\d{2}|[0]{2}\d|\d|0\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[1-9]|[0]\d{2}|[0]{2}\d)$/;if(!s.test(a.startIp))return e.$message.error("第"+(l+1)+"条起始IP不正确,请检查!"),void(t=!1);s.test(a.finishIp)||(e.$message.error("第"+(l+1)+"条结束IP不正确,请检查!"),t=!1)})),t&&this.$refs.userInfoForm.validate((function(t){if(t){var a=JSON.parse(JSON.stringify(e.userInfo));a.password&&(a.password="04"+u.doEncrypt(a.password,c,1)),n()({method:"post",url:"system/user/addOrModify",data:a}).then((function(t){t.data.success?(e.$message.success("操作成功!"),e.visible=!1,e.$emit("callback")):e.$message.error(t.data.msg)}))}else e.$message.error("表单验证不通过，请检查!")}))}}},d=p,f=(a("5b9b"),a("2877")),m=Object(f["a"])(d,l,s,!1,null,null,null);t["default"]=m.exports}}]);