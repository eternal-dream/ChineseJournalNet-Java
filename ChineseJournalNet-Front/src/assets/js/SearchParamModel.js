//检索关键字模型
var SearchKeyModel = (function (window) {

    //构造函数
    var SearchKeyModel = function () {
        return new SearchKeyModel.fn.init();
    }
    SearchKeyModel.fn = SearchKeyModel.prototype = {
        constructor: SearchKeyModel,
        init: function () {
            this.FieldIdentifier = "U";
            this.SearchKey = "";
            this.PreLogicalOperator = "";
            this.IsExact = 0;
        }
    }

    SearchKeyModel.fn.init.prototype = SearchKeyModel.fn;

    return SearchKeyModel;

})();
//检索参数模型
var SearchParamModel = (function (window) {

    //构造函数
    var SearchParamModel = function () {
        return new SearchParamModel.fn.init();
    }
    SearchParamModel.fn = SearchParamModel.prototype = {
        constructor: SearchParamModel,
        init: function () {
            this.ObjectType = 1;//检索对象类型 根据枚举 SearchObjectNameEnum 取值
            this.SearchKeyList = [];
            this.SearchExpression = "";
            this.BeginYear = "";
            this.EndYear = "";
            this.JournalRange = "";
            this.DomainRange = "";
            this.PageSize = "0";
            this.PageNum = "1";
            this.Sort = "0";
            this.ClusterFilter = "";
            this.SType = "";
            this.StrIds = "";
            this.UpdateTimeType = "";
            this.ClusterUseType = "Article";//标记左聚类中二次检索类型
            this.IsNoteHistory = 0;//是否需要记录检索历史
            this.AdvShowTitle = "";//高级检索显示tab标题
            this.ObjectId = "";//对象id
            this.ObjectSearchType = "0";//对象相关检索类型
            this.ChineseEnglishExtend = "0";//中英文扩展
            this.SynonymExtend = "0";//同义词扩展
            this.ShowTotalCount = "0";//显示条数
            this.AdvTabGuid = "";//高级检索显示tab模块guid
        }
    }

    SearchParamModel.fn.init.prototype = SearchParamModel.fn;

    return SearchParamModel;

})();

$.extend({
    StandardPost: function (url, args) {
        var body = $(document.body),
            form = $("<form method='post'></form>"),
            input;
        url += '?from=' + GetCurrentPath();
        form.attr({ "action": url });
        $.each(args, function (key, value) {
            input = $("<input type='hidden'>");
            input.attr({ "name": key });
            input.val(value);
            form.append(input);
        });

        form.appendTo(document.body);
        form.submit();
        document.body.removeChild(form[0]);
    }
});

$.extend({
    StandardPostNewWindow: function (url, args) {
        var body = $(document.body),
            form = $("<form method='post' target='_blank'></form>"),
            input;
        url += '?from=' + GetCurrentPath();
        form.attr({ "action": url });
        $.each(args, function (key, value) {
            input = $("<input type='hidden'>");
            input.attr({ "name": key });
            input.val(value);
            form.append(input);
        });

        form.appendTo(document.body);
        form.submit();
        document.body.removeChild(form[0]);
    }
});
//获取上一次检索条件的方法
window.getSearchParamModel = function () {
    var searchParamModel = new SearchParamModel();

    var searchParamModelJson = $("input[name=preSearchParamModelJson]").val();

    if (searchParamModelJson != "") {

        searchParamModel = JSON.parse(searchParamModelJson);
    }

    return searchParamModel;
}