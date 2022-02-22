/**
 * 检索通用的一些参数
 * */

/**
 * 管理端的下拉框
 * @type {({label: string, value: string}|{label: string, value: string}|{label: string, value: string}|{label: string, value: string}|{label: string, value: string})[]}
 */
export const systemSelectOptions = [
  {
    label: '标题',
    value: 'TITLE'
  },
  {
    label: '关键词',
    value: 'KEYWORD'
  },
  {
    label: '作者',
    value: 'AUTHOR'
  },
  {
    label: '机构',
    value: 'ORGAN'
  },
  {
    label: '刊名',
    value: 'JOURNAL_NAME'
  }
]

/**
 * 所有的检索字段
 *
 * @type {({label: string, value: string})[]}
 */
export const allFiledOptions = [
  {
    label: '文献类型',
    value: 'SOURCE_TYPE',
    filed: 'source_type'
  },
  {
    label: '标题',
    value: 'TITLE',
    filed: 'title,title_alt'
  },
  {
    label: '关键词',
    value: 'KEYWORD',
    filed: 'keyword,keyword_alt'
  },
  {
    label: '作者',
    value: 'AUTHOR',
    filed: 'author,author_alt'
  },
  {
    label: '机构',
    value: 'ORGAN',
    filed: 'organ'
  },
  {
    label: '期刊分类',
    value: 'JOURNAL_NAME',
    filed: 'journal_name'
  },
  {
    label: '中图分类',
    value: 'CLC_NO',
    filed: 'clc_no'
  },
  {
    label: '来源数据库',
    value: 'PROVIDER',
    filed: 'provider'
  },
  {
    label: '是否重复',
    value: 'REPEAT',
    filed: 'repeat'
  },
  {
    label: '是否屏蔽',
    value: 'STATE',
    filed: 'state'
  }
]

export const searchModel = {
  //检索字段（参考上面的检索条件数组）
  searchField: 'MN',
  //检索关键词
  searchKeyword: undefined,
  //逻辑运算符
  logicOperator: 'AND',
  //展示名称
  clusterShowName: undefined,
  //模糊检索false或者精确检索true
  isExact: false,
  isFacet:false
}

/**
 * 检索通用逻辑符
 * @type {({label: string, value: string}|{label: string, value: string}|{label: string, value: string})[]}
 */
export const commonLogic = [
  {
    value: 'AND',
    label: '与'
  },
  {
    value: 'OR',
    label: '或'
  },
  {
    value: 'NOT',
    label: '非'
  }
]


