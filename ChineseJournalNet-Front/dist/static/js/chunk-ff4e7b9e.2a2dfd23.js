(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-ff4e7b9e"],{"68b6":function(e,t,i){"use strict";i.r(t);var n=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{ref:"editor"})},l=[],u=(i("99af"),i("9339")),a=i.n(u),o=(i("a753"),i("8096"),i("14e1"),{name:"d2-quill",props:{value:{type:String,required:!1,default:""}},data:function(){return{Quill:void 0,currentValue:"",options:{theme:"snow",bounds:document.body,debug:"warn",modules:{toolbar:[["bold","italic","underline","strike"],["blockquote","code-block"],[{list:"ordered"},{list:"bullet"}],[{size:["small",!1,"large","huge"]}],[{header:[1,2,3,4,5,6,!1]}],[{color:[]},{background:[]}],[{align:[]}],["clean"],["link","image"]]},placeholder:"书写你的内容",readOnly:!1}}},watch:{value:{handler:function(e){e!==this.currentValue&&(this.currentValue=e,this.Quill&&this.Quill.pasteHTML(this.value))},immediate:!0}},mounted:function(){this.init()},methods:{init:function(){var e=this,t=this.$refs.editor;this.Quill=new a.a(t,this.options),this.Quill.pasteHTML(this.currentValue),this.Quill.on("text-change",(function(t,i,n){var l=e.$refs.editor.children[0].innerHTML,u=e.Quill.getText(),a=e.Quill;e.currentValue=l,e.$emit("input",l),e.$emit("change",{html:l,text:u,quill:a})})),this.Quill.on("text-change",(function(t,i,n){e.$emit("text-change",t,i,n)})),this.Quill.on("selection-change",(function(t,i,n){e.$emit("selection-change",t,i,n)})),this.Quill.on("editor-change",(function(t){for(var i=arguments.length,n=new Array(i>1?i-1:0),l=1;l<i;l++)n[l-1]=arguments[l];e.$emit.apply(e,["editor-change",t].concat(n))}))}}}),r=o,c=(i("f016"),i("2877")),s=Object(c["a"])(r,n,l,!1,null,null,null);t["default"]=s.exports},db7f:function(e,t,i){},f016:function(e,t,i){"use strict";var n=i("db7f"),l=i.n(n);l.a}}]);