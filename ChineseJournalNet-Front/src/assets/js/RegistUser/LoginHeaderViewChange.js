//为 首次加载 页面 登录，退出绑定 事件
$(function () {
    layui.use(['element', 'layer', 'form'], function () {
        var form = layui.form
            , laypage = layui.laypage
            , $ = layui.jquery
            , layer = layui.layer;
        $('#Logout').off('click');
        $('#Logout').on('click', function () {
            // console.log(1111);
            $.get("/RegistLogin/LoginOut?t=" + Math.random(), function (data) {
                // console.log(data.statuesCode);
                if (data.statuesCode == "ok") {
                    $.get("/Qikan/RegistLogin/LoginView?t=" + Math.random(), function (data) {
                        // console.log(data);
                        $('#user-nav').find("li").remove();
                        $('#user-nav').append(data);
                        $('.user-nav #login-btn').off('click');
                        $('.user-nav #login-btn').on('click', function () {
                            loginAction();
                        });
                        // console.log($($.trim(data)).find('#login-btn').html());
                        //$($.trim(data)).find('#login-btn').off('click');
                        //$($.trim(data)).find('#login-btn').bind('click', function () {
                        //    index = layer.open({
                        //        id: 1,
                        //        type: 1
                        //        //,title: false
                        //        , skin: 'layui-layer-rim login'
                        //        , title: ' '
                        //        , area: ['350px', '300px']
                        //        , anim: 0
                        //        , move: false
                        //        , resize: false
                        //        , content: $('#login')
                        //        //,closeBtn: 0
                        //        , zIndex: 2 //重点1
                        //        , success: function (layero) {
                        //            // layer.setTop(layero); //重点2

                        //        }
                        //    });
                        //})
                    })
                }
            })
        })
        $('#login-btn').off('click');
        $('#login-btn').on('click', function () {
            loginAction();
        });
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
    });
})
