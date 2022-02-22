/**
 * Ueditor 通用配置
 * */

export const ueditorConfig={
  autoHeightEnabled: false,
  autoFloatEnabled: true,
  initialContent:'请输入内容',   //初始化编辑器的内容,也可以通过textarea/script给值，看官网例子
  autoClearinitialContent:true, //是否自动清除编辑器初始内容，注意：如果focus属性设置为true,这个也为真，那么编辑器一上来就会触发导致初始化的内容看不到了
  initialFrameWidth: 700,
  initialFrameHeight: 400,
  serverUrl:process.env.VUE_APP_API +"/ueditor/action",
  UEDITOR_HOME_URL:  '/ueditor/'

}
