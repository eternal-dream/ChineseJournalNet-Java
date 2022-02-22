/**
 * 解析blob响应内容并下载
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 */
export function resolveBlob (res, mimeType) {
  const aLink = document.createElement('a')
  var blob = new Blob([res.data], { type: mimeType })
  // //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
  var patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*')
  var contentDisposition = decodeURI(res.headers['content-disposition'])
  var result = patt.exec(contentDisposition)
  var fileName = result[1]
  fileName = fileName.replace(/\"/g, '')
  aLink.href = URL.createObjectURL(blob)
  aLink.setAttribute('download', fileName) // 设置下载文件名称
  document.body.appendChild(aLink)
  aLink.click()
  document.body.appendChild(aLink)
}

/**
 * 解析blob响应内容并下载 (兼容ie)
 * @param res
 */
export function downloadFile (res) {
  const disposition = res.headers['content-disposition']
  // 拿到文件名
  let filename = disposition.split('filename=')[1]
  const blob = new Blob([res.data])
  const iconv = require('iconv-lite')
  iconv.skipDecodeWarning = true// 忽略警告
  filename = iconv.decode(filename, 'utf-8')
  // 兼容ie
  if ('msSaveOrOpenBlob' in navigator) {
    window.navigator.msSaveOrOpenBlob(blob, decodeURI(filename))
  } else {
    const URL = window.URL || window.webkitURL
    const objectUrl = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = objectUrl
    a.download = decodeURI(filename)
    a.click()
    URL.revokeObjectURL(objectUrl)
  }
}

export function readPdf (lngid) {
  return new Promise((resolve, reject) => {
    //判断是否超过限制
    this.$axiosRequest("/view/detail/readLimitCheck", { lngid: lngid }, this.$axiosOptions('get')).then(res => {
      console.log('res', res)
      if (res.data.limited) {
        this.$confirm(res.data.msg, '提示', {
          showCancelButton: false,
          type: 'warning'
        })
        resolve(res);
      } else {
        lngid = encodeURIComponent(lngid)
        let url = process.env.VUE_APP_API + "view/detail/readPdf?lngid=" + lngid;
        let value = window.location.origin;
        window.open(value + "/pdfjs/web/viewer.html?file=" + encodeURIComponent(url));
      }
    });
  });
}

export function downloadPdf (lngid) {
  const loading = this.$loading({
    //同v-loading的修饰符
    lock: true,
    //加载文案
    text: '下载中...',
    //背景色
    backgroundColor: 'rgba(55,55,55,0.4)',
    //加载图标
    spinner: 'el-icon-loading',
    //loading需要覆盖的DOM节点，默认为body
    target: document.querySelector(".el-table__body")
  })
  return new Promise((resolve, reject) => {
    //判断是否超过限制
    this.$axiosRequest("/view/detail/downloadLimitCheck",{lngid:lngid},this.$axiosOptions('get')).then(res=>{
      console.log('res', res)
      if(res.data.limited){
        this.$confirm(res.data.msg, '提示', {
          showCancelButton: false,
          type:  'warning'
        })
        loading.close()
        resolve(res);
      }else{
        this.$axiosRequest("/view/detail/downloadPdf",{lngid:lngid},this.$axiosOptions("get",null,"blob"))
          .then(res=>{
            if(!res.data.size>0){
              this.$confirm("文件不存在,下载失败!", '提示', {
                showCancelButton: false,
                type:  'warning'
              })
              resolve(res);
              return;
            }
            downloadFile(res)
            resolve(res);
          }).catch((error) => {
          reject(error)
        }).finally(e=>{
          loading.close()
        });
      }
    })

  })
}
