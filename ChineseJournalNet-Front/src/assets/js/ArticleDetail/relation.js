function createPages() { //构造函数 构建出一个对象；
    var pages = {
        total_q: 10, //总数据
        current_page: 10,//每页显示的数据
        current_num: 1,//当前页数
        next: ".next",//下一页
        prev: ".prev",//上一页
        showli: $(""),//需要显示的li的
        chosenext: $(".next"),//下一页Juery对象
        choseprev: $(".prev"),//上一页Juery对象
        choseshowli: $(""),//需要显示的liJuery对象
        chosespage: $(""),//需要显示的ul的Juery对象
        curr: ".layui-laypage-curr",//当前页码样式
        currspilt:"",
        total_page: 1,//总页数
        init: function () {
            this.show();
            this.bind();
        },
        bind: function () {
            //下一页
            this.chosenext.click(function () {             
                //如果是最后一页不执行事件
                if (pages.current_num == pages.total_page) {
                    return false;
                } else {
                    //禁用，启用上一页，下一页
                    pages.choseprev.removeClass("layui-disabled");
                    if (pages.current_num + 1 == pages.total_page) {
                        $(this).addClass("layui-disabled");
                    } else {
                        $(this).removeClass("layui-disabled");
                    }
                    ++pages.current_num;

                    //绑定当前页的状态
                    pages.showState(pages.current_num);

                    //显示隐藏数据
                    $.each(pages.choseshowli, function (index, item) {
                        var start = pages.current_page * (pages.current_num - 1); //起始范围
                        var end = pages.current_page * pages.current_num; //结束范围
                        if (index >= start && index < end) { //如果索引值是在start和end之间的元素就显示，否则就隐
                            $(this).show();
                        } else {
                            $(this).hide();
                        }
                    });
                }
            });
            //上一页
            this.choseprev.click(function () {
               

                if (pages.current_num == 1) {
                    return false;
                } else {
                    pages.chosenext.removeClass("layui-disabled");
                    if (pages.current_num - 1 == 1) {
                        $(this).addClass("layui-disabled");
                    } else {
                        $(this).removeClass("layui-disabled");
                    }

                    --pages.current_num;

                    //绑定当前页的状态
                    pages.showState(pages.current_num);

                    $.each(pages.choseshowli, function (index, item) {
                        var start = pages.current_page * (pages.current_num - 1); //起始范围
                        var end = pages.current_page * pages.current_num; //结束范围
                        if (index >= start && index < end) { //如果索引值是start和end之间的元素就显示，否则就隐藏
                            $(this).show();
                        } else {
                            $(this).hide();
                        }
                    });
                }
            });
        },
        show: function () {
            pages.total_page = Math.ceil(pages.total_q / pages.current_page), //总页数
                pages.currspilt = pages.curr.substring(1, pages.curr.length);
                $(pages.showli + ":gt(" + (pages.current_page - 1) + ")").hide();//初始化数据显示，其他的数据隐藏。
            if (pages.total_page <= 1) {
                pages.chosespage.hide();
            }
            pages.showState(pages.current_num);
        },
        showState: function (currentpage) {
            $(pages.curr).siblings().not(pages.next).not(pages.prev).remove();
            $(pages.curr).remove();
            var pageNumber = "";
            for (var j = 1; j <= pages.total_page; j++) {
                if (j == currentpage) {
                    pageNumber += "<span class='layui-laypage-curr " + pages.currspilt + "'><em class='layui-laypage-em'></em><em>" + j + "</em></span>";
                } else {
                    pageNumber += "<a href='javascript:;' datapage=" + j + ">" + j + "</a>";
                }
            }
            pages.choseprev.after(pageNumber);
            $(pages.curr).siblings().not(pages.next).not(pages.prev).click(function () {
                pages.goPage($(this).attr("datapage"));
            });

        },
        goPage: function (page) {
            pages.chosenext.removeClass("layui-disabled");
            pages.choseprev.removeClass("layui-disabled");
            if (page == 1) {
                pages.choseprev.addClass("layui-disabled");
            }
            if (page == pages.total_page) {
                pages.chosenext.addClass("layui-disabled");
            }
            //绑定当前页的状态
            pages.showState(page);

            pages.current_num = page;
            $.each(pages.choseshowli, function (index, item) {
                var start = pages.current_page * (pages.current_num - 1); //起始范围
                var end = pages.current_page * pages.current_num; //结束范围
                if (index >= start && index < end) { //如果索引值是start和end之间的元素就显示，否则就隐藏
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        }
    }
    return pages;
}







