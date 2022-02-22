/// <reference path="public/checkRequest.js" />
/// <reference path="public/checkRequest.js" />
layui.use(['form', 'layer', 'jquery', 'cookie', 'element'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var $ = layui.jquery;
    var cookie = layui.cookie;
    var element = layui.element;

    window.S4 = function () {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
    window.createGuid = function () {
        return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
    }

    window.addSelectArticleSession = function (ids) {
        var slectStr = "";
        var selectArr = new Array();
        if (ids != "") {
            var idArr = new Array(); //定义一数组 
            idArr = ids.split(","); //字符分割 

            slectStr = getSessionSelectArticle();

            if (slectStr == "")//还没选择任何数据的情况
            {
                for (var i = 0; i < idArr.length; i++) {
                    selectArr.push(idArr[i].toString());
                }
            }
            else {
                var cookieArr = new Array();
                cookieArr = slectStr.split(",");
                for (var i = 0; i < idArr.length; i++) {
                    if ($.inArray(idArr[i].toString(), cookieArr) < 0) {
                        selectArr.push(idArr[i].toString());
                    }
                }
                for (var i = 0; i < cookieArr.length; i++) {
                    selectArr.push(cookieArr[i].toString());
                }
            }

            if (selectArr.length >500)
            {
                layer.msg("最多只能选择500条！");
                for (var i = 0; i < idArr.length; i++) {
                    $("#articlelist").find("input[type=checkbox]").each(function () {
                        var id = $(this).data("id");
                        if ($.inArray(id.toString(), idArr) >= 0) {
                            $(this).prop("checked", false);
                        }
                    });
                }
                
                $('input[name=selectArticleAll]').prop("checked", false);

                form.render();
                return;
            }

            setSessionSelectArticle(selectArr.join(','));
        }
    }

    window.delSelectArticleSession = function (ids) {
        var slectStr = "";
        var selectArr = new Array();

        if (ids != "") {
            var idArr = new Array(); //定义一数组 
            idArr = ids.split(","); //字符分割 

            slectStr = getSessionSelectArticle();

            if (slectStr != "") {
                var cookieArr = new Array();
                var liveArr = new Array();
                cookieArr = slectStr.split(",");

                for (var j = 0; j < cookieArr.length; j++) {
                    if ($.inArray(cookieArr[j].toString(), idArr) < 0) {
                        liveArr.push(cookieArr[j].toString());
                    }
                }

                setSessionSelectArticle(liveArr.join(','));
            }
        }
    }

    form.on('submit(btn-search)', function (data) {

        $(this).parent().addClass("searching");
        var urlparam = "";

        if (data.field.searchKeywords != "") {
            urlparam = data.field.identifier + "=" + data.field.searchKeywords;
        }
        //检索操作需要记录检索历史
        var isNoteHistory = 1;

        //是否记录检索日志
        var isLog = 1;

        $.StandardPost('/Qikan/Search/Index', { key: encodeURIComponent(urlparam), isNoteHistory: isNoteHistory, isLog: isLog, indexKey: data.field.searchKeywords, indexIdentifier: data.field.identifier });



    });

    //自动补全方法
    window.bindAutoComplete = function () {
        $("#searchKeywords").AutoComplete({
            'data': "/Qikan/Search/AutoComplate",
            'timeout': 3000, //超时时间设置，单位毫秒
            "ajaxParams": function () {
                var type = "articles";
                return { "objType": type.toLowerCase(), "keyword": encodeURIComponent($("input[name='searchKeywords']").val()) };
            },
            'ajaxDataType': 'json',
            'width': 'auto', //490
            'itemHeight': 25,
            'leftOffset': -6,
            'maxItems': 30,
            'maxHeight': 232,
            'beforeLoadDataHandler': function (keyword) {
                if (keyword.length >= 2)
                {
                    $(".ac").show();
                    return true;
                }
                else
                {
                    $(".ac").hide();
                    return false;
                }
                
            },
            'onerror': function (msg) { alert(msg); },
            "afterSelectedHandler": function () {
                $("#btnSearch").parent().addClass("searching");
                var urlparam = "";
                var identifier = $("select[name='identifier']").val();
                var searchKeywords = $("input[name='searchKeywords']").val();

                if (searchKeywords != "") {
                    urlparam = identifier + "=" + searchKeywords;
                }

                //检索操作需要记录检索历史
                var isNoteHistory = 1;

                //是否记录检索日志
                var isLog = 1;

                $.StandardPost('/Qikan/Search/Index', { key: encodeURIComponent(urlparam), isNoteHistory: isNoteHistory, isLog: isLog, indexKey: searchKeywords, indexIdentifier: identifier });
            }
        });
    }

    bindAutoComplete();

    form.on('checkbox(selectArticleAll)', function () {
        

        var addArr = new Array();
        var delArr = new Array();

        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {

            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            var checkStatus = $(this).prop("checked");
            $("div[data-guid=" + guid + "]").find('input[name=selectArticleAll]').each(function () {
                if (checkStatus) {
                    $(this).prop("checked", true);
                }
                else {
                    $(this).prop("checked", false);
                }
            });

            $("div[data-guid=" + guid + "]").find("div[id=articlelist]").find("input[type=checkbox]").each(function () {
                var id = $(this).data("id");
                if (checkStatus) {
                    $(this).prop("checked", true);
                    if ($.inArray(id.toString(), addArr) < 0) {
                        addArr.push(id.toString());
                    }
                }
                else {
                    $(this).prop("checked", false);
                    if ($.inArray(id.toString(), delArr) < 0) {
                        delArr.push(id.toString());
                    }
                }

            });

            $("div[data-guid=" + guid + "]").siblings().find("div[id=articlelist]").find("input[type=checkbox]").each(function () {
                var id = $(this).data("id");
                if ($.inArray(id.toString(), addArr) >= 0)
                {
                    $(this).prop("checked", true);
                }
                else
                {
                    $(this).prop("checked", false);
                }
            });
        }
        else
        {
            var checkStatus = $(this).prop("checked");
            $('input[name=selectArticleAll]').each(function () {
                if (checkStatus) {
                    $(this).prop("checked", true);
                }
                else {
                    $(this).prop("checked", false);
                }
            });

            $("#articlelist").find("input[type=checkbox]").each(function () {
                var id = $(this).data("id");
                if (checkStatus) {
                    $(this).prop("checked", true);
                    if ($.inArray(id.toString(), addArr) < 0) {
                        addArr.push(id.toString());
                    }
                }
                else {
                    $(this).prop("checked", false);
                    if ($.inArray(id.toString(), delArr) < 0) {
                        delArr.push(id.toString());
                    }
                }

            });
        }

        

        addSelectArticleSession(addArr.join(','));
        delSelectArticleSession(delArr.join(','));

        var count = getSelectArticleCount();
        $('span[data-topcount="topcount"]').text(count);
        $('span[data-footcount="footcount"]').text(count);
        $("#selectArticleCount").text(count);

        form.render();
    })


    window.getSelectArticleCount = function () {
        var count = 0;
        var strSession = getSessionSelectArticle();
        if (strSession != "")
        {
            count = strSession.split(",").length;
        }
        return count;
    }

    $(document).on("click", "#clearSelectArticle", function () {

        layer.confirm('确认要清空已选文章吗?', { icon: 3, title: '提示' }, function (index) {
            setSessionSelectArticle("");
            $(".selection-list dl").each(function () {
                $('input[data-id=' + $(this).data('id') + ']').each(function () {
                    $(this).prop("checked", false);
                    //form.render('checkbox');
                });

                $(this).remove();
            });

            $('input[name=selectArticleAll]').prop("checked", false);
            form.render('checkbox');
            var count = getSelectArticleCount();
            $('span[data-topcount="topcount"]').text(count);
            $('span[data-footcount="footcount"]').text(count);
            $("#selectArticleCount").text(count);
            layer.close(index);
        });
    });

    form.on('checkbox(selectArticle)', function (data) {

        var id = $(this).data("id");
        var checkStatus = $(this).prop("checked");

        var addArr = new Array();
        var delArr = new Array();

        $('input[data-id=' + id + ']').each(function () {

            if (checkStatus) {
                $(this).prop("checked", true);
                if ($.inArray(id.toString(), addArr) < 0) {
                    addArr.push(id.toString());
                }

            }
            else {
                $(this).prop("checked", false);

                if ($.inArray(id.toString(), delArr) < 0) {
                    delArr.push(id.toString());
                }

            }
        });

        addSelectArticleSession(addArr.join(','));
        delSelectArticleSession(delArr.join(','));

        var count = getSelectArticleCount();

        $('span[data-topcount="topcount"]').text(count);
        $('span[data-footcount="footcount"]').text(count);
        $("#selectArticleCount").text(count);

        if (!checkStatus) {
            $('input[name=selectArticleAll]').prop("checked", false);
        }
        else {
            var chose = true;
            $("#articlelist").find("input[type=checkbox]").each(function () {
                checkStatus = $(this).prop("checked");
                if (!checkStatus) {
                    chose = false;
                    return false;
                }
            });
            if (chose) {
                $('input[name=selectArticleAll]').prop("checked", true);
            }
        }
        form.render();
    });

    window.checkIsChecked = function () {

        var sessionStr = getSessionSelectArticle();
        if(sessionStr !="")
        {
            var sessionArr = sessionStr.split(",");
            for (var i = 0; i < sessionArr.length; i++) {
                $('input[data-id=' + sessionArr[i] + ']').each(function () {
                    $(this).prop("checked", true);
                    form.render('checkbox');
                });
            }
        }

        var count = getSelectArticleCount();
        $('span[data-topcount="topcount"]').text(count);
        $('span[data-footcount="footcount"]').text(count);
        $("#selectArticleCount").text(count);

        form.render();
    }

    //统一检索方法
    window.search = function (searchParamModel) {

        $("div[data-container=leftCluster]").html("");
        $(".leftcluser-loading").show();

        var jsonSearchParamModel = JSON.stringify(searchParamModel);
        $("#hidRequestsearchParamModel").val(jsonSearchParamModel);
        $("#hid_searchtype").val("searchList");
    
        searchList(jsonSearchParamModel);
    }

    //统一检索方法
    window.searchList = function (searchParamModel) {
        $.ajax({
            url: "/Search/SearchList",
            type: "post",
            //async: false,
            dataType: "html",
            data: { "searchParamModel": searchParamModel },
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (info) {
                $("#searchlist").html(info);
                checkIsChecked();

                $("html,body").animate({ "scrollTop": 0 }, 0);

                form.render();
                element.render();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/
                loaddingClose();
            }
        });
    }

    //统一检索方法
    window.advSearch = function (searchParamModel, guid) {

        searchParamModel.AdvTabGuid = guid;

        $("div[data-guid=" + guid + "]").find("div[data-container=leftCluster]").html("");
        $("div[data-guid=" + guid + "]").find(".leftcluser-loading").show();

        $.ajax({
            url: "/Search/SearchList",
            type: "post",
            //async: false,
            dataType: "html",
            data: { "searchParamModel": JSON.stringify(searchParamModel)},
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                //loaddingClose();
            },
            success: function (info) {
                $("#body >.l-width> .search-list").html(info);

                var searchParamModelJson = $("#body >.l-width> .search-list").find("input[name=preSearchParamModelJson]").val();

                if (searchParamModelJson != "") {

                    searchParamModel = JSON.parse(searchParamModelJson);
                }

                $("#body >.l-width> .search-list").append("<input type=\"hidden\" name=\"searchParamModelJson\" value='" + searchParamModelJson + " ' />");

                var guiddiv = $("div[data-guid=" + guid + "]");

                if (guiddiv.length > 0) {
                    guiddiv.html($("#body >.l-width> .search-list").html());
                }
                else {
                    $("#location>ul").append("<li data-guid=\"" + guid + "\"><a class=\"sel-close\" href=\"javascript:void(0)\"><i class=\"icon-close\"></i></a><a href=\"javascript:void(0)\" title=\"" + searchParamModel.AdvShowTitle.replace(/\"/g, "") + "\">" + searchParamModel.AdvShowTitle.replace(/\"/g, "") + "</a></li>");
                    $("#location>ul").children("li:last-child").addClass("tab-this").prevAll().removeClass("tab-this");
                    addTabDiv(guid);

                }
                
                checkTabShowStatus();

                checkIsChecked();
                gotoLocation();

                form.render();
                element.render();

                loaddingClose();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/

                loaddingClose();
            }
        });
    }

    window.checkTabShowStatus=function()
    {
        for (var i = 0; i < $("#location li").length; i++) {

            if ($("#location li").length - 5 > i) {
                $("#location li").eq(i).hide();
            }
            else {
                $("#location li").eq(i).show();
            }
        }
    }

    window.gotoLocation = function () {
        $("html,body").animate({ "scrollTop": $(".advance").scrollTop() + $('#location').offset().top - $(".advance").offset().top - 50 }, 0);
    }

    //关闭tab标签页
    $("#location>ul").on("click", "li>a>i", function (e) {
        layui.stope(e);

        var objli = $(this).parent().parent();
        var guid = objli.data("guid");


        if ($("li[data-guid=" + guid + "]").next().length > 0)//后面还有tab标签页，显示后面的
        {
            if (objli.hasClass("tab-this"))
            {
                $("li[data-guid=" + guid + "]").next().addClass("tab-this");
                $("li[data-guid=" + guid + "]").prevAll().removeClass("tab-this");
                $("div[data-guid=" + guid + "]").next().show();
                $("div[data-guid=" + guid + "]").prevAll().hide();
            }
            
        }
        else if ($("li[data-guid=" + guid + "]").prev().length > 0) //后面没有标签页显示前面的
        {
            if (objli.hasClass("tab-this")) {
                $("li[data-guid=" + guid + "]").prev().addClass("tab-this");
                $("li[data-guid=" + guid + "]").prev().prevAll().removeClass("tab-this");
                $("div[data-guid=" + guid + "]").prev().show();
                $("div[data-guid=" + guid + "]").prev().prevAll().hide();
            }
        }

        $("li[data-guid=" + guid + "]").remove();
        $("div[data-guid=" + guid + "]").remove();

        checkTabShowStatus();
    });

    //切换tab标签页
    $(document).on("click", "#location>ul>li", function () {
        $(this).addClass("tab-this").siblings().removeClass("tab-this");
        $("div[data-guid=" + $(this).data("guid") + "]").show().siblings().hide();

        //切换tab标签页考虑检索区域条件回填
        //var guid = $(this).data("guid");
        //var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
        //var searchParamModel = JSON.parse(strsearchParamModelJson);
        //if(searchParamModel.SearchExpression=="")//高级检索
        //{
        //    element.tabChange('tabAdvance', "basic");
        //    if(searchParamModel.SearchKeyList.length>3)
        //    {
        //        if ($(".advance-input>div").length < searchParamModel.SearchKeyList.length)
        //        {
        //            for (var i = $(".advance-input>div").length; i < searchParamModel.SearchKeyList.length; i++) {
        //                $(".advance-input").append($(".advance-input>div").eq(2).clone());
        //            }
        //        }
        //        else
        //        {
        //            for (var i = $(".advance-input>div").length; i > searchParamModel.SearchKeyList.length; i--) {
        //                $(".advance-input>div").eq(i).remove();
        //            }
        //        }

        //    }
        //    else
        //    {
        //        for (var i = $(".advance-input>div").length; i >= 3; i--) {
        //            $(".advance-input>div").eq(i).remove();
        //        }
        //    }
        //}
        //else//检索式检索
        //{
        //    element.tabChange('tabAdvance', "re");
        //}

        form.render();
    });

    //隐藏显示所有作者
    $(document).on("click", ".author .more.hide", function () {
        $(this).hide();
        $(this).siblings("span").show();
    });
    //隐藏显示所有基金
    $(document).on("click", ".fund .more.hide", function () {
        $(this).hide();
        $(this).siblings("span").show();
    });
    //查看选中文献的参考文献、引证文献
    $(document).on("click", ".list-op .articleRefence a", function () {
        layer.closeAll();

        var stype = $(this).data("stype");
        var strIds = "";

        strIds = getSessionSelectArticle();

        if (strIds == "") {
            layer.msg('请选择要查看的文献！');
            return;
        }

        $.StandardPostNewWindow('/Qikan/Search/Index', { stype: stype, strIds: strIds });
    });

    //查看当前文章的引证文献
    $(document).off("click", "#articlelist .cited a,.article .cited a").on("click", "#articlelist .cited a,.article .cited a", function () {
        $.StandardPostNewWindow('/Qikan/Search/Index', { stype: "by", strIds: $(this).data("articleid"), totalcount:$(this).data("zkbycount") });
    })

    $(document).on("click", ".selection-tool .articleRefence a", function () {

        var stype = $(this).data("stype");
        var strIds = "";
        strIds = getSessionSelectArticle();
        if (strIds == "") {
            layer.msg('请选择要查看的文献！');
            return;
        }
        $.StandardPostNewWindow('/Qikan/Search/Index', { stype: stype, strIds: strIds });
    })

    //统计分析

    $(document).on("click", ".list-op  .articleReport a", function () {
        layer.closeAll();

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

        var choseId = "";
        var stype = $(this).data("stype");

        if (stype == "chose") {
            var strIds = "";
            strIds = getSessionSelectArticle();
            if (strIds == "") {
                layer.msg('请选择要统计分析的文献！');
                return;
            }
            choseId = strIds;
        } else {
            if (searchParamModel.ShowRules == "") {
                layer.msg('请先进行检索！');
                return;
            }
        }
        var form = $("<form></form>").attr("action", "/Qikan/Search/ArticleReport?from=" + GetCurrentPath()).attr("method", "post").attr("target", "_blank");
        form.append($("<input></input>").attr("name", "searchParamModel").attr("type", "hidden").attr("value", JSON.stringify(searchParamModel)));
        form.append($("<input></input>").attr("name", "choseId").attr("type", "hidden").attr("value", choseId));
        form.appendTo('body').submit().remove();
    });

    $(document).on('click', '.selection-tool .articleReport a', function () {

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

        var choseId = "";
        var stype = $(this).data("stype");

        if (stype == "chose") {
            var strIds = "";
            strIds = getSessionSelectArticle();
            if (strIds == "") {
                layer.msg('请选择要统计分析的文献！');
                return;
            }
            choseId = strIds;
        } else {
            if (searchParamModel.ShowRules == "") {
                layer.msg('请先进行检索！');
                return;
            }
        }
        var form = $("<form></form>").attr("action", "/Qikan/Search/ArticleReport?from=" + GetCurrentPath()).attr("method", "post").attr("target", "_blank");
        form.append($("<input></input>").attr("name", "searchParamModel").attr("type", "hidden").attr("value", JSON.stringify(searchParamModel)));
        form.append($("<input></input>").attr("name", "choseId").attr("type", "hidden").attr("value", choseId));
        form.appendTo('body').submit().remove();
    });

    //添加或减少检索条件
    $(document).on("click", ".input-group .layui-btn-group .layui-btn,.input-group .layui-btn-group .layui-btn", function () {
        if ($(this).data("type") == "plus") {
            if ($(".advance-input .input-group").length < 5) {
                //$(".advance-input").append($(this).parent().parent().parent().clone());
                $(".advance-input").append($("div[name=template] .input-group").clone());
            }

        }
        else if ($(this).data("type") == "minus") {
            if ($(".advance-input .input-group").length > 3) {
                var div = $(this).parent().parent().parent();
                div.remove();
            }
        }

        form.render();
    });

    window.addTabDiv = function (guid) {
        $("#tabDiv").append($("#body >.l-width> .search-list").clone());
        $("#tabDiv").children("div:last-child").attr("data-guid", guid).show().prevAll().hide();
    }

    //高级检索
    $(document).off("click", "#basic_searchdomainfilter .advance-submit .adv-search").on("click", "#basic_searchdomainfilter .advance-submit .adv-search", function () {

        $("#hid_searchtype").val("RetrievalSearchExpression");
      

        var length = $(".advance-input .input-group").length;
        var searchParamModel = new SearchParamModel();
        searchParamModel.ObjectType = 1;

        var advShowTitle = "";
        for (var i = 0; i < length; i++) {
            if ($("select[name='advIdentifier']").eq(i).val() != "" && $("select[name='advIdentifier']").eq(i).val() != "undefined"
                && $("input[name='advSearchKeywords']").eq(i).val() != "" && $("select[name='advSearchKeywords']").eq(i).val() != "undefined") {
                var searchKeyModel = new SearchKeyModel();

                //if (i > 0 && $("input[name='advSearchKeywords']").eq(i - 1).val() != "") {
                if (i > 0) {
                    searchKeyModel.PreLogicalOperator = $("select[name='logicalOperator']").eq(i - 1).val();
                    advShowTitle += " " + searchKeyModel.PreLogicalOperator + " ";
                }

                searchKeyModel.FieldIdentifier = $("select[name='advIdentifier']").eq(i).val();
                advShowTitle += $("select[name='advIdentifier']").eq(i).find("option:selected").text()
                advShowTitle += "=";
                searchKeyModel.SearchKey = $("input[name='advSearchKeywords']").eq(i).val();
                advShowTitle += searchKeyModel.SearchKey;



                searchKeyModel.IsExact = $("select[name='isExact']").eq(i).val();
                if (searchKeyModel.SearchKey.trim() != "")
                {
                    searchParamModel.SearchKeyList.push(searchKeyModel);
                }
            }
        }

        searchParamModel.AdvShowTitle = advShowTitle;

        //if (searchParamModel.SearchKeyList.length <= 0) {
        //    layer.msg('请输入检索条件！');
        //    return false;
        //}

        var timeStr = getSearchYearRange("basic_");
        if (timeStr == "false") {
            layer.msg('开始时间不能大于结束时间！');
            return false;
        }
        else if (timeStr.indexOf("-") >= 0)//年份区间
        {
            var yearArray = new Array(); //定义一数组 
            yearArray = timeStr.split("-"); //字符分割 
            searchParamModel.BeginYear = yearArray[0];
            searchParamModel.EndYear = yearArray[1];

            if (searchParamModel.AdvShowTitle == "") {
                searchParamModel.AdvShowTitle = "年份：" + searchParamModel.BeginYear + "-" + searchParamModel.EndYear;
            }
            else {
                searchParamModel.AdvShowTitle += " AND 年份：" + searchParamModel.BeginYear + "-" + searchParamModel.EndYear;
            }

            searchParamModel.AdvShowTitle = searchParamModel.AdvShowTitle.replace(/1900/g, "收录起始年");
        }
        else if (timeStr != "") {
            searchParamModel.UpdateTimeType = timeStr;

            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "更新时间：";

            switch (timeStr) {
                case "1":
                    searchParamModel.AdvShowTitle += "一个月内";
                    break;
                case "2":
                    searchParamModel.AdvShowTitle += "三个月内";
                    break;
                case "3":
                    searchParamModel.AdvShowTitle += "半年内";
                    break;
                case "4":
                    searchParamModel.AdvShowTitle += "一年内";
                    break;
                case "5":
                    searchParamModel.AdvShowTitle += "当年内";
                    break;
                default:
                    break;
            }
        }

        var journalRange = getSearchJournalRange("basic_");

        if (journalRange != "") {
            searchParamModel.JournalRange = journalRange;
            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "期刊范围：" + getSearchJournalRangeName("basic_");
        }

        var domainRange = getSearchDomainRange("basic_");
        if (domainRange != "") {
            searchParamModel.DomainRange = domainRange;
            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "学科限定：" + getSearchDomainRangeName("basic_");
        }
        searchParamModel.IsNoteHistory = 1;

        if ($("input[name=basic_chn_en_extend]").prop("checked"))
        {
            searchParamModel.ChineseEnglishExtend = 1;
        }

        if ($("input[name=basic_synonym_extend]").prop("checked")) {
            searchParamModel.SynonymExtend = 1;
        }
        if (searchParamModel.AdvShowTitle == "") {
            searchParamModel.AdvShowTitle = "空检索";
        }
        var guid = createGuid();
        advSearch(searchParamModel, guid);

    });
    //高级检索-检索式检索
    $(document).off("click", "#re_searchdomainfilter .advance-submit .adv-search").on("click", "#re_searchdomainfilter .advance-submit .adv-search", function () {
        $("#hid_searchtype").val("RetrievalBasicExpression");

        var length = $(".advance-input .input-group").length;
        var searchParamModel = new SearchParamModel();
        searchParamModel.ObjectType = 1;

        searchParamModel.SearchExpression = $(".layui-textarea").val();

        if (searchParamModel.SearchExpression.trim() == "") {
            layer.msg('请输入检索条件！');
            return false;
        }

        searchParamModel.AdvShowTitle = searchParamModel.SearchExpression;

        var timeStr = getSearchYearRange("re_");
        if (timeStr == "false") {
            layer.msg('开始时间不能大于结束时间！');
            return false;
        }
        else if (timeStr.indexOf("-") >= 0)//年份区间
        {
            var yearArray = new Array(); //定义一数组 
            yearArray = timeStr.split("-"); //字符分割 
            searchParamModel.BeginYear = yearArray[0];
            searchParamModel.EndYear = yearArray[1];

            if (searchParamModel.AdvShowTitle == "") {
                searchParamModel.AdvShowTitle = "年份：" + searchParamModel.BeginYear + "-" + searchParamModel.EndYear;
            }
            else {
                searchParamModel.AdvShowTitle += " AND 年份：" + searchParamModel.BeginYear + "-" + searchParamModel.EndYear;
            }

            searchParamModel.AdvShowTitle = searchParamModel.AdvShowTitle.replace(/1900/g, "收录起始年");
        }
        else if (timeStr != "") {
            searchParamModel.UpdateTimeType = timeStr;

            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "更新时间：";

            switch (timeStr) {
                case "1":
                    searchParamModel.AdvShowTitle += "一个月内";
                    break;
                case "2":
                    searchParamModel.AdvShowTitle += "三个月内";
                    break;
                case "3":
                    searchParamModel.AdvShowTitle += "半年内";
                    break;
                case "4":
                    searchParamModel.AdvShowTitle += "一年内";
                    break;
                case "5":
                    searchParamModel.AdvShowTitle += "当年内";
                    break;
                default:
                    break;
            }
        }

        var journalRange = getSearchJournalRange("re_");

        if (journalRange != "") {
            searchParamModel.JournalRange = journalRange;
            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "期刊范围：" + getSearchJournalRangeName("re_");

        }

        var domainRange = getSearchDomainRange("re_");
        if (domainRange != "") {
            searchParamModel.DomainRange = domainRange;
            if (searchParamModel.AdvShowTitle != "") {
                searchParamModel.AdvShowTitle += " AND ";
            }
            searchParamModel.AdvShowTitle += "学科限定：" + getSearchDomainRangeName("re_");
        }
        if (searchParamModel.AdvShowTitle == "") {
            searchParamModel.AdvShowTitle = "空检索";
        }
        searchParamModel.IsNoteHistory = 1;
        var guid = createGuid();
        advSearch(searchParamModel, guid);

    });

    $(document).off("keyup","input[name=advSearchKeywords]").on('keyup',"input[name=advSearchKeywords]", function (e) {
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $("#basic_searchdomainfilter .advance-submit .adv-search").click();
        }
    });

    //$(".layui-textarea").on('keyup', function (e) {
    //    var theEvent = window.event || e;
    //    var code = theEvent.keyCode || theEvent.which;
    //    if (code == 13) {
    //        $("#re_searchdomainfilter .advance-submit .adv-search").click();
    //    }
    //});

    window.getSearchDomainRangeName = function (frontName) {
        //学科导航
        var str = "";

        if ($("#" + frontName + "checkboxAllXueKe").prop('checked') == false) {

            $("#" + frontName + "searchDomain>ul>li>span").each(function (a, b) {
                if ($(this).find("input:checkbox:checked").prop("checked") == true) {
                    if (str != "") {
                        str += "," + $(this).find("input:checkbox").attr("title");
                    }
                    else {
                        str += $(this).find("input:checkbox").attr("title");
                    }
                }
            });

            $("#" + frontName + "searchDomain>ul>li>ul>li>span").each(function (a, b) {

                if ($(this).find("input:checkbox:checked").prop("checked") == true) {

                    if ($(this).parent().parent().prev().find("input:checkbox").prop("checked") == false) {

                        if (str != "") {
                            str += "," + $(this).find("input:checkbox").attr("title");
                        }
                        else {
                            str += $(this).find("input:checkbox").attr("title");
                        }
                    }
                }

            });
        }
        return str;
    }

    window.getSearchJournalRangeName = function (frontName) {
        var radion = "";
        $("input[name=" + frontName + "journalRange]:checked").each(function (a, b) {
            if ($(this).val() != 1) {
                if (radion != "") {
                    radion += "," + $(this).attr("title");
                }
                else {
                    radion += $(this).attr("title");
                }
            }
        });
        return radion;
    }

    window.getSearchDomainRange = function (frontName) {
        //学科导航
        var str = "";

        if ($("#" + frontName + "checkboxAllXueKe").prop('checked') == false) {

            $("#" + frontName + "searchDomain>ul>li>span").each(function (a, b) {
                if ($(this).find("input:checkbox:checked").prop("checked") == true) {
                    if (str != "") {
                        str += "," + $(this).find("input:checkbox").val();
                    }
                    else {
                        str += $(this).find("input:checkbox").val();
                    }
                }
            });

            $("#" + frontName + "searchDomain>ul>li>ul>li>span").each(function (a, b) {

                if ($(this).find("input:checkbox:checked").prop("checked") == true) {

                    if ($(this).parent().parent().prev().find("input:checkbox").prop("checked") == false) {

                        if (str != "") {
                            str += "," + $(this).find("input:checkbox").val();
                        }
                        else {
                            str += $(this).find("input:checkbox").val();
                        }
                    }
                }

            });
        }
        return str;
    }

    window.getSearchJournalRange = function (frontName) {
        var radion = "";
        $("input[name=" + frontName + "journalRange]:checked").each(function (a, b) {
            if ($(this).val() != 1) {
                if (radion != "") {
                    radion += "," + $(this).val();
                }
                else {
                    radion += $(this).val();
                }
            }
        });
        return radion;
    }

    //用frontName区分是获取高级检索和检索式检索，高级检索取basic_、检索式检索re_
    window.getSearchYearRange = function (frontName) {
        var str = "";

        //时间
        var timetype = $("input[name=" + frontName + "yearRange]:checked").val();

        if (timetype == 1) {
            var starttime, endtime;
            starttime = $("select[name=" + frontName + "beginYear]").val();
            endtime = $("select[name=" + frontName + "endYear]").val();

            var d = new Date(), nowYear = d.getFullYear();
            if (starttime == 1900 && endtime == nowYear) {
                str = "";
            } else {
                if (endtime < starttime) {
                    return str = "false";
                }
                str = starttime + "-" + endtime;
            }
        } else {
            var utvalue = $("select[name=" + frontName + "updateTimeType]").val();
            if (typeof (utvalue) == "undefined") { utvalue = 1; }
            str = utvalue;
        }

        return str;
    }


    form.on('checkbox(journalRange)', function (element) {
        var frontName = $(this).data("type");

        if (element.value == 1 && $(this).prop("checked")) {
            $('input[name=' + frontName + 'journalRange]').each(function () {
                $(this).prop("checked", false);
            });

            $(this).prop("checked", true);
        }
        else if (element.value == 1 && !$(this).prop("checked")) {
            layer.msg("总得选一个吧！");
            $(this).prop("checked", true);
        }
        else if (element.value != 1 && $(this).prop("checked")) {
            $('input[name=' + frontName + 'journalRange]').each(function () {
                if ($(this).val() == 1) {
                    $(this).prop("checked", false);
                }
            });
        }

        form.render('checkbox');
    });
    //摘要展开全部
    window.showRemark = function (obj) {
        $(obj).parent().parent().find(".abstract").html($(obj).prev().html());
        $(obj).hide();
    }

    form.on('checkbox(basic_domainRangeAll)', function (element) {
        initDomainChekBox();
        form.render();

    });

    form.on('checkbox(re_domainRangeAll)', function (element) {
        initDomainChekBox();
        form.render();

    });

    form.on('checkbox(domainRangeFirstLevel)', function () {

        //var e = arguments.callee.caller.arguments[0] || window.event;
        //layui.stope(e);

        if ($(this).prop('checked')) {

            $(this).parent().next().find(':checkbox').each(function () {
                $(this).prop("checked", true).attr("disabled", true);
            });
        }
        else {
            $(this).parent().next().find(':checkbox').each(function () {
                $(this).prop("checked", false).attr("disabled", false);
            });
        }

        form.render();
    });


    $(".cluster-item .icon-arrow-right").parent().on("click", function (event) {

        var tagName = event.target.nodeName.toLowerCase();
        var parentObj = $(this).parent();

        if (parentObj.hasClass("show-more")) {
            parentObj.removeClass("show-more");
        }
        else {
            parentObj.addClass("show-more");
        }

        layui.stope(event);
        form.render();
    });


    $(document).on('click', '.btn-switch .layui-btn', function () {
        var othis = $(this), type = othis.data('type');
        active[type] ? active[type].call(this, othis) : '';
    });

    var active = {
        set: function (othis) {
            var key = othis.data('key');
            var searchParamModel = getSearchParamModel();
            switch (searchParamModel.ObjectType) {
                case 1:
                    $.cookie('LIBUSERSETARTICLELISTTYPECOOKIE', key, { expires: 1, path: '/' });
                    break;
                case 7:
                    $.cookie('LIBUSERSETJOURNALLISTTYPECOOKIE', key, { expires: 1, path: '/' });
                    break;
            }
            var options = {};
            var searchtype = $("#searchType").val();//判断检索类型
            if (searchtype == "normal") {
                $(".search-result-list>div").each(function () {
                    $(this).hide();
                });
                $('#' + key).show();
                othis.addClass("active").siblings().removeClass("active");
            }
            else if (searchtype == "advance") {
                var guid = "";
                othis.parents().each(function () {
                    if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                        guid = $(this).data("guid");
                        return false;
                    }
                });

                $("div[data-guid=" + guid + "] .search-main .search-result-list>div").each(function () {
                    if ($(this).attr("id") == key) {
                        $(this).show();
                    }
                    else {
                        $(this).hide();
                    }
                });

                othis.addClass("active").siblings().removeClass("active");
            }
        },
        order: function (othis) {

            var key = othis.data('key');
            var searchParamModel = getSearchParamModel();
            searchParamModel.Sort = key;

            switch (searchParamModel.ObjectType) {
                case 1:
                    $.cookie('LIBUSERSETARTICLESORTCOOKIE', key, { expires: 1, path: '/' });
                    break;
                case 7:
                    $.cookie('LIBUSERSETJOURNALSORTCOOKIE', key, { expires: 1, path: '/' });
                    break;
            }

            var searchtype = $("#searchType").val();//判断检索类型
            if (searchtype == "normal") {

                $.when(search(searchParamModel)).done(function () {

                    $('a[data-key=' + key + ']').addClass("active").siblings().removeClass("active");
                    form.render();
                });
            }
            else if (searchtype == "advance") {
                var guid = othis.parent().parent().parent().parent().parent().parent().data("guid");
                var strsearchParamModelJson = othis.parent().parent().parent().parent().parent().parent().find("input[name=searchParamModelJson]").val();
                var searchParamModel = JSON.parse(strsearchParamModelJson);


                searchParamModel.Sort = key;

                $.when(advSearch(searchParamModel, guid)).done(function () {
                    $('a[data-key=' + key + ']').addClass("active").siblings().removeClass("active");
                    form.render();
                });
            }


        }
    };


    window.bindSelectArticle = function () {
        var ids = "";
        ids = getSessionSelectArticle();
        if (ids == "") {
            layer.msg("请选择要导出的文献！")
            return;
        }
        else {

            selectArticle(ids);
        }
    }

    window.selectArticle = function (ids) {

        $.ajax({
            url: "/Qikan/WebControl/SelectArticle",
            type: "post",
            //async: false,
            dataType: "html",
            data: { "ids": ids },
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (info) {
                $("#selection").html(info);
                form.render();

                layer.open({
                    type: 1
                    , skin: 'layui-layer-rim login'
                    , title: ' '
                    , area: ['650px', '365px']
                    , anim: 0
                    , resize: false
                    , content: $('#selection')
                    //,closeBtn: 0
                    //, zIndex: layer.zIndex //重点1
                    , success: function (layero) {
                        //layer.setTop(layero); //重点2
                    }
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/
            }
        });
    }

    $(document).off("click", ".indexArticleIconView").on("click", ".indexArticleIconView", function () {
        var url = $(this).attr('data-pagurl');
        var url2 = $(this).attr('data-pagurl2');
        if (url == "") {
            layer.msg('暂无预览');
            return;
        }
        var image = new Image();
        image.onerror = function () {
            var errosrt = url;
            image.width = 970;
            image.height = 1388;
            image.src = url2;
            image.onerror = null; //控制不要一直跳动
        }
        image.src = url;
        $('#showImage').append(image);
        layer.open({
            title: "在线预览",
            type: 1,
            content: $('#showImage'),
            shadeClose: true,
            scrollbar: false,
            offset: ['15px'],
            area: ['990px', '750px'],
            end: function () {
                $('#showImage').empty();
            }
        });

    })


    form.on('select(advIdentifierChange)', function (data) {
        if(data.value=="C")
        {
            data.othis.parent().next().children(".layui-btn").show();
        }
    });

        
    $(document).off("click", ".classbtn").on("click", ".classbtn", function () {
        var input = $(this).prev();
        input.addClass("selecting");
        $("#category").find("input[type=checkbox]:checked").each(function () {
            $(this).prop('checked', false);
        });

        form.render();

        layer.open({
            type: 1,
            title: '选择分类号',
            area: ['720px', '480px'],
            content: $('#category'),
            success: function (layero, index) {
               
            },
            cancel: function (index, layero) {
                $("#category").hide();
                input.removeClass("selecting");
                layer.close(index);
            }
        });
    })

    $(document).off("click", "#category .class-box .cluster-list .icon-plus,#category .class-box .cluster-list .icon-minus")
        .on("click", "#category .class-box .cluster-list .icon-plus,#category .class-box .cluster-list .icon-minus", function () {

        var jqSelf = $(this);
        var jqChildrenUl = jqSelf.parent().parent().children("ul");
        if (jqChildrenUl.length) {
            if (jqSelf.hasClass("icon-plus")) {
                jqChildrenUl.show();
                jqSelf.removeClass("icon-plus").addClass("icon-minus");
            } else {
                jqChildrenUl.hide();
                jqSelf.removeClass("icon-minus").addClass("icon-plus");
            }
        }
        else
        {
            jqSelf.removeClass("icon-plus").addClass("icon-loading");
            var chkStatus = jqSelf.next().prop('checked');
            
            $.ajax({
                url: "/WebControl/SelectExtendClassNo",
                type: "post",
                dataType: "html",
                data: { "pid": $(this).parent().data("lngid"), "chkStatus": chkStatus },
                success: function (info) {
                    jqSelf.removeClass("icon-loading").addClass("icon-minus");
                    jqSelf.parent().parent().append(info);
                    form.render();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    /*错误信息处理*/
                }
            });
        }

    })
    
    form.on('checkbox(chkClassNo)', function (data) {

        var selectedArr = new Array();
        var strSelectClassNo = $("#hidSelectedClassNo").val();
        if (strSelectClassNo != "")
        {
            selectedArr = strSelectClassNo.split(",");
        }

        var chkStatus = $(this).prop('checked');
        var jqChildrenUl = data.othis.parent().parent().children("ul");
        if (jqChildrenUl.length) {
            jqChildrenUl.find("input").each(function () {
                $(this).prop("checked", chkStatus);
            })
        }

        if (!chkStatus)
        {
            data.othis.parents("li").each(function () {
                $(this).children("span").children("input").prop("checked", chkStatus);
            });
            if ($.inArray(data.elem.value.toString(), selectedArr) >= 0) {
                selectedArr.splice($.inArray(data.elem.value.toString(), selectedArr), 1);
            }

        }
        else
        {
            var pChkStatusFlag = false;
            var firstParentLi = $(this).parent().parent();
            firstParentLi.parents("li").each(function () {
                if($(this).children("span").children("input").prop("checked"))
                {
                    pChkStatusFlag = true;
                    return false;
                }
            });
            if(!pChkStatusFlag)
            {
                if ($.inArray(data.elem.value.toString(), selectedArr) < 0) {
                    selectedArr.push(data.elem.value.toString());
                }
            }
        }
        $("#hidSelectedClassNo").val(selectedArr.join(','));
        form.render();

    });

    $(document).off("click", "#category .submit .layui-btn").on("click", "#category .submit .layui-btn", function () {

        if ($(this).hasClass("layui-btn-primary")) {
            $(".advance .selecting").removeClass("selecting");
            $("#category").hide();
            layer.closeAll();
        }
        else {
            $(".advance .selecting").val($("#hidSelectedClassNo").val()).removeClass("selecting");
            $("#hidSelectedClassNo").val("");
            layer.closeAll();
        }

    });

    $(document).off("keyup", "#searchKeywords").on("keyup", "#searchKeywords", function (e) {
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $("#hid_searchtype").val("search");
            basicSearch();
        }
    });

    window.basicSearch = function () {
        $("#btnSearch").parent().addClass("searching");
        var urlparam = "";
        var identifier = $("select[name='identifier']").val();
        var searchKeywords = $("input[name='searchKeywords']").val();

        if (searchKeywords != "") {
            if (identifier == "")
            {
                identifier = "U";
            }
            urlparam = identifier + "=" + searchKeywords;
        }

        //检索操作需要记录检索历史
        var isNoteHistory = 1;

        //是否记录检索日志
        var isLog = 1;

        $.StandardPost('/Qikan/Search/Index', { key: encodeURIComponent(urlparam), isNoteHistory: isNoteHistory, isLog: isLog, indexKey: searchKeywords, indexIdentifier: identifier });
    }

    //设置cookie
    window.setCookie = function(name, value) {
        if (getCookie(name) != value) {
            $.cookie(name, value, { path: '/' });
        }
    }

    //读取cookies
    window.getCookie = function getCookie(name) {
        var cookieStr = $.cookie(name);
        if (cookieStr != null) {
            return cookieStr;
        }
        return "";
    }

    window.getSessionSelectArticle=function()
    {
        var ids = "";
        $.ajax
            ({
                type: "post",
                url: "/WebControl/GetSessionSelectArticle",
                data: {},
                dataType: "json",
                async: false,
                success: function (data) {
                    if (data.Result == 1)
                    {
                        ids = data.RetValue;
                    }
                },
                complete: function (XMLHttpRequest, textStatus)
                { },
                error: function () {

                }
            });

        return ids;
    }

    window.setSessionSelectArticle=function(ids)
    {
        $.ajax
            ({
                type: "post",
                url: "/WebControl/SetSessionSelectArticle",
                data: { ids: ids },
                dataType: "json",
                async: false,
                success: function (data) {
                },
                complete: function (XMLHttpRequest, textStatus)
                { },
                error: function () {
                }
            });
    }

    $(document).off("click", ".search-history .orderread").on("click", ".search-history .orderread", function () {
        var historyKey = $(this).data("lngmysearhistoryguid");
        var showRules = $(this).data("showrules");

        $.ajax({
            url: "/WebControl/CheckuserType",
            type: "post",
            dataType: "json",
            data: { },
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (info) {
                if (info.RetValue == 0)//游客用户
                {
                    layer.confirm(info.PromptMsg, { icon: 3, title: '提示' }, function (index) {
                        loginAction();
                        layer.close(index);
                    });
                }
                else if(info.RetValue == 2)//机构用户
                {
                    $("#hidOrgMkey").val(info.Tag);
                    layer.confirm(info.PromptMsg, { icon: 3, title: '提示' }, function (index) {
                        loginAction();
                        layer.close(index);
                    });
                }
                else
                {
                    addOrderRead(historyKey, showRules);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/
                loaddingClose();
            }
        });
    })

    $(document).off("click", ".btnSearchhistory").on("click", ".btnSearchhistory", function () {
        window.checklogin(seeSearchhistoryAfterLogin);
    });

    window.seeSearchhistoryAfterLogin = function () {
        var url = '/Qikan/Article/History';
        window.open(url, "_blank");
    }
    form.render();
});



