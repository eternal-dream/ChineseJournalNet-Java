(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-c7fef36e","chunk-a2024f84"],{"460f":function(t,e,s){"use strict";s.d(e,"a",(function(){return r}));s("96cf"),s("1da1");var i=s("22ce"),o="/common/";function r(){var t=Object(i["axiosOptions"])("get");return Object(i["axiosRequest"])(o+"/verifyCode",null,t)}},"60ee":function(t,e,s){},"70cf":function(t,e,s){"use strict";var i=s("60ee"),o=s.n(i);o.a},bfba:function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",[s("el-dialog",{attrs:{title:"用户登录",visible:t.visible,width:"25%","before-close":t.close,"close-on-click-modal":!1,inline:!0,"modal-append-to-body":!1,"append-to-body":!0},on:{"update:visible":function(e){t.visible=e}}},[s("el-form",{ref:"loginForm",attrs:{model:t.userInfo}},[s("el-form-item",{attrs:{label:"用户名",prop:"userName","label-width":"80px",rules:{required:!0,message:"请输入用户名!",trigger:"blur"}}},[s("el-input",{attrs:{placeholder:"请输入用户名"},model:{value:t.userInfo.userName,callback:function(e){t.$set(t.userInfo,"userName",e)},expression:"userInfo.userName"}})],1),s("el-form-item",{attrs:{label:"密码",prop:"password","label-width":"80px",rules:{required:!0,message:"请输入密码!",trigger:"blur"}}},[s("el-input",{attrs:{type:"password",placeholder:"请输入密码"},model:{value:t.userInfo.password,callback:function(e){t.$set(t.userInfo,"password",e)},expression:"userInfo.password"}})],1),s("el-form-item",{attrs:{prop:"code",label:"验证码","label-width":"80px",rules:{required:!0,message:"请输入验证码!",trigger:"blur"}}},[s("el-input",{attrs:{type:"text",placeholder:"验证码"},model:{value:t.userInfo.code,callback:function(e){t.$set(t.userInfo,"code",e)},expression:"userInfo.code"}},[s("template",{slot:"append"},[s("img",{staticClass:"login-code",attrs:{src:t.codeUrl},on:{click:t.getCode}})])],2)],1)],1),s("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{staticStyle:{width:"48%"},attrs:{type:"primary"},on:{click:t.submit}},[t._v("登录")]),s("el-button",{staticStyle:{width:"48%"},attrs:{type:"success"},on:{click:t.tryIpLogin}},[t._v("Ip登录")])],1)],1)],1)},o=[],r=s("5530"),n=s("460f"),a=(s("bc3a"),s("2f62")),l={data:function(){return{visible:!1,userInfo:{},codeUrl:""}},created:function(){},computed:Object(r["a"])({},Object(a["e"])("front/user",["info"])),methods:{show:function(){this.visible=!0,this.getCode()},close:function(){this.$refs.loginForm.resetFields(),this.visible=!1},getCode:function(){var t=this;Object(n["a"])().then((function(e){t.codeUrl="data:image/gif;base64,"+e.img}))},tryIpLogin:function(){var t=this;this.$store.dispatch("front/user/tryIpLogin").then((function(e){e.success?t.close():t.$message.error(e.msg)}))},submit:function(){var t=this;this.$refs.loginForm.validate((function(e){e&&t.$store.dispatch("front/user/login",t.userInfo).then((function(e){e.success?t.close():t.$message.error(e.msg)}))}))}}},c=l,u=s("2877"),f=Object(u["a"])(c,i,o,!1,null,null,null);e["default"]=f.exports},cb91:function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",[s("div",{staticClass:"part index-show show-type2"},[s("div",{staticClass:"l-width"},[s("div",{staticClass:"show-top"},[s("ul",{staticClass:"top-nav"},[s("li",{staticClass:"top-nav-journal"},[s("router-link",{attrs:{to:"/journal/index"}},[s("i",{staticClass:"icon-journal"}),t._v("期刊导航")])],1)]),s("ul",{staticClass:"user-nav"},[t.info.id?s("li",[s("a",{attrs:{href:"javascript:void(0)"}},[s("i",{staticClass:"icon-login"}),t._v(t._s(t.info.userName))])]):s("li",[s("a",{attrs:{href:"javascript:void(0)",id:"login-btn"},on:{click:function(e){return t.$refs.login.show()}}},[s("i",{staticClass:"icon-login"}),t._v("登录")])]),t.info.id?s("li",[s("a",{attrs:{href:"javascript:void(0)"},on:{click:function(e){return t.$store.dispatch("front/user/logout")}}},[s("d2-icon",{attrs:{name:"sign-out"}}),t._v("退出")],1)]):t._e()])]),t._m(0),s("div",{staticClass:"show-text"},[t._v("已收录"),s("strong",[t._v(t._s(t.formatNumber(t.articleNumber))+"+")]),t._v("条文献")]),s("searcher",{ref:"searcher"})],1)]),s("login",{ref:"login"})],1)},o=[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"show-logo"},[i("img",{attrs:{src:s("d63f"),alt:"维普期刊服务平台"}})])}],r=(s("d3b7"),s("ac1f"),s("25f0"),s("5319"),s("5530")),n=s("bfba"),a=s("2f62"),l={name:"Index",components:{login:n["default"]},data:function(){return{articleNumber:71094272}},computed:Object(r["a"])({},Object(a["e"])("front/user",["info","logoutSign","loginSign"])),mounted:function(){this.getArticleNumber(),!1===this.loginSign&&!1===this.logoutSign&&this.$store.dispatch("front/user/tryIpLogin")},methods:{getArticleNumber:function(){var t=this;this.$axiosRequest("view/search/getArticleNumber",{},this.$axiosOptions("get")).then((function(e){t.articleNumber=e.number}))},formatNumber:function(t){return t?t.toString().replace(/\d+/,(function(t){return t.replace(/(\d)(?=(?:\d{3})+$)/g,"$1,")})):0}},watch:{info:function(t,e){this.info.id||this.$refs.login.show()}}},c=l,u=(s("70cf"),s("2877")),f=Object(u["a"])(c,i,o,!1,null,"eed5394c",null);e["default"]=f.exports},d63f:function(t,e,s){t.exports=s.p+"static/img/show-logo.54554fe6.png"}}]);