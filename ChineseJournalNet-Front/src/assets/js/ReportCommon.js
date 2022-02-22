(function () {
    var jqXhr, id;
    //当前主对象
    var object;
    //请求Handler地址
    var requestHandler;

    function initPage() {
        getBasic();
    }

    /*获取对象基础统计数据，通用方法*/
    function getBasic() {
        jqXhr = $.ajax({
            dataType: "json",
            url: requestHandler,
            data: { type: "basic", id: id },
            beforeSend: function () {
                if (document.getElementById("yearlist")) {
                    //$("#yearlist").html(getLoading(""));
                }
                   
            }
        }).done(function (json) {
            if (json.Flag) {
                var data = JSON.parse(json.ResultHtml);
                if (document.getElementById("yearlist"))
                    $("#yearlist").html(data.yeartable);
            }
        });
    }


    //加载效果展示
    function getLoading(msg) {
        var loading = "<div class='alert'><img  src='/images/loading.gif' alt='正在载入数据...'/>" + msg + "</div>";
        return loading;
    }
    //导出报告效果展示
    function getExportLoading(msg) {
        var loading = "<div class='alert' style='display:block'>" + msg + "</div>";
        return loading;
    }


    /*选择调用的Handler*/
    function selectHandler() {
        switch (object) {
            case "media":
                requestHandler = "/ajax/Export/MediaExportHandler.ashx";
                break;
        }
    }

    $(function () {
        id = GetQueryString("id");
        selectHandler();
        initPage();
    });
})();