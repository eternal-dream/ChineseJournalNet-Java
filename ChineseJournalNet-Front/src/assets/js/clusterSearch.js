layui.use(['element', 'form', 'layer'], function () {
    var form = layui.form;
    var element = layui.element;
    var $ = layui.$;
    var layer = layui.layer;

    //element.on('collapse(leftcluser)', function (data) {
    //    var dHeight = 170;
    //    var aHeight = data.content.children('.cluster-term').height() + data.content.children('.cluster-list').height();
    //    if (!data.show && aHeight > dHeight) {
    //        data.content.css("max-height", aHeight);
    //    }
    //    else {
    //        data.content.css("max-height", dHeight);
    //    }
    //});
    element.on('collapse(leftcluser)', function (data) {
        var dHeight = 170;
        var aHeight = data.content.children('.cluster-term').height() + data.content.children('.cluster-list').height() + data.content.children('.cluster-year-sel').height();
        if (data.content.hasClass("extend-show")) {
            data.content.css("max-height", aHeight);
            data.content.removeClass("extend-show");
        }
        else {
            var maxHeight = data.content.css("max-height");
            maxHeight = maxHeight.replace("px", "");

            if (parseInt(maxHeight) > 170) {
                data.content.css("max-height", dHeight);
            }
            else {
                data.content.css("max-height", aHeight);
            }
        }
    });

    $(document).on("click", "#classCluster li i,#areaCluster li i,#organCluster li i,#rangeCluster li i", function () {
        var e = arguments.callee.caller.arguments[0] || window.event;
        var nodeName = e.target.nodeName.toLowerCase();
        if (nodeName !== "i")
            return false;
        var jqSelf = $(this);
        var jqChildrenUl = jqSelf.parent().parent().children("ul");
        var jqSelectUl = jqSelf.parent().parent().parent().prev("ul");
        if (jqChildrenUl.length) {

            if (jqSelf.hasClass("icon-plus")) {
                jqChildrenUl.show();
                jqSelf.removeClass("icon-plus").addClass("icon-minus");
            } else {
                jqChildrenUl.hide();
                jqSelf.removeClass("icon-minus").addClass("icon-plus");
            }

            jqChildrenUl.children().each(function () {
                if (jqSelectUl.length) {
                    var flag = false;
                    var dataSearch = $(this).children("a").data("search");

                    jqSelectUl.children().each(function () {
                        if (dataSearch == $(this).children("a").data("search")) {
                            flag = true;
                            return;
                        }
                    });

                    if (flag) {
                        $(this).remove();
                    }
                }

            });



        }
        else {
            var clusterValue = jqSelf.data("item");
            var sType = "title_info";
            var searchParamModel = getSearchParamModel();
            var objectType = searchParamModel.ObjectType;
            switch (objectType) {
                case 1:
                    sType = "title_info";
                    break;
                case 7:
                    sType = "media_info";
                    break;
                case 3:
                    sType = "writer_info";
                    break;
                case 6:
                    sType = "organ_info";
                    break;
                default:
                    sType = "title_info";
                    break;
            }

            var searchParamModel = getSearchParamModel();

            jqSelf.removeClass("icon-plus").addClass("icon-loading");

            $.ajax({
                url: "/Search/ClusterExtend",
                type: "post",
                dataType: "html",
                data: { "searchParamModel": JSON.stringify(searchParamModel), clusterValue: clusterValue, sType: sType },
                success: function (info) {
                    jqSelf.parent().after(info);
                    jqSelf.removeClass("icon-loading").addClass("icon-minus");
                    if (jqSelf.parents(".layui-colla-content").css("max-height") != undefined) {
                        var maxHeight = jqSelf.parents(".layui-colla-content").css("max-height");
                        maxHeight = maxHeight.replace("px", "");
                        var aHeight = jqSelf.parents(".layui-colla-content").children('.cluster-term').height() + jqSelf.parents(".layui-colla-content").children('.cluster-list').height();
                        if (aHeight > maxHeight) {
                            if (!jqSelf.parents(".layui-colla-content").hasClass("extend-show")) {
                                jqSelf.parents(".layui-colla-content").addClass("extend-show");
                            }
                            jqSelf.parents(".layui-colla-content").prev().click();
                        }

                    }

                    jqChildrenUl = jqSelf.parent().parent().children("ul");
                    jqChildrenUl.children().each(function () {
                        if (jqSelectUl.length) {
                            var flag = false;
                            var dataSearch = $(this).children("a").data("search");
                            jqSelectUl.children().each(function () {
                                if (dataSearch == $(this).children(".cluster-close").data("search")) {
                                    flag = true;
                                    return;
                                }
                            });

                            if (flag) {
                                $(this).remove();
                            }
                        }

                    });

                    $(".layui-colla-item ul li a ul li a").on("click", function (event) {

                        var searchtype = $("#searchType").val();//判断检索类型
                        if (searchtype == "advance") {
                            var guid = "";
                            $(this).parents().each(function () {
                                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                                    guid = $(this).data("guid");
                                    return false;
                                }
                            });

                            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
                            searchParamModel = JSON.parse(strsearchParamModelJson);
                        }

                        var oldClusterFilter = searchParamModel.ClusterFilter;

                        clusterSearch($(this).data("search"), oldClusterFilter, 1, $(this));

                        //阻止事件冒泡
                        event.stopPropagation();
                    });

                    form.render();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    /*错误信息处理*/
                }
            });
        }
    });


    //$(document).off("click", ".cluster-close i").on("click", ".cluster-close i", function () {

    //    var e = arguments.callee.caller.arguments[0] || window.event;
    //    layui.stope(e);

    //    var clusterFilter = $(this).parent().data("search");
    //    var searchParamModel = getSearchParamModel();
    //    var searchtype = $("#searchType").val();//判断检索类型
    //    if (searchtype == "advance") {
    //        var guid = "";
    //        $(this).parents().each(function () {
    //            if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
    //                guid = $(this).data("guid");
    //                return false;
    //            }
    //        });

    //        var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
    //        searchParamModel = JSON.parse(strsearchParamModelJson);
    //    }
    //    var oldClusterFilter = searchParamModel.ClusterFilter;

    //    var newClusterFilter = "";
    //    if (oldClusterFilter.indexOf("[_]" + clusterFilter) >= 0) {
    //        newClusterFilter = oldClusterFilter.replace("[_]" + clusterFilter, "");
    //    }
    //    else if (oldClusterFilter.indexOf(clusterFilter + "[_]") >= 0) {
    //        newClusterFilter = oldClusterFilter.replace(clusterFilter + "[_]", "");
    //    }
    //    else {
    //        newClusterFilter = oldClusterFilter.replace(clusterFilter, "");
    //    }

    //    clusterSearch(newClusterFilter, oldClusterFilter, 0, $(this));
    //});


    $(document).off("click", ".cluster-close").on("click", ".cluster-close", function () {
        var e = arguments.callee.caller.arguments[0] || window.event;
        layui.stope(e);

        var clusterFilter = $(this).data("search");
        var searchParamModel = getSearchParamModel();
        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {
            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            searchParamModel = JSON.parse(strsearchParamModelJson);
        }
        var oldClusterFilter = searchParamModel.ClusterFilter;

        var newClusterFilter = "";
        if (oldClusterFilter.indexOf("[_]" + clusterFilter) >= 0) {
            newClusterFilter = oldClusterFilter.replace("[_]" + clusterFilter, "");
        }
        else if (oldClusterFilter.indexOf(clusterFilter + "[_]") >= 0) {
            newClusterFilter = oldClusterFilter.replace(clusterFilter + "[_]", "");
        }
        else {
            newClusterFilter = oldClusterFilter.replace(clusterFilter, "");
        }

        clusterSearch(newClusterFilter, oldClusterFilter, 0, $(this));
    })


    $(document).on("click", ".layui-colla-item ul li a", function () {
        var e = arguments.callee.caller.arguments[0] || window.event;
        var nodeName = e.target.nodeName.toLowerCase();
        if (nodeName !== "a")
        {
            return false;
        }

        if (!$(this).hasClass("cluster-xg"))
        {
            var searchParamModel = getSearchParamModel();
            var searchtype = $("#searchType").val();//判断检索类型
            if (searchtype == "advance") {
                var guid = "";
                $(this).parents().each(function () {
                    if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                        guid = $(this).data("guid");
                        return false;
                    }
                });

                var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
                searchParamModel = JSON.parse(strsearchParamModelJson);
            }
            var oldClusterFilter = searchParamModel.ClusterFilter;

            clusterSearch($(this).data("search"), oldClusterFilter, 1, $(this));
        }
        else
        {
            var searchParamModel = getSearchParamModel();
            searchParamModel.ObjectSearchType = $(this).data("search");
            search(searchParamModel);
        }
        
    });

    $(document).on("click", "#leftSearch", function () {


        var leftIdentifier = $("select[name='leftIdentifier']").val();
        var leftSearchKeywords = $("input[name='leftSearchKeywords']").val();
        var clusterFilter = leftIdentifier + "=" + leftSearchKeywords;
        var searchParamModel = getSearchParamModel();

        var searchtype = $("#searchType").val();//判断检索类型

        if (searchtype == "advance") {
            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            leftIdentifier = $("div[data-guid=" + guid + "]").find("select[name='leftIdentifier']").val();
            leftSearchKeywords = $("div[data-guid=" + guid + "]").find("input[name='leftSearchKeywords']").val();
            clusterFilter = leftIdentifier + "=" + leftSearchKeywords;

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            searchParamModel = JSON.parse(strsearchParamModelJson);
        }

        var oldClusterFilter = searchParamModel.ClusterFilter;

        if (leftSearchKeywords == "") {
            layer.msg("请输入检索词！");
            return false;
        }

        clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));
    });

    $(document).on("click", "#ObjectLeftSearch", function () {

        var leftIdentifier = $("select[name='leftIdentifier']").val();
        var leftSearchKeywords = $("input[name='leftSearchKeywords']").val();

        var clusterFilter = leftIdentifier + "=" + leftSearchKeywords;
        var searchParamModel = getSearchParamModel();
        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {
            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            searchParamModel = JSON.parse(strsearchParamModelJson);
        }
        var oldClusterFilter = searchParamModel.ClusterFilter;

        if (leftSearchKeywords == "") {
            layer.msg("请输入检索词！");
            return false;
        }

        clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));
    });

    $(document).on("click", "#objectSearch", function () {
        var urlparam = "";

        var leftIdentifier = $("select[name='leftIdentifier']").val();
        var leftSearchKeywords = $("input[name='leftSearchKeywords']").val();

        if (leftSearchKeywords != "") {
            urlparam = leftIdentifier + "=" + leftSearchKeywords;
        }

        var isLog = 1;
        var objectType = $(this).data("objecttype");

        $.StandardPost('/Qikan/Search/Index', { key: encodeURIComponent(urlparam), objectType: objectType, isLog: isLog });
    });

    $(document).on("keyup", "#leftSearchKeywords", function (e) {
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $('#objectSearch').trigger("click");
        }
    });


    $(document).on("click", "#leftSearchForNot", function () {

        var leftIdentifier = $("select[name='leftIdentifier']").val();
        var leftSearchKeywords = $("input[name='leftSearchKeywords']").val();

        var clusterFilter = leftIdentifier + "=" + " NOT " + leftSearchKeywords;
        var searchParamModel = getSearchParamModel();
        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {
            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            leftIdentifier = $("div[data-guid=" + guid + "]").find("select[name='leftIdentifier']").val();
            leftSearchKeywords = $("div[data-guid=" + guid + "]").find("input[name='leftSearchKeywords']").val();

            clusterFilter = leftIdentifier + "=" + " NOT " + leftSearchKeywords;

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            searchParamModel = JSON.parse(strsearchParamModelJson);
        }
        var oldClusterFilter = searchParamModel.ClusterFilter;
        if (leftSearchKeywords == "") {
            layer.msg("请输入检索词！");
            return false;
        }
        clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));

    });

    //阻止默认事件
    window.goStop = function (event) {
        if (event.preventDefault) {
            event.preventDefault();
        } else {
            event.returnValue = false;
        }
    }

    $(document).off("click", ".journal-guid .list-abc a").on("click", ".journal-guid .list-abc a", function () {

        var searchParamModel = getSearchParamModel();
        var oldClusterFilter = searchParamModel.ClusterFilter;
        var clusterFilter = $(this).data("search");

        clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));

        var event = arguments.callee.caller.arguments[0] || window.event;

        goStop(event);
    })

    $(document).off("click", ".journal-guid .journal-class-list a").on("click", ".journal-guid .journal-class-list a", function () {
        var searchParamModel = getSearchParamModel();
        var oldClusterFilter = searchParamModel.ClusterFilter;
        var clusterFilter = $(this).data("search");

        clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));

        var event = arguments.callee.caller.arguments[0] || window.event;

        goStop(event);
    })

    //$(document).off("click", ".cluster-year .layui-btn-primary").on("click", ".cluster-year .layui-btn-primary", function () {
    //    var clusterFilter = "";
    //    var searchParamModel = getSearchParamModel();
    //    var searchtype = $("#searchType").val();//判断检索类型
    //    if (searchtype == "advance") {
    //        var guid = "";
    //        $(this).parents().each(function () {
    //            if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
    //                guid = $(this).data("guid");
    //                return false;
    //            }
    //        });

    //        var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
    //        searchParamModel = JSON.parse(strsearchParamModelJson);
    //    }
    //    var oldClusterFilter = searchParamModel.ClusterFilter;

    //    var timeStr = getClusterYear();
    //    if (timeStr == "false") {
    //        layer.msg('开始时间不能大于结束时间！');
    //        return false;
    //    }
    //    else if (timeStr.indexOf("-") >= 0)//年份区间
    //    {
    //        var yearArray = new Array(); //定义一数组 
    //        yearArray = timeStr.split("-"); //字符分割

    //        searchParamModel.BeginYear = yearArray[0];
    //        searchParamModel.EndYear = yearArray[1];

    //    }

    //    clusterSearch(clusterFilter, oldClusterFilter, 1, $(this));

    //});


    window.getClusterYear = function () {
        var starttime, endtime;
        starttime = $("select[name=cluster_beginYear]").val();
        endtime = $("select[name=cluster_endYear]").val();

        //var d = new Date(), nowYear = d.getFullYear();
        //if (starttime == 1900 && endtime == nowYear) {
        //    str = "";
        //} else {
        //    if (endtime < starttime) {
        //        return str = "false";
        //    }
        //    str = starttime + "-" + endtime;
        //}

        if (endtime < starttime) {
            return str = "false";
        }
        str = starttime + "-" + endtime;

        return str;
    }

    //searchType: 0 减少检索条件，以传入条件为准，1增加检索条件需在原来基础上拼接
    function clusterSearch(clusterFilter, oldClusterFilter, clusterSearchType, othis) {
        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "normal") {
            var searchParamModel = getSearchParamModel();

            if (clusterSearchType == 1) {
                if (oldClusterFilter == clusterFilter || oldClusterFilter == "" || clusterFilter == "") {
                    searchParamModel.ClusterFilter = clusterFilter;
                }
                else if (oldClusterFilter.indexOf(clusterFilter) >= 0) {
                    searchParamModel.ClusterFilter = oldClusterFilter;
                }
                else {
                    searchParamModel.ClusterFilter = oldClusterFilter + "[_]" + clusterFilter;
                }
            }
            else {
                searchParamModel.ClusterFilter = clusterFilter;
            }

            searchParamModel.SType = "";
            searchParamModel.StrIds = "";
            searchParamModel.PageNum = 1;
            if ($(othis).attr("data-ClusterUseType") == "Article")//期刊导航学科分类列表中有此标记
            {
                searchParamModel.ClusterUseType = "Article";
            }
            search(searchParamModel);
        }
        else if (searchtype == "advance") {
            var guid = "";
            othis.parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            var searchParamModel = JSON.parse(strsearchParamModelJson);
            if (clusterSearchType == 1) {
                if (oldClusterFilter == clusterFilter || oldClusterFilter == "" || clusterFilter == "") {
                    searchParamModel.ClusterFilter = clusterFilter;
                }
                else {
                    searchParamModel.ClusterFilter = oldClusterFilter + "[_]" + clusterFilter;
                }
            }
            else {
                searchParamModel.ClusterFilter = clusterFilter;
            }

            searchParamModel.SType = "";
            searchParamModel.StrIds = "";
            searchParamModel.PageNum = 1;
            if ($(othis).attr("data-ClusterUseType") == "Article") { //期刊导航学科分类列表中有此标记
                searchParamModel.ClusterUseType = "Article";
            }
            advSearch(searchParamModel, guid);
        }
    }

});