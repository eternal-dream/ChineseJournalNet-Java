//导出PDF格式
function exportPDF(object) {
    //if (!checkUserLogin()) return;
    loadding("正在生成报告......");
    require(['echarts'], function (ec) {
        //$(".export-msg").html(getExportLoading("正在生成图片......"));
        setTimeout(function () { exportCommon(ec,object); }, 3000);
    });
}

function getImage(ec, obj) {
    try {
        return ec.getInstanceById(obj.attr("_echarts_instance_")).getDataURL("jpeg");
    } catch (e) {
        //alert(e);
        return "";
    }
}

function exportCommon(ec, object) {
    
    var prodImg = getImage(ec, $('#yeargraph'));
    var fstprodImg = getImage(ec, $('#fsttypegraph'));
    var organtypeImg = getImage(ec, $('#organtypegraph'));
    var domainImg = getImage(ec, $('#domaingraph'));
    var organImg = getImage(ec, $('#organgraph'));
    var yearImg = getImage(ec, $('#yeargraph'));
    var fstyearImg = getImage(ec, $('#fstyeargraph'));
    var scdorganImg = getImage(ec, $('#organ2graph'));
    var fthorganImg = getImage(ec, $('#organ4graph'));
    var scdorganbyImg = getImage(ec, $('#organ2graphby'));
    var fthorganbyImg = getImage(ec, $('#organ4graphby'));
    var fwmediaImg = getImage(ec, $('#fwmediagraph'));
    var bymediaImg = getImage(ec, $('#bymediagraph'));
    var yymediaImg = getImage(ec, $('#yymediagraph'));
    var mediaImg = getImage(ec, $('#mediagraph'));
   
    $.ajax({
        type: "post",
        async: false,
        url: "/Qikan/AnalysisReport/SvgExport",
        data: { prod: prodImg, fstprod: fstprodImg, typeorgan: organtypeImg, fstdomain: domainImg, fstorgan: organImg, year: yearImg, fstyear: fstyearImg, scdorgan: scdorganImg, fthorgan: fthorganImg, scdorganby: scdorganbyImg, fthorganby: fthorganbyImg, fwmedia: fwmediaImg, bymedia: bymediaImg, yymedia: yymediaImg, mediaImg: mediaImg, type: object, id: $("#objectId").val() },
        beforeSend: function () {
            //loadding("正在生成报告......");
            //$(".export-msg").html(getExportLoading("正在生成图片......"));
        },
        //complete: function () {
        //    loaddingClose();
        //},
        success: function (json) {
            var title = "", name = "", remark = "", cf = "", rf = "", key = "", sType = "", strIds = "",searchParamModel="",choseId="";
            if (object == "atlas") {
                title = $("#txtPdfTitle").val();
                name = $("#txtPdfPop").val();
                remark = $("#txtPdfDescription").val();
                searchParamModel = $("#searchParamModel").val();
                choseId = $("#choseId").val();
            }
            $.ajax({
                type: "post",
                url: "/Qikan/AnalysisReport/ExportPdf",
                data: { id: $("#objectId").val(), image: json, type: object, cf: cf, rf: rf, key: key, sType: sType, strIds: strIds, title: title, name: name, remark: remark, searchParamModel: searchParamModel, choseId: choseId },
                //beforeSend: function () {
                //    loadding("正在生成报告......");
                //    //$(".export-msg").html(getExportLoading("正在生成报告......"));
                //},
                //complete: function () {
                //    loaddingClose();
                //},
                success: function (fileName) {
                    if (fileName) {
                        $.ajax({
                            type: "post",
                            url: "/Qikan/AnalysisReport/ClearFile",
                            data: { files: json, type: object },
                            //beforeSend: function () {
                            //    loadding("正在整理报告......");
                            //    //$(".export-msg").html(getExportLoading("正在整理报告......"));
                            //},
                            complete: function () {
                                loaddingClose();
                            },
                            success: function () {
                                window.location.href = "/Qikan/AnalysisReport/DownLoadPdfFile?fileName=" + encodeURIComponent(fileName);
                                //$(".export-msg").html("");

                            }, error: function () {
                                loaddingClose();
                                //$(".exportmsg").html("下载报告失败!");
                            }
                        });
                    } else {
                        loaddingClose();
                        //$(".exportmsg").html("生成报告失败!");
                    }

                }, error: function () {
                    loaddingClose();
                    //$(".exportmsg").html("生成报告失败!");
                }
            });
        }, error: function () {
            loaddingClose();
            //$(".exportmsg").html("生成图片失败!");
        }
    });
}