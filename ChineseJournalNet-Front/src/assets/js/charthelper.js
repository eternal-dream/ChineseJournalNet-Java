require.config({
    paths: {
        echarts: '/dist/js'
    }
});

/*
 *Draw the pie chart.
 *Include echarts.js
*/
function drawPieChart(selector, title, series) {
    require(
       [
           'echarts',
           'echarts/chart/pie'
       ],
       function (ec) {
           if ($(selector).length > 0 && series != null && series != "") {
               $(selector).css("height", "500px");
               var myChart = ec.init($(selector)[0]);
               window.onresize = myChart.resize;
               myChart.setOption({
                   title: {
                       text: title,
                       x: 'center'
                   },
                   tooltip: {
                       trigger: 'item',
                       formatter: "{b} : {c} ({d}%)"
                   },
                   legend: {
                       x: 'center',
                       y: 'bottom',
                       data: series.legend
                   },
                   color: ['#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
                   calculable: true,
                   series: [
                      {
                          type: 'pie',
                          radius: '70%',
                          center: ['50%', '50%'],
                          itemStyle: {
                              normal: {
                                  label: {
                                      position: 'outer',
                                      formatter: function (a, b, c, d) { return b + ': ' + d + '%' }
                                  },
                                  labelLine: {
                                      show: true
                                  }
                              },
                          },
                          data: series.data
                      }
                   ]

               });
           }
       }
   );

}

/*
 *Draw the one_line chart.
 *Include echarts.js
*/
function drawLineChart(selector, title, series, xvalue, height) {
    require(
        [
            'echarts',
            'echarts/chart/line'
        ],
        function (ec) {
            if ($(selector).length > 0 && series != null && series != "" && xvalue.length > 0) {
                $(selector).css("height", height);
                alert(JSON.stringify(series));
                alert(JSON.stringify(xvalue));
                var showValue = {
                    "normal": {
                        "label": { "show": "true", "position": "top" }
                    }
                };
                var legendData = [];
                for (var i in series) {
                    legendData[i] = series[i]["name"];
                    series[i]["itemStyle"] = showValue;
                }
                var myChart = ec.init($(selector)[0]);
                window.onresize = myChart.resize;
                myChart.setOption({
                    title: {
                        text: title,
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                    },
                    legend: {
                        data: legendData,
                        y: 'bottom'
                    },
                    color: ['#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            data: xvalue
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            splitArea: { show: true },
                            axisLabel: {
                                formatter: function (v) {
                                    if (v < 1000) {
                                        return v;
                                    } else {
                                        return (v / 1000) + 'k';
                                    }
                                }
                            }
                        }
                    ],
                    series: series
                });
            }
        }
    );
}

/*
 *Draw a two_line chart.
 *Include echarts.js
*/
function drawTwoLineChart(selector, title, series, xvalue, height) {
    require(
       [
           'echarts',
           'echarts/chart/line'
       ],
       function (ec) {
           if ($(selector).length > 0 && series != null && series != "" && xvalue.length > 0) {
               $(selector).css("height", height);
               var legendData = [];
               var showValue = {
                   "normal": {
                       "label": { "show": "true", "position": "top" }
                   }
               };
               for (var i in series) {
                   legendData[i] = series[i]["name"];
                   series[i]["itemStyle"] = showValue;
               }
               var tipTrigger;
               if (legendData.length > 1) {
                   tipTrigger = "axis";
               } else {
                   tipTrigger = "item";
               }
               var myChart = ec.init($(selector)[0]);
               window.onresize = myChart.resize;
               myChart.setOption({
                   title: {
                       text: title,
                       x: 'center'
                   },
                   tooltip: {
                       trigger: tipTrigger,
                   },
                   legend: {
                       data: legendData,
                       y: 'bottom'
                   },
                   color: ['#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
                   calculable: true,
                   xAxis: [
                       {
                           type: 'category',
                           data: xvalue
                       }
                   ],
                   yAxis: [
                       {
                           type: 'value',
                           splitArea: { show: true },
                           axisLabel: {
                               formatter: function (v) {
                                   if (v < 1000) {
                                       return v;
                                   } else {
                                       return (v / 1000) + 'k';
                                   }
                               }
                           }
                       }
                   ],
                   series: series
               });
           }
       }
   );
    //    tooltip: {
    //        formatter: function () {
    //            if (this.series.name == '发文量')
    //                return '<b>' + this.point.x + '年的发文量:' + this.point.y + '篇</b>';
    //            else
    //                return '<b>' + this.point.x + '年的被引次数:' + this.point.y + '次</b>';
    //        }
}

/*
 *Draw the column chart.
*/
function drawColumnChart(selector, title, series, categories, height) {
    require(
       [
           'echarts',
           'echarts/chart/bar'
       ],
       function (ec) {
           if (selector.length > 0 && series != null && series != "" && categories.length > 0) {
               var legendData = [];
               var showValue = {
                   "normal": {
                       "label": { "show": "true", "position": "top" }
                   }
               };
               for (var i in series) {
                   legendData[i] = series[i]["name"];
                   series[i]["itemStyle"] = showValue;
               }
               $(selector).css("height", height);
               var myChart = ec.init(selector[0]);
               window.onresize = myChart.resize;
               myChart.setOption({
                   title: {
                       text: title,
                       x: 'center'
                   },
                   tooltip: {
                       trigger: 'item',
                   },
                   legend: {
                       data: legendData,
                       y: 'bottom'
                   },
                   color: ['#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
                   calculable: true,
                   xAxis: [
                   {
                       axisLabel: { rotate: 60, },
                       type: 'category',
                       data: categories
                   }],
                   grid: { x: 100, x2: 20, y2: 240, },
                   yAxis: [
                       {
                           type: 'value',
                           splitArea: { show: true },
                           axisLabel: {
                               formatter: function (v) {
                                   if (v < 1000) {
                                       return v;
                                   } else {
                                       return (v / 1000) + 'k';
                                   }
                               }
                           }
                       }
                   ],
                   series: series
               });
           }
       }
   );
}

/*
 *Draw the column charts.
*/
function drawColumnGraph(selector, series, type, title, height) {
    require(
       [
           'echarts',
           'echarts/chart/bar'
       ],
       function (ec) {
           if (selector.length > 0 && series != null && series != "") {
               var legendData = [];
               var showValue = {
                   "normal": {
                       "label": { "show": "true", "position": "top" }
                   }
               };
               for (var i in series) {
                   legendData[i] = series[i]["name"];
                   series[i]["itemStyle"] = showValue;
               }
               $(selector).css("height", height);
               var cates = type == "by" ? ["被引量"] : ["发文量"];
               var myChart = ec.init(selector[0]);
               window.onresize = myChart.resize;
               myChart.setOption({
                   title: {
                       text: title,
                       x: 'center'
                   },
                   tooltip: {
                       trigger: 'item',
                   },
                   legend: {
                       data: legendData,
                       y: height - 110,
                       x: 'left'
                   },
                   color: ['#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'],
                   grid: { height: height - 200 },
                   calculable: true,
                   xAxis: [
                       {
                           type: 'category',
                           data: cates
                       }
                   ],
                   yAxis: [
                       {
                           type: 'value',
                           splitArea: { show: true },
                           axisLabel: {
                               formatter: function (v) {
                                   if (v < 1000) {
                                       return v;
                                   } else {
                                       return (v / 1000) + 'k';
                                   }
                               }
                           }
                       }
                   ],
                   series: series
               });
           }
       }
   );
}

/*
*Draw the column charts.
*/
function drawZoomColumnChart(selector, title, xtitle, ytitle, series) {
    //if ($(selector).length < 1) return;

    //if (series == null || series == "") {
    //    $(selector).parent().hide();
    //    return;
    //}
    //$(selector).highcharts({
    //    credits: {
    //        enabled: false
    //    },
    //    chart: {
    //        type: "column",
    //        backgroundColor: '#f4f4f4',
    //        plotBackgroundColor: null, //'#2f7ed8'背景颜色
    //        plotBorderWidth: null
    //    },
    //    exporting: {
    //        enabled: false
    //    },
    //    title: {
    //        text: title
    //    },
    //    legend: {
    //        borderWidth: 1,
    //        backgroundColor: '#FFFFFF',
    //        shadow: true
    //    },
    //    xAxis: {
    //        categories: [''],
    //        allowDecimals: false, //轴上的刻度是否允许使用小数
    //        title: {
    //            text: xtitle
    //        },
    //        labels: {
    //            formatter: function () {
    //                return this.value;
    //            }
    //        },
    //        tickmarkPlacement: "on",
    //        startOnTick: false //强制轴以刻度开始
    //    },
    //    yAxis: {
    //        lineWidth: 1, //基线宽度 
    //        tickWidth: 1,
    //        allowDecimals: false,
    //        min: 0, //y轴值以0开始
    //        title: {
    //            text: ytitle
    //        },
    //        labels: {
    //            format: '{value}'
    //        }
    //    },
    //    tooltip: {
    //        formatter: function () {
    //            return '<b>' + '相关发文量:' + this.point.y + '篇</b>';
    //        },
    //        //控制十字线
    //        crosshairs: [{
    //            width: 1,
    //            color: "#CCC",
    //            dashStyle: "ShortDot"
    //        },
    //        {
    //            width: 1,
    //            color: "#CCC",
    //            dashStyle: "ShortDot"
    //        }]
    //    },
    //    plotOptions: {
    //        column: {
    //            pointPadding: 0.2,
    //            borderWidth: 0,
    //            pointWidth: 30
    //        }
    //    },
    //    series: series
    //});
}