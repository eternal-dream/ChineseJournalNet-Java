function showdown(id, dinfo) {
    var info = id + ',' + dinfo;
    window.checklogin(showDownAfterLogin, [info]);
}

function showDownAfterLogin(info) {
    var articleId = info.split(',')[0];
    var dinfo = info.split(',')[1];
    $.post("/Qikan/Article/GetArticleRight", { articleId: articleId }, function (data) {
        var kind = data.RetValue;
        //如果有权限下载
        if (kind) {
            getdowninfo(articleId, dinfo);
        } else {
            var url = '/';
            window.open(url, "_blank");
        }
    });
}

function getdowninfo(id, dinfo) {
    var localPath = "";
    $.ajax({
        url: "/Qikan/Article/IsLocalDown",
        type: "post",
        async: false,
        data: {
            "id": id
        },
        success: function (data) {
            localPath = data.RetValue.localPath;
            if (localPath == "noLocal") {
                //$.ajax({
                //    url: "/Qikan/Article/ArticleDown",
                //    type: "post",
                //    async: true,
                //    data: {
                //        "id": id,
                //        "info": dinfo,
                //        "ts": new Date().getTime()
                //    },
                //    beforeSend: function () {
                //        loadding();
                //    },
                //    complete: function () {
                //        loaddingClose();
                //    },
                //    success: function (data) {
                //        if (!!window.ActiveXObject || "ActiveXObject" in window || navigator.userAgent.indexOf('Firefox') >= 0 || (navigator.userAgent.indexOf('Mozilla') >= 0 && navigator.userAgent.indexOf('Edge') < 0))
                //            window.location.href = data.url;
                //        else
                //            window.location.href = encodeURI(encodeURI(data.url));
                //    },
                //    error: function (jqXHR, textStatus, errorThrown) {
                //        /*错误信息处理*/
                //    }
                //});

                layer.alert("本地全文缺失");

            } else {
                var url = "/Qikan/Article/ArticleLocalDown";
                var form = $("<form></form>").attr("action", url).attr("method", "post");
                form.append($("<input></input>").attr("type", "hidden").attr("name", "path").attr("value", localPath));
                form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", id));
                form.appendTo('body').submit().remove();
                layer.msg('下载中', {
                    icon: 16,
                    shade: 0.01,
                    time: 3000
                });
            }
        }
    });
}

function showOnLineRead(id, dinfo) {

    var localPath = "";
    $.ajax({
        url: "/Qikan/Article/IsLocalDown",
        type: "post",
        async: false,
        data: {
            "id": id
        },
        success: function (data) {
            localPath = data.RetValue.localPath;
            if (localPath == "noLocal") {
                layer.alert("本地全文缺失");
            }
            else {
                var info = id + ',' + dinfo;
                window.checklogin(showOnLineReadAfterLogin, [info]);
            }
        }
    });
    
}

function showOnLineReadAfterLogin(info) {
    var articleId = info.split(',')[0];
    var dinfo = info.split(',')[1];
    $.ajax({
        url: "/Qikan/Article/GetArticleRight",
        type: "post",
        async: false,
        data: {
            "articleId": articleId
        },
        success: function (data) {
            var kind = data.RetValue;
            if (kind) {
                getOnLineRead(articleId, dinfo);
            }
        }
    });
}

function getOnLineRead(id, dinfo) {
    var url = '/Qikan/Article/ReadIndex?id=' + id + '&info=' + dinfo;
    window.open(url, "_blank");
}
