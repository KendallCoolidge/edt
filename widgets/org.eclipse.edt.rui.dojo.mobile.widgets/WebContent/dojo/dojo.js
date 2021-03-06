/*******************************************************************************
 * Copyright © 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
require({cache:{"dojo/dom-geometry":function(){
define(["./_base/sniff","./_base/window","./dom","./dom-style"],function(_1,_2,_3,_4){
var _5={};
_5.boxModel="content-box";
if(_1("ie")){
_5.boxModel=document.compatMode=="BackCompat"?"border-box":"content-box";
}
_5.getPadExtents=function getPadExtents(_6,_7){
_6=_3.byId(_6);
var s=_7||_4.getComputedStyle(_6),px=_4.toPixelValue,l=px(_6,s.paddingLeft),t=px(_6,s.paddingTop),r=px(_6,s.paddingRight),b=px(_6,s.paddingBottom);
return {l:l,t:t,r:r,b:b,w:l+r,h:t+b};
};
var _8="none";
_5.getBorderExtents=function getBorderExtents(_9,_a){
_9=_3.byId(_9);
var px=_4.toPixelValue,s=_a||_4.getComputedStyle(_9),l=s.borderLeftStyle!=_8?px(_9,s.borderLeftWidth):0,t=s.borderTopStyle!=_8?px(_9,s.borderTopWidth):0,r=s.borderRightStyle!=_8?px(_9,s.borderRightWidth):0,b=s.borderBottomStyle!=_8?px(_9,s.borderBottomWidth):0;
return {l:l,t:t,r:r,b:b,w:l+r,h:t+b};
};
_5.getPadBorderExtents=function getPadBorderExtents(_b,_c){
_b=_3.byId(_b);
var s=_c||_4.getComputedStyle(_b),p=_5.getPadExtents(_b,s),b=_5.getBorderExtents(_b,s);
return {l:p.l+b.l,t:p.t+b.t,r:p.r+b.r,b:p.b+b.b,w:p.w+b.w,h:p.h+b.h};
};
_5.getMarginExtents=function getMarginExtents(_d,_e){
_d=_3.byId(_d);
var s=_e||_4.getComputedStyle(_d),px=_4.toPixelValue,l=px(_d,s.marginLeft),t=px(_d,s.marginTop),r=px(_d,s.marginRight),b=px(_d,s.marginBottom);
if(_1("webkit")&&(s.position!="absolute")){
r=l;
}
return {l:l,t:t,r:r,b:b,w:l+r,h:t+b};
};
_5.getMarginBox=function getMarginBox(_f,_10){
_f=_3.byId(_f);
var s=_10||_4.getComputedStyle(_f),me=_5.getMarginExtents(_f,s),l=_f.offsetLeft-me.l,t=_f.offsetTop-me.t,p=_f.parentNode,px=_4.toPixelValue,pcs;
if(_1("mozilla")){
var sl=parseFloat(s.left),st=parseFloat(s.top);
if(!isNaN(sl)&&!isNaN(st)){
l=sl,t=st;
}else{
if(p&&p.style){
pcs=_4.getComputedStyle(p);
if(pcs.overflow!="visible"){
l+=pcs.borderLeftStyle!=_8?px(_f,pcs.borderLeftWidth):0;
t+=pcs.borderTopStyle!=_8?px(_f,pcs.borderTopWidth):0;
}
}
}
}else{
if(_1("opera")||(_1("ie")==8&&!_1("quirks"))){
if(p){
pcs=_4.getComputedStyle(p);
l-=pcs.borderLeftStyle!=_8?px(_f,pcs.borderLeftWidth):0;
t-=pcs.borderTopStyle!=_8?px(_f,pcs.borderTopWidth):0;
}
}
}
return {l:l,t:t,w:_f.offsetWidth+me.w,h:_f.offsetHeight+me.h};
};
_5.getContentBox=function getContentBox(_11,_12){
_11=_3.byId(_11);
var s=_12||_4.getComputedStyle(_11),w=_11.clientWidth,h,pe=_5.getPadExtents(_11,s),be=_5.getBorderExtents(_11,s);
if(!w){
w=_11.offsetWidth;
h=_11.offsetHeight;
}else{
h=_11.clientHeight;
be.w=be.h=0;
}
if(_1("opera")){
pe.l+=be.l;
pe.t+=be.t;
}
return {l:pe.l,t:pe.t,w:w-pe.w-be.w,h:h-pe.h-be.h};
};
function _13(_14,l,t,w,h,u){
u=u||"px";
var s=_14.style;
if(!isNaN(l)){
s.left=l+u;
}
if(!isNaN(t)){
s.top=t+u;
}
if(w>=0){
s.width=w+u;
}
if(h>=0){
s.height=h+u;
}
};
function _15(_16){
return _16.tagName.toLowerCase()=="button"||_16.tagName.toLowerCase()=="input"&&(_16.getAttribute("type")||"").toLowerCase()=="button";
};
function _17(_18){
return _5.boxModel=="border-box"||_18.tagName.toLowerCase()=="table"||_15(_18);
};
_5.setContentSize=function setContentSize(_19,box,_1a){
_19=_3.byId(_19);
var w=box.w,h=box.h;
if(_17(_19)){
var pb=_5.getPadBorderExtents(_19,_1a);
if(w>=0){
w+=pb.w;
}
if(h>=0){
h+=pb.h;
}
}
_13(_19,NaN,NaN,w,h);
};
var _1b={l:0,t:0,w:0,h:0};
_5.setMarginBox=function setMarginBox(_1c,box,_1d){
_1c=_3.byId(_1c);
var s=_1d||_4.getComputedStyle(_1c),w=box.w,h=box.h,pb=_17(_1c)?_1b:_5.getPadBorderExtents(_1c,s),mb=_5.getMarginExtents(_1c,s);
if(_1("webkit")){
if(_15(_1c)){
var ns=_1c.style;
if(w>=0&&!ns.width){
ns.width="4px";
}
if(h>=0&&!ns.height){
ns.height="4px";
}
}
}
if(w>=0){
w=Math.max(w-pb.w-mb.w,0);
}
if(h>=0){
h=Math.max(h-pb.h-mb.h,0);
}
_13(_1c,box.l,box.t,w,h);
};
_5.isBodyLtr=function isBodyLtr(){
return (_2.body().dir||_2.doc.documentElement.dir||"ltr").toLowerCase()=="ltr";
};
_5.docScroll=function docScroll(){
var _1e=_2.doc.parentWindow||_2.doc.defaultView;
return "pageXOffset" in _1e?{x:_1e.pageXOffset,y:_1e.pageYOffset}:(_1e=_1("quirks")?_2.body():_2.doc.documentElement,{x:_5.fixIeBiDiScrollLeft(_1e.scrollLeft||0),y:_1e.scrollTop||0});
};
_5.getIeDocumentElementOffset=function getIeDocumentElementOffset(){
var de=_2.doc.documentElement;
if(_1("ie")<8){
var r=de.getBoundingClientRect(),l=r.left,t=r.top;
if(_1("ie")<7){
l+=de.clientLeft;
t+=de.clientTop;
}
return {x:l<0?0:l,y:t<0?0:t};
}else{
return {x:0,y:0};
}
};
_5.fixIeBiDiScrollLeft=function fixIeBiDiScrollLeft(_1f){
var ie=_1("ie");
if(ie&&!_5.isBodyLtr()){
var qk=_1("quirks"),de=qk?_2.body():_2.doc.documentElement;
if(ie==6&&!qk&&_2.global.frameElement&&de.scrollHeight>de.clientHeight){
_1f+=de.clientLeft;
}
return (ie<8||qk)?(_1f+de.clientWidth-de.scrollWidth):-_1f;
}
return _1f;
};
_5.position=function(_20,_21){
_20=_3.byId(_20);
var db=_2.body(),dh=db.parentNode,ret=_20.getBoundingClientRect();
ret={x:ret.left,y:ret.top,w:ret.right-ret.left,h:ret.bottom-ret.top};
if(_1("ie")){
var _22=_5.getIeDocumentElementOffset();
ret.x-=_22.x+(_1("quirks")?db.clientLeft+db.offsetLeft:0);
ret.y-=_22.y+(_1("quirks")?db.clientTop+db.offsetTop:0);
}else{
if(_1("ff")==3){
var cs=_4.getComputedStyle(dh),px=_4.toPixelValue;
ret.x-=px(dh,cs.marginLeft)+px(dh,cs.borderLeftWidth);
ret.y-=px(dh,cs.marginTop)+px(dh,cs.borderTopWidth);
}
}
if(_21){
var _23=_5.docScroll();
ret.x+=_23.x;
ret.y+=_23.y;
}
return ret;
};
_5.getMarginSize=function getMarginSize(_24,_25){
_24=_3.byId(_24);
var me=_5.getMarginExtents(_24,_25||_4.getComputedStyle(_24));
var _26=_24.getBoundingClientRect();
return {w:(_26.right-_26.left)+me.w,h:(_26.bottom-_26.top)+me.h};
};
_5.normalizeEvent=function(_27){
if(!("layerX" in _27)){
_27.layerX=_27.offsetX;
_27.layerY=_27.offsetY;
}
if(!_1("dom-addeventlistener")){
var se=_27.target;
var doc=(se&&se.ownerDocument)||document;
var _28=_1("quirks")?doc.body:doc.documentElement;
var _29=_5.getIeDocumentElementOffset();
_27.pageX=_27.clientX+_5.fixIeBiDiScrollLeft(_28.scrollLeft||0)-_29.x;
_27.pageY=_27.clientY+(_28.scrollTop||0)-_29.y;
}
};
return _5;
});
},"dojo/_base/html":function(){
define(["./kernel","../dom","../dom-style","../dom-attr","../dom-prop","../dom-class","../dom-construct","../dom-geometry"],function(_2a,dom,_2b,_2c,_2d,cls,ctr,_2e){
_2a.byId=dom.byId;
_2a.isDescendant=dom.isDescendant;
_2a.setSelectable=dom.setSelectable;
_2a.getAttr=_2c.get;
_2a.setAttr=_2c.set;
_2a.hasAttr=_2c.has;
_2a.removeAttr=_2c.remove;
_2a.getNodeProp=_2c.getNodeProp;
_2a.attr=function(_2f,_30,_31){
if(arguments.length==2){
return _2c[typeof _30=="string"?"get":"set"](_2f,_30);
}
return _2c.set(_2f,_30,_31);
};
_2a.hasClass=cls.contains;
_2a.addClass=cls.add;
_2a.removeClass=cls.remove;
_2a.toggleClass=cls.toggle;
_2a.replaceClass=cls.replace;
_2a._toDom=_2a.toDom=ctr.toDom;
_2a.place=ctr.place;
_2a.create=ctr.create;
_2a.empty=function(_32){
ctr.empty(_32);
};
_2a._destroyElement=_2a.destroy=function(_33){
ctr.destroy(_33);
};
_2a._getPadExtents=_2a.getPadExtents=_2e.getPadExtents;
_2a._getBorderExtents=_2a.getBorderExtents=_2e.getBorderExtents;
_2a._getPadBorderExtents=_2a.getPadBorderExtents=_2e.getPadBorderExtents;
_2a._getMarginExtents=_2a.getMarginExtents=_2e.getMarginExtents;
_2a._getMarginSize=_2a.getMarginSize=_2e.getMarginSize;
_2a._getMarginBox=_2a.getMarginBox=_2e.getMarginBox;
_2a.setMarginBox=_2e.setMarginBox;
_2a._getContentBox=_2a.getContentBox=_2e.getContentBox;
_2a.setContentSize=_2e.setContentSize;
_2a._isBodyLtr=_2a.isBodyLtr=_2e.isBodyLtr;
_2a._docScroll=_2a.docScroll=_2e.docScroll;
_2a._getIeDocumentElementOffset=_2a.getIeDocumentElementOffset=_2e.getIeDocumentElementOffset;
_2a._fixIeBiDiScrollLeft=_2a.fixIeBiDiScrollLeft=_2e.fixIeBiDiScrollLeft;
_2a.position=_2e.position;
_2a.marginBox=function marginBox(_34,box){
return box?_2e.setMarginBox(_34,box):_2e.getMarginBox(_34);
};
_2a.contentBox=function contentBox(_35,box){
return box?_2e.setContentSize(_35,box):_2e.getContentBox(_35);
};
_2a.coords=function(_36,_37){
_2a.deprecated("dojo.coords()","Use dojo.position() or dojo.marginBox().");
_36=dom.byId(_36);
var s=_2b.getComputedStyle(_36),mb=_2e.getMarginBox(_36,s);
var abs=_2e.position(_36,_37);
mb.x=abs.x;
mb.y=abs.y;
return mb;
};
_2a.getProp=_2d.get;
_2a.setProp=_2d.set;
_2a.prop=function(_38,_39,_3a){
if(arguments.length==2){
return _2d[typeof _39=="string"?"get":"set"](_38,_39);
}
return _2d.set(_38,_39,_3a);
};
_2a.getStyle=_2b.get;
_2a.setStyle=_2b.set;
_2a.getComputedStyle=_2b.getComputedStyle;
_2a.__toPixelValue=_2a.toPixelValue=_2b.toPixelValue;
_2a.style=function(_3b,_3c,_3d){
switch(arguments.length){
case 1:
return _2b.get(_3b);
case 2:
return _2b[typeof _3c=="string"?"get":"set"](_3b,_3c);
}
return _2b.set(_3b,_3c,_3d);
};
return _2a;
});
},"dojo/_base/array":function(){
define("dojo/_base/array",["./kernel","../has","./lang"],function(_3e,has,_3f){
var _40={},u,_41;
function _42(){
_40={};
};
function _43(fn){
return _40[fn]=new Function("item","index","array",fn);
};
function _44(_45){
var _46=!_45;
return function(a,fn,o){
var i=0,l=a&&a.length||0,_47;
if(l&&typeof a=="string"){
a=a.split("");
}
if(typeof fn=="string"){
fn=_40[fn]||_43(fn);
}
if(o){
for(;i<l;++i){
_47=!fn.call(o,a[i],i,a);
if(_45^_47){
return !_47;
}
}
}else{
for(;i<l;++i){
_47=!fn(a[i],i,a);
if(_45^_47){
return !_47;
}
}
}
return _46;
};
};
function _48(up){
var _49=1,_4a=0,_4b=0;
if(!up){
_49=_4a=_4b=-1;
}
return function(a,x,_4c,_4d){
if(_4d&&_49>0){
return _41.lastIndexOf(a,x,_4c);
}
var l=a&&a.length||0,end=up?l+_4b:_4a,i;
if(_4c===u){
i=up?_4a:l+_4b;
}else{
if(_4c<0){
i=l+_4c;
if(i<0){
i=_4a;
}
}else{
i=_4c>=l?l+_4b:_4c;
}
}
if(l&&typeof a=="string"){
a=a.split("");
}
for(;i!=end;i+=_49){
if(a[i]==x){
return i;
}
}
return -1;
};
};
function _4e(a,fn,o){
var i=0,l=a&&a.length||0;
if(l&&typeof a=="string"){
a=a.split("");
}
if(typeof fn=="string"){
fn=_40[fn]||_43(fn);
}
if(o){
for(;i<l;++i){
fn.call(o,a[i],i,a);
}
}else{
for(;i<l;++i){
fn(a[i],i,a);
}
}
};
function map(a,fn,o,Ctr){
var i=0,l=a&&a.length||0,out=new (Ctr||Array)(l);
if(l&&typeof a=="string"){
a=a.split("");
}
if(typeof fn=="string"){
fn=_40[fn]||_43(fn);
}
if(o){
for(;i<l;++i){
out[i]=fn.call(o,a[i],i,a);
}
}else{
for(;i<l;++i){
out[i]=fn(a[i],i,a);
}
}
return out;
};
function _4f(a,fn,o){
var i=0,l=a&&a.length||0,out=[],_50;
if(l&&typeof a=="string"){
a=a.split("");
}
if(typeof fn=="string"){
fn=_40[fn]||_43(fn);
}
if(o){
for(;i<l;++i){
_50=a[i];
if(fn.call(o,_50,i,a)){
out.push(_50);
}
}
}else{
for(;i<l;++i){
_50=a[i];
if(fn(_50,i,a)){
out.push(_50);
}
}
}
return out;
};
_41={every:_44(false),some:_44(true),indexOf:_48(true),lastIndexOf:_48(false),forEach:_4e,map:map,filter:_4f,clearCache:_42};
1&&_3f.mixin(_3e,_41);
return _41;
});
},"dojo/_base/Deferred":function(){
define("dojo/_base/Deferred",["./kernel","./lang"],function(_51,_52){
var _53=function(){
};
var _54=Object.freeze||function(){
};
_51.Deferred=function(_55){
var _56,_57,_58,_59,_5a;
var _5b=(this.promise={});
function _5c(_5d){
if(_57){
throw new Error("This deferred has already been resolved");
}
_56=_5d;
_57=true;
_5e();
};
function _5e(){
var _5f;
while(!_5f&&_5a){
var _60=_5a;
_5a=_5a.next;
if((_5f=(_60.progress==_53))){
_57=false;
}
var _61=(_58?_60.error:_60.resolved);
if(_61){
try{
var _62=_61(_56);
if(_62&&typeof _62.then==="function"){
_62.then(_52.hitch(_60.deferred,"resolve"),_52.hitch(_60.deferred,"reject"),_52.hitch(_60.deferred,"progress"));
continue;
}
var _63=_5f&&_62===undefined;
if(_5f&&!_63){
_58=_62 instanceof Error;
}
_60.deferred[_63&&_58?"reject":"resolve"](_63?_56:_62);
}
catch(e){
_60.deferred.reject(e);
}
}else{
if(_58){
_60.deferred.reject(_56);
}else{
_60.deferred.resolve(_56);
}
}
}
};
this.resolve=this.callback=function(_64){
this.fired=0;
this.results=[_64,null];
_5c(_64);
};
this.reject=this.errback=function(_65){
_58=true;
this.fired=1;
_5c(_65);
this.results=[null,_65];
if(!_65||_65.log!==false){
(_51.config.deferredOnError||function(x){
console.error(x);
})(_65);
}
};
this.progress=function(_66){
var _67=_5a;
while(_67){
var _68=_67.progress;
_68&&_68(_66);
_67=_67.next;
}
};
this.addCallbacks=function(_69,_6a){
this.then(_69,_6a,_53);
return this;
};
_5b.then=this.then=function(_6b,_6c,_6d){
var _6e=_6d==_53?this:new _51.Deferred(_5b.cancel);
var _6f={resolved:_6b,error:_6c,progress:_6d,deferred:_6e};
if(_5a){
_59=_59.next=_6f;
}else{
_5a=_59=_6f;
}
if(_57){
_5e();
}
return _6e.promise;
};
var _70=this;
_5b.cancel=this.cancel=function(){
if(!_57){
var _71=_55&&_55(_70);
if(!_57){
if(!(_71 instanceof Error)){
_71=new Error(_71);
}
_71.log=false;
_70.reject(_71);
}
}
};
_54(_5b);
};
_52.extend(_51.Deferred,{addCallback:function(_72){
return this.addCallbacks(_52.hitch.apply(_51,arguments));
},addErrback:function(_73){
return this.addCallbacks(null,_52.hitch.apply(_51,arguments));
},addBoth:function(_74){
var _75=_52.hitch.apply(_51,arguments);
return this.addCallbacks(_75,_75);
},fired:-1});
_51.Deferred.when=_51.when=function(_76,_77,_78,_79){
if(_76&&typeof _76.then==="function"){
return _76.then(_77,_78,_79);
}
return _77?_77(_76):_76;
};
return _51.Deferred;
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./ready","./_base/sniff","./_base/window"],function(_7a,_7b,_7c,has,_7d){
var _7e=_7d.doc.documentElement,ie=has("ie"),_7f=has("opera"),maj=Math.floor,ff=has("ff"),_80=_7a.boxModel.replace(/-/,""),_81={"dj_ie":ie,"dj_ie6":maj(ie)==6,"dj_ie7":maj(ie)==7,"dj_ie8":maj(ie)==8,"dj_ie9":maj(ie)==9,"dj_quirks":has("quirks"),"dj_iequirks":ie&&has("quirks"),"dj_opera":_7f,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_gecko":has("mozilla"),"dj_ff3":maj(ff)==3};
_81["dj_"+_80]=true;
var _82="";
for(var clz in _81){
if(_81[clz]){
_82+=clz+" ";
}
}
_7e.className=_7b.trim(_7e.className+" "+_82);
_7c(90,function(){
if(!_7a.isBodyLtr()){
var _83="dj_rtl dijitRtl "+_82.replace(/ /g,"-rtl ");
_7e.className=_7b.trim(_7e.className+" "+_83+"dj_rtl dijitRtl "+_82.replace(/ /g,"-rtl "));
}
});
return has;
});
},"dojox/mobile/app/_Widget":function(){
define(["dijit","dojo","dojox","dojo/require!dijit/_WidgetBase"],function(_84,_85,_86){
_85.provide("dojox.mobile.app._Widget");
_85.experimental("dojox.mobile.app._Widget");
_85.require("dijit._WidgetBase");
_85.declare("dojox.mobile.app._Widget",_84._WidgetBase,{getScroll:function(){
return {x:_85.global.scrollX,y:_85.global.scrollY};
},connect:function(_87,_88,fn){
if(_88.toLowerCase()=="dblclick"||_88.toLowerCase()=="ondblclick"){
if(_85.global["Mojo"]){
return this.connect(_87,Mojo.Event.tap,fn);
}
}
return this.inherited(arguments);
}});
});
},"dojo/dom":function(){
define(["./_base/sniff","./_base/lang","./_base/window"],function(has,_89,win){
try{
document.execCommand("BackgroundImageCache",false,true);
}
catch(e){
}
var dom={};
if(has("ie")){
dom.byId=function(id,doc){
if(typeof id!="string"){
return id;
}
var _8a=doc||win.doc,te=id&&_8a.getElementById(id);
if(te&&(te.attributes.id.value==id||te.id==id)){
return te;
}else{
var _8b=_8a.all[id];
if(!_8b||_8b.nodeName){
_8b=[_8b];
}
var i=0;
while((te=_8b[i++])){
if((te.attributes&&te.attributes.id&&te.attributes.id.value==id)||te.id==id){
return te;
}
}
}
};
}else{
dom.byId=function(id,doc){
return ((typeof id=="string")?(doc||win.doc).getElementById(id):id)||null;
};
}
dom.isDescendant=function(_8c,_8d){
try{
_8c=dom.byId(_8c);
_8d=dom.byId(_8d);
while(_8c){
if(_8c==_8d){
return true;
}
_8c=_8c.parentNode;
}
}
catch(e){
}
return false;
};
dom.setSelectable=function(_8e,_8f){
_8e=dom.byId(_8e);
if(has("mozilla")){
_8e.style.MozUserSelect=_8f?"":"none";
}else{
if(has("khtml")||has("webkit")){
_8e.style.KhtmlUserSelect=_8f?"auto":"none";
}else{
if(has("ie")){
var v=(_8e.unselectable=_8f?"":"on"),cs=_8e.getElementsByTagName("*"),i=0,l=cs.length;
for(;i<l;++i){
cs.item(i).unselectable=v;
}
}
}
}
};
return dom;
});
},"dojox/mobile/app/ImageThumbView":function(){
define(["dijit","dojo","dojox","dojo/require!dijit/_WidgetBase,dojo/string"],function(_90,_91,_92){
_91.provide("dojox.mobile.app.ImageThumbView");
_91.experimental("dojox.mobile.app.ImageThumbView");
_91.require("dijit._WidgetBase");
_91.require("dojo.string");
_91.declare("dojox.mobile.app.ImageThumbView",_90._WidgetBase,{items:[],urlParam:"url",labelParam:null,itemTemplate:"<div class=\"mblThumbInner\">"+"<div class=\"mblThumbOverlay\"></div>"+"<div class=\"mblThumbMask\">"+"<div class=\"mblThumbSrc\" style=\"background-image:url(${url})\"></div>"+"</div>"+"</div>",minPadding:4,maxPerRow:3,maxRows:-1,baseClass:"mblImageThumbView",thumbSize:"medium",animationEnabled:true,selectedIndex:-1,cache:null,cacheMustMatch:false,clickEvent:"onclick",cacheBust:false,disableHide:false,constructor:function(_93,_94){
},postCreate:function(){
this.inherited(arguments);
var _95=this;
var _96="mblThumbHover";
this.addThumb=_91.hitch(this,this.addThumb);
this.handleImgLoad=_91.hitch(this,this.handleImgLoad);
this.hideCached=_91.hitch(this,this.hideCached);
this._onLoadImages={};
this.cache=[];
this.visibleImages=[];
this._cacheCounter=0;
this.connect(this.domNode,this.clickEvent,function(_97){
var _98=_95._getItemNodeFromEvent(_97);
if(_98&&!_98._cached){
_95.onSelect(_98._item,_98._index,_95.items);
_91.query(".selected",this.domNode).removeClass("selected");
_91.addClass(_98,"selected");
}
});
_91.addClass(this.domNode,this.thumbSize);
this.resize();
this.render();
},onSelect:function(_99,_9a,_9b){
},_setAnimationEnabledAttr:function(_9c){
this.animationEnabled=_9c;
_91[_9c?"addClass":"removeClass"](this.domNode,"animated");
},_setItemsAttr:function(_9d){
this.items=_9d||[];
var _9e={};
var i;
for(i=0;i<this.items.length;i++){
_9e[this.items[i][this.urlParam]]=1;
}
var _9f=[];
for(var url in this._onLoadImages){
if(!_9e[url]&&this._onLoadImages[url]._conn){
_91.disconnect(this._onLoadImages[url]._conn);
this._onLoadImages[url].src=null;
_9f.push(url);
}
}
for(i=0;i<_9f.length;i++){
delete this._onLoadImages[url];
}
this.render();
},_getItemNode:function(_a0){
while(_a0&&!_91.hasClass(_a0,"mblThumb")&&_a0!=this.domNode){
_a0=_a0.parentNode;
}
return (_a0==this.domNode)?null:_a0;
},_getItemNodeFromEvent:function(_a1){
if(_a1.touches&&_a1.touches.length>0){
_a1=_a1.touches[0];
}
return this._getItemNode(_a1.target);
},resize:function(){
this._thumbSize=null;
this._size=_91.contentBox(this.domNode);
this.disableHide=true;
this.render();
this.disableHide=false;
},hideCached:function(){
for(var i=0;i<this.cache.length;i++){
if(this.cache[i]){
_91.style(this.cache[i],"display","none");
}
}
},render:function(){
var i;
var url;
var _a2;
var _a3;
while(this.visibleImages&&this.visibleImages.length>0){
_a3=this.visibleImages.pop();
this.cache.push(_a3);
if(!this.disableHide){
_91.addClass(_a3,"hidden");
}
_a3._cached=true;
}
if(this.cache&&this.cache.length>0){
setTimeout(this.hideCached,1000);
}
if(!this.items||this.items.length==0){
return;
}
for(i=0;i<this.items.length;i++){
_a2=this.items[i];
url=(_91.isString(_a2)?_a2:_a2[this.urlParam]);
this.addThumb(_a2,url,i);
if(this.maxRows>0&&(i+1)/this.maxPerRow>=this.maxRows){
break;
}
}
if(!this._thumbSize){
return;
}
var _a4=0;
var row=-1;
var _a5=this._thumbSize.w+(this.padding*2);
var _a6=this._thumbSize.h+(this.padding*2);
var _a7=this.thumbNodes=_91.query(".mblThumb",this.domNode);
var pos=0;
_a7=this.visibleImages;
for(i=0;i<_a7.length;i++){
if(_a7[i]._cached){
continue;
}
if(pos%this.maxPerRow==0){
row++;
}
_a4=pos%this.maxPerRow;
this.place(_a7[i],(_a4*_a5)+this.padding,(row*_a6)+this.padding);
if(!_a7[i]._loading){
_91.removeClass(_a7[i],"hidden");
}
if(pos==this.selectedIndex){
_91[pos==this.selectedIndex?"addClass":"removeClass"](_a7[i],"selected");
}
pos++;
}
var _a8=Math.ceil(pos/this.maxPerRow);
this._numRows=_a8;
this.setContainerHeight((_a8*(this._thumbSize.h+this.padding*2)));
},setContainerHeight:function(_a9){
_91.style(this.domNode,"height",_a9+"px");
},addThumb:function(_aa,url,_ab){
var _ac;
var _ad=false;
if(this.cache.length>0){
var _ae=false;
for(var i=0;i<this.cache.length;i++){
if(this.cache[i]._url==url){
_ac=this.cache.splice(i,1)[0];
_ae=true;
break;
}
}
if(!_ac&&!this.cacheMustMatch){
_ac=this.cache.pop();
_91.removeClass(_ac,"selected");
}else{
_ad=true;
}
}
if(!_ac){
_ac=_91.create("div",{"class":"mblThumb hidden",innerHTML:_91.string.substitute(this.itemTemplate,{url:url},null,this)},this.domNode);
}
if(this.labelParam){
var _af=_91.query(".mblThumbLabel",_ac)[0];
if(!_af){
_af=_91.create("div",{"class":"mblThumbLabel"},_ac);
}
_af.innerHTML=_aa[this.labelParam]||"";
}
_91.style(_ac,"display","");
if(!this.disableHide){
_91.addClass(_ac,"hidden");
}
if(!_ad){
var _b0=_91.create("img",{});
_b0._thumbDiv=_ac;
_b0._conn=_91.connect(_b0,"onload",this.handleImgLoad);
_b0._url=url;
_ac._loading=true;
this._onLoadImages[url]=_b0;
if(_b0){
_b0.src=url;
}
}
this.visibleImages.push(_ac);
_ac._index=_ab;
_ac._item=_aa;
_ac._url=url;
_ac._cached=false;
if(!this._thumbSize){
this._thumbSize=_91.marginBox(_ac);
if(this._thumbSize.h==0){
this._thumbSize.h=100;
this._thumbSize.w=100;
}
if(this.labelParam){
this._thumbSize.h+=8;
}
this.calcPadding();
}
},handleImgLoad:function(_b1){
var img=_b1.target;
_91.disconnect(img._conn);
_91.removeClass(img._thumbDiv,"hidden");
img._thumbDiv._loading=false;
img._conn=null;
var url=img._url;
if(this.cacheBust){
url+=(url.indexOf("?")>-1?"&":"?")+"cacheBust="+(new Date()).getTime()+"_"+(this._cacheCounter++);
}
_91.query(".mblThumbSrc",img._thumbDiv).style("backgroundImage","url("+url+")");
delete this._onLoadImages[img._url];
},calcPadding:function(){
var _b2=this._size.w;
var _b3=this._thumbSize.w;
var _b4=_b3+this.minPadding;
this.maxPerRow=Math.floor(_b2/_b4);
this.padding=Math.floor((_b2-(_b3*this.maxPerRow))/(this.maxPerRow*2));
},place:function(_b5,x,y){
_91.style(_b5,{"-webkit-transform":"translate("+x+"px,"+y+"px)"});
},destroy:function(){
var img;
var _b6=0;
for(var url in this._onLoadImages){
img=this._onLoadImages[url];
if(img){
img.src=null;
_b6++;
}
}
this.inherited(arguments);
}});
});
},"dojo/dom-style":function(){
define(["./_base/sniff","./dom"],function(has,dom){
var _b7,_b8={};
if(has("webkit")){
_b7=function(_b9){
var s;
if(_b9.nodeType==1){
var dv=_b9.ownerDocument.defaultView;
s=dv.getComputedStyle(_b9,null);
if(!s&&_b9.style){
_b9.style.display="";
s=dv.getComputedStyle(_b9,null);
}
}
return s||{};
};
}else{
if(has("ie")&&(has("ie")<9||has("quirks"))){
_b7=function(_ba){
return _ba.nodeType==1?_ba.currentStyle:{};
};
}else{
_b7=function(_bb){
return _bb.nodeType==1?_bb.ownerDocument.defaultView.getComputedStyle(_bb,null):{};
};
}
}
_b8.getComputedStyle=_b7;
var _bc;
if(!has("ie")){
_bc=function(_bd,_be){
return parseFloat(_be)||0;
};
}else{
_bc=function(_bf,_c0){
if(!_c0){
return 0;
}
if(_c0=="medium"){
return 4;
}
if(_c0.slice&&_c0.slice(-2)=="px"){
return parseFloat(_c0);
}
var s=_bf.style,rs=_bf.runtimeStyle,cs=_bf.currentStyle,_c1=s.left,_c2=rs.left;
rs.left=cs.left;
try{
s.left=_c0;
_c0=s.pixelLeft;
}
catch(e){
_c0=0;
}
s.left=_c1;
rs.left=_c2;
return _c0;
};
}
_b8.toPixelValue=_bc;
var _c3="DXImageTransform.Microsoft.Alpha";
var af=function(n,f){
try{
return n.filters.item(_c3);
}
catch(e){
return f?{}:null;
}
};
var _c4=has("ie")<9||(has("ie")&&has("quirks"))?function(_c5){
try{
return af(_c5).Opacity/100;
}
catch(e){
return 1;
}
}:function(_c6){
return _b7(_c6).opacity;
};
var _c7=has("ie")<9||(has("ie")&&has("quirks"))?function(_c8,_c9){
var ov=_c9*100,_ca=_c9==1;
_c8.style.zoom=_ca?"":1;
if(!af(_c8)){
if(_ca){
return _c9;
}
_c8.style.filter+=" progid:"+_c3+"(Opacity="+ov+")";
}else{
af(_c8,1).Opacity=ov;
}
af(_c8,1).Enabled=!_ca;
if(_c8.tagName.toLowerCase()=="tr"){
for(var td=_c8.firstChild;td;td=td.nextSibling){
if(td.tagName.toLowerCase()=="td"){
_c7(td,_c9);
}
}
}
return _c9;
}:function(_cb,_cc){
return _cb.style.opacity=_cc;
};
var _cd={left:true,top:true};
var _ce=/margin|padding|width|height|max|min|offset/;
function _cf(_d0,_d1,_d2){
_d1=_d1.toLowerCase();
if(has("ie")){
if(_d2=="auto"){
if(_d1=="height"){
return _d0.offsetHeight;
}
if(_d1=="width"){
return _d0.offsetWidth;
}
}
if(_d1=="fontweight"){
switch(_d2){
case 700:
return "bold";
case 400:
default:
return "normal";
}
}
}
if(!(_d1 in _cd)){
_cd[_d1]=_ce.test(_d1);
}
return _cd[_d1]?_bc(_d0,_d2):_d2;
};
var _d3=has("ie")?"styleFloat":"cssFloat",_d4={"cssFloat":_d3,"styleFloat":_d3,"float":_d3};
_b8.get=function getStyle(_d5,_d6){
var n=dom.byId(_d5),l=arguments.length,op=(_d6=="opacity");
if(l==2&&op){
return _c4(n);
}
_d6=_d4[_d6]||_d6;
var s=_b8.getComputedStyle(n);
return (l==1)?s:_cf(n,_d6,s[_d6]||n.style[_d6]);
};
_b8.set=function setStyle(_d7,_d8,_d9){
var n=dom.byId(_d7),l=arguments.length,op=(_d8=="opacity");
_d8=_d4[_d8]||_d8;
if(l==3){
return op?_c7(n,_d9):n.style[_d8]=_d9;
}
for(var x in _d8){
_b8.set(_d7,x,_d8[x]);
}
return _b8.getComputedStyle(n);
};
return _b8;
});
},"dojox/mobile/TransitionEvent":function(){
define("dojox/mobile/TransitionEvent",["dojo/_base/declare","dojo/_base/Deferred","dojo/_base/lang","dojo/on","./transition"],function(_da,_db,_dc,on,_dd){
return _da("dojox.mobile.TransitionEvent",null,{constructor:function(_de,_df,_e0){
this.transitionOptions=_df;
this.target=_de;
this.triggerEvent=_e0||null;
},dispatch:function(){
var _e1={bubbles:true,cancelable:true,detail:this.transitionOptions,triggerEvent:this.triggerEvent};
var evt=on.emit(this.target,"startTransition",_e1);
if(evt){
_db.when(_dd,_dc.hitch(this,function(_e2){
_db.when(_e2.call(this,evt),_dc.hitch(this,function(_e3){
this.endTransition(_e3);
}));
}));
}
},endTransition:function(_e4){
on.emit(this.target,"endTransition",{detail:_e4.transitionOptions});
}});
});
},"dojox/mobile/ViewController":function(){
define("dojox/mobile/ViewController",["dojo/_base/kernel","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom","dojo/dom-class","dojo/dom-construct","dojo/on","dojo/ready","dijit/registry","./ProgressIndicator","./TransitionEvent"],function(_e5,_e6,_e7,_e8,_e9,win,dom,_ea,_eb,on,_ec,_ed,_ee,_ef){
var dm=_e9.getObject("dojox.mobile",true);
var _f0=_e8("dojox.mobile.ViewController",null,{constructor:function(){
this.viewMap={};
this.currentView=null;
this.defaultView=null;
_ec(_e9.hitch(this,function(){
on(win.body(),"startTransition",_e9.hitch(this,"onStartTransition"));
}));
},findCurrentView:function(_f1,src){
if(_f1){
var w=_ed.byId(_f1);
if(w&&w.getShowingView){
return w.getShowingView();
}
}
if(dm.currentView){
return dm.currentView;
}
w=src;
while(true){
w=w.getParent();
if(!w){
return null;
}
if(_ea.contains(w.domNode,"mblView")){
break;
}
}
return w;
},onStartTransition:function(evt){
evt.preventDefault();
if(!evt.detail||(evt.detail&&!evt.detail.moveTo&&!evt.detail.href&&!evt.detail.url&&!evt.detail.scene)){
return;
}
var w=this.findCurrentView(evt.detail.moveTo,(evt.target&&evt.target.id)?_ed.byId(evt.target.id):_ed.byId(evt.target));
if(!w||(evt.detail&&evt.detail.moveTo&&w===_ed.byId(evt.detail.moveTo))){
return;
}
if(evt.detail.href){
var t=_ed.byId(evt.target.id).hrefTarget;
if(t){
dm.openWindow(evt.detail.href,t);
}else{
w.performTransition(null,evt.detail.transitionDir,evt.detail.transition,evt.target,function(){
location.href=evt.detail.href;
});
}
return;
}else{
if(evt.detail.scene){
_e7.publish("/dojox/mobile/app/pushScene",[evt.detail.scene]);
return;
}
}
var _f2=evt.detail.moveTo;
if(evt.detail.url){
var id;
if(dm._viewMap&&dm._viewMap[evt.detail.url]){
id=dm._viewMap[evt.detail.url];
}else{
var _f3=this._text;
if(!_f3){
if(_ed.byId(evt.target.id).sync){
_e5.xhrGet({url:evt.detail.url,sync:true,load:function(_f4){
_f3=_e9.trim(_f4);
}});
}else{
var s="dojo/_base/xhr";
require([s],_e9.hitch(this,function(xhr){
var _f5=_ee.getInstance();
win.body().appendChild(_f5.domNode);
_f5.start();
var obj=xhr.get({url:evt.detail.url,handleAs:"text"});
obj.addCallback(_e9.hitch(this,function(_f6,_f7){
_f5.stop();
if(_f6){
this._text=_f6;
new _ef(evt.target,{transition:evt.detail.transition,transitionDir:evt.detail.transitionDir,moveTo:_f2,href:evt.detail.href,url:evt.detail.url,scene:evt.detail.scene},evt.detail).dispatch();
}
}));
obj.addErrback(function(_f8){
_f5.stop();
console.log("Failed to load "+evt.detail.url+"\n"+(_f8.description||_f8));
});
}));
return;
}
}
this._text=null;
id=this._parse(_f3,_ed.byId(evt.target.id).urlTarget);
if(!dm._viewMap){
dm._viewMap=[];
}
dm._viewMap[evt.detail.url]=id;
}
_f2=id;
w=this.findCurrentView(_f2,_ed.byId(evt.target.id))||w;
}
w.performTransition(_f2,evt.detail.transitionDir,evt.detail.transition,null,null);
},_parse:function(_f9,id){
var _fa,_fb,i,j,len;
var _fc=this.findCurrentView();
var _fd=_ed.byId(id)&&_ed.byId(id).containerNode||dom.byId(id)||_fc&&_fc.domNode.parentNode||win.body();
var _fe=null;
for(j=_fd.childNodes.length-1;j>=0;j--){
var c=_fd.childNodes[j];
if(c.nodeType===1){
if(c.getAttribute("fixed")==="bottom"){
_fe=c;
}
break;
}
}
if(_f9.charAt(0)==="<"){
_fa=_eb.create("DIV",{innerHTML:_f9});
for(i=0;i<_fa.childNodes.length;i++){
var n=_fa.childNodes[i];
if(n.nodeType===1){
_fb=n;
break;
}
}
if(!_fb){
console.log("dojox.mobile.ViewController#_parse: invalid view content");
return;
}
_fb.style.visibility="hidden";
_fd.insertBefore(_fa,_fe);
var ws=_e5.parser.parse(_fa);
_e6.forEach(ws,function(w){
if(w&&!w._started&&w.startup){
w.startup();
}
});
for(i=0,len=_fa.childNodes.length;i<len;i++){
_fd.insertBefore(_fa.firstChild,_fe);
}
_fd.removeChild(_fa);
_ed.byNode(_fb)._visible=true;
}else{
if(_f9.charAt(0)==="{"){
_fa=_eb.create("DIV");
_fd.insertBefore(_fa,_fe);
this._ws=[];
_fb=this._instantiate(eval("("+_f9+")"),_fa);
for(i=0;i<this._ws.length;i++){
var w=this._ws[i];
w.startup&&!w._started&&(!w.getParent||!w.getParent())&&w.startup();
}
this._ws=null;
}
}
_fb.style.display="none";
_fb.style.visibility="visible";
return _e5.hash?"#"+_fb.id:_fb.id;
},_instantiate:function(obj,_ff,_100){
var _101;
for(var key in obj){
if(key.charAt(0)=="@"){
continue;
}
var cls=_e9.getObject(key);
if(!cls){
continue;
}
var _102={};
var _103=cls.prototype;
var objs=_e9.isArray(obj[key])?obj[key]:[obj[key]];
for(var i=0;i<objs.length;i++){
for(var prop in objs[i]){
if(prop.charAt(0)=="@"){
var val=objs[i][prop];
prop=prop.substring(1);
if(typeof _103[prop]=="string"){
_102[prop]=val;
}else{
if(typeof _103[prop]=="number"){
_102[prop]=val-0;
}else{
if(typeof _103[prop]=="boolean"){
_102[prop]=(val!="false");
}else{
if(typeof _103[prop]=="object"){
_102[prop]=eval("("+val+")");
}
}
}
}
}
}
_101=new cls(_102,_ff);
if(_ff){
_101._visible=true;
this._ws.push(_101);
}
if(_100&&_100.addChild){
_100.addChild(_101);
}
this._instantiate(objs[i],null,_101);
}
}
return _101&&_101.domNode;
}});
new _f0();
return _f0;
});
},"dojox/mobile/_ItemBase":function(){
define("dojox/mobile/_ItemBase",["dojo/_base/kernel","dojo/_base/config","dojo/_base/declare","dijit/registry","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./TransitionEvent","./View"],function(_104,_105,_106,_107,_108,_109,_10a,_10b,View){
return _106("dojox.mobile._ItemBase",[_10a,_109,_108],{icon:"",iconPos:"",alt:"",href:"",hrefTarget:"",moveTo:"",scene:"",clickable:false,url:"",urlTarget:"",transition:"",transitionDir:1,transitionOptions:null,callback:null,sync:true,label:"",toggle:false,_duration:800,inheritParams:function(){
var _10c=this.getParent();
if(_10c){
if(!this.transition){
this.transition=_10c.transition;
}
if(this.icon&&_10c.iconBase&&_10c.iconBase.charAt(_10c.iconBase.length-1)==="/"){
this.icon=_10c.iconBase+this.icon;
}
if(!this.icon){
this.icon=_10c.iconBase;
}
if(!this.iconPos){
this.iconPos=_10c.iconPos;
}
}
},select:function(){
},deselect:function(){
},defaultClickAction:function(e){
if(this.toggle){
if(this.selected){
this.deselect();
}else{
this.select();
}
}else{
if(!this.selected){
this.select();
if(!this.selectOne){
var _10d=this;
setTimeout(function(){
_10d.deselect();
},this._duration);
}
var _10e;
if(this.moveTo||this.href||this.url||this.scene){
_10e={moveTo:this.moveTo,href:this.href,url:this.url,scene:this.scene,transition:this.transition,transitionDir:this.transitionDir};
}else{
if(this.transitionOptions){
_10e=this.transitionOptions;
}
}
if(_10e){
return new _10b(this.domNode,_10e,e).dispatch();
}
}
}
},getParent:function(){
var ref=this.srcNodeRef||this.domNode;
return ref&&ref.parentNode?_107.getEnclosingWidget(ref.parentNode):null;
},setTransitionPos:function(e){
var w=this;
while(true){
w=w.getParent();
if(!w||w instanceof View){
break;
}
}
if(w){
w.clickedPosX=e.clientX;
w.clickedPosY=e.clientY;
}
},transitionTo:function(_10f,href,url,_110){
if(_105.isDebug){
var _111=arguments.callee._ach||(arguments.callee._ach={}),_112=(arguments.callee.caller||"unknown caller").toString();
if(!_111[_112]){
_104.deprecated(this.declaredClass+"::transitionTo() is deprecated."+_112,"","2.0");
_111[_112]=true;
}
}
new _10b(this.domNode,{moveTo:_10f,href:href,url:url,scene:_110,transition:this.transition,transitionDir:this.transitionDir}).dispatch();
}});
});
},"dojox/mobile/ToolBarButton":function(){
define("dojox/mobile/ToolBarButton",["dojo/_base/declare","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-style","./common","./_ItemBase"],function(_113,win,_114,_115,_116,_117,_118){
return _113("dojox.mobile.ToolBarButton",_118,{selected:false,btnClass:"",_defaultColor:"mblColorDefault",_selColor:"mblColorDefaultSel",buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("div");
this.inheritParams();
_114.add(this.domNode,"mblToolBarButton mblArrowButtonText");
var _119;
if(this.selected){
_119=this._selColor;
}else{
if(this.domNode.className.indexOf("mblColor")==-1){
_119=this._defaultColor;
}
}
_114.add(this.domNode,_119);
if(!this.label){
this.label=this.domNode.innerHTML;
}
if(this.icon&&this.icon!="none"){
this.iconNode=_115.create("div",{className:"mblToolBarButtonIcon"},this.domNode);
_117.createIcon(this.icon,this.iconPos,null,this.alt,this.iconNode);
if(this.iconPos){
_114.add(this.iconNode.firstChild,"mblToolBarButtonSpriteIcon");
}
}else{
if(_117.createDomButton(this.domNode)){
_114.add(this.domNode,"mblToolBarButtonDomButton");
}else{
_114.add(this.domNode,"mblToolBarButtonText");
}
}
this.connect(this.domNode,"onclick","onClick");
},select:function(){
_114.toggle(this.domNode,this._selColor,!arguments[0]);
this.selected=!arguments[0];
},deselect:function(){
this.select(true);
},onClick:function(e){
this.setTransitionPos(e);
this.defaultClickAction();
},_setBtnClassAttr:function(_11a){
var node=this.domNode;
if(node.className.match(/(mblDomButton\w+)/)){
_114.remove(node,RegExp.$1);
}
_114.add(node,_11a);
if(_117.createDomButton(this.domNode)){
_114.add(this.domNode,"mblToolBarButtonDomButton");
}
},_setLabelAttr:function(text){
this.label=text;
this.domNode.innerHTML=this._cv?this._cv(text):text;
}});
});
},"dijit/hccss":function(){
define("dijit/hccss",["require","dojo/_base/config","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dojo/ready","dojo/_base/sniff","dojo/_base/window"],function(_11b,_11c,_11d,_11e,_11f,_120,has,win){
if(has("ie")||has("mozilla")){
_120(90,function(){
var div=_11e.create("div",{id:"a11yTestNode",style:{cssText:"border: 1px solid;"+"border-color:red green;"+"position: absolute;"+"height: 5px;"+"top: -999px;"+"background-image: url(\""+(_11c.blankGif||_11b.toUrl("dojo/resources/blank.gif"))+"\");"}},win.body());
var cs=_11f.getComputedStyle(div);
if(cs){
var _121=cs.backgroundImage;
var _122=(cs.borderTopColor==cs.borderRightColor)||(_121!=null&&(_121=="none"||_121=="url(invalid-url:)"));
if(_122){
_11d.add(win.body(),"dijit_a11y");
}
if(has("ie")){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
}
});
}
});
},"dojo/dom-form":function(){
define("dojo/dom-form",["./_base/lang","./dom","./io-query","./json"],function(lang,dom,ioq,json){
function _123(obj,name,_124){
if(_124===null){
return;
}
var val=obj[name];
if(typeof val=="string"){
obj[name]=[val,_124];
}else{
if(lang.isArray(val)){
val.push(_124);
}else{
obj[name]=_124;
}
}
};
var _125="file|submit|image|reset|button";
var form={fieldToObject:function fieldToObject(_126){
var ret=null;
_126=dom.byId(_126);
if(_126){
var _127=_126.name,type=(_126.type||"").toLowerCase();
if(_127&&type&&!_126.disabled){
if(type=="radio"||type=="checkbox"){
if(_126.checked){
ret=_126.value;
}
}else{
if(_126.multiple){
ret=[];
var _128=[_126.firstChild];
while(_128.length){
for(var node=_128.pop();node;node=node.nextSibling){
if(node.nodeType==1&&node.tagName.toLowerCase()=="option"){
if(node.selected){
ret.push(node.value);
}
}else{
if(node.nextSibling){
_128.push(node.nextSibling);
}
if(node.firstChild){
_128.push(node.firstChild);
}
break;
}
}
}
}else{
ret=_126.value;
}
}
}
}
return ret;
},toObject:function formToObject(_129){
var ret={},_12a=dom.byId(_129).elements;
for(var i=0,l=_12a.length;i<l;++i){
var item=_12a[i],_12b=item.name,type=(item.type||"").toLowerCase();
if(_12b&&type&&_125.indexOf(type)<0&&!item.disabled){
_123(ret,_12b,form.fieldToObject(item));
if(type=="image"){
ret[_12b+".x"]=ret[_12b+".y"]=ret[_12b].x=ret[_12b].y=0;
}
}
}
return ret;
},toQuery:function formToQuery(_12c){
return ioq.objectToQuery(form.toObject(_12c));
},toJson:function formToJson(_12d,_12e){
return json.stringify(form.toObject(_12d),null,_12e?4:0);
}};
return form;
});
},"dijit/_Contained":function(){
define("dijit/_Contained",["dojo/_base/declare","./registry"],function(_12f,_130){
return _12f("dijit._Contained",null,{_getSibling:function(_131){
var node=this.domNode;
do{
node=node[_131+"Sibling"];
}while(node&&node.nodeType!=1);
return node&&_130.byNode(node);
},getPreviousSibling:function(){
return this._getSibling("previous");
},getNextSibling:function(){
return this._getSibling("next");
},getIndexInParent:function(){
var p=this.getParent();
if(!p||!p.getIndexOfChild){
return -1;
}
return p.getIndexOfChild(this);
}});
});
},"dijit/form/_TextBoxMixin":function(){
define("dijit/form/_TextBoxMixin",["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/_base/event","dojo/keys","dojo/_base/lang",".."],function(_132,_133,dom,_134,keys,lang,_135){
var _136=_133("dijit.form._TextBoxMixin",null,{trim:false,uppercase:false,lowercase:false,propercase:false,maxLength:"",selectOnClick:false,placeHolder:"",_getValueAttr:function(){
return this.parse(this.get("displayedValue"),this.constraints);
},_setValueAttr:function(_137,_138,_139){
var _13a;
if(_137!==undefined){
_13a=this.filter(_137);
if(typeof _139!="string"){
if(_13a!==null&&((typeof _13a!="number")||!isNaN(_13a))){
_139=this.filter(this.format(_13a,this.constraints));
}else{
_139="";
}
}
}
if(_139!=null&&_139!=undefined&&((typeof _139)!="number"||!isNaN(_139))&&this.textbox.value!=_139){
this.textbox.value=_139;
this._set("displayedValue",this.get("displayedValue"));
}
if(this.textDir=="auto"){
this.applyTextDir(this.focusNode,_139);
}
this.inherited(arguments,[_13a,_138]);
},displayedValue:"",_getDisplayedValueAttr:function(){
return this.filter(this.textbox.value);
},_setDisplayedValueAttr:function(_13b){
if(_13b===null||_13b===undefined){
_13b="";
}else{
if(typeof _13b!="string"){
_13b=String(_13b);
}
}
this.textbox.value=_13b;
this._setValueAttr(this.get("value"),undefined);
this._set("displayedValue",this.get("displayedValue"));
if(this.textDir=="auto"){
this.applyTextDir(this.focusNode,_13b);
}
},format:function(_13c){
return ((_13c==null||_13c==undefined)?"":(_13c.toString?_13c.toString():_13c));
},parse:function(_13d){
return _13d;
},_refreshState:function(){
},onInput:function(){
},__skipInputEvent:false,_onInput:function(){
if(this.textDir=="auto"){
this.applyTextDir(this.focusNode,this.focusNode.value);
}
this._refreshState();
this._set("displayedValue",this.get("displayedValue"));
},postCreate:function(){
this.textbox.setAttribute("value",this.textbox.value);
this.inherited(arguments);
var _13e=function(e){
var _13f=e.charOrCode||e.keyCode||229;
if(e.type=="keydown"){
switch(_13f){
case keys.SHIFT:
case keys.ALT:
case keys.CTRL:
case keys.META:
case keys.CAPS_LOCK:
return;
default:
if(_13f>=65&&_13f<=90){
return;
}
}
}
if(e.type=="keypress"&&typeof _13f!="string"){
return;
}
if(e.type=="input"){
if(this.__skipInputEvent){
this.__skipInputEvent=false;
return;
}
}else{
this.__skipInputEvent=true;
}
var faux=lang.mixin({},e,{charOrCode:_13f,wasConsumed:false,preventDefault:function(){
faux.wasConsumed=true;
e.preventDefault();
},stopPropagation:function(){
e.stopPropagation();
}});
if(this.onInput(faux)===false){
_134.stop(faux);
}
if(faux.wasConsumed){
return;
}
setTimeout(lang.hitch(this,"_onInput",faux),0);
};
_132.forEach(["onkeydown","onkeypress","onpaste","oncut","oninput"],function(_140){
this.connect(this.textbox,_140,_13e);
},this);
},_blankValue:"",filter:function(val){
if(val===null){
return this._blankValue;
}
if(typeof val!="string"){
return val;
}
if(this.trim){
val=lang.trim(val);
}
if(this.uppercase){
val=val.toUpperCase();
}
if(this.lowercase){
val=val.toLowerCase();
}
if(this.propercase){
val=val.replace(/[^\s]+/g,function(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
});
}
return val;
},_setBlurValue:function(){
this._setValueAttr(this.get("value"),true);
},_onBlur:function(e){
if(this.disabled){
return;
}
this._setBlurValue();
this.inherited(arguments);
if(this._selectOnClickHandle){
this.disconnect(this._selectOnClickHandle);
}
},_isTextSelected:function(){
return this.textbox.selectionStart==this.textbox.selectionEnd;
},_onFocus:function(by){
if(this.disabled||this.readOnly){
return;
}
if(this.selectOnClick&&by=="mouse"){
this._selectOnClickHandle=this.connect(this.domNode,"onmouseup",function(){
this.disconnect(this._selectOnClickHandle);
if(this._isTextSelected()){
_136.selectInputText(this.textbox);
}
});
}
this.inherited(arguments);
this._refreshState();
},reset:function(){
this.textbox.value="";
this.inherited(arguments);
},_setTextDirAttr:function(_141){
if(!this._created||this.textDir!=_141){
this._set("textDir",_141);
this.applyTextDir(this.focusNode,this.focusNode.value);
}
}});
_136._setSelectionRange=_135._setSelectionRange=function(_142,_143,stop){
if(_142.setSelectionRange){
_142.setSelectionRange(_143,stop);
}
};
_136.selectInputText=_135.selectInputText=function(_144,_145,stop){
_144=dom.byId(_144);
if(isNaN(_145)){
_145=0;
}
if(isNaN(stop)){
stop=_144.value?_144.value.length:0;
}
try{
_144.focus();
_136._setSelectionRange(_144,_145,stop);
}
catch(e){
}
};
return _136;
});
},"dojox/mobile/parser":function(){
define("dojox/mobile/parser",["dojo/_base/kernel","dojo/_base/config","dojo/_base/lang","dojo/_base/window","dojo/ready"],function(dojo,_146,lang,win,_147){
var dm=lang.getObject("dojox.mobile",true);
var _148=new function(){
this.instantiate=function(_149,_14a,args){
_14a=_14a||{};
args=args||{};
var i,ws=[];
if(_149){
for(i=0;i<_149.length;i++){
var n=_149[i];
var cls=lang.getObject(n.getAttribute("dojoType")||n.getAttribute("data-dojo-type"));
var _14b=cls.prototype;
var _14c={},prop,v,t;
lang.mixin(_14c,eval("({"+(n.getAttribute("data-dojo-props")||"")+"})"));
lang.mixin(_14c,args.defaults);
lang.mixin(_14c,_14a);
for(prop in _14b){
v=n.getAttributeNode(prop);
v=v&&v.nodeValue;
t=typeof _14b[prop];
if(!v&&(t!=="boolean"||v!=="")){
continue;
}
if(t==="string"){
_14c[prop]=v;
}else{
if(t==="number"){
_14c[prop]=v-0;
}else{
if(t==="boolean"){
_14c[prop]=(v!=="false");
}else{
if(t==="object"){
_14c[prop]=eval("("+v+")");
}
}
}
}
}
_14c["class"]=n.className;
_14c.style=n.style&&n.style.cssText;
v=n.getAttribute("data-dojo-attach-point");
if(v){
_14c.dojoAttachPoint=v;
}
v=n.getAttribute("data-dojo-attach-event");
if(v){
_14c.dojoAttachEvent=v;
}
var _14d=new cls(_14c,n);
ws.push(_14d);
var jsId=n.getAttribute("jsId")||n.getAttribute("data-dojo-id");
if(jsId){
lang.setObject(jsId,_14d);
}
}
for(i=0;i<ws.length;i++){
var w=ws[i];
!args.noStart&&w.startup&&!w._started&&w.startup();
}
}
return ws;
};
this.parse=function(_14e,args){
if(!_14e){
_14e=win.body();
}else{
if(!args&&_14e.rootNode){
args=_14e;
_14e=_14e.rootNode;
}
}
var _14f=_14e.getElementsByTagName("*");
var i,list=[];
for(i=0;i<_14f.length;i++){
var n=_14f[i];
if(n.getAttribute("dojoType")||n.getAttribute("data-dojo-type")){
list.push(n);
}
}
var _150=args&&args.template?{template:true}:null;
return this.instantiate(list,_150,args);
};
}();
if(_146.parseOnLoad){
_147(100,_148,"parse");
}
dm.parser=_148;
dojo.parser=_148;
return _148;
});
},"dojox/mobile/scrollable":function(){
define("dojox/mobile/scrollable",["dojo/_base/kernel","dojo/_base/connect","dojo/_base/event","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-style","./sniff"],function(dojo,_151,_152,lang,win,_153,_154,_155,has){
var dm=lang.getObject("dojox.mobile",true);
var _156=function(dojo,_157){
this.fixedHeaderHeight=0;
this.fixedFooterHeight=0;
this.isLocalFooter=false;
this.scrollBar=true;
this.scrollDir="v";
this.weight=0.6;
this.fadeScrollBar=true;
this.disableFlashScrollBar=false;
this.threshold=4;
this.constraint=true;
this.touchNode=null;
this.isNested=false;
this.dirLock=false;
this.height="";
this.androidWorkaroud=true;
this.init=function(_158){
if(_158){
for(var p in _158){
if(_158.hasOwnProperty(p)){
this[p]=((p=="domNode"||p=="containerNode")&&typeof _158[p]=="string")?win.doc.getElementById(_158[p]):_158[p];
}
}
}
this.touchNode=this.touchNode||this.containerNode;
this._v=(this.scrollDir.indexOf("v")!=-1);
this._h=(this.scrollDir.indexOf("h")!=-1);
this._f=(this.scrollDir=="f");
this._ch=[];
this._ch.push(_151.connect(this.touchNode,has("touch")?"touchstart":"onmousedown",this,"onTouchStart"));
if(has("webkit")){
this._ch.push(_151.connect(this.domNode,"webkitAnimationEnd",this,"onFlickAnimationEnd"));
this._ch.push(_151.connect(this.domNode,"webkitAnimationStart",this,"onFlickAnimationStart"));
this._aw=this.androidWorkaroud&&has("android")>=2.2&&has("android")<3;
if(this._aw){
this._ch.push(_151.connect(win.global,"onresize",this,"onScreenSizeChanged"));
this._ch.push(_151.connect(win.global,"onfocus",this,function(e){
if(this.containerNode.style.webkitTransform){
this.stopAnimation();
this.toTopLeft();
}
}));
this._sz=this.getScreenSize();
}
for(var i=0;i<3;i++){
this.setKeyframes(null,null,i);
}
}
if(has("iphone")){
_155.set(this.containerNode,"webkitTransform","translate3d(0,0,0)");
}
this._speed={x:0,y:0};
this._appFooterHeight=0;
if(this.isTopLevel()&&!this.noResize){
this.resize();
}
var _159=this;
setTimeout(function(){
_159.flashScrollBar();
},600);
};
this.isTopLevel=function(){
return true;
};
this.cleanup=function(){
if(this._ch){
for(var i=0;i<this._ch.length;i++){
_151.disconnect(this._ch[i]);
}
this._ch=null;
}
};
this.findDisp=function(node){
if(!node.parentNode){
return null;
}
var _15a=node.parentNode.childNodes;
for(var i=0;i<_15a.length;i++){
var n=_15a[i];
if(n.nodeType===1&&_153.contains(n,"mblView")&&n.style.display!=="none"){
return n;
}
}
return node;
};
this.getScreenSize=function(){
return {h:win.global.innerHeight||win.doc.documentElement.clientHeight||win.doc.documentElement.offsetHeight,w:win.global.innerWidth||win.doc.documentElement.clientWidth||win.doc.documentElement.offsetWidth};
};
this.isKeyboardShown=function(e){
if(!this._sz){
return false;
}
var sz=this.getScreenSize();
return (sz.w*sz.h)/(this._sz.w*this._sz.h)<0.8;
};
this.disableScroll=function(v){
if(this.disableTouchScroll===v||this.domNode.style.display==="none"){
return;
}
this.disableTouchScroll=v;
this.scrollBar=!v;
dm.disableHideAddressBar=dm.disableResizeAll=v;
var of=v?"visible":"hidden";
_155.set(this.domNode,"overflow",of);
_155.set(win.doc.documentElement,"overflow",of);
_155.set(win.body(),"overflow",of);
var c=this.containerNode;
if(v){
if(!c.style.webkitTransform){
this.stopAnimation();
this.toTopLeft();
}
var mt=parseInt(c.style.marginTop)||0;
var h=c.offsetHeight+mt+this.fixedFooterHeight-this._appFooterHeight;
_155.set(this.domNode,"height",h+"px");
this._cPos={x:parseInt(c.style.left)||0,y:parseInt(c.style.top)||0};
_155.set(c,{top:"0px",left:"0px"});
var a=win.doc.activeElement;
if(a){
var at=0;
for(var n=a;n.tagName!="BODY";n=n.offsetParent){
at+=n.offsetTop;
}
var st=at+a.clientHeight+10-this.getScreenSize().h;
if(st>0){
win.body().scrollTop=st;
}
}
}else{
if(this._cPos){
_155.set(c,{top:this._cPos.y+"px",left:this._cPos.x+"px"});
this._cPos=null;
}
var tags=this.domNode.getElementsByTagName("*");
for(var i=0;i<tags.length;i++){
tags[i].blur&&tags[i].blur();
}
dm.resizeAll&&dm.resizeAll();
}
};
this.onScreenSizeChanged=function(e){
var sz=this.getScreenSize();
if(sz.w*sz.h>this._sz.w*this._sz.h){
this._sz=sz;
}
this.disableScroll(this.isKeyboardShown());
};
this.toTransform=function(e){
var c=this.containerNode;
if(c.offsetTop===0&&c.offsetLeft===0||!c._webkitTransform){
return;
}
_155.set(c,{webkitTransform:c._webkitTransform,top:"0px",left:"0px"});
c._webkitTransform=null;
};
this.toTopLeft=function(){
var c=this.containerNode;
if(!c.style.webkitTransform){
return;
}
c._webkitTransform=c.style.webkitTransform;
var pos=this.getPos();
_155.set(c,{webkitTransform:"",top:pos.y+"px",left:pos.x+"px"});
};
this.resize=function(e){
this._appFooterHeight=(this.fixedFooterHeight&&!this.isLocalFooter)?this.fixedFooterHeight:0;
if(this.isLocalHeader){
this.containerNode.style.marginTop=this.fixedHeaderHeight+"px";
}
var top=0;
for(var n=this.domNode;n&&n.tagName!="BODY";n=n.offsetParent){
n=this.findDisp(n);
if(!n){
break;
}
top+=n.offsetTop;
}
var h,_15b=this.getScreenSize().h,dh=_15b-top-this._appFooterHeight;
if(this.height==="inherit"){
if(this.domNode.offsetParent){
h=this.domNode.offsetParent.offsetHeight+"px";
}
}else{
if(this.height==="auto"){
var _15c=this.domNode.offsetParent;
if(_15c){
this.domNode.style.height="0px";
var _15d=_15c.getBoundingClientRect(),_15e=this.domNode.getBoundingClientRect(),_15f=_15d.bottom-this._appFooterHeight;
if(_15e.bottom>=_15f){
dh=_15b-(_15e.top-_15d.top)-this._appFooterHeight;
}else{
dh=_15f-_15e.bottom;
}
}
var _160=Math.max(this.domNode.scrollHeight,this.containerNode.scrollHeight);
h=(_160?Math.min(_160,dh):dh)+"px";
}else{
if(this.height){
h=this.height;
}
}
}
if(!h){
h=dh+"px";
}
if(h.charAt(0)!=="-"&&h!=="default"){
this.domNode.style.height=h;
}
this.onTouchEnd();
};
this.onFlickAnimationStart=function(e){
_152.stop(e);
};
this.onFlickAnimationEnd=function(e){
var an=e&&e.animationName;
if(an&&an.indexOf("scrollableViewScroll2")===-1){
if(an.indexOf("scrollableViewScroll0")!==-1){
_153.remove(this._scrollBarNodeV,"mblScrollableScrollTo0");
}else{
if(an.indexOf("scrollableViewScroll1")!==-1){
_153.remove(this._scrollBarNodeH,"mblScrollableScrollTo1");
}else{
if(this._scrollBarNodeV){
this._scrollBarNodeV.className="";
}
if(this._scrollBarNodeH){
this._scrollBarNodeH.className="";
}
}
}
return;
}
if(e&&e.srcElement){
_152.stop(e);
}
this.stopAnimation();
if(this._bounce){
var _161=this;
var _162=_161._bounce;
setTimeout(function(){
_161.slideTo(_162,0.3,"ease-out");
},0);
_161._bounce=undefined;
}else{
this.hideScrollBar();
this.removeCover();
if(this._aw){
this.toTopLeft();
}
}
};
this.isFormElement=function(node){
if(node&&node.nodeType!==1){
node=node.parentNode;
}
if(!node||node.nodeType!==1){
return false;
}
var t=node.tagName;
return (t==="SELECT"||t==="INPUT"||t==="TEXTAREA"||t==="BUTTON");
};
this.onTouchStart=function(e){
if(this.disableTouchScroll){
return;
}
if(this._conn&&(new Date()).getTime()-this.startTime<500){
return;
}
if(!this._conn){
this._conn=[];
this._conn.push(_151.connect(win.doc,has("touch")?"touchmove":"onmousemove",this,"onTouchMove"));
this._conn.push(_151.connect(win.doc,has("touch")?"touchend":"onmouseup",this,"onTouchEnd"));
}
this._aborted=false;
if(_153.contains(this.containerNode,"mblScrollableScrollTo2")){
this.abort();
}else{
if(this._scrollBarNodeV){
this._scrollBarNodeV.className="";
}
if(this._scrollBarNodeH){
this._scrollBarNodeH.className="";
}
}
if(this._aw){
this.toTransform(e);
}
this.touchStartX=e.touches?e.touches[0].pageX:e.clientX;
this.touchStartY=e.touches?e.touches[0].pageY:e.clientY;
this.startTime=(new Date()).getTime();
this.startPos=this.getPos();
this._dim=this.getDim();
this._time=[0];
this._posX=[this.touchStartX];
this._posY=[this.touchStartY];
this._locked=false;
if(!this.isFormElement(e.target)&&!this.isNested){
_152.stop(e);
}
};
this.onTouchMove=function(e){
if(this._locked){
return;
}
var x=e.touches?e.touches[0].pageX:e.clientX;
var y=e.touches?e.touches[0].pageY:e.clientY;
var dx=x-this.touchStartX;
var dy=y-this.touchStartY;
var to={x:this.startPos.x+dx,y:this.startPos.y+dy};
var dim=this._dim;
dx=Math.abs(dx);
dy=Math.abs(dy);
if(this._time.length==1){
if(this.dirLock){
if(this._v&&!this._h&&dx>=this.threshold&&dx>=dy||(this._h||this._f)&&!this._v&&dy>=this.threshold&&dy>=dx){
this._locked=true;
return;
}
}
if(this._v&&Math.abs(dy)<this.threshold||(this._h||this._f)&&Math.abs(dx)<this.threshold){
return;
}
this.addCover();
this.showScrollBar();
}
var _163=this.weight;
if(this._v&&this.constraint){
if(to.y>0){
to.y=Math.round(to.y*_163);
}else{
if(to.y<-dim.o.h){
if(dim.c.h<dim.d.h){
to.y=Math.round(to.y*_163);
}else{
to.y=-dim.o.h-Math.round((-dim.o.h-to.y)*_163);
}
}
}
}
if((this._h||this._f)&&this.constraint){
if(to.x>0){
to.x=Math.round(to.x*_163);
}else{
if(to.x<-dim.o.w){
if(dim.c.w<dim.d.w){
to.x=Math.round(to.x*_163);
}else{
to.x=-dim.o.w-Math.round((-dim.o.w-to.x)*_163);
}
}
}
}
this.scrollTo(to);
var max=10;
var n=this._time.length;
if(n>=2){
var d0,d1;
if(this._v&&!this._h){
d0=this._posY[n-1]-this._posY[n-2];
d1=y-this._posY[n-1];
}else{
if(!this._v&&this._h){
d0=this._posX[n-1]-this._posX[n-2];
d1=x-this._posX[n-1];
}
}
if(d0*d1<0){
this._time=[this._time[n-1]];
this._posX=[this._posX[n-1]];
this._posY=[this._posY[n-1]];
n=1;
}
}
if(n==max){
this._time.shift();
this._posX.shift();
this._posY.shift();
}
this._time.push((new Date()).getTime()-this.startTime);
this._posX.push(x);
this._posY.push(y);
};
this.onTouchEnd=function(e){
if(this._locked){
return;
}
var _164=this._speed={x:0,y:0};
var dim=this._dim;
var pos=this.getPos();
var to={};
if(e){
if(!this._conn){
return;
}
for(var i=0;i<this._conn.length;i++){
_151.disconnect(this._conn[i]);
}
this._conn=null;
var n=this._time.length;
var _165=false;
if(!this._aborted){
if(n<=1){
_165=true;
}else{
if(n==2&&Math.abs(this._posY[1]-this._posY[0])<4&&has("touch")){
_165=true;
}
}
}
var _166=this.isFormElement(e.target);
if(_165&&!_166){
this.hideScrollBar();
this.removeCover();
if(has("touch")){
var elem=e.target;
if(elem.nodeType!=1){
elem=elem.parentNode;
}
var ev=win.doc.createEvent("MouseEvents");
ev.initMouseEvent("click",true,true,win.global,1,e.screenX,e.screenY,e.clientX,e.clientY);
setTimeout(function(){
elem.dispatchEvent(ev);
},0);
}
return;
}else{
if(this._aw&&_165&&_166){
this.hideScrollBar();
this.toTopLeft();
return;
}
}
_164=this._speed=this.getSpeed();
}else{
if(pos.x==0&&pos.y==0){
return;
}
dim=this.getDim();
}
if(this._v){
to.y=pos.y+_164.y;
}
if(this._h||this._f){
to.x=pos.x+_164.x;
}
this.adjustDestination(to,pos);
if(this.scrollDir=="v"&&dim.c.h<dim.d.h){
this.slideTo({y:0},0.3,"ease-out");
return;
}else{
if(this.scrollDir=="h"&&dim.c.w<dim.d.w){
this.slideTo({x:0},0.3,"ease-out");
return;
}else{
if(this._v&&this._h&&dim.c.h<dim.d.h&&dim.c.w<dim.d.w){
this.slideTo({x:0,y:0},0.3,"ease-out");
return;
}
}
}
var _167,_168="ease-out";
var _169={};
if(this._v&&this.constraint){
if(to.y>0){
if(pos.y>0){
_167=0.3;
to.y=0;
}else{
to.y=Math.min(to.y,20);
_168="linear";
_169.y=0;
}
}else{
if(-_164.y>dim.o.h-(-pos.y)){
if(pos.y<-dim.o.h){
_167=0.3;
to.y=dim.c.h<=dim.d.h?0:-dim.o.h;
}else{
to.y=Math.max(to.y,-dim.o.h-20);
_168="linear";
_169.y=-dim.o.h;
}
}
}
}
if((this._h||this._f)&&this.constraint){
if(to.x>0){
if(pos.x>0){
_167=0.3;
to.x=0;
}else{
to.x=Math.min(to.x,20);
_168="linear";
_169.x=0;
}
}else{
if(-_164.x>dim.o.w-(-pos.x)){
if(pos.x<-dim.o.w){
_167=0.3;
to.x=dim.c.w<=dim.d.w?0:-dim.o.w;
}else{
to.x=Math.max(to.x,-dim.o.w-20);
_168="linear";
_169.x=-dim.o.w;
}
}
}
}
this._bounce=(_169.x!==undefined||_169.y!==undefined)?_169:undefined;
if(_167===undefined){
var _16a,_16b;
if(this._v&&this._h){
_16b=Math.sqrt(_164.x+_164.x+_164.y*_164.y);
_16a=Math.sqrt(Math.pow(to.y-pos.y,2)+Math.pow(to.x-pos.x,2));
}else{
if(this._v){
_16b=_164.y;
_16a=to.y-pos.y;
}else{
if(this._h){
_16b=_164.x;
_16a=to.x-pos.x;
}
}
}
if(_16a===0&&!e){
return;
}
_167=_16b!==0?Math.abs(_16a/_16b):0.01;
}
this.slideTo(to,_167,_168);
};
this.adjustDestination=function(to,pos){
};
this.abort=function(){
this.scrollTo(this.getPos());
this.stopAnimation();
this._aborted=true;
};
this.stopAnimation=function(){
_153.remove(this.containerNode,"mblScrollableScrollTo2");
if(has("android")){
_155.set(this.containerNode,"webkitAnimationDuration","0s");
}
if(this._scrollBarV){
this._scrollBarV.className="";
}
if(this._scrollBarH){
this._scrollBarH.className="";
}
};
this.getSpeed=function(){
var x=0,y=0,n=this._time.length;
if(n>=2&&(new Date()).getTime()-this.startTime-this._time[n-1]<500){
var dy=this._posY[n-(n>3?2:1)]-this._posY[(n-6)>=0?n-6:0];
var dx=this._posX[n-(n>3?2:1)]-this._posX[(n-6)>=0?n-6:0];
var dt=this._time[n-(n>3?2:1)]-this._time[(n-6)>=0?n-6:0];
y=this.calcSpeed(dy,dt);
x=this.calcSpeed(dx,dt);
}
return {x:x,y:y};
};
this.calcSpeed=function(d,t){
return Math.round(d/t*100)*4;
};
this.scrollTo=function(to,_16c,node){
var s=(node||this.containerNode).style;
if(has("webkit")){
s.webkitTransform=this.makeTranslateStr(to);
}else{
if(this._v){
s.top=to.y+"px";
}
if(this._h||this._f){
s.left=to.x+"px";
}
}
if(!_16c){
this.scrollScrollBarTo(this.calcScrollBarPos(to));
}
};
this.slideTo=function(to,_16d,_16e){
this._runSlideAnimation(this.getPos(),to,_16d,_16e,this.containerNode,2);
this.slideScrollBarTo(to,_16d,_16e);
};
this.makeTranslateStr=function(to){
var y=this._v&&typeof to.y=="number"?to.y+"px":"0px";
var x=(this._h||this._f)&&typeof to.x=="number"?to.x+"px":"0px";
return dm.hasTranslate3d?"translate3d("+x+","+y+",0px)":"translate("+x+","+y+")";
};
this.getPos=function(){
if(has("webkit")){
var m=win.doc.defaultView.getComputedStyle(this.containerNode,"")["-webkit-transform"];
if(m&&m.indexOf("matrix")===0){
var arr=m.split(/[,\s\)]+/);
return {y:arr[5]-0,x:arr[4]-0};
}
return {x:0,y:0};
}else{
var y=parseInt(this.containerNode.style.top)||0;
return {y:y,x:this.containerNode.offsetLeft};
}
};
this.getDim=function(){
var d={};
d.c={h:this.containerNode.offsetHeight,w:this.containerNode.offsetWidth};
d.v={h:this.domNode.offsetHeight+this._appFooterHeight,w:this.domNode.offsetWidth};
d.d={h:d.v.h-this.fixedHeaderHeight-this.fixedFooterHeight,w:d.v.w};
d.o={h:d.c.h-d.v.h+this.fixedHeaderHeight+this.fixedFooterHeight,w:d.c.w-d.v.w};
return d;
};
this.showScrollBar=function(){
if(!this.scrollBar){
return;
}
var dim=this._dim;
if(this.scrollDir=="v"&&dim.c.h<=dim.d.h){
return;
}
if(this.scrollDir=="h"&&dim.c.w<=dim.d.w){
return;
}
if(this._v&&this._h&&dim.c.h<=dim.d.h&&dim.c.w<=dim.d.w){
return;
}
var _16f=function(self,dir){
var bar=self["_scrollBarNode"+dir];
if(!bar){
var _170=_154.create("div",null,self.domNode);
var _171={position:"absolute",overflow:"hidden"};
if(dir=="V"){
_171.right="2px";
_171.width="5px";
}else{
_171.bottom=(self.isLocalFooter?self.fixedFooterHeight:0)+2+"px";
_171.height="5px";
}
_155.set(_170,_171);
_170.className="mblScrollBarWrapper";
self["_scrollBarWrapper"+dir]=_170;
bar=_154.create("div",null,_170);
_155.set(bar,{opacity:0.6,position:"absolute",backgroundColor:"#606060",fontSize:"1px",webkitBorderRadius:"2px",MozBorderRadius:"2px",webkitTransformOrigin:"0 0",zIndex:2147483647});
_155.set(bar,dir=="V"?{width:"5px"}:{height:"5px"});
self["_scrollBarNode"+dir]=bar;
}
return bar;
};
if(this._v&&!this._scrollBarV){
this._scrollBarV=_16f(this,"V");
}
if(this._h&&!this._scrollBarH){
this._scrollBarH=_16f(this,"H");
}
this.resetScrollBar();
};
this.hideScrollBar=function(){
var _172;
if(this.fadeScrollBar&&has("webkit")){
if(!dm._fadeRule){
var node=_154.create("style",null,win.doc.getElementsByTagName("head")[0]);
node.textContent=".mblScrollableFadeScrollBar{"+"  -webkit-animation-duration: 1s;"+"  -webkit-animation-name: scrollableViewFadeScrollBar;}"+"@-webkit-keyframes scrollableViewFadeScrollBar{"+"  from { opacity: 0.6; }"+"  to { opacity: 0; }}";
dm._fadeRule=node.sheet.cssRules[1];
}
_172=dm._fadeRule;
}
if(!this.scrollBar){
return;
}
var f=function(bar,self){
_155.set(bar,{opacity:0,webkitAnimationDuration:""});
if(self._aw){
bar.style.webkitTransform="";
}else{
bar.className="mblScrollableFadeScrollBar";
}
};
if(this._scrollBarV){
f(this._scrollBarV,this);
this._scrollBarV=null;
}
if(this._scrollBarH){
f(this._scrollBarH,this);
this._scrollBarH=null;
}
};
this.calcScrollBarPos=function(to){
var pos={};
var dim=this._dim;
var f=function(_173,barH,t,d,c){
var y=Math.round((d-barH-8)/(d-c)*t);
if(y<-barH+5){
y=-barH+5;
}
if(y>_173-5){
y=_173-5;
}
return y;
};
if(typeof to.y=="number"&&this._scrollBarV){
pos.y=f(this._scrollBarWrapperV.offsetHeight,this._scrollBarV.offsetHeight,to.y,dim.d.h,dim.c.h);
}
if(typeof to.x=="number"&&this._scrollBarH){
pos.x=f(this._scrollBarWrapperH.offsetWidth,this._scrollBarH.offsetWidth,to.x,dim.d.w,dim.c.w);
}
return pos;
};
this.scrollScrollBarTo=function(to){
if(!this.scrollBar){
return;
}
if(this._v&&this._scrollBarV&&typeof to.y=="number"){
if(has("webkit")){
this._scrollBarV.style.webkitTransform=this.makeTranslateStr({y:to.y});
}else{
this._scrollBarV.style.top=to.y+"px";
}
}
if(this._h&&this._scrollBarH&&typeof to.x=="number"){
if(has("webkit")){
this._scrollBarH.style.webkitTransform=this.makeTranslateStr({x:to.x});
}else{
this._scrollBarH.style.left=to.x+"px";
}
}
};
this.slideScrollBarTo=function(to,_174,_175){
if(!this.scrollBar){
return;
}
var _176=this.calcScrollBarPos(this.getPos());
var _177=this.calcScrollBarPos(to);
if(this._v&&this._scrollBarV){
this._runSlideAnimation({y:_176.y},{y:_177.y},_174,_175,this._scrollBarV,0);
}
if(this._h&&this._scrollBarH){
this._runSlideAnimation({x:_176.x},{x:_177.x},_174,_175,this._scrollBarH,1);
}
};
this._runSlideAnimation=function(from,to,_178,_179,node,idx){
if(has("webkit")){
this.setKeyframes(from,to,idx);
_155.set(node,{webkitAnimationDuration:_178+"s",webkitAnimationTimingFunction:_179});
_153.add(node,"mblScrollableScrollTo"+idx);
if(idx==2){
this.scrollTo(to,true,node);
}else{
this.scrollScrollBarTo(to);
}
}else{
if(dojo.fx&&dojo.fx.easing&&_178){
var s=dojo.fx.slideTo({node:node,duration:_178*1000,left:to.x,top:to.y,easing:(_179=="ease-out")?dojo.fx.easing.quadOut:dojo.fx.easing.linear}).play();
if(idx==2){
_151.connect(s,"onEnd",this,"onFlickAnimationEnd");
}
}else{
if(idx==2){
this.scrollTo(to,false,node);
this.onFlickAnimationEnd();
}else{
this.scrollScrollBarTo(to);
}
}
}
};
this.resetScrollBar=function(){
var f=function(_17a,bar,d,c,hd,v){
if(!bar){
return;
}
var _17b={};
_17b[v?"top":"left"]=hd+4+"px";
var t=(d-8)<=0?1:d-8;
_17b[v?"height":"width"]=t+"px";
_155.set(_17a,_17b);
var l=Math.round(d*d/c);
l=Math.min(Math.max(l-8,5),t);
bar.style[v?"height":"width"]=l+"px";
_155.set(bar,{"opacity":0.6});
};
var dim=this.getDim();
f(this._scrollBarWrapperV,this._scrollBarV,dim.d.h,dim.c.h,this.fixedHeaderHeight,true);
f(this._scrollBarWrapperH,this._scrollBarH,dim.d.w,dim.c.w,0);
this.createMask();
};
this.createMask=function(){
if(!has("webkit")){
return;
}
var ctx;
if(this._scrollBarWrapperV){
var h=this._scrollBarWrapperV.offsetHeight;
ctx=win.doc.getCSSCanvasContext("2d","scrollBarMaskV",5,h);
ctx.fillStyle="rgba(0,0,0,0.5)";
ctx.fillRect(1,0,3,2);
ctx.fillRect(0,1,5,1);
ctx.fillRect(0,h-2,5,1);
ctx.fillRect(1,h-1,3,2);
ctx.fillStyle="rgb(0,0,0)";
ctx.fillRect(0,2,5,h-4);
this._scrollBarWrapperV.style.webkitMaskImage="-webkit-canvas(scrollBarMaskV)";
}
if(this._scrollBarWrapperH){
var w=this._scrollBarWrapperH.offsetWidth;
ctx=win.doc.getCSSCanvasContext("2d","scrollBarMaskH",w,5);
ctx.fillStyle="rgba(0,0,0,0.5)";
ctx.fillRect(0,1,2,3);
ctx.fillRect(1,0,1,5);
ctx.fillRect(w-2,0,1,5);
ctx.fillRect(w-1,1,2,3);
ctx.fillStyle="rgb(0,0,0)";
ctx.fillRect(2,0,w-4,5);
this._scrollBarWrapperH.style.webkitMaskImage="-webkit-canvas(scrollBarMaskH)";
}
};
this.flashScrollBar=function(){
if(this.disableFlashScrollBar||!this.domNode){
return;
}
this._dim=this.getDim();
if(this._dim.d.h<=0){
return;
}
this.showScrollBar();
var _17c=this;
setTimeout(function(){
_17c.hideScrollBar();
},300);
};
this.addCover=function(){
if(!has("touch")&&!this.noCover){
if(!this._cover){
this._cover=_154.create("div",null,win.doc.body);
_155.set(this._cover,{backgroundColor:"#ffff00",opacity:0,position:"absolute",top:"0px",left:"0px",width:"100%",height:"100%",zIndex:2147483647});
this._ch.push(_151.connect(this._cover,has("touch")?"touchstart":"onmousedown",this,"onTouchEnd"));
}else{
this._cover.style.display="";
}
this.setSelectable(this._cover,false);
this.setSelectable(this.domNode,false);
}
};
this.removeCover=function(){
if(!has("touch")&&this._cover){
this._cover.style.display="none";
this.setSelectable(this._cover,true);
this.setSelectable(this.domNode,true);
}
};
this.setKeyframes=function(from,to,idx){
if(!dm._rule){
dm._rule=[];
}
if(!dm._rule[idx]){
var node=_154.create("style",null,win.doc.getElementsByTagName("head")[0]);
node.textContent=".mblScrollableScrollTo"+idx+"{-webkit-animation-name: scrollableViewScroll"+idx+";}"+"@-webkit-keyframes scrollableViewScroll"+idx+"{}";
dm._rule[idx]=node.sheet.cssRules[1];
}
var rule=dm._rule[idx];
if(rule){
if(from){
rule.deleteRule("from");
rule.insertRule("from { -webkit-transform: "+this.makeTranslateStr(from)+"; }");
}
if(to){
if(to.x===undefined){
to.x=from.x;
}
if(to.y===undefined){
to.y=from.y;
}
rule.deleteRule("to");
rule.insertRule("to { -webkit-transform: "+this.makeTranslateStr(to)+"; }");
}
}
};
this.setSelectable=function(node,_17d){
node.style.KhtmlUserSelect=_17d?"auto":"none";
node.style.MozUserSelect=_17d?"":"none";
node.onselectstart=_17d?null:function(){
return false;
};
if(has("ie")){
node.unselectable=_17d?"":"on";
var _17e=node.getElementsByTagName("*");
for(var i=0;i<_17e.length;i++){
_17e[i].unselectable=_17d?"":"on";
}
}
};
if(has("webkit")){
var elem=win.doc.createElement("div");
elem.style.webkitTransform="translate3d(0px,1px,0px)";
win.doc.documentElement.appendChild(elem);
var v=win.doc.defaultView.getComputedStyle(elem,"")["-webkit-transform"];
dm.hasTranslate3d=v&&v.indexOf("matrix")===0;
win.doc.documentElement.removeChild(elem);
}
};
dm.scrollable=_156;
return _156;
});
},"dijit/_Container":function(){
define("dijit/_Container",["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","./registry"],function(_17f,_180,_181,_182){
return _180("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_183,_184){
var _185=this.containerNode;
if(_184&&typeof _184=="number"){
var _186=this.getChildren();
if(_186&&_186.length>=_184){
_185=_186[_184-1].domNode;
_184="after";
}
}
_181.place(_183.domNode,_185,_184);
if(this._started&&!_183._started){
_183.startup();
}
},removeChild:function(_187){
if(typeof _187=="number"){
_187=this.getChildren()[_187];
}
if(_187){
var node=_187.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_188,dir){
var node=_188.domNode,_189=(dir>0?"nextSibling":"previousSibling");
do{
node=node[_189];
}while(node&&(node.nodeType!=1||!_182.byNode(node)));
return node&&_182.byNode(node);
},getIndexOfChild:function(_18a){
return _17f.indexOf(this.getChildren(),_18a);
}});
});
},"dojo/aspect":function(){
define([],function(){
"use strict";
function _18b(_18c,type,_18d,_18e){
var _18f=_18c[type];
var _190=type=="around";
var _191;
if(_190){
var _192=_18d(function(){
return _18f.advice(this,arguments);
});
_191={remove:function(){
_191.cancelled=true;
},advice:function(_193,args){
return _191.cancelled?_18f.advice(_193,args):_192.apply(_193,args);
}};
}else{
_191={remove:function(){
var _194=_191.previous;
var next=_191.next;
if(!next&&!_194){
delete _18c[type];
}else{
if(_194){
_194.next=next;
}else{
_18c[type]=next;
}
if(next){
next.previous=_194;
}
}
},advice:_18d,receiveArguments:_18e};
}
if(_18f&&!_190){
if(type=="after"){
var next=_18f;
while(next){
_18f=next;
next=next.next;
}
_18f.next=_191;
_191.previous=_18f;
}else{
if(type=="before"){
_18c[type]=_191;
_191.next=_18f;
_18f.previous=_191;
}
}
}else{
_18c[type]=_191;
}
return _191;
};
function _195(type){
return function(_196,_197,_198,_199){
var _19a=_196[_197],_19b;
if(!_19a||_19a.target!=_196){
_19b=_196[_197]=function(){
var args=arguments;
var _19c=_19b.before;
while(_19c){
args=_19c.advice.apply(this,args)||args;
_19c=_19c.next;
}
if(_19b.around){
var _19d=_19b.around.advice(this,args);
}
var _19e=_19b.after;
while(_19e){
_19d=_19e.receiveArguments?_19e.advice.apply(this,args)||_19d:_19e.advice.call(this,_19d);
_19e=_19e.next;
}
return _19d;
};
if(_19a){
_19b.around={advice:function(_19f,args){
return _19a.apply(_19f,args);
}};
}
_19b.target=_196;
}
var _1a0=_18b((_19b||_19a),type,_198,_199);
_198=null;
return _1a0;
};
};
return {before:_195("before"),around:_195("around"),after:_195("after")};
});
},"dojox/mobile/app/SceneController":function(){
define(["dijit","dojo","dojox","dojo/require!dojox/mobile/_base"],function(_1a1,dojo,_1a2){
dojo.provide("dojox.mobile.app.SceneController");
dojo.experimental("dojox.mobile.app.SceneController");
dojo.require("dojox.mobile._base");
(function(){
var app=_1a2.mobile.app;
var _1a3={};
dojo.declare("dojox.mobile.app.SceneController",_1a2.mobile.View,{stageController:null,keepScrollPos:false,init:function(_1a4,_1a5){
this.sceneName=_1a4;
this.params=_1a5;
var _1a6=app.resolveTemplate(_1a4);
this._deferredInit=new dojo.Deferred();
if(_1a3[_1a4]){
this._setContents(_1a3[_1a4]);
}else{
dojo.xhrGet({url:_1a6,handleAs:"text"}).addCallback(dojo.hitch(this,this._setContents));
}
return this._deferredInit;
},_setContents:function(_1a7){
_1a3[this.sceneName]=_1a7;
this.domNode.innerHTML="<div>"+_1a7+"</div>";
var _1a8="";
var _1a9=this.sceneName.split("-");
for(var i=0;i<_1a9.length;i++){
_1a8+=_1a9[i].substring(0,1).toUpperCase()+_1a9[i].substring(1);
}
_1a8+="Assistant";
this.sceneAssistantName=_1a8;
var _1aa=this;
_1a2.mobile.app.loadResourcesForScene(this.sceneName,function(){
console.log("All resources for ",_1aa.sceneName," loaded");
var _1ab;
if(typeof (dojo.global[_1a8])!="undefined"){
_1aa._initAssistant();
}else{
var _1ac=app.resolveAssistant(_1aa.sceneName);
dojo.xhrGet({url:_1ac,handleAs:"text"}).addCallback(function(text){
try{
dojo.eval(text);
}
catch(e){
console.log("Error initializing code for scene "+_1aa.sceneName+". Please check for syntax errors");
throw e;
}
_1aa._initAssistant();
});
}
});
},_initAssistant:function(){
console.log("Instantiating the scene assistant "+this.sceneAssistantName);
var cls=dojo.getObject(this.sceneAssistantName);
if(!cls){
throw Error("Unable to resolve scene assistant "+this.sceneAssistantName);
}
this.assistant=new cls(this.params);
this.assistant.controller=this;
this.assistant.domNode=this.domNode.firstChild;
this.assistant.setup();
this._deferredInit.callback();
},query:function(_1ad,node){
return dojo.query(_1ad,node||this.domNode);
},parse:function(node){
var _1ae=this._widgets=_1a2.mobile.parser.parse(node||this.domNode,{controller:this});
for(var i=0;i<_1ae.length;i++){
_1ae[i].set("controller",this);
}
},getWindowSize:function(){
return {w:dojo.global.innerWidth,h:dojo.global.innerHeight};
},showAlertDialog:function(_1af){
var size=dojo.marginBox(this.assistant.domNode);
var _1b0=new _1a2.mobile.app.AlertDialog(dojo.mixin(_1af,{controller:this}));
this.assistant.domNode.appendChild(_1b0.domNode);
console.log("Appended ",_1b0.domNode," to ",this.assistant.domNode);
_1b0.show();
},popupSubMenu:function(info){
var _1b1=new _1a2.mobile.app.ListSelector({controller:this,destroyOnHide:true,onChoose:info.onChoose});
this.assistant.domNode.appendChild(_1b1.domNode);
_1b1.set("data",info.choices);
_1b1.show(info.fromNode);
}});
})();
});
},"dojox/mobile/SpinWheelDatePicker":function(){
define("dojox/mobile/SpinWheelDatePicker",["dojo/_base/declare","dojo/dom-class","dojo/date","dojo/date/locale","./SpinWheel","./SpinWheelSlot"],function(_1b2,_1b3,_1b4,_1b5,_1b6,_1b7){
var _1b8=_1b2(_1b7,{buildRendering:function(){
this.labels=[];
if(this.labelFrom!==this.labelTo){
var dtA=new Date(this.labelFrom,0,1);
var i,idx;
for(i=this.labelFrom,idx=0;i<=this.labelTo;i++,idx++){
dtA.setFullYear(i);
this.labels.push(_1b5.format(dtA,{datePattern:"yyyy",selector:"date"}));
}
}
this.inherited(arguments);
}});
var _1b9=_1b2(_1b7,{buildRendering:function(){
this.labels=[];
var dtA=new Date(2000,0,1);
var _1ba;
for(var i=0;i<12;i++){
dtA.setMonth(i);
_1ba=_1b5.format(dtA,{datePattern:"MMM",selector:"date"});
this.labels.push(_1ba);
}
this.inherited(arguments);
}});
var _1bb=_1b2(_1b7,{});
return _1b2("dojox.mobile.SpinWheelDatePicker",_1b6,{slotClasses:[_1b8,_1b9,_1bb],slotProps:[{labelFrom:1970,labelTo:2038},{},{labelFrom:1,labelTo:31}],buildRendering:function(){
this.inherited(arguments);
_1b3.add(this.domNode,"mblSpinWheelDatePicker");
this.connect(this.slots[1],"onFlickAnimationEnd","onMonthSet");
this.connect(this.slots[2],"onFlickAnimationEnd","onDaySet");
},reset:function(){
var _1bc=this.slots;
var now=new Date();
var _1bd=_1b5.format(now,{datePattern:"MMM",selector:"date"});
this.setValue([now.getFullYear(),_1bd,now.getDate()]);
},onMonthSet:function(){
var _1be=this.onDaySet();
var _1bf={28:[29,30,31],29:[30,31],30:[31],31:[]};
this.slots[2].disableValues(_1bf[_1be]);
},onDaySet:function(){
var y=this.slots[0].getValue();
var m=this.slots[1].getValue();
var _1c0=_1b5.parse(y+"/"+m,{datePattern:"yyyy/MMM",selector:"date"});
var _1c1=_1b4.getDaysInMonth(_1c0);
var d=this.slots[2].getValue();
if(_1c1<d){
this.slots[2].setValue(_1c1);
}
return _1c1;
}});
});
},"dojo/cldr/supplemental":function(){
define(["../_base/kernel","../_base/lang","../i18n"],function(dojo,lang){
lang.getObject("cldr.supplemental",true,dojo);
dojo.cldr.supplemental.getFirstDayOfWeek=function(_1c2){
var _1c3={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,sy:6,tn:6,ye:6,ar:0,as:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,il:0,"in":0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mn:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,zw:0};
var _1c4=dojo.cldr.supplemental._region(_1c2);
var dow=_1c3[_1c4];
return (dow===undefined)?1:dow;
};
dojo.cldr.supplemental._region=function(_1c5){
_1c5=dojo.i18n.normalizeLocale(_1c5);
var tags=_1c5.split("-");
var _1c6=tags[1];
if(!_1c6){
_1c6={de:"de",en:"us",es:"es",fi:"fi",fr:"fr",he:"il",hu:"hu",it:"it",ja:"jp",ko:"kr",nl:"nl",pt:"br",sv:"se",zh:"cn"}[tags[0]];
}else{
if(_1c6.length==4){
_1c6=tags[2];
}
}
return _1c6;
};
dojo.cldr.supplemental.getWeekend=function(_1c7){
var _1c8={"in":0,af:4,dz:4,ir:4,om:4,sa:4,ye:4,ae:5,bh:5,eg:5,il:5,iq:5,jo:5,kw:5,ly:5,ma:5,qa:5,sd:5,sy:5,tn:5};
var _1c9={af:5,dz:5,ir:5,om:5,sa:5,ye:5,ae:6,bh:5,eg:6,il:6,iq:6,jo:6,kw:6,ly:6,ma:6,qa:6,sd:6,sy:6,tn:6};
var _1ca=dojo.cldr.supplemental._region(_1c7);
var _1cb=_1c8[_1ca];
var end=_1c9[_1ca];
if(_1cb===undefined){
_1cb=6;
}
if(end===undefined){
end=0;
}
return {start:_1cb,end:end};
};
return dojo.cldr.supplemental;
});
},"dojo/cldr/nls/gregorian":function(){
define({root:{"months-format-narrow":["1","2","3","4","5","6","7","8","9","10","11","12"],"quarters-standAlone-narrow":["1","2","3","4"],"field-weekday":"Day of the Week","dateFormatItem-yQQQ":"y QQQ","dateFormatItem-yMEd":"EEE, y-M-d","dateFormatItem-MMMEd":"E MMM d","eraNarrow":["BCE","CE"],"dateTimeFormats-appendItem-Day-Of-Week":"{0} {1}","dateFormat-long":"y MMMM d","months-format-wide":["1","2","3","4","5","6","7","8","9","10","11","12"],"dateTimeFormat-medium":"{1} {0}","dateFormatItem-EEEd":"d EEE","dayPeriods-format-wide-pm":"PM","dateFormat-full":"EEEE, y MMMM dd","dateFormatItem-Md":"M-d","dayPeriods-format-abbr-am":"AM","dateTimeFormats-appendItem-Second":"{0} ({2}: {1})","field-era":"Era","dateFormatItem-yM":"y-M","months-standAlone-wide":["1","2","3","4","5","6","7","8","9","10","11","12"],"timeFormat-short":"HH:mm","quarters-format-wide":["Q1","Q2","Q3","Q4"],"timeFormat-long":"HH:mm:ss z","field-year":"Year","dateFormatItem-yMMM":"y MMM","dateFormatItem-yQ":"y Q","dateTimeFormats-appendItem-Era":"{0} {1}","field-hour":"Hour","months-format-abbr":["1","2","3","4","5","6","7","8","9","10","11","12"],"timeFormat-full":"HH:mm:ss zzzz","dateTimeFormats-appendItem-Week":"{0} ({2}: {1})","field-day-relative+0":"Today","field-day-relative+1":"Tomorrow","dateFormatItem-H":"HH","months-standAlone-abbr":["1","2","3","4","5","6","7","8","9","10","11","12"],"quarters-format-abbr":["Q1","Q2","Q3","Q4"],"quarters-standAlone-wide":["Q1","Q2","Q3","Q4"],"dateFormatItem-M":"L","days-standAlone-wide":["1","2","3","4","5","6","7"],"timeFormat-medium":"HH:mm:ss","dateFormatItem-Hm":"HH:mm","quarters-standAlone-abbr":["Q1","Q2","Q3","Q4"],"eraAbbr":["BCE","CE"],"field-minute":"Minute","field-dayperiod":"Dayperiod","days-standAlone-abbr":["1","2","3","4","5","6","7"],"dateFormatItem-d":"d","dateFormatItem-ms":"mm:ss","quarters-format-narrow":["1","2","3","4"],"field-day-relative+-1":"Yesterday","dateFormatItem-h":"h a","dateTimeFormat-long":"{1} {0}","dayPeriods-format-narrow-am":"AM","dateFormatItem-MMMd":"MMM d","dateFormatItem-MEd":"E, M-d","dateTimeFormat-full":"{1} {0}","field-day":"Day","days-format-wide":["1","2","3","4","5","6","7"],"field-zone":"Zone","dateTimeFormats-appendItem-Day":"{0} ({2}: {1})","dateFormatItem-y":"y","months-standAlone-narrow":["1","2","3","4","5","6","7","8","9","10","11","12"],"dateFormatItem-hm":"h:mm a","dateTimeFormats-appendItem-Year":"{0} {1}","dateTimeFormats-appendItem-Hour":"{0} ({2}: {1})","dayPeriods-format-abbr-pm":"PM","days-format-abbr":["1","2","3","4","5","6","7"],"eraNames":["BCE","CE"],"days-format-narrow":["1","2","3","4","5","6","7"],"days-standAlone-narrow":["1","2","3","4","5","6","7"],"dateFormatItem-MMM":"LLL","field-month":"Month","dateTimeFormats-appendItem-Quarter":"{0} ({2}: {1})","dayPeriods-format-wide-am":"AM","dateTimeFormats-appendItem-Month":"{0} ({2}: {1})","dateTimeFormats-appendItem-Minute":"{0} ({2}: {1})","dateFormat-short":"yyyy-MM-dd","field-second":"Second","dateFormatItem-yMMMEd":"EEE, y MMM d","dateTimeFormats-appendItem-Timezone":"{0} {1}","field-week":"Week","dateFormat-medium":"y MMM d","dayPeriods-format-narrow-pm":"PM","dateTimeFormat-short":"{1} {0}","dateFormatItem-Hms":"HH:mm:ss","dateFormatItem-hms":"h:mm:ss a"},"ar":true,"ca":true,"cs":true,"da":true,"de":true,"el":true,"en":true,"en-au":true,"en-ca":true,"en-gb":true,"es":true,"fi":true,"fr":true,"fr-ch":true,"he":true,"hu":true,"it":true,"ja":true,"ko":true,"nb":true,"nl":true,"pl":true,"pt":true,"pt-pt":true,"ro":true,"ru":true,"sk":true,"sl":true,"sv":true,"th":true,"tr":true,"zh":true,"zh-hant":true,"zh-hk":true,"zh-tw":true});
},"dojox/mobile/app/_base":function(){
define(["dijit","dojo","dojox","dojo/require!dijit/_base,dijit/_WidgetBase,dojox/mobile,dojox/mobile/parser,dojox/mobile/Button,dojox/mobile/app/_event,dojox/mobile/app/_Widget,dojox/mobile/app/StageController,dojox/mobile/app/SceneController,dojox/mobile/app/SceneAssistant,dojox/mobile/app/AlertDialog,dojox/mobile/app/List,dojox/mobile/app/ListSelector,dojox/mobile/app/TextBox,dojox/mobile/app/ImageView,dojox/mobile/app/ImageThumbView"],function(_1cc,dojo,_1cd){
dojo.provide("dojox.mobile.app._base");
dojo.experimental("dojox.mobile.app._base");
dojo.require("dijit._base");
dojo.require("dijit._WidgetBase");
dojo.require("dojox.mobile");
dojo.require("dojox.mobile.parser");
dojo.require("dojox.mobile.Button");
dojo.require("dojox.mobile.app._event");
dojo.require("dojox.mobile.app._Widget");
dojo.require("dojox.mobile.app.StageController");
dojo.require("dojox.mobile.app.SceneController");
dojo.require("dojox.mobile.app.SceneAssistant");
dojo.require("dojox.mobile.app.AlertDialog");
dojo.require("dojox.mobile.app.List");
dojo.require("dojox.mobile.app.ListSelector");
dojo.require("dojox.mobile.app.TextBox");
dojo.require("dojox.mobile.app.ImageView");
dojo.require("dojox.mobile.app.ImageThumbView");
(function(){
var _1ce;
var _1cf;
var _1d0=["dojox.mobile","dojox.mobile.parser"];
var _1d1={};
var _1d2;
var _1d3;
var _1d4=[];
function _1d5(_1d6,_1d7){
var _1d8;
var url;
do{
_1d8=_1d6.pop();
if(_1d8.source){
url=_1d8.source;
}else{
if(_1d8.module){
url=dojo.moduleUrl(_1d8.module)+".js";
}else{
console.log("Error: invalid JavaScript resource "+dojo.toJson(_1d8));
return;
}
}
}while(_1d6.length>0&&_1d1[url]);
if(_1d6.length<1&&_1d1[url]){
_1d7();
return;
}
dojo.xhrGet({url:url,sync:false}).addCallbacks(function(text){
dojo["eval"](text);
_1d1[url]=true;
if(_1d6.length>0){
_1d5(_1d6,_1d7);
}else{
_1d7();
}
},function(){
console.log("Failed to load resource "+url);
});
};
var _1d9=function(){
_1ce=new _1cd.mobile.app.StageController(_1d3);
var _1da={id:"com.test.app",version:"1.0.0",initialScene:"main"};
if(dojo.global["appInfo"]){
dojo.mixin(_1da,dojo.global["appInfo"]);
}
_1cf=_1cd.mobile.app.info=_1da;
if(_1cf.title){
var _1db=dojo.query("head title")[0]||dojo.create("title",{},dojo.query("head")[0]);
document.title=_1cf.title;
}
_1ce.pushScene(_1cf.initialScene);
};
var _1dc=function(){
var _1dd=false;
if(dojo.global.BackButton){
BackButton.override();
dojo.connect(document,"backKeyDown",function(e){
dojo.publish("/dojox/mobile/app/goback");
});
_1dd=true;
}else{
if(dojo.global.Mojo){
}
}
if(_1dd){
dojo.addClass(dojo.body(),"mblNativeBack");
}
};
dojo.mixin(_1cd.mobile.app,{init:function(node){
_1d3=node||dojo.body();
_1cd.mobile.app.STAGE_CONTROLLER_ACTIVE=true;
dojo.subscribe("/dojox/mobile/app/goback",function(){
_1ce.popScene();
});
dojo.subscribe("/dojox/mobile/app/alert",function(_1de){
_1cd.mobile.app.getActiveSceneController().showAlertDialog(_1de);
});
dojo.subscribe("/dojox/mobile/app/pushScene",function(_1df,_1e0){
_1ce.pushScene(_1df,_1e0||{});
});
dojo.xhrGet({url:"view-resources.json",load:function(data){
var _1e1=[];
if(data){
_1d4=data=dojo.fromJson(data);
for(var i=0;i<data.length;i++){
if(!data[i].scene){
_1e1.push(data[i]);
}
}
}
if(_1e1.length>0){
_1d5(_1e1,_1d9);
}else{
_1d9();
}
},error:_1d9});
_1dc();
},getActiveSceneController:function(){
return _1ce.getActiveSceneController();
},getStageController:function(){
return _1ce;
},loadResources:function(_1e2,_1e3){
_1d5(_1e2,_1e3);
},loadResourcesForScene:function(_1e4,_1e5){
var _1e6=[];
for(var i=0;i<_1d4.length;i++){
if(_1d4[i].scene==_1e4){
_1e6.push(_1d4[i]);
}
}
if(_1e6.length>0){
_1d5(_1e6,_1e5);
}else{
_1e5();
}
},resolveTemplate:function(_1e7){
return "app/views/"+_1e7+"/"+_1e7+"-scene.html";
},resolveAssistant:function(_1e8){
return "app/assistants/"+_1e8+"-assistant.js";
}});
})();
});
},"dojo/topic":function(){
define(["./Evented"],function(_1e9){
var hub=new _1e9;
return {publish:function(_1ea,_1eb){
return hub.emit.apply(hub,arguments);
},subscribe:function(_1ec,_1ed){
return hub.on.apply(hub,arguments);
}};
});
},"dijit/_base/scroll":function(){
define("dijit/_base/scroll",["dojo/window",".."],function(_1ee,_1ef){
_1ef.scrollIntoView=function(node,pos){
_1ee.scrollIntoView(node,pos);
};
});
},"dojo/NodeList-dom":function(){
define(["./_base/kernel","./query","./_base/array","./_base/lang","./dom-class","./dom-construct","./dom-geometry","./dom-attr","./dom-style"],function(dojo,_1f0,_1f1,lang,_1f2,_1f3,_1f4,_1f5,_1f6){
var _1f7=function(a){
return a.length==1&&(typeof a[0]=="string");
};
var _1f8=function(node){
var p=node.parentNode;
if(p){
p.removeChild(node);
}
};
var _1f9=_1f0.NodeList,awc=_1f9._adaptWithCondition,aafe=_1f9._adaptAsForEach,aam=_1f9._adaptAsMap;
function _1fa(_1fb){
return function(node,name,_1fc){
if(arguments.length==2){
return _1fb[typeof name=="string"?"get":"set"](node,name);
}
return _1fb.set(node,name,_1fc);
};
};
lang.extend(_1f9,{_normalize:function(_1fd,_1fe){
var _1ff=_1fd.parse===true;
if(typeof _1fd.template=="string"){
var _200=_1fd.templateFunc||(dojo.string&&dojo.string.substitute);
_1fd=_200?_200(_1fd.template,_1fd):_1fd;
}
var type=(typeof _1fd);
if(type=="string"||type=="number"){
_1fd=_1f3.toDom(_1fd,(_1fe&&_1fe.ownerDocument));
if(_1fd.nodeType==11){
_1fd=lang._toArray(_1fd.childNodes);
}else{
_1fd=[_1fd];
}
}else{
if(!lang.isArrayLike(_1fd)){
_1fd=[_1fd];
}else{
if(!lang.isArray(_1fd)){
_1fd=lang._toArray(_1fd);
}
}
}
if(_1ff){
_1fd._runParse=true;
}
return _1fd;
},_cloneNode:function(node){
return node.cloneNode(true);
},_place:function(ary,_201,_202,_203){
if(_201.nodeType!=1&&_202=="only"){
return;
}
var _204=_201,_205;
var _206=ary.length;
for(var i=_206-1;i>=0;i--){
var node=(_203?this._cloneNode(ary[i]):ary[i]);
if(ary._runParse&&dojo.parser&&dojo.parser.parse){
if(!_205){
_205=_204.ownerDocument.createElement("div");
}
_205.appendChild(node);
dojo.parser.parse(_205);
node=_205.firstChild;
while(_205.firstChild){
_205.removeChild(_205.firstChild);
}
}
if(i==_206-1){
_1f3.place(node,_204,_202);
}else{
_204.parentNode.insertBefore(node,_204);
}
_204=node;
}
},attr:awc(_1fa(_1f5),_1f7),style:awc(_1fa(_1f6),_1f7),addClass:aafe(_1f2.add),removeClass:aafe(_1f2.remove),replaceClass:aafe(_1f2.replace),toggleClass:aafe(_1f2.toggle),empty:aafe(_1f3.empty),removeAttr:aafe(_1f5.remove),position:aam(_1f4.position),marginBox:aam(_1f4.getMarginBox),place:function(_207,_208){
var item=_1f0(_207)[0];
return this.forEach(function(node){
_1f3.place(node,item,_208);
});
},orphan:function(_209){
return (_209?_1f0._filterResult(this,_209):this).forEach(_1f8);
},adopt:function(_20a,_20b){
return _1f0(_20a).place(this[0],_20b)._stash(this);
},query:function(_20c){
if(!_20c){
return this;
}
var ret=new _1f9;
this.map(function(node){
_1f0(_20c,node).forEach(function(_20d){
if(_20d!==undefined){
ret.push(_20d);
}
});
});
return ret._stash(this);
},filter:function(_20e){
var a=arguments,_20f=this,_210=0;
if(typeof _20e=="string"){
_20f=_1f0._filterResult(this,a[0]);
if(a.length==1){
return _20f._stash(this);
}
_210=1;
}
return this._wrap(_1f1.filter(_20f,a[_210],a[_210+1]),this);
},addContent:function(_211,_212){
_211=this._normalize(_211,this[0]);
for(var i=0,node;(node=this[i]);i++){
this._place(_211,node,_212,i>0);
}
return this;
}});
return _1f9;
});
},"dojox/mobile/SwapView":function(){
define("dojox/mobile/SwapView",["dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-class","dijit/registry","./View","./_ScrollableMixin"],function(_213,_214,_215,dom,_216,_217,View,_218){
return _215("dojox.mobile.SwapView",[View,_218],{scrollDir:"f",weight:1.2,buildRendering:function(){
this.inherited(arguments);
_216.add(this.domNode,"mblSwapView");
this.setSelectable(this.domNode,false);
this.containerNode=this.domNode;
_214.subscribe("/dojox/mobile/nextPage",this,"handleNextPage");
_214.subscribe("/dojox/mobile/prevPage",this,"handlePrevPage");
this.findAppBars();
},resize:function(){
this.inherited(arguments);
_213.forEach(this.getChildren(),function(_219){
if(_219.resize){
_219.resize();
}
});
},onTouchStart:function(e){
var _21a=this.domNode.offsetTop;
var _21b=this.nextView(this.domNode);
if(_21b){
_21b.stopAnimation();
_216.add(_21b.domNode,"mblIn");
_21b.containerNode.style.paddingTop=_21a+"px";
}
var _21c=this.previousView(this.domNode);
if(_21c){
_21c.stopAnimation();
_216.add(_21c.domNode,"mblIn");
_21c.containerNode.style.paddingTop=_21a+"px";
}
this.inherited(arguments);
},handleNextPage:function(w){
var _21d=w.refId&&dom.byId(w.refId)||w.domNode;
if(this.domNode.parentNode!==_21d.parentNode){
return;
}
if(this.getShowingView()!==this){
return;
}
this.goTo(1);
},handlePrevPage:function(w){
var _21e=w.refId&&dom.byId(w.refId)||w.domNode;
if(this.domNode.parentNode!==_21e.parentNode){
return;
}
if(this.getShowingView()!==this){
return;
}
this.goTo(-1);
},goTo:function(dir){
var w=this.domNode.offsetWidth;
var view=(dir==1)?this.nextView(this.domNode):this.previousView(this.domNode);
if(!view){
return;
}
view._beingFlipped=true;
view.scrollTo({x:w*dir});
view._beingFlipped=false;
view.domNode.style.display="";
_216.add(view.domNode,"mblIn");
this.slideTo({x:0},0.5,"ease-out",{x:-w*dir});
},isSwapView:function(node){
return (node&&node.nodeType===1&&_216.contains(node,"mblSwapView"));
},nextView:function(node){
for(var n=node.nextSibling;n;n=n.nextSibling){
if(this.isSwapView(n)){
return _217.byNode(n);
}
}
return null;
},previousView:function(node){
for(var n=node.previousSibling;n;n=n.previousSibling){
if(this.isSwapView(n)){
return _217.byNode(n);
}
}
return null;
},scrollTo:function(to){
if(!this._beingFlipped){
var _21f,x;
if(to.x<0){
_21f=this.nextView(this.domNode);
x=to.x+this.domNode.offsetWidth;
}else{
_21f=this.previousView(this.domNode);
x=to.x-this.domNode.offsetWidth;
}
if(_21f){
_21f.domNode.style.display="";
_21f._beingFlipped=true;
_21f.scrollTo({x:x});
_21f._beingFlipped=false;
}
}
this.inherited(arguments);
},slideTo:function(to,_220,_221,_222){
if(!this._beingFlipped){
var w=this.domNode.offsetWidth;
var pos=_222||this.getPos();
var _223,newX;
if(pos.x<0){
_223=this.nextView(this.domNode);
if(pos.x<-w/4){
if(_223){
to.x=-w;
newX=0;
}
}else{
if(_223){
newX=w;
}
}
}else{
_223=this.previousView(this.domNode);
if(pos.x>w/4){
if(_223){
to.x=w;
newX=0;
}
}else{
if(_223){
newX=-w;
}
}
}
if(_223){
_223._beingFlipped=true;
_223.slideTo({x:newX},_220,_221);
_223._beingFlipped=false;
if(newX===0){
dojox.mobile.currentView=_223;
}
_223.domNode._isShowing=(_223&&newX===0);
}
this.domNode._isShowing=!(_223&&newX===0);
}
this.inherited(arguments);
},onFlickAnimationEnd:function(e){
if(e&&e.animationName&&e.animationName!=="scrollableViewScroll2"){
return;
}
var _224=this.domNode.parentNode.childNodes;
for(var i=0;i<_224.length;i++){
var c=_224[i];
if(this.isSwapView(c)){
_216.remove(c,"mblIn");
if(!c._isShowing){
c.style.display="none";
}
}
}
this.inherited(arguments);
if(this.getShowingView()===this){
_214.publish("/dojox/mobile/viewChanged",[this]);
this.containerNode.style.paddingTop="";
}
}});
});
},"dojox/mobile/SpinWheelTimePicker":function(){
define("dojox/mobile/SpinWheelTimePicker",["dojo/_base/declare","dojo/dom-class","./SpinWheel","./SpinWheelSlot"],function(_225,_226,_227,_228){
return _225("dojox.mobile.SpinWheelTimePicker",_227,{slotClasses:[_228,_228],slotProps:[{labelFrom:0,labelTo:23},{labels:["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"]}],buildRendering:function(){
this.inherited(arguments);
_226.add(this.domNode,"mblSpinWheelTimePicker");
},reset:function(){
var _229=this.slots;
var now=new Date();
var _22a=now.getHours()+"";
_229[0].setValue(_22a);
_229[0].setColor(_22a);
var m=now.getMinutes();
var _22b=(m<10?"0":"")+m;
_229[1].setValue(_22b);
_229[1].setColor(_22b);
}});
});
},"dojo/_base/connect":function(){
define(["./kernel","../on","../topic","../aspect","./event","../mouse","./sniff","./lang","../keys"],function(_22c,on,hub,_22d,_22e,_22f,has,lang){
has.add("events-keypress-typed",function(){
var _230={charCode:0};
try{
_230=document.createEvent("KeyboardEvent");
(_230.initKeyboardEvent||_230.initKeyEvent).call(_230,"keypress",true,true,null,false,false,false,false,9,3);
}
catch(e){
}
return _230.charCode==0&&!has("opera");
});
function _231(obj,_232,_233,_234,_235){
_234=lang.hitch(_233,_234);
if(!obj||!(obj.addEventListener||obj.attachEvent)){
return _22d.after(obj||_22c.global,_232,_234,true);
}
if(typeof _232=="string"&&_232.substring(0,2)=="on"){
_232=_232.substring(2);
}
if(!obj){
obj=_22c.global;
}
if(!_235){
switch(_232){
case "keypress":
_232=_236;
break;
case "mouseenter":
_232=_22f.enter;
break;
case "mouseleave":
_232=_22f.leave;
break;
}
}
return on(obj,_232,_234,_235);
};
var _237={106:42,111:47,186:59,187:43,188:44,189:45,190:46,191:47,192:96,219:91,220:92,221:93,222:39,229:113};
var _238=has("mac")?"metaKey":"ctrlKey";
var _239=function(evt,_23a){
var faux=lang.mixin({},evt,_23a);
_23b(faux);
faux.preventDefault=function(){
evt.preventDefault();
};
faux.stopPropagation=function(){
evt.stopPropagation();
};
return faux;
};
function _23b(evt){
evt.keyChar=evt.charCode?String.fromCharCode(evt.charCode):"";
evt.charOrCode=evt.keyChar||evt.keyCode;
};
var _236;
if(has("events-keypress-typed")){
var _23c=function(e,code){
try{
return (e.keyCode=code);
}
catch(e){
return 0;
}
};
_236=function(_23d,_23e){
var _23f=on(_23d,"keydown",function(evt){
var k=evt.keyCode;
var _240=(k!=13||(has("ie")>=9&&!has("quirks")))&&k!=32&&(k!=27||!has("ie"))&&(k<48||k>90)&&(k<96||k>111)&&(k<186||k>192)&&(k<219||k>222)&&k!=229;
if(_240||evt.ctrlKey){
var c=_240?0:k;
if(evt.ctrlKey){
if(k==3||k==13){
return _23e.call(evt.currentTarget,evt);
}else{
if(c>95&&c<106){
c-=48;
}else{
if((!evt.shiftKey)&&(c>=65&&c<=90)){
c+=32;
}else{
c=_237[c]||c;
}
}
}
}
var faux=_239(evt,{type:"keypress",faux:true,charCode:c});
_23e.call(evt.currentTarget,faux);
if(has("ie")){
_23c(evt,faux.keyCode);
}
}
});
var _241=on(_23d,"keypress",function(evt){
var c=evt.charCode;
c=c>=32?c:0;
evt=_239(evt,{charCode:c,faux:true});
return _23e.call(this,evt);
});
return {remove:function(){
_23f.remove();
_241.remove();
}};
};
}else{
if(has("opera")){
_236=function(_242,_243){
return on(_242,"keypress",function(evt){
var c=evt.which;
if(c==3){
c=99;
}
c=c<32&&!evt.shiftKey?0:c;
if(evt.ctrlKey&&!evt.shiftKey&&c>=65&&c<=90){
c+=32;
}
return _243.call(this,_239(evt,{charCode:c}));
});
};
}else{
_236=function(_244,_245){
return on(_244,"keypress",function(evt){
_23b(evt);
return _245.call(this,evt);
});
};
}
}
var _246={_keypress:_236,connect:function(obj,_247,_248,_249,_24a){
var a=arguments,args=[],i=0;
args.push(typeof a[0]=="string"?null:a[i++],a[i++]);
var a1=a[i+1];
args.push(typeof a1=="string"||typeof a1=="function"?a[i++]:null,a[i++]);
for(var l=a.length;i<l;i++){
args.push(a[i]);
}
return _231.apply(this,args);
},disconnect:function(_24b){
if(_24b){
_24b.remove();
}
},subscribe:function(_24c,_24d,_24e){
return hub.subscribe(_24c,lang.hitch(_24d,_24e));
},publish:function(_24f,args){
return hub.publish.apply(hub,[_24f].concat(args));
},connectPublisher:function(_250,obj,_251){
var pf=function(){
_246.publish(_250,arguments);
};
return _251?_246.connect(obj,_251,pf):_246.connect(obj,pf);
},isCopyKey:function(e){
return e[_238];
}};
_246.unsubscribe=_246.disconnect;
1&&lang.mixin(_22c,_246);
return _246;
});
},"dojo/_base/fx":function(){
define(["./kernel","./lang","../Evented","./Color","./connect","./sniff","../dom","../dom-style"],function(dojo,lang,_252,_253,_254,has,dom,_255){
var _256=lang.mixin;
dojo._Line=function(_257,end){
this.start=_257;
this.end=end;
};
dojo._Line.prototype.getValue=function(n){
return ((this.end-this.start)*n)+this.start;
};
dojo.Animation=function(args){
_256(this,args);
if(lang.isArray(this.curve)){
this.curve=new dojo._Line(this.curve[0],this.curve[1]);
}
};
dojo.Animation.prototype=new _252();
dojo._Animation=dojo.Animation;
lang.extend(dojo.Animation,{duration:350,repeat:0,rate:20,_percent:0,_startRepeatCount:0,_getStep:function(){
var _258=this._percent,_259=this.easing;
return _259?_259(_258):_258;
},_fire:function(evt,args){
var a=args||[];
if(this[evt]){
if(dojo.config.debugAtAllCosts){
this[evt].apply(this,a);
}else{
try{
this[evt].apply(this,a);
}
catch(e){
console.error("exception in animation handler for:",evt);
console.error(e);
}
}
}
return this;
},play:function(_25a,_25b){
var _25c=this;
if(_25c._delayTimer){
_25c._clearTimer();
}
if(_25b){
_25c._stopTimer();
_25c._active=_25c._paused=false;
_25c._percent=0;
}else{
if(_25c._active&&!_25c._paused){
return _25c;
}
}
_25c._fire("beforeBegin",[_25c.node]);
var de=_25a||_25c.delay,_25d=lang.hitch(_25c,"_play",_25b);
if(de>0){
_25c._delayTimer=setTimeout(_25d,de);
return _25c;
}
_25d();
return _25c;
},_play:function(_25e){
var _25f=this;
if(_25f._delayTimer){
_25f._clearTimer();
}
_25f._startTime=new Date().valueOf();
if(_25f._paused){
_25f._startTime-=_25f.duration*_25f._percent;
}
_25f._active=true;
_25f._paused=false;
var _260=_25f.curve.getValue(_25f._getStep());
if(!_25f._percent){
if(!_25f._startRepeatCount){
_25f._startRepeatCount=_25f.repeat;
}
_25f._fire("onBegin",[_260]);
}
_25f._fire("onPlay",[_260]);
_25f._cycle();
return _25f;
},pause:function(){
var _261=this;
if(_261._delayTimer){
_261._clearTimer();
}
_261._stopTimer();
if(!_261._active){
return _261;
}
_261._paused=true;
_261._fire("onPause",[_261.curve.getValue(_261._getStep())]);
return _261;
},gotoPercent:function(_262,_263){
var _264=this;
_264._stopTimer();
_264._active=_264._paused=true;
_264._percent=_262;
if(_263){
_264.play();
}
return _264;
},stop:function(_265){
var _266=this;
if(_266._delayTimer){
_266._clearTimer();
}
if(!_266._timer){
return _266;
}
_266._stopTimer();
if(_265){
_266._percent=1;
}
_266._fire("onStop",[_266.curve.getValue(_266._getStep())]);
_266._active=_266._paused=false;
return _266;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}
return "stopped";
},_cycle:function(){
var _267=this;
if(_267._active){
var curr=new Date().valueOf();
var step=(curr-_267._startTime)/(_267.duration);
if(step>=1){
step=1;
}
_267._percent=step;
if(_267.easing){
step=_267.easing(step);
}
_267._fire("onAnimate",[_267.curve.getValue(step)]);
if(_267._percent<1){
_267._startTimer();
}else{
_267._active=false;
if(_267.repeat>0){
_267.repeat--;
_267.play(null,true);
}else{
if(_267.repeat==-1){
_267.play(null,true);
}else{
if(_267._startRepeatCount){
_267.repeat=_267._startRepeatCount;
_267._startRepeatCount=0;
}
}
}
_267._percent=0;
_267._fire("onEnd",[_267.node]);
!_267.repeat&&_267._stopTimer();
}
}
return _267;
},_clearTimer:function(){
clearTimeout(this._delayTimer);
delete this._delayTimer;
}});
var ctr=0,_268=null,_269={run:function(){
}};
lang.extend(dojo.Animation,{_startTimer:function(){
if(!this._timer){
this._timer=_254.connect(_269,"run",this,"_cycle");
ctr++;
}
if(!_268){
_268=setInterval(lang.hitch(_269,"run"),this.rate);
}
},_stopTimer:function(){
if(this._timer){
_254.disconnect(this._timer);
this._timer=null;
ctr--;
}
if(ctr<=0){
clearInterval(_268);
_268=null;
ctr=0;
}
}});
var _26a=has("ie")?function(node){
var ns=node.style;
if(!ns.width.length&&_255.get(node,"width")=="auto"){
ns.width="auto";
}
}:function(){
};
dojo._fade=function(args){
args.node=dom.byId(args.node);
var _26b=_256({properties:{}},args),_26c=(_26b.properties.opacity={});
_26c.start=!("start" in _26b)?function(){
return +_255.get(_26b.node,"opacity")||0;
}:_26b.start;
_26c.end=_26b.end;
var anim=dojo.animateProperty(_26b);
_254.connect(anim,"beforeBegin",lang.partial(_26a,_26b.node));
return anim;
};
dojo.fadeIn=function(args){
return dojo._fade(_256({end:1},args));
};
dojo.fadeOut=function(args){
return dojo._fade(_256({end:0},args));
};
dojo._defaultEasing=function(n){
return 0.5+((Math.sin((n+1.5)*Math.PI))/2);
};
var _26d=function(_26e){
this._properties=_26e;
for(var p in _26e){
var prop=_26e[p];
if(prop.start instanceof _253){
prop.tempColor=new _253();
}
}
};
_26d.prototype.getValue=function(r){
var ret={};
for(var p in this._properties){
var prop=this._properties[p],_26f=prop.start;
if(_26f instanceof _253){
ret[p]=_253.blendColors(_26f,prop.end,r,prop.tempColor).toCss();
}else{
if(!lang.isArray(_26f)){
ret[p]=((prop.end-_26f)*r)+_26f+(p!="opacity"?prop.units||"px":0);
}
}
}
return ret;
};
dojo.animateProperty=function(args){
var n=args.node=dom.byId(args.node);
if(!args.easing){
args.easing=dojo._defaultEasing;
}
var anim=new dojo.Animation(args);
_254.connect(anim,"beforeBegin",anim,function(){
var pm={};
for(var p in this.properties){
if(p=="width"||p=="height"){
this.node.display="block";
}
var prop=this.properties[p];
if(lang.isFunction(prop)){
prop=prop(n);
}
prop=pm[p]=_256({},(lang.isObject(prop)?prop:{end:prop}));
if(lang.isFunction(prop.start)){
prop.start=prop.start(n);
}
if(lang.isFunction(prop.end)){
prop.end=prop.end(n);
}
var _270=(p.toLowerCase().indexOf("color")>=0);
function _271(node,p){
var v={height:node.offsetHeight,width:node.offsetWidth}[p];
if(v!==undefined){
return v;
}
v=_255.get(node,p);
return (p=="opacity")?+v:(_270?v:parseFloat(v));
};
if(!("end" in prop)){
prop.end=_271(n,p);
}else{
if(!("start" in prop)){
prop.start=_271(n,p);
}
}
if(_270){
prop.start=new _253(prop.start);
prop.end=new _253(prop.end);
}else{
prop.start=(p=="opacity")?+prop.start:parseFloat(prop.start);
}
}
this.curve=new _26d(pm);
});
_254.connect(anim,"onAnimate",lang.hitch(_255,"set",anim.node));
return anim;
};
dojo.anim=function(node,_272,_273,_274,_275,_276){
return dojo.animateProperty({node:node,duration:_273||dojo.Animation.prototype.duration,properties:_272,easing:_274,onEnd:_275}).play(_276||0);
};
return {_Line:dojo._Line,Animation:dojo.Animation,_fade:dojo._fade,fadeIn:dojo.fadeIn,fadeOut:dojo.fadeOut,_defaultEasing:dojo._defaultEasing,animateProperty:dojo.animateProperty,anim:dojo.anim};
});
},"dojox/mobile/ScrollableView":function(){
define("dojox/mobile/ScrollableView",["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dijit/registry","./View","./_ScrollableMixin"],function(_277,_278,_279,_27a,_27b,View,_27c){
return _278("dojox.mobile.ScrollableView",[View,_27c],{scrollableParams:null,keepScrollPos:false,constructor:function(){
this.scrollableParams={noResize:true};
},buildRendering:function(){
this.inherited(arguments);
_279.add(this.domNode,"mblScrollableView");
this.domNode.style.overflow="hidden";
this.domNode.style.top="0px";
this.containerNode=_27a.create("DIV",{className:"mblScrollableViewContainer"},this.domNode);
this.containerNode.style.position="absolute";
this.containerNode.style.top="0px";
if(this.scrollDir==="v"){
this.containerNode.style.width="100%";
}
this.reparent();
this.findAppBars();
},resize:function(){
this.inherited(arguments);
_277.forEach(this.getChildren(),function(_27d){
if(_27d.resize){
_27d.resize();
}
});
},isTopLevel:function(e){
var _27e=this.getParent&&this.getParent();
return (!_27e||!_27e.resize);
},addChild:function(_27f,_280){
var c=_27f.domNode;
var _281=this.checkFixedBar(c,true);
if(_281){
this.domNode.appendChild(c);
if(_281==="top"){
this.fixedHeaderHeight=c.offsetHeight;
this.isLocalHeader=true;
}else{
if(_281==="bottom"){
this.fixedFooterHeight=c.offsetHeight;
this.isLocalFooter=true;
c.style.bottom="0px";
}
}
this.resize();
if(this._started&&!_27f._started){
_27f.startup();
}
}else{
this.inherited(arguments);
}
},reparent:function(){
var i,idx,len,c;
for(i=0,idx=0,len=this.domNode.childNodes.length;i<len;i++){
c=this.domNode.childNodes[idx];
if(c===this.containerNode||this.checkFixedBar(c,true)){
idx++;
continue;
}
this.containerNode.appendChild(this.domNode.removeChild(c));
}
},onAfterTransitionIn:function(_282,dir,_283,_284,_285){
this.flashScrollBar();
},getChildren:function(){
var _286=this.inherited(arguments);
if(this.fixedHeader&&this.fixedHeader.parentNode===this.domNode){
_286.push(_27b.byNode(this.fixedHeader));
}
if(this.fixedFooter&&this.fixedFooter.parentNode===this.domNode){
_286.push(_27b.byNode(this.fixedFooter));
}
return _286;
}});
});
},"dojo/_base/config":function(){
define(["../has","require"],function(has,_287){
var _288={};
if(1){
var src=_287.rawConfig,p;
for(p in src){
_288[p]=src[p];
}
}else{
var _289=function(_28a,_28b,_28c){
for(p in _28a){
p!="has"&&has.add(_28b+p,_28a[p],0,_28c);
}
};
_288=1?_287.rawConfig:this.dojoConfig||this.djConfig||{};
_289(_288,"config",1);
_289(_288.has,"",1);
}
return _288;
});
},"dojo/_base/unload":function(){
define(["./kernel","./connect"],function(dojo,_28d){
var win=window;
dojo.addOnWindowUnload=function(obj,_28e){
if(!dojo.windowUnloaded){
_28d.connect(win,"unload",(dojo.windowUnloaded=function(){
}));
}
_28d.connect(win,"unload",obj,_28e);
};
dojo.addOnUnload=function(obj,_28f){
_28d.connect(win,"beforeunload",obj,_28f);
};
return {addOnWindowUnload:dojo.addOnWindowUnload,addOnUnload:dojo.addOnUnload};
});
},"dojo/selector/_loader":function(){
define(["../has","require"],function(has,_290){
"use strict";
var _291=document.createElement("div");
has.add("dom-qsa2.1",!!_291.querySelectorAll);
has.add("dom-qsa3",function(){
try{
_291.innerHTML="<p class='TEST'></p>";
return _291.querySelectorAll(".TEST:empty").length==1;
}
catch(e){
}
});
var _292;
var acme="./acme",lite="./lite";
return {load:function(id,_293,_294,_295){
var req=_290;
id=id=="default"?has("config-selectorEngine")||"css3":id;
id=id=="css2"||id=="lite"?lite:id=="css2.1"?has("dom-qsa2.1")?lite:acme:id=="css3"?has("dom-qsa3")?lite:acme:id=="acme"?acme:(req=_293)&&id;
if(id.charAt(id.length-1)=="?"){
id=id.substring(0,id.length-1);
var _296=true;
}
if(_296&&(has("dom-compliant-qsa")||_292)){
return _294(_292);
}
req([id],function(_297){
if(id!="./lite"){
_292=_297;
}
_294(_297);
});
}};
});
},"dojo/_base/declare":function(){
define(["./kernel","../has","./lang"],function(dojo,has,lang){
var mix=lang.mixin,op=Object.prototype,opts=op.toString,xtor=new Function,_298=0,_299="constructor";
function err(msg,cls){
throw new Error("declare"+(cls?" "+cls:"")+": "+msg);
};
function _29a(_29b,_29c){
var _29d=[],_29e=[{cls:0,refs:[]}],_29f={},_2a0=1,l=_29b.length,i=0,j,lin,base,top,_2a1,rec,name,refs;
for(;i<l;++i){
base=_29b[i];
if(!base){
err("mixin #"+i+" is unknown. Did you use dojo.require to pull it in?",_29c);
}else{
if(opts.call(base)!="[object Function]"){
err("mixin #"+i+" is not a callable constructor.",_29c);
}
}
lin=base._meta?base._meta.bases:[base];
top=0;
for(j=lin.length-1;j>=0;--j){
_2a1=lin[j].prototype;
if(!_2a1.hasOwnProperty("declaredClass")){
_2a1.declaredClass="uniqName_"+(_298++);
}
name=_2a1.declaredClass;
if(!_29f.hasOwnProperty(name)){
_29f[name]={count:0,refs:[],cls:lin[j]};
++_2a0;
}
rec=_29f[name];
if(top&&top!==rec){
rec.refs.push(top);
++top.count;
}
top=rec;
}
++top.count;
_29e[0].refs.push(top);
}
while(_29e.length){
top=_29e.pop();
_29d.push(top.cls);
--_2a0;
while(refs=top.refs,refs.length==1){
top=refs[0];
if(!top||--top.count){
top=0;
break;
}
_29d.push(top.cls);
--_2a0;
}
if(top){
for(i=0,l=refs.length;i<l;++i){
top=refs[i];
if(!--top.count){
_29e.push(top);
}
}
}
}
if(_2a0){
err("can't build consistent linearization",_29c);
}
base=_29b[0];
_29d[0]=base?base._meta&&base===_29d[_29d.length-base._meta.bases.length]?base._meta.bases.length:1:0;
return _29d;
};
function _2a2(args,a,f){
var name,_2a3,_2a4,_2a5,meta,base,_2a6,opf,pos,_2a7=this._inherited=this._inherited||{};
if(typeof args=="string"){
name=args;
args=a;
a=f;
}
f=0;
_2a5=args.callee;
name=name||_2a5.nom;
if(!name){
err("can't deduce a name to call inherited()",this.declaredClass);
}
meta=this.constructor._meta;
_2a4=meta.bases;
pos=_2a7.p;
if(name!=_299){
if(_2a7.c!==_2a5){
pos=0;
base=_2a4[0];
meta=base._meta;
if(meta.hidden[name]!==_2a5){
_2a3=meta.chains;
if(_2a3&&typeof _2a3[name]=="string"){
err("calling chained method with inherited: "+name,this.declaredClass);
}
do{
meta=base._meta;
_2a6=base.prototype;
if(meta&&(_2a6[name]===_2a5&&_2a6.hasOwnProperty(name)||meta.hidden[name]===_2a5)){
break;
}
}while(base=_2a4[++pos]);
pos=base?pos:-1;
}
}
base=_2a4[++pos];
if(base){
_2a6=base.prototype;
if(base._meta&&_2a6.hasOwnProperty(name)){
f=_2a6[name];
}else{
opf=op[name];
do{
_2a6=base.prototype;
f=_2a6[name];
if(f&&(base._meta?_2a6.hasOwnProperty(name):f!==opf)){
break;
}
}while(base=_2a4[++pos]);
}
}
f=base&&f||op[name];
}else{
if(_2a7.c!==_2a5){
pos=0;
meta=_2a4[0]._meta;
if(meta&&meta.ctor!==_2a5){
_2a3=meta.chains;
if(!_2a3||_2a3.constructor!=="manual"){
err("calling chained constructor with inherited",this.declaredClass);
}
while(base=_2a4[++pos]){
meta=base._meta;
if(meta&&meta.ctor===_2a5){
break;
}
}
pos=base?pos:-1;
}
}
while(base=_2a4[++pos]){
meta=base._meta;
f=meta?meta.ctor:base;
if(f){
break;
}
}
f=base&&f;
}
_2a7.c=f;
_2a7.p=pos;
if(f){
return a===true?f:f.apply(this,a||args);
}
};
function _2a8(name,args){
if(typeof name=="string"){
return this.__inherited(name,args,true);
}
return this.__inherited(name,true);
};
function _2a9(args,a1,a2){
var f=this.getInherited(args,a1);
if(f){
return f.apply(this,a2||a1||args);
}
};
var _2aa=dojo.config.isDebug?_2a9:_2a2;
function _2ab(cls){
var _2ac=this.constructor._meta.bases;
for(var i=0,l=_2ac.length;i<l;++i){
if(_2ac[i]===cls){
return true;
}
}
return this instanceof cls;
};
function _2ad(_2ae,_2af){
for(var name in _2af){
if(name!=_299&&_2af.hasOwnProperty(name)){
_2ae[name]=_2af[name];
}
}
if(has("bug-for-in-skips-shadowed")){
for(var _2b0=lang._extraNames,i=_2b0.length;i;){
name=_2b0[--i];
if(name!=_299&&_2af.hasOwnProperty(name)){
_2ae[name]=_2af[name];
}
}
}
};
function _2b1(_2b2,_2b3){
var name,t;
for(name in _2b3){
t=_2b3[name];
if((t!==op[name]||!(name in op))&&name!=_299){
if(opts.call(t)=="[object Function]"){
t.nom=name;
}
_2b2[name]=t;
}
}
if(has("bug-for-in-skips-shadowed")){
for(var _2b4=lang._extraNames,i=_2b4.length;i;){
name=_2b4[--i];
t=_2b3[name];
if((t!==op[name]||!(name in op))&&name!=_299){
if(opts.call(t)=="[object Function]"){
t.nom=name;
}
_2b2[name]=t;
}
}
}
return _2b2;
};
function _2b5(_2b6){
_2b7.safeMixin(this.prototype,_2b6);
return this;
};
function _2b8(_2b9,_2ba){
return function(){
var a=arguments,args=a,a0=a[0],f,i,m,l=_2b9.length,_2bb;
if(!(this instanceof a.callee)){
return _2bc(a);
}
if(_2ba&&(a0&&a0.preamble||this.preamble)){
_2bb=new Array(_2b9.length);
_2bb[0]=a;
for(i=0;;){
a0=a[0];
if(a0){
f=a0.preamble;
if(f){
a=f.apply(this,a)||a;
}
}
f=_2b9[i].prototype;
f=f.hasOwnProperty("preamble")&&f.preamble;
if(f){
a=f.apply(this,a)||a;
}
if(++i==l){
break;
}
_2bb[i]=a;
}
}
for(i=l-1;i>=0;--i){
f=_2b9[i];
m=f._meta;
f=m?m.ctor:f;
if(f){
f.apply(this,_2bb?_2bb[i]:a);
}
}
f=this.postscript;
if(f){
f.apply(this,args);
}
};
};
function _2bd(ctor,_2be){
return function(){
var a=arguments,t=a,a0=a[0],f;
if(!(this instanceof a.callee)){
return _2bc(a);
}
if(_2be){
if(a0){
f=a0.preamble;
if(f){
t=f.apply(this,t)||t;
}
}
f=this.preamble;
if(f){
f.apply(this,t);
}
}
if(ctor){
ctor.apply(this,a);
}
f=this.postscript;
if(f){
f.apply(this,a);
}
};
};
function _2bf(_2c0){
return function(){
var a=arguments,i=0,f,m;
if(!(this instanceof a.callee)){
return _2bc(a);
}
for(;f=_2c0[i];++i){
m=f._meta;
f=m?m.ctor:f;
if(f){
f.apply(this,a);
break;
}
}
f=this.postscript;
if(f){
f.apply(this,a);
}
};
};
function _2c1(name,_2c2,_2c3){
return function(){
var b,m,f,i=0,step=1;
if(_2c3){
i=_2c2.length-1;
step=-1;
}
for(;b=_2c2[i];i+=step){
m=b._meta;
f=(m?m.hidden:b.prototype)[name];
if(f){
f.apply(this,arguments);
}
}
};
};
function _2c4(ctor){
xtor.prototype=ctor.prototype;
var t=new xtor;
xtor.prototype=null;
return t;
};
function _2bc(args){
var ctor=args.callee,t=_2c4(ctor);
ctor.apply(t,args);
return t;
};
function _2b7(_2c5,_2c6,_2c7){
if(typeof _2c5!="string"){
_2c7=_2c6;
_2c6=_2c5;
_2c5="";
}
_2c7=_2c7||{};
var _2c8,i,t,ctor,name,_2c9,_2ca,_2cb=1,_2cc=_2c6;
if(opts.call(_2c6)=="[object Array]"){
_2c9=_29a(_2c6,_2c5);
t=_2c9[0];
_2cb=_2c9.length-t;
_2c6=_2c9[_2cb];
}else{
_2c9=[0];
if(_2c6){
if(opts.call(_2c6)=="[object Function]"){
t=_2c6._meta;
_2c9=_2c9.concat(t?t.bases:_2c6);
}else{
err("base class is not a callable constructor.",_2c5);
}
}else{
if(_2c6!==null){
err("unknown base class. Did you use dojo.require to pull it in?",_2c5);
}
}
}
if(_2c6){
for(i=_2cb-1;;--i){
_2c8=_2c4(_2c6);
if(!i){
break;
}
t=_2c9[i];
(t._meta?_2ad:mix)(_2c8,t.prototype);
ctor=new Function;
ctor.superclass=_2c6;
ctor.prototype=_2c8;
_2c6=_2c8.constructor=ctor;
}
}else{
_2c8={};
}
_2b7.safeMixin(_2c8,_2c7);
t=_2c7.constructor;
if(t!==op.constructor){
t.nom=_299;
_2c8.constructor=t;
}
for(i=_2cb-1;i;--i){
t=_2c9[i]._meta;
if(t&&t.chains){
_2ca=mix(_2ca||{},t.chains);
}
}
if(_2c8["-chains-"]){
_2ca=mix(_2ca||{},_2c8["-chains-"]);
}
t=!_2ca||!_2ca.hasOwnProperty(_299);
_2c9[0]=ctor=(_2ca&&_2ca.constructor==="manual")?_2bf(_2c9):(_2c9.length==1?_2bd(_2c7.constructor,t):_2b8(_2c9,t));
ctor._meta={bases:_2c9,hidden:_2c7,chains:_2ca,parents:_2cc,ctor:_2c7.constructor};
ctor.superclass=_2c6&&_2c6.prototype;
ctor.extend=_2b5;
ctor.prototype=_2c8;
_2c8.constructor=ctor;
_2c8.getInherited=_2a8;
_2c8.isInstanceOf=_2ab;
_2c8.inherited=_2aa;
_2c8.__inherited=_2a2;
if(_2c5){
_2c8.declaredClass=_2c5;
lang.setObject(_2c5,ctor);
}
if(_2ca){
for(name in _2ca){
if(_2c8[name]&&typeof _2ca[name]=="string"&&name!=_299){
t=_2c8[name]=_2c1(name,_2c9,_2ca[name]==="after");
t.nom=name;
}
}
}
return ctor;
};
dojo.safeMixin=_2b7.safeMixin=_2b1;
dojo.declare=_2b7;
return _2b7;
});
},"dojo/fx":function(){
define(["./_base/lang","./Evented","./_base/kernel","./_base/array","./_base/connect","./_base/fx","./dom","./dom-style","./dom-geometry","./ready","require"],function(lang,_2cd,dojo,_2ce,_2cf,_2d0,dom,_2d1,geom,_2d2,_2d3){
if(!dojo.isAsync){
_2d2(0,function(){
var _2d4=["./fx/Toggler"];
_2d3(_2d4);
});
}
var _2d5=dojo.fx={};
var _2d6={_fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,args||[]);
}
return this;
}};
var _2d7=function(_2d8){
this._index=-1;
this._animations=_2d8||[];
this._current=this._onAnimateCtx=this._onEndCtx=null;
this.duration=0;
_2ce.forEach(this._animations,function(a){
this.duration+=a.duration;
if(a.delay){
this.duration+=a.delay;
}
},this);
};
_2d7.prototype=new _2cd();
lang.extend(_2d7,{_onAnimate:function(){
this._fire("onAnimate",arguments);
},_onEnd:function(){
_2cf.disconnect(this._onAnimateCtx);
_2cf.disconnect(this._onEndCtx);
this._onAnimateCtx=this._onEndCtx=null;
if(this._index+1==this._animations.length){
this._fire("onEnd");
}else{
this._current=this._animations[++this._index];
this._onAnimateCtx=_2cf.connect(this._current,"onAnimate",this,"_onAnimate");
this._onEndCtx=_2cf.connect(this._current,"onEnd",this,"_onEnd");
this._current.play(0,true);
}
},play:function(_2d9,_2da){
if(!this._current){
this._current=this._animations[this._index=0];
}
if(!_2da&&this._current.status()=="playing"){
return this;
}
var _2db=_2cf.connect(this._current,"beforeBegin",this,function(){
this._fire("beforeBegin");
}),_2dc=_2cf.connect(this._current,"onBegin",this,function(arg){
this._fire("onBegin",arguments);
}),_2dd=_2cf.connect(this._current,"onPlay",this,function(arg){
this._fire("onPlay",arguments);
_2cf.disconnect(_2db);
_2cf.disconnect(_2dc);
_2cf.disconnect(_2dd);
});
if(this._onAnimateCtx){
_2cf.disconnect(this._onAnimateCtx);
}
this._onAnimateCtx=_2cf.connect(this._current,"onAnimate",this,"_onAnimate");
if(this._onEndCtx){
_2cf.disconnect(this._onEndCtx);
}
this._onEndCtx=_2cf.connect(this._current,"onEnd",this,"_onEnd");
this._current.play.apply(this._current,arguments);
return this;
},pause:function(){
if(this._current){
var e=_2cf.connect(this._current,"onPause",this,function(arg){
this._fire("onPause",arguments);
_2cf.disconnect(e);
});
this._current.pause();
}
return this;
},gotoPercent:function(_2de,_2df){
this.pause();
var _2e0=this.duration*_2de;
this._current=null;
_2ce.some(this._animations,function(a){
if(a.duration<=_2e0){
this._current=a;
return true;
}
_2e0-=a.duration;
return false;
});
if(this._current){
this._current.gotoPercent(_2e0/this._current.duration,_2df);
}
return this;
},stop:function(_2e1){
if(this._current){
if(_2e1){
for(;this._index+1<this._animations.length;++this._index){
this._animations[this._index].stop(true);
}
this._current=this._animations[this._index];
}
var e=_2cf.connect(this._current,"onStop",this,function(arg){
this._fire("onStop",arguments);
_2cf.disconnect(e);
});
this._current.stop();
}
return this;
},status:function(){
return this._current?this._current.status():"stopped";
},destroy:function(){
if(this._onAnimateCtx){
_2cf.disconnect(this._onAnimateCtx);
}
if(this._onEndCtx){
_2cf.disconnect(this._onEndCtx);
}
}});
lang.extend(_2d7,_2d6);
_2d5.chain=function(_2e2){
return new _2d7(_2e2);
};
var _2e3=function(_2e4){
this._animations=_2e4||[];
this._connects=[];
this._finished=0;
this.duration=0;
_2ce.forEach(_2e4,function(a){
var _2e5=a.duration;
if(a.delay){
_2e5+=a.delay;
}
if(this.duration<_2e5){
this.duration=_2e5;
}
this._connects.push(_2cf.connect(a,"onEnd",this,"_onEnd"));
},this);
this._pseudoAnimation=new _2d0.Animation({curve:[0,1],duration:this.duration});
var self=this;
_2ce.forEach(["beforeBegin","onBegin","onPlay","onAnimate","onPause","onStop","onEnd"],function(evt){
self._connects.push(_2cf.connect(self._pseudoAnimation,evt,function(){
self._fire(evt,arguments);
}));
});
};
lang.extend(_2e3,{_doAction:function(_2e6,args){
_2ce.forEach(this._animations,function(a){
a[_2e6].apply(a,args);
});
return this;
},_onEnd:function(){
if(++this._finished>this._animations.length){
this._fire("onEnd");
}
},_call:function(_2e7,args){
var t=this._pseudoAnimation;
t[_2e7].apply(t,args);
},play:function(_2e8,_2e9){
this._finished=0;
this._doAction("play",arguments);
this._call("play",arguments);
return this;
},pause:function(){
this._doAction("pause",arguments);
this._call("pause",arguments);
return this;
},gotoPercent:function(_2ea,_2eb){
var ms=this.duration*_2ea;
_2ce.forEach(this._animations,function(a){
a.gotoPercent(a.duration<ms?1:(ms/a.duration),_2eb);
});
this._call("gotoPercent",arguments);
return this;
},stop:function(_2ec){
this._doAction("stop",arguments);
this._call("stop",arguments);
return this;
},status:function(){
return this._pseudoAnimation.status();
},destroy:function(){
_2ce.forEach(this._connects,_2cf.disconnect);
}});
lang.extend(_2e3,_2d6);
_2d5.combine=function(_2ed){
return new _2e3(_2ed);
};
_2d5.wipeIn=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_2d0.animateProperty(lang.mixin({properties:{height:{start:function(){
o=s.overflow;
s.overflow="hidden";
if(s.visibility=="hidden"||s.display=="none"){
s.height="1px";
s.display="";
s.visibility="";
return 1;
}else{
var _2ee=_2d1.get(node,"height");
return Math.max(_2ee,1);
}
},end:function(){
return node.scrollHeight;
}}}},args));
var fini=function(){
s.height="auto";
s.overflow=o;
};
_2cf.connect(anim,"onStop",fini);
_2cf.connect(anim,"onEnd",fini);
return anim;
};
_2d5.wipeOut=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_2d0.animateProperty(lang.mixin({properties:{height:{end:1}}},args));
_2cf.connect(anim,"beforeBegin",function(){
o=s.overflow;
s.overflow="hidden";
s.display="";
});
var fini=function(){
s.overflow=o;
s.height="auto";
s.display="none";
};
_2cf.connect(anim,"onStop",fini);
_2cf.connect(anim,"onEnd",fini);
return anim;
};
_2d5.slideTo=function(args){
var node=args.node=dom.byId(args.node),top=null,left=null;
var init=(function(n){
return function(){
var cs=_2d1.getComputedStyle(n);
var pos=cs.position;
top=(pos=="absolute"?n.offsetTop:parseInt(cs.top)||0);
left=(pos=="absolute"?n.offsetLeft:parseInt(cs.left)||0);
if(pos!="absolute"&&pos!="relative"){
var ret=geom.position(n,true);
top=ret.y;
left=ret.x;
n.style.position="absolute";
n.style.top=top+"px";
n.style.left=left+"px";
}
};
})(node);
init();
var anim=_2d0.animateProperty(lang.mixin({properties:{top:args.top||0,left:args.left||0}},args));
_2cf.connect(anim,"beforeBegin",anim,init);
return anim;
};
return _2d5;
});
},"dojo/date/locale":function(){
define("dojo/date/locale",["../_base/kernel","../_base/lang","../_base/array","../date","../cldr/supplemental","../regexp","../string","../i18n!../cldr/nls/gregorian"],function(dojo,lang,_2ef,date,cldr,_2f0,_2f1,_2f2){
lang.getObject("date.locale",true,dojo);
function _2f3(_2f4,_2f5,_2f6,_2f7){
return _2f7.replace(/([a-z])\1*/ig,function(_2f8){
var s,pad,c=_2f8.charAt(0),l=_2f8.length,_2f9=["abbr","wide","narrow"];
switch(c){
case "G":
s=_2f5[(l<4)?"eraAbbr":"eraNames"][_2f4.getFullYear()<0?0:1];
break;
case "y":
s=_2f4.getFullYear();
switch(l){
case 1:
break;
case 2:
if(!_2f6.fullYear){
s=String(s);
s=s.substr(s.length-2);
break;
}
default:
pad=true;
}
break;
case "Q":
case "q":
s=Math.ceil((_2f4.getMonth()+1)/3);
pad=true;
break;
case "M":
var m=_2f4.getMonth();
if(l<3){
s=m+1;
pad=true;
}else{
var _2fa=["months","format",_2f9[l-3]].join("-");
s=_2f5[_2fa][m];
}
break;
case "w":
var _2fb=0;
s=dojo.date.locale._getWeekOfYear(_2f4,_2fb);
pad=true;
break;
case "d":
s=_2f4.getDate();
pad=true;
break;
case "D":
s=dojo.date.locale._getDayOfYear(_2f4);
pad=true;
break;
case "E":
var d=_2f4.getDay();
if(l<3){
s=d+1;
pad=true;
}else{
var _2fc=["days","format",_2f9[l-3]].join("-");
s=_2f5[_2fc][d];
}
break;
case "a":
var _2fd=(_2f4.getHours()<12)?"am":"pm";
s=_2f6[_2fd]||_2f5["dayPeriods-format-wide-"+_2fd];
break;
case "h":
case "H":
case "K":
case "k":
var h=_2f4.getHours();
switch(c){
case "h":
s=(h%12)||12;
break;
case "H":
s=h;
break;
case "K":
s=(h%12);
break;
case "k":
s=h||24;
break;
}
pad=true;
break;
case "m":
s=_2f4.getMinutes();
pad=true;
break;
case "s":
s=_2f4.getSeconds();
pad=true;
break;
case "S":
s=Math.round(_2f4.getMilliseconds()*Math.pow(10,l-3));
pad=true;
break;
case "v":
case "z":
s=dojo.date.locale._getZone(_2f4,true,_2f6);
if(s){
break;
}
l=4;
case "Z":
var _2fe=dojo.date.locale._getZone(_2f4,false,_2f6);
var tz=[(_2fe<=0?"+":"-"),_2f1.pad(Math.floor(Math.abs(_2fe)/60),2),_2f1.pad(Math.abs(_2fe)%60,2)];
if(l==4){
tz.splice(0,0,"GMT");
tz.splice(3,0,":");
}
s=tz.join("");
break;
default:
throw new Error("dojo.date.locale.format: invalid pattern char: "+_2f7);
}
if(pad){
s=_2f1.pad(s,l);
}
return s;
});
};
dojo.date.locale._getZone=function(_2ff,_300,_301){
if(_300){
return date.getTimezoneName(_2ff);
}else{
return _2ff.getTimezoneOffset();
}
};
dojo.date.locale.format=function(_302,_303){
_303=_303||{};
var _304=dojo.i18n.normalizeLocale(_303.locale),_305=_303.formatLength||"short",_306=dojo.date.locale._getGregorianBundle(_304),str=[],_307=lang.hitch(this,_2f3,_302,_306,_303);
if(_303.selector=="year"){
return _308(_306["dateFormatItem-yyyy"]||"yyyy",_307);
}
var _309;
if(_303.selector!="date"){
_309=_303.timePattern||_306["timeFormat-"+_305];
if(_309){
str.push(_308(_309,_307));
}
}
if(_303.selector!="time"){
_309=_303.datePattern||_306["dateFormat-"+_305];
if(_309){
str.push(_308(_309,_307));
}
}
return str.length==1?str[0]:_306["dateTimeFormat-"+_305].replace(/\{(\d+)\}/g,function(_30a,key){
return str[key];
});
};
dojo.date.locale.regexp=function(_30b){
return dojo.date.locale._parseInfo(_30b).regexp;
};
dojo.date.locale._parseInfo=function(_30c){
_30c=_30c||{};
var _30d=dojo.i18n.normalizeLocale(_30c.locale),_30e=dojo.date.locale._getGregorianBundle(_30d),_30f=_30c.formatLength||"short",_310=_30c.datePattern||_30e["dateFormat-"+_30f],_311=_30c.timePattern||_30e["timeFormat-"+_30f],_312;
if(_30c.selector=="date"){
_312=_310;
}else{
if(_30c.selector=="time"){
_312=_311;
}else{
_312=_30e["dateTimeFormat-"+_30f].replace(/\{(\d+)\}/g,function(_313,key){
return [_311,_310][key];
});
}
}
var _314=[],re=_308(_312,lang.hitch(this,_315,_314,_30e,_30c));
return {regexp:re,tokens:_314,bundle:_30e};
};
dojo.date.locale.parse=function(_316,_317){
var _318=/[\u200E\u200F\u202A\u202E]/g,info=dojo.date.locale._parseInfo(_317),_319=info.tokens,_31a=info.bundle,re=new RegExp("^"+info.regexp.replace(_318,"")+"$",info.strict?"":"i"),_31b=re.exec(_316&&_316.replace(_318,""));
if(!_31b){
return null;
}
var _31c=["abbr","wide","narrow"],_31d=[1970,0,1,0,0,0,0],amPm="",_31e=dojo.every(_31b,function(v,i){
if(!i){
return true;
}
var _31f=_319[i-1];
var l=_31f.length;
switch(_31f.charAt(0)){
case "y":
if(l!=2&&_317.strict){
_31d[0]=v;
}else{
if(v<100){
v=Number(v);
var year=""+new Date().getFullYear(),_320=year.substring(0,2)*100,_321=Math.min(Number(year.substring(2,4))+20,99);
_31d[0]=(v<_321)?_320+v:_320-100+v;
}else{
if(_317.strict){
return false;
}
_31d[0]=v;
}
}
break;
case "M":
if(l>2){
var _322=_31a["months-format-"+_31c[l-3]].concat();
if(!_317.strict){
v=v.replace(".","").toLowerCase();
_322=dojo.map(_322,function(s){
return s.replace(".","").toLowerCase();
});
}
v=dojo.indexOf(_322,v);
if(v==-1){
return false;
}
}else{
v--;
}
_31d[1]=v;
break;
case "E":
case "e":
var days=_31a["days-format-"+_31c[l-3]].concat();
if(!_317.strict){
v=v.toLowerCase();
days=dojo.map(days,function(d){
return d.toLowerCase();
});
}
v=dojo.indexOf(days,v);
if(v==-1){
return false;
}
break;
case "D":
_31d[1]=0;
case "d":
_31d[2]=v;
break;
case "a":
var am=_317.am||_31a["dayPeriods-format-wide-am"],pm=_317.pm||_31a["dayPeriods-format-wide-pm"];
if(!_317.strict){
var _323=/\./g;
v=v.replace(_323,"").toLowerCase();
am=am.replace(_323,"").toLowerCase();
pm=pm.replace(_323,"").toLowerCase();
}
if(_317.strict&&v!=am&&v!=pm){
return false;
}
amPm=(v==pm)?"p":(v==am)?"a":"";
break;
case "K":
if(v==24){
v=0;
}
case "h":
case "H":
case "k":
if(v>23){
return false;
}
_31d[3]=v;
break;
case "m":
_31d[4]=v;
break;
case "s":
_31d[5]=v;
break;
case "S":
_31d[6]=v;
}
return true;
});
var _324=+_31d[3];
if(amPm==="p"&&_324<12){
_31d[3]=_324+12;
}else{
if(amPm==="a"&&_324==12){
_31d[3]=0;
}
}
var _325=new Date(_31d[0],_31d[1],_31d[2],_31d[3],_31d[4],_31d[5],_31d[6]);
if(_317.strict){
_325.setFullYear(_31d[0]);
}
var _326=_319.join(""),_327=_326.indexOf("d")!=-1,_328=_326.indexOf("M")!=-1;
if(!_31e||(_328&&_325.getMonth()>_31d[1])||(_327&&_325.getDate()>_31d[2])){
return null;
}
if((_328&&_325.getMonth()<_31d[1])||(_327&&_325.getDate()<_31d[2])){
_325=date.add(_325,"hour",1);
}
return _325;
};
function _308(_329,_32a,_32b,_32c){
var _32d=function(x){
return x;
};
_32a=_32a||_32d;
_32b=_32b||_32d;
_32c=_32c||_32d;
var _32e=_329.match(/(''|[^'])+/g),_32f=_329.charAt(0)=="'";
dojo.forEach(_32e,function(_330,i){
if(!_330){
_32e[i]="";
}else{
_32e[i]=(_32f?_32b:_32a)(_330.replace(/''/g,"'"));
_32f=!_32f;
}
});
return _32c(_32e.join(""));
};
function _315(_331,_332,_333,_334){
_334=_2f0.escapeString(_334);
if(!_333.strict){
_334=_334.replace(" a"," ?a");
}
return _334.replace(/([a-z])\1*/ig,function(_335){
var s,c=_335.charAt(0),l=_335.length,p2="",p3="";
if(_333.strict){
if(l>1){
p2="0"+"{"+(l-1)+"}";
}
if(l>2){
p3="0"+"{"+(l-2)+"}";
}
}else{
p2="0?";
p3="0{0,2}";
}
switch(c){
case "y":
s="\\d{2,4}";
break;
case "M":
s=(l>2)?"\\S+?":"1[0-2]|"+p2+"[1-9]";
break;
case "D":
s="[12][0-9][0-9]|3[0-5][0-9]|36[0-6]|"+p2+"[1-9][0-9]|"+p3+"[1-9]";
break;
case "d":
s="3[01]|[12]\\d|"+p2+"[1-9]";
break;
case "w":
s="[1-4][0-9]|5[0-3]|"+p2+"[1-9]";
break;
case "E":
s="\\S+";
break;
case "h":
s="1[0-2]|"+p2+"[1-9]";
break;
case "k":
s="1[01]|"+p2+"\\d";
break;
case "H":
s="1\\d|2[0-3]|"+p2+"\\d";
break;
case "K":
s="1\\d|2[0-4]|"+p2+"[1-9]";
break;
case "m":
case "s":
s="[0-5]\\d";
break;
case "S":
s="\\d{"+l+"}";
break;
case "a":
var am=_333.am||_332["dayPeriods-format-wide-am"],pm=_333.pm||_332["dayPeriods-format-wide-pm"];
s=am+"|"+pm;
if(!_333.strict){
if(am!=am.toLowerCase()){
s+="|"+am.toLowerCase();
}
if(pm!=pm.toLowerCase()){
s+="|"+pm.toLowerCase();
}
if(s.indexOf(".")!=-1){
s+="|"+s.replace(/\./g,"");
}
}
s=s.replace(/\./g,"\\.");
break;
default:
s=".*";
}
if(_331){
_331.push(_335);
}
return "("+s+")";
}).replace(/[\xa0 ]/g,"[\\s\\xa0]");
};
var _336=[];
dojo.date.locale.addCustomFormats=function(_337,_338){
_336.push({pkg:_337,name:_338});
};
dojo.date.locale._getGregorianBundle=function(_339){
var _33a={};
dojo.forEach(_336,function(desc){
var _33b=dojo.i18n.getLocalization(desc.pkg,desc.name,_339);
_33a=lang.mixin(_33a,_33b);
},this);
return _33a;
};
dojo.date.locale.addCustomFormats("dojo.cldr","gregorian");
dojo.date.locale.getNames=function(item,type,_33c,_33d){
var _33e,_33f=dojo.date.locale._getGregorianBundle(_33d),_340=[item,_33c,type];
if(_33c=="standAlone"){
var key=_340.join("-");
_33e=_33f[key];
if(_33e[0]==1){
_33e=undefined;
}
}
_340[1]="format";
return (_33e||_33f[_340.join("-")]).concat();
};
dojo.date.locale.isWeekend=function(_341,_342){
var _343=cldr.getWeekend(_342),day=(_341||new Date()).getDay();
if(_343.end<_343.start){
_343.end+=7;
if(day<_343.start){
day+=7;
}
}
return day>=_343.start&&day<=_343.end;
};
dojo.date.locale._getDayOfYear=function(_344){
return date.difference(new Date(_344.getFullYear(),0,1,_344.getHours()),_344)+1;
};
dojo.date.locale._getWeekOfYear=function(_345,_346){
if(arguments.length==1){
_346=0;
}
var _347=new Date(_345.getFullYear(),0,1).getDay(),adj=(_347-_346+7)%7,week=Math.floor((dojo.date.locale._getDayOfYear(_345)+adj-1)/7);
if(_347==_346){
week++;
}
return week;
};
return dojo.date.locale;
});
},"dojo/json":function(){
define(["./has"],function(has){
"use strict";
var _348=typeof JSON!="undefined";
has.add("json-parse",_348);
has.add("json-stringify",_348&&JSON.stringify({a:0},function(k,v){
return v||1;
})=="{\"a\":1}");
if(has("json-stringify")){
return JSON;
}else{
var _349=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
return {parse:has("json-parse")?JSON.parse:function(str,_34a){
if(_34a&&!/^([\s\[\{]*(?:"(?:\\.|[^"])+"|-?\d[\d\.]*(?:[Ee][+-]?\d+)?|null|true|false|)[\s\]\}]*(?:,|:|$))+$/.test(str)){
throw new SyntaxError("Invalid characters in JSON");
}
return eval("("+str+")");
},stringify:function(_34b,_34c,_34d){
var _34e;
if(typeof _34c=="string"){
_34d=_34c;
_34c=null;
}
function _34f(it,_350,key){
if(_34c){
it=_34c(key,it);
}
var val,_351=typeof it;
if(_351=="number"){
return isFinite(it)?it+"":"null";
}
if(_351=="boolean"){
return it+"";
}
if(it===null){
return "null";
}
if(typeof it=="string"){
return _349(it);
}
if(_351=="function"||_351=="undefined"){
return _34e;
}
if(typeof it.toJSON=="function"){
return _34f(it.toJSON(key),_350,key);
}
if(it instanceof Date){
return "\"{FullYear}-{Month+}-{Date}T{Hours}:{Minutes}:{Seconds}Z\"".replace(/\{(\w+)(\+)?\}/g,function(t,prop,plus){
var num=it["getUTC"+prop]()+(plus?1:0);
return num<10?"0"+num:num;
});
}
if(it.valueOf()!==it){
return _34f(it.valueOf(),_350,key);
}
var _352=_34d?(_350+_34d):"";
var sep=_34d?" ":"";
var _353=_34d?"\n":"";
if(it instanceof Array){
var itl=it.length,res=[];
for(key=0;key<itl;key++){
var obj=it[key];
val=_34f(obj,_352,key);
if(typeof val!="string"){
val="null";
}
res.push(_353+_352+val);
}
return "["+res.join(",")+_353+_350+"]";
}
var _354=[];
for(key in it){
var _355;
if(typeof key=="number"){
_355="\""+key+"\"";
}else{
if(typeof key=="string"){
_355=_349(key);
}else{
continue;
}
}
val=_34f(it[key],_352,key);
if(typeof val!="string"){
continue;
}
_354.push(_353+_352+_355+":"+sep+val);
}
return "{"+_354.join(",")+_353+_350+"}";
};
return _34f(_34b,"","");
}};
}
});
},"dijit/_base":function(){
define("dijit/_base",[".","./a11y","./WidgetSet","./_base/focus","./_base/manager","./_base/place","./_base/popup","./_base/scroll","./_base/sniff","./_base/typematic","./_base/wai","./_base/window"],function(_356){
return _356._base;
});
},"dojo/_base/json":function(){
define(["./kernel","../json"],function(dojo,json){
dojo.fromJson=function(js){
return eval("("+js+")");
};
dojo._escapeString=json.stringify;
dojo.toJsonIndentStr="\t";
dojo.toJson=function(it,_357){
return json.stringify(it,function(key,_358){
if(_358){
var tf=_358.__json__||_358.json;
if(typeof tf=="function"){
return tf.call(_358);
}
}
return _358;
},_357&&dojo.toJsonIndentStr);
};
return dojo;
});
},"dojox/mobile/sniff":function(){
define("dojox/mobile/sniff",["dojo/_base/window","dojo/_base/sniff"],function(win,has){
var ua=navigator.userAgent;
has.add("bb",ua.indexOf("BlackBerry")>=0&&parseFloat(ua.split("Version/")[1])||undefined,undefined,true);
has.add("android",parseFloat(ua.split("Android ")[1])||undefined,undefined,true);
if(ua.match(/(iPhone|iPod|iPad)/)){
var p=RegExp.$1.replace(/P/,"p");
var v=ua.match(/OS ([\d_]+)/)?RegExp.$1:"1";
var os=parseFloat(v.replace(/_/,".").replace(/_/g,""));
has.add(p,os,undefined,true);
has.add("iphone",os,undefined,true);
}
if(has("webkit")){
has.add("touch",(typeof win.doc.documentElement.ontouchstart!="undefined"&&navigator.appVersion.indexOf("Mobile")!=-1)||!!has("android"),undefined,true);
}
return has;
});
},"dojox/mobile/ProgressIndicator":function(){
define("dojox/mobile/ProgressIndicator",["dojo/_base/config","dojo/_base/declare","dojo/dom-construct","dojo/dom-style","dojo/has"],function(_359,_35a,_35b,_35c,has){
var cls=_35a("dojox.mobile.ProgressIndicator",null,{interval:100,colors:["#C0C0C0","#C0C0C0","#C0C0C0","#C0C0C0","#C0C0C0","#C0C0C0","#B8B9B8","#AEAFAE","#A4A5A4","#9A9A9A","#8E8E8E","#838383"],constructor:function(){
this._bars=[];
this.domNode=_35b.create("DIV");
this.domNode.className="mblProgContainer";
if(_359["mblAndroidWorkaround"]!==false&&has("android")>=2.2&&has("android")<3){
_35c.set(this.domNode,"webkitTransform","translate3d(0,0,0)");
}
this.spinnerNode=_35b.create("DIV",null,this.domNode);
for(var i=0;i<this.colors.length;i++){
var div=_35b.create("DIV",{className:"mblProg mblProg"+i},this.spinnerNode);
this._bars.push(div);
}
},start:function(){
if(this.imageNode){
var img=this.imageNode;
var l=Math.round((this.domNode.offsetWidth-img.offsetWidth)/2);
var t=Math.round((this.domNode.offsetHeight-img.offsetHeight)/2);
img.style.margin=t+"px "+l+"px";
return;
}
var cntr=0;
var _35d=this;
var n=this.colors.length;
this.timer=setInterval(function(){
cntr--;
cntr=cntr<0?n-1:cntr;
var c=_35d.colors;
for(var i=0;i<n;i++){
var idx=(cntr+i)%n;
_35d._bars[i].style.backgroundColor=c[idx];
}
},this.interval);
},stop:function(){
if(this.timer){
clearInterval(this.timer);
}
this.timer=null;
if(this.domNode.parentNode){
this.domNode.parentNode.removeChild(this.domNode);
}
},setImage:function(file){
if(file){
this.imageNode=_35b.create("IMG",{src:file},this.domNode);
this.spinnerNode.style.display="none";
}else{
if(this.imageNode){
this.domNode.removeChild(this.imageNode);
this.imageNode=null;
}
this.spinnerNode.style.display="";
}
}});
cls._instance=null;
cls.getInstance=function(){
if(!cls._instance){
cls._instance=new cls();
}
return cls._instance;
};
return cls;
});
},"dijit/form/_FormWidgetMixin":function(){
define("dijit/form/_FormWidgetMixin",["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/_base/sniff","dojo/_base/window","dojo/window","../a11y"],function(_35e,_35f,_360,_361,lang,_362,has,win,_363,a11y){
return _35f("dijit.form._FormWidgetMixin",null,{name:"",alt:"",value:"",type:"text",tabIndex:"0",_setTabIndexAttr:"focusNode",disabled:false,intermediateChanges:false,scrollOnFocus:true,_setIdAttr:"focusNode",postCreate:function(){
this.inherited(arguments);
this.connect(this.domNode,"onmousedown","_onMouseDown");
},_setDisabledAttr:function(_364){
this._set("disabled",_364);
_360.set(this.focusNode,"disabled",_364);
if(this.valueNode){
_360.set(this.valueNode,"disabled",_364);
}
this.focusNode.setAttribute("aria-disabled",_364);
if(_364){
this._set("hovering",false);
this._set("active",false);
var _365="tabIndex" in this.attributeMap?this.attributeMap.tabIndex:("_setTabIndexAttr" in this)?this._setTabIndexAttr:"focusNode";
_35e.forEach(lang.isArray(_365)?_365:[_365],function(_366){
var node=this[_366];
if(has("webkit")||a11y.hasDefaultTabStop(node)){
node.setAttribute("tabIndex","-1");
}else{
node.removeAttribute("tabIndex");
}
},this);
}else{
if(this.tabIndex!=""){
this.set("tabIndex",this.tabIndex);
}
}
},_onFocus:function(e){
if(this.scrollOnFocus){
_363.scrollIntoView(this.domNode);
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&this.focusNode&&(_361.get(this.domNode,"display")!="none");
},focus:function(){
if(!this.disabled&&this.focusNode.focus){
try{
this.focusNode.focus();
}
catch(e){
}
}
},compare:function(val1,val2){
if(typeof val1=="number"&&typeof val2=="number"){
return (isNaN(val1)&&isNaN(val2))?0:val1-val2;
}else{
if(val1>val2){
return 1;
}else{
if(val1<val2){
return -1;
}else{
return 0;
}
}
}
},onChange:function(){
},_onChangeActive:false,_handleOnChange:function(_367,_368){
if(this._lastValueReported==undefined&&(_368===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_367;
}
this._pendingOnChange=this._pendingOnChange||(typeof _367!=typeof this._lastValueReported)||(this.compare(_367,this._lastValueReported)!=0);
if((this.intermediateChanges||_368||_368===undefined)&&this._pendingOnChange){
this._lastValueReported=_367;
this._pendingOnChange=false;
if(this._onChangeActive){
if(this._onChangeHandle){
clearTimeout(this._onChangeHandle);
}
this._onChangeHandle=setTimeout(lang.hitch(this,function(){
this._onChangeHandle=null;
this.onChange(_367);
}),0);
}
}
},create:function(){
this.inherited(arguments);
this._onChangeActive=true;
},destroy:function(){
if(this._onChangeHandle){
clearTimeout(this._onChangeHandle);
this.onChange(this._lastValueReported);
}
this.inherited(arguments);
},_onMouseDown:function(e){
if((!this.focused||!has("ie"))&&!e.ctrlKey&&_362.isLeft(e)&&this.isFocusable()){
var _369=this.connect(win.body(),"onmouseup",function(){
if(this.isFocusable()){
this.focus();
}
this.disconnect(_369);
});
}
}});
});
},"dojo/i18n":function(){
define(["./_base/kernel","require","./has","./_base/array","./_base/lang","./_base/xhr"],function(dojo,_36a,has,_36b,lang){
var _36c=dojo.i18n={},_36d=/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,_36e=function(root,_36f,_370,_371){
for(var _372=[_370+_371],_373=_36f.split("-"),_374="",i=0;i<_373.length;i++){
_374+=(_374?"-":"")+_373[i];
if(!root||root[_374]){
_372.push(_370+_374+"/"+_371);
}
}
return _372;
},_375={},_376=dojo.getL10nName=function(_377,_378,_379){
_379=_379?_379.toLowerCase():dojo.locale;
_377="dojo/i18n!"+_377.replace(/\./g,"/");
_378=_378.replace(/\./g,"/");
return (/root/i.test(_379))?(_377+"/nls/"+_378):(_377+"/nls/"+_379+"/"+_378);
},_37a=function(_37b,_37c,_37d,_37e,_37f,load){
_37b([_37c],function(root){
var _380=_375[_37c+"/"]=lang.clone(root.root),_381=_36e(!root._v1x&&root,_37f,_37d,_37e);
_37b(_381,function(){
for(var i=1;i<_381.length;i++){
_375[_381[i]]=_380=lang.mixin(lang.clone(_380),arguments[i]);
}
var _382=_37c+"/"+_37f;
_375[_382]=_380;
load&&load(lang.delegate(_380));
});
});
},_383=function(id,_384){
var _385=_36d.exec(id),_386=_385[1];
return /^\./.test(_386)?_384(_386)+"/"+id.substring(_386.length):id;
};
load=function(id,_387,load){
var _388=_36d.exec(id),_389=_388[1]+"/",_38a=_388[5]||_388[4],_38b=_389+_38a,_38c=(_388[5]&&_388[4]),_38d=_38c||dojo.locale,_38e=_38b+"/"+_38d;
if(_38c){
if(_375[_38e]){
load(_375[_38e]);
}else{
_37a(_387,_38b,_389,_38a,_38d,load);
}
return;
}
var _38f=dojo.config.extraLocale||[];
_38f=lang.isArray(_38f)?_38f:[_38f];
_38f.push(_38d);
_36b.forEach(_38f,function(_390){
_37a(_387,_38b,_389,_38a,_390,_390==_38d&&load);
});
};
true||has.add("dojo-v1x-i18n-Api",1);
if(1){
var _391=new Function("bundle","var __preAmdResult, __amdResult; function define(bundle){__amdResult= bundle;} __preAmdResult= eval(bundle); return [__preAmdResult, __amdResult];"),_392=function(url,_393,_394){
return _393?(/nls\/[^\/]+\/[^\/]+$/.test(url)?_393:{root:_393,_v1x:1}):_394;
},_395=function(deps,_396){
var _397=[];
dojo.forEach(deps,function(mid){
var url=_36a.toUrl(mid+".js");
if(_375[url]){
_397.push(_375[url]);
}else{
try{
var _398=_36a(mid);
if(_398){
_397.push(_398);
return;
}
}
catch(e){
}
dojo.xhrGet({url:url,sync:true,load:function(text){
var _399=_391(text);
_397.push(_375[url]=_392(url,_399[0],_399[1]));
},error:function(){
_397.push(_375[url]={});
}});
}
});
_396.apply(null,_397);
};
_36c.getLocalization=function(_39a,_39b,_39c){
var _39d,_39e=_376(_39a,_39b,_39c).substring(10);
load(_39e,(1&&!_36a.isXdUrl(_36a.toUrl(_39e+".js"))?_395:_36a),function(_39f){
_39d=_39f;
});
return _39d;
};
_36c.normalizeLocale=function(_3a0){
var _3a1=_3a0?_3a0.toLowerCase():dojo.locale;
if(_3a1=="root"){
_3a1="ROOT";
}
return _3a1;
};
}
return lang.mixin(_36c,{dynamic:true,normalize:_383,load:load,cache:function(mid,_3a2){
_375[mid]=_3a2;
}});
});
},"dijit/BackgroundIframe":function(){
define("dijit/BackgroundIframe",["require",".","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/_base/sniff","dojo/_base/window"],function(_3a3,_3a4,_3a5,_3a6,_3a7,lang,on,has,win){
var _3a8=new function(){
var _3a9=[];
this.pop=function(){
var _3aa;
if(_3a9.length){
_3aa=_3a9.pop();
_3aa.style.display="";
}else{
if(has("ie")<9){
var burl=_3a5["dojoBlankHtmlUrl"]||_3a3.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_3aa=win.doc.createElement(html);
}else{
_3aa=_3a6.create("iframe");
_3aa.src="javascript:\"\"";
_3aa.className="dijitBackgroundIframe";
_3aa.setAttribute("role","presentation");
_3a7.set(_3aa,"opacity",0.1);
}
_3aa.tabIndex=-1;
}
return _3aa;
};
this.push=function(_3ab){
_3ab.style.display="none";
_3a9.push(_3ab);
};
}();
_3a4.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("ie")||has("mozilla")){
var _3ac=(this.iframe=_3a8.pop());
node.appendChild(_3ac);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,function(){
this.resize(node);
}));
}else{
_3a7.set(_3ac,{width:"100%",height:"100%"});
}
}
};
lang.extend(_3a4.BackgroundIframe,{resize:function(node){
if(this.iframe){
_3a7.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
_3a8.push(this.iframe);
delete this.iframe;
}
}});
return _3a4.BackgroundIframe;
});
},"dojo/dom-construct":function(){
define("dojo/dom-construct",["exports","./_base/kernel","./_base/sniff","./_base/window","./dom","./dom-attr","./on"],function(_3ad,dojo,has,win,dom,attr,on){
var _3ae={option:["select"],tbody:["table"],thead:["table"],tfoot:["table"],tr:["table","tbody"],td:["table","tbody","tr"],th:["table","thead","tr"],legend:["fieldset"],caption:["table"],colgroup:["table"],col:["table","colgroup"],li:["ul"]},_3af=/<\s*([\w\:]+)/,_3b0={},_3b1=0,_3b2="__"+dojo._scopeName+"ToDomId";
for(var _3b3 in _3ae){
if(_3ae.hasOwnProperty(_3b3)){
var tw=_3ae[_3b3];
tw.pre=_3b3=="option"?"<select multiple=\"multiple\">":"<"+tw.join("><")+">";
tw.post="</"+tw.reverse().join("></")+">";
}
}
function _3b4(node,ref){
var _3b5=ref.parentNode;
if(_3b5){
_3b5.insertBefore(node,ref);
}
};
function _3b6(node,ref){
var _3b7=ref.parentNode;
if(_3b7){
if(_3b7.lastChild==ref){
_3b7.appendChild(node);
}else{
_3b7.insertBefore(node,ref.nextSibling);
}
}
};
var _3b8=null,_3b9;
on(window,"unload",function(){
_3b8=null;
});
_3ad.toDom=function toDom(frag,doc){
doc=doc||win.doc;
var _3ba=doc[_3b2];
if(!_3ba){
doc[_3b2]=_3ba=++_3b1+"";
_3b0[_3ba]=doc.createElement("div");
}
frag+="";
var _3bb=frag.match(_3af),tag=_3bb?_3bb[1].toLowerCase():"",_3bc=_3b0[_3ba],wrap,i,fc,df;
if(_3bb&&_3ae[tag]){
wrap=_3ae[tag];
_3bc.innerHTML=wrap.pre+frag+wrap.post;
for(i=wrap.length;i;--i){
_3bc=_3bc.firstChild;
}
}else{
_3bc.innerHTML=frag;
}
if(_3bc.childNodes.length==1){
return _3bc.removeChild(_3bc.firstChild);
}
df=doc.createDocumentFragment();
while(fc=_3bc.firstChild){
df.appendChild(fc);
}
return df;
};
_3ad.place=function place(node,_3bd,_3be){
_3bd=dom.byId(_3bd);
if(typeof node=="string"){
node=/^\s*</.test(node)?_3ad.toDom(node,_3bd.ownerDocument):dom.byId(node);
}
if(typeof _3be=="number"){
var cn=_3bd.childNodes;
if(!cn.length||cn.length<=_3be){
_3bd.appendChild(node);
}else{
_3b4(node,cn[_3be<0?0:_3be]);
}
}else{
switch(_3be){
case "before":
_3b4(node,_3bd);
break;
case "after":
_3b6(node,_3bd);
break;
case "replace":
_3bd.parentNode.replaceChild(node,_3bd);
break;
case "only":
_3ad.empty(_3bd);
_3bd.appendChild(node);
break;
case "first":
if(_3bd.firstChild){
_3b4(node,_3bd.firstChild);
break;
}
default:
_3bd.appendChild(node);
}
}
return node;
};
_3ad.create=function create(tag,_3bf,_3c0,pos){
var doc=win.doc;
if(_3c0){
_3c0=dom.byId(_3c0);
doc=_3c0.ownerDocument;
}
if(typeof tag=="string"){
tag=doc.createElement(tag);
}
if(_3bf){
attr.set(tag,_3bf);
}
if(_3c0){
_3ad.place(tag,_3c0,pos);
}
return tag;
};
_3ad.empty=has("ie")?function(node){
node=dom.byId(node);
for(var c;c=node.lastChild;){
_3ad.destroy(c);
}
}:function(node){
dom.byId(node).innerHTML="";
};
_3ad.destroy=function destroy(node){
node=dom.byId(node);
try{
var doc=node.ownerDocument;
if(!_3b8||_3b9!=doc){
_3b8=doc.createElement("div");
_3b9=doc;
}
_3b8.appendChild(node.parentNode?node.parentNode.removeChild(node):node);
_3b8.innerHTML="";
}
catch(e){
}
};
});
},"dojox/mobile/Opener":function(){
define("dojox/mobile/Opener",["dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dojo/dom-geometry","./Tooltip","./Overlay"],function(_3c1,lang,win,_3c2,_3c3,_3c4,_3c5,_3c6,_3c7){
var _3c8=_3c2.contains(win.doc.documentElement,"dj_phone");
var cls=_3c1("dojox.mobile.Opener",_3c8?_3c7:_3c6,{buildRendering:function(){
this.inherited(arguments);
this.cover=_3c3.create("div",{onclick:lang.hitch(this,"_onBlur"),"class":"mblOpenerUnderlay",style:{top:"0px",left:"0px",width:"0px",height:"0px",position:_3c8?"absolute":"fixed",backgroundColor:"transparent",overflow:"hidden",zIndex:"-1"}},this.domNode,"first");
this.connect(null,win.global.onorientationchange!==undefined?"onorientationchange":"onresize",lang.hitch(this,function(){
if(_3c4.get(this.cover,"height")!=="0px"){
this._resizeCover();
}
}));
},onShow:function(node){
},onHide:function(node,v){
},show:function(node,_3c9){
this.node=node;
this.onShow(node);
this._resizeCover();
return this.inherited(arguments);
},hide:function(val){
this.inherited(arguments);
_3c4.set(this.cover,{height:"0px"});
this.onHide(this.node,val);
},_resizeCover:function(){
if(_3c8){
_3c4.set(this.cover,{height:"0px"});
setTimeout(lang.hitch(this,function(){
var pos=_3c5.position(this.domNode,false);
_3c4.set(this.cover,{top:-pos.y+"px",left:-pos.x+"px",width:(pos.w+pos.x)+"px",height:(pos.h+pos.y)+"px"});
}),0);
}else{
_3c4.set(this.cover,{width:Math.max(win.doc.documentElement.scrollWidth||win.body().scrollWidth||win.doc.documentElement.clientWidth)+"px",height:Math.max(win.doc.documentElement.scrollHeight||win.body().scrollHeight||win.doc.documentElement.clientHeight)+"px"});
}
},_onBlur:function(e){
var ret=this.onBlur(e);
if(ret!==false){
this.hide(e);
}
return ret;
}});
cls.prototype.baseClass+=" mblOpener";
return cls;
});
},"dojox/mobile":function(){
define("dojox/mobile",[".","dojo/_base/lang","dojox/mobile/_base"],function(_3ca,lang,base){
lang.getObject("mobile",true,_3ca);
return _3ca.mobile;
});
},"dijit/form/_FormValueMixin":function(){
define("dijit/form/_FormValueMixin",["dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/sniff","./_FormWidgetMixin"],function(_3cb,_3cc,keys,has,_3cd){
return _3cb("dijit.form._FormValueMixin",_3cd,{readOnly:false,_setReadOnlyAttr:function(_3ce){
_3cc.set(this.focusNode,"readOnly",_3ce);
this.focusNode.setAttribute("aria-readonly",_3ce);
this._set("readOnly",_3ce);
},postCreate:function(){
this.inherited(arguments);
if(has("ie")){
this.connect(this.focusNode||this.domNode,"onkeydown",this._onKeyDown);
}
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.value;
}
},_setValueAttr:function(_3cf,_3d0){
this._handleOnChange(_3cf,_3d0);
},_handleOnChange:function(_3d1,_3d2){
this._set("value",_3d1);
this.inherited(arguments);
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
},_onKeyDown:function(e){
if(e.keyCode==keys.ESCAPE&&!(e.ctrlKey||e.altKey||e.metaKey)){
var te;
if(has("ie")<9||(has("ie")&&has("quirks"))){
e.preventDefault();
te=document.createEventObject();
te.keyCode=keys.ESCAPE;
te.shiftKey=e.shiftKey;
e.srcElement.fireEvent("onkeypress",te);
}
}
}});
});
},"dojo/_base/browser":function(){
if(require.has){
require.has.add("config-selectorEngine","acme");
}
define("dojo/_base/browser",["../ready","./kernel","./connect","./unload","./window","./event","./html","./NodeList","../query","./xhr","./fx"],function(dojo){
return dojo;
});
},"dojox/mobile/common":function(){
define("dojox/mobile/common",["dojo/_base/kernel","dojo/_base/array","dojo/_base/config","dojo/_base/connect","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dojo/ready","dijit/registry","./sniff","./uacss"],function(dojo,_3d3,_3d4,_3d5,lang,win,_3d6,_3d7,_3d8,_3d9,_3da,has,_3db){
var dm=lang.getObject("dojox.mobile",true);
dm.getScreenSize=function(){
return {h:win.global.innerHeight||win.doc.documentElement.clientHeight,w:win.global.innerWidth||win.doc.documentElement.clientWidth};
};
dm.updateOrient=function(){
var dim=dm.getScreenSize();
_3d6.replace(win.doc.documentElement,dim.h>dim.w?"dj_portrait":"dj_landscape",dim.h>dim.w?"dj_landscape":"dj_portrait");
};
dm.updateOrient();
dm.tabletSize=500;
dm.detectScreenSize=function(_3dc){
var dim=dm.getScreenSize();
var sz=Math.min(dim.w,dim.h);
var from,to;
if(sz>=dm.tabletSize&&(_3dc||(!this._sz||this._sz<dm.tabletSize))){
from="phone";
to="tablet";
}else{
if(sz<dm.tabletSize&&(_3dc||(!this._sz||this._sz>=dm.tabletSize))){
from="tablet";
to="phone";
}
}
if(to){
_3d6.replace(win.doc.documentElement,"dj_"+to,"dj_"+from);
_3d5.publish("/dojox/mobile/screenSize/"+to,[dim]);
}
this._sz=sz;
};
dm.detectScreenSize();
dm.setupIcon=function(_3dd,_3de){
if(_3dd&&_3de){
var arr=_3d3.map(_3de.split(/[ ,]/),function(item){
return item-0;
});
var t=arr[0];
var r=arr[1]+arr[2];
var b=arr[0]+arr[3];
var l=arr[1];
_3d8.set(_3dd,{clip:"rect("+t+"px "+r+"px "+b+"px "+l+"px)",top:(_3dd.parentNode?_3d8.get(_3dd,"top"):0)-t+"px",left:-l+"px"});
}
};
dm.hideAddressBarWait=typeof (_3d4["mblHideAddressBarWait"])==="number"?_3d4["mblHideAddressBarWait"]:1500;
dm.hide_1=function(_3df){
scrollTo(0,1);
var h=dm.getScreenSize().h+"px";
if(has("android")){
if(_3df){
win.body().style.minHeight=h;
}
dm.resizeAll();
}else{
if(_3df||dm._h===h&&h!==win.body().style.minHeight){
win.body().style.minHeight=h;
dm.resizeAll();
}
}
dm._h=h;
};
dm.hide_fs=function(){
var t=win.body().style.minHeight;
win.body().style.minHeight=(dm.getScreenSize().h*2)+"px";
scrollTo(0,1);
setTimeout(function(){
dm.hide_1(1);
dm._hiding=false;
},1000);
};
dm.hideAddressBar=function(evt){
if(dm.disableHideAddressBar||dm._hiding){
return;
}
dm._hiding=true;
dm._h=0;
win.body().style.minHeight=(dm.getScreenSize().h*2)+"px";
setTimeout(dm.hide_1,0);
setTimeout(dm.hide_1,200);
setTimeout(dm.hide_1,800);
setTimeout(dm.hide_fs,dm.hideAddressBarWait);
};
dm.resizeAll=function(evt,root){
if(dm.disableResizeAll){
return;
}
_3d5.publish("/dojox/mobile/resizeAll",[evt,root]);
dm.updateOrient();
dm.detectScreenSize();
var _3e0=function(w){
var _3e1=w.getParent&&w.getParent();
return !!((!_3e1||!_3e1.resize)&&w.resize);
};
var _3e2=function(w){
_3d3.forEach(w.getChildren(),function(_3e3){
if(_3e0(_3e3)){
_3e3.resize();
}
_3e2(_3e3);
});
};
if(root){
if(root.resize){
root.resize();
}
_3e2(root);
}else{
_3d3.forEach(_3d3.filter(_3da.toArray(),_3e0),function(w){
w.resize();
});
}
};
dm.openWindow=function(url,_3e4){
win.global.open(url,_3e4||"_blank");
};
dm.createDomButton=function(_3e5,_3e6,_3e7){
if(!dm._domButtons){
if(has("webkit")){
var _3e8=function(_3e9,dic){
var i,j;
if(!_3e9){
var dic={};
var ss=dojo.doc.styleSheets;
for(i=0;i<ss.length;i++){
ss[i]&&_3e8(ss[i],dic);
}
return dic;
}
var _3ea=_3e9.cssRules||[];
for(i=0;i<_3ea.length;i++){
var rule=_3ea[i];
if(rule.href&&rule.styleSheet){
_3e8(rule.styleSheet,dic);
}else{
if(rule.selectorText){
var sels=rule.selectorText.split(/,/);
for(j=0;j<sels.length;j++){
var sel=sels[j];
var n=sel.split(/>/).length-1;
if(sel.match(/(mblDomButton\w+)/)){
var cls=RegExp.$1;
if(!dic[cls]||n>dic[cls]){
dic[cls]=n;
}
}
}
}
}
}
};
dm._domButtons=_3e8();
}else{
dm._domButtons={};
}
}
var s=_3e5.className;
var node=_3e7||_3e5;
if(s.match(/(mblDomButton\w+)/)&&s.indexOf("/")===-1){
var _3eb=RegExp.$1;
var nDiv=4;
if(s.match(/(mblDomButton\w+_(\d+))/)){
nDiv=RegExp.$2-0;
}else{
if(dm._domButtons[_3eb]!==undefined){
nDiv=dm._domButtons[_3eb];
}
}
var _3ec=null;
if(has("bb")&&_3d4["mblBBBoxShadowWorkaround"]!==false){
_3ec={style:"-webkit-box-shadow:none"};
}
for(var i=0,p=node;i<nDiv;i++){
p=p.firstChild||_3d7.create("DIV",_3ec,p);
}
if(_3e7){
setTimeout(function(){
_3d6.remove(_3e5,_3eb);
},0);
_3d6.add(_3e7,_3eb);
}
}else{
if(s.indexOf(".")!==-1){
_3d7.create("IMG",{src:s},node);
}else{
return null;
}
}
_3d6.add(node,"mblDomButton");
if(_3d4["mblAndroidWorkaround"]!==false&&has("android")>=2.2){
_3d8.set(node,"webkitTransform","translate3d(0,0,0)");
}
!!_3e6&&_3d8.set(node,_3e6);
return node;
};
dm.createIcon=function(icon,_3ed,node,_3ee,_3ef){
if(icon&&icon.indexOf("mblDomButton")===0){
if(node&&node.className.match(/(mblDomButton\w+)/)){
_3d6.remove(node,RegExp.$1);
}else{
node=_3d7.create("DIV");
}
node.title=_3ee;
_3d6.add(node,icon);
dm.createDomButton(node);
}else{
if(icon&&icon!=="none"){
if(!node||node.nodeName!=="IMG"){
node=_3d7.create("IMG",{alt:_3ee});
}
node.src=(icon||"").replace("${theme}",dm.currentTheme);
dm.setupIcon(node,_3ed);
if(_3ef&&_3ed){
var arr=_3ed.split(/[ ,]/);
_3d8.set(_3ef,{width:arr[2]+"px",height:arr[3]+"px"});
}
}
}
if(_3ef){
_3ef.appendChild(node);
}
return node;
};
dm._iw=_3d4["mblIosWorkaround"]!==false&&has("iphone");
if(dm._iw){
dm._iwBgCover=_3d7.create("div");
}
if(_3d4.parseOnLoad){
_3d9(90,function(){
var _3f0=win.body().getElementsByTagName("*");
var i,len,s;
len=_3f0.length;
for(i=0;i<len;i++){
s=_3f0[i].getAttribute("dojoType");
if(s){
if(_3f0[i].parentNode.getAttribute("lazy")=="true"){
_3f0[i].setAttribute("__dojoType",s);
_3f0[i].removeAttribute("dojoType");
}
}
}
});
}
_3d9(function(){
dm.detectScreenSize(true);
if(_3d4["mblApplyPageStyles"]!==false){
_3d6.add(win.doc.documentElement,"mobile");
}
if(has("chrome")){
_3d6.add(win.doc.documentElement,"dj_chrome");
}
if(_3d4["mblAndroidWorkaround"]!==false&&has("android")>=2.2){
if(_3d4["mblAndroidWorkaroundButtonStyle"]!==false){
_3d7.create("style",{innerHTML:"BUTTON,INPUT[type='button'],INPUT[type='submit'],INPUT[type='reset'],INPUT[type='file']::-webkit-file-upload-button{-webkit-appearance:none;}"},win.doc.head,"first");
}
if(has("android")<3){
_3d8.set(win.doc.documentElement,"webkitTransform","translate3d(0,0,0)");
_3d5.connect(null,"onfocus",null,function(e){
_3d8.set(win.doc.documentElement,"webkitTransform","");
});
_3d5.connect(null,"onblur",null,function(e){
_3d8.set(win.doc.documentElement,"webkitTransform","translate3d(0,0,0)");
});
}else{
if(_3d4["mblAndroid3Workaround"]!==false){
_3d8.set(win.doc.documentElement,{webkitBackfaceVisibility:"hidden",webkitPerspective:8000});
}
}
}
var f=dm.resizeAll;
if(_3d4["mblHideAddressBar"]!==false&&navigator.appVersion.indexOf("Mobile")!=-1||_3d4["mblForceHideAddressBar"]===true){
dm.hideAddressBar();
if(_3d4["mblAlwaysHideAddressBar"]===true){
f=dm.hideAddressBar;
}
}
_3d5.connect(null,(win.global.onorientationchange!==undefined&&!has("android"))?"onorientationchange":"onresize",null,f);
var _3f1=win.body().getElementsByTagName("*");
var i,len=_3f1.length,s;
for(i=0;i<len;i++){
s=_3f1[i].getAttribute("__dojoType");
if(s){
_3f1[i].setAttribute("dojoType",s);
_3f1[i].removeAttribute("__dojoType");
}
}
if(dojo.hash){
var _3f2=function(root){
if(!root){
return [];
}
var arr=_3da.findWidgets(root);
var _3f3=arr;
for(var i=0;i<_3f3.length;i++){
arr=arr.concat(_3f2(_3f3[i].containerNode));
}
return arr;
};
_3d5.subscribe("/dojo/hashchange",null,function(_3f4){
var view=dm.currentView;
if(!view){
return;
}
var _3f5=dm._params;
if(!_3f5){
var _3f6=_3f4?_3f4:dm._defaultView.id;
var _3f7=_3f2(view.domNode);
var dir=1,_3f8="slide";
for(i=0;i<_3f7.length;i++){
var w=_3f7[i];
if("#"+_3f6==w.moveTo){
_3f8=w.transition;
dir=(w instanceof dm.Heading)?-1:1;
break;
}
}
_3f5=[_3f6,dir,_3f8];
}
view.performTransition.apply(view,_3f5);
dm._params=null;
});
}
win.body().style.visibility="visible";
});
_3da.getEnclosingWidget=function(node){
while(node){
var id=node.getAttribute&&node.getAttribute("widgetId");
if(id){
return _3da.byId(id);
}
node=node._parentNode||node.parentNode;
}
return null;
};
return dm;
});
},"dojox/mobile/Heading":function(){
define("dojox/mobile/Heading",["dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dijit/registry","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./View"],function(_3f9,_3fa,_3fb,lang,win,_3fc,_3fd,_3fe,_3ff,_400,_401,_402,View){
var dm=lang.getObject("dojox.mobile",true);
return _3fb("dojox.mobile.Heading",[_402,_401,_400],{back:"",href:"",moveTo:"",transition:"slide",label:"",iconBase:"",backProp:{className:"mblArrowButton"},tag:"H1",buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement(this.tag);
this.domNode.className="mblHeading";
if(!this.label){
_3f9.forEach(this.domNode.childNodes,function(n){
if(n.nodeType==3){
var v=lang.trim(n.nodeValue);
if(v){
this.label=v;
this.labelNode=_3fd.create("SPAN",{innerHTML:v},n,"replace");
}
}
},this);
}
if(!this.labelNode){
this.labelNode=_3fd.create("SPAN",null,this.domNode);
}
this.labelNode.className="mblHeadingSpanTitle";
this.labelDivNode=_3fd.create("DIV",{className:"mblHeadingDivTitle",innerHTML:this.labelNode.innerHTML},this.domNode);
},startup:function(){
if(this._started){
return;
}
var _403=this.getParent&&this.getParent();
if(!_403||!_403.resize){
var _404=this;
setTimeout(function(){
_404.resize();
},0);
}
this.inherited(arguments);
},resize:function(){
if(this._btn){
this._btn.style.width=this._body.offsetWidth+this._head.offsetWidth+"px";
}
if(this.labelNode){
var _405,_406;
var _407=this.containerNode.childNodes;
for(var i=_407.length-1;i>=0;i--){
var c=_407[i];
if(c.nodeType===1){
if(!_406&&_3fc.contains(c,"mblToolBarButton")&&_3fe.get(c,"float")==="right"){
_406=c;
}
if(!_405&&(_3fc.contains(c,"mblToolBarButton")&&_3fe.get(c,"float")==="left"||c===this._btn)){
_405=c;
}
}
}
if(!this.labelNodeLen&&this.label){
this.labelNode.style.display="inline";
this.labelNodeLen=this.labelNode.offsetWidth;
this.labelNode.style.display="";
}
var bw=this.domNode.offsetWidth;
var rw=_406?bw-_406.offsetLeft+5:0;
var lw=_405?_405.offsetLeft+_405.offsetWidth+5:0;
var tw=this.labelNodeLen||0;
_3fc[bw-Math.max(rw,lw)*2>tw?"add":"remove"](this.domNode,"mblHeadingCenterTitle");
}
_3f9.forEach(this.getChildren(),function(_408){
if(_408.resize){
_408.resize();
}
});
},_setBackAttr:function(back){
if(!back){
_3fd.destroy(this._btn);
this._btn=null;
this.back="";
}else{
if(!this._btn){
var btn=_3fd.create("DIV",this.backProp,this.domNode,"first");
var head=_3fd.create("DIV",{className:"mblArrowButtonHead"},btn);
var body=_3fd.create("DIV",{className:"mblArrowButtonBody mblArrowButtonText"},btn);
this._body=body;
this._head=head;
this._btn=btn;
this.backBtnNode=btn;
this.connect(body,"onclick","onClick");
}
this.back=back;
this._body.innerHTML=this._cv?this._cv(this.back):this.back;
}
this.resize();
},_setLabelAttr:function(_409){
this.label=_409;
this.labelNode.innerHTML=this.labelDivNode.innerHTML=this._cv?this._cv(_409):_409;
},findCurrentView:function(){
var w=this;
while(true){
w=w.getParent();
if(!w){
return null;
}
if(w instanceof View){
break;
}
}
return w;
},onClick:function(e){
var h1=this.domNode;
_3fc.add(h1,"mblArrowButtonSelected");
setTimeout(function(){
_3fc.remove(h1,"mblArrowButtonSelected");
},1000);
if(this.back&&!this.moveTo&&!this.href&&history){
history.back();
return;
}
var view=this.findCurrentView();
if(view){
view.clickedPosX=e.clientX;
view.clickedPosY=e.clientY;
}
this.goTo(this.moveTo,this.href);
},goTo:function(_40a,href){
var view=this.findCurrentView();
if(!view){
return;
}
if(href){
view.performTransition(null,-1,this.transition,this,function(){
location.href=href;
});
}else{
if(dm.app&&dm.app.STAGE_CONTROLLER_ACTIVE){
_3fa.publish("/dojox/mobile/app/goback");
}else{
var node=_3ff.byId(view.convertToId(_40a));
if(node){
var _40b=node.getParent();
while(view){
var _40c=view.getParent();
if(_40b===_40c){
break;
}
view=_40c;
}
}
if(view){
view.performTransition(_40a,-1,this.transition);
}
}
}
}});
});
},"dojo/_base/event":function(){
define(["./kernel","../on","../has","../dom-geometry"],function(dojo,on,has,dom){
if(on._fixEvent){
var _40d=on._fixEvent;
on._fixEvent=function(evt,se){
evt=_40d(evt,se);
if(evt){
dom.normalizeEvent(evt);
}
return evt;
};
}
dojo.fixEvent=function(evt,_40e){
if(on._fixEvent){
return on._fixEvent(evt,_40e);
}
return evt;
};
dojo.stopEvent=function(evt){
if(has("dom-addeventlistener")||(evt&&evt.preventDefault)){
evt.preventDefault();
evt.stopPropagation();
}else{
evt=evt||window.event;
evt.cancelBubble=true;
on._preventDefault.call(evt);
}
};
return {fix:dojo.fixEvent,stop:dojo.stopEvent};
});
},"dojox/main":function(){
define("dojox/main",["dojo/_base/kernel"],function(dojo){
return dojo.dojox;
});
},"dojox/mobile/RoundRectList":function(){
define("dojox/mobile/RoundRectList",["dojo/_base/array","dojo/_base/declare","dojo/_base/window","dijit/_Contained","dijit/_Container","dijit/_WidgetBase"],function(_40f,_410,win,_411,_412,_413){
return _410("dojox.mobile.RoundRectList",[_413,_412,_411],{transition:"slide",iconBase:"",iconPos:"",select:"",stateful:false,buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("UL");
this.domNode.className="mblRoundRectList";
},resize:function(){
_40f.forEach(this.getChildren(),function(_414){
if(_414.resize){
_414.resize();
}
});
},onCheckStateChanged:function(_415,_416){
},_setStatefulAttr:function(_417){
this.stateful=_417;
_40f.forEach(this.getChildren(),function(_418){
_418.setArrow&&_418.setArrow();
});
},deselectItem:function(item){
item.deselect();
},deselectAll:function(){
_40f.forEach(this.getChildren(),function(_419){
_419.deselect&&_419.deselect();
});
},selectItem:function(item){
item.select();
}});
});
},"dojo/Stateful":function(){
define(["./_base/kernel","./_base/declare","./_base/lang","./_base/array"],function(dojo,_41a,lang,_41b){
return dojo.declare("dojo.Stateful",null,{postscript:function(_41c){
if(_41c){
lang.mixin(this,_41c);
}
},get:function(name){
return this[name];
},set:function(name,_41d){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var _41e=this[name];
this[name]=_41d;
if(this._watchCallbacks){
this._watchCallbacks(name,_41e,_41d);
}
return this;
},watch:function(name,_41f){
var _420=this._watchCallbacks;
if(!_420){
var self=this;
_420=this._watchCallbacks=function(name,_421,_422,_423){
var _424=function(_425){
if(_425){
_425=_425.slice();
for(var i=0,l=_425.length;i<l;i++){
try{
_425[i].call(self,name,_421,_422);
}
catch(e){
console.error(e);
}
}
}
};
_424(_420["_"+name]);
if(!_423){
_424(_420["*"]);
}
};
}
if(!_41f&&typeof name==="function"){
_41f=name;
name="*";
}else{
name="_"+name;
}
var _426=_420[name];
if(typeof _426!=="object"){
_426=_420[name]=[];
}
_426.push(_41f);
return {unwatch:function(){
_426.splice(_41b.indexOf(_426,_41f),1);
}};
}});
});
},"dojox/mobile/deviceTheme":function(){
define("dojox/mobile/deviceTheme",["dojo/_base/array","dojo/_base/config","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","require"],function(_427,_428,lang,win,_429,_42a,_42b){
var dm=lang.getObject("dojox.mobile",true);
dm.loadCssFile=function(file){
dm.loadedCssFiles.push(_42a.create("LINK",{href:file,type:"text/css",rel:"stylesheet"},win.doc.getElementsByTagName("head")[0]));
};
dm.themeMap=dm.themeMap||[["Android","android",[]],["BlackBerry","blackberry",[]],["iPad","iphone",[_42b.toUrl("dojox/mobile/themes/iphone/ipad.css")]],["Custom","custom",[]],[".*","iphone",[]]];
dm.loadDeviceTheme=function(_42c){
var t=_428["mblThemeFiles"]||dm.themeFiles||["@theme"];
if(!lang.isArray(t)){
console.log("loadDeviceTheme: array is expected but found: "+t);
}
var i,j;
var m=dm.themeMap;
var ua=_42c||_428["mblUserAgent"]||(location.search.match(/theme=(\w+)/)?RegExp.$1:navigator.userAgent);
for(i=0;i<m.length;i++){
if(ua.match(new RegExp(m[i][0]))){
var _42d=m[i][1];
_429.replace(win.doc.documentElement,_42d+"_theme",dm.currentTheme?dm.currentTheme+"_theme":"");
dm.currentTheme=_42d;
var _42e=[].concat(m[i][2]);
for(j=t.length-1;j>=0;j--){
var pkg=lang.isArray(t[j])?(t[j][0]||"").replace(/\./g,"/"):"dojox/mobile";
var name=lang.isArray(t[j])?t[j][1]:t[j];
var f="themes/"+_42d+"/"+(name==="@theme"?_42d:name)+".css";
_42e.unshift(_42b.toUrl(pkg+"/"+f));
}
_427.forEach(dm.loadedCssFiles,function(n){
n.parentNode.removeChild(n);
});
dm.loadedCssFiles=[];
for(j=0;j<_42e.length;j++){
dm.loadCssFile(_42e[j].toString());
}
if(_42c&&dm.loadCompatCssFiles){
dm.loadCompatCssFiles();
}
break;
}
}
};
if(dm.configDeviceTheme){
dm.configDeviceTheme();
}
dm.loadDeviceTheme();
return dm;
});
},"dojox/mobile/app/List":function(){
define(["dijit","dojo","dojox","dojo/require!dojo/string,dijit/_WidgetBase"],function(_42f,dojo,_430){
dojo.provide("dojox.mobile.app.List");
dojo.experimental("dojox.mobile.app.List");
dojo.require("dojo.string");
dojo.require("dijit._WidgetBase");
(function(){
var _431={};
dojo.declare("dojox.mobile.app.List",_42f._WidgetBase,{items:null,itemTemplate:"",emptyTemplate:"",dividerTemplate:"",dividerFunction:null,labelDelete:"Delete",labelCancel:"Cancel",controller:null,autoDelete:true,enableDelete:true,enableHold:true,formatters:null,_templateLoadCount:0,_mouseDownPos:null,baseClass:"list",constructor:function(){
this._checkLoadComplete=dojo.hitch(this,this._checkLoadComplete);
this._replaceToken=dojo.hitch(this,this._replaceToken);
this._postDeleteAnim=dojo.hitch(this,this._postDeleteAnim);
},postCreate:function(){
var _432=this;
if(this.emptyTemplate){
this._templateLoadCount++;
}
if(this.itemTemplate){
this._templateLoadCount++;
}
if(this.dividerTemplate){
this._templateLoadCount++;
}
this.connect(this.domNode,"onmousedown",function(_433){
var _434=_433;
if(_433.targetTouches&&_433.targetTouches.length>0){
_434=_433.targetTouches[0];
}
var _435=_432._getRowNode(_433.target);
if(_435){
_432._setDataInfo(_435,_433);
_432._selectRow(_435);
_432._mouseDownPos={x:_434.pageX,y:_434.pageY};
_432._dragThreshold=null;
}
});
this.connect(this.domNode,"onmouseup",function(_436){
if(_436.targetTouches&&_436.targetTouches.length>0){
_436=_436.targetTouches[0];
}
var _437=_432._getRowNode(_436.target);
if(_437){
_432._setDataInfo(_437,_436);
if(_432._selectedRow){
_432.onSelect(_437._data,_437._idx,_437);
}
this._deselectRow();
}
});
if(this.enableDelete){
this.connect(this.domNode,"mousemove",function(_438){
dojo.stopEvent(_438);
if(!_432._selectedRow){
return;
}
var _439=_432._getRowNode(_438.target);
if(_432.enableDelete&&_439&&!_432._deleting){
_432.handleDrag(_438);
}
});
}
this.connect(this.domNode,"onclick",function(_43a){
if(_43a.touches&&_43a.touches.length>0){
_43a=_43a.touches[0];
}
var _43b=_432._getRowNode(_43a.target,true);
if(_43b){
_432._setDataInfo(_43b,_43a);
}
});
this.connect(this.domNode,"mouseout",function(_43c){
if(_43c.touches&&_43c.touches.length>0){
_43c=_43c.touches[0];
}
if(_43c.target==_432._selectedRow){
_432._deselectRow();
}
});
if(!this.itemTemplate){
throw Error("An item template must be provided to "+this.declaredClass);
}
this._loadTemplate(this.itemTemplate,"itemTemplate",this._checkLoadComplete);
if(this.emptyTemplate){
this._loadTemplate(this.emptyTemplate,"emptyTemplate",this._checkLoadComplete);
}
if(this.dividerTemplate){
this._loadTemplate(this.dividerTemplate,"dividerTemplate",this._checkLoadComplete);
}
},handleDrag:function(_43d){
var _43e=_43d;
if(_43d.targetTouches&&_43d.targetTouches.length>0){
_43e=_43d.targetTouches[0];
}
var diff=_43e.pageX-this._mouseDownPos.x;
var _43f=Math.abs(diff);
if(_43f>10&&!this._dragThreshold){
this._dragThreshold=dojo.marginBox(this._selectedRow).w*0.6;
if(!this.autoDelete){
this.createDeleteButtons(this._selectedRow);
}
}
this._selectedRow.style.left=(_43f>10?diff:0)+"px";
if(this._dragThreshold&&this._dragThreshold<_43f){
this.preDelete(diff);
}
},handleDragCancel:function(){
if(this._deleting){
return;
}
dojo.removeClass(this._selectedRow,"hold");
this._selectedRow.style.left=0;
this._mouseDownPos=null;
this._dragThreshold=null;
this._deleteBtns&&dojo.style(this._deleteBtns,"display","none");
},preDelete:function(_440){
var self=this;
this._deleting=true;
dojo.animateProperty({node:this._selectedRow,duration:400,properties:{left:{end:_440+((_440>0?1:-1)*this._dragThreshold*0.8)}},onEnd:dojo.hitch(this,function(){
if(this.autoDelete){
this.deleteRow(this._selectedRow);
}
})}).play();
},deleteRow:function(row){
dojo.style(row,{visibility:"hidden",minHeight:"0px"});
dojo.removeClass(row,"hold");
this._deleteAnimConn=this.connect(row,"webkitAnimationEnd",this._postDeleteAnim);
dojo.addClass(row,"collapsed");
},_postDeleteAnim:function(_441){
if(this._deleteAnimConn){
this.disconnect(this._deleteAnimConn);
this._deleteAnimConn=null;
}
var row=this._selectedRow;
var _442=row.nextSibling;
var _443=row.previousSibling;
if(_443&&_443._isDivider){
if(!_442||_442._isDivider){
_443.parentNode.removeChild(_443);
}
}
row.parentNode.removeChild(row);
this.onDelete(row._data,row._idx,this.items);
while(_442){
if(_442._idx){
_442._idx--;
}
_442=_442.nextSibling;
}
dojo.destroy(row);
dojo.query("> *:not(.buttons)",this.domNode).forEach(this.applyClass);
this._deleting=false;
this._deselectRow();
},createDeleteButtons:function(_444){
var mb=dojo.marginBox(_444);
var pos=dojo._abs(_444,true);
if(!this._deleteBtns){
this._deleteBtns=dojo.create("div",{"class":"buttons"},this.domNode);
this.buttons=[];
this.buttons.push(new _430.mobile.Button({btnClass:"mblRedButton",label:this.labelDelete}));
this.buttons.push(new _430.mobile.Button({btnClass:"mblBlueButton",label:this.labelCancel}));
dojo.place(this.buttons[0].domNode,this._deleteBtns);
dojo.place(this.buttons[1].domNode,this._deleteBtns);
dojo.addClass(this.buttons[0].domNode,"deleteBtn");
dojo.addClass(this.buttons[1].domNode,"cancelBtn");
this._handleButtonClick=dojo.hitch(this._handleButtonClick);
this.connect(this._deleteBtns,"onclick",this._handleButtonClick);
}
dojo.removeClass(this._deleteBtns,"fade out fast");
dojo.style(this._deleteBtns,{display:"",width:mb.w+"px",height:mb.h+"px",top:(_444.offsetTop)+"px",left:"0px"});
},onDelete:function(data,_445,_446){
_446.splice(_445,1);
if(_446.length<1){
this.render();
}
},cancelDelete:function(){
this._deleting=false;
this.handleDragCancel();
},_handleButtonClick:function(_447){
if(_447.touches&&_447.touches.length>0){
_447=_447.touches[0];
}
var node=_447.target;
if(dojo.hasClass(node,"deleteBtn")){
this.deleteRow(this._selectedRow);
}else{
if(dojo.hasClass(node,"cancelBtn")){
this.cancelDelete();
}else{
return;
}
}
dojo.addClass(this._deleteBtns,"fade out");
},applyClass:function(node,idx,_448){
dojo.removeClass(node,"first last");
if(idx==0){
dojo.addClass(node,"first");
}
if(idx==_448.length-1){
dojo.addClass(node,"last");
}
},_setDataInfo:function(_449,_44a){
_44a.item=_449._data;
_44a.index=_449._idx;
},onSelect:function(data,_44b,_44c){
},_selectRow:function(row){
if(this._deleting&&this._selectedRow&&row!=this._selectedRow){
this.cancelDelete();
}
if(!dojo.hasClass(row,"row")){
return;
}
if(this.enableHold||this.enableDelete){
dojo.addClass(row,"hold");
}
this._selectedRow=row;
},_deselectRow:function(){
if(!this._selectedRow||this._deleting){
return;
}
this.handleDragCancel();
dojo.removeClass(this._selectedRow,"hold");
this._selectedRow=null;
},_getRowNode:function(_44d,_44e){
while(_44d&&!_44d._data&&_44d!=this.domNode){
if(!_44e&&dojo.hasClass(_44d,"noclick")){
return null;
}
_44d=_44d.parentNode;
}
return _44d==this.domNode?null:_44d;
},applyTemplate:function(_44f,data){
return dojo._toDom(dojo.string.substitute(_44f,data,this._replaceToken,this.formatters||this));
},render:function(){
dojo.query("> *:not(.buttons)",this.domNode).forEach(dojo.destroy);
if(this.items.length<1&&this.emptyTemplate){
dojo.place(dojo._toDom(this.emptyTemplate),this.domNode,"first");
}else{
this.domNode.appendChild(this._renderRange(0,this.items.length));
}
if(dojo.hasClass(this.domNode.parentNode,"mblRoundRect")){
dojo.addClass(this.domNode.parentNode,"mblRoundRectList");
}
var divs=dojo.query("> .row",this.domNode);
if(divs.length>0){
dojo.addClass(divs[0],"first");
dojo.addClass(divs[divs.length-1],"last");
}
},_renderRange:function(_450,_451){
var rows=[];
var row,i;
var frag=document.createDocumentFragment();
_450=Math.max(0,_450);
_451=Math.min(_451,this.items.length);
for(i=_450;i<_451;i++){
row=this.applyTemplate(this.itemTemplate,this.items[i]);
dojo.addClass(row,"row");
row._data=this.items[i];
row._idx=i;
rows.push(row);
}
if(!this.dividerFunction||!this.dividerTemplate){
for(i=_450;i<_451;i++){
rows[i]._data=this.items[i];
rows[i]._idx=i;
frag.appendChild(rows[i]);
}
}else{
var _452=null;
var _453;
var _454;
for(i=_450;i<_451;i++){
rows[i]._data=this.items[i];
rows[i]._idx=i;
_453=this.dividerFunction(this.items[i]);
if(_453&&_453!=_452){
_454=this.applyTemplate(this.dividerTemplate,{label:_453,item:this.items[i]});
_454._isDivider=true;
frag.appendChild(_454);
_452=_453;
}
frag.appendChild(rows[i]);
}
}
return frag;
},_replaceToken:function(_455,key){
if(key.charAt(0)=="!"){
_455=dojo.getObject(key.substr(1),false,_this);
}
if(typeof _455=="undefined"){
return "";
}
if(_455==null){
return "";
}
return key.charAt(0)=="!"?_455:_455.toString().replace(/"/g,"&quot;");
},_checkLoadComplete:function(){
this._templateLoadCount--;
if(this._templateLoadCount<1&&this.get("items")){
this.render();
}
},_loadTemplate:function(url,_456,_457){
if(!url){
_457();
return;
}
if(_431[url]){
this.set(_456,_431[url]);
_457();
}else{
var _458=this;
dojo.xhrGet({url:url,sync:false,handleAs:"text",load:function(text){
_431[url]=dojo.trim(text);
_458.set(_456,_431[url]);
_457();
}});
}
},_setFormattersAttr:function(_459){
this.formatters=_459;
},_setItemsAttr:function(_45a){
this.items=_45a||[];
if(this._templateLoadCount<1&&_45a){
this.render();
}
},destroy:function(){
if(this.buttons){
dojo.forEach(this.buttons,function(_45b){
_45b.destroy();
});
this.buttons=null;
}
this.inherited(arguments);
}});
})();
});
},"dojox/mobile/Overlay":function(){
define("dojox/mobile/Overlay",["dojo/_base/declare","dojo/_base/lang","dojo/_base/sniff","dojo/_base/window","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/window","dijit/_WidgetBase","dojo/_base/array","dijit/registry"],function(_45c,lang,has,win,_45d,_45e,_45f,_460,_461,_462,_463){
return _45c("dojox.mobile.Overlay",_461,{baseClass:"mblOverlay mblOverlayHidden",show:function(_464){
_462.forEach(_463.findWidgets(this.domNode),function(w){
if(w&&w.height=="auto"&&typeof w.resize=="function"){
w.resize();
}
});
var vp,_465;
var _466=lang.hitch(this,function(){
_45f.set(this.domNode,{position:"",top:"auto",bottom:"0px"});
_465=_45e.position(this.domNode);
vp=_460.getBox();
if((_465.y+_465.h)!=vp.h||has("android")<3){
_465.y=vp.t+vp.h-_465.h;
_45f.set(this.domNode,{position:"absolute",top:_465.y+"px",bottom:"auto"});
}
});
_466();
if(_464){
var _467=_45e.position(_464);
if(_465.y<_467.y){
win.global.scrollBy(0,_467.y+_467.h-_465.y);
_466();
}
}
_45d.replace(this.domNode,["mblCoverv","mblIn"],["mblOverlayHidden","mblRevealv","mblOut","mblReverse"]);
var _468=this.domNode;
setTimeout(function(){
_45d.add(_468,"mblTransition");
},100);
var _469=null;
this._moveHandle=this.connect(win.doc.documentElement,"ontouchmove",function(){
if(_469){
clearTimeout(_469);
}
_469=setTimeout(function(){
_466();
_469=null;
},0);
});
},hide:function(){
if(this._moveHandle){
this.disconnect(this._moveHandle);
this._moveHandle=null;
}
if(has("webkit")){
var _46a=this.connect(this.domNode,"webkitTransitionEnd",function(){
this.disconnect(_46a);
_45d.replace(this.domNode,["mblOverlayHidden"],["mblRevealv","mblOut","mblReverse","mblTransition"]);
});
_45d.replace(this.domNode,["mblRevealv","mblOut","mblReverse"],["mblCoverv","mblIn","mblTransition"]);
var _46b=this.domNode;
setTimeout(function(){
_45d.add(_46b,"mblTransition");
},100);
}else{
_45d.replace(this.domNode,["mblOverlayHidden"],["mblCoverv","mblIn","mblRevealv","mblOut","mblReverse"]);
}
},onBlur:function(e){
return false;
}});
});
},"dojox/mobile/IconItem":function(){
define("dojox/mobile/IconItem",["dojo/_base/kernel","dojo/_base/array","dojo/_base/declare","dojo/_base/lang","dojo/_base/sniff","dojo/_base/window","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dijit/registry","./common","./_ItemBase","./TransitionEvent"],function(dojo,_46c,_46d,lang,has,win,_46e,_46f,_470,_471,_472,_473,_474,_475){
return _46d("dojox.mobile.IconItem",_474,{lazy:false,requires:"",timeout:10,closeBtnClass:"mblDomButtonBlueMinus",closeBtnProp:null,templateString:"<div class=\"mblIconArea\" dojoAttachPoint=\"iconDivNode\">"+"<div><img src=\"${icon}\" dojoAttachPoint=\"iconNode\"></div><span dojoAttachPoint=\"labelNode1\"></span>"+"</div>",templateStringSub:"<li class=\"mblIconItemSub\" lazy=\"${lazy}\" style=\"display:none;\" dojoAttachPoint=\"contentNode\">"+"<h2 class=\"mblIconContentHeading\" dojoAttachPoint=\"closeNode\">"+"<div class=\"${closeBtnClass}\" style=\"position:absolute;left:4px;top:2px;\" dojoAttachPoint=\"closeIconNode\"></div><span dojoAttachPoint=\"labelNode2\"></span>"+"</h2>"+"<div class=\"mblContent\" dojoAttachPoint=\"containerNode\"></div>"+"</li>",createTemplate:function(s){
_46c.forEach(["lazy","icon","closeBtnClass"],function(v){
while(s.indexOf("${"+v+"}")!=-1){
s=s.replace("${"+v+"}",this[v]);
}
},this);
var div=win.doc.createElement("DIV");
div.innerHTML=s;
var _476=div.getElementsByTagName("*");
var i,len,s1;
len=_476.length;
for(i=0;i<len;i++){
s1=_476[i].getAttribute("dojoAttachPoint");
if(s1){
this[s1]=_476[i];
}
}
if(this.closeIconNode&&this.closeBtnProp){
_46e.set(this.closeIconNode,this.closeBtnProp);
}
var _477=div.removeChild(div.firstChild);
div=null;
return _477;
},buildRendering:function(){
this.inheritParams();
var node=this.createTemplate(this.templateString);
this.subNode=this.createTemplate(this.templateStringSub);
this.subNode._parentNode=this.domNode;
this.domNode=this.srcNodeRef||_470.create("LI");
_46f.add(this.domNode,"mblIconItem");
if(this.srcNodeRef){
for(var i=0,len=this.srcNodeRef.childNodes.length;i<len;i++){
this.containerNode.appendChild(this.srcNodeRef.firstChild);
}
}
this.domNode.appendChild(node);
},postCreate:function(){
_473.createDomButton(this.closeIconNode,{top:"-2px",left:"1px"});
this.connect(this.iconNode,"onmousedown","onMouseDownIcon");
this.connect(this.iconNode,"onclick","iconClicked");
this.connect(this.closeIconNode,"onclick","closeIconClicked");
this.connect(this.iconNode,"onerror","onError");
},highlight:function(){
_46f.add(this.iconDivNode,"mblVibrate");
if(this.timeout>0){
var _478=this;
setTimeout(function(){
_478.unhighlight();
},this.timeout*1000);
}
},unhighlight:function(){
_46f.remove(this.iconDivNode,"mblVibrate");
},instantiateWidget:function(e){
var _479=this.containerNode.getElementsByTagName("*");
var len=_479.length;
var s;
for(var i=0;i<len;i++){
s=_479[i].getAttribute("dojoType");
if(s){
dojo["require"](s);
}
}
if(len>0){
dojo.parser.parse(this.containerNode);
}
this.lazy=false;
},isOpen:function(e){
return this.containerNode.style.display!="none";
},onMouseDownIcon:function(e){
_471.set(this.iconNode,"opacity",this.getParent().pressedIconOpacity);
},iconClicked:function(e){
if(e){
this.setTransitionPos(e);
setTimeout(lang.hitch(this,function(d){
this.iconClicked();
}),0);
return;
}
if(this.href&&this.hrefTarget){
_473.openWindow(this.href,this.hrefTarget);
dojo.style(this.iconNode,"opacity",1);
return;
}
var _47a;
if(this.moveTo||this.href||this.url||this.scene){
_47a={moveTo:this.moveTo,href:this.href,url:this.url,scene:this.scene,transitionDir:this.transitionDir,transition:this.transition};
}else{
if(this.transitionOptions){
_47a=this.transitionOptions;
}
}
if(_47a){
setTimeout(lang.hitch(this,function(d){
_471.set(this.iconNode,"opacity",1);
}),1500);
}else{
return this.open(e);
}
if(_47a){
return new _475(this.domNode,_47a,e).dispatch();
}
},closeIconClicked:function(e){
if(e){
setTimeout(lang.hitch(this,function(d){
this.closeIconClicked();
}),0);
return;
}
this.close();
},open:function(e){
var _47b=this.getParent();
if(this.transition=="below"){
if(_47b.single){
_47b.closeAll();
_471.set(this.iconNode,"opacity",this.getParent().pressedIconOpacity);
}
this._open_1();
}else{
_47b._opening=this;
if(_47b.single){
this.closeNode.style.display="none";
_47b.closeAll();
var view=_472.byId(_47b.id+"_mblApplView");
view._heading._setLabelAttr(this.label);
}
var _47c=this.transitionOptions||{transition:this.transition,transitionDir:this.transitionDir,moveTo:_47b.id+"_mblApplView"};
new _475(this.domNode,_47c,e).dispatch();
}
},_open_1:function(){
this.contentNode.style.display="";
this.unhighlight();
if(this.lazy){
if(this.requires){
_46c.forEach(this.requires.split(/,/),function(c){
dojo["require"](c);
});
}
this.instantiateWidget();
}
this.contentNode.scrollIntoView();
this.onOpen();
},close:function(){
if(has("webkit")){
var t=this.domNode.parentNode.offsetWidth/8;
var y=this.iconNode.offsetLeft;
var pos=0;
for(var i=1;i<=3;i++){
if(t*(2*i-1)<y&&y<=t*(2*(i+1)-1)){
pos=i;
break;
}
}
_46f.add(this.containerNode.parentNode,"mblCloseContent mblShrink"+pos);
}else{
this.containerNode.parentNode.style.display="none";
}
_471.set(this.iconNode,"opacity",1);
this.onClose();
},onOpen:function(){
},onClose:function(){
},onError:function(){
var icon=this.getParent().defaultIcon;
if(icon){
this.iconNode.src=icon;
}
},_setIconAttr:function(icon){
if(!this.getParent()){
return;
}
this.icon=icon;
_473.createIcon(icon,this.iconPos,this.iconNode,this.alt);
if(this.iconPos){
_46f.add(this.iconNode,"mblIconItemSpriteIcon");
var arr=this.iconPos.split(/[ ,]/);
var p=this.iconNode.parentNode;
_471.set(p,{width:arr[2]+"px",top:Math.round((p.offsetHeight-arr[3])/2)+1+"px",margin:"auto"});
}
},_setLabelAttr:function(text){
this.label=text;
var s=this._cv?this._cv(text):text;
this.labelNode1.innerHTML=s;
this.labelNode2.innerHTML=s;
}});
});
},"dojox/mobile/app/ListSelector":function(){
define(["dijit","dojo","dojox","dojo/require!dojox/mobile/app/_Widget,dojo/fx"],function(_47d,dojo,_47e){
dojo.provide("dojox.mobile.app.ListSelector");
dojo.experimental("dojox.mobile.app.ListSelector");
dojo.require("dojox.mobile.app._Widget");
dojo.require("dojo.fx");
dojo.declare("dojox.mobile.app.ListSelector",_47e.mobile.app._Widget,{data:null,controller:null,onChoose:null,destroyOnHide:false,_setDataAttr:function(data){
this.data=data;
if(this.data){
this.render();
}
},postCreate:function(){
dojo.addClass(this.domNode,"listSelector");
var _47f=this;
this.connect(this.domNode,"onclick",function(_480){
if(!dojo.hasClass(_480.target,"listSelectorRow")){
return;
}
if(_47f.onChoose){
_47f.onChoose(_47f.data[_480.target._idx].value);
}
_47f.hide();
});
this.connect(this.domNode,"onmousedown",function(_481){
if(!dojo.hasClass(_481.target,"listSelectorRow")){
return;
}
dojo.addClass(_481.target,"listSelectorRow-selected");
});
this.connect(this.domNode,"onmouseup",function(_482){
if(!dojo.hasClass(_482.target,"listSelectorRow")){
return;
}
dojo.removeClass(_482.target,"listSelectorRow-selected");
});
this.connect(this.domNode,"onmouseout",function(_483){
if(!dojo.hasClass(_483.target,"listSelectorRow")){
return;
}
dojo.removeClass(_483.target,"listSelectorRow-selected");
});
var _484=this.controller.getWindowSize();
this.mask=dojo.create("div",{"class":"dialogUnderlayWrapper",innerHTML:"<div class=\"dialogUnderlay\"></div>"},this.controller.assistant.domNode);
this.connect(this.mask,"onclick",function(){
_47f.onChoose&&_47f.onChoose();
_47f.hide();
});
},show:function(_485){
var _486;
var _487=this.controller.getWindowSize();
var _488;
if(_485){
_488=dojo._abs(_485);
_486=_488;
}else{
_486.x=_487.w/2;
_486.y=200;
}
console.log("startPos = ",_486);
dojo.style(this.domNode,{opacity:0,display:"",width:Math.floor(_487.w*0.8)+"px"});
var _489=0;
dojo.query(">",this.domNode).forEach(function(node){
dojo.style(node,{"float":"left"});
_489=Math.max(_489,dojo.marginBox(node).w);
dojo.style(node,{"float":"none"});
});
_489=Math.min(_489,Math.round(_487.w*0.8))+dojo.style(this.domNode,"paddingLeft")+dojo.style(this.domNode,"paddingRight")+1;
dojo.style(this.domNode,"width",_489+"px");
var _48a=dojo.marginBox(this.domNode).h;
var _48b=this;
var _48c=_488?Math.max(30,_488.y-_48a-10):this.getScroll().y+30;
console.log("fromNodePos = ",_488," targetHeight = ",_48a," targetY = "+_48c," startPos ",_486);
var _48d=dojo.animateProperty({node:this.domNode,duration:400,properties:{width:{start:1,end:_489},height:{start:1,end:_48a},top:{start:_486.y,end:_48c},left:{start:_486.x,end:(_487.w/2-_489/2)},opacity:{start:0,end:1},fontSize:{start:1}},onEnd:function(){
dojo.style(_48b.domNode,"width","inherit");
}});
var _48e=dojo.fadeIn({node:this.mask,duration:400});
dojo.fx.combine([_48d,_48e]).play();
},hide:function(){
var _48f=this;
var _490=dojo.animateProperty({node:this.domNode,duration:500,properties:{width:{end:1},height:{end:1},opacity:{end:0},fontSize:{end:1}},onEnd:function(){
if(_48f.get("destroyOnHide")){
_48f.destroy();
}
}});
var _491=dojo.fadeOut({node:this.mask,duration:400});
dojo.fx.combine([_490,_491]).play();
},render:function(){
dojo.empty(this.domNode);
dojo.style(this.domNode,"opacity",0);
var row;
for(var i=0;i<this.data.length;i++){
row=dojo.create("div",{"class":"listSelectorRow "+(this.data[i].className||""),innerHTML:this.data[i].label},this.domNode);
row._idx=i;
if(i==0){
dojo.addClass(row,"first");
}
if(i==this.data.length-1){
dojo.addClass(row,"last");
}
}
},destroy:function(){
this.inherited(arguments);
dojo.destroy(this.mask);
}});
});
},"dojox/mobile/EdgeToEdgeCategory":function(){
define("dojox/mobile/EdgeToEdgeCategory",["dojo/_base/declare","./RoundRectCategory"],function(_492,_493){
return _492("dojox.mobile.EdgeToEdgeCategory",_493,{buildRendering:function(){
this.inherited(arguments);
this.domNode.className="mblEdgeToEdgeCategory";
}});
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
lang.getObject("string",true,dojo);
dojo.string.rep=function(str,num){
if(num<=0||!str){
return "";
}
var buf=[];
for(;;){
if(num&1){
buf.push(str);
}
if(!(num>>=1)){
break;
}
str+=str;
}
return buf.join("");
};
dojo.string.pad=function(text,size,ch,end){
if(!ch){
ch="0";
}
var out=String(text),pad=dojo.string.rep(ch,Math.ceil((size-out.length)/ch.length));
return end?out+pad:pad+out;
};
dojo.string.substitute=function(_494,map,_495,_496){
_496=_496||dojo.global;
_495=_495?lang.hitch(_496,_495):function(v){
return v;
};
return _494.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_497,key,_498){
var _499=lang.getObject(key,false,map);
if(_498){
_499=lang.getObject(_498,false,_496).call(_496,_499,key);
}
return _495(_499,key).toString();
});
};
dojo.string.trim=String.prototype.trim?lang.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return dojo.string;
});
},"dojo/domReady":function(){
define(["./has"],function(has){
var _49a=this,doc=document,_49b={"loaded":1,"complete":1},_49c=typeof doc.readyState!="string",_49d=!!_49b[doc.readyState];
if(_49c){
doc.readyState="loading";
}
if(!_49d){
var _49e=[],_49f=[],_4a0=function(evt){
evt=evt||_49a.event;
if(_49d||(evt.type=="readystatechange"&&!_49b[doc.readyState])){
return;
}
_49d=1;
if(_49c){
doc.readyState="complete";
}
while(_49e.length){
(_49e.shift())();
}
},on=function(node,_4a1){
node.addEventListener(_4a1,_4a0,false);
_49e.push(function(){
node.removeEventListener(_4a1,_4a0,false);
});
};
if(!has("dom-addeventlistener")){
on=function(node,_4a2){
_4a2="on"+_4a2;
node.attachEvent(_4a2,_4a0);
_49e.push(function(){
node.detachEvent(_4a2,_4a0);
});
};
var div=doc.createElement("div");
try{
if(div.doScroll&&_49a.frameElement===null){
_49f.push(function(){
try{
div.doScroll("left");
return 1;
}
catch(e){
}
});
}
}
catch(e){
}
}
on(doc,"DOMContentLoaded");
on(_49a,"load");
if("onreadystatechange" in doc){
on(doc,"readystatechange");
}else{
if(!_49c){
_49f.push(function(){
return _49b[doc.readyState];
});
}
}
if(_49f.length){
var _4a3=function(){
if(_49d){
return;
}
var i=_49f.length;
while(i--){
if(_49f[i]()){
_4a0("poller");
return;
}
}
setTimeout(_4a3,30);
};
_4a3();
}
}
function _4a4(_4a5){
if(_49d){
_4a5(1);
}else{
_49e.push(_4a5);
}
};
_4a4.load=function(id,req,load){
_4a4(load);
};
return _4a4;
});
},"dojox/mobile/TextBox":function(){
define("dojox/mobile/TextBox",["dojo/_base/declare","dojo/dom-construct","dijit/_WidgetBase","dijit/form/_FormValueMixin","dijit/form/_TextBoxMixin"],function(_4a6,_4a7,_4a8,_4a9,_4aa){
return _4a6("dojox.mobile.TextBox",[_4a8,_4a9,_4aa],{baseClass:"mblTextBox",_setTypeAttr:null,_setPlaceHolderAttr:"textbox",buildRendering:function(){
if(!this.srcNodeRef){
this.srcNodeRef=_4a7.create("input",{"type":this.type});
}
this.inherited(arguments);
this.textbox=this.focusNode=this.domNode;
},postCreate:function(){
this.inherited(arguments);
this.connect(this.textbox,"onfocus","_onFocus");
this.connect(this.textbox,"onblur","_onBlur");
}});
});
},"dojo/dom-prop":function(){
define("dojo/dom-prop",["exports","./_base/kernel","./_base/sniff","./_base/lang","./dom","./dom-style","./dom-construct","./_base/connect"],function(_4ab,dojo,has,lang,dom,_4ac,ctr,conn){
var _4ad={},_4ae=0,_4af=dojo._scopeName+"attrid";
var _4b0={col:1,colgroup:1,table:1,tbody:1,tfoot:1,thead:1,tr:1,title:1};
_4ab.names={"class":"className","for":"htmlFor",tabindex:"tabIndex",readonly:"readOnly",colspan:"colSpan",frameborder:"frameBorder",rowspan:"rowSpan",valuetype:"valueType"};
_4ab.get=function getProp(node,name){
node=dom.byId(node);
var lc=name.toLowerCase(),_4b1=_4ab.names[lc]||name;
return node[_4b1];
};
_4ab.set=function setProp(node,name,_4b2){
node=dom.byId(node);
var l=arguments.length;
if(l==2&&typeof name!="string"){
for(var x in name){
_4ab.set(node,x,name[x]);
}
return node;
}
var lc=name.toLowerCase(),_4b3=_4ab.names[lc]||name;
if(_4b3=="style"&&typeof _4b2!="string"){
_4ac.style(node,_4b2);
return node;
}
if(_4b3=="innerHTML"){
if(has("ie")&&node.tagName.toLowerCase() in _4b0){
ctr.empty(node);
node.appendChild(ctr.toDom(_4b2,node.ownerDocument));
}else{
node[_4b3]=_4b2;
}
return node;
}
if(lang.isFunction(_4b2)){
var _4b4=node[_4af];
if(!_4b4){
_4b4=_4ae++;
node[_4af]=_4b4;
}
if(!_4ad[_4b4]){
_4ad[_4b4]={};
}
var h=_4ad[_4b4][_4b3];
if(h){
conn.disconnect(h);
}else{
try{
delete node[_4b3];
}
catch(e){
}
}
if(_4b2){
_4ad[_4b4][_4b3]=conn.connect(node,_4b3,_4b2);
}else{
node[_4b3]=null;
}
return node;
}
node[_4b3]=_4b2;
return node;
};
});
},"dojo/keys":function(){
define("dojo/keys",["./_base/kernel","./_base/sniff"],function(dojo,has){
return dojo.keys={BACKSPACE:8,TAB:9,CLEAR:12,ENTER:13,SHIFT:16,CTRL:17,ALT:18,META:has("safari")?91:224,PAUSE:19,CAPS_LOCK:20,ESCAPE:27,SPACE:32,PAGE_UP:33,PAGE_DOWN:34,END:35,HOME:36,LEFT_ARROW:37,UP_ARROW:38,RIGHT_ARROW:39,DOWN_ARROW:40,INSERT:45,DELETE:46,HELP:47,LEFT_WINDOW:91,RIGHT_WINDOW:92,SELECT:93,NUMPAD_0:96,NUMPAD_1:97,NUMPAD_2:98,NUMPAD_3:99,NUMPAD_4:100,NUMPAD_5:101,NUMPAD_6:102,NUMPAD_7:103,NUMPAD_8:104,NUMPAD_9:105,NUMPAD_MULTIPLY:106,NUMPAD_PLUS:107,NUMPAD_ENTER:108,NUMPAD_MINUS:109,NUMPAD_PERIOD:110,NUMPAD_DIVIDE:111,F1:112,F2:113,F3:114,F4:115,F5:116,F6:117,F7:118,F8:119,F9:120,F10:121,F11:122,F12:123,F13:124,F14:125,F15:126,NUM_LOCK:144,SCROLL_LOCK:145,UP_DPAD:175,DOWN_DPAD:176,LEFT_DPAD:177,RIGHT_DPAD:178,copyKey:has("mac")&&!has("air")?(has("safari")?91:224):17};
});
},"dojo/_base/lang":function(){
define(["./kernel","../has","./sniff"],function(dojo,has){
has.add("bug-for-in-skips-shadowed",function(){
for(var i in {toString:1}){
return 0;
}
return 1;
});
var _4b5=has("bug-for-in-skips-shadowed")?"hasOwnProperty.valueOf.isPrototypeOf.propertyIsEnumerable.toLocaleString.toString.constructor".split("."):[],_4b6=_4b5.length,_4b7=function(dest,_4b8,_4b9){
var name,s,i,_4ba={};
for(name in _4b8){
s=_4b8[name];
if(!(name in dest)||(dest[name]!==s&&(!(name in _4ba)||_4ba[name]!==s))){
dest[name]=_4b9?_4b9(s):s;
}
}
if(has("bug-for-in-skips-shadowed")){
if(_4b8){
for(i=0;i<_4b6;++i){
name=_4b5[i];
s=_4b8[name];
if(!(name in dest)||(dest[name]!==s&&(!(name in _4ba)||_4ba[name]!==s))){
dest[name]=_4b9?_4b9(s):s;
}
}
}
}
return dest;
},_4bb=function(dest,_4bc){
if(!dest){
dest={};
}
for(var i=1,l=arguments.length;i<l;i++){
lang._mixin(dest,arguments[i]);
}
return dest;
},_4bd=function(_4be,_4bf,_4c0){
var p,i=0,_4c1=dojo.global;
if(!_4c0){
if(!_4be.length){
return _4c1;
}else{
p=_4be[i++];
try{
_4c0=dojo.scopeMap[p]&&dojo.scopeMap[p][1];
}
catch(e){
}
_4c0=_4c0||(p in _4c1?_4c1[p]:(_4bf?_4c1[p]={}:undefined));
}
}
while(_4c0&&(p=_4be[i++])){
_4c0=(p in _4c0?_4c0[p]:(_4bf?_4c0[p]={}:undefined));
}
return _4c0;
},_4c2=function(name,_4c3,_4c4){
var _4c5=name.split("."),p=_4c5.pop(),obj=_4bd(_4c5,true,_4c4);
return obj&&p?(obj[p]=_4c3):undefined;
},_4c6=function(name,_4c7,_4c8){
return _4bd(name.split("."),_4c7,_4c8);
},_4c9=function(name,obj){
return lang.getObject(name,false,obj)!==undefined;
},opts=Object.prototype.toString,_4ca=function(it){
return (typeof it=="string"||it instanceof String);
},_4cb=function(it){
return it&&(it instanceof Array||typeof it=="array");
},_4cc=function(it){
return opts.call(it)==="[object Function]";
},_4cd=function(it){
return it!==undefined&&(it===null||typeof it=="object"||lang.isArray(it)||lang.isFunction(it));
},_4ce=function(it){
return it&&it!==undefined&&!lang.isString(it)&&!lang.isFunction(it)&&!(it.tagName&&it.tagName.toLowerCase()=="form")&&(lang.isArray(it)||isFinite(it.length));
},_4cf=function(it){
return it&&!lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));
},_4d0=function(_4d1,_4d2){
for(var i=1,l=arguments.length;i<l;i++){
lang._mixin(_4d1.prototype,arguments[i]);
}
return _4d1;
},_4d3=function(_4d4,_4d5){
var pre=_4d6(arguments,2);
var _4d7=lang.isString(_4d5);
return function(){
var args=_4d6(arguments);
var f=_4d7?(_4d4||dojo.global)[_4d5]:_4d5;
return f&&f.apply(_4d4||this,pre.concat(args));
};
},_4d8=function(_4d9,_4da){
if(arguments.length>2){
return lang._hitchArgs.apply(dojo,arguments);
}
if(!_4da){
_4da=_4d9;
_4d9=null;
}
if(lang.isString(_4da)){
_4d9=_4d9||dojo.global;
if(!_4d9[_4da]){
throw (["dojo.hitch: scope[\"",_4da,"\"] is null (scope=\"",_4d9,"\")"].join(""));
}
return function(){
return _4d9[_4da].apply(_4d9,arguments||[]);
};
}
return !_4d9?_4da:function(){
return _4da.apply(_4d9,arguments||[]);
};
},_4db=(function(){
function TMP(){
};
return function(obj,_4dc){
TMP.prototype=obj;
var tmp=new TMP();
TMP.prototype=null;
if(_4dc){
lang._mixin(tmp,_4dc);
}
return tmp;
};
})(),_4dd=function(obj,_4de,_4df){
return (_4df||[]).concat(Array.prototype.slice.call(obj,_4de||0));
},_4d6=has("ie")?(function(){
function slow(obj,_4e0,_4e1){
var arr=_4e1||[];
for(var x=_4e0||0;x<obj.length;x++){
arr.push(obj[x]);
}
return arr;
};
return function(obj){
return ((obj.item)?slow:_4dd).apply(this,arguments);
};
})():_4dd,_4e2=function(_4e3){
var arr=[null];
return lang.hitch.apply(dojo,arr.concat(lang._toArray(arguments)));
},_4e4=function(src){
if(!src||typeof src!="object"||lang.isFunction(src)){
return src;
}
if(src.nodeType&&"cloneNode" in src){
return src.cloneNode(true);
}
if(src instanceof Date){
return new Date(src.getTime());
}
if(src instanceof RegExp){
return new RegExp(src);
}
var r,i,l;
if(lang.isArray(src)){
r=[];
for(i=0,l=src.length;i<l;++i){
if(i in src){
r.push(_4e4(src[i]));
}
}
}else{
r=src.constructor?new src.constructor():{};
}
return lang._mixin(r,src,_4e4);
},trim=String.prototype.trim?function(str){
return str.trim();
}:function(str){
return str.replace(/^\s\s*/,"").replace(/\s\s*$/,"");
},_4e5=/\{([^\}]+)\}/g,_4e6=function(tmpl,map,_4e7){
return tmpl.replace(_4e7||_4e5,lang.isFunction(map)?map:function(_4e8,k){
return _4c6(k,false,map);
});
},lang={_extraNames:_4b5,_mixin:_4b7,mixin:_4bb,setObject:_4c2,getObject:_4c6,exists:_4c9,isString:_4ca,isArray:_4cb,isFunction:_4cc,isObject:_4cd,isArrayLike:_4ce,isAlien:_4cf,extend:_4d0,_hitchArgs:_4d3,hitch:_4d8,delegate:_4db,_toArray:_4d6,partial:_4e2,clone:_4e4,trim:trim,replace:_4e6};
1&&_4bb(dojo,lang);
return lang;
});
},"dojox/mobile/TextArea":function(){
define("dojox/mobile/TextArea",["dojo/_base/declare","dojo/dom-construct","./TextBox"],function(_4e9,_4ea,_4eb){
return _4e9("dojox.mobile.TextArea",_4eb,{baseClass:"mblTextArea",postMixInProperties:function(){
if(!this.value&&this.srcNodeRef){
this.value=this.srcNodeRef.value;
}
this.inherited(arguments);
},buildRendering:function(){
if(!this.srcNodeRef){
this.srcNodeRef=_4ea.create("textarea",{});
}
this.inherited(arguments);
}});
});
},"dijit/registry":function(){
define("dijit/registry",["dojo/_base/array","dojo/_base/sniff","dojo/_base/unload","dojo/_base/window","."],function(_4ec,has,_4ed,win,_4ee){
var _4ef={},hash={};
var _4f0={length:0,add:function(_4f1){
if(hash[_4f1.id]){
throw new Error("Tried to register widget with id=="+_4f1.id+" but that id is already registered");
}
hash[_4f1.id]=_4f1;
this.length++;
},remove:function(id){
if(hash[id]){
delete hash[id];
this.length--;
}
},byId:function(id){
return typeof id=="string"?hash[id]:id;
},byNode:function(node){
return hash[node.getAttribute("widgetId")];
},toArray:function(){
var ar=[];
for(var id in hash){
ar.push(hash[id]);
}
return ar;
},getUniqueId:function(_4f2){
var id;
do{
id=_4f2+"_"+(_4f2 in _4ef?++_4ef[_4f2]:_4ef[_4f2]=0);
}while(hash[id]);
return _4ee._scopeName=="dijit"?id:_4ee._scopeName+"_"+id;
},findWidgets:function(root){
var _4f3=[];
function _4f4(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _4f5=node.getAttribute("widgetId");
if(_4f5){
var _4f6=hash[_4f5];
if(_4f6){
_4f3.push(_4f6);
}
}else{
_4f4(node);
}
}
}
};
_4f4(root);
return _4f3;
},_destroyAll:function(){
_4ee._curFocus=null;
_4ee._prevFocus=null;
_4ee._activeStack=[];
_4ec.forEach(_4f0.findWidgets(win.body()),function(_4f7){
if(!_4f7._destroyed){
if(_4f7.destroyRecursive){
_4f7.destroyRecursive();
}else{
if(_4f7.destroy){
_4f7.destroy();
}
}
}
});
},getEnclosingWidget:function(node){
while(node){
var id=node.getAttribute&&node.getAttribute("widgetId");
if(id){
return hash[id];
}
node=node.parentNode;
}
return null;
},_hash:hash};
if(has("ie")){
_4ed.addOnWindowUnload(function(){
_4f0._destroyAll();
});
}
_4ee.registry=_4f0;
return _4f0;
});
},"dijit/_base/manager":function(){
define("dijit/_base/manager",["dojo/_base/array","dojo/_base/config","../registry",".."],function(_4f8,_4f9,_4fa,_4fb){
_4f8.forEach(["byId","getUniqueId","findWidgets","_destroyAll","byNode","getEnclosingWidget"],function(name){
_4fb[name]=_4fa[name];
});
_4fb.defaultDuration=_4f9["defaultDuration"]||200;
return _4fb;
});
},"dijit/_base/place":function(){
define("dijit/_base/place",["dojo/_base/array","dojo/_base/lang","dojo/window","../place",".."],function(_4fc,lang,_4fd,_4fe,_4ff){
_4ff.getViewport=function(){
return _4fd.getBox();
};
_4ff.placeOnScreen=_4fe.at;
_4ff.placeOnScreenAroundElement=function(node,_500,_501,_502){
var _503;
if(lang.isArray(_501)){
_503=_501;
}else{
_503=[];
for(var key in _501){
_503.push({aroundCorner:key,corner:_501[key]});
}
}
return _4fe.around(node,_500,_503,true,_502);
};
_4ff.placeOnScreenAroundNode=_4ff.placeOnScreenAroundElement;
_4ff.placeOnScreenAroundRectangle=_4ff.placeOnScreenAroundElement;
_4ff.getPopupAroundAlignment=function(_504,_505){
var _506={};
_4fc.forEach(_504,function(pos){
var ltr=_505;
switch(pos){
case "after":
_506[_505?"BR":"BL"]=_505?"BL":"BR";
break;
case "before":
_506[_505?"BL":"BR"]=_505?"BR":"BL";
break;
case "below-alt":
ltr=!ltr;
case "below":
_506[ltr?"BL":"BR"]=ltr?"TL":"TR";
_506[ltr?"BR":"BL"]=ltr?"TR":"TL";
break;
case "above-alt":
ltr=!ltr;
case "above":
default:
_506[ltr?"TL":"TR"]=ltr?"BL":"BR";
_506[ltr?"TR":"TL"]=ltr?"BR":"BL";
break;
}
});
return _506;
};
return _4ff;
});
},"dojox/mobile/View":function(){
define("dojox/mobile/View",["dojo/_base/kernel","dojo/_base/array","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/_base/sniff","dojo/_base/window","dojo/_base/Deferred","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dijit/registry","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./ViewController","./transition"],function(dojo,_507,_508,_509,_50a,lang,has,win,_50b,dom,_50c,_50d,_50e,_50f,_510,_511,_512,_513,_514){
var dm=lang.getObject("dojox.mobile",true);
return _50a("dojox.mobile.View",[_512,_511,_510],{selected:false,keepScrollPos:true,constructor:function(_515,node){
if(node){
dom.byId(node).style.visibility="hidden";
}
this._aw=has("android")>=2.2&&has("android")<3;
},buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("DIV");
this.domNode.className="mblView";
this.connect(this.domNode,"webkitAnimationEnd","onAnimationEnd");
this.connect(this.domNode,"webkitAnimationStart","onAnimationStart");
if(!_508["mblCSS3Transition"]){
this.connect(this.domNode,"webkitTransitionEnd","onAnimationEnd");
}
var id=location.href.match(/#(\w+)([^\w=]|$)/)?RegExp.$1:null;
this._visible=this.selected&&!id||this.id==id;
if(this.selected){
dm._defaultView=this;
}
},startup:function(){
if(this._started){
return;
}
var _516=[];
var _517=this.domNode.parentNode.childNodes;
var _518=false;
for(var i=0;i<_517.length;i++){
var c=_517[i];
if(c.nodeType===1&&_50c.contains(c,"mblView")){
_516.push(c);
_518=_518||_50f.byNode(c)._visible;
}
}
var _519=this._visible;
if(_516.length===1||(!_518&&_516[0]===this.domNode)){
_519=true;
}
var _51a=this;
setTimeout(function(){
if(!_519){
_51a.domNode.style.display="none";
}else{
dm.currentView=_51a;
_51a.onStartView();
_509.publish("/dojox/mobile/startView",[_51a]);
}
if(_51a.domNode.style.visibility!="visible"){
_51a.domNode.style.visibility="visible";
}
var _51b=_51a.getParent&&_51a.getParent();
if(!_51b||!_51b.resize){
_51a.resize();
}
},has("ie")?100:0);
this.inherited(arguments);
},resize:function(){
_507.forEach(this.getChildren(),function(_51c){
if(_51c.resize){
_51c.resize();
}
});
},onStartView:function(){
},onBeforeTransitionIn:function(_51d,dir,_51e,_51f,_520){
},onAfterTransitionIn:function(_521,dir,_522,_523,_524){
},onBeforeTransitionOut:function(_525,dir,_526,_527,_528){
},onAfterTransitionOut:function(_529,dir,_52a,_52b,_52c){
},_saveState:function(_52d,dir,_52e,_52f,_530){
this._context=_52f;
this._method=_530;
if(_52e=="none"){
_52e=null;
}
this._moveTo=_52d;
this._dir=dir;
this._transition=_52e;
this._arguments=lang._toArray(arguments);
this._args=[];
if(_52f||_530){
for(var i=5;i<arguments.length;i++){
this._args.push(arguments[i]);
}
}
},_fixViewState:function(_531){
var _532=this.domNode.parentNode.childNodes;
for(var i=0;i<_532.length;i++){
var n=_532[i];
if(n.nodeType===1&&_50c.contains(n,"mblView")){
n.className="mblView";
}
}
_531.className="mblView";
},convertToId:function(_533){
if(typeof (_533)=="string"){
_533.match(/^#?([^&?]+)/);
return RegExp.$1;
}
return _533;
},performTransition:function(_534,dir,_535,_536,_537){
if(_534==="#"){
return;
}
if(dojo.hash){
if(typeof (_534)=="string"&&_534.charAt(0)=="#"&&!dm._params){
dm._params=[];
for(var i=0;i<arguments.length;i++){
dm._params.push(arguments[i]);
}
dojo.hash(_534);
return;
}
}
this._saveState.apply(this,arguments);
var _538;
if(_534){
_538=this.convertToId(_534);
}else{
if(!this._dummyNode){
this._dummyNode=win.doc.createElement("DIV");
win.body().appendChild(this._dummyNode);
}
_538=this._dummyNode;
}
var _539=this.domNode;
var _53a=_539.offsetTop;
_538=this.toNode=dom.byId(_538);
if(!_538){
console.log("dojox.mobile.View#performTransition: destination view not found: "+_534);
return;
}
_538.style.visibility=this._aw?"visible":"hidden";
_538.style.display="";
this._fixViewState(_538);
var _53b=_50f.byNode(_538);
if(_53b){
if(_508["mblAlwaysResizeOnTransition"]||!_53b._resized){
dm.resizeAll(null,_53b);
_53b._resized=true;
}
if(_535&&_535!="none"){
_53b.containerNode.style.paddingTop=_53a+"px";
}
_53b.movedFrom=_539.id;
}
this.onBeforeTransitionOut.apply(this,arguments);
_509.publish("/dojox/mobile/beforeTransitionOut",[this].concat(lang._toArray(arguments)));
if(_53b){
if(this.keepScrollPos&&!this.getParent()){
var _53c=win.body().scrollTop||win.doc.documentElement.scrollTop||win.global.pageYOffset||0;
_539._scrollTop=_53c;
var _53d=(dir==1)?0:(_538._scrollTop||0);
_538.style.top="0px";
if(_53c>1||_53d!==0){
_539.style.top=_53d-_53c+"px";
if(_508["mblHideAddressBar"]!==false){
setTimeout(function(){
win.global.scrollTo(0,(_53d||1));
},0);
}
}
}else{
_538.style.top="0px";
}
_53b.onBeforeTransitionIn.apply(_53b,arguments);
_509.publish("/dojox/mobile/beforeTransitionIn",[_53b].concat(lang._toArray(arguments)));
}
if(!this._aw){
_538.style.display="none";
_538.style.visibility="visible";
}
if(dm._iw&&dm.scrollable){
var ss=dm.getScreenSize();
win.body().appendChild(dm._iwBgCover);
_50e.set(dm._iwBgCover,{position:"absolute",top:"0px",left:"0px",height:(ss.h+1)+"px",width:ss.w+"px",backgroundColor:_50e.get(win.body(),"background-color"),zIndex:-10000,display:""});
_50e.set(_538,{position:"absolute",zIndex:-10001,visibility:"visible",display:""});
setTimeout(lang.hitch(this,function(){
this._doTransition(_539,_538,_535,dir);
}),80);
}else{
this._doTransition(_539,_538,_535,dir);
}
},_toCls:function(s){
return "mbl"+s.charAt(0).toUpperCase()+s.substring(1);
},_doTransition:function(_53e,_53f,_540,dir){
var rev=(dir==-1)?" mblReverse":"";
if(dm._iw&&dm.scrollable){
_50e.set(_53f,{position:"",zIndex:""});
win.body().removeChild(dm._iwBgCover);
}else{
if(!this._aw){
_53f.style.display="";
}
}
if(!_540||_540=="none"){
this.domNode.style.display="none";
this.invokeCallback();
}else{
if(_508["mblCSS3Transition"]){
_50b.when(_514,lang.hitch(this,function(_541){
var _542=_50e.get(_53f,"position");
_50e.set(_53f,"position","absolute");
_50b.when(_541(_53e,_53f,{transition:_540,reverse:(dir===-1)?true:false}),lang.hitch(this,function(){
_50e.set(_53f,"position",_542);
this.invokeCallback();
}));
}));
}else{
var s=this._toCls(_540);
_50c.add(_53e,s+" mblOut"+rev);
_50c.add(_53f,s+" mblIn"+rev);
setTimeout(function(){
_50c.add(_53e,"mblTransition");
_50c.add(_53f,"mblTransition");
},100);
var _543="50% 50%";
var _544="50% 50%";
var _545,posX,posY;
if(_540.indexOf("swirl")!=-1||_540.indexOf("zoom")!=-1){
if(this.keepScrollPos&&!this.getParent()){
_545=win.body().scrollTop||win.doc.documentElement.scrollTop||win.global.pageYOffset||0;
}else{
_545=-_50d.position(_53e,true).y;
}
posY=win.global.innerHeight/2+_545;
_543="50% "+posY+"px";
_544="50% "+posY+"px";
}else{
if(_540.indexOf("scale")!=-1){
var _546=_50d.position(_53e,true);
posX=((this.clickedPosX!==undefined)?this.clickedPosX:win.global.innerWidth/2)-_546.x;
if(this.keepScrollPos&&!this.getParent()){
_545=win.body().scrollTop||win.doc.documentElement.scrollTop||win.global.pageYOffset||0;
}else{
_545=-_546.y;
}
posY=((this.clickedPosY!==undefined)?this.clickedPosY:win.global.innerHeight/2)+_545;
_543=posX+"px "+posY+"px";
_544=posX+"px "+posY+"px";
}
}
_50e.set(_53e,{webkitTransformOrigin:_543});
_50e.set(_53f,{webkitTransformOrigin:_544});
}
}
dm.currentView=_50f.byNode(_53f);
},onAnimationStart:function(e){
},onAnimationEnd:function(e){
var name=e.animationName||e.target.className;
if(name.indexOf("Out")===-1&&name.indexOf("In")===-1&&name.indexOf("Shrink")===-1){
return;
}
var _547=false;
if(_50c.contains(this.domNode,"mblOut")){
_547=true;
this.domNode.style.display="none";
_50c.remove(this.domNode,[this._toCls(this._transition),"mblIn","mblOut","mblReverse"]);
}else{
this.containerNode.style.paddingTop="";
}
_50e.set(this.domNode,{webkitTransformOrigin:""});
if(name.indexOf("Shrink")!==-1){
var li=e.target;
li.style.display="none";
_50c.remove(li,"mblCloseContent");
}
if(_547){
this.invokeCallback();
}
this.domNode&&(this.domNode.className="mblView");
this.clickedPosX=this.clickedPosY=undefined;
},invokeCallback:function(){
this.onAfterTransitionOut.apply(this,this._arguments);
_509.publish("/dojox/mobile/afterTransitionOut",[this].concat(this._arguments));
var _548=_50f.byNode(this.toNode);
if(_548){
_548.onAfterTransitionIn.apply(_548,this._arguments);
_509.publish("/dojox/mobile/afterTransitionIn",[_548].concat(this._arguments));
_548.movedFrom=undefined;
}
var c=this._context,m=this._method;
if(!c&&!m){
return;
}
if(!m){
m=c;
c=null;
}
c=c||win.global;
if(typeof (m)=="string"){
c[m].apply(c,this._args);
}else{
m.apply(c,this._args);
}
},getShowingView:function(){
var _549=this.domNode.parentNode.childNodes;
for(var i=0;i<_549.length;i++){
var n=_549[i];
if(n.nodeType===1&&_50c.contains(n,"mblView")&&_50e.get(n,"display")!=="none"){
return _50f.byNode(n);
}
}
return null;
},show:function(){
var view=this.getShowingView();
if(view){
view.domNode.style.display="none";
}
this.domNode.style.display="";
dm.currentView=this;
}});
});
},"dijit/form/_ExpandingTextAreaMixin":function(){
define("dijit/form/_ExpandingTextAreaMixin",["dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/_base/window"],function(_54a,_54b,lang,win){
var _54c;
return _54a("dijit.form._ExpandingTextAreaMixin",null,{_setValueAttr:function(){
this.inherited(arguments);
this.resize();
},postCreate:function(){
this.inherited(arguments);
var _54d=this.textbox;
if(_54c==undefined){
var te=_54b.create("textarea",{rows:"5",cols:"20",value:" ",style:{zoom:1,overflow:"hidden",visibility:"hidden",position:"absolute",border:"0px solid black",padding:"0px"}},win.body(),"last");
_54c=te.scrollHeight>=te.clientHeight;
win.body().removeChild(te);
}
this.connect(_54d,"onscroll","_resizeLater");
this.connect(_54d,"onresize","_resizeLater");
this.connect(_54d,"onfocus","_resizeLater");
_54d.style.overflowY="hidden";
this._estimateHeight();
this._resizeLater();
},_onInput:function(e){
this.inherited(arguments);
this.resize();
},_estimateHeight:function(){
var _54e=this.textbox;
_54e.style.height="auto";
_54e.rows=(_54e.value.match(/\n/g)||[]).length+2;
},_resizeLater:function(){
setTimeout(lang.hitch(this,"resize"),0);
},resize:function(){
function _54f(){
var _550=false;
if(_551.value===""){
_551.value=" ";
_550=true;
}
var sh=_551.scrollHeight;
if(_550){
_551.value="";
}
return sh;
};
var _551=this.textbox;
if(_551.style.overflowY=="hidden"){
_551.scrollTop=0;
}
if(this.resizeTimer){
clearTimeout(this.resizeTimer);
}
this.resizeTimer=null;
if(this.busyResizing){
return;
}
this.busyResizing=true;
if(_54f()||_551.offsetHeight){
var _552=_551.style.height;
if(!(/px/.test(_552))){
_552=_54f();
_551.rows=1;
_551.style.height=_552+"px";
}
var newH=Math.max(parseInt(_552)-_551.clientHeight,0)+_54f();
var _553=newH+"px";
if(_553!=_551.style.height){
_551.rows=1;
_551.style.height=_553;
}
if(_54c){
var _554=_54f();
_551.style.height="auto";
if(_54f()<_554){
_553=newH-_554+_54f()+"px";
}
_551.style.height=_553;
}
_551.style.overflowY=_54f()>_551.clientHeight?"auto":"hidden";
}else{
this._estimateHeight();
}
this.busyResizing=false;
},destroy:function(){
if(this.resizeTimer){
clearTimeout(this.resizeTimer);
}
if(this.shrinkTimer){
clearTimeout(this.shrinkTimer);
}
this.inherited(arguments);
}});
});
},"dojo/_base/Color":function(){
define(["./kernel","./lang","./array","./config"],function(dojo,lang,_555,_556){
var _557=dojo.Color=function(_558){
if(_558){
this.setColor(_558);
}
};
_557.named={"black":[0,0,0],"silver":[192,192,192],"gray":[128,128,128],"white":[255,255,255],"maroon":[128,0,0],"red":[255,0,0],"purple":[128,0,128],"fuchsia":[255,0,255],"green":[0,128,0],"lime":[0,255,0],"olive":[128,128,0],"yellow":[255,255,0],"navy":[0,0,128],"blue":[0,0,255],"teal":[0,128,128],"aqua":[0,255,255],"transparent":_556.transparentColor||[0,0,0,0]};
lang.extend(_557,{r:255,g:255,b:255,a:1,_set:function(r,g,b,a){
var t=this;
t.r=r;
t.g=g;
t.b=b;
t.a=a;
},setColor:function(_559){
if(lang.isString(_559)){
_557.fromString(_559,this);
}else{
if(lang.isArray(_559)){
_557.fromArray(_559,this);
}else{
this._set(_559.r,_559.g,_559.b,_559.a);
if(!(_559 instanceof _557)){
this.sanitize();
}
}
}
return this;
},sanitize:function(){
return this;
},toRgb:function(){
var t=this;
return [t.r,t.g,t.b];
},toRgba:function(){
var t=this;
return [t.r,t.g,t.b,t.a];
},toHex:function(){
var arr=_555.map(["r","g","b"],function(x){
var s=this[x].toString(16);
return s.length<2?"0"+s:s;
},this);
return "#"+arr.join("");
},toCss:function(_55a){
var t=this,rgb=t.r+", "+t.g+", "+t.b;
return (_55a?"rgba("+rgb+", "+t.a:"rgb("+rgb)+")";
},toString:function(){
return this.toCss(true);
}});
_557.blendColors=dojo.blendColors=function(_55b,end,_55c,obj){
var t=obj||new _557();
_555.forEach(["r","g","b","a"],function(x){
t[x]=_55b[x]+(end[x]-_55b[x])*_55c;
if(x!="a"){
t[x]=Math.round(t[x]);
}
});
return t.sanitize();
};
_557.fromRgb=dojo.colorFromRgb=function(_55d,obj){
var m=_55d.toLowerCase().match(/^rgba?\(([\s\.,0-9]+)\)/);
return m&&_557.fromArray(m[1].split(/\s*,\s*/),obj);
};
_557.fromHex=dojo.colorFromHex=function(_55e,obj){
var t=obj||new _557(),bits=(_55e.length==4)?4:8,mask=(1<<bits)-1;
_55e=Number("0x"+_55e.substr(1));
if(isNaN(_55e)){
return null;
}
_555.forEach(["b","g","r"],function(x){
var c=_55e&mask;
_55e>>=bits;
t[x]=bits==4?17*c:c;
});
t.a=1;
return t;
};
_557.fromArray=dojo.colorFromArray=function(a,obj){
var t=obj||new _557();
t._set(Number(a[0]),Number(a[1]),Number(a[2]),Number(a[3]));
if(isNaN(t.a)){
t.a=1;
}
return t.sanitize();
};
_557.fromString=dojo.colorFromString=function(str,obj){
var a=_557.named[str];
return a&&_557.fromArray(a,obj)||_557.fromRgb(str,obj)||_557.fromHex(str,obj);
};
return _557;
});
},"dojox/mobile/Slider":function(){
define("dojox/mobile/Slider",["dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dijit/_WidgetBase","dijit/form/_FormValueMixin"],function(_55f,_560,_561,lang,win,_562,_563,_564,_565,_566,_567){
return _561("dojox.mobile.Slider",[_566,_567],{value:0,min:0,max:100,step:1,baseClass:"mblSlider",flip:false,orientation:"auto",halo:"8pt",buildRendering:function(){
this.focusNode=this.domNode=_563.create("div",{});
this.valueNode=_563.create("input",(this.srcNodeRef&&this.srcNodeRef.name)?{type:"hidden",name:this.srcNodeRef.name}:{type:"hidden"},this.domNode,"last");
var _568=_563.create("div",{style:{position:"relative",height:"100%",width:"100%"}},this.domNode,"last");
this.progressBar=_563.create("div",{style:{position:"absolute"},"class":"mblSliderProgressBar"},_568,"last");
this.touchBox=_563.create("div",{style:{position:"absolute"},"class":"mblSliderTouchBox"},_568,"last");
this.handle=_563.create("div",{style:{position:"absolute"},"class":"mblSliderHandle"},_568,"last");
this.inherited(arguments);
},_setValueAttr:function(_569,_56a){
var _56b=(this.value-this.min)*100/(this.max-this.min);
this.valueNode.value=_569;
this.inherited(arguments);
if(!this._started){
return;
}
this.focusNode.setAttribute("aria-valuenow",_569);
var _56c=(_569-this.min)*100/(this.max-this.min);
var _56d=this.orientation!="V";
if(_56a===true){
_562.add(this.handle,"mblSliderTransition");
_562.add(this.progressBar,"mblSliderTransition");
}else{
_562.remove(this.handle,"mblSliderTransition");
_562.remove(this.progressBar,"mblSliderTransition");
}
_565.set(this.handle,this._attrs.handleLeft,(this._reversed?(100-_56c):_56c)+"%");
_565.set(this.progressBar,this._attrs.width,_56c+"%");
},postCreate:function(){
this.inherited(arguments);
function _56e(e){
function _56f(e){
_57b=_570?e[this._attrs.pageX]:(e.touches?e.touches[0][this._attrs.pageX]:e[this._attrs.clientX]);
_57c=_57b-_571;
_57c=Math.min(Math.max(_57c,0),_572);
var _573=this.step?((this.max-this.min)/this.step):_572;
if(_573<=1||_573==Infinity){
_573=_572;
}
var _574=Math.round(_57c*_573/_572);
_57a=(this.max-this.min)*_574/_573;
_57a=this._reversed?(this.max-_57a):(this.min+_57a);
};
function _575(e){
e.preventDefault();
lang.hitch(this,_56f)(e);
this.set("value",_57a,false);
};
function _576(e){
e.preventDefault();
_55f.forEach(_577,lang.hitch(this,"disconnect"));
_577=[];
this.set("value",this.value,true);
};
e.preventDefault();
var _570=e.type=="mousedown";
var box=_564.position(node,false);
var _578=_565.get(win.body(),"zoom")||1;
if(isNaN(_578)){
_578=1;
}
var _579=_565.get(node,"zoom")||1;
if(isNaN(_579)){
_579=1;
}
var _571=box[this._attrs.x]*_579*_578+_564.docScroll()[this._attrs.x];
var _572=box[this._attrs.w]*_579*_578;
lang.hitch(this,_56f)(e);
if(e.target==this.touchBox){
this.set("value",_57a,true);
}
_55f.forEach(_577,_560.disconnect);
var root=win.doc.documentElement;
var _577=[this.connect(root,_570?"onmousemove":"ontouchmove",_575),this.connect(root,_570?"onmouseup":"ontouchend",_576)];
};
var _57b,_57c,_57a;
var node=this.domNode;
if(this.orientation=="auto"){
this.orientation=node.offsetHeight<=node.offsetWidth?"H":"V";
}
_562.add(this.domNode,_55f.map(this.baseClass.split(" "),lang.hitch(this,function(c){
return c+this.orientation;
})));
var _57d=this.orientation!="V";
var ltr=_57d?this.isLeftToRight():false;
var flip=this.flip;
this._reversed=!(_57d&&((ltr&&!flip)||(!ltr&&flip)))||(!_57d&&!flip);
this._attrs=_57d?{x:"x",w:"w",l:"l",r:"r",pageX:"pageX",clientX:"clientX",handleLeft:"left",left:this._reversed?"right":"left",width:"width"}:{x:"y",w:"h",l:"t",r:"b",pageX:"pageY",clientX:"clientY",handleLeft:"top",left:this._reversed?"bottom":"top",width:"height"};
this.progressBar.style[this._attrs.left]="0px";
this.connect(this.touchBox,"touchstart",_56e);
this.connect(this.touchBox,"onmousedown",_56e);
this.connect(this.handle,"touchstart",_56e);
this.connect(this.handle,"onmousedown",_56e);
this.startup();
this.set("value",this.value);
}});
});
},"dojo/_base/loader":function(){
define(["./kernel","../has","require","module","./json","./lang","./array"],function(dojo,has,_57e,_57f,json,lang,_580){
if(!1){
console.error("cannot load the Dojo v1.x loader with a foreign loader");
return 0;
}
var _581=function(id){
return {src:_57f.id,id:id};
},_582=function(name){
return name.replace(/\./g,"/");
},_583=/\/\/>>built/,_584=[],_585=[],_586=function(mid,_587,_588){
_584.push(_588);
_580.forEach(mid.split(","),function(mid){
var _589=_58a(mid,_587.module);
_585.push(_589);
_58b(_589);
});
_58c();
},_58c=function(){
_585=_580.filter(_585,function(_58d){
return _58d.injected!==_5b8&&!_58d.executed;
});
if(!_585.length){
_58f.holdIdle();
var _58e=_584;
_584=[];
_580.forEach(_58e,function(cb){
cb(1);
});
_58f.releaseIdle();
}
},_590=function(mid,_591,_592){
_591([mid],function(_593){
_591(_593.names,function(){
for(var _594="",args=[],i=0;i<arguments.length;i++){
_594+="var "+_593.names[i]+"= arguments["+i+"]; ";
args.push(arguments[i]);
}
eval(_594);
var _595=_591.module,deps=[],hold={},_596=[],p,_597={provide:function(_598){
_598=_582(_598);
var _599=_58a(_598,_595);
if(_599!==_595){
_5bf(_599);
}
},require:function(_59a,_59b){
_59a=_582(_59a);
_59b&&(_58a(_59a,_595).result=_5b9);
_596.push(_59a);
},requireLocalization:function(_59c,_59d,_59e){
deps.length||(deps=["dojo/i18n"]);
_59e=(_59e||dojo.locale).toLowerCase();
_59c=_582(_59c)+"/nls/"+(/root/i.test(_59e)?"":_59e+"/")+_582(_59d);
if(_58a(_59c,_595).isXd){
deps.push("dojo/i18n!"+_59c);
}
},loadInit:function(f){
f();
}};
try{
for(p in _597){
hold[p]=dojo[p];
dojo[p]=_597[p];
}
_593.def.apply(null,args);
}
catch(e){
_5c0("error",[_581("failedDojoLoadInit"),e]);
}
finally{
for(p in _597){
dojo[p]=hold[p];
}
}
_596.length&&deps.push("dojo/require!"+_596.join(","));
_584.push(_592);
_580.forEach(_596,function(mid){
var _59f=_58a(mid,_591.module);
_585.push(_59f);
_58b(_59f);
});
_58c();
});
});
},_5a0=function(text,_5a1,_5a2){
var _5a3=/\(|\)/g,_5a4=1,_5a5;
_5a3.lastIndex=_5a1;
while((_5a5=_5a3.exec(text))){
if(_5a5[0]==")"){
_5a4-=1;
}else{
_5a4+=1;
}
if(_5a4==0){
break;
}
}
if(_5a4!=0){
throw "unmatched paren around character "+_5a3.lastIndex+" in: "+text;
}
return [dojo.trim(text.substring(_5a2,_5a3.lastIndex))+";\n",_5a3.lastIndex];
},_5a6=/(\/\*([\s\S]*?)\*\/|\/\/(.*)$)/mg,_5a7=/(^|\s)dojo\.(loadInit|require|provide|requireLocalization|requireIf|requireAfterIf|platformRequire)\s*\(/mg,_5a8=/(^|\s)(require|define)\s*\(/m,_5a9=function(text,_5aa){
var _5ab,_5ac,_5ad,_5ae,_5af=[],_5b0=[],_5b1=[];
_5aa=_5aa||text.replace(_5a6,function(_5b2){
_5a7.lastIndex=_5a8.lastIndex=0;
return (_5a7.test(_5b2)||_5a8.test(_5b2))?"":_5b2;
});
while((_5ab=_5a7.exec(_5aa))){
_5ac=_5a7.lastIndex;
_5ad=_5ac-_5ab[0].length;
_5ae=_5a0(_5aa,_5ac,_5ad);
if(_5ab[2]=="loadInit"){
_5af.push(_5ae[0]);
}else{
_5b0.push(_5ae[0]);
}
_5a7.lastIndex=_5ae[1];
}
_5b1=_5af.concat(_5b0);
if(_5b1.length||!_5a8.test(_5aa)){
return [text.replace(/(^|\s)dojo\.loadInit\s*\(/g,"\n0 && dojo.loadInit("),_5b1.join(""),_5b1];
}else{
return 0;
}
},_5b3=function(_5b4,text){
var _5b5,id,_5b6=[],_5b7=[];
if(_583.test(text)||!(_5b5=_5a9(text))){
return 0;
}
id=_5b4.mid+"-*loadInit";
for(var p in _58a("dojo",_5b4).result.scopeMap){
_5b6.push(p);
_5b7.push("\""+p+"\"");
}
return "// xdomain rewrite of "+_5b4.path+"\n"+"define('"+id+"',{\n"+"\tnames:"+dojo.toJson(_5b6)+",\n"+"\tdef:function("+_5b6.join(",")+"){"+_5b5[1]+"}"+"});\n\n"+"define("+dojo.toJson(_5b6.concat(["dojo/loadInit!"+id]))+", function("+_5b6.join(",")+"){\n"+_5b5[0]+"});";
},_58f=_57e.initSyncLoader(_586,_58c,_5b3),sync=_58f.sync,xd=_58f.xd,_5b8=_58f.arrived,_5b9=_58f.nonmodule,_5ba=_58f.executing,_5bb=_58f.executed,_5bc=_58f.syncExecStack,_5bd=_58f.modules,_5be=_58f.execQ,_58a=_58f.getModule,_58b=_58f.injectModule,_5bf=_58f.setArrived,_5c0=_58f.signal,_5c1=_58f.finishExec,_5c2=_58f.execModule,_5c3=_58f.getLegacyMode;
dojo.provide=function(mid){
var _5c4=_5bc[0],_5c5=lang.mixin(_58a(_582(mid),_57e.module),{executed:_5ba,result:lang.getObject(mid,true)});
_5bf(_5c5);
if(_5c4){
(_5c4.provides||(_5c4.provides=[])).push(function(){
_5c5.result=lang.getObject(mid);
delete _5c5.provides;
_5c5.executed!==_5bb&&_5c1(_5c5);
});
}
return _5c5.result;
};
has.add("config-publishRequireResult",1,0,0);
dojo.require=function(_5c6,_5c7){
function _5c8(mid,_5c9){
var _5ca=_58a(_582(mid),_57e.module);
if(_5bc.length&&_5bc[0].finish){
_5bc[0].finish.push(mid);
return undefined;
}
if(_5ca.executed){
return _5ca.result;
}
_5c9&&(_5ca.result=_5b9);
var _5cb=_5c3();
_58b(_5ca);
_5cb=_5c3();
if(_5ca.executed!==_5bb&&_5ca.injected===_5b8){
_58f.holdIdle();
_5c2(_5ca);
_58f.releaseIdle();
}
if(_5ca.executed){
return _5ca.result;
}
if(_5cb==sync){
if(_5ca.cjs){
_5be.unshift(_5ca);
}else{
_5bc.length&&(_5bc[0].finish=[mid]);
}
}else{
_5be.push(_5ca);
}
return undefined;
};
var _5cc=_5c8(_5c6,_5c7);
if(has("config-publishRequireResult")&&!lang.exists(_5c6)&&_5cc!==undefined){
lang.setObject(_5c6,_5cc);
}
return _5cc;
};
dojo.loadInit=function(f){
f();
};
dojo.registerModulePath=function(_5cd,_5ce){
var _5cf={};
_5cf[_5cd.replace(/\./g,"/")]=_5ce;
_57e({paths:_5cf});
};
dojo.platformRequire=function(_5d0){
var _5d1=(_5d0.common||[]).concat(_5d0[dojo._name]||_5d0["default"]||[]),temp;
while(_5d1.length){
if(lang.isArray(temp=_5d1.shift())){
dojo.require.apply(dojo,temp);
}else{
dojo.require(temp);
}
}
};
dojo.requireIf=dojo.requireAfterIf=function(_5d2,_5d3,_5d4){
if(_5d2){
dojo.require(_5d3,_5d4);
}
};
dojo.requireLocalization=function(_5d5,_5d6,_5d7){
_57e(["../i18n"],function(i18n){
i18n.getLocalization(_5d5,_5d6,_5d7);
});
};
return {extractLegacyApiApplications:_5a9,require:_58f.dojoRequirePlugin,loadInit:_590};
});
},"dijit/WidgetSet":function(){
define("dijit/WidgetSet",["dojo/_base/array","dojo/_base/declare","dojo/_base/window","./registry"],function(_5d8,_5d9,win,_5da){
var _5db=_5d9("dijit.WidgetSet",null,{constructor:function(){
this._hash={};
this.length=0;
},add:function(_5dc){
if(this._hash[_5dc.id]){
throw new Error("Tried to register widget with id=="+_5dc.id+" but that id is already registered");
}
this._hash[_5dc.id]=_5dc;
this.length++;
},remove:function(id){
if(this._hash[id]){
delete this._hash[id];
this.length--;
}
},forEach:function(func,_5dd){
_5dd=_5dd||win.global;
var i=0,id;
for(id in this._hash){
func.call(_5dd,this._hash[id],i++,this._hash);
}
return this;
},filter:function(_5de,_5df){
_5df=_5df||win.global;
var res=new _5db(),i=0,id;
for(id in this._hash){
var w=this._hash[id];
if(_5de.call(_5df,w,i++,this._hash)){
res.add(w);
}
}
return res;
},byId:function(id){
return this._hash[id];
},byClass:function(cls){
var res=new _5db(),id,_5e0;
for(id in this._hash){
_5e0=this._hash[id];
if(_5e0.declaredClass==cls){
res.add(_5e0);
}
}
return res;
},toArray:function(){
var ar=[];
for(var id in this._hash){
ar.push(this._hash[id]);
}
return ar;
},map:function(func,_5e1){
return _5d8.map(this.toArray(),func,_5e1);
},every:function(func,_5e2){
_5e2=_5e2||win.global;
var x=0,i;
for(i in this._hash){
if(!func.call(_5e2,this._hash[i],x++,this._hash)){
return false;
}
}
return true;
},some:function(func,_5e3){
_5e3=_5e3||win.global;
var x=0,i;
for(i in this._hash){
if(func.call(_5e3,this._hash[i],x++,this._hash)){
return true;
}
}
return false;
}});
_5d8.forEach(["forEach","filter","byClass","map","every","some"],function(func){
_5da[func]=_5db.prototype[func];
});
return _5db;
});
},"dojo/fx/easing":function(){
define(["../_base/lang"],function(lang){
var _5e4={linear:function(n){
return n;
},quadIn:function(n){
return Math.pow(n,2);
},quadOut:function(n){
return n*(n-2)*-1;
},quadInOut:function(n){
n=n*2;
if(n<1){
return Math.pow(n,2)/2;
}
return -1*((--n)*(n-2)-1)/2;
},cubicIn:function(n){
return Math.pow(n,3);
},cubicOut:function(n){
return Math.pow(n-1,3)+1;
},cubicInOut:function(n){
n=n*2;
if(n<1){
return Math.pow(n,3)/2;
}
n-=2;
return (Math.pow(n,3)+2)/2;
},quartIn:function(n){
return Math.pow(n,4);
},quartOut:function(n){
return -1*(Math.pow(n-1,4)-1);
},quartInOut:function(n){
n=n*2;
if(n<1){
return Math.pow(n,4)/2;
}
n-=2;
return -1/2*(Math.pow(n,4)-2);
},quintIn:function(n){
return Math.pow(n,5);
},quintOut:function(n){
return Math.pow(n-1,5)+1;
},quintInOut:function(n){
n=n*2;
if(n<1){
return Math.pow(n,5)/2;
}
n-=2;
return (Math.pow(n,5)+2)/2;
},sineIn:function(n){
return -1*Math.cos(n*(Math.PI/2))+1;
},sineOut:function(n){
return Math.sin(n*(Math.PI/2));
},sineInOut:function(n){
return -1*(Math.cos(Math.PI*n)-1)/2;
},expoIn:function(n){
return (n==0)?0:Math.pow(2,10*(n-1));
},expoOut:function(n){
return (n==1)?1:(-1*Math.pow(2,-10*n)+1);
},expoInOut:function(n){
if(n==0){
return 0;
}
if(n==1){
return 1;
}
n=n*2;
if(n<1){
return Math.pow(2,10*(n-1))/2;
}
--n;
return (-1*Math.pow(2,-10*n)+2)/2;
},circIn:function(n){
return -1*(Math.sqrt(1-Math.pow(n,2))-1);
},circOut:function(n){
n=n-1;
return Math.sqrt(1-Math.pow(n,2));
},circInOut:function(n){
n=n*2;
if(n<1){
return -1/2*(Math.sqrt(1-Math.pow(n,2))-1);
}
n-=2;
return 1/2*(Math.sqrt(1-Math.pow(n,2))+1);
},backIn:function(n){
var s=1.70158;
return Math.pow(n,2)*((s+1)*n-s);
},backOut:function(n){
n=n-1;
var s=1.70158;
return Math.pow(n,2)*((s+1)*n+s)+1;
},backInOut:function(n){
var s=1.70158*1.525;
n=n*2;
if(n<1){
return (Math.pow(n,2)*((s+1)*n-s))/2;
}
n-=2;
return (Math.pow(n,2)*((s+1)*n+s)+2)/2;
},elasticIn:function(n){
if(n==0||n==1){
return n;
}
var p=0.3;
var s=p/4;
n=n-1;
return -1*Math.pow(2,10*n)*Math.sin((n-s)*(2*Math.PI)/p);
},elasticOut:function(n){
if(n==0||n==1){
return n;
}
var p=0.3;
var s=p/4;
return Math.pow(2,-10*n)*Math.sin((n-s)*(2*Math.PI)/p)+1;
},elasticInOut:function(n){
if(n==0){
return 0;
}
n=n*2;
if(n==2){
return 1;
}
var p=0.3*1.5;
var s=p/4;
if(n<1){
n-=1;
return -0.5*(Math.pow(2,10*n)*Math.sin((n-s)*(2*Math.PI)/p));
}
n-=1;
return 0.5*(Math.pow(2,-10*n)*Math.sin((n-s)*(2*Math.PI)/p))+1;
},bounceIn:function(n){
return (1-_5e4.bounceOut(1-n));
},bounceOut:function(n){
var s=7.5625;
var p=2.75;
var l;
if(n<(1/p)){
l=s*Math.pow(n,2);
}else{
if(n<(2/p)){
n-=(1.5/p);
l=s*Math.pow(n,2)+0.75;
}else{
if(n<(2.5/p)){
n-=(2.25/p);
l=s*Math.pow(n,2)+0.9375;
}else{
n-=(2.625/p);
l=s*Math.pow(n,2)+0.984375;
}
}
}
return l;
},bounceInOut:function(n){
if(n<0.5){
return _5e4.bounceIn(n*2)/2;
}
return (_5e4.bounceOut(n*2-1)/2)+0.5;
}};
lang.setObject("dojo.fx.easing",_5e4);
return _5e4;
});
},"dojo/mouse":function(){
define(["./_base/kernel","./on","./has","./dom","./_base/window"],function(dojo,on,has,dom,win){
has.add("dom-quirks",win.doc&&win.doc.compatMode=="BackCompat");
has.add("events-mouseenter",win.doc&&"onmouseenter" in win.doc.createElement("div"));
var _5e5;
if(has("dom-quirks")||!has("dom-addeventlistener")){
_5e5={LEFT:1,MIDDLE:4,RIGHT:2,isButton:function(e,_5e6){
return e.button&_5e6;
},isLeft:function(e){
return e.button&1;
},isMiddle:function(e){
return e.button&4;
},isRight:function(e){
return e.button&2;
}};
}else{
_5e5={LEFT:0,MIDDLE:1,RIGHT:2,isButton:function(e,_5e7){
return e.button==_5e7;
},isLeft:function(e){
return e.button==0;
},isMiddle:function(e){
return e.button==1;
},isRight:function(e){
return e.button==2;
}};
}
dojo.mouseButtons=_5e5;
function _5e8(type,_5e9){
var _5ea=function(node,_5eb){
return on(node,type,function(evt){
if(!dom.isDescendant(evt.relatedTarget,_5e9?evt.target:node)){
return _5eb.call(this,evt);
}
});
};
if(!_5e9){
_5ea.bubble=_5e8(type,true);
}
return _5ea;
};
return {enter:_5e8("mouseover"),leave:_5e8("mouseout"),isLeft:_5e5.isLeft,isMiddle:_5e5.isMiddle,isRight:_5e5.isRight};
});
},"dijit/a11y":function(){
define("dijit/a11y",["dojo/_base/array","dojo/_base/config","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/_base/sniff","./_base/manager","."],function(_5ec,_5ed,_5ee,dom,_5ef,_5f0,has,_5f1,_5f2){
var _5f3=(_5f2._isElementShown=function(elem){
var s=_5f0.get(elem);
return (s.visibility!="hidden")&&(s.visibility!="collapsed")&&(s.display!="none")&&(_5ef.get(elem,"type")!="hidden");
});
_5f2.hasDefaultTabStop=function(elem){
switch(elem.nodeName.toLowerCase()){
case "a":
return _5ef.has(elem,"href");
case "area":
case "button":
case "input":
case "object":
case "select":
case "textarea":
return true;
case "iframe":
var body;
try{
var _5f4=elem.contentDocument;
if("designMode" in _5f4&&_5f4.designMode=="on"){
return true;
}
body=_5f4.body;
}
catch(e1){
try{
body=elem.contentWindow.document.body;
}
catch(e2){
return false;
}
}
return body&&(body.contentEditable=="true"||(body.firstChild&&body.firstChild.contentEditable=="true"));
default:
return elem.contentEditable=="true";
}
};
var _5f5=(_5f2.isTabNavigable=function(elem){
if(_5ef.get(elem,"disabled")){
return false;
}else{
if(_5ef.has(elem,"tabIndex")){
return _5ef.get(elem,"tabIndex")>=0;
}else{
return _5f2.hasDefaultTabStop(elem);
}
}
});
_5f2._getTabNavigable=function(root){
var _5f6,last,_5f7,_5f8,_5f9,_5fa,_5fb={};
function _5fc(node){
return node&&node.tagName.toLowerCase()=="input"&&node.type&&node.type.toLowerCase()=="radio"&&node.name&&node.name.toLowerCase();
};
var _5fd=function(_5fe){
for(var _5ff=_5fe.firstChild;_5ff;_5ff=_5ff.nextSibling){
if(_5ff.nodeType!=1||(has("ie")&&_5ff.scopeName!=="HTML")||!_5f3(_5ff)){
continue;
}
if(_5f5(_5ff)){
var _600=_5ef.get(_5ff,"tabIndex");
if(!_5ef.has(_5ff,"tabIndex")||_600==0){
if(!_5f6){
_5f6=_5ff;
}
last=_5ff;
}else{
if(_600>0){
if(!_5f7||_600<_5f8){
_5f8=_600;
_5f7=_5ff;
}
if(!_5f9||_600>=_5fa){
_5fa=_600;
_5f9=_5ff;
}
}
}
var rn=_5fc(_5ff);
if(_5ef.get(_5ff,"checked")&&rn){
_5fb[rn]=_5ff;
}
}
if(_5ff.nodeName.toUpperCase()!="SELECT"){
_5fd(_5ff);
}
}
};
if(_5f3(root)){
_5fd(root);
}
function rs(node){
return _5fb[_5fc(node)]||node;
};
return {first:rs(_5f6),last:rs(last),lowest:rs(_5f7),highest:rs(_5f9)};
};
_5f2.getFirstInTabbingOrder=function(root){
var _601=_5f2._getTabNavigable(dom.byId(root));
return _601.lowest?_601.lowest:_601.first;
};
_5f2.getLastInTabbingOrder=function(root){
var _602=_5f2._getTabNavigable(dom.byId(root));
return _602.last?_602.last:_602.highest;
};
return {hasDefaultTabStop:_5f2.hasDefaultTabStop,isTabNavigable:_5f2.isTabNavigable,_getTabNavigable:_5f2._getTabNavigable,getFirstInTabbingOrder:_5f2.getFirstInTabbingOrder,getLastInTabbingOrder:_5f2.getLastInTabbingOrder};
});
},"dijit/typematic":function(){
define("dijit/typematic",["dojo/_base/array","dojo/_base/connect","dojo/_base/event","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/_base/sniff","."],function(_603,_604,_605,_606,lang,on,has,_607){
var _608=(_607.typematic={_fireEventAndReload:function(){
this._timer=null;
this._callback(++this._count,this._node,this._evt);
this._currentTimeout=Math.max(this._currentTimeout<0?this._initialDelay:(this._subsequentDelay>1?this._subsequentDelay:Math.round(this._currentTimeout*this._subsequentDelay)),this._minDelay);
this._timer=setTimeout(lang.hitch(this,"_fireEventAndReload"),this._currentTimeout);
},trigger:function(evt,_609,node,_60a,obj,_60b,_60c,_60d){
if(obj!=this._obj){
this.stop();
this._initialDelay=_60c||500;
this._subsequentDelay=_60b||0.9;
this._minDelay=_60d||10;
this._obj=obj;
this._evt=evt;
this._node=node;
this._currentTimeout=-1;
this._count=-1;
this._callback=lang.hitch(_609,_60a);
this._fireEventAndReload();
this._evt=lang.mixin({faux:true},evt);
}
},stop:function(){
if(this._timer){
clearTimeout(this._timer);
this._timer=null;
}
if(this._obj){
this._callback(-1,this._node,this._evt);
this._obj=null;
}
},addKeyListener:function(node,_60e,_60f,_610,_611,_612,_613){
if(_60e.keyCode){
_60e.charOrCode=_60e.keyCode;
_606.deprecated("keyCode attribute parameter for dijit.typematic.addKeyListener is deprecated. Use charOrCode instead.","","2.0");
}else{
if(_60e.charCode){
_60e.charOrCode=String.fromCharCode(_60e.charCode);
_606.deprecated("charCode attribute parameter for dijit.typematic.addKeyListener is deprecated. Use charOrCode instead.","","2.0");
}
}
var _614=[on(node,_604._keypress,lang.hitch(this,function(evt){
if(evt.charOrCode==_60e.charOrCode&&(_60e.ctrlKey===undefined||_60e.ctrlKey==evt.ctrlKey)&&(_60e.altKey===undefined||_60e.altKey==evt.altKey)&&(_60e.metaKey===undefined||_60e.metaKey==(evt.metaKey||false))&&(_60e.shiftKey===undefined||_60e.shiftKey==evt.shiftKey)){
_605.stop(evt);
_608.trigger(evt,_60f,node,_610,_60e,_611,_612,_613);
}else{
if(_608._obj==_60e){
_608.stop();
}
}
})),on(node,"keyup",lang.hitch(this,function(){
if(_608._obj==_60e){
_608.stop();
}
}))];
return {remove:function(){
_603.forEach(_614,function(h){
h.remove();
});
}};
},addMouseListener:function(node,_615,_616,_617,_618,_619){
var _61a=[on(node,"mousedown",lang.hitch(this,function(evt){
_605.stop(evt);
_608.trigger(evt,_615,node,_616,node,_617,_618,_619);
})),on(node,"mouseup",lang.hitch(this,function(evt){
if(this._obj){
_605.stop(evt);
}
_608.stop();
})),on(node,"mouseout",lang.hitch(this,function(evt){
_605.stop(evt);
_608.stop();
})),on(node,"mousemove",lang.hitch(this,function(evt){
evt.preventDefault();
})),on(node,"dblclick",lang.hitch(this,function(evt){
_605.stop(evt);
if(has("ie")){
_608.trigger(evt,_615,node,_616,node,_617,_618,_619);
setTimeout(lang.hitch(this,_608.stop),50);
}
}))];
return {remove:function(){
_603.forEach(_61a,function(h){
h.remove();
});
}};
},addListener:function(_61b,_61c,_61d,_61e,_61f,_620,_621,_622){
var _623=[this.addKeyListener(_61c,_61d,_61e,_61f,_620,_621,_622),this.addMouseListener(_61b,_61e,_61f,_620,_621,_622)];
return {remove:function(){
_603.forEach(_623,function(h){
h.remove();
});
}};
}});
return _608;
});
},"dojox/mobile/app/ImageView":function(){
define(["dijit","dojo","dojox","dojo/require!dojox/mobile/app/_Widget,dojo/fx/easing"],function(_624,dojo,_625){
dojo.provide("dojox.mobile.app.ImageView");
dojo.experimental("dojox.mobile.app.ImageView");
dojo.require("dojox.mobile.app._Widget");
dojo.require("dojo.fx.easing");
dojo.declare("dojox.mobile.app.ImageView",_625.mobile.app._Widget,{zoom:1,zoomCenterX:0,zoomCenterY:0,maxZoom:5,autoZoomLevel:3,disableAutoZoom:false,disableSwipe:false,autoZoomEvent:null,_leftImg:null,_centerImg:null,_rightImg:null,_leftSmallImg:null,_centerSmallImg:null,_rightSmallImg:null,constructor:function(){
this.panX=0;
this.panY=0;
this.handleLoad=dojo.hitch(this,this.handleLoad);
this._updateAnimatedZoom=dojo.hitch(this,this._updateAnimatedZoom);
this._updateAnimatedPan=dojo.hitch(this,this._updateAnimatedPan);
this._onAnimPanEnd=dojo.hitch(this,this._onAnimPanEnd);
},buildRendering:function(){
this.inherited(arguments);
this.canvas=dojo.create("canvas",{},this.domNode);
dojo.addClass(this.domNode,"mblImageView");
},postCreate:function(){
this.inherited(arguments);
this.size=dojo.marginBox(this.domNode);
dojo.style(this.canvas,{width:this.size.w+"px",height:this.size.h+"px"});
this.canvas.height=this.size.h;
this.canvas.width=this.size.w;
var _626=this;
this.connect(this.domNode,"onmousedown",function(_627){
if(_626.isAnimating()){
return;
}
if(_626.panX){
_626.handleDragEnd();
}
_626.downX=_627.targetTouches?_627.targetTouches[0].clientX:_627.clientX;
_626.downY=_627.targetTouches?_627.targetTouches[0].clientY:_627.clientY;
});
this.connect(this.domNode,"onmousemove",function(_628){
if(_626.isAnimating()){
return;
}
if((!_626.downX&&_626.downX!==0)||(!_626.downY&&_626.downY!==0)){
return;
}
if((!_626.disableSwipe&&_626.zoom==1)||(!_626.disableAutoZoom&&_626.zoom!=1)){
var x=_628.targetTouches?_628.targetTouches[0].clientX:_628.pageX;
var y=_628.targetTouches?_628.targetTouches[0].clientY:_628.pageY;
_626.panX=x-_626.downX;
_626.panY=y-_626.downY;
if(_626.zoom==1){
if(Math.abs(_626.panX)>10){
_626.render();
}
}else{
if(Math.abs(_626.panX)>10||Math.abs(_626.panY)>10){
_626.render();
}
}
}
});
this.connect(this.domNode,"onmouseout",function(_629){
if(!_626.isAnimating()&&_626.panX){
_626.handleDragEnd();
}
});
this.connect(this.domNode,"onmouseover",function(_62a){
_626.downX=_626.downY=null;
});
this.connect(this.domNode,"onclick",function(_62b){
if(_626.isAnimating()){
return;
}
if(_626.downX==null||_626.downY==null){
return;
}
var x=(_62b.targetTouches?_62b.targetTouches[0].clientX:_62b.pageX);
var y=(_62b.targetTouches?_62b.targetTouches[0].clientY:_62b.pageY);
if(Math.abs(_626.panX)>14||Math.abs(_626.panY)>14){
_626.downX=_626.downY=null;
_626.handleDragEnd();
return;
}
_626.downX=_626.downY=null;
if(!_626.disableAutoZoom){
if(!_626._centerImg||!_626._centerImg._loaded){
return;
}
if(_626.zoom!=1){
_626.set("animatedZoom",1);
return;
}
var pos=dojo._abs(_626.domNode);
var _62c=_626.size.w/_626._centerImg.width;
var _62d=_626.size.h/_626._centerImg.height;
_626.zoomTo(((x-pos.x)/_62c)-_626.panX,((y-pos.y)/_62d)-_626.panY,_626.autoZoomLevel);
}
});
dojo.connect(this.domNode,"flick",this,"handleFlick");
},isAnimating:function(){
return this._anim&&this._anim.status()=="playing";
},handleDragEnd:function(){
this.downX=this.downY=null;
console.log("handleDragEnd");
if(this.zoom==1){
if(!this.panX){
return;
}
var _62e=(this._leftImg&&this._leftImg._loaded)||(this._leftSmallImg&&this._leftSmallImg._loaded);
var _62f=(this._rightImg&&this._rightImg._loaded)||(this._rightSmallImg&&this._rightSmallImg._loaded);
var _630=!(Math.abs(this.panX)<this._centerImg._baseWidth/2)&&((this.panX>0&&_62e?1:0)||(this.panX<0&&_62f?1:0));
if(!_630){
this._animPanTo(0,dojo.fx.easing.expoOut,700);
}else{
this.moveTo(this.panX);
}
}else{
if(!this.panX&&!this.panY){
return;
}
this.zoomCenterX-=(this.panX/this.zoom);
this.zoomCenterY-=(this.panY/this.zoom);
this.panX=this.panY=0;
}
},handleFlick:function(_631){
if(this.zoom==1&&_631.duration<500){
if(_631.direction=="ltr"){
this.moveTo(1);
}else{
if(_631.direction=="rtl"){
this.moveTo(-1);
}
}
this.downX=this.downY=null;
}
},moveTo:function(_632){
_632=_632>0?1:-1;
var _633;
if(_632<1){
if(this._rightImg&&this._rightImg._loaded){
_633=this._rightImg;
}else{
if(this._rightSmallImg&&this._rightSmallImg._loaded){
_633=this._rightSmallImg;
}
}
}else{
if(this._leftImg&&this._leftImg._loaded){
_633=this._leftImg;
}else{
if(this._leftSmallImg&&this._leftSmallImg._loaded){
_633=this._leftSmallImg;
}
}
}
this._moveDir=_632;
var _634=this;
if(_633&&_633._loaded){
this._animPanTo(this.size.w*_632,null,500,function(){
_634.panX=0;
_634.panY=0;
if(_632<0){
_634._switchImage("left","right");
}else{
_634._switchImage("right","left");
}
_634.render();
_634.onChange(_632*-1);
});
}else{
console.log("moveTo image not loaded!",_633);
this._animPanTo(0,dojo.fx.easing.expoOut,700);
}
},_switchImage:function(_635,_636){
var _637="_"+_635+"SmallImg";
var _638="_"+_635+"Img";
var _639="_"+_636+"SmallImg";
var _63a="_"+_636+"Img";
this[_638]=this._centerImg;
this[_637]=this._centerSmallImg;
this[_638]._type=_635;
if(this[_637]){
this[_637]._type=_635;
}
this._centerImg=this[_63a];
this._centerSmallImg=this[_639];
this._centerImg._type="center";
if(this._centerSmallImg){
this._centerSmallImg._type="center";
}
this[_63a]=this[_639]=null;
},_animPanTo:function(to,_63b,_63c,_63d){
this._animCallback=_63d;
this._anim=new dojo.Animation({curve:[this.panX,to],onAnimate:this._updateAnimatedPan,duration:_63c||500,easing:_63b,onEnd:this._onAnimPanEnd});
this._anim.play();
return this._anim;
},onChange:function(_63e){
},_updateAnimatedPan:function(_63f){
this.panX=_63f;
this.render();
},_onAnimPanEnd:function(){
this.panX=this.panY=0;
if(this._animCallback){
this._animCallback();
}
},zoomTo:function(_640,_641,zoom){
this.set("zoomCenterX",_640);
this.set("zoomCenterY",_641);
this.set("animatedZoom",zoom);
},render:function(){
var cxt=this.canvas.getContext("2d");
cxt.clearRect(0,0,this.canvas.width,this.canvas.height);
this._renderImg(this._centerSmallImg,this._centerImg,this.zoom==1?(this.panX<0?1:this.panX>0?-1:0):0);
if(this.zoom==1&&this.panX!=0){
if(this.panX>0){
this._renderImg(this._leftSmallImg,this._leftImg,1);
}else{
this._renderImg(this._rightSmallImg,this._rightImg,-1);
}
}
},_renderImg:function(_642,_643,_644){
var img=(_643&&_643._loaded)?_643:_642;
if(!img||!img._loaded){
return;
}
var cxt=this.canvas.getContext("2d");
var _645=img._baseWidth;
var _646=img._baseHeight;
var _647=_645*this.zoom;
var _648=_646*this.zoom;
var _649=Math.min(this.size.w,_647);
var _64a=Math.min(this.size.h,_648);
var _64b=this.dispWidth=img.width*(_649/_647);
var _64c=this.dispHeight=img.height*(_64a/_648);
var _64d=this.zoomCenterX-(this.panX/this.zoom);
var _64e=this.zoomCenterY-(this.panY/this.zoom);
var _64f=Math.floor(Math.max(_64b/2,Math.min(img.width-_64b/2,_64d)));
var _650=Math.floor(Math.max(_64c/2,Math.min(img.height-_64c/2,_64e)));
var _651=Math.max(0,Math.round((img.width-_64b)/2+(_64f-img._centerX)));
var _652=Math.max(0,Math.round((img.height-_64c)/2+(_650-img._centerY)));
var _653=Math.round(Math.max(0,this.canvas.width-_649)/2);
var _654=Math.round(Math.max(0,this.canvas.height-_64a)/2);
var _655=_649;
var _656=_64b;
if(this.zoom==1&&_644&&this.panX){
if(this.panX<0){
if(_644>0){
_649-=Math.abs(this.panX);
_653=0;
}else{
if(_644<0){
_649=Math.max(1,Math.abs(this.panX)-5);
_653=this.size.w-_649;
}
}
}else{
if(_644>0){
_649=Math.max(1,Math.abs(this.panX)-5);
_653=0;
}else{
if(_644<0){
_649-=Math.abs(this.panX);
_653=this.size.w-_649;
}
}
}
_64b=Math.max(1,Math.floor(_64b*(_649/_655)));
if(_644>0){
_651=(_651+_656)-(_64b);
}
_651=Math.floor(_651);
}
try{
cxt.drawImage(img,Math.max(0,_651),_652,Math.min(_656,_64b),_64c,_653,_654,Math.min(_655,_649),_64a);
}
catch(e){
console.log("Caught Error",e,"type=",img._type,"oldDestWidth = ",_655,"destWidth",_649,"destX",_653,"oldSourceWidth=",_656,"sourceWidth=",_64b,"sourceX = "+_651);
}
},_setZoomAttr:function(_657){
this.zoom=Math.min(this.maxZoom,Math.max(1,_657));
if(this.zoom==1&&this._centerImg&&this._centerImg._loaded){
if(!this.isAnimating()){
this.zoomCenterX=this._centerImg.width/2;
this.zoomCenterY=this._centerImg.height/2;
}
this.panX=this.panY=0;
}
this.render();
},_setZoomCenterXAttr:function(_658){
if(_658!=this.zoomCenterX){
if(this._centerImg&&this._centerImg._loaded){
_658=Math.min(this._centerImg.width,_658);
}
this.zoomCenterX=Math.max(0,Math.round(_658));
}
},_setZoomCenterYAttr:function(_659){
if(_659!=this.zoomCenterY){
if(this._centerImg&&this._centerImg._loaded){
_659=Math.min(this._centerImg.height,_659);
}
this.zoomCenterY=Math.max(0,Math.round(_659));
}
},_setZoomCenterAttr:function(_65a){
if(_65a.x!=this.zoomCenterX||_65a.y!=this.zoomCenterY){
this.set("zoomCenterX",_65a.x);
this.set("zoomCenterY",_65a.y);
this.render();
}
},_setAnimatedZoomAttr:function(_65b){
if(this._anim&&this._anim.status()=="playing"){
return;
}
this._anim=new dojo.Animation({curve:[this.zoom,_65b],onAnimate:this._updateAnimatedZoom,onEnd:this._onAnimEnd});
this._anim.play();
},_updateAnimatedZoom:function(_65c){
this._setZoomAttr(_65c);
},_setCenterUrlAttr:function(_65d){
this._setImage("center",_65d);
},_setLeftUrlAttr:function(_65e){
this._setImage("left",_65e);
},_setRightUrlAttr:function(_65f){
this._setImage("right",_65f);
},_setImage:function(name,_660){
var _661=null;
var _662=null;
if(dojo.isString(_660)){
_662=_660;
}else{
_662=_660.large;
_661=_660.small;
}
if(this["_"+name+"Img"]&&this["_"+name+"Img"]._src==_662){
return;
}
var _663=this["_"+name+"Img"]=new Image();
_663._type=name;
_663._loaded=false;
_663._src=_662;
_663._conn=dojo.connect(_663,"onload",this.handleLoad);
if(_661){
var _664=this["_"+name+"SmallImg"]=new Image();
_664._type=name;
_664._loaded=false;
_664._conn=dojo.connect(_664,"onload",this.handleLoad);
_664._isSmall=true;
_664._src=_661;
_664.src=_661;
}
_663.src=_662;
},handleLoad:function(evt){
var img=evt.target;
img._loaded=true;
dojo.disconnect(img._conn);
var type=img._type;
switch(type){
case "center":
this.zoomCenterX=img.width/2;
this.zoomCenterY=img.height/2;
break;
}
var _665=img.height;
var _666=img.width;
if(_666/this.size.w<_665/this.size.h){
img._baseHeight=this.canvas.height;
img._baseWidth=_666/(_665/this.size.h);
}else{
img._baseWidth=this.canvas.width;
img._baseHeight=_665/(_666/this.size.w);
}
img._centerX=_666/2;
img._centerY=_665/2;
this.render();
this.onLoad(img._type,img._src,img._isSmall);
},onLoad:function(type,url,_667){
}});
});
},"dijit/_base/focus":function(){
define("dijit/_base/focus",["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/topic","dojo/_base/window","../focus",".."],function(_668,dom,lang,_669,win,_66a,_66b){
lang.mixin(_66b,{_curFocus:null,_prevFocus:null,isCollapsed:function(){
return _66b.getBookmark().isCollapsed;
},getBookmark:function(){
var bm,rg,tg,sel=win.doc.selection,cf=_66a.curNode;
if(win.global.getSelection){
sel=win.global.getSelection();
if(sel){
if(sel.isCollapsed){
tg=cf?cf.tagName:"";
if(tg){
tg=tg.toLowerCase();
if(tg=="textarea"||(tg=="input"&&(!cf.type||cf.type.toLowerCase()=="text"))){
sel={start:cf.selectionStart,end:cf.selectionEnd,node:cf,pRange:true};
return {isCollapsed:(sel.end<=sel.start),mark:sel};
}
}
bm={isCollapsed:true};
if(sel.rangeCount){
bm.mark=sel.getRangeAt(0).cloneRange();
}
}else{
rg=sel.getRangeAt(0);
bm={isCollapsed:false,mark:rg.cloneRange()};
}
}
}else{
if(sel){
tg=cf?cf.tagName:"";
tg=tg.toLowerCase();
if(cf&&tg&&(tg=="button"||tg=="textarea"||tg=="input")){
if(sel.type&&sel.type.toLowerCase()=="none"){
return {isCollapsed:true,mark:null};
}else{
rg=sel.createRange();
return {isCollapsed:rg.text&&rg.text.length?false:true,mark:{range:rg,pRange:true}};
}
}
bm={};
try{
rg=sel.createRange();
bm.isCollapsed=!(sel.type=="Text"?rg.htmlText.length:rg.length);
}
catch(e){
bm.isCollapsed=true;
return bm;
}
if(sel.type.toUpperCase()=="CONTROL"){
if(rg.length){
bm.mark=[];
var i=0,len=rg.length;
while(i<len){
bm.mark.push(rg.item(i++));
}
}else{
bm.isCollapsed=true;
bm.mark=null;
}
}else{
bm.mark=rg.getBookmark();
}
}else{
console.warn("No idea how to store the current selection for this browser!");
}
}
return bm;
},moveToBookmark:function(_66c){
var _66d=win.doc,mark=_66c.mark;
if(mark){
if(win.global.getSelection){
var sel=win.global.getSelection();
if(sel&&sel.removeAllRanges){
if(mark.pRange){
var n=mark.node;
n.selectionStart=mark.start;
n.selectionEnd=mark.end;
}else{
sel.removeAllRanges();
sel.addRange(mark);
}
}else{
console.warn("No idea how to restore selection for this browser!");
}
}else{
if(_66d.selection&&mark){
var rg;
if(mark.pRange){
rg=mark.range;
}else{
if(lang.isArray(mark)){
rg=_66d.body.createControlRange();
_668.forEach(mark,function(n){
rg.addElement(n);
});
}else{
rg=_66d.body.createTextRange();
rg.moveToBookmark(mark);
}
}
rg.select();
}
}
}
},getFocus:function(menu,_66e){
var node=!_66a.curNode||(menu&&dom.isDescendant(_66a.curNode,menu.domNode))?_66b._prevFocus:_66a.curNode;
return {node:node,bookmark:node&&(node==_66a.curNode)&&win.withGlobal(_66e||win.global,_66b.getBookmark),openedForWindow:_66e};
},_activeStack:[],registerIframe:function(_66f){
return _66a.registerIframe(_66f);
},unregisterIframe:function(_670){
_670&&_670.remove();
},registerWin:function(_671,_672){
return _66a.registerWin(_671,_672);
},unregisterWin:function(_673){
_673&&_673.remove();
}});
_66a.focus=function(_674){
if(!_674){
return;
}
var node="node" in _674?_674.node:_674,_675=_674.bookmark,_676=_674.openedForWindow,_677=_675?_675.isCollapsed:false;
if(node){
var _678=(node.tagName.toLowerCase()=="iframe")?node.contentWindow:node;
if(_678&&_678.focus){
try{
_678.focus();
}
catch(e){
}
}
_66a._onFocusNode(node);
}
if(_675&&win.withGlobal(_676||win.global,_66b.isCollapsed)&&!_677){
if(_676){
_676.focus();
}
try{
win.withGlobal(_676||win.global,_66b.moveToBookmark,null,[_675]);
}
catch(e2){
}
}
};
_66a.watch("curNode",function(name,_679,_67a){
_66b._curFocus=_67a;
_66b._prevFocus=_679;
if(_67a){
_669.publish("focusNode",_67a);
}
});
_66a.watch("activeStack",function(name,_67b,_67c){
_66b._activeStack=_67c;
});
_66a.on("widget-blur",function(_67d,by){
_669.publish("widgetBlur",_67d,by);
});
_66a.on("widget-focus",function(_67e,by){
_669.publish("widgetFocus",_67e,by);
});
return _66b;
});
},"dojox/mobile/ListItem":function(){
define("dojox/mobile/ListItem",["dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/dom-class","dojo/dom-construct","dojo/has","./common","./_ItemBase","./TransitionEvent"],function(_67f,_680,_681,lang,_682,_683,has,_684,_685,_686){
return _681("dojox.mobile.ListItem",_685,{rightText:"",rightIcon:"",rightIcon2:"",anchorLabel:false,noArrow:false,selected:false,checked:false,arrowClass:"mblDomButtonArrow",checkClass:"mblDomButtonCheck",variableHeight:false,rightIconTitle:"",rightIcon2Title:"",btnClass:"",btnClass2:"",tag:"li",postMixInProperties:function(){
if(this.btnClass){
this.rightIcon=this.btnClass;
}
this._setBtnClassAttr=this._setRightIconAttr;
this._setBtnClass2Attr=this._setRightIcon2Attr;
},buildRendering:function(){
this.domNode=this.srcNodeRef||_683.create(this.tag);
this.inherited(arguments);
this.domNode.className="mblListItem"+(this.selected?" mblItemSelected":"");
var box=this.box=_683.create("DIV");
box.className="mblListItemTextBox";
if(this.anchorLabel){
box.style.cursor="pointer";
}
var r=this.srcNodeRef;
if(r&&!this.label){
this.label="";
for(var i=0,len=r.childNodes.length;i<len;i++){
var n=r.firstChild;
if(n.nodeType===3&&lang.trim(n.nodeValue)!==""){
n.nodeValue=this._cv?this._cv(n.nodeValue):n.nodeValue;
this.labelNode=_683.create("SPAN",{className:"mblListItemLabel"});
this.labelNode.appendChild(n);
n=this.labelNode;
}
box.appendChild(n);
}
}
if(!this.labelNode){
this.labelNode=_683.create("SPAN",{className:"mblListItemLabel"},box);
}
if(this.anchorLabel){
box.style.display="inline";
}
var a=this.anchorNode=_683.create("A");
a.className="mblListItemAnchor";
this.domNode.appendChild(a);
a.appendChild(box);
},startup:function(){
if(this._started){
return;
}
this.inheritParams();
var _687=this.getParent();
if(this.moveTo||this.href||this.url||this.clickable||(_687&&_687.select)){
this._onClickHandle=this.connect(this.anchorNode,"onclick","onClick");
}
this.setArrow();
if(_682.contains(this.domNode,"mblVariableHeight")){
this.variableHeight=true;
}
if(this.variableHeight){
_682.add(this.domNode,"mblVariableHeight");
setTimeout(lang.hitch(this,"layoutVariableHeight"));
}
this.set("icon",this.icon);
if(!this.checked&&this.checkClass.indexOf(",")!==-1){
this.set("checked",this.checked);
}
this.inherited(arguments);
},resize:function(){
if(this.variableHeight){
this.layoutVariableHeight();
}
},onClick:function(e){
var a=e.currentTarget;
var li=a.parentNode;
if(_682.contains(li,"mblItemSelected")){
return;
}
if(this.anchorLabel){
for(var p=e.target;p.tagName!==this.tag.toUpperCase();p=p.parentNode){
if(p.className=="mblListItemTextBox"){
_682.add(p,"mblListItemTextBoxSelected");
setTimeout(function(){
_682.remove(p,"mblListItemTextBoxSelected");
},has("android")?300:1000);
this.onAnchorLabelClicked(e);
return;
}
}
}
var _688=this.getParent();
if(_688.select){
if(_688.select==="single"){
if(!this.checked){
this.set("checked",true);
}
}else{
if(_688.select==="multiple"){
this.set("checked",!this.checked);
}
}
}
this.select();
if(this.href&&this.hrefTarget){
_684.openWindow(this.href,this.hrefTarget);
return;
}
var _689;
if(this.moveTo||this.href||this.url||this.scene){
_689={moveTo:this.moveTo,href:this.href,url:this.url,scene:this.scene,transition:this.transition,transitionDir:this.transitionDir};
}else{
if(this.transitionOptions){
_689=this.transitionOptions;
}
}
if(_689){
this.setTransitionPos(e);
return new _686(this.domNode,_689,e).dispatch();
}
},select:function(){
var _68a=this.getParent();
if(_68a.stateful){
_68a.deselectAll();
}else{
var _68b=this;
setTimeout(function(){
_68b.deselect();
},has("android")?300:1000);
}
_682.add(this.domNode,"mblItemSelected");
},deselect:function(){
_682.remove(this.domNode,"mblItemSelected");
},onAnchorLabelClicked:function(e){
},layoutVariableHeight:function(){
var h=this.anchorNode.offsetHeight;
if(h===this.anchorNodeHeight){
return;
}
this.anchorNodeHeight=h;
_67f.forEach([this.rightTextNode,this.rightIcon2Node,this.rightIconNode,this.iconNode],function(n){
if(n){
var t=Math.round((h-n.offsetHeight)/2);
n.style.marginTop=t+"px";
}
});
},setArrow:function(){
if(this.checked){
return;
}
var c="";
var _68c=this.getParent();
if(this.moveTo||this.href||this.url||this.clickable){
if(!this.noArrow&&!(_68c&&_68c.stateful)){
c=this.arrowClass;
}
}
if(c){
this._setRightIconAttr(c);
}
},_setIconAttr:function(icon){
if(!this.getParent()){
return;
}
this.icon=icon;
var a=this.anchorNode;
if(!this.iconNode){
if(icon){
var ref=this.rightIconNode||this.rightIcon2Node||this.rightTextNode||this.box;
this.iconNode=_683.create("DIV",{className:"mblListItemIcon"},ref,"before");
}
}else{
_683.empty(this.iconNode);
}
if(icon&&icon!=="none"){
_684.createIcon(icon,this.iconPos,null,this.alt,this.iconNode);
if(this.iconPos){
_682.add(this.iconNode.firstChild,"mblListItemSpriteIcon");
}
_682.remove(a,"mblListItemAnchorNoIcon");
}else{
_682.add(a,"mblListItemAnchorNoIcon");
}
},_setCheckedAttr:function(_68d){
var _68e=this.getParent();
if(_68e&&_68e.select==="single"&&_68d){
_67f.forEach(_68e.getChildren(),function(_68f){
_68f.set("checked",false);
});
}
this._setRightIconAttr(this.checkClass);
var _690=this.rightIconNode.childNodes;
if(_690.length===1){
this.rightIconNode.style.display=_68d?"":"none";
}else{
_690[0].style.display=_68d?"":"none";
_690[1].style.display=!_68d?"":"none";
}
_682.toggle(this.domNode,"mblListItemChecked",_68d);
if(_68e&&this.checked!==_68d){
_68e.onCheckStateChanged(this,_68d);
}
this.checked=_68d;
},_setRightTextAttr:function(text){
if(!this.rightTextNode){
this.rightTextNode=_683.create("DIV",{className:"mblListItemRightText"},this.box,"before");
}
this.rightText=text;
this.rightTextNode.innerHTML=this._cv?this._cv(text):text;
},_setRightIconAttr:function(icon){
if(!this.rightIconNode){
var ref=this.rightIcon2Node||this.rightTextNode||this.box;
this.rightIconNode=_683.create("DIV",{className:"mblListItemRightIcon"},ref,"before");
}else{
_683.empty(this.rightIconNode);
}
this.rightIcon=icon;
var arr=(icon||"").split(/,/);
if(arr.length===1){
_684.createIcon(icon,null,null,this.rightIconTitle,this.rightIconNode);
}else{
_684.createIcon(arr[0],null,null,this.rightIconTitle,this.rightIconNode);
_684.createIcon(arr[1],null,null,this.rightIconTitle,this.rightIconNode);
}
},_setRightIcon2Attr:function(icon){
if(!this.rightIcon2Node){
var ref=this.rightTextNode||this.box;
this.rightIcon2Node=_683.create("DIV",{className:"mblListItemRightIcon2"},ref,"before");
}else{
_683.empty(this.rightIcon2Node);
}
this.rightIcon2=icon;
_684.createIcon(icon,null,null,this.rightIcon2Title,this.rightIcon2Node);
},_setLabelAttr:function(text){
this.label=text;
this.labelNode.innerHTML=this._cv?this._cv(text):text;
}});
});
},"dojo/regexp":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
lang.getObject("regexp",true,dojo);
dojo.regexp.escapeString=function(str,_691){
return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g,function(ch){
if(_691&&_691.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
dojo.regexp.buildGroupRE=function(arr,re,_692){
if(!(arr instanceof Array)){
return re(arr);
}
var b=[];
for(var i=0;i<arr.length;i++){
b.push(re(arr[i]));
}
return dojo.regexp.group(b.join("|"),_692);
};
dojo.regexp.group=function(_693,_694){
return "("+(_694?"?:":"")+_693+")";
};
return dojo.regexp;
});
},"dojox/mobile/app/StageController":function(){
define(["dijit","dojo","dojox","dojo/require!dojox/mobile/app/SceneController"],function(_695,dojo,_696){
dojo.provide("dojox.mobile.app.StageController");
dojo.experimental("dojox.mobile.app.StageController");
dojo.require("dojox.mobile.app.SceneController");
dojo.declare("dojox.mobile.app.StageController",null,{scenes:null,effect:"fade",constructor:function(node){
this.domNode=node;
this.scenes=[];
if(dojo.config.mobileAnim){
this.effect=dojo.config.mobileAnim;
}
},getActiveSceneController:function(){
return this.scenes[this.scenes.length-1];
},pushScene:function(_697,_698){
if(this._opInProgress){
return;
}
this._opInProgress=true;
var node=dojo.create("div",{"class":"scene-wrapper",style:{visibility:"hidden"}},this.domNode);
var _699=new _696.mobile.app.SceneController({},node);
if(this.scenes.length>0){
this.scenes[this.scenes.length-1].assistant.deactivate();
}
this.scenes.push(_699);
var _69a=this;
dojo.forEach(this.scenes,this.setZIndex);
_699.stageController=this;
_699.init(_697,_698).addCallback(function(){
if(_69a.scenes.length==1){
_699.domNode.style.visibility="visible";
_69a.scenes[_69a.scenes.length-1].assistant.activate(_698);
_69a._opInProgress=false;
}else{
_69a.scenes[_69a.scenes.length-2].performTransition(_69a.scenes[_69a.scenes.length-1].domNode,1,_69a.effect,null,function(){
_69a.scenes[_69a.scenes.length-1].assistant.activate(_698);
_69a._opInProgress=false;
});
}
});
},setZIndex:function(_69b,idx){
dojo.style(_69b.domNode,"zIndex",idx+1);
},popScene:function(data){
if(this._opInProgress){
return;
}
var _69c=this;
if(this.scenes.length>1){
this._opInProgress=true;
this.scenes[_69c.scenes.length-2].assistant.activate(data);
this.scenes[_69c.scenes.length-1].performTransition(_69c.scenes[this.scenes.length-2].domNode,-1,this.effect,null,function(){
_69c._destroyScene(_69c.scenes[_69c.scenes.length-1]);
_69c.scenes.splice(_69c.scenes.length-1,1);
_69c._opInProgress=false;
});
}else{
console.log("cannot pop the scene if there is just one");
}
},popScenesTo:function(_69d,data){
if(this._opInProgress){
return;
}
while(this.scenes.length>2&&this.scenes[this.scenes.length-2].sceneName!=_69d){
this._destroyScene(this.scenes[this.scenes.length-2]);
this.scenes.splice(this.scenes.length-2,1);
}
this.popScene(data);
},_destroyScene:function(_69e){
_69e.assistant.deactivate();
_69e.assistant.destroy();
_69e.destroyRecursive();
}});
});
},"dojo/_base/NodeList":function(){
define(["./kernel","../query","./array","./html","../NodeList-dom"],function(dojo,_69f,_6a0){
var _6a1=_69f.NodeList;
var nlp=_6a1.prototype;
nlp.connect=_6a1._adaptAsForEach(function(){
return dojo.connect.apply(this,arguments);
});
nlp.coords=_6a1._adaptAsMap(dojo.coords);
_6a1.events=["blur","focus","change","click","error","keydown","keypress","keyup","load","mousedown","mouseenter","mouseleave","mousemove","mouseout","mouseover","mouseup","submit"];
_6a0.forEach(_6a1.events,function(evt){
var _6a2="on"+evt;
nlp[_6a2]=function(a,b){
return this.connect(_6a2,a,b);
};
});
dojo.NodeList=_6a1;
return dojo.NodeList;
});
},"dijit/place":function(){
define("dijit/place",["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","dojo/window","."],function(_6a3,_6a4,_6a5,_6a6,win,_6a7,_6a8){
function _6a9(node,_6aa,_6ab,_6ac){
var view=_6a7.getBox();
if(!node.parentNode||String(node.parentNode.tagName).toLowerCase()!="body"){
win.body().appendChild(node);
}
var best=null;
_6a3.some(_6aa,function(_6ad){
var _6ae=_6ad.corner;
var pos=_6ad.pos;
var _6af=0;
var _6b0={w:{"L":view.l+view.w-pos.x,"R":pos.x-view.l,"M":view.w}[_6ae.charAt(1)],h:{"T":view.t+view.h-pos.y,"B":pos.y-view.t,"M":view.h}[_6ae.charAt(0)]};
if(_6ab){
var res=_6ab(node,_6ad.aroundCorner,_6ae,_6b0,_6ac);
_6af=typeof res=="undefined"?0:res;
}
var _6b1=node.style;
var _6b2=_6b1.display;
var _6b3=_6b1.visibility;
if(_6b1.display=="none"){
_6b1.visibility="hidden";
_6b1.display="";
}
var mb=_6a4.getMarginBox(node);
_6b1.display=_6b2;
_6b1.visibility=_6b3;
var _6b4={"L":pos.x,"R":pos.x-mb.w,"M":Math.max(view.l,Math.min(view.l+view.w,pos.x+(mb.w>>1))-mb.w)}[_6ae.charAt(1)],_6b5={"T":pos.y,"B":pos.y-mb.h,"M":Math.max(view.t,Math.min(view.t+view.h,pos.y+(mb.h>>1))-mb.h)}[_6ae.charAt(0)],_6b6=Math.max(view.l,_6b4),_6b7=Math.max(view.t,_6b5),endX=Math.min(view.l+view.w,_6b4+mb.w),endY=Math.min(view.t+view.h,_6b5+mb.h),_6b8=endX-_6b6,_6b9=endY-_6b7;
_6af+=(mb.w-_6b8)+(mb.h-_6b9);
if(best==null||_6af<best.overflow){
best={corner:_6ae,aroundCorner:_6ad.aroundCorner,x:_6b6,y:_6b7,w:_6b8,h:_6b9,overflow:_6af,spaceAvailable:_6b0};
}
return !_6af;
});
if(best.overflow&&_6ab){
_6ab(node,best.aroundCorner,best.corner,best.spaceAvailable,_6ac);
}
var l=_6a4.isBodyLtr(),s=node.style;
s.top=best.y+"px";
s[l?"left":"right"]=(l?best.x:view.w-best.x-best.w)+"px";
s[l?"right":"left"]="auto";
return best;
};
return (_6a8.place={at:function(node,pos,_6ba,_6bb){
var _6bc=_6a3.map(_6ba,function(_6bd){
var c={corner:_6bd,pos:{x:pos.x,y:pos.y}};
if(_6bb){
c.pos.x+=_6bd.charAt(1)=="L"?_6bb.x:-_6bb.x;
c.pos.y+=_6bd.charAt(0)=="T"?_6bb.y:-_6bb.y;
}
return c;
});
return _6a9(node,_6bc);
},around:function(node,_6be,_6bf,_6c0,_6c1){
var _6c2=(typeof _6be=="string"||"offsetWidth" in _6be)?_6a4.position(_6be,true):_6be;
if(_6be.parentNode){
var _6c3=_6be.parentNode;
while(_6c3&&_6c3.nodeType==1&&_6c3.nodeName!="BODY"){
var _6c4=_6a4.position(_6c3,true);
var _6c5=_6a5.getComputedStyle(_6c3).overflow;
if(_6c5=="hidden"||_6c5=="auto"||_6c5=="scroll"){
var _6c6=Math.min(_6c2.y+_6c2.h,_6c4.y+_6c4.h);
var _6c7=Math.min(_6c2.x+_6c2.w,_6c4.x+_6c4.w);
_6c2.x=Math.max(_6c2.x,_6c4.x);
_6c2.y=Math.max(_6c2.y,_6c4.y);
_6c2.h=_6c6-_6c2.y;
_6c2.w=_6c7-_6c2.x;
}
_6c3=_6c3.parentNode;
}
}
var x=_6c2.x,y=_6c2.y,_6c8="w" in _6c2?_6c2.w:(_6c2.w=_6c2.width),_6c9="h" in _6c2?_6c2.h:(_6a6.deprecated("place.around: dijit.place.__Rectangle: { x:"+x+", y:"+y+", height:"+_6c2.height+", width:"+_6c8+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_6c2.height+", w:"+_6c8+" }","","2.0"),_6c2.h=_6c2.height);
var _6ca=[];
function push(_6cb,_6cc){
_6ca.push({aroundCorner:_6cb,corner:_6cc,pos:{x:{"L":x,"R":x+_6c8,"M":x+(_6c8>>1)}[_6cb.charAt(1)],y:{"T":y,"B":y+_6c9,"M":y+(_6c9>>1)}[_6cb.charAt(0)]}});
};
_6a3.forEach(_6bf,function(pos){
var ltr=_6c0;
switch(pos){
case "above-centered":
push("TM","BM");
break;
case "below-centered":
push("BM","TM");
break;
case "after":
ltr=!ltr;
case "before":
push(ltr?"ML":"MR",ltr?"MR":"ML");
break;
case "below-alt":
ltr=!ltr;
case "below":
push(ltr?"BL":"BR",ltr?"TL":"TR");
push(ltr?"BR":"BL",ltr?"TR":"TL");
break;
case "above-alt":
ltr=!ltr;
case "above":
push(ltr?"TL":"TR",ltr?"BL":"BR");
push(ltr?"TR":"TL",ltr?"BR":"BL");
break;
default:
push(pos.aroundCorner,pos.corner);
}
});
var _6cd=_6a9(node,_6ca,_6c1,{w:_6c8,h:_6c9});
_6cd.aroundNodePos=_6c2;
return _6cd;
}});
});
},"dojo/_base/kernel":function(){
define(["../has","./config","require","module"],function(has,_6ce,_6cf,_6d0){
var i,p,_6d1={},_6d2={},dojo={config:_6ce,global:this,dijit:_6d1,dojox:_6d2};
var _6d3={dojo:["dojo",dojo],dijit:["dijit",_6d1],dojox:["dojox",_6d2]},_6d4=(_6cf.packs&&_6cf.packs[_6d0.id.match(/[^\/]+/)[0]].packageMap)||{},item;
for(p in _6d4){
if(_6d3[p]){
_6d3[p][0]=_6d4[p];
}else{
_6d3[p]=[_6d4[p],{}];
}
}
for(p in _6d3){
item=_6d3[p];
item[1]._scopeName=item[0];
if(!_6ce.noGlobals){
this[item[0]]=item[1];
}
}
dojo.scopeMap=_6d3;
dojo.baseUrl=dojo.config.baseUrl=_6cf.baseUrl;
dojo.isAsync=!1||_6cf.async;
dojo.locale=_6ce.locale;
var rev="$Rev: 27407 $".match(/\d+/);
dojo.version={major:1,minor:7,patch:1,flag:"",revision:rev?+rev[0]:NaN,toString:function(){
var v=dojo.version;
return v.major+"."+v.minor+"."+v.patch+v.flag+" ("+v.revision+")";
}};
true||has.add("extend-dojo",1);
if(1){
dojo.eval=_6cf.eval;
}else{
var _6d5=new Function("__text","return eval(__text);");
dojo.eval=function(text,hint){
return _6d5(text+"\r\n////@ sourceURL="+hint);
};
}
if(0){
dojo.exit=function(_6d6){
quit(_6d6);
};
}else{
dojo.exit=function(){
};
}
true||has.add("dojo-guarantee-console",1);
if(1){
typeof console!="undefined"||(console={});
var cn=["assert","count","debug","dir","dirxml","error","group","groupEnd","info","profile","profileEnd","time","timeEnd","trace","warn","log"];
var tn;
i=0;
while((tn=cn[i++])){
if(!console[tn]){
(function(){
var tcn=tn+"";
console[tcn]=("log" in console)?function(){
var a=Array.apply({},arguments);
a.unshift(tcn+":");
console["log"](a.join(" "));
}:function(){
};
console[tcn]._fake=true;
})();
}
}
}
has.add("dojo-debug-messages",!!_6ce.isDebug);
if(has("dojo-debug-messages")){
dojo.deprecated=function(_6d7,_6d8,_6d9){
var _6da="DEPRECATED: "+_6d7;
if(_6d8){
_6da+=" "+_6d8;
}
if(_6d9){
_6da+=" -- will be removed in version: "+_6d9;
}
console.warn(_6da);
};
dojo.experimental=function(_6db,_6dc){
var _6dd="EXPERIMENTAL: "+_6db+" -- APIs subject to change without notice.";
if(_6dc){
_6dd+=" "+_6dc;
}
console.warn(_6dd);
};
}else{
dojo.deprecated=dojo.experimental=function(){
};
}
true||has.add("dojo-modulePaths",1);
if(1){
if(_6ce.modulePaths){
dojo.deprecated("dojo.modulePaths","use paths configuration");
var _6de={};
for(p in _6ce.modulePaths){
_6de[p.replace(/\./g,"/")]=_6ce.modulePaths[p];
}
_6cf({paths:_6de});
}
}
true||has.add("dojo-moduleUrl",1);
if(1){
dojo.moduleUrl=function(_6df,url){
dojo.deprecated("dojo.moduleUrl()","use require.toUrl","2.0");
var _6e0=null;
if(_6df){
_6e0=_6cf.toUrl(_6df.replace(/\./g,"/")+(url?("/"+url):"")+"/*.*").replace(/\/\*\.\*/,"")+(url?"":"/");
}
return _6e0;
};
}
dojo._hasResource={};
return dojo;
});
},"dojo/date":function(){
define("dojo/date",["./_base/kernel","./_base/lang"],function(dojo,lang){
lang.getObject("date",true,dojo);
dojo.date.getDaysInMonth=function(_6e1){
var _6e2=_6e1.getMonth();
var days=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_6e2==1&&dojo.date.isLeapYear(_6e1)){
return 29;
}
return days[_6e2];
};
dojo.date.isLeapYear=function(_6e3){
var year=_6e3.getFullYear();
return !(year%400)||(!(year%4)&&!!(year%100));
};
dojo.date.getTimezoneName=function(_6e4){
var str=_6e4.toString();
var tz="";
var _6e5;
var pos=str.indexOf("(");
if(pos>-1){
tz=str.substring(++pos,str.indexOf(")"));
}else{
var pat=/([A-Z\/]+) \d{4}$/;
if((_6e5=str.match(pat))){
tz=_6e5[1];
}else{
str=_6e4.toLocaleString();
pat=/ ([A-Z\/]+)$/;
if((_6e5=str.match(pat))){
tz=_6e5[1];
}
}
}
return (tz=="AM"||tz=="PM")?"":tz;
};
dojo.date.compare=function(_6e6,_6e7,_6e8){
_6e6=new Date(+_6e6);
_6e7=new Date(+(_6e7||new Date()));
if(_6e8=="date"){
_6e6.setHours(0,0,0,0);
_6e7.setHours(0,0,0,0);
}else{
if(_6e8=="time"){
_6e6.setFullYear(0,0,0);
_6e7.setFullYear(0,0,0);
}
}
if(_6e6>_6e7){
return 1;
}
if(_6e6<_6e7){
return -1;
}
return 0;
};
dojo.date.add=function(date,_6e9,_6ea){
var sum=new Date(+date);
var _6eb=false;
var _6ec="Date";
switch(_6e9){
case "day":
break;
case "weekday":
var days,_6ed;
var mod=_6ea%5;
if(!mod){
days=(_6ea>0)?5:-5;
_6ed=(_6ea>0)?((_6ea-5)/5):((_6ea+5)/5);
}else{
days=mod;
_6ed=parseInt(_6ea/5);
}
var strt=date.getDay();
var adj=0;
if(strt==6&&_6ea>0){
adj=1;
}else{
if(strt==0&&_6ea<0){
adj=-1;
}
}
var trgt=strt+days;
if(trgt==0||trgt==6){
adj=(_6ea>0)?2:-2;
}
_6ea=(7*_6ed)+days+adj;
break;
case "year":
_6ec="FullYear";
_6eb=true;
break;
case "week":
_6ea*=7;
break;
case "quarter":
_6ea*=3;
case "month":
_6eb=true;
_6ec="Month";
break;
default:
_6ec="UTC"+_6e9.charAt(0).toUpperCase()+_6e9.substring(1)+"s";
}
if(_6ec){
sum["set"+_6ec](sum["get"+_6ec]()+_6ea);
}
if(_6eb&&(sum.getDate()<date.getDate())){
sum.setDate(0);
}
return sum;
};
dojo.date.difference=function(_6ee,_6ef,_6f0){
_6ef=_6ef||new Date();
_6f0=_6f0||"day";
var _6f1=_6ef.getFullYear()-_6ee.getFullYear();
var _6f2=1;
switch(_6f0){
case "quarter":
var m1=_6ee.getMonth();
var m2=_6ef.getMonth();
var q1=Math.floor(m1/3)+1;
var q2=Math.floor(m2/3)+1;
q2+=(_6f1*4);
_6f2=q2-q1;
break;
case "weekday":
var days=Math.round(dojo.date.difference(_6ee,_6ef,"day"));
var _6f3=parseInt(dojo.date.difference(_6ee,_6ef,"week"));
var mod=days%7;
if(mod==0){
days=_6f3*5;
}else{
var adj=0;
var aDay=_6ee.getDay();
var bDay=_6ef.getDay();
_6f3=parseInt(days/7);
mod=days%7;
var _6f4=new Date(_6ee);
_6f4.setDate(_6f4.getDate()+(_6f3*7));
var _6f5=_6f4.getDay();
if(days>0){
switch(true){
case aDay==6:
adj=-1;
break;
case aDay==0:
adj=0;
break;
case bDay==6:
adj=-1;
break;
case bDay==0:
adj=-2;
break;
case (_6f5+mod)>5:
adj=-2;
}
}else{
if(days<0){
switch(true){
case aDay==6:
adj=0;
break;
case aDay==0:
adj=1;
break;
case bDay==6:
adj=2;
break;
case bDay==0:
adj=1;
break;
case (_6f5+mod)<0:
adj=2;
}
}
}
days+=adj;
days-=(_6f3*2);
}
_6f2=days;
break;
case "year":
_6f2=_6f1;
break;
case "month":
_6f2=(_6ef.getMonth()-_6ee.getMonth())+(_6f1*12);
break;
case "week":
_6f2=parseInt(dojo.date.difference(_6ee,_6ef,"day")/7);
break;
case "day":
_6f2/=24;
case "hour":
_6f2/=60;
case "minute":
_6f2/=60;
case "second":
_6f2/=1000;
case "millisecond":
_6f2*=_6ef.getTime()-_6ee.getTime();
}
return Math.round(_6f2);
};
return dojo.date;
});
},"dojox/mobile/ExpandingTextArea":function(){
define("dojox/mobile/ExpandingTextArea",["dojo/_base/declare","dijit/form/_ExpandingTextAreaMixin","./TextArea"],function(_6f6,_6f7,_6f8){
return _6f6("dojox.mobile.ExpandingTextArea",[_6f8,_6f7],{baseClass:"mblTextArea mblExpandingTextArea"});
});
},"dijit/form/_CheckBoxMixin":function(){
define("dijit/form/_CheckBoxMixin",["dojo/_base/declare","dojo/dom-attr","dojo/_base/event"],function(_6f9,_6fa,_6fb){
return _6f9("dijit.form._CheckBoxMixin",null,{type:"checkbox",value:"on",readOnly:false,_aria_attr:"aria-checked",_setReadOnlyAttr:function(_6fc){
this._set("readOnly",_6fc);
_6fa.set(this.focusNode,"readOnly",_6fc);
this.focusNode.setAttribute("aria-readonly",_6fc);
},_setLabelAttr:undefined,postMixInProperties:function(){
if(this.value==""){
this.value="on";
}
this.inherited(arguments);
},reset:function(){
this.inherited(arguments);
this._set("value",this.params.value||"on");
_6fa.set(this.focusNode,"value",this.value);
},_onClick:function(e){
if(this.readOnly){
_6fb.stop(e);
return false;
}
return this.inherited(arguments);
}});
});
},"dojo/main":function(){
define(["./_base/kernel","./has","require","./_base/sniff","./_base/lang","./_base/array","./ready","./_base/declare","./_base/connect","./_base/Deferred","./_base/json","./_base/Color","./has!dojo-firebug?./_firebug/firebug","./_base/browser","./_base/loader"],function(dojo,has,_6fd,_6fe,lang,_6ff,_700){
if(dojo.config.isDebug){
_6fd(["./_firebug/firebug"]);
}
true||has.add("dojo-config-require",1);
if(1){
var deps=dojo.config.require;
if(deps){
deps=_6ff.map(lang.isArray(deps)?deps:[deps],function(item){
return item.replace(/\./g,"/");
});
if(dojo.isAsync){
_6fd(deps);
}else{
_700(1,function(){
_6fd(deps);
});
}
}
}
return dojo;
});
},"dojo/on":function(){
define(["./has!dom-addeventlistener?:./aspect","./_base/kernel","./has"],function(_701,dojo,has){
"use strict";
if(1){
var _702=window.ScriptEngineMajorVersion;
has.add("jscript",_702&&(_702()+ScriptEngineMinorVersion()/10));
has.add("event-orientationchange",has("touch")&&!has("android"));
}
var on=function(_703,type,_704,_705){
if(_703.on){
return _703.on(type,_704);
}
return on.parse(_703,type,_704,_706,_705,this);
};
on.pausable=function(_707,type,_708,_709){
var _70a;
var _70b=on(_707,type,function(){
if(!_70a){
return _708.apply(this,arguments);
}
},_709);
_70b.pause=function(){
_70a=true;
};
_70b.resume=function(){
_70a=false;
};
return _70b;
};
on.once=function(_70c,type,_70d,_70e){
var _70f=on(_70c,type,function(){
_70f.remove();
return _70d.apply(this,arguments);
});
return _70f;
};
on.parse=function(_710,type,_711,_712,_713,_714){
if(type.call){
return type.call(_714,_710,_711);
}
if(type.indexOf(",")>-1){
var _715=type.split(/\s*,\s*/);
var _716=[];
var i=0;
var _717;
while(_717=_715[i++]){
_716.push(_712(_710,_717,_711,_713,_714));
}
_716.remove=function(){
for(var i=0;i<_716.length;i++){
_716[i].remove();
}
};
return _716;
}
return _712(_710,type,_711,_713,_714);
};
var _718=/^touch/;
function _706(_719,type,_71a,_71b,_71c){
var _71d=type.match(/(.*):(.*)/);
if(_71d){
type=_71d[2];
_71d=_71d[1];
return on.selector(_71d,type).call(_71c,_719,_71a);
}
if(has("touch")){
if(_718.test(type)){
_71a=_71e(_71a);
}
if(!has("event-orientationchange")&&(type=="orientationchange")){
type="resize";
_719=window;
_71a=_71e(_71a);
}
}
if(_719.addEventListener){
var _71f=type in _720;
_719.addEventListener(_71f?_720[type]:type,_71a,_71f);
return {remove:function(){
_719.removeEventListener(type,_71a,_71f);
}};
}
type="on"+type;
if(_721&&_719.attachEvent){
return _721(_719,type,_71a);
}
throw new Error("Target must be an event emitter");
};
on.selector=function(_722,_723,_724){
return function(_725,_726){
var _727=this;
var _728=_723.bubble;
if(_728){
_723=_728;
}else{
if(_724!==false){
_724=true;
}
}
return on(_725,_723,function(_729){
var _72a=_729.target;
_727=_727&&_727.matches?_727:dojo.query;
while(!_727.matches(_72a,_722,_725)){
if(_72a==_725||!_724||!(_72a=_72a.parentNode)){
return;
}
}
return _726.call(_72a,_729);
});
};
};
function _72b(){
this.cancelable=false;
};
function _72c(){
this.bubbles=false;
};
var _72d=[].slice,_72e=on.emit=function(_72f,type,_730){
var args=_72d.call(arguments,2);
var _731="on"+type;
if("parentNode" in _72f){
var _732=args[0]={};
for(var i in _730){
_732[i]=_730[i];
}
_732.preventDefault=_72b;
_732.stopPropagation=_72c;
_732.target=_72f;
_732.type=type;
_730=_732;
}
do{
_72f[_731]&&_72f[_731].apply(_72f,args);
}while(_730&&_730.bubbles&&(_72f=_72f.parentNode));
return _730&&_730.cancelable&&_730;
};
var _720={};
if(has("dom-addeventlistener")){
_720={focusin:"focus",focusout:"blur"};
if(has("opera")){
_720.keydown="keypress";
}
on.emit=function(_733,type,_734){
if(_733.dispatchEvent&&document.createEvent){
var _735=document.createEvent("HTMLEvents");
_735.initEvent(type,!!_734.bubbles,!!_734.cancelable);
for(var i in _734){
var _736=_734[i];
if(!(i in _735)){
_735[i]=_734[i];
}
}
return _733.dispatchEvent(_735)&&_735;
}
return _72e.apply(on,arguments);
};
}else{
on._fixEvent=function(evt,_737){
if(!evt){
var w=_737&&(_737.ownerDocument||_737.document||_737).parentWindow||window;
evt=w.event;
}
if(!evt){
return (evt);
}
if(!evt.target){
evt.target=evt.srcElement;
evt.currentTarget=(_737||evt.srcElement);
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
if(!evt.stopPropagation){
evt.stopPropagation=_738;
evt.preventDefault=_739;
}
switch(evt.type){
case "keypress":
var c=("charCode" in evt?evt.charCode:evt.keyCode);
if(c==10){
c=0;
evt.keyCode=13;
}else{
if(c==13||c==27){
c=0;
}else{
if(c==3){
c=99;
}
}
}
evt.charCode=c;
_73a(evt);
break;
}
}
return evt;
};
var _73b=function(_73c){
this.handle=_73c;
};
_73b.prototype.remove=function(){
delete _dojoIEListeners_[this.handle];
};
var _73d=function(_73e){
return function(evt){
evt=on._fixEvent(evt,this);
return _73e.call(this,evt);
};
};
var _721=function(_73f,type,_740){
_740=_73d(_740);
if(((_73f.ownerDocument?_73f.ownerDocument.parentWindow:_73f.parentWindow||_73f.window||window)!=top||has("jscript")<5.8)&&!has("config-_allow_leaks")){
if(typeof _dojoIEListeners_=="undefined"){
_dojoIEListeners_=[];
}
var _741=_73f[type];
if(!_741||!_741.listeners){
var _742=_741;
_73f[type]=_741=Function("event","var callee = arguments.callee; for(var i = 0; i<callee.listeners.length; i++){var listener = _dojoIEListeners_[callee.listeners[i]]; if(listener){listener.call(this,event);}}");
_741.listeners=[];
_741.global=this;
if(_742){
_741.listeners.push(_dojoIEListeners_.push(_742)-1);
}
}
var _743;
_741.listeners.push(_743=(_741.global._dojoIEListeners_.push(_740)-1));
return new _73b(_743);
}
return _701.after(_73f,type,_740,true);
};
var _73a=function(evt){
evt.keyChar=evt.charCode?String.fromCharCode(evt.charCode):"";
evt.charOrCode=evt.keyChar||evt.keyCode;
};
var _738=function(){
this.cancelBubble=true;
};
var _739=on._preventDefault=function(){
this.bubbledKeyCode=this.keyCode;
if(this.ctrlKey){
try{
this.keyCode=0;
}
catch(e){
}
}
this.returnValue=false;
};
}
if(has("touch")){
var _744=function(){
};
var _745=window.orientation;
var _71e=function(_746){
return function(_747){
var _748=_747.corrected;
if(!_748){
var type=_747.type;
try{
delete _747.type;
}
catch(e){
}
if(_747.type){
_744.prototype=_747;
var _748=new _744;
_748.preventDefault=function(){
_747.preventDefault();
};
_748.stopPropagation=function(){
_747.stopPropagation();
};
}else{
_748=_747;
_748.type=type;
}
_747.corrected=_748;
if(type=="resize"){
if(_745==window.orientation){
return null;
}
_745=window.orientation;
_748.type="orientationchange";
return _746.call(this,_748);
}
if(!("rotation" in _748)){
_748.rotation=0;
_748.scale=1;
}
var _749=_748.changedTouches[0];
for(var i in _749){
delete _748[i];
_748[i]=_749[i];
}
}
return _746.call(this,_748);
};
};
}
return on;
});
},"dojox/mobile/app/_event":function(){
define(["dijit","dojo","dojox"],function(_74a,dojo,_74b){
dojo.provide("dojox.mobile.app._event");
dojo.experimental("dojox.mobile.app._event.js");
dojo.mixin(_74b.mobile.app,{eventMap:{},connectFlick:function(_74c,_74d,_74e){
var _74f;
var _750;
var _751=false;
var _752;
var _753;
var _754;
var _755;
var _756;
var time;
var _757=dojo.connect("onmousedown",_74c,function(_758){
_751=false;
_74f=_758.targetTouches?_758.targetTouches[0].clientX:_758.clientX;
_750=_758.targetTouches?_758.targetTouches[0].clientY:_758.clientY;
time=(new Date()).getTime();
_754=dojo.connect(_74c,"onmousemove",_759);
_755=dojo.connect(_74c,"onmouseup",onUp);
});
var _759=function(_75a){
dojo.stopEvent(_75a);
_752=_75a.targetTouches?_75a.targetTouches[0].clientX:_75a.clientX;
_753=_75a.targetTouches?_75a.targetTouches[0].clientY:_75a.clientY;
if(Math.abs(Math.abs(_752)-Math.abs(_74f))>15){
_751=true;
_756=(_752>_74f)?"ltr":"rtl";
}else{
if(Math.abs(Math.abs(_753)-Math.abs(_750))>15){
_751=true;
_756=(_753>_750)?"ttb":"btt";
}
}
};
var onUp=function(_75b){
dojo.stopEvent(_75b);
_754&&dojo.disconnect(_754);
_755&&dojo.disconnect(_755);
if(_751){
var _75c={target:_74c,direction:_756,duration:(new Date()).getTime()-time};
if(_74d&&_74e){
_74d[_74e](_75c);
}else{
_74e(_75c);
}
}
};
}});
_74b.mobile.app.isIPhone=(dojo.isSafari&&(navigator.userAgent.indexOf("iPhone")>-1||navigator.userAgent.indexOf("iPod")>-1));
_74b.mobile.app.isWebOS=(navigator.userAgent.indexOf("webOS")>-1);
_74b.mobile.app.isAndroid=(navigator.userAgent.toLowerCase().indexOf("android")>-1);
if(_74b.mobile.app.isIPhone||_74b.mobile.app.isAndroid){
_74b.mobile.app.eventMap={onmousedown:"ontouchstart",mousedown:"ontouchstart",onmouseup:"ontouchend",mouseup:"ontouchend",onmousemove:"ontouchmove",mousemove:"ontouchmove"};
}
dojo._oldConnect=dojo._connect;
dojo._connect=function(obj,_75d,_75e,_75f,_760){
_75d=_74b.mobile.app.eventMap[_75d]||_75d;
if(_75d=="flick"||_75d=="onflick"){
if(dojo.global["Mojo"]){
_75d=Mojo.Event.flick;
}else{
return _74b.mobile.app.connectFlick(obj,_75e,_75f);
}
}
return dojo._oldConnect(obj,_75d,_75e,_75f,_760);
};
});
},"dojox/mobile/app/_FormWidget":function(){
define(["dijit","dojo","dojox","dojo/require!dojo/window,dijit/_WidgetBase,dijit/focus"],function(_761,dojo,_762){
dojo.provide("dojox.mobile.app._FormWidget");
dojo.experimental("dojox.mobile.app._FormWidget");
dojo.require("dojo.window");
dojo.require("dijit._WidgetBase");
dojo.require("dijit.focus");
dojo.declare("dojox.mobile.app._FormWidget",_761._WidgetBase,{name:"",alt:"",value:"",type:"text",disabled:false,intermediateChanges:false,scrollOnFocus:false,attributeMap:dojo.delegate(_761._WidgetBase.prototype.attributeMap,{value:"focusNode",id:"focusNode",alt:"focusNode",title:"focusNode"}),postMixInProperties:function(){
this.nameAttrSetting=this.name?("name=\""+this.name.replace(/'/g,"&quot;")+"\""):"";
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
this.connect(this.domNode,"onmousedown","_onMouseDown");
},_setDisabledAttr:function(_763){
this.disabled=_763;
dojo.attr(this.focusNode,"disabled",_763);
if(this.valueNode){
dojo.attr(this.valueNode,"disabled",_763);
}
},_onFocus:function(e){
if(this.scrollOnFocus){
dojo.window.scrollIntoView(this.domNode);
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&!this.readOnly&&this.focusNode&&(dojo.style(this.domNode,"display")!="none");
},focus:function(){
this.focusNode.focus();
},compare:function(val1,val2){
if(typeof val1=="number"&&typeof val2=="number"){
return (isNaN(val1)&&isNaN(val2))?0:val1-val2;
}else{
if(val1>val2){
return 1;
}else{
if(val1<val2){
return -1;
}else{
return 0;
}
}
}
},onChange:function(_764){
},_onChangeActive:false,_handleOnChange:function(_765,_766){
this._lastValue=_765;
if(this._lastValueReported==undefined&&(_766===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_765;
}
if((this.intermediateChanges||_766||_766===undefined)&&((typeof _765!=typeof this._lastValueReported)||this.compare(_765,this._lastValueReported)!=0)){
this._lastValueReported=_765;
if(this._onChangeActive){
if(this._onChangeHandle){
clearTimeout(this._onChangeHandle);
}
this._onChangeHandle=setTimeout(dojo.hitch(this,function(){
this._onChangeHandle=null;
this.onChange(_765);
}),0);
}
}
},create:function(){
this.inherited(arguments);
this._onChangeActive=true;
},destroy:function(){
if(this._onChangeHandle){
clearTimeout(this._onChangeHandle);
this.onChange(this._lastValueReported);
}
this.inherited(arguments);
},_onMouseDown:function(e){
if(this.isFocusable()){
var _767=this.connect(dojo.body(),"onmouseup",function(){
if(this.isFocusable()){
this.focus();
}
this.disconnect(_767);
});
}
},selectInputText:function(_768,_769,stop){
var _76a=dojo.global;
var _76b=dojo.doc;
_768=dojo.byId(_768);
if(isNaN(_769)){
_769=0;
}
if(isNaN(stop)){
stop=_768.value?_768.value.length:0;
}
_761.focus(_768);
if(_76a["getSelection"]&&_768.setSelectionRange){
_768.setSelectionRange(_769,stop);
}
}});
dojo.declare("dojox.mobile.app._FormValueWidget",_762.mobile.app._FormWidget,{readOnly:false,attributeMap:dojo.delegate(_762.mobile.app._FormWidget.prototype.attributeMap,{value:"",readOnly:"focusNode"}),_setReadOnlyAttr:function(_76c){
this.readOnly=_76c;
dojo.attr(this.focusNode,"readOnly",_76c);
},postCreate:function(){
this.inherited(arguments);
if(this._resetValue===undefined){
this._resetValue=this.value;
}
},_setValueAttr:function(_76d,_76e){
this.value=_76d;
this._handleOnChange(_76d,_76e);
},_getValueAttr:function(){
return this._lastValue;
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
}});
});
},"dojox/mobile/_base":function(){
define("dojox/mobile/_base",["./common","./View","./Heading","./RoundRect","./RoundRectCategory","./EdgeToEdgeCategory","./RoundRectList","./EdgeToEdgeList","./ListItem","./Switch","./ToolBarButton","./ProgressIndicator"],function(_76f,View,_770,_771,_772,_773,_774,_775,_776,_777,_778,_779){
return _76f;
});
},"dojox/mobile/Button":function(){
define("dojox/mobile/Button",["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dijit/_WidgetBase","dijit/form/_ButtonMixin","dijit/form/_FormWidgetMixin"],function(_77a,_77b,_77c,_77d,_77e,_77f,_780){
return _77b("dojox.mobile.Button",[_77e,_780,_77f],{baseClass:"mblButton",_setTypeAttr:null,duration:1000,_onClick:function(e){
var ret=this.inherited(arguments);
if(ret&&this.duration>=0){
var _781=this.focusNode||this.domNode;
var _782=(this.baseClass+" "+this["class"]).split(" ");
_782=_77a.map(_782,function(c){
return c+"Selected";
});
_77c.add(_781,_782);
setTimeout(function(){
_77c.remove(_781,_782);
},this.duration);
}
return ret;
},isFocusable:function(){
return false;
},buildRendering:function(){
if(!this.srcNodeRef){
this.srcNodeRef=_77d.create("button",{"type":this.type});
}else{
if(this._cv){
var n=this.srcNodeRef.firstChild;
if(n&&n.nodeType===3){
n.nodeValue=this._cv(n.nodeValue);
}
}
}
this.inherited(arguments);
this.focusNode=this.domNode;
},postCreate:function(){
this.inherited(arguments);
this.connect(this.domNode,"onclick","_onClick");
},_setLabelAttr:function(_783){
this.inherited(arguments,[this._cv?this._cv(_783):_783]);
}});
});
},"dojo/query":function(){
define(["./_base/kernel","./has","./dom","./on","./_base/array","./_base/lang","./selector/_loader","./selector/_loader!default"],function(dojo,has,dom,on,_784,lang,_785,_786){
"use strict";
has.add("array-extensible",function(){
return lang.delegate([],{length:1}).length==1&&!has("bug-for-in-skips-shadowed");
});
var ap=Array.prototype,aps=ap.slice,apc=ap.concat,_787=_784.forEach;
var tnl=function(a,_788,_789){
var _78a=new (_789||this._NodeListCtor||nl)(a);
return _788?_78a._stash(_788):_78a;
};
var _78b=function(f,a,o){
a=[0].concat(aps.call(a,0));
o=o||dojo.global;
return function(node){
a[0]=node;
return f.apply(o,a);
};
};
var _78c=function(f,o){
return function(){
this.forEach(_78b(f,arguments,o));
return this;
};
};
var _78d=function(f,o){
return function(){
return this.map(_78b(f,arguments,o));
};
};
var _78e=function(f,o){
return function(){
return this.filter(_78b(f,arguments,o));
};
};
var _78f=function(f,g,o){
return function(){
var a=arguments,body=_78b(f,a,o);
if(g.call(o||dojo.global,a)){
return this.map(body);
}
this.forEach(body);
return this;
};
};
var _790=function(_791){
var _792=this instanceof nl&&has("array-extensible");
if(typeof _791=="number"){
_791=Array(_791);
}
var _793=(_791&&"length" in _791)?_791:arguments;
if(_792||!_793.sort){
var _794=_792?this:[],l=_794.length=_793.length;
for(var i=0;i<l;i++){
_794[i]=_793[i];
}
if(_792){
return _794;
}
_793=_794;
}
lang._mixin(_793,nlp);
_793._NodeListCtor=function(_795){
return nl(_795);
};
return _793;
};
var nl=_790,nlp=nl.prototype=has("array-extensible")?[]:{};
nl._wrap=nlp._wrap=tnl;
nl._adaptAsMap=_78d;
nl._adaptAsForEach=_78c;
nl._adaptAsFilter=_78e;
nl._adaptWithCondition=_78f;
_787(["slice","splice"],function(name){
var f=ap[name];
nlp[name]=function(){
return this._wrap(f.apply(this,arguments),name=="slice"?this:null);
};
});
_787(["indexOf","lastIndexOf","every","some"],function(name){
var f=_784[name];
nlp[name]=function(){
return f.apply(dojo,[this].concat(aps.call(arguments,0)));
};
});
lang.extend(_790,{constructor:nl,_NodeListCtor:nl,toString:function(){
return this.join(",");
},_stash:function(_796){
this._parent=_796;
return this;
},on:function(_797,_798){
var _799=this.map(function(node){
return on(node,_797,_798);
});
_799.remove=function(){
for(var i=0;i<_799.length;i++){
_799[i].remove();
}
};
return _799;
},end:function(){
if(this._parent){
return this._parent;
}else{
return new this._NodeListCtor(0);
}
},concat:function(item){
var t=lang.isArray(this)?this:aps.call(this,0),m=_784.map(arguments,function(a){
return a&&!lang.isArray(a)&&(typeof _790!="undefined"&&a.constructor===_790||a.constructor===this._NodeListCtor)?aps.call(a,0):a;
});
return this._wrap(apc.apply(t,m),this);
},map:function(func,obj){
return this._wrap(_784.map(this,func,obj),this);
},forEach:function(_79a,_79b){
_787(this,_79a,_79b);
return this;
},filter:function(_79c){
var a=arguments,_79d=this,_79e=0;
if(typeof _79c=="string"){
_79d=_79f._filterResult(this,a[0]);
if(a.length==1){
return _79d._stash(this);
}
_79e=1;
}
return this._wrap(_784.filter(_79d,a[_79e],a[_79e+1]),this);
},instantiate:function(_7a0,_7a1){
var c=lang.isFunction(_7a0)?_7a0:lang.getObject(_7a0);
_7a1=_7a1||{};
return this.forEach(function(node){
new c(_7a1,node);
});
},at:function(){
var t=new this._NodeListCtor(0);
_787(arguments,function(i){
if(i<0){
i=this.length+i;
}
if(this[i]){
t.push(this[i]);
}
},this);
return t._stash(this);
}});
function _7a2(_7a3,_7a4){
var _7a5=function(_7a6,root){
if(typeof root=="string"){
root=dom.byId(root);
if(!root){
return new _7a4([]);
}
}
var _7a7=typeof _7a6=="string"?_7a3(_7a6,root):_7a6.orphan?_7a6:[_7a6];
if(_7a7.orphan){
return _7a7;
}
return new _7a4(_7a7);
};
_7a5.matches=_7a3.match||function(node,_7a8,root){
return _7a5.filter([node],_7a8,root).length>0;
};
_7a5.filter=_7a3.filter||function(_7a9,_7aa,root){
return _7a5(_7aa,root).filter(function(node){
return _784.indexOf(_7a9,node)>-1;
});
};
if(typeof _7a3!="function"){
var _7ab=_7a3.search;
_7a3=function(_7ac,root){
return _7ab(root||document,_7ac);
};
}
return _7a5;
};
var _79f=_7a2(_786,_790);
dojo.query=_7a2(_786,function(_7ad){
return _790(_7ad);
});
_79f.load=function(id,_7ae,_7af,_7b0){
_785.load(id,_7ae,function(_7b1){
_7af(_7a2(_7b1,_790));
});
};
dojo._filterQueryResult=_79f._filterResult=function(_7b2,_7b3,root){
return new _790(_79f.filter(_7b2,_7b3,root));
};
dojo.NodeList=_79f.NodeList=_790;
return _79f;
});
},"dojox/mobile/_ScrollableMixin":function(){
define("dojox/mobile/_ScrollableMixin",["dojo/_base/kernel","dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom","dojo/dom-class","dijit/registry","./scrollable"],function(dojo,_7b4,lang,win,dom,_7b5,_7b6,_7b7){
var cls=_7b4("dojox.mobile._ScrollableMixin",null,{fixedHeader:"",fixedFooter:"",scrollableParams:null,allowNestedScrolls:true,constructor:function(){
this.scrollableParams={};
},destroy:function(){
this.cleanup();
this.inherited(arguments);
},startup:function(){
if(this._started){
return;
}
var node;
var _7b8=this.scrollableParams;
if(this.fixedHeader){
node=dom.byId(this.fixedHeader);
if(node.parentNode==this.domNode){
this.isLocalHeader=true;
}
_7b8.fixedHeaderHeight=node.offsetHeight;
}
if(this.fixedFooter){
node=dom.byId(this.fixedFooter);
if(node.parentNode==this.domNode){
this.isLocalFooter=true;
node.style.bottom="0px";
}
_7b8.fixedFooterHeight=node.offsetHeight;
}
this.init(_7b8);
if(this.allowNestedScrolls){
for(var p=this.getParent();p;p=p.getParent()){
if(p&&p.scrollableParams){
this.isNested=true;
this.dirLock=true;
p.dirLock=true;
break;
}
}
}
this.inherited(arguments);
},findAppBars:function(){
var i,len,c;
for(i=0,len=win.body().childNodes.length;i<len;i++){
c=win.body().childNodes[i];
this.checkFixedBar(c,false);
}
if(this.domNode.parentNode){
for(i=0,len=this.domNode.parentNode.childNodes.length;i<len;i++){
c=this.domNode.parentNode.childNodes[i];
this.checkFixedBar(c,false);
}
}
this.fixedFooterHeight=this.fixedFooter?this.fixedFooter.offsetHeight:0;
},checkFixedBar:function(node,_7b9){
if(node.nodeType===1){
var _7ba=node.getAttribute("fixed")||(_7b6.byNode(node)&&_7b6.byNode(node).fixed);
if(_7ba==="top"){
_7b5.add(node,"mblFixedHeaderBar");
if(_7b9){
node.style.top="0px";
this.fixedHeader=node;
}
return _7ba;
}else{
if(_7ba==="bottom"){
_7b5.add(node,"mblFixedBottomBar");
this.fixedFooter=node;
return _7ba;
}
}
}
return null;
}});
lang.extend(cls,new _7b7(dojo,dojox));
return cls;
});
},"dojox/mobile/Switch":function(){
define("dojox/mobile/Switch",["dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/event","dojo/_base/window","dojo/dom-class","dijit/_Contained","dijit/_WidgetBase","./sniff"],function(_7bb,_7bc,_7bd,_7be,win,_7bf,_7c0,_7c1,has){
return _7bd("dojox.mobile.Switch",[_7c1,_7c0],{value:"on",name:"",leftLabel:"ON",rightLabel:"OFF",_width:53,buildRendering:function(){
this.domNode=win.doc.createElement("DIV");
var c=(this.srcNodeRef&&this.srcNodeRef.className)||this.className||this["class"];
this._swClass=(c||"").replace(/ .*/,"");
this.domNode.className="mblSwitch";
var _7c2=this.name?" name=\""+this.name+"\"":"";
this.domNode.innerHTML="<div class=\"mblSwitchInner\">"+"<div class=\"mblSwitchBg mblSwitchBgLeft\">"+"<div class=\"mblSwitchText mblSwitchTextLeft\"></div>"+"</div>"+"<div class=\"mblSwitchBg mblSwitchBgRight\">"+"<div class=\"mblSwitchText mblSwitchTextRight\"></div>"+"</div>"+"<div class=\"mblSwitchKnob\"></div>"+"<input type=\"hidden\""+_7c2+"></div>"+"</div>";
var n=this.inner=this.domNode.firstChild;
this.left=n.childNodes[0];
this.right=n.childNodes[1];
this.knob=n.childNodes[2];
this.input=n.childNodes[3];
},postCreate:function(){
this.connect(this.domNode,"onclick","onClick");
this.connect(this.domNode,has("touch")?"touchstart":"onmousedown","onTouchStart");
this._initialValue=this.value;
},_changeState:function(_7c3,anim){
var on=(_7c3==="on");
this.left.style.display="";
this.right.style.display="";
this.inner.style.left="";
if(anim){
_7bf.add(this.domNode,"mblSwitchAnimation");
}
_7bf.remove(this.domNode,on?"mblSwitchOff":"mblSwitchOn");
_7bf.add(this.domNode,on?"mblSwitchOn":"mblSwitchOff");
var _7c4=this;
setTimeout(function(){
_7c4.left.style.display=on?"":"none";
_7c4.right.style.display=!on?"":"none";
_7bf.remove(_7c4.domNode,"mblSwitchAnimation");
},anim?300:0);
},startup:function(){
if(this._swClass.indexOf("Round")!=-1){
var r=Math.round(this.domNode.offsetHeight/2);
this.createRoundMask(this._swClass,r,this.domNode.offsetWidth);
}
},createRoundMask:function(_7c5,r,w){
if(!has("webkit")||!_7c5){
return;
}
if(!this._createdMasks){
this._createdMasks=[];
}
if(this._createdMasks[_7c5]){
return;
}
this._createdMasks[_7c5]=1;
var ctx=win.doc.getCSSCanvasContext("2d",_7c5+"Mask",w,100);
ctx.fillStyle="#000000";
ctx.beginPath();
ctx.moveTo(r,0);
ctx.arcTo(0,0,0,2*r,r);
ctx.arcTo(0,2*r,r,2*r,r);
ctx.lineTo(w-r,2*r);
ctx.arcTo(w,2*r,w,r,r);
ctx.arcTo(w,0,w-r,0,r);
ctx.closePath();
ctx.fill();
},onClick:function(e){
if(this._moved){
return;
}
this.value=this.input.value=(this.value=="on")?"off":"on";
this._changeState(this.value,true);
this.onStateChanged(this.value);
},onTouchStart:function(e){
this._moved=false;
this.innerStartX=this.inner.offsetLeft;
if(!this._conn){
this._conn=[];
this._conn.push(_7bc.connect(this.inner,has("touch")?"touchmove":"onmousemove",this,"onTouchMove"));
this._conn.push(_7bc.connect(this.inner,has("touch")?"touchend":"onmouseup",this,"onTouchEnd"));
}
this.touchStartX=e.touches?e.touches[0].pageX:e.clientX;
this.left.style.display="";
this.right.style.display="";
_7be.stop(e);
},onTouchMove:function(e){
e.preventDefault();
var dx;
if(e.targetTouches){
if(e.targetTouches.length!=1){
return false;
}
dx=e.targetTouches[0].clientX-this.touchStartX;
}else{
dx=e.clientX-this.touchStartX;
}
var pos=this.innerStartX+dx;
var d=10;
if(pos<=-(this._width-d)){
pos=-this._width;
}
if(pos>=-d){
pos=0;
}
this.inner.style.left=pos+"px";
if(Math.abs(dx)>d){
this._moved=true;
}
},onTouchEnd:function(e){
_7bb.forEach(this._conn,_7bc.disconnect);
this._conn=null;
if(this.innerStartX==this.inner.offsetLeft){
if(has("touch")){
var ev=win.doc.createEvent("MouseEvents");
ev.initEvent("click",true,true);
this.inner.dispatchEvent(ev);
}
return;
}
var _7c6=(this.inner.offsetLeft<-(this._width/2))?"off":"on";
this._changeState(_7c6,true);
if(_7c6!=this.value){
this.value=this.input.value=_7c6;
this.onStateChanged(_7c6);
}
},onStateChanged:function(_7c7){
},_setValueAttr:function(_7c8){
this._changeState(_7c8,false);
if(this.value!=_7c8){
this.onStateChanged(_7c8);
}
this.value=this.input.value=_7c8;
},_setLeftLabelAttr:function(_7c9){
this.leftLabel=_7c9;
this.left.firstChild.innerHTML=this._cv?this._cv(_7c9):_7c9;
},_setRightLabelAttr:function(_7ca){
this.rightLabel=_7ca;
this.right.firstChild.innerHTML=this._cv?this._cv(_7ca):_7ca;
},reset:function(){
this.set("value",this._initialValue);
}});
});
},"dijit/form/_ToggleButtonMixin":function(){
define("dijit/form/_ToggleButtonMixin",["dojo/_base/declare","dojo/dom-attr"],function(_7cb,_7cc){
return _7cb("dijit.form._ToggleButtonMixin",null,{checked:false,_aria_attr:"aria-pressed",_onClick:function(evt){
var _7cd=this.checked;
this._set("checked",!_7cd);
var ret=this.inherited(arguments);
this.set("checked",ret?this.checked:_7cd);
return ret;
},_setCheckedAttr:function(_7ce,_7cf){
this._set("checked",_7ce);
_7cc.set(this.focusNode||this.domNode,"checked",_7ce);
(this.focusNode||this.domNode).setAttribute(this._aria_attr,_7ce?"true":"false");
this._handleOnChange(_7ce,_7cf);
},reset:function(){
this._hasBeenBlurred=false;
this.set("checked",this.params.checked||false);
}});
});
},"dojo/dom-class":function(){
define(["./_base/lang","./_base/array","./dom"],function(lang,_7d0,dom){
var _7d1="className";
var cls,_7d2=/\s+/,a1=[""];
function _7d3(s){
if(typeof s=="string"||s instanceof String){
if(s&&!_7d2.test(s)){
a1[0]=s;
return a1;
}
var a=s.split(_7d2);
if(a.length&&!a[0]){
a.shift();
}
if(a.length&&!a[a.length-1]){
a.pop();
}
return a;
}
if(!s){
return [];
}
return _7d0.filter(s,function(x){
return x;
});
};
var _7d4={};
cls={contains:function containsClass(node,_7d5){
return ((" "+dom.byId(node)[_7d1]+" ").indexOf(" "+_7d5+" ")>=0);
},add:function addClass(node,_7d6){
node=dom.byId(node);
_7d6=_7d3(_7d6);
var cls=node[_7d1],_7d7;
cls=cls?" "+cls+" ":" ";
_7d7=cls.length;
for(var i=0,len=_7d6.length,c;i<len;++i){
c=_7d6[i];
if(c&&cls.indexOf(" "+c+" ")<0){
cls+=c+" ";
}
}
if(_7d7<cls.length){
node[_7d1]=cls.substr(1,cls.length-2);
}
},remove:function removeClass(node,_7d8){
node=dom.byId(node);
var cls;
if(_7d8!==undefined){
_7d8=_7d3(_7d8);
cls=" "+node[_7d1]+" ";
for(var i=0,len=_7d8.length;i<len;++i){
cls=cls.replace(" "+_7d8[i]+" "," ");
}
cls=lang.trim(cls);
}else{
cls="";
}
if(node[_7d1]!=cls){
node[_7d1]=cls;
}
},replace:function replaceClass(node,_7d9,_7da){
node=dom.byId(node);
_7d4[_7d1]=node[_7d1];
cls.remove(_7d4,_7da);
cls.add(_7d4,_7d9);
if(node[_7d1]!==_7d4[_7d1]){
node[_7d1]=_7d4[_7d1];
}
},toggle:function toggleClass(node,_7db,_7dc){
node=dom.byId(node);
if(_7dc===undefined){
_7db=_7d3(_7db);
for(var i=0,len=_7db.length,c;i<len;++i){
c=_7db[i];
cls[cls.contains(node,c)?"remove":"add"](node,c);
}
}else{
cls[_7dc?"add":"remove"](node,_7db);
}
return _7dc;
}};
return cls;
});
},"dijit/focus":function(){
define("dijit/focus",["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/ready","dojo/_base/sniff","dojo/Stateful","dojo/_base/unload","dojo/_base/window","dojo/window","./a11y","./registry","."],function(_7dd,_7de,dom,_7df,_7e0,_7e1,lang,on,_7e2,has,_7e3,_7e4,win,_7e5,a11y,_7e6,_7e7){
var _7e8=_7de([_7e3,_7e1],{curNode:null,activeStack:[],constructor:function(){
var _7e9=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_7dd.before(_7e0,"empty",_7e9);
_7dd.before(_7e0,"destroy",_7e9);
},registerIframe:function(_7ea){
return this.registerWin(_7ea.contentWindow,_7ea);
},registerWin:function(_7eb,_7ec){
var _7ed=this;
var _7ee=function(evt){
_7ed._justMouseDowned=true;
setTimeout(function(){
_7ed._justMouseDowned=false;
},0);
if(has("ie")&&evt&&evt.srcElement&&evt.srcElement.parentNode==null){
return;
}
_7ed._onTouchNode(_7ec||evt.target||evt.srcElement,"mouse");
};
var doc=has("ie")?_7eb.document.documentElement:_7eb.document;
if(doc){
if(has("ie")){
_7eb.document.body.attachEvent("onmousedown",_7ee);
var _7ef=function(evt){
var tag=evt.srcElement.tagName.toLowerCase();
if(tag=="#document"||tag=="body"){
return;
}
if(a11y.isTabNavigable(evt.srcElement)){
_7ed._onFocusNode(_7ec||evt.srcElement);
}else{
_7ed._onTouchNode(_7ec||evt.srcElement);
}
};
doc.attachEvent("onactivate",_7ef);
var _7f0=function(evt){
_7ed._onBlurNode(_7ec||evt.srcElement);
};
doc.attachEvent("ondeactivate",_7f0);
return {remove:function(){
_7eb.document.detachEvent("onmousedown",_7ee);
doc.detachEvent("onactivate",_7ef);
doc.detachEvent("ondeactivate",_7f0);
doc=null;
}};
}else{
doc.body.addEventListener("mousedown",_7ee,true);
doc.body.addEventListener("touchstart",_7ee,true);
var _7f1=function(evt){
_7ed._onFocusNode(_7ec||evt.target);
};
doc.addEventListener("focus",_7f1,true);
var _7f2=function(evt){
_7ed._onBlurNode(_7ec||evt.target);
};
doc.addEventListener("blur",_7f2,true);
return {remove:function(){
doc.body.removeEventListener("mousedown",_7ee,true);
doc.body.removeEventListener("touchstart",_7ee,true);
doc.removeEventListener("focus",_7f1,true);
doc.removeEventListener("blur",_7f2,true);
doc=null;
}};
}
}
},_onBlurNode:function(){
this.set("prevNode",this.curNode);
this.set("curNode",null);
if(this._justMouseDowned){
return;
}
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
this.prevNode=null;
}),100);
},_onTouchNode:function(node,by){
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
var _7f3=[];
try{
while(node){
var _7f4=_7df.get(node,"dijitPopupParent");
if(_7f4){
node=_7e6.byId(_7f4).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_7e5.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_7f5=id&&_7e6.byId(id);
if(_7f5&&!(by=="mouse"&&_7f5.get("disabled"))){
_7f3.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_7f3,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
this._onTouchNode(node);
if(node==this.curNode){
return;
}
this.set("curNode",node);
},_setStack:function(_7f6,by){
var _7f7=this.activeStack;
this.set("activeStack",_7f6);
for(var _7f8=0;_7f8<Math.min(_7f7.length,_7f6.length);_7f8++){
if(_7f7[_7f8]!=_7f6[_7f8]){
break;
}
}
var _7f9;
for(var i=_7f7.length-1;i>=_7f8;i--){
_7f9=_7e6.byId(_7f7[i]);
if(_7f9){
_7f9._hasBeenBlurred=true;
_7f9.set("focused",false);
if(_7f9._focusManager==this){
_7f9._onBlur(by);
}
this.emit("widget-blur",_7f9,by);
}
}
for(i=_7f8;i<_7f6.length;i++){
_7f9=_7e6.byId(_7f6[i]);
if(_7f9){
_7f9.set("focused",true);
if(_7f9._focusManager==this){
_7f9._onFocus(by);
}
this.emit("widget-focus",_7f9,by);
}
}
},focus:function(node){
if(node){
try{
node.focus();
}
catch(e){
}
}
}});
var _7fa=new _7e8();
_7e2(function(){
var _7fb=_7fa.registerWin(win.doc.parentWindow||win.doc.defaultView);
if(has("ie")){
_7e4.addOnWindowUnload(function(){
_7fb.remove();
_7fb=null;
});
}
});
_7e7.focus=function(node){
_7fa.focus(node);
};
for(var attr in _7fa){
if(!/^_/.test(attr)){
_7e7.focus[attr]=typeof _7fa[attr]=="function"?lang.hitch(_7fa,attr):_7fa[attr];
}
}
_7fa.watch(function(attr,_7fc,_7fd){
_7e7.focus[attr]=_7fd;
});
return _7fa;
});
},"dojo/dom-attr":function(){
define(["exports","./_base/sniff","./_base/lang","./dom","./dom-style","./dom-prop"],function(_7fe,has,lang,dom,_7ff,prop){
var _800={innerHTML:1,className:1,htmlFor:has("ie"),value:1},_801={classname:"class",htmlfor:"for",tabindex:"tabIndex",readonly:"readOnly"};
function _802(node,name){
var attr=node.getAttributeNode&&node.getAttributeNode(name);
return attr&&attr.specified;
};
_7fe.has=function hasAttr(node,name){
var lc=name.toLowerCase();
return _800[prop.names[lc]||name]||_802(dom.byId(node),_801[lc]||name);
};
_7fe.get=function getAttr(node,name){
node=dom.byId(node);
var lc=name.toLowerCase(),_803=prop.names[lc]||name,_804=_800[_803];
value=node[_803];
if(_804&&typeof value!="undefined"){
return value;
}
if(_803!="href"&&(typeof value=="boolean"||lang.isFunction(value))){
return value;
}
var _805=_801[lc]||name;
return _802(node,_805)?node.getAttribute(_805):null;
};
_7fe.set=function setAttr(node,name,_806){
node=dom.byId(node);
if(arguments.length==2){
for(var x in name){
_7fe.set(node,x,name[x]);
}
return node;
}
var lc=name.toLowerCase(),_807=prop.names[lc]||name,_808=_800[_807];
if(_807=="style"&&typeof _806!="string"){
_7ff.set(node,_806);
return node;
}
if(_808||typeof _806=="boolean"||lang.isFunction(_806)){
return prop.set(node,name,_806);
}
node.setAttribute(_801[lc]||name,_806);
return node;
};
_7fe.remove=function removeAttr(node,name){
dom.byId(node).removeAttribute(_801[name.toLowerCase()]||name);
};
_7fe.getNodeProp=function getNodeProp(node,name){
node=dom.byId(node);
var lc=name.toLowerCase(),_809=prop.names[lc]||name;
if((_809 in node)&&_809!="href"){
return node[_809];
}
var _80a=_801[lc]||name;
return _802(node,_80a)?node.getAttribute(_80a):null;
};
});
},"dojox/mobile/SpinWheelSlot":function(){
define("dojox/mobile/SpinWheelSlot",["dojo/_base/declare","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dijit/_Contained","dijit/_WidgetBase","./_ScrollableMixin"],function(_80b,win,_80c,_80d,_80e,_80f,_810){
return _80b("dojox.mobile.SpinWheelSlot",[_80f,_80e,_810],{items:[],labels:[],labelFrom:0,labelTo:0,value:"",maxSpeed:500,minItems:15,centerPos:0,scrollBar:false,constraint:false,allowNestedScrolls:false,androidWorkaroud:false,buildRendering:function(){
this.inherited(arguments);
_80c.add(this.domNode,"mblSpinWheelSlot");
var i,j,idx;
if(this.labelFrom!==this.labelTo){
this.labels=[];
for(i=this.labelFrom,idx=0;i<=this.labelTo;i++,idx++){
this.labels[idx]=String(i);
}
}
if(this.labels.length>0){
this.items=[];
for(i=0;i<this.labels.length;i++){
this.items.push([i,this.labels[i]]);
}
}
this.containerNode=_80d.create("DIV",{className:"mblSpinWheelSlotContainer"});
this.containerNode.style.height=(win.global.innerHeight||win.doc.documentElement.clientHeight)*2+"px";
this.panelNodes=[];
for(var k=0;k<3;k++){
this.panelNodes[k]=_80d.create("DIV",{className:"mblSpinWheelSlotPanel"});
var len=this.items.length;
var n=Math.ceil(this.minItems/len);
for(j=0;j<n;j++){
for(i=0;i<len;i++){
_80d.create("DIV",{className:"mblSpinWheelSlotLabel",name:this.items[i][0],innerHTML:this._cv?this._cv(this.items[i][1]):this.items[i][1]},this.panelNodes[k]);
}
}
this.containerNode.appendChild(this.panelNodes[k]);
}
this.domNode.appendChild(this.containerNode);
this.touchNode=_80d.create("DIV",{className:"mblSpinWheelSlotTouch"},this.domNode);
this.setSelectable(this.domNode,false);
},startup:function(){
this.inherited(arguments);
this.centerPos=this.getParent().centerPos;
var _811=this.panelNodes[1].childNodes;
this._itemHeight=_811[0].offsetHeight;
this.adjust();
},adjust:function(){
var _812=this.panelNodes[1].childNodes;
var _813;
for(var i=0,len=_812.length;i<len;i++){
var item=_812[i];
if(item.offsetTop<=this.centerPos&&this.centerPos<item.offsetTop+item.offsetHeight){
_813=this.centerPos-(item.offsetTop+Math.round(item.offsetHeight/2));
break;
}
}
var h=this.panelNodes[0].offsetHeight;
this.panelNodes[0].style.top=-h+_813+"px";
this.panelNodes[1].style.top=_813+"px";
this.panelNodes[2].style.top=h+_813+"px";
},setInitialValue:function(){
if(this.items.length>0){
var val=(this.value!=="")?this.value:this.items[0][1];
this.setValue(val);
}
},getCenterPanel:function(){
var pos=this.getPos();
for(var i=0,len=this.panelNodes.length;i<len;i++){
var top=pos.y+this.panelNodes[i].offsetTop;
if(top<=this.centerPos&&this.centerPos<top+this.panelNodes[i].offsetHeight){
return this.panelNodes[i];
}
}
return null;
},setColor:function(_814){
for(var i=0,len=this.panelNodes.length;i<len;i++){
var _815=this.panelNodes[i].childNodes;
for(var j=0;j<_815.length;j++){
if(_815[j].innerHTML===String(_814)){
_80c.add(_815[j],"mblSpinWheelSlotLabelBlue");
}else{
_80c.remove(_815[j],"mblSpinWheelSlotLabelBlue");
}
}
}
},disableValues:function(_816){
for(var i=0,len=this.panelNodes.length;i<len;i++){
var _817=this.panelNodes[i].childNodes;
for(var j=0;j<_817.length;j++){
_80c.remove(_817[j],"mblSpinWheelSlotLabelGray");
for(var k=0;k<_816.length;k++){
if(_817[j].innerHTML===String(_816[k])){
_80c.add(_817[j],"mblSpinWheelSlotLabelGray");
break;
}
}
}
}
},getCenterItem:function(){
var pos=this.getPos();
var _818=this.getCenterPanel();
if(_818){
var top=pos.y+_818.offsetTop;
var _819=_818.childNodes;
for(var i=0,len=_819.length;i<len;i++){
if(top+_819[i].offsetTop<=this.centerPos&&this.centerPos<top+_819[i].offsetTop+_819[i].offsetHeight){
return _819[i];
}
}
}
return null;
},getValue:function(){
var item=this.getCenterItem();
return (item&&item.innerHTML);
},getKey:function(){
return this.getCenterItem().getAttribute("name");
},setValue:function(_81a){
var idx0,idx1;
var _81b=this.getValue();
if(!_81b){
this._penddingValue=_81a;
return;
}
this._penddingValue=undefined;
var n=this.items.length;
for(var i=0;i<n;i++){
if(this.items[i][1]===String(_81b)){
idx0=i;
}
if(this.items[i][1]===String(_81a)){
idx1=i;
}
if(idx0!==undefined&&idx1!==undefined){
break;
}
}
var d=idx1-(idx0||0);
var m;
if(d>0){
m=(d<n-d)?-d:n-d;
}else{
m=(-d<n+d)?-d:-(n+d);
}
var to=this.getPos();
to.y+=m*this._itemHeight;
this.slideTo(to,1);
},getSpeed:function(){
var y=0,n=this._time.length;
var _81c=(new Date()).getTime()-this.startTime-this._time[n-1];
if(n>=2&&_81c<200){
var dy=this._posY[n-1]-this._posY[(n-6)>=0?n-6:0];
var dt=this._time[n-1]-this._time[(n-6)>=0?n-6:0];
y=this.calcSpeed(dy,dt);
}
return {x:0,y:y};
},calcSpeed:function(d,t){
var _81d=this.inherited(arguments);
if(!_81d){
return 0;
}
var v=Math.abs(_81d);
var ret=_81d;
if(v>this.maxSpeed){
ret=this.maxSpeed*(_81d/v);
}
return ret;
},adjustDestination:function(to,pos){
var h=this._itemHeight;
var j=to.y+Math.round(h/2);
var a=Math.abs(j);
var r=j>=0?j%h:j%h+h;
to.y=j-r;
},resize:function(e){
if(this._penddingValue){
this.setValue(this._penddingValue);
}
},slideTo:function(to,_81e,_81f){
var pos=this.getPos();
var top=pos.y+this.panelNodes[1].offsetTop;
var _820=top+this.panelNodes[1].offsetHeight;
var vh=this.domNode.parentNode.offsetHeight;
var t;
if(pos.y<to.y){
if(_820>vh){
t=this.panelNodes[2];
t.style.top=this.panelNodes[0].offsetTop-this.panelNodes[0].offsetHeight+"px";
this.panelNodes[2]=this.panelNodes[1];
this.panelNodes[1]=this.panelNodes[0];
this.panelNodes[0]=t;
}
}else{
if(pos.y>to.y){
if(top<0){
t=this.panelNodes[0];
t.style.top=this.panelNodes[2].offsetTop+this.panelNodes[2].offsetHeight+"px";
this.panelNodes[0]=this.panelNodes[1];
this.panelNodes[1]=this.panelNodes[2];
this.panelNodes[2]=t;
}
}
}
if(!this._initialized){
_81e=0;
this._initialized=true;
}else{
if(Math.abs(this._speed.y)<40){
_81e=0.2;
}
}
this.inherited(arguments,[to,_81e,_81f]);
}});
});
},"dojox/mobile/TabBar":function(){
define("dojox/mobile/TabBar",["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./Heading","./TabBarButton"],function(_821,_822,_823,_824,_825,_826,_827,_828,_829,_82a,_82b){
return _822("dojox.mobile.TabBar",[_829,_828,_827],{iconBase:"",iconPos:"",barType:"tabBar",inHeading:false,tag:"UL",_fixedButtonWidth:76,_fixedButtonMargin:17,_largeScreenWidth:500,buildRendering:function(){
this._clsName=this.barType=="segmentedControl"?"mblTabButton":"mblTabBarButton";
this.domNode=this.containerNode=this.srcNodeRef||_824.create(this.tag);
this.domNode.className=this.barType=="segmentedControl"?"mblTabPanelHeader":"mblTabBar";
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
this.resize();
},resize:function(size){
var i,w;
if(size&&size.w){
_825.setMarginBox(this.domNode,size);
w=size.w;
}else{
w=_826.get(this.domNode,"position")==="absolute"?_825.getContentBox(this.domNode).w:_825.getMarginBox(this.domNode).w;
}
var bw=this._fixedButtonWidth;
var bm=this._fixedButtonMargin;
var _82c=this.containerNode.childNodes;
var arr=[];
for(i=0;i<_82c.length;i++){
var c=_82c[i];
if(c.nodeType!=1){
continue;
}
if(_823.contains(c,this._clsName)){
arr.push(c);
}
}
var _82d;
if(this.barType=="segmentedControl"){
_82d=w;
var _82e=0;
for(i=0;i<arr.length;i++){
_82d-=_825.getMarginBox(arr[i]).w;
_82e+=arr[i].offsetWidth;
}
_82d=Math.floor(_82d/2);
var _82f=this.getParent();
var _830=this.inHeading||_82f instanceof _82a;
this.containerNode.style.padding=(_830?0:3)+"px 0px 0px "+(_830?0:_82d)+"px";
if(_830){
_826.set(this.domNode,{background:"none",border:"none",width:_82e+2+"px"});
}
_823.add(this.domNode,"mblTabBar"+(_830?"Head":"Top"));
}else{
_82d=Math.floor((w-(bw+bm*2)*arr.length)/2);
if(w<this._largeScreenWidth||_82d<0){
for(i=0;i<arr.length;i++){
arr[i].style.width=Math.round(98/arr.length)+"%";
arr[i].style.margin="0px";
}
this.containerNode.style.padding="0px 0px 0px 1%";
}else{
for(i=0;i<arr.length;i++){
arr[i].style.width=bw+"px";
arr[i].style.margin="0 "+bm+"px";
}
if(arr.length>0){
arr[0].style.marginLeft=_82d+bm+"px";
}
this.containerNode.style.padding="0px";
}
}
if(!_821.some(this.getChildren(),function(_831){
return _831.iconNode1;
})){
_823.add(this.domNode,"mblTabBarNoIcons");
}else{
_823.remove(this.domNode,"mblTabBarNoIcons");
}
if(!_821.some(this.getChildren(),function(_832){
return _832.label;
})){
_823.add(this.domNode,"mblTabBarNoText");
}else{
_823.remove(this.domNode,"mblTabBarNoText");
}
}});
});
},"dojo/selector/acme":function(){
define(["../_base/kernel","../has","../dom","../_base/sniff","../_base/array","../_base/lang","../_base/window"],function(dojo,has,dom){
var trim=dojo.trim;
var each=dojo.forEach;
var _833=function(){
return dojo.doc;
};
var _834=((dojo.isWebKit||dojo.isMozilla)&&((_833().compatMode)=="BackCompat"));
var _835=">~+";
var _836=false;
var _837=function(){
return true;
};
var _838=function(_839){
if(_835.indexOf(_839.slice(-1))>=0){
_839+=" * ";
}else{
_839+=" ";
}
var ts=function(s,e){
return trim(_839.slice(s,e));
};
var _83a=[];
var _83b=-1,_83c=-1,_83d=-1,_83e=-1,_83f=-1,inId=-1,_840=-1,lc="",cc="",_841;
var x=0,ql=_839.length,_842=null,_843=null;
var _844=function(){
if(_840>=0){
var tv=(_840==x)?null:ts(_840,x);
_842[(_835.indexOf(tv)<0)?"tag":"oper"]=tv;
_840=-1;
}
};
var _845=function(){
if(inId>=0){
_842.id=ts(inId,x).replace(/\\/g,"");
inId=-1;
}
};
var _846=function(){
if(_83f>=0){
_842.classes.push(ts(_83f+1,x).replace(/\\/g,""));
_83f=-1;
}
};
var _847=function(){
_845();
_844();
_846();
};
var _848=function(){
_847();
if(_83e>=0){
_842.pseudos.push({name:ts(_83e+1,x)});
}
_842.loops=(_842.pseudos.length||_842.attrs.length||_842.classes.length);
_842.oquery=_842.query=ts(_841,x);
_842.otag=_842.tag=(_842["oper"])?null:(_842.tag||"*");
if(_842.tag){
_842.tag=_842.tag.toUpperCase();
}
if(_83a.length&&(_83a[_83a.length-1].oper)){
_842.infixOper=_83a.pop();
_842.query=_842.infixOper.query+" "+_842.query;
}
_83a.push(_842);
_842=null;
};
for(;lc=cc,cc=_839.charAt(x),x<ql;x++){
if(lc=="\\"){
continue;
}
if(!_842){
_841=x;
_842={query:null,pseudos:[],attrs:[],classes:[],tag:null,oper:null,id:null,getTag:function(){
return (_836)?this.otag:this.tag;
}};
_840=x;
}
if(_83b>=0){
if(cc=="]"){
if(!_843.attr){
_843.attr=ts(_83b+1,x);
}else{
_843.matchFor=ts((_83d||_83b+1),x);
}
var cmf=_843.matchFor;
if(cmf){
if((cmf.charAt(0)=="\"")||(cmf.charAt(0)=="'")){
_843.matchFor=cmf.slice(1,-1);
}
}
_842.attrs.push(_843);
_843=null;
_83b=_83d=-1;
}else{
if(cc=="="){
var _849=("|~^$*".indexOf(lc)>=0)?lc:"";
_843.type=_849+cc;
_843.attr=ts(_83b+1,x-_849.length);
_83d=x+1;
}
}
}else{
if(_83c>=0){
if(cc==")"){
if(_83e>=0){
_843.value=ts(_83c+1,x);
}
_83e=_83c=-1;
}
}else{
if(cc=="#"){
_847();
inId=x+1;
}else{
if(cc=="."){
_847();
_83f=x;
}else{
if(cc==":"){
_847();
_83e=x;
}else{
if(cc=="["){
_847();
_83b=x;
_843={};
}else{
if(cc=="("){
if(_83e>=0){
_843={name:ts(_83e+1,x),value:null};
_842.pseudos.push(_843);
}
_83c=x;
}else{
if((cc==" ")&&(lc!=cc)){
_848();
}
}
}
}
}
}
}
}
}
return _83a;
};
var _84a=function(_84b,_84c){
if(!_84b){
return _84c;
}
if(!_84c){
return _84b;
}
return function(){
return _84b.apply(window,arguments)&&_84c.apply(window,arguments);
};
};
var _84d=function(i,arr){
var r=arr||[];
if(i){
r.push(i);
}
return r;
};
var _84e=function(n){
return (1==n.nodeType);
};
var _84f="";
var _850=function(elem,attr){
if(!elem){
return _84f;
}
if(attr=="class"){
return elem.className||_84f;
}
if(attr=="for"){
return elem.htmlFor||_84f;
}
if(attr=="style"){
return elem.style.cssText||_84f;
}
return (_836?elem.getAttribute(attr):elem.getAttribute(attr,2))||_84f;
};
var _851={"*=":function(attr,_852){
return function(elem){
return (_850(elem,attr).indexOf(_852)>=0);
};
},"^=":function(attr,_853){
return function(elem){
return (_850(elem,attr).indexOf(_853)==0);
};
},"$=":function(attr,_854){
return function(elem){
var ea=" "+_850(elem,attr);
return (ea.lastIndexOf(_854)==(ea.length-_854.length));
};
},"~=":function(attr,_855){
var tval=" "+_855+" ";
return function(elem){
var ea=" "+_850(elem,attr)+" ";
return (ea.indexOf(tval)>=0);
};
},"|=":function(attr,_856){
var _857=_856+"-";
return function(elem){
var ea=_850(elem,attr);
return ((ea==_856)||(ea.indexOf(_857)==0));
};
},"=":function(attr,_858){
return function(elem){
return (_850(elem,attr)==_858);
};
}};
var _859=(typeof _833().firstChild.nextElementSibling=="undefined");
var _85a=!_859?"nextElementSibling":"nextSibling";
var _85b=!_859?"previousElementSibling":"previousSibling";
var _85c=(_859?_84e:_837);
var _85d=function(node){
while(node=node[_85b]){
if(_85c(node)){
return false;
}
}
return true;
};
var _85e=function(node){
while(node=node[_85a]){
if(_85c(node)){
return false;
}
}
return true;
};
var _85f=function(node){
var root=node.parentNode;
var i=0,tret=root.children||root.childNodes,ci=(node["_i"]||-1),cl=(root["_l"]||-1);
if(!tret){
return -1;
}
var l=tret.length;
if(cl==l&&ci>=0&&cl>=0){
return ci;
}
root["_l"]=l;
ci=-1;
for(var te=root["firstElementChild"]||root["firstChild"];te;te=te[_85a]){
if(_85c(te)){
te["_i"]=++i;
if(node===te){
ci=i;
}
}
}
return ci;
};
var _860=function(elem){
return !((_85f(elem))%2);
};
var _861=function(elem){
return ((_85f(elem))%2);
};
var _862={"checked":function(name,_863){
return function(elem){
return !!("checked" in elem?elem.checked:elem.selected);
};
},"first-child":function(){
return _85d;
},"last-child":function(){
return _85e;
},"only-child":function(name,_864){
return function(node){
return _85d(node)&&_85e(node);
};
},"empty":function(name,_865){
return function(elem){
var cn=elem.childNodes;
var cnl=elem.childNodes.length;
for(var x=cnl-1;x>=0;x--){
var nt=cn[x].nodeType;
if((nt===1)||(nt==3)){
return false;
}
}
return true;
};
},"contains":function(name,_866){
var cz=_866.charAt(0);
if(cz=="\""||cz=="'"){
_866=_866.slice(1,-1);
}
return function(elem){
return (elem.innerHTML.indexOf(_866)>=0);
};
},"not":function(name,_867){
var p=_838(_867)[0];
var _868={el:1};
if(p.tag!="*"){
_868.tag=1;
}
if(!p.classes.length){
_868.classes=1;
}
var ntf=_869(p,_868);
return function(elem){
return (!ntf(elem));
};
},"nth-child":function(name,_86a){
var pi=parseInt;
if(_86a=="odd"){
return _861;
}else{
if(_86a=="even"){
return _860;
}
}
if(_86a.indexOf("n")!=-1){
var _86b=_86a.split("n",2);
var pred=_86b[0]?((_86b[0]=="-")?-1:pi(_86b[0])):1;
var idx=_86b[1]?pi(_86b[1]):0;
var lb=0,ub=-1;
if(pred>0){
if(idx<0){
idx=(idx%pred)&&(pred+(idx%pred));
}else{
if(idx>0){
if(idx>=pred){
lb=idx-idx%pred;
}
idx=idx%pred;
}
}
}else{
if(pred<0){
pred*=-1;
if(idx>0){
ub=idx;
idx=idx%pred;
}
}
}
if(pred>0){
return function(elem){
var i=_85f(elem);
return (i>=lb)&&(ub<0||i<=ub)&&((i%pred)==idx);
};
}else{
_86a=idx;
}
}
var _86c=pi(_86a);
return function(elem){
return (_85f(elem)==_86c);
};
}};
var _86d=(dojo.isIE&&(dojo.isIE<9||dojo.isQuirks))?function(cond){
var clc=cond.toLowerCase();
if(clc=="class"){
cond="className";
}
return function(elem){
return (_836?elem.getAttribute(cond):elem[cond]||elem[clc]);
};
}:function(cond){
return function(elem){
return (elem&&elem.getAttribute&&elem.hasAttribute(cond));
};
};
var _869=function(_86e,_86f){
if(!_86e){
return _837;
}
_86f=_86f||{};
var ff=null;
if(!("el" in _86f)){
ff=_84a(ff,_84e);
}
if(!("tag" in _86f)){
if(_86e.tag!="*"){
ff=_84a(ff,function(elem){
return (elem&&(elem.tagName==_86e.getTag()));
});
}
}
if(!("classes" in _86f)){
each(_86e.classes,function(_870,idx,arr){
var re=new RegExp("(?:^|\\s)"+_870+"(?:\\s|$)");
ff=_84a(ff,function(elem){
return re.test(elem.className);
});
ff.count=idx;
});
}
if(!("pseudos" in _86f)){
each(_86e.pseudos,function(_871){
var pn=_871.name;
if(_862[pn]){
ff=_84a(ff,_862[pn](pn,_871.value));
}
});
}
if(!("attrs" in _86f)){
each(_86e.attrs,function(attr){
var _872;
var a=attr.attr;
if(attr.type&&_851[attr.type]){
_872=_851[attr.type](a,attr.matchFor);
}else{
if(a.length){
_872=_86d(a);
}
}
if(_872){
ff=_84a(ff,_872);
}
});
}
if(!("id" in _86f)){
if(_86e.id){
ff=_84a(ff,function(elem){
return (!!elem&&(elem.id==_86e.id));
});
}
}
if(!ff){
if(!("default" in _86f)){
ff=_837;
}
}
return ff;
};
var _873=function(_874){
return function(node,ret,bag){
while(node=node[_85a]){
if(_859&&(!_84e(node))){
continue;
}
if((!bag||_875(node,bag))&&_874(node)){
ret.push(node);
}
break;
}
return ret;
};
};
var _876=function(_877){
return function(root,ret,bag){
var te=root[_85a];
while(te){
if(_85c(te)){
if(bag&&!_875(te,bag)){
break;
}
if(_877(te)){
ret.push(te);
}
}
te=te[_85a];
}
return ret;
};
};
var _878=function(_879){
_879=_879||_837;
return function(root,ret,bag){
var te,x=0,tret=root.children||root.childNodes;
while(te=tret[x++]){
if(_85c(te)&&(!bag||_875(te,bag))&&(_879(te,x))){
ret.push(te);
}
}
return ret;
};
};
var _87a=function(node,root){
var pn=node.parentNode;
while(pn){
if(pn==root){
break;
}
pn=pn.parentNode;
}
return !!pn;
};
var _87b={};
var _87c=function(_87d){
var _87e=_87b[_87d.query];
if(_87e){
return _87e;
}
var io=_87d.infixOper;
var oper=(io?io.oper:"");
var _87f=_869(_87d,{el:1});
var qt=_87d.tag;
var _880=("*"==qt);
var ecs=_833()["getElementsByClassName"];
if(!oper){
if(_87d.id){
_87f=(!_87d.loops&&_880)?_837:_869(_87d,{el:1,id:1});
_87e=function(root,arr){
var te=dom.byId(_87d.id,(root.ownerDocument||root));
if(!te||!_87f(te)){
return;
}
if(9==root.nodeType){
return _84d(te,arr);
}else{
if(_87a(te,root)){
return _84d(te,arr);
}
}
};
}else{
if(ecs&&/\{\s*\[native code\]\s*\}/.test(String(ecs))&&_87d.classes.length&&!_834){
_87f=_869(_87d,{el:1,classes:1,id:1});
var _881=_87d.classes.join(" ");
_87e=function(root,arr,bag){
var ret=_84d(0,arr),te,x=0;
var tret=root.getElementsByClassName(_881);
while((te=tret[x++])){
if(_87f(te,root)&&_875(te,bag)){
ret.push(te);
}
}
return ret;
};
}else{
if(!_880&&!_87d.loops){
_87e=function(root,arr,bag){
var ret=_84d(0,arr),te,x=0;
var tret=root.getElementsByTagName(_87d.getTag());
while((te=tret[x++])){
if(_875(te,bag)){
ret.push(te);
}
}
return ret;
};
}else{
_87f=_869(_87d,{el:1,tag:1,id:1});
_87e=function(root,arr,bag){
var ret=_84d(0,arr),te,x=0;
var tret=root.getElementsByTagName(_87d.getTag());
while((te=tret[x++])){
if(_87f(te,root)&&_875(te,bag)){
ret.push(te);
}
}
return ret;
};
}
}
}
}else{
var _882={el:1};
if(_880){
_882.tag=1;
}
_87f=_869(_87d,_882);
if("+"==oper){
_87e=_873(_87f);
}else{
if("~"==oper){
_87e=_876(_87f);
}else{
if(">"==oper){
_87e=_878(_87f);
}
}
}
}
return _87b[_87d.query]=_87e;
};
var _883=function(root,_884){
var _885=_84d(root),qp,x,te,qpl=_884.length,bag,ret;
for(var i=0;i<qpl;i++){
ret=[];
qp=_884[i];
x=_885.length-1;
if(x>0){
bag={};
ret.nozip=true;
}
var gef=_87c(qp);
for(var j=0;(te=_885[j]);j++){
gef(te,ret,bag);
}
if(!ret.length){
break;
}
_885=ret;
}
return ret;
};
var _886={},_887={};
var _888=function(_889){
var _88a=_838(trim(_889));
if(_88a.length==1){
var tef=_87c(_88a[0]);
return function(root){
var r=tef(root,[]);
if(r){
r.nozip=true;
}
return r;
};
}
return function(root){
return _883(root,_88a);
};
};
var nua=navigator.userAgent;
var wk="WebKit/";
var _88b=(dojo.isWebKit&&(nua.indexOf(wk)>0)&&(parseFloat(nua.split(wk)[1])>528));
var _88c=dojo.isIE?"commentStrip":"nozip";
var qsa="querySelectorAll";
var _88d=(!!_833()[qsa]&&(!dojo.isSafari||(dojo.isSafari>3.1)||_88b));
var _88e=/n\+\d|([^ ])?([>~+])([^ =])?/g;
var _88f=function(_890,pre,ch,post){
return ch?(pre?pre+" ":"")+ch+(post?" "+post:""):_890;
};
var _891=function(_892,_893){
_892=_892.replace(_88e,_88f);
if(_88d){
var _894=_887[_892];
if(_894&&!_893){
return _894;
}
}
var _895=_886[_892];
if(_895){
return _895;
}
var qcz=_892.charAt(0);
var _896=(-1==_892.indexOf(" "));
if((_892.indexOf("#")>=0)&&(_896)){
_893=true;
}
var _897=(_88d&&(!_893)&&(_835.indexOf(qcz)==-1)&&(!dojo.isIE||(_892.indexOf(":")==-1))&&(!(_834&&(_892.indexOf(".")>=0)))&&(_892.indexOf(":contains")==-1)&&(_892.indexOf(":checked")==-1)&&(_892.indexOf("|=")==-1));
if(_897){
var tq=(_835.indexOf(_892.charAt(_892.length-1))>=0)?(_892+" *"):_892;
return _887[_892]=function(root){
try{
if(!((9==root.nodeType)||_896)){
throw "";
}
var r=root[qsa](tq);
r[_88c]=true;
return r;
}
catch(e){
return _891(_892,true)(root);
}
};
}else{
var _898=_892.split(/\s*,\s*/);
return _886[_892]=((_898.length<2)?_888(_892):function(root){
var _899=0,ret=[],tp;
while((tp=_898[_899++])){
ret=ret.concat(_888(tp)(root));
}
return ret;
});
}
};
var _89a=0;
var _89b=dojo.isIE?function(node){
if(_836){
return (node.getAttribute("_uid")||node.setAttribute("_uid",++_89a)||_89a);
}else{
return node.uniqueID;
}
}:function(node){
return (node._uid||(node._uid=++_89a));
};
var _875=function(node,bag){
if(!bag){
return 1;
}
var id=_89b(node);
if(!bag[id]){
return bag[id]=1;
}
return 0;
};
var _89c="_zipIdx";
var _89d=function(arr){
if(arr&&arr.nozip){
return arr;
}
var ret=[];
if(!arr||!arr.length){
return ret;
}
if(arr[0]){
ret.push(arr[0]);
}
if(arr.length<2){
return ret;
}
_89a++;
if(dojo.isIE&&_836){
var _89e=_89a+"";
arr[0].setAttribute(_89c,_89e);
for(var x=1,te;te=arr[x];x++){
if(arr[x].getAttribute(_89c)!=_89e){
ret.push(te);
}
te.setAttribute(_89c,_89e);
}
}else{
if(dojo.isIE&&arr.commentStrip){
try{
for(var x=1,te;te=arr[x];x++){
if(_84e(te)){
ret.push(te);
}
}
}
catch(e){
}
}else{
if(arr[0]){
arr[0][_89c]=_89a;
}
for(var x=1,te;te=arr[x];x++){
if(arr[x][_89c]!=_89a){
ret.push(te);
}
te[_89c]=_89a;
}
}
}
return ret;
};
var _89f=function(_8a0,root){
root=root||_833();
var od=root.ownerDocument||root.documentElement;
_836=(root.contentType&&root.contentType=="application/xml")||(dojo.isOpera&&(root.doctype||od.toString()=="[object XMLDocument]"))||(!!od)&&(dojo.isIE?od.xml:(root.xmlVersion||od.xmlVersion));
var r=_891(_8a0)(root);
if(r&&r.nozip){
return r;
}
return _89d(r);
};
_89f.filter=function(_8a1,_8a2,root){
var _8a3=[],_8a4=_838(_8a2),_8a5=(_8a4.length==1&&!/[^\w#\.]/.test(_8a2))?_869(_8a4[0]):function(node){
return dojo.query(_8a2,root).indexOf(node)!=-1;
};
for(var x=0,te;te=_8a1[x];x++){
if(_8a5(te)){
_8a3.push(te);
}
}
return _8a3;
};
return _89f;
});
},"dijit/_base/sniff":function(){
define("dijit/_base/sniff",["dojo/uacss"],function(){
});
},"dojox/mobile/IconContainer":function(){
define("dojox/mobile/IconContainer",["dojo/_base/array","dojo/_base/declare","dojo/_base/window","dojo/dom-construct","dojo/dom-style","dijit/registry","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./IconItem","./Heading","./View"],function(_8a6,_8a7,win,_8a8,_8a9,_8aa,_8ab,_8ac,_8ad,_8ae,_8af,View){
return _8a7("dojox.mobile.IconContainer",[_8ad,_8ac,_8ab],{defaultIcon:"",transition:"below",pressedIconOpacity:0.4,iconBase:"",iconPos:"",back:"Home",label:"My Application",single:false,buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("UL");
this.domNode.className="mblIconContainer";
var t=this._terminator=_8a8.create("LI");
t.className="mblIconItemTerminator";
t.innerHTML="&nbsp;";
this.domNode.appendChild(t);
},_setupSubNodes:function(ul){
_8a6.forEach(this.getChildren(),function(w){
ul.appendChild(w.subNode);
});
},startup:function(){
if(this._started){
return;
}
if(this.transition==="below"){
this._setupSubNodes(this.domNode);
}else{
var view=this.appView=new View({id:this.id+"_mblApplView"});
var _8b0=this;
view.onAfterTransitionIn=function(_8b1,dir,_8b2,_8b3,_8b4){
_8b0._opening._open_1();
};
view.domNode.style.visibility="hidden";
var _8b5=view._heading=new _8af({back:this._cv?this._cv(this.back):this.back,label:this._cv?this._cv(this.label):this.label,moveTo:this.domNode.parentNode.id,transition:this.transition});
view.addChild(_8b5);
var ul=view._ul=win.doc.createElement("UL");
ul.className="mblIconContainer";
ul.style.marginTop="0px";
this._setupSubNodes(ul);
view.domNode.appendChild(ul);
var _8b6;
for(var w=this.getParent();w;w=w.getParent()){
if(w instanceof View){
_8b6=w.domNode.parentNode;
break;
}
}
if(!_8b6){
_8b6=win.body();
}
_8b6.appendChild(view.domNode);
view.startup();
}
this.inherited(arguments);
},closeAll:function(){
var len=this.domNode.childNodes.length,_8b7,w;
for(var i=0;i<len;i++){
var _8b7=this.domNode.childNodes[i];
if(_8b7.nodeType!==1){
continue;
}
if(_8b7===this._terminator){
break;
}
var w=_8aa.byNode(_8b7);
w.containerNode.parentNode.style.display="none";
_8a9.set(w.iconNode,"opacity",1);
}
},addChild:function(_8b8,_8b9){
var _8ba=this.getChildren();
if(typeof _8b9!=="number"||_8b9>_8ba.length){
_8b9=_8ba.length;
}
var idx=_8b9;
var _8bb=this.containerNode;
if(idx>0){
_8bb=_8ba[idx-1].domNode;
idx="after";
}
_8a8.place(_8b8.domNode,_8bb,idx);
_8b8.transition=this.transition;
if(this.transition==="below"){
for(var i=0,_8bb=this._terminator;i<_8b9;i++){
_8bb=_8bb.nextSibling;
}
_8a8.place(_8b8.subNode,_8bb,"after");
}else{
_8a8.place(_8b8.subNode,this.appView._ul,_8b9);
}
_8b8.inheritParams();
_8b8._setIconAttr(_8b8.icon);
if(this._started&&!_8b8._started){
_8b8.startup();
}
},removeChild:function(_8bc){
if(typeof _8bc==="number"){
_8bc=this.getChildren()[_8bc];
}
if(_8bc){
this.inherited(arguments);
if(this.transition==="below"){
this.containerNode.removeChild(_8bc.subNode);
}else{
this.appView._ul.removeChild(_8bc.subNode);
}
}
}});
});
},"dijit/main":function(){
define("dijit/main",["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"dojox/mobile/RoundRect":function(){
define("dojox/mobile/RoundRect",["dojo/_base/array","dojo/_base/declare","dojo/_base/window","dijit/_Contained","dijit/_Container","dijit/_WidgetBase"],function(_8bd,_8be,win,_8bf,_8c0,_8c1){
return _8be("dojox.mobile.RoundRect",[_8c1,_8c0,_8bf],{shadow:false,buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("DIV");
this.domNode.className=this.shadow?"mblRoundRect mblShadow":"mblRoundRect";
},resize:function(){
_8bd.forEach(this.getChildren(),function(_8c2){
if(_8c2.resize){
_8c2.resize();
}
});
}});
});
},"dojox/mobile/TabBarButton":function(){
define("dojox/mobile/TabBarButton",["dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/dom-class","dojo/dom-construct","dijit/registry","./common","./_ItemBase"],function(_8c3,lang,win,_8c4,_8c5,_8c6,_8c7,_8c8){
return _8c3("dojox.mobile.TabBarButton",_8c8,{icon1:"",icon2:"",iconPos1:"",iconPos2:"",selected:false,transition:"none",tag:"LI",selectOne:true,inheritParams:function(){
if(this.icon&&!this.icon1){
this.icon1=this.icon;
}
var _8c9=this.getParent();
if(_8c9){
if(!this.transition){
this.transition=_8c9.transition;
}
if(this.icon1&&_8c9.iconBase&&_8c9.iconBase.charAt(_8c9.iconBase.length-1)==="/"){
this.icon1=_8c9.iconBase+this.icon1;
}
if(!this.icon1){
this.icon1=_8c9.iconBase;
}
if(!this.iconPos1){
this.iconPos1=_8c9.iconPos;
}
if(this.icon2&&_8c9.iconBase&&_8c9.iconBase.charAt(_8c9.iconBase.length-1)==="/"){
this.icon2=_8c9.iconBase+this.icon2;
}
if(!this.icon2){
this.icon2=_8c9.iconBase||this.icon1;
}
if(!this.iconPos2){
this.iconPos2=_8c9.iconPos||this.iconPos1;
}
}
},buildRendering:function(){
var a=this.anchorNode=_8c5.create("A",{className:"mblTabBarButtonAnchor"});
this.connect(a,"onclick","onClick");
this.box=_8c5.create("DIV",{className:"mblTabBarButtonTextBox"},a);
var box=this.box;
var _8ca="";
var r=this.srcNodeRef;
if(r){
for(var i=0,len=r.childNodes.length;i<len;i++){
var n=r.firstChild;
if(n.nodeType===3){
_8ca+=lang.trim(n.nodeValue);
}
box.appendChild(n);
}
}
if(!this.label){
this.label=_8ca;
}
this.domNode=this.srcNodeRef||_8c5.create(this.tag);
this.containerNode=this.domNode;
this.domNode.appendChild(a);
if(this.domNode.className.indexOf("mblDomButton")!=-1){
var _8cb=_8c5.create("DIV",null,a);
_8c7.createDomButton(this.domNode,null,_8cb);
_8c4.add(this.domNode,"mblTabButtonDomButton");
_8c4.add(_8cb,"mblTabButtonDomButtonClass");
}
if((this.icon1||this.icon).indexOf("mblDomButton")!=-1){
_8c4.add(this.domNode,"mblTabButtonDomButton");
}
},startup:function(){
if(this._started){
return;
}
this.inheritParams();
var _8cc=this.getParent();
var _8cd=_8cc?_8cc._clsName:"mblTabBarButton";
_8c4.add(this.domNode,_8cd+(this.selected?" mblTabButtonSelected":""));
if(_8cc&&_8cc.barType=="segmentedControl"){
_8c4.remove(this.domNode,"mblTabBarButton");
_8c4.add(this.domNode,_8cc._clsName);
this.box.className="";
}
this.set({icon1:this.icon1,icon2:this.icon2});
this.inherited(arguments);
},select:function(){
if(arguments[0]){
this.selected=false;
_8c4.remove(this.domNode,"mblTabButtonSelected");
}else{
this.selected=true;
_8c4.add(this.domNode,"mblTabButtonSelected");
for(var i=0,c=this.domNode.parentNode.childNodes;i<c.length;i++){
if(c[i].nodeType!=1){
continue;
}
var w=_8c6.byNode(c[i]);
if(w&&w!=this){
w.deselect();
}
}
}
if(this.iconNode1){
this.iconNode1.style.visibility=this.selected?"hidden":"";
}
if(this.iconNode2){
this.iconNode2.style.visibility=this.selected?"":"hidden";
}
},deselect:function(){
this.select(true);
},onClick:function(e){
this.defaultClickAction();
},_setIcon:function(icon,pos,num,sel){
var i="icon"+num,n="iconNode"+num,p="iconPos"+num;
if(icon){
this[i]=icon;
}
if(pos){
if(this[p]===pos){
return;
}
this[p]=pos;
}
if(icon&&icon!=="none"){
if(!this.iconDivNode){
this.iconDivNode=_8c5.create("DIV",{className:"mblTabBarButtonDiv"},this.anchorNode,"first");
}
if(!this[n]){
this[n]=_8c5.create("div",{className:"mblTabBarButtonIcon"},this.iconDivNode);
}else{
_8c5.empty(this[n]);
}
_8c7.createIcon(icon,this[p],null,this.alt,this[n]);
if(this[p]){
_8c4.add(this[n].firstChild,"mblTabBarButtonSpriteIcon");
}
_8c4.remove(this.iconDivNode,"mblTabBarButtonNoIcon");
this[n].style.visibility=sel?"hidden":"";
}else{
if(this.iconDivNode){
_8c4.add(this.iconDivNode,"mblTabBarButtonNoIcon");
}
}
},_setIcon1Attr:function(icon){
this._setIcon(icon,null,1,this.selected);
},_setIcon2Attr:function(icon){
this._setIcon(icon,null,2,!this.selected);
},_setIconPos1Attr:function(pos){
this._setIcon(null,pos,1,this.selected);
},_setIconPos2Attr:function(pos){
this._setIcon(null,pos,2,!this.selected);
},_setLabelAttr:function(text){
this.label=text;
this.box.innerHTML=this._cv?this._cv(text):text;
}});
});
},"dojox/mobile/CheckBox":function(){
define("dojox/mobile/CheckBox",["dojo/_base/declare","dojo/dom-construct","dijit/form/_CheckBoxMixin","./ToggleButton"],function(_8ce,_8cf,_8d0,_8d1){
return _8ce("dojox.mobile.CheckBox",[_8d1,_8d0],{baseClass:"mblCheckBox",_setTypeAttr:function(){
},buildRendering:function(){
if(!this.srcNodeRef){
this.srcNodeRef=_8cf.create("input",{type:this.type});
}
this.inherited(arguments);
this.focusNode=this.domNode;
},_getValueAttr:function(){
return (this.checked?this.value:false);
}});
});
},"dijit/form/_ButtonMixin":function(){
define("dijit/form/_ButtonMixin",["dojo/_base/declare","dojo/dom","dojo/_base/event","../registry"],function(_8d2,dom,_8d3,_8d4){
return _8d2("dijit.form._ButtonMixin",null,{label:"",type:"button",_onClick:function(e){
if(this.disabled){
_8d3.stop(e);
return false;
}
var _8d5=this.onClick(e)===false;
if(!_8d5&&this.type=="submit"&&!(this.valueNode||this.focusNode).form){
for(var node=this.domNode;node.parentNode;node=node.parentNode){
var _8d6=_8d4.byNode(node);
if(_8d6&&typeof _8d6._onSubmit=="function"){
_8d6._onSubmit(e);
_8d5=true;
break;
}
}
}
if(_8d5){
e.preventDefault();
}
return !_8d5;
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.focusNode,false);
},onClick:function(){
return true;
},_setLabelAttr:function(_8d7){
this._set("label",_8d7);
(this.containerNode||this.focusNode).innerHTML=_8d7;
}});
});
},"dijit/_base/typematic":function(){
define("dijit/_base/typematic",["../typematic"],function(){
});
},"dojox/mobile/RoundRectCategory":function(){
define("dojox/mobile/RoundRectCategory",["dojo/_base/declare","dojo/_base/window","dijit/_Contained","dijit/_WidgetBase"],function(_8d8,win,_8d9,_8da){
return _8d8("dojox.mobile.RoundRectCategory",[_8da,_8d9],{label:"",buildRendering:function(){
this.domNode=this.containerNode=this.srcNodeRef||win.doc.createElement("H2");
this.domNode.className="mblRoundRectCategory";
if(!this.label){
this.label=this.domNode.innerHTML;
}
},_setLabelAttr:function(_8db){
this.label=_8db;
this.domNode.innerHTML=this._cv?this._cv(_8db):_8db;
}});
});
},"dojox/mobile/app/TextBox":function(){
define(["dijit","dojo","dojox","dojo/require!dojox/mobile/TextBox"],function(_8dc,dojo,_8dd){
dojo.provide("dojox.mobile.app.TextBox");
dojo.deprecated("dojox.mobile.app.TextBox is deprecated","dojox.mobile.app.TextBox moved to dojox.mobile.TextBox",1.8);
dojo.require("dojox.mobile.TextBox");
_8dd.mobile.app.TextBox=_8dd.mobile.TextBox;
});
},"dojox/mobile/app/SceneAssistant":function(){
define(["dijit","dojo","dojox"],function(_8de,dojo,_8df){
dojo.provide("dojox.mobile.app.SceneAssistant");
dojo.experimental("dojox.mobile.app.SceneAssistant");
dojo.declare("dojox.mobile.app.SceneAssistant",null,{constructor:function(){
},setup:function(){
},activate:function(_8e0){
},deactivate:function(){
},destroy:function(){
var _8e1=dojo.query("> [widgetId]",this.containerNode).map(_8de.byNode);
dojo.forEach(_8e1,function(_8e2){
_8e2.destroyRecursive();
});
this.disconnect();
},connect:function(obj,_8e3,_8e4){
if(!this._connects){
this._connects=[];
}
this._connects.push(dojo.connect(obj,_8e3,_8e4));
},disconnect:function(){
dojo.forEach(this._connects,dojo.disconnect);
this._connects=[];
}});
});
},"dojo/io-query":function(){
define(["./_base/lang"],function(lang){
var _8e5={};
function _8e6(map){
var enc=encodeURIComponent,_8e7=[];
for(var name in map){
var _8e8=map[name];
if(_8e8!=_8e5[name]){
var _8e9=enc(name)+"=";
if(lang.isArray(_8e8)){
for(var i=0,l=_8e8.length;i<l;++i){
_8e7.push(_8e9+enc(_8e8[i]));
}
}else{
_8e7.push(_8e9+enc(_8e8));
}
}
}
return _8e7.join("&");
};
function _8ea(str){
var dec=decodeURIComponent,qp=str.split("&"),ret={},name,val;
for(var i=0,l=qp.length,item;i<l;++i){
item=qp[i];
if(item.length){
var s=item.indexOf("=");
if(s<0){
name=dec(item);
val="";
}else{
name=dec(item.slice(0,s));
val=dec(item.slice(s+1));
}
if(typeof ret[name]=="string"){
ret[name]=[ret[name]];
}
if(lang.isArray(ret[name])){
ret[name].push(val);
}else{
ret[name]=val;
}
}
}
return ret;
};
return {objectToQuery:_8e6,queryToObject:_8ea};
});
},"dijit/_base/popup":function(){
define("dijit/_base/popup",["dojo/dom-class","../popup","../BackgroundIframe"],function(_8eb,_8ec){
var _8ed=_8ec._createWrapper;
_8ec._createWrapper=function(_8ee){
if(!_8ee.declaredClass){
_8ee={_popupWrapper:(_8ee.parentNode&&_8eb.contains(_8ee.parentNode,"dijitPopup"))?_8ee.parentNode:null,domNode:_8ee,destroy:function(){
}};
}
return _8ed.call(this,_8ee);
};
var _8ef=_8ec.open;
_8ec.open=function(args){
if(args.orient&&typeof args.orient!="string"&&!("length" in args.orient)){
var ary=[];
for(var key in args.orient){
ary.push({aroundCorner:key,corner:args.orient[key]});
}
args.orient=ary;
}
return _8ef.call(this,args);
};
return _8ec;
});
},"dojo/ready":function(){
define(["./_base/kernel","./has","require","./domReady","./_base/lang"],function(dojo,has,_8f0,_8f1,lang){
var _8f2=0,_8f3,_8f4=[],_8f5=0,_8f6=function(){
_8f2=1;
dojo._postLoad=dojo.config.afterOnLoad=true;
if(_8f4.length){
_8f3(_8f7);
}
},_8f7=function(){
if(_8f2&&!_8f5&&_8f4.length){
_8f5=1;
var f=_8f4.shift();
try{
f();
}
finally{
_8f5=0;
}
_8f5=0;
if(_8f4.length){
_8f3(_8f7);
}
}
};
if(1){
_8f0.on("idle",_8f7);
_8f3=function(){
if(_8f0.idle()){
_8f7();
}
};
}else{
_8f3=function(){
_8f0.ready(_8f7);
};
}
var _8f8=dojo.ready=dojo.addOnLoad=function(_8f9,_8fa,_8fb){
var _8fc=lang._toArray(arguments);
if(typeof _8f9!="number"){
_8fb=_8fa;
_8fa=_8f9;
_8f9=1000;
}else{
_8fc.shift();
}
_8fb=_8fb?lang.hitch.apply(dojo,_8fc):function(){
_8fa();
};
_8fb.priority=_8f9;
for(var i=0;i<_8f4.length&&_8f9>=_8f4[i].priority;i++){
}
_8f4.splice(i,0,_8fb);
_8f3();
};
true||has.add("dojo-config-addOnLoad",1);
if(1){
var dca=dojo.config.addOnLoad;
if(dca){
_8f8[(lang.isArray(dca)?"apply":"call")](dojo,dca);
}
}
if(1&&dojo.config.parseOnLoad&&!dojo.isAsync){
_8f8(99,function(){
if(!dojo.parser){
dojo.deprecated("Add explicit require(['dojo/parser']);","","2.0");
_8f0(["dojo/parser"]);
}
});
}
if(1){
_8f1(_8f6);
}else{
_8f6();
}
return _8f8;
});
},"dojox/mobile/transition":function(){
define("dojox/mobile/transition",["dojo/_base/Deferred","dojo/_base/config"],function(_8fd,_8fe){
if(_8fe["mblCSS3Transition"]){
var _8ff=new _8fd();
require([_8fe["mblCSS3Transition"]],function(_900){
_8ff.resolve(_900);
});
return _8ff;
}
return null;
});
},"dojox/mobile/ToggleButton":function(){
define("dojox/mobile/ToggleButton",["dojo/_base/declare","dojo/dom-class","dijit/form/_ToggleButtonMixin","./Button"],function(_901,_902,_903,_904){
return _901("dojox.mobile.ToggleButton",[_904,_903],{baseClass:"mblToggleButton",_setCheckedAttr:function(){
this.inherited(arguments);
var _905=(this.baseClass+" "+this["class"]).replace(/(\S+)\s*/g,"$1Checked ").split(" ");
_902[this.checked?"add":"remove"](this.focusNode||this.domNode,_905);
}});
});
},"dijit/_base/wai":function(){
define("dijit/_base/wai",["dojo/dom-attr","dojo/_base/lang","..","../hccss"],function(_906,lang,_907){
lang.mixin(_907,{hasWaiRole:function(elem,role){
var _908=this.getWaiRole(elem);
return role?(_908.indexOf(role)>-1):(_908.length>0);
},getWaiRole:function(elem){
return lang.trim((_906.get(elem,"role")||"").replace("wairole:",""));
},setWaiRole:function(elem,role){
_906.set(elem,"role",role);
},removeWaiRole:function(elem,role){
var _909=_906.get(elem,"role");
if(!_909){
return;
}
if(role){
var t=lang.trim((" "+_909+" ").replace(" "+role+" "," "));
_906.set(elem,"role",t);
}else{
elem.removeAttribute("role");
}
},hasWaiState:function(elem,_90a){
return elem.hasAttribute?elem.hasAttribute("aria-"+_90a):!!elem.getAttribute("aria-"+_90a);
},getWaiState:function(elem,_90b){
return elem.getAttribute("aria-"+_90b)||"";
},setWaiState:function(elem,_90c,_90d){
elem.setAttribute("aria-"+_90c,_90d);
},removeWaiState:function(elem,_90e){
elem.removeAttribute("aria-"+_90e);
}});
return _907;
});
},"dojo/Evented":function(){
define(["./aspect","./on"],function(_90f,on){
"use strict";
var _910=_90f.after;
function _911(){
};
_911.prototype={on:function(type,_912){
return on.parse(this,type,_912,function(_913,type){
return _910(_913,"on"+type,_912,true);
});
},emit:function(type,_914){
var args=[this];
args.push.apply(args,arguments);
return on.emit.apply(on,args);
}};
return _911;
});
},"dojo/window":function(){
define(["./_base/kernel","./_base/lang","./_base/sniff","./_base/window","./dom","./dom-geometry","./dom-style"],function(dojo,lang,has,_915,dom,geom,_916){
lang.getObject("window",true,dojo);
dojo.window.getBox=function(){
var _917=(_915.doc.compatMode=="BackCompat")?_915.body():_915.doc.documentElement;
var _918=geom.docScroll();
var _919=_915.doc.parentWindow||_915.doc.defaultView;
return {l:_918.x,t:_918.y,w:_919.innerWidth||_917.clientWidth,h:_919.innerHeight||_917.clientHeight};
};
dojo.window.get=function(doc){
if(has("ie")&&window!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
};
dojo.window.scrollIntoView=function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_915.doc,body=doc.body||_915.body(),html=doc.documentElement||body.parentNode,isIE=has("ie"),isWK=has("webkit");
if((!(has("mozilla")||isIE||isWK||has("opera"))||node==body||node==html)&&(typeof node.scrollIntoView!="undefined")){
node.scrollIntoView(false);
return;
}
var _91a=doc.compatMode=="BackCompat",_91b=(isIE>=9&&node.ownerDocument.parentWindow.frameElement)?((html.clientHeight>0&&html.clientWidth>0&&(body.clientHeight==0||body.clientWidth==0||body.clientHeight>html.clientHeight||body.clientWidth>html.clientWidth))?html:body):(_91a?body:html),_91c=isWK?body:_91b,_91d=_91b.clientWidth,_91e=_91b.clientHeight,rtl=!geom.isBodyLtr(),_91f=pos||geom.position(node),el=node.parentNode,_920=function(el){
return ((isIE<=6||(isIE&&_91a))?false:(_916.get(el,"position").toLowerCase()=="fixed"));
};
if(_920(node)){
return;
}
while(el){
if(el==body){
el=_91c;
}
var _921=geom.position(el),_922=_920(el);
if(el==_91c){
_921.w=_91d;
_921.h=_91e;
if(_91c==html&&isIE&&rtl){
_921.x+=_91c.offsetWidth-_921.w;
}
if(_921.x<0||!isIE){
_921.x=0;
}
if(_921.y<0||!isIE){
_921.y=0;
}
}else{
var pb=geom.getPadBorderExtents(el);
_921.w-=pb.w;
_921.h-=pb.h;
_921.x+=pb.l;
_921.y+=pb.t;
var _923=el.clientWidth,_924=_921.w-_923;
if(_923>0&&_924>0){
_921.w=_923;
_921.x+=(rtl&&(isIE||el.clientLeft>pb.l))?_924:0;
}
_923=el.clientHeight;
_924=_921.h-_923;
if(_923>0&&_924>0){
_921.h=_923;
}
}
if(_922){
if(_921.y<0){
_921.h+=_921.y;
_921.y=0;
}
if(_921.x<0){
_921.w+=_921.x;
_921.x=0;
}
if(_921.y+_921.h>_91e){
_921.h=_91e-_921.y;
}
if(_921.x+_921.w>_91d){
_921.w=_91d-_921.x;
}
}
var l=_91f.x-_921.x,t=_91f.y-Math.max(_921.y,0),r=l+_91f.w-_921.w,bot=t+_91f.h-_921.h;
if(r*l>0){
var s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_91a)||isIE>=9)){
s=-s;
}
_91f.x+=el.scrollLeft;
el.scrollLeft+=s;
_91f.x-=el.scrollLeft;
}
if(bot*t>0){
_91f.y+=el.scrollTop;
el.scrollTop+=Math[t<0?"max":"min"](t,bot);
_91f.y-=el.scrollTop;
}
el=(el!=_91c)&&!_922&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
};
return dojo.window;
});
},"dojo/_base/xhr":function(){
define(["./kernel","./sniff","require","../io-query","../dom","../dom-form","./Deferred","./json","./lang","./array","../on"],function(dojo,has,_925,ioq,dom,_926,_927,json,lang,_928,on){
has.add("native-xhr",function(){
return typeof XMLHttpRequest!=="undefined";
});
if(1){
dojo._xhrObj=_925.getXhr;
}else{
if(has("native-xhr")){
dojo._xhrObj=function(){
try{
return new XMLHttpRequest();
}
catch(e){
throw new Error("XMLHTTP not available: "+e);
}
};
}else{
for(var _929=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"],_92a,i=0;i<3;){
try{
_92a=_929[i++];
if(new ActiveXObject(_92a)){
break;
}
}
catch(e){
}
}
dojo._xhrObj=function(){
return new ActiveXObject(_92a);
};
}
}
var cfg=dojo.config;
dojo.objectToQuery=ioq.objectToQuery;
dojo.queryToObject=ioq.queryToObject;
dojo.fieldToObject=_926.fieldToObject;
dojo.formToObject=_926.toObject;
dojo.formToQuery=_926.toQuery;
dojo.formToJson=_926.toJson;
dojo._blockAsync=false;
var _92b=dojo._contentHandlers=dojo.contentHandlers={"text":function(xhr){
return xhr.responseText;
},"json":function(xhr){
return json.fromJson(xhr.responseText||null);
},"json-comment-filtered":function(xhr){
if(!dojo.config.useCommentedJson){
console.warn("Consider using the standard mimetype:application/json."+" json-commenting can introduce security issues. To"+" decrease the chances of hijacking, use the standard the 'json' handler and"+" prefix your json with: {}&&\n"+"Use djConfig.useCommentedJson=true to turn off this message.");
}
var _92c=xhr.responseText;
var _92d=_92c.indexOf("/*");
var _92e=_92c.lastIndexOf("*/");
if(_92d==-1||_92e==-1){
throw new Error("JSON was not comment filtered");
}
return json.fromJson(_92c.substring(_92d+2,_92e));
},"javascript":function(xhr){
return dojo.eval(xhr.responseText);
},"xml":function(xhr){
var _92f=xhr.responseXML;
if(has("ie")){
if((!_92f||!_92f.documentElement)){
var ms=function(n){
return "MSXML"+n+".DOMDocument";
};
var dp=["Microsoft.XMLDOM",ms(6),ms(4),ms(3),ms(2)];
_928.some(dp,function(p){
try{
var dom=new ActiveXObject(p);
dom.async=false;
dom.loadXML(xhr.responseText);
_92f=dom;
}
catch(e){
return false;
}
return true;
});
}
}
return _92f;
},"json-comment-optional":function(xhr){
if(xhr.responseText&&/^[^{\[]*\/\*/.test(xhr.responseText)){
return _92b["json-comment-filtered"](xhr);
}else{
return _92b["json"](xhr);
}
}};
dojo._ioSetArgs=function(args,_930,_931,_932){
var _933={args:args,url:args.url};
var _934=null;
if(args.form){
var form=dom.byId(args.form);
var _935=form.getAttributeNode("action");
_933.url=_933.url||(_935?_935.value:null);
_934=_926.toObject(form);
}
var _936=[{}];
if(_934){
_936.push(_934);
}
if(args.content){
_936.push(args.content);
}
if(args.preventCache){
_936.push({"dojo.preventCache":new Date().valueOf()});
}
_933.query=ioq.objectToQuery(lang.mixin.apply(null,_936));
_933.handleAs=args.handleAs||"text";
var d=new _927(_930);
d.addCallbacks(_931,function(_937){
return _932(_937,d);
});
var ld=args.load;
if(ld&&lang.isFunction(ld)){
d.addCallback(function(_938){
return ld.call(args,_938,_933);
});
}
var err=args.error;
if(err&&lang.isFunction(err)){
d.addErrback(function(_939){
return err.call(args,_939,_933);
});
}
var _93a=args.handle;
if(_93a&&lang.isFunction(_93a)){
d.addBoth(function(_93b){
return _93a.call(args,_93b,_933);
});
}
if(cfg.ioPublish&&dojo.publish&&_933.args.ioPublish!==false){
d.addCallbacks(function(res){
dojo.publish("/dojo/io/load",[d,res]);
return res;
},function(res){
dojo.publish("/dojo/io/error",[d,res]);
return res;
});
d.addBoth(function(res){
dojo.publish("/dojo/io/done",[d,res]);
return res;
});
}
d.ioArgs=_933;
return d;
};
var _93c=function(dfd){
dfd.canceled=true;
var xhr=dfd.ioArgs.xhr;
var _93d=typeof xhr.abort;
if(_93d=="function"||_93d=="object"||_93d=="unknown"){
xhr.abort();
}
var err=dfd.ioArgs.error;
if(!err){
err=new Error("xhr cancelled");
err.dojoType="cancel";
}
return err;
};
var _93e=function(dfd){
var ret=_92b[dfd.ioArgs.handleAs](dfd.ioArgs.xhr);
return ret===undefined?null:ret;
};
var _93f=function(_940,dfd){
if(!dfd.ioArgs.args.failOk){
console.error(_940);
}
return _940;
};
var _941=null;
var _942=[];
var _943=0;
var _944=function(dfd){
if(_943<=0){
_943=0;
if(cfg.ioPublish&&dojo.publish&&(!dfd||dfd&&dfd.ioArgs.args.ioPublish!==false)){
dojo.publish("/dojo/io/stop");
}
}
};
var _945=function(){
var now=(new Date()).getTime();
if(!dojo._blockAsync){
for(var i=0,tif;i<_942.length&&(tif=_942[i]);i++){
var dfd=tif.dfd;
var func=function(){
if(!dfd||dfd.canceled||!tif.validCheck(dfd)){
_942.splice(i--,1);
_943-=1;
}else{
if(tif.ioCheck(dfd)){
_942.splice(i--,1);
tif.resHandle(dfd);
_943-=1;
}else{
if(dfd.startTime){
if(dfd.startTime+(dfd.ioArgs.args.timeout||0)<now){
_942.splice(i--,1);
var err=new Error("timeout exceeded");
err.dojoType="timeout";
dfd.errback(err);
dfd.cancel();
_943-=1;
}
}
}
}
};
if(dojo.config.debugAtAllCosts){
func.call(this);
}else{
func.call(this);
}
}
}
_944(dfd);
if(!_942.length){
clearInterval(_941);
_941=null;
}
};
dojo._ioCancelAll=function(){
try{
_928.forEach(_942,function(i){
try{
i.dfd.cancel();
}
catch(e){
}
});
}
catch(e){
}
};
if(has("ie")){
on(window,"unload",dojo._ioCancelAll);
}
dojo._ioNotifyStart=function(dfd){
if(cfg.ioPublish&&dojo.publish&&dfd.ioArgs.args.ioPublish!==false){
if(!_943){
dojo.publish("/dojo/io/start");
}
_943+=1;
dojo.publish("/dojo/io/send",[dfd]);
}
};
dojo._ioWatch=function(dfd,_946,_947,_948){
var args=dfd.ioArgs.args;
if(args.timeout){
dfd.startTime=(new Date()).getTime();
}
_942.push({dfd:dfd,validCheck:_946,ioCheck:_947,resHandle:_948});
if(!_941){
_941=setInterval(_945,50);
}
if(args.sync){
_945();
}
};
var _949="application/x-www-form-urlencoded";
var _94a=function(dfd){
return dfd.ioArgs.xhr.readyState;
};
var _94b=function(dfd){
return 4==dfd.ioArgs.xhr.readyState;
};
var _94c=function(dfd){
var xhr=dfd.ioArgs.xhr;
if(dojo._isDocumentOk(xhr)){
dfd.callback(dfd);
}else{
var err=new Error("Unable to load "+dfd.ioArgs.url+" status:"+xhr.status);
err.status=xhr.status;
err.responseText=xhr.responseText;
err.xhr=xhr;
dfd.errback(err);
}
};
dojo._ioAddQueryToUrl=function(_94d){
if(_94d.query.length){
_94d.url+=(_94d.url.indexOf("?")==-1?"?":"&")+_94d.query;
_94d.query=null;
}
};
dojo.xhr=function(_94e,args,_94f){
var dfd=dojo._ioSetArgs(args,_93c,_93e,_93f);
var _950=dfd.ioArgs;
var xhr=_950.xhr=dojo._xhrObj(_950.args);
if(!xhr){
dfd.cancel();
return dfd;
}
if("postData" in args){
_950.query=args.postData;
}else{
if("putData" in args){
_950.query=args.putData;
}else{
if("rawBody" in args){
_950.query=args.rawBody;
}else{
if((arguments.length>2&&!_94f)||"POST|PUT".indexOf(_94e.toUpperCase())==-1){
dojo._ioAddQueryToUrl(_950);
}
}
}
}
xhr.open(_94e,_950.url,args.sync!==true,args.user||undefined,args.password||undefined);
if(args.headers){
for(var hdr in args.headers){
if(hdr.toLowerCase()==="content-type"&&!args.contentType){
args.contentType=args.headers[hdr];
}else{
if(args.headers[hdr]){
xhr.setRequestHeader(hdr,args.headers[hdr]);
}
}
}
}
if(args.contentType!==false){
xhr.setRequestHeader("Content-Type",args.contentType||_949);
}
if(!args.headers||!("X-Requested-With" in args.headers)){
xhr.setRequestHeader("X-Requested-With","XMLHttpRequest");
}
dojo._ioNotifyStart(dfd);
if(dojo.config.debugAtAllCosts){
xhr.send(_950.query);
}else{
try{
xhr.send(_950.query);
}
catch(e){
_950.error=e;
dfd.cancel();
}
}
dojo._ioWatch(dfd,_94a,_94b,_94c);
xhr=null;
return dfd;
};
dojo.xhrGet=function(args){
return dojo.xhr("GET",args);
};
dojo.rawXhrPost=dojo.xhrPost=function(args){
return dojo.xhr("POST",args,true);
};
dojo.rawXhrPut=dojo.xhrPut=function(args){
return dojo.xhr("PUT",args,true);
};
dojo.xhrDelete=function(args){
return dojo.xhr("DELETE",args);
};
dojo._isDocumentOk=function(http){
var stat=http.status||0;
stat=(stat>=200&&stat<300)||stat==304||stat==1223||!stat;
return stat;
};
dojo._getText=function(url){
var _951;
dojo.xhrGet({url:url,sync:true,load:function(text){
_951=text;
}});
return _951;
};
lang.mixin(dojo.xhr,{_xhrObj:dojo._xhrObj,fieldToObject:_926.fieldToObject,formToObject:_926.toObject,objectToQuery:ioq.objectToQuery,formToQuery:_926.toQuery,formToJson:_926.toJson,queryToObject:ioq.queryToObject,contentHandlers:_92b,_ioSetArgs:dojo._ioSetArgs,_ioCancelAll:dojo._ioCancelAll,_ioNotifyStart:dojo._ioNotifyStart,_ioWatch:dojo._ioWatch,_ioAddQueryToUrl:dojo._ioAddQueryToUrl,_isDocumentOk:dojo._isDocumentOk,_getText:dojo._getText,get:dojo.xhrGet,post:dojo.xhrPost,put:dojo.xhrPut,del:dojo.xhrDelete});
return dojo.xhr;
});
},"dojo/has":function(){
define(["require"],function(_952){
var has=_952.has||function(){
};
if(!1){
var _953=typeof window!="undefined"&&typeof location!="undefined"&&typeof document!="undefined"&&window.location==location&&window.document==document,_954=this,doc=_953&&document,_955=doc&&doc.createElement("DiV"),_956={};
has=function(name){
return _956[name]=typeof _956[name]=="function"?_956[name](_954,doc,_955):_956[name];
};
has.cache=_956;
has.add=function(name,test,now,_957){
(typeof _956[name]=="undefined"||_957)&&(_956[name]=test);
return now&&has(name);
};
true||has.add("host-browser",_953);
true||has.add("dom",_953);
true||has.add("dojo-dom-ready-api",1);
true||has.add("dojo-sniff",1);
}
if(1){
var _958=navigator.userAgent;
has.add("dom-addeventlistener",!!document.addEventListener);
has.add("touch","ontouchstart" in document);
has.add("device-width",screen.availWidth||innerWidth);
has.add("agent-ios",!!_958.match(/iPhone|iP[ao]d/));
has.add("agent-android",_958.indexOf("android")>1);
}
has.clearElement=function(_959){
_959.innerHTML="";
return _959;
};
has.normalize=function(id,_95a){
var _95b=id.match(/[\?:]|[^:\?]*/g),i=0,get=function(skip){
var term=_95b[i++];
if(term==":"){
return 0;
}else{
if(_95b[i++]=="?"){
if(!skip&&has(term)){
return get();
}else{
get(true);
return get(skip);
}
}
return term||0;
}
};
id=get();
return id&&_95a(id);
};
has.load=function(id,_95c,_95d){
if(id){
_95c([id],_95d);
}else{
_95d();
}
};
return has;
});
},"dojox/mobile/Tooltip":function(){
define("dojox/mobile/Tooltip",["dojo/_base/array","dijit/registry","dojo/_base/declare","dojo/_base/lang","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dijit/place","dijit/_WidgetBase"],function(_95e,_95f,_960,lang,_961,_962,_963,_964,_965,_966){
return _960("dojox.mobile.Tooltip",_966,{baseClass:"mblTooltip mblTooltipHidden",buildRendering:function(){
this.inherited(arguments);
this.anchor=_962.create("div",{"class":"mblTooltipAnchor"},this.domNode,"first");
this.arrow=_962.create("div",{"class":"mblTooltipArrow"},this.anchor);
this.innerArrow=_962.create("div",{"class":"mblTooltipInnerArrow"},this.anchor);
},show:function(_967,_968){
var _969=this.domNode;
var _96a={"MRM":"mblTooltipAfter","MLM":"mblTooltipBefore","BMT":"mblTooltipBelow","TMB":"mblTooltipAbove","BLT":"mblTooltipBelow","TLB":"mblTooltipAbove","BRT":"mblTooltipBelow","TRB":"mblTooltipAbove","TLT":"mblTooltipBefore","TRT":"mblTooltipAfter","BRB":"mblTooltipAfter","BLB":"mblTooltipBefore"};
_961.remove(_969,["mblTooltipAfter","mblTooltipBefore","mblTooltipBelow","mblTooltipAbove"]);
_95e.forEach(_95f.findWidgets(_969),function(_96b){
if(_96b.height=="auto"&&typeof _96b.resize=="function"){
if(!_96b.fixedFooterHeight){
_96b.fixedFooterHeight=_963.getPadBorderExtents(_969).b;
}
_96b.resize();
}
});
var best=_965.around(_969,_967,_968||["below-centered","above-centered","after","before"],this.isLeftToRight());
var _96c=_96a[best.corner+best.aroundCorner.charAt(0)]||"";
_961.add(_969,_96c);
var pos=_963.position(_967,true);
_964.set(this.anchor,(_96c=="mblTooltipAbove"||_96c=="mblTooltipBelow")?{top:"",left:Math.max(0,pos.x-best.x+(pos.w>>1)-(this.arrow.offsetWidth>>1))+"px"}:{left:"",top:Math.max(0,pos.y-best.y+(pos.h>>1)-(this.arrow.offsetHeight>>1))+"px"});
_961.replace(_969,"mblTooltipVisible","mblTooltipHidden");
this.resize=lang.hitch(this,"show",_967,_968);
return best;
},hide:function(){
this.resize=undefined;
_961.replace(this.domNode,"mblTooltipHidden","mblTooltipVisible");
},onBlur:function(e){
return true;
},destroy:function(){
if(this.anchor){
this.anchor.removeChild(this.innerArrow);
this.anchor.removeChild(this.arrow);
this.domNode.removeChild(this.anchor);
this.anchor=this.arrow=this.innerArrow=undefined;
}
this.inherited(arguments);
}});
});
},"dojo/_base/sniff":function(){
define(["./kernel","../has"],function(dojo,has){
if(!1){
return has;
}
dojo.isBrowser=true,dojo._name="browser";
var _96d=has.add,n=navigator,dua=n.userAgent,dav=n.appVersion,tv=parseFloat(dav),_96e,_96f,_970,_971,_972,_973,_974,_975,_976,isIE,isFF,_977,_978,_979,_97a;
if(dua.indexOf("AdobeAIR")>=0){
_96f=1;
}
_970=(dav.indexOf("Konqueror")>=0)?tv:0;
_971=parseFloat(dua.split("WebKit/")[1])||undefined;
_972=parseFloat(dua.split("Chrome/")[1])||undefined;
_973=dav.indexOf("Macintosh")>=0;
_978=/iPhone|iPod|iPad/.test(dua);
_979=parseFloat(dua.split("Android ")[1])||undefined;
_97a=typeof opera!="undefined"&&opera.wiiremote;
var _97b=Math.max(dav.indexOf("WebKit"),dav.indexOf("Safari"),0);
if(_97b&&!_972){
_974=parseFloat(dav.split("Version/")[1]);
if(!_974||parseFloat(dav.substr(_97b+7))<=419.3){
_974=2;
}
}
if(!has("dojo-webkit")){
if(dua.indexOf("Opera")>=0){
_96e=tv;
if(_96e>=9.8){
_96e=parseFloat(dua.split("Version/")[1])||tv;
}
}
if(dua.indexOf("Gecko")>=0&&!_970&&!_971){
_975=_976=tv;
}
if(_976){
isFF=parseFloat(dua.split("Firefox/")[1]||dua.split("Minefield/")[1])||undefined;
}
if(document.all&&!_96e){
isIE=parseFloat(dav.split("MSIE ")[1])||undefined;
var mode=document.documentMode;
if(mode&&mode!=5&&Math.floor(isIE)!=mode){
isIE=mode;
}
}
}
_977=document.compatMode=="BackCompat";
_96d("opera",dojo.isOpera=_96e);
_96d("air",dojo.isAIR=_96f);
_96d("khtml",dojo.isKhtml=_970);
_96d("webkit",dojo.isWebKit=_971);
_96d("chrome",dojo.isChrome=_972);
_96d("mac",dojo.isMac=_973);
_96d("safari",dojo.isSafari=_974);
_96d("mozilla",dojo.isMozilla=dojo.isMoz=_975);
_96d("ie",dojo.isIE=isIE);
_96d("ff",dojo.isFF=isFF);
_96d("quirks",dojo.isQuirks=_977);
_96d("ios",dojo.isIos=_978);
_96d("android",dojo.isAndroid=_979);
dojo.locale=dojo.locale||(isIE?n.userLanguage:n.language).toLowerCase();
return has;
});
},"dojox/mobile/SpinWheel":function(){
define("dojox/mobile/SpinWheel",["dojo/_base/array","dojo/_base/declare","dojo/_base/lang","dojo/dom-class","dojo/dom-construct","dijit/_Contained","dijit/_Container","dijit/_WidgetBase","./SpinWheelSlot"],function(_97c,_97d,lang,_97e,_97f,_980,_981,_982,_983){
return _97d("dojox.mobile.SpinWheel",[_982,_981,_980],{slotClasses:[],slotProps:[],centerPos:0,buildRendering:function(){
this.inherited(arguments);
_97e.add(this.domNode,"mblSpinWheel");
this.centerPos=Math.round(this.domNode.offsetHeight/2);
this.slots=[];
for(var i=0;i<this.slotClasses.length;i++){
this.slots.push(((typeof this.slotClasses[i]=="string")?lang.getObject(this.slotClasses[i]):this.slotClasses[i])(this.slotProps[i]));
this.addChild(this.slots[i]);
}
_97f.create("DIV",{className:"mblSpinWheelBar"},this.domNode);
},startup:function(){
this.inherited(arguments);
this.reset();
},getValue:function(){
var a=[];
_97c.forEach(this.getChildren(),function(w){
if(w instanceof _983){
a.push(w.getValue());
}
},this);
return a;
},setValue:function(a){
var i=0;
_97c.forEach(this.getChildren(),function(w){
if(w instanceof _983){
w.setValue(a[i]);
w.setColor(a[i]);
i++;
}
},this);
},reset:function(){
_97c.forEach(this.getChildren(),function(w){
if(w instanceof _983){
w.setInitialValue();
}
},this);
}});
});
},"dojo/_base/window":function(){
define(["./kernel","../has","./sniff"],function(dojo,has){
dojo.doc=this["document"]||null;
dojo.body=function(){
return dojo.doc.body||dojo.doc.getElementsByTagName("body")[0];
};
dojo.setContext=function(_984,_985){
dojo.global=ret.global=_984;
dojo.doc=ret.doc=_985;
};
dojo.withGlobal=function(_986,_987,_988,_989){
var _98a=dojo.global;
try{
dojo.global=ret.global=_986;
return dojo.withDoc.call(null,_986.document,_987,_988,_989);
}
finally{
dojo.global=ret.global=_98a;
}
};
dojo.withDoc=function(_98b,_98c,_98d,_98e){
var _98f=dojo.doc,oldQ=dojo.isQuirks,_990=dojo.isIE,isIE,mode,pwin;
try{
dojo.doc=ret.doc=_98b;
dojo.isQuirks=has.add("quirks",dojo.doc.compatMode=="BackCompat",true,true);
if(has("ie")){
if((pwin=_98b.parentWindow)&&pwin.navigator){
isIE=parseFloat(pwin.navigator.appVersion.split("MSIE ")[1])||undefined;
mode=_98b.documentMode;
if(mode&&mode!=5&&Math.floor(isIE)!=mode){
isIE=mode;
}
dojo.isIE=has.add("ie",isIE,true,true);
}
}
if(_98d&&typeof _98c=="string"){
_98c=_98d[_98c];
}
return _98c.apply(_98d,_98e||[]);
}
finally{
dojo.doc=ret.doc=_98f;
dojo.isQuirks=has.add("quirks",oldQ,true,true);
dojo.isIE=has.add("ie",_990,true,true);
}
};
var ret={global:dojo.global,doc:dojo.doc,body:dojo.body,setContext:dojo.setContext,withGlobal:dojo.withGlobal,withDoc:dojo.withDoc};
return ret;
});
},"dojox/mobile/EdgeToEdgeList":function(){
define("dojox/mobile/EdgeToEdgeList",["dojo/_base/declare","./RoundRectList"],function(_991,_992){
return _991("dojox.mobile.EdgeToEdgeList",_992,{buildRendering:function(){
this.inherited(arguments);
this.domNode.className="mblEdgeToEdgeList";
}});
});
},"dijit/popup":function(){
define("dijit/popup",["dojo/_base/array","dojo/aspect","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/_base/event","dojo/keys","dojo/_base/lang","dojo/on","dojo/_base/sniff","dojo/_base/window","./place","./BackgroundIframe","."],function(_993,_994,_995,_996,dom,_997,_998,_999,_99a,_99b,keys,lang,on,has,win,_99c,_99d,_99e){
var _99f=_996(null,{_stack:[],_beginZIndex:1000,_idGen:1,_createWrapper:function(_9a0){
var _9a1=_9a0._popupWrapper,node=_9a0.domNode;
if(!_9a1){
_9a1=_998.create("div",{"class":"dijitPopup",style:{display:"none"},role:"presentation"},win.body());
_9a1.appendChild(node);
var s=node.style;
s.display="";
s.visibility="";
s.position="";
s.top="0px";
_9a0._popupWrapper=_9a1;
_994.after(_9a0,"destroy",function(){
_998.destroy(_9a1);
delete _9a0._popupWrapper;
});
}
return _9a1;
},moveOffScreen:function(_9a2){
var _9a3=this._createWrapper(_9a2);
_99a.set(_9a3,{visibility:"hidden",top:"-9999px",display:""});
},hide:function(_9a4){
var _9a5=this._createWrapper(_9a4);
_99a.set(_9a5,"display","none");
},getTopPopup:function(){
var _9a6=this._stack;
for(var pi=_9a6.length-1;pi>0&&_9a6[pi].parent===_9a6[pi-1].widget;pi--){
}
return _9a6[pi];
},open:function(args){
var _9a7=this._stack,_9a8=args.popup,_9a9=args.orient||["below","below-alt","above","above-alt"],ltr=args.parent?args.parent.isLeftToRight():_999.isBodyLtr(),_9aa=args.around,id=(args.around&&args.around.id)?(args.around.id+"_dropdown"):("popup_"+this._idGen++);
while(_9a7.length&&(!args.parent||!dom.isDescendant(args.parent.domNode,_9a7[_9a7.length-1].widget.domNode))){
this.close(_9a7[_9a7.length-1].widget);
}
var _9ab=this._createWrapper(_9a8);
_997.set(_9ab,{id:id,style:{zIndex:this._beginZIndex+_9a7.length},"class":"dijitPopup "+(_9a8.baseClass||_9a8["class"]||"").split(" ")[0]+"Popup",dijitPopupParent:args.parent?args.parent.id:""});
if(has("ie")||has("mozilla")){
if(!_9a8.bgIframe){
_9a8.bgIframe=new _99d(_9ab);
}
}
var best=_9aa?_99c.around(_9ab,_9aa,_9a9,ltr,_9a8.orient?lang.hitch(_9a8,"orient"):null):_99c.at(_9ab,args,_9a9=="R"?["TR","BR","TL","BL"]:["TL","BL","TR","BR"],args.padding);
_9ab.style.display="";
_9ab.style.visibility="visible";
_9a8.domNode.style.visibility="visible";
var _9ac=[];
_9ac.push(on(_9ab,_995._keypress,lang.hitch(this,function(evt){
if(evt.charOrCode==keys.ESCAPE&&args.onCancel){
_99b.stop(evt);
args.onCancel();
}else{
if(evt.charOrCode===keys.TAB){
_99b.stop(evt);
var _9ad=this.getTopPopup();
if(_9ad&&_9ad.onCancel){
_9ad.onCancel();
}
}
}
})));
if(_9a8.onCancel&&args.onCancel){
_9ac.push(_9a8.on("cancel",args.onCancel));
}
_9ac.push(_9a8.on(_9a8.onExecute?"execute":"change",lang.hitch(this,function(){
var _9ae=this.getTopPopup();
if(_9ae&&_9ae.onExecute){
_9ae.onExecute();
}
})));
_9a7.push({widget:_9a8,parent:args.parent,onExecute:args.onExecute,onCancel:args.onCancel,onClose:args.onClose,handlers:_9ac});
if(_9a8.onOpen){
_9a8.onOpen(best);
}
return best;
},close:function(_9af){
var _9b0=this._stack;
while((_9af&&_993.some(_9b0,function(elem){
return elem.widget==_9af;
}))||(!_9af&&_9b0.length)){
var top=_9b0.pop(),_9b1=top.widget,_9b2=top.onClose;
if(_9b1.onClose){
_9b1.onClose();
}
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_9b1&&_9b1.domNode){
this.hide(_9b1);
}
if(_9b2){
_9b2();
}
}
}});
return (_99e.popup=new _99f());
});
},"dojox/mobile/uacss":function(){
define("dojox/mobile/uacss",["dojo/_base/kernel","dojo/_base/lang","dojo/_base/window","dojox/mobile/sniff"],function(dojo,lang,win,has){
win.doc.documentElement.className+=lang.trim([has("bb")?"dj_bb":"",has("android")?"dj_android":"",has("iphone")?"dj_iphone":"",has("ipod")?"dj_ipod":"",has("ipad")?"dj_ipad":""].join(" ").replace(/ +/g," "));
return dojo;
});
},"dijit/_base/window":function(){
define("dijit/_base/window",["dojo/window",".."],function(_9b3,_9b4){
_9b4.getDocumentWindow=function(doc){
return _9b3.get(doc);
};
});
},"dijit/_WidgetBase":function(){
define("dijit/_WidgetBase",["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./registry"],function(_9b5,_9b6,_9b7,_9b8,_9b9,_9ba,dom,_9bb,_9bc,_9bd,_9be,_9bf,_9c0,lang,on,_9c1,_9c2,_9c3,win,_9c4){
if(!_9c0.isAsync){
_9c1(0,function(){
var _9c5=["dijit/_base/manager"];
_9b5(_9c5);
});
}
var _9c6={};
function _9c7(obj){
var ret={};
for(var attr in obj){
ret[attr.toLowerCase()]=true;
}
return ret;
};
function _9c8(attr){
return function(val){
_9bb[val?"set":"remove"](this.domNode,attr,val);
this._set(attr,val);
};
};
return _9ba("dijit._WidgetBase",_9c2,{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_9c8("lang"),dir:"",_setDirAttr:_9c8("dir"),textDir:"","class":"",_setClassAttr:{node:"domNode",type:"class"},style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,attributeMap:{},_blankGif:_9b8.blankGif||_9b5.toUrl("dojo/resources/blank.gif"),postscript:function(_9c9,_9ca){
this.create(_9c9,_9ca);
},create:function(_9cb,_9cc){
this.srcNodeRef=dom.byId(_9cc);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_9cb){
this.params=_9cb;
lang.mixin(this,_9cb);
}
this.postMixInProperties();
if(!this.id){
this.id=_9c4.getUniqueId(this.declaredClass.replace(/\./g,"_"));
}
_9c4.add(this);
this.buildRendering();
if(this.domNode){
this._applyAttributes();
var _9cd=this.srcNodeRef;
if(_9cd&&_9cd.parentNode&&this.domNode!==_9cd){
_9cd.parentNode.replaceChild(this.domNode,_9cd);
}
}
if(this.domNode){
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(this.srcNodeRef&&!this.srcNodeRef.parentNode){
delete this.srcNodeRef;
}
this._created=true;
},_applyAttributes:function(){
var ctor=this.constructor,list=ctor._setterAttrs;
if(!list){
list=(ctor._setterAttrs=[]);
for(var attr in this.attributeMap){
list.push(attr);
}
var _9ce=ctor.prototype;
for(var _9cf in _9ce){
if(_9cf in this.attributeMap){
continue;
}
var _9d0="_set"+_9cf.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
})+"Attr";
if(_9d0 in _9ce){
list.push(_9cf);
}
}
}
_9b6.forEach(list,function(attr){
if(this.params&&attr in this.params){
}else{
if(this[attr]){
this.set(attr,this[attr]);
}
}
},this);
for(var _9d1 in this.params){
this.set(_9d1,this[_9d1]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||_9bd.create("div");
}
if(this.baseClass){
var _9d2=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_9d2=_9d2.concat(_9b6.map(_9d2,function(name){
return name+"Rtl";
}));
}
_9bc.add(this.domNode,_9d2);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_9b6.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_9d3){
this._beingDestroyed=true;
this.destroyDescendants(_9d3);
this.destroy(_9d3);
},destroy:function(_9d4){
this._beingDestroyed=true;
this.uninitialize();
var c;
while(c=this._connects.pop()){
c.remove();
}
var w;
while(w=this._supportingWidgets.pop()){
if(w.destroyRecursive){
w.destroyRecursive();
}else{
if(w.destroy){
w.destroy();
}
}
}
this.destroyRendering(_9d4);
_9c4.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_9d5){
if(this.bgIframe){
this.bgIframe.destroy(_9d5);
delete this.bgIframe;
}
if(this.domNode){
if(_9d5){
_9bb.remove(this.domNode,"widgetId");
}else{
_9bd.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_9d5){
_9bd.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_9d6){
_9b6.forEach(this.getChildren(),function(_9d7){
if(_9d7.destroyRecursive){
_9d7.destroyRecursive(_9d6);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_9d8){
var _9d9=this.domNode;
if(lang.isObject(_9d8)){
_9bf.set(_9d9,_9d8);
}else{
if(_9d9.style.cssText){
_9d9.style.cssText+="; "+_9d8;
}else{
_9d9.style.cssText=_9d8;
}
}
this._set("style",_9d8);
},_attrToDom:function(attr,_9da,_9db){
_9db=arguments.length>=3?_9db:this.attributeMap[attr];
_9b6.forEach(lang.isArray(_9db)?_9db:[_9db],function(_9dc){
var _9dd=this[_9dc.node||_9dc||"domNode"];
var type=_9dc.type||"attribute";
switch(type){
case "attribute":
if(lang.isFunction(_9da)){
_9da=lang.hitch(this,_9da);
}
var _9de=_9dc.attribute?_9dc.attribute:(/^on[A-Z][a-zA-Z]*$/.test(attr)?attr.toLowerCase():attr);
_9bb.set(_9dd,_9de,_9da);
break;
case "innerText":
_9dd.innerHTML="";
_9dd.appendChild(win.doc.createTextNode(_9da));
break;
case "innerHTML":
_9dd.innerHTML=_9da;
break;
case "class":
_9bc.replace(_9dd,_9da,this[attr]);
break;
}
},this);
},get:function(name){
var _9df=this._getAttrNames(name);
return this[_9df.g]?this[_9df.g]():this[name];
},set:function(name,_9e0){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var _9e1=this._getAttrNames(name),_9e2=this[_9e1.s];
if(lang.isFunction(_9e2)){
var _9e3=_9e2.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _9e4=this.focusNode&&!lang.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_9e4].tagName,_9e5=_9c6[tag]||(_9c6[tag]=_9c7(this[_9e4])),map=name in this.attributeMap?this.attributeMap[name]:_9e1.s in this?this[_9e1.s]:((_9e1.l in _9e5&&typeof _9e0!="function")||/^aria-|^data-|^role$/.test(name))?_9e4:null;
if(map!=null){
this._attrToDom(name,_9e0,map);
}
this._set(name,_9e0);
}
return _9e3||this;
},_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
var uc=name.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[name]={n:name+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(name,_9e6){
var _9e7=this[name];
this[name]=_9e6;
if(this._watchCallbacks&&this._created&&_9e6!==_9e7){
this._watchCallbacks(name,_9e7,_9e6);
}
},on:function(type,func){
return _9b7.after(this,this._onMap(type),func,true);
},_onMap:function(type){
var ctor=this.constructor,map=ctor._onMap;
if(!map){
map=(ctor._onMap={});
for(var attr in ctor.prototype){
if(/^on/.test(attr)){
map[attr.replace(/^on/,"").toLowerCase()]=attr;
}
}
}
return map[type.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_9c4.findWidgets(this.containerNode):[];
},getParent:function(){
return _9c4.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_9e8,_9e9){
var _9ea=_9b9.connect(obj,_9e8,this,_9e9);
this._connects.push(_9ea);
return _9ea;
},disconnect:function(_9eb){
var i=_9b6.indexOf(this._connects,_9eb);
if(i!=-1){
_9eb.remove();
this._connects.splice(i,1);
}
},subscribe:function(t,_9ec){
var _9ed=_9c3.subscribe(t,lang.hitch(this,_9ec));
this._connects.push(_9ed);
return _9ed;
},unsubscribe:function(_9ee){
this.disconnect(_9ee);
},isLeftToRight:function(){
return this.dir?(this.dir=="ltr"):_9be.isBodyLtr();
},isFocusable:function(){
return this.focus&&(_9bf.get(this.domNode,"display")!="none");
},placeAt:function(_9ef,_9f0){
if(_9ef.declaredClass&&_9ef.addChild){
_9ef.addChild(this,_9f0);
}else{
_9bd.place(this.domNode,_9ef,_9f0);
}
return this;
},getTextDir:function(text,_9f1){
return _9f1;
},applyTextDir:function(){
}});
});
},"dojox/mobile/app/AlertDialog":function(){
define(["dijit","dojo","dojox","dojo/require!dijit/_WidgetBase"],function(_9f2,dojo,_9f3){
dojo.provide("dojox.mobile.app.AlertDialog");
dojo.experimental("dojox.mobile.app.AlertDialog");
dojo.require("dijit._WidgetBase");
dojo.declare("dojox.mobile.app.AlertDialog",_9f2._WidgetBase,{title:"",text:"",controller:null,buttons:null,defaultButtonLabel:"OK",onChoose:null,constructor:function(){
this.onClick=dojo.hitch(this,this.onClick);
this._handleSelect=dojo.hitch(this,this._handleSelect);
},buildRendering:function(){
this.domNode=dojo.create("div",{"class":"alertDialog"});
var _9f4=dojo.create("div",{"class":"alertDialogBody"},this.domNode);
dojo.create("div",{"class":"alertTitle",innerHTML:this.title||""},_9f4);
dojo.create("div",{"class":"alertText",innerHTML:this.text||""},_9f4);
var _9f5=dojo.create("div",{"class":"alertBtns"},_9f4);
if(!this.buttons||this.buttons.length==0){
this.buttons=[{label:this.defaultButtonLabel,value:"ok","class":"affirmative"}];
}
var _9f6=this;
dojo.forEach(this.buttons,function(_9f7){
var btn=new _9f3.mobile.Button({btnClass:_9f7["class"]||"",label:_9f7.label});
btn._dialogValue=_9f7.value;
dojo.place(btn.domNode,_9f5);
_9f6.connect(btn,"onClick",_9f6._handleSelect);
});
var _9f8=this.controller.getWindowSize();
this.mask=dojo.create("div",{"class":"dialogUnderlayWrapper",innerHTML:"<div class=\"dialogUnderlay\"></div>",style:{width:_9f8.w+"px",height:_9f8.h+"px"}},this.controller.assistant.domNode);
this.connect(this.mask,"onclick",function(){
_9f6.onChoose&&_9f6.onChoose();
_9f6.hide();
});
},postCreate:function(){
this.subscribe("/dojox/mobile/app/goback",this._handleSelect);
},_handleSelect:function(_9f9){
var node;
console.log("handleSelect");
if(_9f9&&_9f9.target){
node=_9f9.target;
while(!_9f2.byNode(node)){
node-node.parentNode;
}
}
if(this.onChoose){
this.onChoose(node?_9f2.byNode(node)._dialogValue:undefined);
}
this.hide();
},show:function(){
this._doTransition(1);
},hide:function(){
this._doTransition(-1);
},_doTransition:function(dir){
var anim;
var h=dojo.marginBox(this.domNode.firstChild).h;
var _9fa=this.controller.getWindowSize().h;
console.log("dialog height = "+h," body height = "+_9fa);
var high=_9fa-h;
var low=_9fa;
var _9fb=dojo.fx.slideTo({node:this.domNode,duration:400,top:{start:dir<0?high:low,end:dir<0?low:high}});
var _9fc=dojo[dir<0?"fadeOut":"fadeIn"]({node:this.mask,duration:400});
var anim=dojo.fx.combine([_9fb,_9fc]);
var _9fd=this;
dojo.connect(anim,"onEnd",this,function(){
if(dir<0){
_9fd.domNode.style.display="none";
dojo.destroy(_9fd.domNode);
dojo.destroy(_9fd.mask);
}
});
anim.play();
},destroy:function(){
this.inherited(arguments);
dojo.destroy(this.mask);
},onClick:function(){
}});
});
}}});
(function(){
require({cache:{}});
!require.async&&require(["dojo"]);
require.boot&&require.apply(null,require.boot);
})();

