jQuery(document).ready(function () {
    if (window.history && window.history.pushState) {
        $(window).on('popstate', function () {
            /// 当点击浏览器的 后退和前进按钮 时才会被触发， 
            window.history.pushState('forward', null, '');
            window.history.forward(1);
        });
    }
    //
    window.history.pushState('forward', null, '');  //在IE中必须得有这两行
    window.history.forward(1);

    $(document).bind("keydown", function (e) {
        e = window.event || arguments.callee.caller.arguments[0];
        if (e.keyCode == 116) {
            e.keyCode = 0;
            return false;
        }
    });
});