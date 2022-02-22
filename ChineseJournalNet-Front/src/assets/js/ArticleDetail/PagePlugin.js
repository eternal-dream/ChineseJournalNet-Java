var plugin = (function () {
    var option = {//默认配置
        PageFirst: 1,//起始页
        MaxPage: 0,//总页数
        PageDownElmentId: "downPage",//下一页按钮
        PageUPElmentId: "upPage",// 上一页按钮
        CanvanId: "myCanvas",// Canvansid
        CurrSpan: "CurPage",//当前页 span
        TotalPageSpan: "TotalPage",// 总页数span
        FristPageElmentId: "FristPage",// 首页按钮
        LastPageElementId: "LastPage",// 末页按钮
        dowload: "pdfdown",
        isie8: "0"
    };
    var cxt;
    // var InitPlugin_api = {
    // init:
    function InitPlugin_api(defaultOption) {
        if (!defaultOption) {
            throw new Error("请传入配置参数");
        }

        // Combin();

        // Object.assign(option, defaultOption);
        $.extend(option, defaultOption);
        if (option.MaxPage == 0)
            // throw "总页数未定义";
            return;
        if (option.ArticleId == undefined) {
            throw "文章ID未定义";
        }
        if (null == document.getElementById(option.PageDownElmentId)
            || null == document.getElementById(option.PageUPElmentId)
            || null == document.getElementById(option.CanvanId)
            || null == document.getElementById(option.CurrSpan)
            || null == document.getElementById(option.TotalPageSpan)
        )
            throw "Dom 节点未定义";
        //if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i) == "8.") {
        //    // alert("IE 8.0");
        //    //
        //    option.isie8 = "1";
        //    ie8(option.FirstPageUrl, option.PageFirst, option.ArticleId);
            //}
        if (1==2) {
            // alert("IE 8.0");
            //
            option.isie8 = "1";
            ie8(option.FirstPageUrl, option.PageFirst, option.ArticleId);
        }
        else {
            var canvas = document.getElementById(option.CanvanId);
            //if (typeof window.G_vmlCanvasManager != "undefined") {
            //    canvas = window.G_vmlCanvasManager.initElement(canvas);
            //    cxt = canvas.getContext("2d");
            // } else {
            cxt = canvas.getContext("2d");
            //  }

            // cxt = document.getElementById(option.CanvanId).getContext("2d");

            var img = new Image(1000, 1000);
            //img.src = decodeURIComponent(option.FirstPageUrl) + '&page=' + option.PageFirst + '&lngID=' + option.ArticleId;
            img.src = decodeURIComponent(option.FirstPageUrl1);
            img.onload = function () {
                cxt.drawImage(img, 0, 0);
            }
            img.onerror = function () {
                var errosrt = img.src;
                img.src = decodeURIComponent(option.FirstPageUrl2);//errosrt.replace('.gif', '.jpg');
                img.onerror = null; //控制不要一直跳动
            }
        }

        document.getElementById(option.CurrSpan).innerText = option.PageFirst;
        document.getElementById(option.TotalPageSpan).innerText = option.MaxPage;

        document.getElementById(option.PageUPElmentId).disabled = true;//初次加载 上一页按钮不可用
        document.getElementById(option.FristPageElmentId).disabled = true;// 初次加载 首页按钮可用
        if (option.MaxPage == 1) {
            document.getElementById(option.PageDownElmentId).disabled = true;//初次加载 上一页按钮不可用
            document.getElementById(option.LastPageElementId).disabled = true;// 初次加载 首页按钮可用
        }

        document.getElementById(option.PageUPElmentId).onclick = function () {// 上一页函数绑定
            ///得到上一页的地址 prevUrl
            bindUpPage(option.PageFirst, option);
        }
        document.getElementById(option.PageDownElmentId).onclick = function () {//下一页
            // 下一页 地址 在此处传递 nextUrl
            BindDownPage(option.PageFirst, option);
        }

        document.getElementById(option.FristPageElmentId).onclick = function () { //首页
            firstPage(option);
        }

        document.getElementById(option.LastPageElementId).onclick = function () {//末页
            lastPage(option);
        }

        document.getElementById(option.dowload).onclick = function () {
            if (option.Download == "")
                return;
            var info = option.ArticleId + ',' + option.Download;
            window.checklogin(showDownAfterLogin, [info]);
            //var info = [option.ArticleId, option.Download];
            //window.checklogin(getdowninfo, info);
        }
        //  & page=1 & lngID=44873388
        // console.log(decodeURIComponent(option.FirstPageUrl));
        //var img = new Image(100, 100);
        //img.src = decodeURIComponent(option.FirstPageUrl) + '&page=' + option.PageFirst + '&lngID=' + option.ArticleId;
        //img.onload = function () {
        //    cxt.drawImage(img, 0, 0);
        //}
        return this;
    }
    //  };
    //function getdowninfo(id, dinfo) {
    //    var url = "/Qikan/Article/ArticleDown";
    //    var form = $("<form></form>").attr("action", url).attr("method", "post");
    //    form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", id));
    //    form.append($("<input></input>").attr("type", "hidden").attr("name", "info").attr("value", dinfo));
    //    form.append($("<input></input>").attr("type", "hidden").attr("name", "ts").attr("value", new Date().getTime()));
    //    form.appendTo('body').submit().remove();
    //    layer.msg('下载中', {
    //        icon: 16,
    //        shade: 0.01,
    //        time: 3000
    //    });
    //}
    function Combin() {
        if (!Object.assign) {
            Object.defineProperty(Object, "assign", {
                enumerable: false,
                configurable: true,
                writable: true,
                value: function (target, firstSource) {
                    "use strict";
                    if (target === undefined || target === null)
                        throw new TypeError("Cannot convert first argument to object");
                    var to = Object(target);
                    for (var i = 1; i < arguments.length; i++) {
                        var nextSource = arguments[i];
                        if (nextSource === undefined || nextSource === null) continue;
                        var keysArray = Object.keys(Object(nextSource));
                        for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
                            var nextKey = keysArray[nextIndex];
                            var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
                            if (desc !== undefined && desc.enumerable) to[nextKey] = nextSource[nextKey];
                        }
                    }
                    return to;
                }
            });
        }
    }

    function bindUpPage(PageFirst, option) {
        PageFirst = PageFirst - 1;
        if (PageFirst >= 1) {
            // option.PageFirst = option.PageFirst - 1;
            // PageAction(option.ArticleId, option.PageFirst, "up");
            //  option.PageFirst = option.PageFirst - 1;

            // PageAction(option.ArticleId, PageFirst, "up");//修改前

            var info = [option.ArticleId, PageFirst, "up"];//修改后的
            window.checklogin(PageAction, info);

            //if (option.PageFirst == 1) {
            //    document.getElementById(option.PageUPElmentId).disabled = true;
            //    document.getElementById(option.FristPageElmentId).disabled = true;
            //}

            //if (option.PageFirst < option.MaxPage) {
            //    document.getElementById(option.PageDownElmentId).disabled = false;
            //    document.getElementById(option.LastPageElementId).disabled = false;
            //}
            //   document.getElementById(option.CurrSpan).innerText = option.PageFirst - 1;

        }
    }
    function BindDownPage(PageFirst, option) {
        PageFirst = PageFirst + 1;
        if (PageFirst <= option.MaxPage) {
            // option.PageFirst = option.PageFirst + 1;
            // PageAction(option.ArticleId, option.PageFirst);//修改前的

            var info = [option.ArticleId, PageFirst, "down"];//修改后的
            window.checklogin(PageAction, info);


            //if (option.PageFirst == option.MaxPage) {
            //    document.getElementById(option.PageDownElmentId).disabled = true; //当前 页 是最后一页 下一页不可用
            //    document.getElementById(option.LastPageElementId).disabled = true;
            //}
            //if (option.PageFirst > 1) {
            //    document.getElementById(option.PageUPElmentId).disabled = false;//当前页不是首页  上一页按钮可用
            //    document.getElementById(option.FristPageElmentId).disabled = false;///
            //}

            // document.getElementById(option.CurrSpan).innerText = option.PageFirst + 1;

        }
    }
    function ie8(a, b, c) {
        var nodeElement = document.createElement("canvas");

        nodeElement.width = 1000;
        nodeElement.height = 1000;
        $("#imgcontent").empty();
        $("#imgcontent").append(nodeElement);

        window.G_vmlCanvasManager.initElement(nodeElement);// G_vmlCanvasManager_.initElement(nodeElement);
        //  $("#imgcontent").empty();
        //  document.getElementById("imgcontent").removeChild("myCanvas");
        //  document.appendChild(nodeElement);


        //if ($.browser.msie) {
        //    canvas = window.G_vmlCanvasManager.initElement(canvas);
        //}

        var cxtie8 = nodeElement.getContext("2d");

        var img = new Image(1000, 1000);

        img.onload = function () {
            cxtie8.drawImage(img, 0, 0);
        }
        img.src = decodeURIComponent(a) + '&page=' + b + '&lngID=' + c;
    }
    function firstPage(option) {
        // var img = new Image(100, 100);
        // option.PageFirst = 1;
        var index = option.PageFirst / option.PageFirst;
        //这里得到 图片地址

        //img.src = option.PageFirst / option.PageFirst + ".png";
        //img.onload = function () {
        //    cxt.drawImage(img, 0, 0);
        //};
        // PageAction(option.ArticleId, index, "first");//翻页

        var info = [option.ArticleId, index, "last"];
        window.checklogin(PageAction, info);

        document.getElementById(option.CurrSpan).innerText = option.PageFirst / option.PageFirst;

        //document.getElementById(option.LastPageElementId).disabled = false;
        //document.getElementById(option.PageDownElmentId).disabled = false;

        //document.getElementById(option.FristPageElmentId).disabled = true;
        //document.getElementById(option.PageUPElmentId).disabled = true; //当前页 是最后一页则 下一页不可用
    }
    function lastPage(option) {
        // option.PageFirst = option.MaxPage;

        var index = option.MaxPage;
        //  PageAction(option.ArticleId, index, "last");//翻页
        var info = [option.ArticleId, index, "last"];
        window.checklogin(PageAction, info);
        document.getElementById(option.CurrSpan).innerText = option.MaxPage;

        //document.getElementById(option.FristPageElmentId).disabled = false;
        //document.getElementById(option.PageUPElmentId).disabled = false;
        //document.getElementById(option.LastPageElementId).disabled = true;
        //document.getElementById(option.PageDownElmentId).disabled = true; //当前 页 是最后一页 下一页不可用

    }
    //这里确定了插件的名称
    // this.CJPlugin = InitPlugin_api;
    function changeButton() {
        if (option.PageFirst == 1) {
            document.getElementById(option.PageUPElmentId).disabled = true;
            document.getElementById(option.FristPageElmentId).disabled = true;
            document.getElementById(option.PageDownElmentId).disabled = false;
            document.getElementById(option.LastPageElementId).disabled = false;
        }

        if (option.PageFirst != 1 && option.PageFirst < option.MaxPage) {

            document.getElementById(option.PageUPElmentId).disabled = false;
            document.getElementById(option.FristPageElmentId).disabled = false;
            document.getElementById(option.PageDownElmentId).disabled = false;
            document.getElementById(option.LastPageElementId).disabled = false;
        }
        if (option.PageFirst == option.MaxPage) {
            document.getElementById(option.PageUPElmentId).disabled = false;
            document.getElementById(option.FristPageElmentId).disabled = false;
            document.getElementById(option.PageDownElmentId).disabled = true; //当前 页 是最后一页 下一页不可用
            document.getElementById(option.LastPageElementId).disabled = true;
        }
        if (option.PageFirst > option.MaxPage) {
            document.getElementById(option.PageUPElmentId).disabled = false;
            document.getElementById(option.FristPageElmentId).disabled = false;
            document.getElementById(option.PageDownElmentId).disabled = true; //当前 页 是最后一页 下一页不可用
            document.getElementById(option.LastPageElementId).disabled = true;
            document.getElementById(option.CurrSpan).innerText = option.MaxPage;

        }
    }
    function PageAction(id, index, action) {
        if (id == "" || index == "")
            return;
        $.ajax({
            url: '/Qikan/Article/ArticlePageInfo',
            data: { ArticleId: id, pageIndex: parseInt(index) },
            async: true,
            type: 'post',
            dataType: 'json',
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (succ) {
                if (succ.Result==1) {
                    if (option.isie8 == "1") {
                        ie8(succ.RetValue.Pagefirst, option.PageFirst, option.ArticleId)
                    }
                    else {
                        var img = new Image(1000, 1000);
                        img.src = decodeURIComponent(succ.RetValue.Pagefirst) + '&page=' + option.PageFirst + '&lngID=' + option.ArticleId;
                        img.onload = function () {
                            cxt.drawImage(img, 0, 0);
                        }
                    }
                    option.PageFirst = parseInt(index);
                    document.getElementById(option.CurrSpan).innerText = option.PageFirst;//
                    $('html,body').animate({ scrollTop: 0 }, 1500);
                    changeButton();
                }
                else {
                    // $('#login-btn').trigger('click');
                    //layer.open({
                    //    id: 1,
                    //    type: 1,
                    //    offset: '100px'
                    //    //,title: false
                    //    , skin: 'layui-layer-rim login'
                    //    , title: ' '
                    //    , area: ['350px', '300px']
                    //    , anim: 0
                    //    , move: false
                    //    , resize: false
                    //    , content: $('#login')
                    //    //,closeBtn: 0
                    //    , zIndex: 2 //重点1
                    //    , success: function (layero) {
                    //        // layer.setTop(layero); //重点2

                    //    }
                    //});
                    //矫正一边按钮的 状态 页码 状态
                    //layer.msg(succ.Msg+'aa');//没有权限
                    var url = '/';
                    window.open(url, "_blank");
                    //  document.getElementById(option.CurrSpan).innerText = option.PageFirst;
                }
            },
            error: function () {
                layer.alert("请求错误", {
                    title: '提示！'
                });

            }
        })
    }
    return {
        CJPlugin: InitPlugin_api
    };
})();
