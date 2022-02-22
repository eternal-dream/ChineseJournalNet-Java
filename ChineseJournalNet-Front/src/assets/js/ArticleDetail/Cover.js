var pluginCover = (function () {
    var mf = {
        gch: "",//参数
        id: "",
        name: "",//参数
        fmpicxml: "",// 下拉框的值
        fdpicxml: "",
        mpicxml: "",
        bigPath: "http://image1.cqvip.com/vip/qkhclearimg/",
        smallPath: "http://image1.cqvip.com/vip/qk/",
    }
    function Init(defaultOption) {//初始化
        if (undefined == defaultOption)
            return;
        // Combin();
        $.extend(mf, defaultOption);
        // Object.assign(mf, defaultOption);
        $.ajax({
            type: "get",
            url: "/Journal/InitSelect",
            dataType: "json",
            data: { gch: mf.gch, id: mf.id },
            success: function (json) {
                $($.parseXML(json.fmpicxml)).find("years").each(function () {
                    var year = $(this).attr("value");
                    $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
                });
                mf.fmpicxml = $.parseXML(json.fmpicxml);
                mf.mpicxml = $.parseXML(json.mpicxml);
                mf.fdpicxml = $.parseXML(json.fdpicxml);
                searchnum(mf.fmpicxml, $("#dropJournalYear").val());

                //layui.use('form', function () {
                //    var form = layui.form();//高版本建议把括号去掉，有的低版本，需要加()
                //    form.render('select');
                //});

                layui.use(['element', 'layer', 'form'], function () {
                    var form = layui.form
                    form.render('select');
                });

                //首次加载 显示
                var imgType = $("#dropCoverType").val(), year = $("#dropJournalYear").val(), num = $("#dropJournalNum").val();
                $.get("/Qikan/Journal/GetImageList", { "gch": mf.gch, "year": year, "num": num, "imgtype": imgType }, function (html) {
                    $("#imglist ul").html(html);
                    var img = $("#imglist ul").find("li.current").find("img"), src = img.attr("src"), cli = src.replace(mf.smallPath, "");
                    $("#bigimg").attr("src", mf.bigPath + cli);
                });

            }
        });

    }
    layui.use(['element', 'layer', 'form'], function () {
        var form = layui.form
        form.on('select(dropCoverType)', function (data) {
            var val = parseInt(data.value);
            $("#dropJournalYear").empty();
            $("#dropJournalNum").empty();
            if (val == 0) {
                $(mf.fmpicxml).find("years").each(function () {
                    var year = $(this).attr("value");
                    $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
                });
                searchnum(mf.fmpicxml, $("#dropJournalYear").val());
            } else if (val == 1) {
                $(mf.fdpicxml).find("years").each(function () {
                    var year = $(this).attr("value");
                    $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
                });
                searchnum(mf.fdpicxml, $("#dropJournalYear").val());
            } else if (val == 2) {
                $(mf.mpicxml).find("years").each(function () {
                    var year = $(this).attr("value");
                    $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
                });
                searchnum(mf.mpicxml, $("#dropJournalYear").val());
            }
            form.render('select');
        });
        form.on('select(dropJournalYear)', function (data) {
            var val = parseInt(data.value);
            $("#dropJournalNum").empty();
            searchnum(mf.fmpicxml, val, true);
            form.render('select');
        });
    });
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
    //$("#dropCoverType").change(function () {
    //    // var val = $(this).attr("value");
    //    var val = parseInt($(this).val());
    //    $("#dropJournalYear").empty();
    //    if (val == 0) {
    //        $(mf.fmpicxml).find("years").each(function () {
    //            var year = $(this).attr("value");
    //            $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
    //        });
    //        searchnum(mf.fmpicxml, $("#dropJournalYear").val());
    //    } else if (val == 1) {
    //        $(mf.fdpicxml).find("years").each(function () {
    //            var year = $(this).attr("value");
    //            $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
    //        });
    //        searchnum(mf.fdpicxml, $("#dropJournalYear").val());
    //    } else if (val == 2) {
    //        $(mf.mpicxml).find("years").each(function () {
    //            var year = $(this).attr("value");
    //            $("#dropJournalYear").append("<option value='" + year + "'>" + year + "</option>");
    //        });
    //        searchnum(mf.mpicxml, $("#dropJournalYear").val());
    //    }

    //    //layui.use(['element', 'layer', 'form'], function () {
    //    //    var form = layui.form;
    //    //    form.render('select');
    //    //})
    //});

    //$("#dropJournalYear").change(function () {
    //    searchnum(mf.fmpicxml, $(this).val(), true);
    //    layui.use(['element', 'layer', 'form'], function () {
    //        var form = layui.form;
    //        form.render('select');
    //    })
    //});


    ///向右移动
    $("#btnRight").on("click", function () {
        var cli = $("#imglist").find("li.current"), index = cli.index(), nli = cli.next(), num = cli.nextAll().length, max = $("#imglist ul li").length;
        if (num == 0) {//最后一个
            return false;
        }
        if (max - index > 7) {
            var _left = parseInt($("#imglist ul").css("margin-left"));
            $("#imglist ul").css("margin-left", _left + (-130) + "px");
        }
        var img = nli.find("img"), error = img.attr("iserror"), src = img.attr("src"), filename = src.replace(mf.smallPath, "");
        $("#bigimg").attr("src", mf.bigPath + filename);
        if (error == "true") {
            $(".save").hide();
            $(".print").hide();
        } else {
            $(".save").show();
            $(".print").show();
        }
        cli.removeClass("current");
        nli.addClass("current");
    });

    ///向左移动
    $("#btnLeft").on("click", function () {
        var cli = $("#imglist").find("li.current"), nli = cli.prev(), num = cli.prevAll().length;
        if (num == 0) {//第一个
            return false;
        }
        var _left = parseInt($("#imglist ul").css("margin-left"));
        if (_left != 0) {
            $("#imglist ul").css("margin-left", _left + 130 + "px");
        }
        var img = nli.find("img"), error = img.attr("iserror"), src = img.attr("src"), filename = src.replace(mf.smallPath, "");
        $("#bigimg").attr("src", mf.bigPath + filename);
        if (error == "true") {
            $(".save").hide();
            $(".print").hide();
        } else {
            $(".save").show();
            $(".print").show();
        }
        cli.removeClass("current");
        nli.addClass("current");
    });

    $("#btnFindImg").on("click", function () {
        //  var imgType = $("#dropCoverType").attr("value"), year = $("#dropJournalYear").attr("value"), num = $("#dropJournalNum").attr("value");
        var imgType = $("#dropCoverType").val(), year = $("#dropJournalYear").val(), num = $("#dropJournalNum").val(), gch = $("#dropJournalNum").find("option:selected").attr("data-gch");

        $.ajax({
            url: "/Qikan/Journal/GetImageList",
            type: "post",
            async: true,
            dataType: "json",
            data: { "gch": gch, "year": year, "num": num, "imgtype": imgType },
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (html) {
                $("#imglist ul").html(html);
                var src = $("#imglist ul").find("li.current").find("img").attr("src");
                if (src) {
                    cli = src.replace(mf.smallPath, "");
                    $("#bigimg").attr("src", mf.bigPath + cli);
                    $(".save").show();
                    $(".print").show();
                } else {
                    $("#bigimg").attr("src", "");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/
            }
        })

        //$.get("/Qikan/Journal/GetImageList",
        //    { "gch": mf.gch, "year": year, "num": num, "imgtype": imgType },           
        //    function (html) {
        //        $("#imglist ul").html(html);
        //        var src = $("#imglist ul").find("li.current").find("img").attr("src");
        //        if (src) {
        //            cli = src.replace(mf.smallPath, "");
        //            $("#bigimg").attr("src", mf.bigPath + cli);
        //            $(".save").show();
        //            $(".print").show();
        //        } else {
        //            $("#bigimg").attr("src", "");
        //        }
        //    });
    });
    ///点击小图显示对应大图
    $("#imglist ul").on("click", "li a", function () {
        var img = $(this).find("img"),
            error = img.attr("iserror"),
            src = img.attr("src"),
            cli = src.replace(mf.smallPath, "");
        $("#imglist ul li").removeClass("current");
        $(this).parent("li").addClass("current");
        //if (src == "../../dist/images/defaultberror.jpg")/// 这张图片没有的时候显示默认图片
        //    $("#bigimg").attr("src", "../../dist/images/defaultberror.jpg");
        //else
        {
            $("#bigimg").attr("src", mf.bigPath + cli);
            if (error == "true") {
                $(".save").hide();
                $(".print").hide();
            } else {
                $(".save").show();
                $(".print").show();
            }
        }
    });

    ///保存图片
    $(".save").on("click", function () {
        savepic();
    });
    function savepic() {
        var path = $("#bigimg").attr("src");
        if (document.all.a1 == undefined) {
            objIframe = document.createElement("a");
            document.body.appendChild(objIframe);
            objIframe.outerHTML = "<a name='a1' id='a1' style='width:400px;hieght:300px' target='_blank' href='" + path + "' download='封面.jpg'></a>";
            // re = setTimeout("savepic()", 1);
        }
        else {
            // clearTimeout(re);
            document.getElementById('a1').href = $("#bigimg").attr("src");
        }
        document.all.a1.click();
    };
    ///打印图片
    $(".print").on("click", function () {
        var url = location.href;
        var bdhtml = window.document.body.innerHTML;//获取当前页的html代码 
        var sprnstr = "<!--startprint-->";//设置打印开始区域 
        var eprnstr = "<!--endprint-->";//设置打印结束区域 
        var prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 17); //从开始代码向后取html
        var prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html 
        window.document.body.innerHTML = prnhtml;
        window.print();
        location.href = url;
        //window.document.body.innerHTML=bdhtml; 
    });

    ///图片放大和缩小
    $("#bigimg").on("click", function () {
        if ($(this).hasClass("zoom-in")) {
            $(this).addClass("zoom-out");
            $(this).removeClass("zoom-in");
            $(this).removeAttr("height");
        } else {
            $(this).addClass("zoom-in");
            $(this).removeClass("zoom-out");
            $(this).attr("height", "100%");
        }
    });


    function searchnum(xml, value, isChange) {
        var stroption = "", ulli = "", seledvalue = "", showvalue = "";
        var len = $(xml).find("years[value='" + value + "']>num").length;
        $(xml).find("years[value='" + value + "']>num").each(function (i, item) {
            var num = $(this).text(), numValue = "";
            var gch = $(this).attr('gch');
            if (num.length == 1) {
                numValue = "00" + num;
            } else {
                numValue = "0" + num;
            }
            if (i == 0) {
                seledvalue = numValue;
                showvalue = num;
            }
            if (i == len - 1) {
                stroption += "<option selected data-gch='" + gch + "' value='" + numValue + "'>" + num + "</option>";
            }
            else
                stroption += "<option data-gch='" + gch + "' value='" + numValue + "'>" + num + "</option>";
            // ulli += "<li><a class='' rel='" + numValue + "' href='javascript:void(0);'>" + num + "</a></li>";
        });
        $("#dropJournalNum").append(stroption);
        //if (isChange) {
        //    var div = $("#dropJournalNum").next("div.easy-select-box");
        //    $("#dropJournalNum").attr("value", seledvalue);
        //    div.find("a.disp").html(showvalue);
        //    div.find("div.sel-box ul").html(ulli);
        //}
    }


    function imgerror(img) {
        $(img).attr("src", "../../dist/images/defaultserror.jpg");
        $(img).attr("iserror", "true");
        img.onerror = null;
    }
    function imgberror(img) {
        $(img).attr("src", "../../dist/images/defaultberror.jpg");
        $(img).attr("iserror", "true");
        $(".save").hide();
        $(".print").hide();
        img.onerror = null;
    }
    return {
        CJPlugin: Init,
        imgebError: imgerror,
        imgeBrror: imgberror
    };
})()