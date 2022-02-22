const provinces = [// 省份
  '其它', '北京', '天津', '上海', '重庆', '四川', '黑龙江', '吉林', '辽宁', '河北', '山东', '江苏', '浙江', '安徽', '福建', '广东',
  '广西', '海南', '云南', '贵州', '湖南', '湖北', '江西', '河南', '陕西', '山西', '青海', '甘肃', '宁夏', '内蒙古', '新疆', '西藏'
]

const industries = [// 行业
  '其它',
  '医药卫生',
  '石油/化工/能源',
  '机械/汽车',
  '电子电器',
  '邮电通信',
  '交通运输',
  '冶金/矿业',
  '计算机/网络',
  '商业/贸易',
  '教育/图书/印刷/出版',
  '建筑/房地产',
  '农业/渔业/林业/畜牧业',
  '银行/金融/证券/投资'
]

const searchFields = [
  { value: 'T', label: '题名' },
  { value: 'K', label: '关键词' },
  { value: 'R', label: '文摘' },
  { value: 'A', label: '作者' },
  { value: 'F', label: '第一作者' },
  { value: 'S', label: '机构' },
  { value: 'J', label: '刊名' },
  { value: 'C', label: '分类号' },
  { value: 'I', label: '基金资助' },
  { value: 'L', label: '栏目信息' }
]

export default {
  provinces,
  industries,
  searchFields
}
