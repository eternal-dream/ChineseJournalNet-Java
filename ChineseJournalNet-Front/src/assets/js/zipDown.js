function isDown(years, num, gch, articleId) {
    var paramstr = {};
    paramstr.year = years;
    paramstr.num = num;
    paramstr.gch = gch;
    paramstr.articleId = articleId;
    paramstr.t = new Date().getTime();

    $.ajax({
        type: "post",
        url: "/Qikan/Article/ShowDwon",
        dataType: "json",
        data: paramstr,
        success: function (data) {
            var limit = data.RetValue.limit;
            if (limit) {
                //$(".imgdownzip").css('display', 'inline-block');
                $("#rightimgdownzip").css('display', 'inline-block');

            }
        }
    });
}

function showZipDown(id) {
    var info = [id];
    window.checklogin(showZipDownAfterLogin, info);
}

function showZipDownAfterLogin(info) {
    $.post("/Qikan/Article/GetArticleRight", { articleId: info, kind: 6 }, function (data) {
        var kind = data.RetValue;
        //如果有权限下载
        if (kind) {
            getZipDownInfo(info);
        } else {
            var url = '/Qikan/UserPay/CheckOrder?id=' + info + '&kind=2';
            window.open(url, "_blank");
        }
    });
}

function getZipDownInfo(id) {
    var url = "/Qikan/Article/ZipDown";
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", id));
    form.appendTo('body').submit().remove();
    layer.msg('下载中', {
        icon: 16,
        shade: 0.01,
        time: 7000
    });
}