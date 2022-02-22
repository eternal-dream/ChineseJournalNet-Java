(function () {
    $(document).ready(function () {
        layui.use(['element', 'layer', 'form'], function () {
            var form = layui.form
                , laypage = layui.laypage
                , $ = layui.jquery
                , layer = layui.layer;
            var index;
            //$('#login-btn').off('click');
            //$('#login-btn').on('click', function () {
            //    loginAction();
            //});
            $(document).off("click", "#login-btn").on("click", "#login-btn", function () {
                loginAction();
            })
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
            //function LoginCallBack(index, Msg) {
            //    layer.close(index);
            //    $('.user-nav li:eq(0)').after('<li><a href="#"><i class="icon-login"></i>欢迎:' + Msg + '</a></li>').remove();
            //    var li = $('<li></li>');
            //    var link = $('<a href="#" id="Logout"><i class="icon-login"></i>退出</a>');
            //    link.bind('click', function () {
            //        //登录状态改回  （用  js  操作改变页面的 Header 实现）
            //        $.get("/RegistLogin/LoginOut", function (data) {
            //            if (data.statuesCode == "ok") {
            //                var lilogin = $('<li></li>');
            //                var loginlick = $('<a href="#" id="login-btn"><i class="icon-login"></i>登录</a>')
            //                lilogin.append(loginlick);
            //                $('.user-nav li:eq(0)').after(lilogin).remove();
            //                $(this).remove();
            //                $('#Logout').parent().remove();
            //                loginlick.bind('click', function () {
            //                    loginAction();
            //                })
            //            } else {
            //                layer.alert("注销失败",
            //                    {
            //                        title: "提示！"
            //                    })
            //            }
            //        })


            //    });
            //    li.append(link);
            //    $('.user-nav').append(li);
            //}
            //form.verify({
            //    UserNameVertify: function (value, item) {
            //        var sRegex = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
            //        if (!sRegex.test(value)) {
            //            return '邮箱格式错误！';
            //        }
            //    }
            //    , PasswordVertify: function (value, item) {
            //        var sRegex = /[^0-9a-zA-Z#_]/;
            //        var lBadWord = value.search(sRegex);
            //        if (lBadWord >= 0) {
            //            return "有非法字符'" + String(value).substr(lBadWord, 1) + "'";
            //        }
            //        if (!new RegExp('^[0-9a-zA-Z#_]{6,16}$').test(value))
            //            return "密码必须6到16位";
            //    }
            //});

            //登录执行移动到  loginaction.JS 文件中，header只负责 弹出登录框
            //form.on('submit(Login)', function (data) {
            //    //alert(1);
            //    // console.log(data.field);
            //    data.field['LoginType'] = "normallogin";
            //    data.field['LoginType'] = "normallogin";
            //    //console.log(data.field);
            //    $.ajax({
            //        url: '/RegistLogin/Login',
            //        data: data.field,
            //        type: 'post',
            //        dataType: 'json',
            //        success: function (succ) {
            //            $("input[name='LoginUserPassword']").val("");
            //            if (succ.Status) {
            //                //成功后改变 页面 右上角
            //                $.get("/Qikan/RegistLogin/LoginView", function (data) {
            //                    //  console.log(data);
            //                    $('.user-nav').find("li").remove();
            //                    $('.user-nav').append(data);
            //                });//用 后台生成HTML 实现 改变 Header;
            //                layer.closeAll('page');
            //            } else {
            //                layer.alert("用户名或密码错误",
            //                    {
            //                        title: "提示！"
            //                    })
            //            }
            //        }, error: function (err) {
            //            layer.alert("用户名或密码错误",
            //                {
            //                    title: "提示！"
            //                })
            //        }
            //    });
            //    return false;
            //})
            //$('#IPLogin').on('click', function () {
            //    var json = {};
            //    json['LoginType'] = 'iplogin';
            //    $.ajax({
            //        url: '/RegistLogin/Login',
            //        data: json,
            //        type: 'post',
            //        dataType: 'json',
            //        success: function (succ) {
            //            if (succ.Status) {
            //                //成功后改变 页面 右上角
            //                $.get("/Qikan/RegistLogin/LoginView", function (data) {
            //                    // console.log(data);
            //                    $('.user-nav').find("li").remove();
            //                    $('.user-nav').append(data);
            //                })//用 后台生成HTML 实现 改变 Header

            //                // LoginCallBack(index, succ.Msg);// 用 js 操作 页面元素 实现改变Header
            //                //layer.close(index);

            //                layer.closeAll('page');
            //            } else {
            //                layer.alert("登录失败",
            //                    {
            //                        title: "提示！"
            //                    })
            //            }
            //        }, error: function (err) {
            //            layer.alert("登录失败",
            //                {
            //                    title: "提示！"
            //                })
            //        }
            //    });
            //})
            //登录执行移动到  loginaction.JS 文件中，header只负责 弹出登录框
        });
    })
})()