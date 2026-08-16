import{a as I}from"./chunk-LDGUOCHA.js";import{$ as m,A as p,Ka as x,La as f,T as a,ba as d,fa as t,ga as n,ha as s,pb as u,qb as C,rb as v,ua as e,xa as c,yb as g,zb as S}from"./chunk-M3JHAR7E.js";function E(i,r){i&1&&(t(0,"span"),e(1,"Invalid URL, Please open the valid url"),n())}function _(i,r){i&1&&(t(0,"span"),e(1,"Invalid Order Number, Please Provide Valid Order Number"),n())}function y(i,r){i&1&&(t(0,"span"),e(1,"Order/Invoice Number Already Verified"),n())}var R=(()=>{let r=class r{constructor(){this.orderStatus=I.orderStatus}};r.\u0275fac=function(o){return new(o||r)},r.\u0275cmp=p({type:r,selectors:[["app-invalid-order-controls"]],standalone:!0,features:[c],decls:31,vars:3,consts:[["xs","12",1,"px-0"],[1,"mb-2"],["src","./assets/brand/logo.png",2,"width","100px","height","40px"],[1,"d-grid","gap-2","col-6","mx-auto"],[4,"ngIf"]],template:function(o,l){o&1&&(t(0,"c-row"),e(1,`
  `),t(2,"c-col",0),e(3,`
    `),t(4,"c-card",1),e(5,`
      `),t(6,"c-card-header"),e(7,`
        `),s(8,"img",2),e(9,`
        `),t(10,"strong"),e(11,"Address Verification Form"),n(),e(12,`
      `),n(),e(13,`
      `),t(14,"c-card-body"),e(15,` 
          `),t(16,"c-row",3),e(17,`
            `),t(18,"c-col"),e(19,`
              `),m(20,E,2,0,"span",4),e(21,`
              `),m(22,_,2,0,"span",4),e(23,`
              `),m(24,y,2,0,"span",4),e(25,`
            `),n(),e(26,`
          `),n(),e(27,`
      `),n(),e(28,`
    `),n(),e(29,`
  `),n(),e(30,`
`),n()),o&2&&(a(20),d("ngIf",l.orderStatus=="INVALID_URL"),a(2),d("ngIf",l.orderStatus=="INVALID"),a(2),d("ngIf",l.orderStatus=="ALREADY_VERIFIED"))},dependencies:[S,g,u,v,C,f,x],styles:["[_nghost-%COMP%]   #exampleColorInput[_ngcontent-%COMP%]{min-width:2.5rem}[_nghost-%COMP%]   .color-box[_ngcontent-%COMP%]{min-width:2rem;min-height:2rem}"]});let i=r;return i})();export{R as InvalidOrderComponent};
