layui.use(['form'], function () {
    var form = layui.form;
    var $ = layui.$;
    var layer = layui.layer;

    $(document).on("click", "#headerpager a", function () {
        var selectPageSize = $("#selectPageSize .active").data("count");

        if (selectPageSize == null || selectPageSize == undefined || selectPageSize == "")
        {
            var searchParamModel = getSearchParamModel();
            switch (searchParamModel.ObjectType) {
                case 1://文章
                    if ($.cookie('LIBUSERSETARTICLEPAGESIZECOOKIE') != null && $.cookie('LIBUSERSETARTICLEPAGESIZECOOKIE') != undefined) {
                        if ($.cookie('LIBUSERSETARTICLEPAGESIZECOOKIE') != "") {
                            selectPageSize = $.cookie('LIBUSERSETARTICLEPAGESIZECOOKIE');
                        }
                    }
                    break;
                case 7://期刊
                    if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != null && $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != undefined) {
                        if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != "") {
                            selectPageSize = $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE');
                        }
                    }
                    break;
                case 3://作者
                    if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != null && $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != undefined) {
                        if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != "") {
                            selectPageSize = $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE');
                        }
                    }
                    break;
                case 6://机构
                    if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != null && $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != undefined) {
                        if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != "") {
                            selectPageSize = $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE');
                        }
                    }
                    break;
                default://默认采用期刊的作为对象分页参数
                    if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != null && $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != undefined) {
                        if ($.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE') != "") {
                            selectPageSize = $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE');
                        }
                    }
                    break;
            }
        }

        if (selectPageSize == null || selectPageSize == undefined || selectPageSize == "") {
            selectPageSize = 20;
        }

        gotoPage($(this).data('page'), selectPageSize, $(this));
    })

    $(document).on("click", "#selectPageSize a", function () {
        var selectPageSize = $(this).data("count");
        var searchParamModel = getSearchParamModel();
        switch (searchParamModel.ObjectType) {
            case 1://文章
                $.cookie('LIBUSERSETARTICLEPAGESIZECOOKIE', selectPageSize, { expires: 1, path: '/' });
                break;
            case 7://期刊
                $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE', selectPageSize, { expires: 1, path: '/' });
                break;
            case 3://作者
                $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE', selectPageSize, { expires: 1, path: '/' });
                break;
            case 6://机构
                $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE', selectPageSize, { expires: 1, path: '/' });
                break;
            default://默认采用期刊的作为对象分页参数
                $.cookie('LIBUSERSETJOURNALPAGESIZECOOKIE', selectPageSize, { expires: 1, path: '/' });
                break;
        }

        gotoPage(1, selectPageSize, $(this));
    })

    $(document).on("click", "#footerpager a", function () {
        var selectPageSize = $("#selectPageSize .active").data("count");
        gotoPage($(this).data('page'), selectPageSize, $(this));
    })

    $(document).on("click", "#footerpager .layui-laypage-btn", function () {
        var selectPageSize = $("#selectPageSize .active").data("count");

        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {

            var guid = "";
            $(this).parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });
            var pageNum = $("div[data-guid=" + guid + "]").find("#footerpager .layui-input").val();
            if (pageNum < 0 || pageNum == "") {
                layer.msg("跳转失败");
                return;
            }
            gotoPage(pageNum, selectPageSize, $(this));
        }
        else
        {
            var pageNum = $("#footerpager .layui-input").val();
            if (pageNum < 0 || pageNum == "") {
                layer.msg("跳转失败");
                return;
            }
            gotoPage(pageNum, selectPageSize, $(this));
        }

        
    })

    function gotoPage(pageNum, pageSize,othis) {
        var searchParamModel = getSearchParamModel();
        var searchtype = $("#searchType").val();//判断检索类型
        if (searchtype == "advance") {
            var guid = "";
            othis.parents().each(function () {
                if ($(this).data("guid") != undefined && $(this).data("guid") != "") {
                    guid = $(this).data("guid");
                    return false;
                }
            });

            var strsearchParamModelJson = $("div[data-guid=" + guid + "]").find("input[name=searchParamModelJson]").val();
            searchParamModel = JSON.parse(strsearchParamModelJson);
            searchParamModel.PageNum = pageNum;
            searchParamModel.PageSize = pageSize;
            advSearch(searchParamModel, guid);
        }
        else if (searchtype == "normal")
        {
            searchParamModel.PageNum = pageNum;
            searchParamModel.PageSize = pageSize;
            search(searchParamModel);
        }

        
    }
});
