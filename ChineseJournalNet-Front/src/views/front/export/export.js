import {axiosOptions, axiosRequest, commonContentType} from "@api/service";
import {FRONT_API_PREFIX} from "@api/common/commonUrl";
import {saveAs} from 'file-saver';
import XLSX from 'xlsx';

export default {
  name: 'ExportIndex',
  data() {
    return {
      ids: [],
      articleList: [],
      //显示类型
      showType: ["文  本", "查新格式", "参考文献", "XML", "NoteExpress", "Refworks", "EndNote", "Note First", "自定义导出", "Excel导出"],
      //自定义导出条件
      checkBoxes: ["题名", "作者", "刊名", "机构", "文摘", "ISSN", "CN", "页码", "关键词", "分类号", "网址"],
      //不同类型显示
      typeIndex: 0,
      //选中的数组
      checked: ["题名", "作者", "刊名", "机构", "文摘", "ISSN", "CN", "页码", "关键词", "分类号", "网址"],
    }
  },
  methods: {

    /**
     * 从路由获取参数并查询
     */
    getIdsFromRouter() {
      this.ids = this.$route.query.ids;
      if (this.ids.length > 0) {
        this.getArticleList()
      } else {
        alert("没有选择需要导出的数据！")
        this.$router.go(-1)
      }
    },
    /**
     * 针对当前solr版本，需要将文献内容提出来
     * @param arr
     */
    formatArticle(arr) {
      let newArr = []
      arr.forEach(item => {
        newArr.push(item.ndo_content)
      })
      return newArr
    },
    /**
     * 获取文献列表
     */
    getArticleList() {
      let _ids = this.ids
      axiosRequest(FRONT_API_PREFIX + 'search/getArticlesByIds',
        _ids,
        axiosOptions(null, commonContentType.json))
        .then(res => {
          if (res.success) {
            this.articleList = this.formatArticle(res.pageBean.rows)
          }
        })
    },
    /**
     * 切换显示
     * @param index
     */
    changeSHowType(index) {
      this.typeIndex = index
      if (index == 9) {
        this.exportExcel()
      }
    },
    //取消按钮
    cancelChoose() {
      this.checked = []
    },
    //确定自定义导出
    customExport() {
      let arr = this.checked;
      if (arr.length <= 0) {
        this.$message.error("请选择需要导出的数据！")
        return
      }
      let s = this.buildCuscom()
      let blob = new Blob([s], {type: "text/plain;charset=utf-8"});
      let name = this.$moment(new Date()).format('YYYY-MM-DD') + "@cuscom.txt";
      saveAs(blob, name);
    },
    // todo 组装导出数据内容开始
    /**
     * 组装文本TXT
     * @returns {string}
     */
    buildTxt() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "[" + (i + 1) + "]" + " " + (arr[i].showwriter ? arr[i].showwriter.replace(/\[.*?\]/g, '').replace(";", ",") + "." : "") + arr[i].title_c + (arr[i].media_c ? "." + arr[i].media_c + "[J]" : "") + "," + arr[i].years +
          "(" + arr[i].num + "):" + (arr[i].beginpage ? arr[i].beginpage + "-" : "") + arr[i].endpage + (arr[i].remark_c ? "\r\n" + arr[i].remark_c : "") + "\r\n\r\n";
      }
      return str.replace(/undefined/g, '');
    },
    /**
     * 查新格式
     */
    buildChaxin() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "[" + (i + 1) + "]" + (arr[i].showwriter ? arr[i].showwriter.replace(/\[.*?\]/g, '').replace(";", ",") + "." : "") + arr[i].title_c + (arr[i].media_c ? "." + arr[i].media_c + "[J]" : "") + "," + arr[i].years +
          "(" + arr[i].num + "):" + (arr[i].beginpage ? arr[i].beginpage + "-" : "") + arr[i].endpage + ".\r\n机构："
        if (arr[i].showorgan) {
          str += arr[i].showorgan.replace(/\[.*?\]/g, '')
        } else {
          str += ''
        }
        str += "\r\n摘要：" + arr[i].remark_c + "\r\n\r\n";
      }
      return str.replace(/undefined/g, '');
    },
    /**
     * 参考文献
     */
    buildRef() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "[" + (i + 1) + "]" + " " + (arr[i].showwriter ? arr[i].showwriter.replace(/\[.*?\]/g, '').replace(";", ",") + "." : "")
          + arr[i].title_c +'[J]'+ arr[i].media_c + "," + arr[i].years + "," +
          "(" + arr[i].num + "):" + arr[i].beginpage + "-" + arr[i].endpage + "\r\n \r\n";
      }
      return str.replace(/undefined/g, '');
    },
    //xml
    buildXml() {
      let str = "&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;" + "\r\n &lt;ResourceList&gt;"
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        debugger
        str += "\r\n\r\n" + "&lt;PeriodicalPaper xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"&gt;\r\n" +
          "    &lt;Type&gt;" + (i + 1) + "&lt;/Type&gt;\r\n" +
          "    &lt;ID&gt;" + arr[i].lngid + "&lt;/ID&gt;\r\n" +
          "    &lt;Titles&gt;\r\n" +
          "    &lt;Title&gt;\r\n" +
          "    &lt;Language&gt;chi&lt;/Language&gt;\r\n" +
          "    &lt;Text&gt;&lt;![CDATA[" + arr[i].title_c + "]]&gt;&lt;/Text&gt;\r\n" +
          "    &lt;/Title&gt;\r\n" +
          "    &lt;/Titles&gt;\r\n" +
          "    &lt;Creators&gt;\r\n"
        let writters = arr[i].showwriter
        if (writters) {
          if (writters.indexOf(";") != -1) {
            let split = writters.split(";")
            split.forEach(item => {
              str +=
                "    &lt;Creator&gt;\r\n" +
                "    &lt;Name&gt;&lt;![CDATA[" + item + "]]&gt;&lt;/Name&gt;\r\n" +
                "    &lt;/Creator&gt;\r\n"
            })
          } else {
            str +=
              "    &lt;Creator&gt;\r\n" +
              "    &lt;Name&gt;&lt;![CDATA[" + writters + "]]&gt;&lt;/Name&gt;\r\n" +
              "    &lt;/Creator&gt;\r\n"
          }
        } else {
          str +=
            "    &lt;Creator&gt;\r\n" +
            "    &lt;Name&gt;&lt;![CDATA[]]&gt;&lt;/Name&gt;\r\n" +
            "    &lt;/Creator&gt;\r\n"
        }
        str +=
          "    &lt;/Creators&gt;\r\n" +
          "    &lt;PublishDate&gt;&lt;![CDATA[" + arr[i].publishdate + "]]&gt;&lt;/PublishDate&gt;\r\n" +
          "    &lt;CLCs&gt;\r\n"
          if (arr[i].class){
            if (arr[i].class.indexOf(';') != -1){
              let split = arr[i].class.split(';')
              split.forEach(item =>{
                str += "    &lt;CLC&gt;\r\n" +
                  "    &lt;Code&gt;&lt;![CDATA[" + item + "]]&gt;&lt;/Code&gt;\r\n" +
                  "    &lt;/CLC&gt;\r\n"
              })
            }else {
              str += "    &lt;CLC&gt;\r\n" +
                "    &lt;Code&gt;&lt;![CDATA[" + arr[i].class + "]]&gt;&lt;/Code&gt;\r\n" +
                "    &lt;/CLC&gt;\r\n"
            }
          }
        str +=
          "    &lt;/CLCs&gt;\r\n" +
          "    &lt;HasOriginalDoc&gt;true&lt;/HasOriginalDoc&gt;\r\n" +
          "    &lt;DataSource&gt;&lt;/DataSource&gt;\r\n" +
          "    &lt;Keywords&gt;\r\n"
        let keywords = arr[i].keyword_c
        if (keywords) {
          if (keywords.indexOf(";") != -1) {
            let splitKeyword = keywords.split(";")
            splitKeyword.forEach(item => {
              str += "    &lt;Keyword&gt;&lt;![CDATA[" + item.replace(/\[.*?\]/g, '') + "]]&gt;&lt;/Keyword&gt;\r\n"
            })
          } else {
            str += "    &lt;Keyword&gt;&lt;![CDATA[" + keywords.replace(/\[.*?\]/g, '') + "]]&gt;&lt;/Keyword&gt;\r\n"
          }
        } else {
          str += "    &lt;Keyword&gt;&lt;![CDATA[]]&gt;&lt;/Keyword&gt;\r\n"
        }
        str +=
          "    &lt;/Keywords&gt;\r\n" +
          "    &lt;Abstracts&gt;\r\n" +
          "    &lt;Abstract&gt;\r\n" +
          "    &lt;Text&gt;&lt;![CDATA[" + arr[i].remark_c + "]]&gt;&lt;/Text&gt;\r\n" +
          "    &lt;/Abstract&gt;\r\n" +
          "    &lt;/Abstracts&gt;\r\n" +
          "    &lt;Periodical&gt;\r\n" +
          "    &lt;ID&gt;" + arr[i].gch + "&lt;/ID&gt;\r\n" +
          "    &lt;Name&gt;&lt;![CDATA[" + arr[i].media_c + "]]&gt;&lt;/Name&gt;\r\n" +
          "    &lt;ISSN&gt;" + arr[i].issn + "&lt;/ISSN&gt;\r\n" +
          "    &lt;/Periodical&gt;\r\n" +
          "    &lt;Column&gt;&lt;/Column&gt;\r\n"
        if (arr[i].vol){

          str += "    &lt;Volum&gt;&lt;![CDATA["+ arr[i].vol +"]]&gt;&lt;/Volum&gt;\r\n"
        }
        str +=
          "    &lt;Issue&gt;&lt;![CDATA[" + arr[i].num + "]]&gt;&lt;/Issue&gt;\r\n" +
          "    &lt;Page&gt;" + arr[i].beginpage + "-" + arr[i].endpage + "&lt;/Page&gt;\r\n" +
          "    &lt;Organs&gt;\r\n"
        let organs = arr[i].showorgan
        if (organs) {
          if (organs.indexOf(";") != -1) {
            let splitOrgan = organs.split(";")
            splitOrgan.forEach(item => {
              str += "    &lt;Organ&gt;&lt;![CDATA[" + item.replace(/\[.*?\]/g, '') + "]]&gt;&lt;/Organ&gt;\r\n"
            })
          } else {
            str += "    &lt;Organ&gt;&lt;![CDATA[" + organs.replace(/\[.*?\]/g, '') + "]]&gt;&lt;/Organ&gt;\r\n"
          }
        } else {
          str += "    &lt;Organ&gt;&lt;![CDATA[]]&gt;&lt;/Organ&gt;\r\n"
        }
        str +=
          "    &lt;/Organs&gt;\r\n" +
          "    &lt;ServiceMode&gt;1&lt;/ServiceMode&gt;\r\n" +
          "    &lt;/PeriodicalPaper&gt;";
      }
      str += "\r\n" + "    &lt;/ResourceList&gt;"
      return str.replace(/undefined/g, '')
        .replace(/&gt;/g, ">")
        .replace(/&lt;/g, "<");
    },
    //NoteExpress
    buildNote() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "{Reference Type}: Journal Article \r\n";
        str += "{Title}:" + arr[i].title_c + "\r\n"
        let writters = arr[i].showwriter
        if (writters) {
          if (writters.indexOf(";") != -1) {
            let split = writters.split(";")
            split.forEach(item => {
              str += "{Author}: " + item.replace(/\[.*?\]/g, '') + "\r\n"
            })
          } else {
            str += "{Author}: " + writters.replace(/\[.*?\]/g, '') + "\r\n"
          }
        }
        str += "{Journal}: " + arr[i].media_c + "\r\n{Year}: " +
          arr[i].years
        if (arr[i].vol) {
          str += "\r\n{Volume} : " + arr[i].vol
        }
        str += "\r\n" + "{Issue}: " + arr[i].num + "\r\n"
        let keywords = arr[i].keyword_c
        if (keywords) {
          if (keywords.indexOf(";") != -1) {
            let splitKeyword = keywords.split(";")
            splitKeyword.forEach(item => {
              str += "{Keywords}:" + item.replace(/\[.*?\]/g, '') + "\r\n"
            })
          } else {
            str += "{Keywords}:" + keywords.replace(/\[.*?\]/g, '') + "\r\n"
          }
        }
        str += "{Abstract}: " + arr[i].remark_c + "\r\n{URL}: " + window.location.href.match(/^(?:[^\/]|\/\/)*/) + "/detail/index?lngid=" +
          arr[i].lngid + "\r\n{Database Provider}: 重庆维普资讯有限公司\r\n \r\n \r\n \r\n"
      }
      return str.replace(/undefined/g, '');
    },
    //Refworks
    buildRefworks() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "RT Journal \r\n";
        str += "T1 " + arr[i].title_c + "\r\n" +
        "JF " + arr[i].media_c + "\r\n" +
        "YR " + arr[i].years + "\r\n" +
        "VO " + arr[i].vol  + "\r\n" +
        "IS " + arr[i].num + "\r\n" +
        "AB " + arr[i].remark_c + "\r\n" +
        "LK " + window.location.href.match(/^(?:[^\/]|\/\/)*/) + "/detail/index?lngid=" + arr[i].lngid + "\r\n" +
        "A1 " + (arr[i].showwriter ? arr[i].showwriter.replace(/\[.*?\]/g, '') : '') + "\r\n" +
        "AD " + (arr[i].showorgan ? arr[i].showorgan.replace(/\[.*?\]/g, '') : '') +"\r\n" +
        "CL " + arr[i].class + "\r\n" +
        "K1 " + (arr[i].keyword_c ? arr[i].keyword_c.replace(/\[.*?\]/g, '') : '') + "\r\nPP 中国 \r\nDS 重庆维普 \r\nNO 作者个数:" +
        (arr[i].showwriter ? arr[i].showwriter.split(";").length : 0) + "; 第一作者:" + arr[i].firstwriter + '\r\n\r\n'
      }
      return str.replace(/undefined/g, '');
    },
    //EndNote
    buildEndNote() {
      let str = "";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "%0 Journal Article \r\n";
        str += "%A " + arr[i].showwriter + "\r\n%+:" +
          (arr[i].showorgan ? arr[i].showorgan.replace(/\[.*?\]/g, '') : '') + "\r\n%T " + arr[i].title_c + "\r\n%J " +
          arr[i].media_c + "\r\n%D " + arr[i].years + "\r\n%N " + arr[i].num +
          "\r\n%V " + arr[i].vol+ "\r\n%K " +
          (arr[i].keyword_c ? arr[i].keyword_c.replace(/\[.*?\]/g, '') : '') + "\r\n%X " + arr[i].remark_c + "\r\n%U " + window.location.href.match(/^(?:[^\/]|\/\/)*/) + "/detail/index?lngid=" + arr[i].lngid +
          "\r\n%W 重庆维普资讯有限公司 \r\n\r\n"
      }
      return str.replace(/undefined/g, '');
    },
    // Note First
    buildNoteFirst() {
      let arr = this.articleList
      let str = "&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;" + "\r\n &lt;Bibliographies&gt;\r\n&lt;BibliographiesCount&gt;"
        + arr.length + "&lt;/BibliographiesCount&gt;\r\n"
      for (let i = 0; i < arr.length; i++) {
        let item = arr[i]
        str +=
          "&lt;Bibliography&gt;\r\n" +
          "&lt;Type&gt;JournalArticle&lt;/Type&gt;\r\n" +
          "&lt;Language&gt;zh-CHS&lt;/Language&gt;\r\n" +
          "&lt;PrimaryTitle&gt; &lt;Title Lang=\"zh-CHS\"&gt;&lt;![CDATA[" + item.title_c + "]]&gt;&lt;/Title&gt;&lt;/PrimaryTitle&gt;\r\n" +
          "&lt;Authors&gt;&lt;Author&gt;&lt;Info lang=\"zh-CHS\"&gt;\r\n" +
          "&lt;FullName&gt;&lt;![CDATA[" + (item.showwriter ? item.showwriter.replace(/\[.*?\]/g, '') : '') + "]]&gt;&lt;/FullName&gt;\r\n" +
          "&lt;Organization&gt;&lt;![CDATA[[1]" + (item.showorgan ? item.showorgan.replace(/\[.*?\]/g, '') : '') + "]]&gt;&lt;/Organization&gt;\r\n" +
          "&lt;/Info&gt;&lt;/Author&gt;&lt;/Authors&gt;\r\n" +
          "&lt;Medium&gt;&lt;Media Lang=\"zh-CHS\"&gt;&lt;![CDATA[" + item.media_c + "]]&gt;&lt;/Media&gt;&lt;/Medium&gt;\r\n" +
          "&lt;Year&gt;&lt;![CDATA[" + item.years + "]]&gt;&lt;/Year&gt;\r\n" +
          "&lt;Volume&gt;&lt;![CDATA[" + item.volumn + "]]&gt;&lt;/Volume&gt;\r\n" +
          "&lt;Issue&gt;&lt;![CDATA[" + item.num + "]]&gt;&lt;/Issue&gt;\r\n" +
          "&lt;Keywords&gt;\r\n" +
          "&lt;Keyword Lang=\"zh-CHS\"&gt;&lt;![CDATA[" + item.keyword_c + "]]&gt;&lt;/Keyword&gt;\r\n" +
          "&lt;Keyword Lang=\"en\"&gt;&lt;![CDATA[" + item.keyword_e + "]]&gt;&lt;/Keyword&gt;\r\n" +
          "&lt;/Keywords&gt;\r\n" +
          "&lt;Abstracts&gt;&lt;Abstract Lang=\"zh-CHS\"&gt;\r\n" +
          "&lt;![CDATA[" + item.remark_c + "]]&gt;\r\n" +
          "&lt;/Abstract&gt; &lt;/Abstracts&gt;\r\n" +
          "&lt;PageScope&gt;&lt;![CDATA[" + item.beginpage + "-" + item.endpage + "]]&gt;&lt;/PageScope&gt;\r\n" +
          "&lt;Publisher&gt;&lt;![CDATA[" + item.publisher + "]]&gt;&lt;/Publisher&gt;\r\n" +
          "&lt;Code&gt;&lt;![CDATA[" + item.class + "]]&gt;&lt;/Code&gt;\r\n" +
          "&lt;/Bibliography&gt;\r\n\r\n"

      }
      str += "&lt;/Bibliographies&gt;"
      return str.replace(/undefined/g, '')
        .replace(/&gt;/g, ">")
        .replace(/&lt;/g, "<");
    },
    //自定义导出数据组装
    buildCuscom() {
      let check = this.checked
      let str = "〖检索时间〗" + this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss') + "\r\n " +
        "〖检索结果〗选中" + this.ids.length + "篇\r\n";
      let arr = this.articleList
      for (let i = 0; i < arr.length; i++) {
        str += "【序  号】" + (i + 1) + "/" + arr.length + "\r\n"
        if (check.indexOf("题名") != -1) {
          str += "【题　名】" + arr[i].title_c + "\r\n"
        }
        if (check.indexOf("作者") != -1) {
          str += "【作　者】" + (arr[i].showwriter ? arr[i].showwriter.replace(/\[.*?\]/g, '') : '') + "\r\n"
        }
        if (check.indexOf("机构") != -1) {
          str += "【机　构】" + (arr[i].showorgan ? arr[i].showorgan.replace(/\[.*?\]/g, '') : '') + "\r\n"
        }
        if (check.indexOf("刊名") != -1) {
          str += "【刊　名】" + arr[i].media_c + arr[i].years + " " + arr[i].vol + "(" + arr[i].num + "):" + arr[i].beginpage + "-" + arr[i].endpage + "\r\n"
        }
        if (check.indexOf("ISSN") != -1) {
          str += "【ISSN号】" + arr[i].issn + "\r\n"
        }
        if (check.indexOf("CN") != -1) {
          str += "【C N 号】" + arr[i].cn + "\r\n"
        }
        if (check.indexOf("页码") != -1) {
          str += "【页　码】" + arr[i].beginpage + "-" + arr[i].endpage + "\r\n"
        }
        if (check.indexOf("关键词") != -1) {
          str += "【关键词】" + arr[i].keyword_c + "\r\n"
        }
        if (check.indexOf("分类号") != -1) {
          str += "【分类号】" + arr[i].class + "\r\n"
        }
        if (check.indexOf("文摘") != -1) {
          str += "【文　摘】" + arr[i].remark_c + "\r\n"
        }
        if (check.indexOf("网址") != -1) {
          str += "【网　址】" + window.location.href.match(/^(?:[^\/]|\/\/)*/) + "/detail/index?lngid=" + arr[i].lngid + "\r\n"
        }
        str += "\r\n\r\n"
      }
      return str.replace(/undefined/g, '');
    },

    // todo 组装导出数据内容结束
    /**
     * 导出excel
     */
    exportExcel() {
      let vb = XLSX.utils.table_to_book(document.getElementById('outTable'));
      let vbout = XLSX.write(vb, {bookType: 'xlsx', bookSST: true, type: 'array'});
      let fileName = this.$moment(new Date()).format('YYYY-MM-DD') + "cqvip.xlsx";
      saveAs(new Blob([vbout], {type: 'application/octet-stream'}), fileName);
    },
    //导出
    exportFile() {
      let willExport;
      let index = this.typeIndex;
      let fileName = this.$moment(new Date()).format('YYYY-MM-DD');
      switch (index) {
        case 0:
          willExport = this.buildTxt();
          fileName += "@txt.txt";
          break;
        case 1:
          willExport = this.buildChaxin();
          fileName += "@cx.txt";
          break;
        case 2:
          willExport = this.buildRef();
          fileName += "@ref.txt";
          break;
        case 3:
          willExport = this.buildXml();
          fileName += "@xml.xml";
          break;
        case 4:
          willExport = this.buildNote();
          fileName += "@note.txt";
          break;
        case 5:
          willExport = this.buildRefworks();
          fileName += "@rew.txt";
          break;
        case 6:
          willExport = this.buildEndNote();
          fileName += "@end.txt";
          break;
        case 7:
          willExport = this.buildNoteFirst();
          fileName += "@nf.txt"
          break
        case 8:
          willExport = this.buildCuscom()
          fileName += "@cus.txt"
        default:
          break;
      }
      let blob = new Blob([willExport], {type: "text/plain;charset=utf-8"});
      saveAs(blob, fileName);
    },
    //打印
    printFile() {
      window.print();
    },
    windowPath(item) {
      return window.location.href.match(/^(?:[^\/]|\/\/)*/) + "/detail/index?lngid=" + item.lngid.replace(/\<.*?\>/g, '')
    },
    // 复制
    doCopy() {
      let willExport;
      let index = this.typeIndex;
      switch (index) {
        case 0:
          willExport = this.buildTxt();
          break;
        case 1:
          willExport = this.buildChaxin();
          break;
        case 2:
          willExport = this.buildRef();
          break;
        case 3:
          willExport = this.buildXml();
          break;
        case 4:
          willExport = this.buildNote();
          break;
        case 5:
          willExport = this.buildRefworks();
          break;
        case 6:
          willExport = this.buildEndNote();
          break;
        case 7:
          willExport = this.buildNoteFirst();
          break
        default:
          break;
      }
      this.$copyText(willExport).then(e => {
        this.$message.success('内容已成功复制到剪贴板！')
      }).catch(err => {
        this.$message.error("复制内容出错！")
      })
    },

  },
  created() {
    this.getIdsFromRouter()
  }
}
