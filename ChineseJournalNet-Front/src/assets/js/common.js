$(document).ready(function () {
    SetATagFrom($(this));
});

//URL参数获取
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = decodeURIComponent(window.location.search.substr(1)).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

//URL加上From参数
function SetATagFrom(obj) {
    var path = GetCurrentPath();
    $(obj).find("a").each(function () {
        var href = $(this).attr("href");
        if (href != undefined && href != "/" && href != "" && href.indexOf("javascript") == -1 && href.indexOf("#") == -1 && href.indexOf("from") == -1 && href.indexOf("www.miitbeian.gov.cn") <= -1) {
            var newhref = href.indexOf("?") > -1 ? "&from=" : "?from=";
            $(this).attr("href", href + newhref + path);
        }
    });
}

//获取From参数的路径
function GetCurrentPath()
{
    var path = window.location.pathname.split('.')[0];
    path = path.indexOf('\/') > -1 ? path.replace('\/', '') : path;
    path = path.replace(/(\/)/g, '_');
    path = (path == "" ? "index" : path);

    return path;
}


layui.use(['form', 'layer', 'jquery', 'cookie', 'element'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var $ = layui.jquery;

    // 操作按钮----关闭自身层
    window.close_self = function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
})