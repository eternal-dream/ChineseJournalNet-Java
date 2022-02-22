layui.use(['form', 'layer'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var idscontain = "";//所选id 字符串 

    var isxml = false;

    $(document).off("click", "a[data-key=export]").on("click", "a[data-key=export]", function () {
        exportArticle();
    });

    //统一检索方法
    window.exportArticle = function () {
        var ids = "";

        ids = getSessionSelectArticle();

        if (ids == "") {
            layer.msg("请选择要导出的文献！");
            return false;
        }
        idscontain = ids;
        $.StandardPostNewWindow('/Qikan/Search/Export', { ids: ids, strType: "title_info" });
    }

    $(".export-type li").on("click", function () {

        var type = $(this).data("type");
        //if (type == "excel") {
            
        //}
        if (type == 'txt') {
            $("#nomar").css('display', 'none');
            $("#exportform").css('display', 'block');
            $('.export-op').css('display', 'none');
            $('#exportform .layui-tab-item').addClass('layui-show');
        }
        else {
            $('.export-op').css('display', 'block');
            $("#nomar").css('display', 'block');
            $("#exportform").css('display', 'none');
        }
        if (type == "xml" || type == "xmlfirstnode") {
            m_ShowXml(type);//
        }
        else if (type == "excel")//excel
        {
            m_ExportExcel();
        }
        else //txt 结构
        {
            m_ShowInfo(type);
        }

    });

    /**点击切换将显示的数据用不同的标准显示
    *@param {string} xslFileName
    *@param {Dom} obj
    */

    window.m_ShowInfo = function (xslFileName) {

        isxml = false;
        var printBtn = document.getElementById("printbtn");
        var copyBtn = document.getElementById("copybtn");
        var showArea = document.getElementById("showArea");
        var customExportDiv = document.getElementById("customExportDiv");
        var exportBtn = document.getElementById("exportbtn");
        if (xslFileName == "text") {

            loadXls(xslFileName + ".xsl");
        } else if (xslFileName == "txt") {

            //自定义导出
        } else if (xslFileName == "chaxin") {
            loadXls(xslFileName + ".xsl");
        }
        else {

            loadXls(xslFileName + ".xsl");
        }

    };

    /**显示xml格式的数据*/
    window.m_ShowXml = function (type) {
        isxml = true;
        var divxml = document.getElementById("showArea");
        if (type == 'xmlfirstnode') {
            divxml.innerHTML = document.getElementById("xmlFirstNode").value.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n+/g, "<BR/>");
        }
        else {
            divxml.innerHTML = document.getElementById("xmlContent").value.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n+/g, "<BR/>");
        }
    };

    /**根据文件名加载样式文件
    *@param {string} xslfile
    */
    function loadXls(xslfile, strIds) {
        var xmlDoc, xslDoc;
        xmlDoc = $.parseXML(document.getElementById("xmlContent").value.replace(/\r\n/gi, ""));
        var domailUrl = location.protocol + "//" + location.host + "/";
        var browser = navigator.appName;
        if (window.ActiveXObject || "ActiveXObject" in window || navigator.userAgent.indexOf('MSIE') >= 0 || browser == "Microsoft Internet Explorer") {
            try {
                //ie必须如此获取xsl
                xslDoc = new ActiveXObject("Msxml2.FreeThreadedDOMDocument");
                xslDoc.async = false;
                xslDoc.load("/Export/" + xslfile);
                var oTemplate = new ActiveXObject("Msxml2.XSLTemplate");
                oTemplate.stylesheet = xslDoc;
                var oProcessor = oTemplate.createProcessor();
                oProcessor.input = xmlDoc;
                oProcessor.addParameter("domailUrl", domailUrl);
                oProcessor.transform();

                document.getElementById('showArea').innerHTML = oProcessor.output;

            } catch (e) {
                //alert(e);
            }
        }
        else {
            try {
                //返回的文件本身就是一个xml document对象
                $.ajax({
                    async: false,
                    url: "/Export/" + xslfile
                }).done(function (data) {
                    xslDoc = data;
                });
                // 定义XSLTProcessor对象
                var xsltProcessor = new XSLTProcessor();
                xsltProcessor.importStylesheet(xslDoc);
                xsltProcessor.setParameter(null, "domailUrl", domailUrl);
                // transformToDocument方式
                var result = xsltProcessor.transformToDocument(xmlDoc);

                document.getElementById('showArea').innerHTML = result.documentElement.innerHTML;

            } catch (e) {
            }
        }
    }
    //导出按钮导出 
    window.m_ExportFile = function () {
        //if (!checkUserLogin()) return;
        var today = new Date();
        var fileName = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate() + " " + today.getSeconds();
        var data;
        if (isxml) {
            var text = $("#showArea").text();
            data = { content: text, act: "xml" };
            //notefirst 到处txt
            //if ($("#showinfobtn8").attr("class").indexOf("on") > -1) {
            //    fileName = fileName + "@cqvip.txt";
            //}
            //else {
            //    fileName = fileName + "@cqvip.xml";
            //}
            if ($('.layui-this').data("type") == 'xmlfirstnode') {
                //例外 notefirsrt 导出为txt 
                fileName = fileName + "@cqvip.txt";
            }
            else {
                fileName = fileName + "@cqvip.xml";
            }

        } else {
            //  data = { content: $('<div/>').text($("#showArea").html()).html(), act: "html" };
            data = { content: $("#showArea").html(), act: "html" };
            fileName = fileName + "@cqvip.txt";
        }
        export2File(data, fileName);

    };
    function export2File(content, filename) {
       // var s = $.trim(content);
        if (content === null || content === undefined || $.trim(content) == "") {
            layer.msg('内容为空');
            return;
        }


        // content['filename'] = filename;
        //$.ajax({
        //    url: '/Search/ExportFile',
        //    data: { content: JSON.stringify(content), filename: filename },
        //    type: 'POST',
        //    dataType: 'json',
        //    success: function (data) {
        //        console.log(1);
        //    }, error: function (data) {
        //        console.log(data);
        //    }
        //})

        var tempForm = document.getElementById("TempForm");
        if (tempForm) {
            tempForm.action = "/Qikan/Search/ExportFile";
            tempForm.target = "_self";
            tempForm.method = "post";
            tempForm.content.value = JSON.stringify(content);
            tempForm.filename.value = filename;
            tempForm.submit();
        }
    }
    /**Excel导出*/
    window.m_ExportExcel = function () {
        //if (!checkUserLogin()) return;

        var idscontain = this.document.getElementById('ids').value;
        if (idscontain == "")
            return;
        var data = { ids: idscontain };
        if (JSON.stringify(data) == "") {
            layer.msg('文章ID为空');
            return;
        }
        var tempForm = document.getElementById("TempForm");
        if (tempForm) {
            tempForm.action = "/Qikan/Search/ExportExcel";
            tempForm.target = "_self";
            tempForm.method = "post";
            tempForm.content.value = JSON.stringify(data);
            //tempForm.fileName.value = filename;
            tempForm.submit();
        }
    };

    /**自定义导出字段*/
    window.m_CustomExportFile = function () {

        // if (!checkUserLogin()) return;

        var fname = "";
        var str = '';
        var today = new Date();
        $("input[cname='cbitem']:checked").each(function () {
            str += "," + this.value;
        });
        if (str.length == 0) {
            // alert("请选择输出项!");
            layer.msg('请选择输出项')
            return;
        } else {
            str = str.substring(1, str.length);
        }
        fname = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate() + " " + today.getSeconds();
        idscontain = this.document.getElementById('ids').value;
        if (idscontain == "")
            return;
        var strIds = idscontain;
        //if (request["strIds"] == null) {
        //    strIds = $("#content_hidExportIds").val();
        //} else {
        //    strIds = request.strIds;
        //}
        var data = { strIds: strIds, strType: "title_info", act: "custom", customFields: str, sType: "" };
        export2File(data, fname + "@cqvip.txt");
    };
    window.m_Copy = function () {
        var div = document.getElementById('showArea');
        //ie
        if (document.body.createControlRange) {
            div.contentEditable = 'true';
            var controlRange;
            controlRange = document.body.createControlRange();
            controlRange.addElement(div);
            controlRange.execCommand('Copy');

            div.contentEditable = 'false';
            // alert('成功复制到剪贴板！');
            layer.msg('成功复制到剪贴板！');
        } else {
            try {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            } catch (e) {
                //alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
                layer.msg("被浏览器拒绝！\n请在浏览器地址栏输入'about: config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true");
                return;
            }

            //ff
            var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
            if (!clip)
                return;
            var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
            if (!trans)
                return;
            trans.addDataFlavor('text/unicode');
            var str = new Object();
            var len = new Object();
            var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
            alert(div.innerHTML)
            var copytext = div.innerHTML.replace(/<br>/gi, "\r\n").replace(/&lt;/g, "<").replace(/&gt;/g, ">");
            str.data = copytext;
            trans.setTransferData("text/unicode", str, copytext.length * 2);
            var clipid = Components.interfaces.nsIClipboard;
            if (!clip)
                return false;
            clip.setData(trans, null, clipid.kGlobalClipboard);
            //alert('成功复制到剪贴板！');
            layer.msg('成功复制到剪贴板！');
        }
    };
});

