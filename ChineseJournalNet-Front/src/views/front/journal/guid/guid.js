import {getMediaFacet, getMediaPage} from "@api/view/jounrnal.api";

export default {
  name: "JournalGuid",
  data() {
    return {
      loadDefaultImg: 'this.src="' + require('@/assets/images/default_qk.jpg') + '"',
      //加载动画
      loading: false,
      //当前页面右侧显示期刊列表还是学科分类列表,true是学科分类，false是期刊列表
      classOrMedia: true,
      //检索下拉框
      selectOptions: [{
        value: 'MN',
        label: '刊名'
      }, {
        value: 'ISSN',
        label: 'issn'
      }, {
        value: 'CN',
        label: 'cn'
      }, {
        value: 'PB',
        label: '主办单位'
      }, {
        value: 'ED',
        label: '主编'
      }, {
        value: 'PS',
        label: '邮发代号'
      }],
      // 检索用参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        searchModels: [],
        sortField: 'zkbycount',
        isAsc: false
      },
      //通用的检索实体
      commonSearchParams: {
        //检索字段（参考上面的检索条件数组）
        searchField: 'MN',
        //检索关键词
        value: undefined,
        //逻辑运算符
        logicOperator: 'AND',
        displayName: undefined,
        //模糊检索false或者精确检索true
        exact: false
      },
      //按字母检索的大写字母
      theLetters: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
      //核心期刊收录聚类数据
      providerFacets: {value: [], limit: 5, showAll: false},
      //核心期刊点击后，需要提交的检索参数
      providerFacetsList: [],
      //国外收录聚类数据
      outsideFacets: {value: [], limit: 5, showAll: false},
      //国外数据库点击后，需要提交的检索参数
      outsideFacetsList: [],
      //地区聚类数据
      areaFacets: {value: [], limit: 5, showAll: false},
      //地区聚类点击后需要提交的参数
      areaFacetsList: [],
      //主题聚类数据
      subjectFacets: {value: [], limit: 5, showAll: false},
      //主题聚类点击后需要提交的参数
      subjectFacetsList: [],
      //学科分类聚类数据
      classTypeFacets: {value: [], limit: 5, showAll: false},
      //学科分类聚类点击后需要提交的参数
      classTypeFacetsList: [],
      // 期刊总数
      total: 0,
      // 期刊数据
      mediaList: [],
      // 封面显示还是列表显示，true是封面显示，false是列表显示，默认封面显示
      coverOrTable: true,
      rowsList: [0, 1, 2, 3]
    }
  },
  methods: {
    /**
     * 获取期刊的聚类信息
     */
    getMediaFacetMethod(param) {
      this.loading = true
      getMediaFacet(param).then(res => {
        if (res.success) {
          this.providerFacets.value = this.setIsOpenChild(res.cMap, this.providerFacetsList)
          this.outsideFacets.value = this.setIsOpenChild(res.oMap, this.outsideFacetsList)
          this.areaFacets.value = this.setIsOpenChild(res.areaFacet, this.areaFacetsList)
          this.subjectFacets.value = this.setIsOpenChild(res.subjectFacet, this.subjectFacetsList)
          this.classTypeFacets.value = this.setIsOpenChild(res.classFacet, this.classTypeFacetsList)
          this.loading = false
        }
      }).catch(err =>{
        this.loading = false
      })
    },
    /**
     * 获取期刊分页的方法
     * @param param
     */
    getMediaPageMethod(param) {
      this.loading = true
      getMediaPage(param).then(res => {
        this.loading = false
        if (res.success) {
          this.total = res.pageBean.total
          this.mediaList = res.pageBean.rows
          this.queryParams.pageNum = res.pageBean.page
        }
      }).catch(err=>{
        this.loading = false
      })
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getMediaPageMethod(this.queryParams)
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getMediaPageMethod(this.queryParams)
    },
    changeSort(val) {
      this.queryParams.sortField = val
      this.queryParams.pageNum = 1
      this.getMediaPageMethod(this.queryParams)
    },
    /**
     * 按钮提交检索请求,这个时候移除所有聚类条件
     */
    submitSearch() {
      let param = this.queryParams, inputParam = this.commonSearchParams
      if (inputParam.value) {
        param.searchModels = []
        // inputParam.logicOperator = undefined
        param.searchModels.push(inputParam)
      }
      this.classOrMedia = false
      this.getMediaFacetMethod(param)
      this.getMediaPageMethod(param)
    },
    formatNumber (num) {
      if (!num) {
        return 0
      }
      return num.toString().replace(/\d+/, function (n) {
        return n.replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      })
    },
    /**
     * 初次聚类
     */
    initFacet() {
      this.commonSearchParams.value = this.$route.query.keyword || undefined
      let param = this.queryParams

      if (this.commonSearchParams.value){
        param.searchModels.push(this.commonSearchParams)
        this.classOrMedia = false
        this.coverOrTable = false
      }
      this.getMediaFacetMethod(param)
      this.getMediaPageMethod(param)
    },
    /**
     * 为了显示子集，这里要处理刚获取到的聚类数据，有子集的，要给他一个默认关闭子集的状态
     * 同时，点击聚类后下面不再显示被点击的项，这里设置一下子项的显示与否（有父子层级的，只显示父级）
     * @param val
     */
    setIsOpenChild(val, arr) {
      val.forEach((item) => {
        if (item.children && item.children.length > 0) {
          this.$set(item, 'isOpen', false)
          item.children.forEach((child) => {
            if (arr.find(it => it.value === child.key)) {
              this.$set(child, "show", false)
            }
          })
        }
        if (arr.find(it => it.value === item.key)) {
          this.$set(item, "show", false)
        }
      })
      return val
    },
    /**
     * 改变地区聚类子集的显示与否
     * @param item
     */
    changeAreaChildShow(item) {
      let now = item.isOpen
      if (now) {
        this.areaFacets.showAll = false
      } else {
        this.areaFacets.showAll = true
      }
      item.isOpen = !item.isOpen
    },
    /**
     * 改变学科分类子集显示与否
     * @param item
     */
    changeClassTypeChildShow(item) {
      let now = item.isOpen
      if (now) {
        this.classTypeFacets.showAll = false
      } else {
        this.classTypeFacets.showAll = true
      }
      item.isOpen = !item.isOpen
    },
    /**
     * 学科分类子集的显示
     */
    splitClassName(val) {
      if (val.indexOf("—") != -1) {
        return val.split("—")[1]
      }
    },
    /**
     * 聚类数据被点击后的函数
     * @param item 当前被点击的聚类项
     * @param name 当前被点击的聚类分类（'P:核心期刊','O:国内外数据库收录','S:主题','A:地区','C:学科分类'）
     * @param classOrMedia 默认为true,如果为false,则该改变右边的显示状态
     */
    clickFacetItem(item, name, classOrMedia) {
      let commonParam = {
        searchField: undefined,
        value: undefined,
        logicOperator: 'AND',
        displayName: undefined,
        exact: true
      }
      switch (name) {
        case 'P':
          commonParam.searchField = 'LNG'
          commonParam.value = item.key
          commonParam.displayName = item.label
          if (!this.providerFacetsList.some(it => it.value === item.key)) {
            this.providerFacetsList.push(commonParam)
          }
          if (!this.queryParams.searchModels.some(it => it.value === item.key && it.searchField === 'LNG')) {
            this.queryParams.searchModels.push(commonParam)
          }
          break
        case 'O':
          commonParam.searchField = 'LNG'
          commonParam.value = item.key
          commonParam.displayName = item.label
          if (!this.outsideFacetsList.some(it => it.value === item.key)) {
            this.outsideFacetsList.push(commonParam)
          }
          if (!this.queryParams.searchModels.some(it => it.value === item.key && it.searchField === 'LNG')) {
            this.queryParams.searchModels.push(commonParam)
          }
          break
        case 'S':
          commonParam.searchField = 'SUB'
          commonParam.value = item.key
          commonParam.displayName = item.label
          if (!this.subjectFacetsList.some(it => it.value === item.key)) {
            this.subjectFacetsList.push(commonParam)
          }
          if (!this.queryParams.searchModels.some(it => it.value === item.key && it.searchField === 'SUB')) {
            this.queryParams.searchModels.push(commonParam)
          }
          break
        case 'A':
          commonParam.searchField = 'AREA'
          commonParam.value = item.key
          commonParam.displayName = item.label
          commonParam.exact = true
          if (!this.areaFacetsList.some(it => it.value === item.key)) {
            this.areaFacetsList.push(commonParam)
          }
          if (!this.queryParams.searchModels.some(it => it.value === item.key && it.searchField === 'AREA')) {
            this.queryParams.searchModels.push(commonParam)
          }
          break
        case 'C':
          commonParam.searchField = 'CL'
          commonParam.value = item.key
          commonParam.displayName = item.label
          if (!this.classTypeFacetsList.some(it => it.value === item.key)) {
            this.classTypeFacetsList.push(commonParam)
          }
          if (!this.queryParams.searchModels.some(it => it.value === item.key && it.searchField === 'CL')) {
            this.queryParams.searchModels.push(commonParam)
          }
          break
      }
      if (classOrMedia === false) {
        this.classOrMedia = false
      }
      this.queryParams.pageNum = 1
      this.searchWithoutButton()
    },
    /**
     * 移除聚类数据的函数
     * @param item
     * @param name
     */
    removeFacet(item, name) {
      switch (name) {
        case 'P':
          this.providerFacetsList = this.providerFacetsList.filter(it => it.value !== item.value)
          this.queryParams.searchModels.some((it, index) => {
            if (it.searchField === 'LNG' && it.value === item.value) {
              this.queryParams.searchModels.splice(index, 1)
            }
          })
          break
        case 'O':
          this.outsideFacetsList = this.outsideFacetsList.filter(it => it.value !== item.value)
          this.queryParams.searchModels.some((it, index) => {
            if (it.searchField === 'LNG' && it.value === item.value) {
              this.queryParams.searchModels.splice(index, 1)
            }
          })
          break
        case 'S':
          this.subjectFacetsList = this.subjectFacetsList.filter(it => it.value !== item.value)
          this.queryParams.searchModels.some((it, index) => {
            if (it.searchField === 'SUB' && it.value === item.value) {
              this.queryParams.searchModels.splice(index, 1)
            }
          })
          break
        case 'A':
          this.areaFacetsList = this.areaFacetsList.filter(it => it.value !== item.value)
          this.queryParams.searchModels.some((it, index) => {
            if (it.searchField === 'AREA' && it.value === item.value) {
              this.queryParams.searchModels.splice(index, 1)
            }
          })
          break
        case 'C':
          this.classTypeFacetsList = this.classTypeFacetsList.filter(it => it.value !== item.value)
          this.queryParams.searchModels.some((it, index) => {
            if (it.searchField === 'CL' && it.value === item.value) {
              this.queryParams.searchModels.splice(index, 1)
            }
          })
          break
      }
      this.queryParams.pageNum = 1
      this.searchWithoutButton()
    },
    /**
     * 点击按字母查找的字母函数
     * @param item
     */
    clickTheLetter(item) {
      let commonParam = {
        searchField: undefined,
        value: undefined,
        logicOperator: 'AND',
        displayName: undefined,
        exact: false
      }
      commonParam.searchField = 'FN'
      commonParam.value = item
      let val = this.queryParams.searchModels.find(it => it.searchField === 'FN');
      if (val) {
        val.value = item
      } else if (!this.queryParams.searchModels.some(it => it.value === item)) {
        this.queryParams.searchModels.push(commonParam)
      }
      this.classOrMedia = false
      this.getMediaPageMethod(this.queryParams)
      this.getMediaFacetMethod(this.queryParams)
    },
    /**
     * 检索前需要执行的函数（除点击"期刊检索"按钮会移除所有聚类条件外，其他点击检索的地方都会保留聚类条件），
     * 当条件过多时，需要将第一个条件的逻辑运算符删掉（solr限制）
     */
    searchWithoutButton() {
      let param = this.queryParams
      if (param.searchModels.searchModels && param.searchModels.length > 0) {
        param.searchModels[0].logicOperator = undefined
      }
      this.getMediaFacetMethod(param)
      this.getMediaPageMethod(param)
    },
    /**
     * 拼接封面路径地址
     * @param gch
     * @returns {string}
     */
    buildImgUrl(gch) {
      return 'http://image1.cqvip.com//vip1000/qkclearimg/' + gch.toLowerCase() + '/' + gch.toLowerCase() + '.jpg?site=d68adea8566ee0bb33fc319fa2279e77'
    },
    /**
     * 根据属性值返回所属核心期刊标识
     * @param value
     */
    buildProvider(value) {
      let arr = []
      if (value) {
        let split = value.split(";")
        if (split[0] == '核心刊') {
          if (value.indexOf('BDHX') != -1) {
            arr.push('北大核心')
          }
          if (value.indexOf('CSSCI') != -1) {
            arr.push('CSSCI')
          }
          if (value.indexOf('CSCD') != -1) {
            arr.push('CSCD')
          }
        }
      }
      return arr
    },
    /**
     * 跳转到期刊详情
     * @param id
     */
    toMediaDetail(id) {
      const { href } = this.$router.resolve({
        name: 'JournalsIndex',
        query: {
          gch5: id
        }
      })
      window.open(href,"_blank")
    }

  },
  created() {
    this.initFacet()
  },
  watch: {
    //监听各个聚类数据，根据点击事件来展开或收起显示
    providerFacets: {
      deep: true,
      handler(val) {
        if (val.showAll) {
          this.providerFacets.limit = this.providerFacets.value.length
        } else {
          this.providerFacets.limit = 5
        }
      }
    },
    outsideFacets: {
      deep: true,
      handler(val) {
        if (val.showAll) {
          this.outsideFacets.limit = this.outsideFacets.value.length
        } else {
          this.outsideFacets.limit = 5
        }
      }
    },
    subjectFacets: {
      deep: true,
      handler(val) {
        if (val.showAll) {
          this.subjectFacets.limit = this.subjectFacets.value.length
        } else {
          this.subjectFacets.limit = 5
        }
      }
    },
    areaFacets: {
      deep: true,
      handler(val) {
        if (val.showAll) {
          this.areaFacets.limit = this.areaFacets.value.length
        } else {
          this.areaFacets.limit = 5
        }
      }
    },
    classTypeFacets: {
      deep: true,
      handler(val) {
        if (val.showAll) {
          this.classTypeFacets.limit = this.classTypeFacets.value.length
        } else {
          this.classTypeFacets.limit = 5
        }
      }
    }
  }
}
