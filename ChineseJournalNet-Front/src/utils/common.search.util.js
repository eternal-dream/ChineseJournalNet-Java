/**
 * 检索时用到的一些公共方法
 * */

import { commonEnums } from '@api/common/param'

/**
 * 从localStorage获取已缓存的分类树形菜单
 * @returns {string}
 */
export function getClassTreeFromLocal () {
  return JSON.parse(localStorage.getItem('class_info_tree'))
}

/**
 * 将获取到的分类树形菜单存到localStorage
 * @param val
 */
export function setClassTreeFromLocal (val = []) {
  localStorage.setItem('class_info_tree', JSON.stringify(val))
}

/**
 * 构建范围参数表达式
 * @param key
 * @param value
 */
export function buildRangeSearchExpression (key, value = {}) {
  if (key && value) {
    if (value.min && value.max){
      return key + ':FROM ' + value.min + ' TO ' + value.max
    }else if (value.min && !value.max){
      return key + ':>=' + value.min
    }else if (!value.min && value.max){
      return key + ':<=' + value.max
    }else {
      return null
    }
  }
}

/**
 * 根据检索参数构建表达式
 * @param key
 * @param value
 * @param isExact true精确检索，false模糊检索(可不传，不传默认为false)
 */
export function buildComplexitySearchExpression (key, value, isExact) {
  let exact = false
  if (isExact) exact = isExact
  let expression
  switch (key) {
    case 'SOURCE_TYPE':
      if (value) expression = 'source_type:' + value
      break
    case 'TITLE':
      if (value) {
        if (exact) {
          expression = '(title:"' + value + '" OR title_alt:"' + value + '")'
        } else {
          expression = '(title:' + value + ' OR title_alt:' + value + ')'
        }
      }
      break
    case 'KEYWORD':
      if (value) {
        if (exact) {
          expression = '(keyword:"' + value + '" OR keyword_alt:"' + value + '")'
        } else {
          expression = '(keyword:' + value + ' OR keyword_alt:' + value + ')'
        }
      }
      break
    case 'AUTHOR':
      if (value) {
        if (exact) {
          expression = '(author:"' + value + '" OR author_alt:"' + value + '")'
        } else {
          expression = '(author:' + value + ' OR author_alt:' + value + ')'
        }
      }
      break
    case 'FIRST_AUTHOR':
      if (value) {
        if (exact) {
          expression = 'first_author:"' + value + '"'
        } else {
          expression = 'first_author:' + value
        }
      }
      break
    case 'ORGAN':
      if (value) {
        if (exact) {
          expression = 'organ:"' + value + '"'
        } else {
          expression = 'organ:' + value
        }
      }
      break
    case 'JOURNAL_NAME':
      if (value) {
        if (exact) {
          expression = 'journal_name:"' + value + '"'
        } else {
          expression = 'journal_name:' + value
        }
      }
      break
    case 'CLC_NO':
      if (value) expression = 'clc_no:' + value
      break
    case 'PROVIDER':
      if (value) expression = 'provider:' + value
      break
    case 'REPEAT':
      if (value !== undefined) expression = 'repeat:' + value
      break
    case 'STATE':
      if (value) expression = 'state:' + value
      break
  }
  return expression
}

/**
 * 根据检索参数构建简单的检索表达式{构建的表达式不包含精确检索("value")和模糊检索(*value*)的检索条件，全部是会被分词检索的}
 * @param key 字段枚举名
 * @param value 字段检索值
 */
export function buildSimpleSearchExpression (key, value) {
  let expression
  switch (key) {
    case 'SOURCE_TYPE':
      if (value) expression = 'source_type:' + value
      break
    case 'TITLE':
      if (value) expression = '(title:' + value + ' OR title_alt:' + value + ')'
      break
    case 'KEYWORD':
      if (value) expression = '(keyword:' + value + ' OR keyword_alt:' + value + ')'
      break
    case 'AUTHOR':
      if (value) expression = '(author:' + value + ' OR author_alt:' + value + ')'
      break
    case 'FIRST_AUTHOR':
      if (value) expression = 'first_author:' + value
      break
    case 'ORGAN':
      if (value) expression = 'organ:' + value
      break
    case 'JOURNAL_NAME':
      if (value) expression = 'journal_name:' + value
      break
    case 'CLC_NO':
      if (value) expression = 'clc_no:' + value
      break
    case 'PROVIDER':
      if (value) expression = 'provider:' + value
      break
    case 'REPEAT':
      if (value !== undefined) expression = 'repeat:' + value
      break
    case 'STATE':
      if (value) expression = 'state:' + value
      break
  }
  return expression
}

/**
 * 元数据中的articleType的值
 * @param type
 * @returns {*}
 */
export function getArticleNameByType (type) {
  let arr = commonEnums.commonArticleType
  let filter = arr.filter(item => item.esTag === type)
  if (filter[0] && filter[0].label) {
    return filter[0].label
  }
  return ''
}

/**
 * 获取数据库名字（根据数据库标识）
 * @param dataBases 数据库数组
 * @param tag 数据库标识
 */
export function getDatabaseNameByTag (dataBases, tag) {
  let filter = dataBases.filter(item => item.provider == tag)
  if (filter[0] && filter[0].title) {
    return filter[0].title
  }
  return ''
}

/**
 * 根据条件字段名设置显示的名字
 * @param field
 * @returns {string}
 */
export function getConditionLabelByField(field){
  let str = ''
  if (field == 'SOURCE_TYPE'){
    str = '文献类型'
  }else if (field == 'TITLE'){
    str = '标题'
  }else if (field == 'KEYWORD'){
    str = '关键词'
  }else if (field == 'AUTHOR'){
    str = '作者'
  }else if (field == 'ORGAN'){
    str = '机构'
  }else if (field == 'PUBLISHER'){
    str = '出版社'
  }else if (field == 'FIRST_AUTHOR'){
    str = '作者'
  }else if (field == 'CLC_NO'){
    str = '学科分类'
  }else if (field == 'JOURNAL_NAME'){
    str = '期刊'
  }
  return str
}
