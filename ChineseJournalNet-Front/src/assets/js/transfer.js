function clickTransfer(id) {

    $.ajax
		({
		    type: "post",
		    url: "/Qikan/Article/Transfer?ts=" + new Date().getTime(),
		    dataType: "json",
		    data: { id: id },
		    async: false,
		    success: function (data) {
		        var transfer = {};
		        transfer.transferUrl = data.RetValue.TransferUrl;
		        transfer.articleId = data.RetValue.ArticleId;
		        transfer.strDate = data.RetValue.StrDate;
		        transfer.strCheck = data.RetValue.StrCheck;
		        transfer.title = data.RetValue.Title;
		        transfer.showWriter = data.RetValue.ShowWriter;
		        transfer.media = data.RetValue.Media;
		        transfer.years = data.RetValue.Years;
		        transfer.num = data.RetValue.Num;
		        transfer.volumn = data.RetValue.Volumn;
		        transfer.gch = data.RetValue.Gch;
		        transfer.vol = data.RetValue.Vol;
		        transfer.mediaId = data.RetValue.MediaId;
		        transfer.subjectNum = data.RetValue.SubjectNum;
		        transfer.specialNum = data.RetValue.SpecialNum;
		        transfer.unit = data.RetValue.Unit;
		        transfer.userId = data.RetValue.UserId;
		        transfer.userName = data.RetValue.UserName;
		        transfer.siteId = data.RetValue.SiteId;

		        var form = $("<form></form>").attr("action", transfer.transferUrl).attr("method", "post").attr("target", "_blank");
		        form.append($("<input></input>").attr("name", "lid").attr("type", "hidden").attr("value", transfer.articleId));
		        form.append($("<input></input>").attr("name", "date").attr("type", "hidden").attr("value", transfer.strDate));
		        form.append($("<input></input>").attr("name", "co").attr("type", "hidden").attr("value", transfer.strCheck));
		        form.append($("<input></input>").attr("name", "Title_C").attr("type", "hidden").attr("value", transfer.title));
		        form.append($("<input></input>").attr("name", "Writers").attr("type", "hidden").attr("value", transfer.showWriter));
		        form.append($("<input></input>").attr("name", "Name_C").attr("type", "hidden").attr("value", transfer.media));
		        form.append($("<input></input>").attr("name", "Num").attr("type", "hidden").attr("value", transfer.num));
		        form.append($("<input></input>").attr("name", "Years").attr("type", "hidden").attr("value", transfer.years));
		        form.append($("<input></input>").attr("name", "Volumn").attr("type", "hidden").attr("value", transfer.volumn));
		        form.append($("<input></input>").attr("name", "GCH").attr("type", "hidden").attr("value", transfer.gch));
		        form.append($("<input></input>").attr("name", "Vol").attr("type", "hidden").attr("value", transfer.vol));
		        form.append($("<input></input>").attr("name", "BookID").attr("type", "hidden").attr("value", transfer.mediaId));
		        form.append($("<input></input>").attr("name", "SubjectNum").attr("type", "hidden").attr("value", transfer.subjectNum));
		        form.append($("<input></input>").attr("name", "SpecialNum").attr("type", "hidden").attr("value", transfer.specialNum));
		        form.append($("<input></input>").attr("name", "mirrorName").attr("type", "hidden").attr("value", transfer.unit));
		        form.append($("<input></input>").attr("name", "UserID").attr("type", "hidden").attr("value", transfer.userId));
		        form.append($("<input></input>").attr("name", "UserName").attr("type", "hidden").attr("value", transfer.userName));
		        form.append($("<input></input>").attr("name", "sourceID").attr("type", "hidden").attr("value", transfer.siteId));
		        form.appendTo('body').submit().remove();
		    },
		    error: function () {
		        alert("该功能暂不可用!");
		    }
		});
}









