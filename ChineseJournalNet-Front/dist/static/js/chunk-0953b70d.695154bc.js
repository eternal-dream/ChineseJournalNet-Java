(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0953b70d"],{"5ae0":function(e,t,s){},"5b9b":function(e,t,s){"use strict";var l=s("5ae0"),r=s.n(l);r.a},"61f7":function(e,t,s){"use strict";s.d(t,"c",(function(){return r})),s.d(t,"b",(function(){return a})),s.d(t,"a",(function(){return o})),s.d(t,"d",(function(){return n}));s("d3b7"),s("25f0");var l=!0;function r(e){var t=/^.*(?=.{6,})(?=.*\d)(?=.*[a-zA-Z]).*$/;return t.test(e)}function a(e){if(l)return!0;var t=/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*$/;return t.test(e)}function o(e){var t=/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;return t.test(e)}function n(e){var t=/^1(3|4|5|6|7|8|9)\d{9}$/;return t.test(e)}},d3a4:function(e,t,s){"use strict";s.r(t);var l=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",[s("el-dialog",{attrs:{title:e.title,visible:e.visible,width:"70%","before-close":e.close,"close-on-click-modal":!1,inline:!0},on:{"update:visible":function(t){e.visible=t}}},[s("el-form",{ref:"userInfoForm",attrs:{model:e.userInfo,rules:e.rules,size:"small"}},[s("el-card",{staticClass:"box-card"},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("账号基本信息>>")])]),s("el-col",{attrs:{span:9}},[s("el-form-item",{attrs:{label:"用户名",prop:"userName","label-width":"150px"}},[s("el-input",{model:{value:e.userInfo.userName,callback:function(t){e.$set(e.userInfo,"userName",t)},expression:"userInfo.userName"}})],1)],1),s("el-col",{attrs:{span:3}},[s("el-link",{attrs:{type:"primary",underline:!1},on:{click:e.checkUserName}},[e._v("验证账号有效性")])],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"密码",prop:"password","label-width":"150px"}},[s("el-input",{attrs:{type:"password",placeholder:"新增用户"===e.title?"密码最少8位，必须包含大写字母、小写字母和数字":"不填表示不修改"},model:{value:e.userInfo.password,callback:function(t){e.$set(e.userInfo,"password",t)},expression:"userInfo.password"}})],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"登录验证方式",prop:"verificationType","label-width":"150px",rules:{required:!0,message:"验证方式不能为空!",trigger:"blur"}}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.verificationType,callback:function(t){e.$set(e.userInfo,"verificationType",t)},expression:"userInfo.verificationType"}},e._l(e.$enums.value.verificationType,(function(e){return s("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"账户状态",prop:"status","label-width":"150px"}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.status,callback:function(t){e.$set(e.userInfo,"status",t)},expression:"userInfo.status"}},[s("el-option",{attrs:{value:!0,label:"有效"}}),s("el-option",{attrs:{value:!1,label:"无效"}})],1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"所属用户组",prop:"userGroupId","label-width":"150px"}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.userGroupId,callback:function(t){e.$set(e.userInfo,"userGroupId",t)},expression:"userInfo.userGroupId"}},e._l(e.allGroups,(function(e){return s("el-option",{key:e.id,attrs:{value:e.id,label:e.name}})})),1)],1)],1)],1),"Ip"==e.userInfo.verificationType||"All"==e.userInfo.verificationType?s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("用户IP信息>>")]),s("el-button",{staticStyle:{float:"right",padding:"3px 0"},attrs:{type:"text"},on:{click:function(t){return e.addIp()}}},[e._v("新增IP")])],1),s("el-form-item",{attrs:{label:"",prop:"userIpInfos"}},[s("el-col",{attrs:{span:5}},[e._v("开始IP:")]),s("el-col",{attrs:{span:5}},[e._v("结束IP:")]),s("el-col",{attrs:{span:6}},[e._v("IP绑定单位:")]),s("el-col",{attrs:{span:6}},[e._v("IP备注:")]),e._l(e.userInfo.userIpInfos,(function(t,l){return s("el-row",{key:l,staticStyle:{"margin-bottom":"5px"},attrs:{gutter:5}},[s("el-col",{attrs:{span:5}},[s("el-input",{attrs:{placeholder:"请输入开始IP"},model:{value:t.startIp,callback:function(s){e.$set(t,"startIp",s)},expression:"item.startIp"}})],1),s("el-col",{attrs:{span:5}},[s("el-input",{attrs:{placeholder:"请输入结束IP"},model:{value:t.finishIp,callback:function(s){e.$set(t,"finishIp",s)},expression:"item.finishIp"}})],1),s("el-col",{attrs:{span:6}},[s("el-input",{attrs:{placeholder:"请输入IP绑定单位"},model:{value:t.unit,callback:function(s){e.$set(t,"unit",s)},expression:"item.unit"}})],1),s("el-col",{attrs:{span:6}},[s("el-input",{attrs:{placeholder:"请输入备注"},model:{value:t.remark,callback:function(s){e.$set(t,"remark",s)},expression:"item.remark"}})],1),s("el-col",{attrs:{span:2}},[s("el-button",{on:{click:function(t){return e.removeIp(l)}}},[s("d2-icon",{attrs:{name:"minus"}})],1)],1)],1)}))],2)],1):e._e(),s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("文献下载限制规则>>")]),s("el-button",{staticStyle:{float:"right",padding:"3px 0"},attrs:{type:"text"},on:{click:function(t){return e.addRule()}}},[e._v("新增规则")])],1),s("el-form-item",{attrs:{label:"",prop:"downloadRules"}},[s("el-col",{attrs:{span:10}},[e._v("限制类型：")]),s("el-col",{attrs:{span:10}},[e._v("最大下载量：")]),e._l(e.userInfo.downloadRules,(function(t,l){return s("el-row",{key:l,staticStyle:{"margin-bottom":"5px"},attrs:{gutter:5}},[s("el-col",{attrs:{span:10}},[s("el-select",{model:{value:e.userInfo.downloadRules[l].rule,callback:function(t){e.$set(e.userInfo.downloadRules[l],"rule",t)},expression:"userInfo.downloadRules[i].rule"}},e._l(e.$enums.value.downloadRule,(function(e){return s("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1),s("el-col",{attrs:{span:10}},[s("el-input-number",{attrs:{step:1,min:0},model:{value:e.userInfo.downloadRules[l].limit,callback:function(t){e.$set(e.userInfo.downloadRules[l],"limit",t)},expression:"userInfo.downloadRules[i].limit"}})],1),s("el-col",{attrs:{span:4}},[s("el-button",{on:{click:function(t){return e.removeRule(l)}}},[s("d2-icon",{attrs:{name:"minus"}})],1)],1)],1)}))],2)],1),s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("联系信息>>")])]),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"联系人",prop:"linkMan","label-width":"150px"}},[s("el-input",{model:{value:e.userInfo.linkMan,callback:function(t){e.$set(e.userInfo,"linkMan",t)},expression:"userInfo.linkMan"}})],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"所在单位",prop:"unit","label-width":"150px"}},[s("el-input",{model:{value:e.userInfo.unit,callback:function(t){e.$set(e.userInfo,"unit",t)},expression:"userInfo.unit"}})],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"联系电话",prop:"phone","label-width":"150px"}},[s("el-input",{model:{value:e.userInfo.phone,callback:function(t){e.$set(e.userInfo,"phone",t)},expression:"userInfo.phone"}})],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"联系地址",prop:"address","label-width":"150px"}},[s("el-input",{model:{value:e.userInfo.address,callback:function(t){e.$set(e.userInfo,"address",t)},expression:"userInfo.address"}})],1)],1),s("el-col",{attrs:{span:8}},[s("el-form-item",{attrs:{label:"电子邮箱",prop:"email","label-width":"120px"}},[s("el-input",{model:{value:e.userInfo.email,callback:function(t){e.$set(e.userInfo,"email",t)},expression:"userInfo.email"}})],1)],1),s("el-col",{attrs:{span:8}},[s("el-form-item",{attrs:{label:"邮政编码",prop:"postCode","label-width":"120px"}},[s("el-input",{model:{value:e.userInfo.postCode,callback:function(t){e.$set(e.userInfo,"postCode",t)},expression:"userInfo.postCode"}})],1)],1),s("el-col",{attrs:{span:8}},[s("el-form-item",{attrs:{label:"传真",prop:"fax","label-width":"120px"}},[s("el-input",{model:{value:e.userInfo.fax,callback:function(t){e.$set(e.userInfo,"fax",t)},expression:"userInfo.fax"}})],1)],1)],1),s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("分类统计信息>>")])]),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"所在区域",prop:"area","label-width":"150px"}},[s("el-select",{model:{value:e.userInfo.area,callback:function(t){e.$set(e.userInfo,"area",t)},expression:"userInfo.area"}},[s("el-option",{attrs:{value:"大陆",label:"大陆"}}),s("el-option",{attrs:{value:"其它",label:"其它"}})],1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"所在省份",prop:"province","label-width":"150px"}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.province,callback:function(t){e.$set(e.userInfo,"province",t)},expression:"userInfo.province"}},e._l(e.commonEnums.provinces,(function(e){return s("el-option",{key:e,attrs:{value:e,label:e}})})),1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"单位类型",prop:"unitType","label-width":"150px"}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.unitType,callback:function(t){e.$set(e.userInfo,"unitType",t)},expression:"userInfo.unitType"}},e._l(e.$enums.value.unitType,(function(e){return s("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"所在行业",prop:"industry","label-width":"150px"}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.industry,callback:function(t){e.$set(e.userInfo,"industry",t)},expression:"userInfo.industry"}},e._l(e.commonEnums.industries,(function(e){return s("el-option",{key:e,attrs:{value:e,label:e}})})),1)],1)],1)],1),s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("费用信息>>")])]),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"用户类型",prop:"userType","label-width":"150px",rules:{required:!0,message:"用户类型不能为空!",trigger:"blur"}}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.userType,callback:function(t){e.$set(e.userInfo,"userType",t)},expression:"userInfo.userType"}},e._l(e.$enums.value.userType,(function(e){return s("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1),s("el-col",{attrs:{span:12}},[s("el-form-item",{attrs:{label:"付费方式",prop:"paymentType","label-width":"150px",rules:{required:!0,message:"付费方式不能为空!",trigger:"blur"}}},[s("el-select",{attrs:{placeholder:"请选择",clearable:!0},model:{value:e.userInfo.paymentType,callback:function(t){e.$set(e.userInfo,"paymentType",t)},expression:"userInfo.paymentType"}},e._l(e.$enums.value.paymentType,(function(e){return s("el-option",{key:e.value,attrs:{value:e.value,label:e.label}})})),1)],1)],1)],1),s("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"5px"}},[s("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[s("span",[e._v("备注信息>>")])]),s("el-col",{attrs:{span:24}},[s("el-form-item",{attrs:{label:"",prop:"remark"}},[s("el-input",{attrs:{width:"100%",rows:"4",type:"textarea",placeholder:"请输入备注信息"},model:{value:e.userInfo.remark,callback:function(t){e.$set(e.userInfo,"remark",t)},expression:"userInfo.remark"}})],1)],1)],1)],1),s("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{on:{click:e.close}},[e._v("取 消")]),s("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.submit()}}},[e._v("确 定")])],1)],1)],1)},r=[],a=(s("4160"),s("a434"),s("159b"),s("bc3a")),o=s.n(a),n=s("61f7"),i=s("cd49"),u=s("8060").sm2,c=s("22ce").publicKey,p={props:["allGroups"],data:function(){var e=this,t=function(t,s,l){"新增用户"===e.title||s||l(),"Ip"==e.userInfo.verificationType&&l(),s||l(new Error("密码不能为空!")),Object(n["b"])(s)||l(new Error("密码最少8位，必须包含大写字母、小写字母和数字")),l()},s=function(e,t,s){t||s(),Object(n["d"])(t)||s(new Error("手机号码格式不正确")),s()},l=function(e,t,s){t||s(),Object(n["a"])(t)||s(new Error("邮箱格式不正确,请检查!")),s()};return{title:"",visible:!1,userInfo:{},status:!0,api:"/api/",commonEnums:i["b"],rules:{userName:[{required:!0,message:"登录名不能为空!",trigger:"blur"}],password:[{validator:t,trigger:"blur"}],phone:[{validator:s,trigger:"blur"}],realName:[{required:!0,message:"姓名不能为空!",trigger:"blur"}],email:[{validator:l,trigger:"blur"}]}}},methods:{show:function(e){this.userInfo=JSON.parse(JSON.stringify(e)),e&&e.id?(this.title="修改用户信息",this.getUserIpInfo(),this.getUserDownloadRules()):this.title="新增用户",this.userInfo.userIpInfos||(this.userInfo.userIpInfos=[]),this.userInfo.downloadRules||(this.userInfo.downloadRules=[]),this.visible=!0},close:function(){this.visible=!1},removeIp:function(e){this.userInfo.userIpInfos.splice(e,1),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},addIp:function(){this.userInfo.userIpInfos.push({}),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},getUserIpInfo:function(){var e=this;this.$axiosRequest("system/userIpInfo/getUserIpInfo",{userId:this.userInfo.id},this.$axiosOptions("get")).then((function(t){e.userInfo.userIpInfos=t.userIpInfos}))},removeRule:function(e){this.userInfo.downloadRules.splice(e,1)},addRule:function(){var e={rule:this.$enums.value.downloadRule[0].value,limit:1};this.userInfo.downloadRules.push(e),this.userInfo=JSON.parse(JSON.stringify(this.userInfo))},getUserDownloadRules:function(){var e=this;this.$axiosRequest("system/downloadRules/getRules",{userId:this.userInfo.id},this.$axiosOptions("get")).then((function(t){e.userInfo.downloadRules=t.downloadRules,e.userInfo=JSON.parse(JSON.stringify(e.userInfo))}))},checkUserName:function(){var e=this;this.$axiosRequest("system/user/checkUserName",{userName:this.userInfo.userName},this.$axiosOptions("get")).then((function(t){t.success?e.$message.success("该账号可用!"):e.$message.error(t.msg)}))},submit:function(){var e=this,t=!0;this.userInfo.userIpInfos.forEach((function(s,l){var r=/^((25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[0]\d{2}|[0]{2}\d|\d|0\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[0-9]\d|[1-9]|[0]\d{2}|[0]{2}\d)$/;if(!r.test(s.startIp))return e.$message.error("第"+(l+1)+"条起始IP不正确,请检查!"),void(t=!1);r.test(s.finishIp)||(e.$message.error("第"+(l+1)+"条结束IP不正确,请检查!"),t=!1)})),t&&this.$refs.userInfoForm.validate((function(t){if(t){var s=JSON.parse(JSON.stringify(e.userInfo));s.password&&(s.password="04"+u.doEncrypt(s.password,c,1)),o()({method:"post",url:"system/user/addOrModify",data:s}).then((function(t){t.data.success?(e.$message.success("操作成功!"),e.visible=!1,e.$emit("callback")):e.$message.error(t.data.msg)}))}else e.$message.error("表单验证不通过，请检查!")}))}}},f=p,d=(s("5b9b"),s("2877")),m=Object(d["a"])(f,l,r,!1,null,null,null);t["default"]=m.exports}}]);