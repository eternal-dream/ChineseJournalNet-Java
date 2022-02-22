/**
 * 一些通用工具类
 * */

export const commonUtils= {
  processItems: processItems,
  familyTree: familyTree,
  formatTag: formatTag,
  formatFacet: formatFacet,
  sliceText: sliceText,
  removeByValue: removeByValue,
  uniqueEs6: uniqueEs6,
  isExistByValue: isExistByValue,
  htmlToText: htmlToText,
  getBrowser: getBrowser
}

/**
 * 组装树形结构
 * @param classData
 * @returns {[]}
 */
function processItems (classData) {
  function findChildren(classData,node) {
    if (!node.children){
      node.children = [];
    }
    var nid = node.id;
    classData.forEach((item)=>{
      if (item.parentId==nid){
        node.children.push(item);
      }
    })
    if (node.children.length<=0){
      delete node.children;
    }

  }
  var data = [];
  classData.forEach((it)=>{
    if (it.parentId==null){
      data.push(it);
    }
    findChildren(classData,it);

  })
  if(data.length<=0){
    return classData;
  }else {
    return data;
  }
}

/**
 * 递归查找父节点
 * @param arr1
 * @param id
 * @returns {[]}
 */
function familyTree (arr1, id) {
  var temp = []
  var forFn = function (arr, id) {
    for (var i = 0; i < arr.length; i++) {
      var item = arr[i]
      if (item.id == id) {
        temp.push(item)
        forFn(arr1, item.parentId)
        break
      } else {
        if (item.children) {
          forFn(item.children, id)
        }
      }
    }
  }
  forFn(arr1, id)
  return temp
}

/**
 * 格式化期刊标识(先去除标识中的数字，然后分割再去重)
 * @param value
 * @returns {[]}
 */
function formatTag(value){
  if (!value){
    return []
  }
  let replace = value.replace(/\d/g,"")
  replace = replace.replace(/\_/g,"")
  let arr = replace.split(";")
  let newsArr = [];
  for (let i = 0; i < arr.length; i++) {
    if(newsArr.indexOf(arr[i]) === -1){
      if (arr[i] != null && arr[i] != ""){
        newsArr.push(arr[i]);
      }
    }
  }
  return newsArr;
}

/**
 * 针对es的classTypes聚类，组装一下子级和父级
 * @param value
 * @returns {[]}
 */
function formatFacet(value){
  let fatherArr = []
  let sonArr = []
  for (let i = 0; i < value.length; i++) {
    if (value[i].key.indexOf("—") ==-1){
      fatherArr.push(value[i])
    }else{
      sonArr.push(value[i])
    }
  }
  for (let i = 0; i < fatherArr.length; i++) {
    let children = fatherArr[i].children=[]
    for (let j = 0; j < sonArr.length; j++) {
      if (sonArr[j].key.indexOf(fatherArr[i].key) !=-1){
        children.push(sonArr[j])
      }
    }
  }
  return fatherArr
}

/**
 * 按指定长度切割字符串
 * @param text
 * @param length
 * @returns {string|*}
 */
function sliceText(text,length){
  if (!text) return '' //如果没有返回空
  if (text.length > length) {
    return text.slice(0, length) + '...' //长度大于100的后面用......代替
  } else {
    return text;
  }
}

/**
 * 根据对象的属性和值删除当前数组内的某对象
 * @param arr 需要删除对象的数组
 * @param attr 需要删除的对象的属性名
 * @param value 属性值
 */
function removeByValue(arr, attr, value) {
  let index=0;
  for(let i in arr){
    if(arr[i][attr]==value){
      index=i;
      break;
    }
  }
  arr.splice(index,1);
}

/**
 * es6对象数组去重方法
 * @param arr1
 * @returns {*}
 */
function uniqueEs6(arr1) {
  const res = new Map();
  return arr1.filter((a) => !res.has(a.from) && res.set(a.from, 1))
}

/**
 * 根据对象的属性和值判断数组内是否存在某个对象
 * @param arr 需要判断对象的数组
 * @param attr 需要判断的对象的属性名
 * @param value 属性值
 */
function isExistByValue(arr, attr, value){
  let exist = false
  for (let i in arr) {
    if (arr[i][attr] == value){
      exist = true
      break
    }
  }
  return exist
}

/**
 * 带格式的html转换为纯文本
 * @param HTML
 * @returns {string}
 */
function htmlToText(HTML) {
  let input = HTML
  if (input == null) {
    return ''
  } else {
    var res = input.replace(/<(style|script|iframe)[^>]*?>[\s\S]+?<\/\1\s*>/gi, '').replace(/<[^>]+?>/g, '').replace(/\s+/g, ' ').replace(/ /g, ' ').replace(/>/g, ' ').replace(/&nbsp;/g, ' ')
  }
  return res;
}

/**
 * 获取浏览器版本和型号
 * @returns {{type，versions}}
 */
function getBrowser() {
  var UserAgent = navigator.userAgent.toLowerCase();
  var browserInfo = {}
  var browserArray = {
    IE: window.ActiveXObject || 'ActiveXObject' in window, // IE
    Chrome: UserAgent.indexOf('chrome') > -1 && UserAgent.indexOf('safari') > -1, // Chrome浏览器
    Firefox: UserAgent.indexOf('firefox') > -1, // 火狐浏览器
    Opera: UserAgent.indexOf('opera') > -1, // Opera浏览器
    Safari: UserAgent.indexOf('safari') > -1 && UserAgent.indexOf('chrome') == -1, // safari浏览器
    Edge: UserAgent.indexOf('edge') > -1, // Edge浏览器
    QQBrowser: /qqbrowser/.test(UserAgent), // qq浏览器
    WeixinBrowser: /MicroMessenger/i.test(UserAgent) // 微信浏览器
  }
  // console.log(browserArray)
  for (var i in browserArray) {
    if (browserArray[i]) {
      var versions = ''
      if (i == 'IE') {
        versions = UserAgent.match(/(msie\s|trident.*rv:)([\w.]+)/)[2]
      } else if (i == 'Chrome') {
        for (var mt in navigator.mimeTypes) {
          //检测是否是360浏览器(测试只有pc端的360才起作用)
          if (navigator.mimeTypes[mt]['type'] == 'application/360softmgrplugin') {
            i = '360'
          }
        }
        versions = UserAgent.match(/chrome\/([\d.]+)/)[1]
      } else if (i == 'Firefox') {
        versions = UserAgent.match(/firefox\/([\d.]+)/)[1]
      } else if (i == 'Opera') {
        versions = UserAgent.match(/opera\/([\d.]+)/)[1]
      } else if (i == 'Safari') {
        versions = UserAgent.match(/version\/([\d.]+)/)[1]
      } else if (i == 'Edge') {
        versions = UserAgent.match(/edge\/([\d.]+)/)[1]
      } else if (i == 'QQBrowser') {
        versions = UserAgent.match(/qqbrowser\/([\d.]+)/)[1]
      }
      browserInfo.type = i
      browserInfo.versions = parseInt(versions)
    }
  }
  return browserInfo
}
