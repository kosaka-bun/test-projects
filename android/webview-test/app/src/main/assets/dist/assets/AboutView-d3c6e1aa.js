import{d as l,r as i,c as _,a as d,o as p,b,e as o,f as s,t as c,u as a,g as f,w as m}from"./index-7ea66a17.js";const v=l("counter",()=>{const t=i(0),e=_(()=>t.value*2);function n(){t.value++}return{count:t,doubleCount:e,increment:n}});const h={class:"about"},C={style:{display:"block"}},x=o("h1",null,"This is an about page",-1),y=o("br",null,null,-1),w={__name:"AboutView",setup(t){const e=v();function n(){e.increment()}return(V,u)=>{const r=d("el-button");return p(),b("div",h,[o("div",C,[x,o("p",null,[s(" counter: "+c(a(e).count),1),y,s(" double counter: "+c(a(e).doubleCount)+" ",1),f(r,{onClick:u[0]||(u[0]=g=>n())},{default:m(()=>[s("incr")]),_:1})])])])}}};export{w as default};
