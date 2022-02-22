//全局变量
var callParams = [];
var callBackFunction;
(function () {
    $(document).ready(function () {
        layui.use(['element', 'layer', 'form'], function () {
            var form = layui.form
                , laypage = layui.laypage
                , $ = layui.jquery
                , layer = layui.layer;
            var index;
            callBackFunction = LoginHeraderChange;//登录后的回调函数
            form.on('submit(Login)', function (data) {
                data.field['LoginType'] = "normallogin";
                data.field['PToken'] = AesEncrypt(data.field['LoginUserPassword']);
                data.field['LoginUserPassword'] = $.md5(data.field['LoginUserPassword']);
                $.ajax({
                    url: '/RegistLogin/Login',
                    data: data.field,
                    type: 'post',
                    dataType: 'json',
                    beforeSend: function () {
                        loadding();
                    },
                    complete: function () {
                        loaddingClose();
                    },
                    success: function (succ) {
                        $("input[name='LoginUserPassword']").val("");
                        //console.log(succ.Status);
                        if (succ.Status) {
                            //成功后改变 页面 右上角
                            $.get("/Qikan/RegistLogin/LoginView", { t: Math.random() }, function (data) {
                                $('#user-nav').find("li").remove();
                                $('#user-nav').append(data);
                                if (callBackFunction) {
                                    if (callBackFunction == LoginHeraderChange) {// 正常登录
                                        LoginHeraderChange();
                                    } else {// 其他操作检测是否登录 后回调
                                        LoginHeraderChange();
                                        eval("callBackFunction(" + callParams + ")");
                                        callBackFunction = LoginHeraderChange;
                                        //  eval("callbackFuntion(" + p + ")");
                                    }
                                }
                                //  LoginHeraderChange();
                            });//用 后台生成HTML 实现 改变 Header;

                            layer.closeAll('page');
                        }
                        else if (succ.StauesCode == "CodeErro") {
                            layer.alert(succ.Msg, { title: "提示！" }, function (index) {
                                $("input[name='VerificationCode']").val("");
                                $(".code-img>img").attr("src", "/Qikan/RegistLogin/VerficationCode?" + Math.random());
                                layer.close(index);
                            });
                        }
                        else if (succ.StauesCode === "resetpassword")
                        {
                            layer.alert('亲爱的客户您好！根据《网络安全法》和《通信网络安全防护管理办法》的有关规定，现系统监测到您的登录密码类属于网络安全威胁弱密码范畴，请尽快联系平台管理员修改您的密码后使用！', function (index) {
                                layer.close(index);
                            });
                        }
                        else {
                            layer.alert("用户名或密码错误", { title: "提示！" }, function (index) {
                                $("input[name='VerificationCode']").val("");
                                $(".code-img>img").attr("src", "/Qikan/RegistLogin/VerficationCode?" + Math.random());
                                layer.close(index);
                            });
                        }
                    }, error: function (err) {
                        layer.alert("用户名或密码错误", { title: "提示！" }, function (index) {
                            $("input[name='VerificationCode']").val("");
                            $(".code-img>img").attr("src", "/Qikan/RegistLogin/VerficationCode?" + Math.random());
                            layer.close(index);
                        });
                    }
                });
                return false;
            })
            $('#IPLogin').on('click', function () {
                var json = {};
                json['LoginType'] = 'iplogin';
                $.ajax({
                    url: '/RegistLogin/Login',
                    data: json,
                    type: 'post',
                    async: true,
                    dataType: 'json',
                    beforeSend: function () {
                        loadding();
                    },
                    complete: function () {
                        loaddingClose();
                    },
                    success: function (succ) {
                        if (succ.Status) {
                            //成功后改变 页面 右上角
                            $.get("/Qikan/RegistLogin/LoginView", { t: Math.random() }, function (data) {
                                //  console.log(data);
                                $('#user-nav').find("li").remove();
                                $('#user-nav').append(data);
                                if (callBackFunction) {
                                    if (callBackFunction == LoginHeraderChange) {
                                        LoginHeraderChange();
                                    } else {
                                        LoginHeraderChange();
                                        eval("callBackFunction(" + callParams + ")");
                                        callBackFunction = LoginHeraderChange;//下载后把登录后的操作重置
                                        // eval("callbackFuntion(" + p + ")");
                                    }
                                }
                                // 登录操作后 改变 dom 结构时绑定 退出操作
                                // console.log($('#Logout'));
                                // LoginHeraderChange();
                            })//用 后台生成HTML 实现 改变 Header
                            // LoginCallBack(index, succ.Msg);// 用 js 操作 页面元素 实现改变Header
                            //layer.close(index);
                            layer.closeAll('page');
                        } else {
                            layer.alert("登录失败",
                                {
                                    title: "提示！"
                                })
                        }
                    }, error: function (err) {
                        layer.alert("登录失败", { title: "提示！" })
                    }
                });

            })
            $("#login").bind("keypress", function (e) {
                var theEvent = e || window.event;
                var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
                if (code == 13) {    //回车键的键值为13
                    $("#normalLogin").click(); //调用登录按钮的登录事件
                }
            })

            $('#login ul li').bind('click', function () {
                if ($(this).text() == '用户登录') {
                    if ($('.login-switch a').hasClass('text'))
                        $('.login-switch a').removeClass('text').addClass('code');
                    if (iCount != null) {
                        clearInterval(iCount);
                        iCount = null;
                    }
                }
            })
            function CheckUserLogin(callbackFuntion, params) {
                var logined = false;
                $.ajax({
                    url: '/RegistLogin/CheckUserIslogin?' + Math.random(),
                    dataType: 'json',
                    async: false,
                    success: function (json) { logined = json.isLogined; },
                    error: function () { logined = false; }
                });
                if (logined) {//登录了
                    if (callbackFuntion) {
                        var p = "";
                        if (params) {
                            for (var i = 0; i < params.length; i++) {
                                var temp = params[i];
                                if (isNaN(params[i]))//非法数字也加''
                                {
                                    temp = "'" + params[i] + "'";
                                }
                                if (p == "") {
                                    if (/^[\d.]+$/.test(temp))//纯数字
                                        p = "'" + temp.toString() + "'";
                                    else
                                        p = temp;
                                } else {
                                    if (/^[\d.]+$/.test(temp))//纯数字
                                        p += "," + "'" + temp.toString() + "'";
                                    else
                                        p += "," + temp;
                                    // p += "," + temp.toString();
                                }
                            }
                        }
                        eval("callbackFuntion(" + p + ")");
                    }
                }
                else {//没有登录 准备 会回调函数 和调用登录框

                    callBackFunction = (callbackFuntion || LoginHeraderChange);
                    var p = "";
                    if (params) {
                        for (var i = 0; i < params.length; i++) {
                            var temp = params[i];
                            if (isNaN(params[i])) {
                                temp = "'" + params[i] + "'";
                            }
                            if (p == "") {
                                p = temp;
                            } else {
                                p += "," + temp;
                            }
                        }
                    }
                    callParams = p;
                    loginAction();//调用登录框
                }
            }// 
            window.checklogin = CheckUserLogin;//全局检测登录回调
        });
    })
})()
//全局函数
function LoginHeraderChange() {
    $(document).off("click", ".user-nav #Logout").on("click", ".user-nav #Logout", function () {
        $.get("/RegistLogin/LoginOut?t=" + Math.random(), function (data) {
            // console.log(data.statuesCode);
            if (data.statuesCode == "ok") {
                $.get("/Qikan/RegistLogin/LoginView?t=" + Math.random(), function (data) {
                    //  console.log(data);
                    $('#user-nav').find("li").remove();
                    $('#user-nav').append(data);

                    $('.user-nav #login-btn').off('click');
                    $('.user-nav #login-btn').on('click', function () {
                        loginAction();
                    });
                })
            }
        })
    })
}
function loginAction() {
    index = layer.open({
        id: 1,
        type: 1
        //,title: false
        , skin: 'layui-layer-rim login'
        , title: ' '
        , area: ['350px', '300px']
        , anim: 0
        , move: false
        , resize: false
        , content: $('#login')
        //,closeBtn: 0
        //, zIndex: 100 //重点1
        , success: function (layero) {
            // layer.setTop(layero); //重点2
            $("input[name='VerificationCode']").val("");
            $(".code-img>img").attr("src", "/Qikan/RegistLogin/VerficationCode?" + Math.random());
        }
    });
}

