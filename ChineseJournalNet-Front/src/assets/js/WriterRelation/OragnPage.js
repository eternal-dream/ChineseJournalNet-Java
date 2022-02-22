
layui.use(['element', 'layer', 'form'], function () {
    var form = layui.form
        , laypage = layui.laypage
        , $ = layui.jquery
        , layer = layui.layer;
    $(document).on("click", "#layerDemo #footerpager a", function () {
        if ($(this).data('page') === undefined)
            return;
        if ($(this).hasClass('layui-disabled'))
            return;
        var objectType = $('.object-title').attr('objectType');
        var hideid = $('.object-title').attr('hideid');
        var json = {
            pageIndex: $(this).data('page'),
            objectType: objectType,
            hideid: hideid
        };
        search(json);
    });

    $(document).on("click", "#layerDemo #footerpager .layui-laypage-btn", function () {
        var pageNum = $("#layerDemo #footerpager .layui-input").val();
        if (pageNum < 0 || pageNum === "") {
            // layer.msg("跳转失败");
            return;
        }
        var objectType = $('.object-title').attr('objectType');
        var hideid = $('.object-title').attr('hideid');
        if (hideid === "" || hideid === undefined)
            return;
        var json = {
            pageIndex: pageNum,
            objectType: objectType,
            hideid: hideid
        };
        search(json);
    })
    //搜索函数
    function search(searchPara) {
        $.ajax({
            url: "/Object/WritersOrganPage",
            type: "post",
            async: true,
            dataType: "html",
            data: {
                "pageIndex": searchPara.pageIndex,
                "objectType": searchPara.objectType,
                "id": searchPara.hideid
            },
            beforeSend: function () {
                loadding();
            },
            complete: function () {
                loaddingClose();
            },
            success: function (info) {
                $("#layerDemo").html(info);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                /*错误信息处理*/
            }
        });
    }
});
