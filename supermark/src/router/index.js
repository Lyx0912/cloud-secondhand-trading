import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/login/Login'
import Regiter from "@/views/register/Regiter";
import Home from "@/views/frontdesk/home"
import NowHome from "@/views/frontdesk/nowHome"
import Order from "@/views/frontdesk/userinfo/Order";
import ReleaseGoods from "@/views/frontdesk/userinfo/ReleaseGoods"
import HomePage from "@/views/backstage/homepage";
import Goods from "@/views/frontdesk/goods"
import Product from "@/views/frontdesk/product"
import Index from "@/views/frontdesk"
import Checkstand from "@/views/frontdesk/Checkstand"
import OrderDetails from "@/views/frontdesk/userinfo/OrderDetails"
import ReleaseMember from "@/views/frontdesk/userinfo/ReleaseMember"
import Detail from "@/views/frontdesk/detail";
import NowConfirmcart from "@/views/frontdesk/nowConfirmcart";
import UserInfo from "@/views/frontdesk/userinfo/userInfo"
Vue.use(VueRouter)

const routes = [
  {
    path:"/nowConfirmcart/:id",
    component: NowConfirmcart
  },
  {
    path:"/checkstand",
    component: Checkstand
  },
  {
    path: '/',
    redirect:"/home"
  },
  {
    path: '/login',
    component: Login
  },
  {
    path:'/register',
    component: Regiter
  },
  {
    path:"/home" ,
    component: Home,
    children:[
      {
        path: "/",
        redirect:"/index"
      },
      {
       path:"/index",
        component:Index,
      },
      {
        path:"/goods",
        component:Goods
      },
      {
        path:"/detail/:gid",
        component: Detail
      },
      {
        path:"/product/:pid",
        component: Product
      },
      {
        path:"/activityNotic",
        component:()=>import("@/views/frontdesk/activityNotic")
      },
      {
        path: "/noticeShow",
        component: () => import("@/views/frontdesk/noticeShow")
      }
    ]
  },
  {
   path:"/homepage",
   component: HomePage,
    children: [
      {
        path:"/",
        redirect:"/homeList"
      },
      {
        path:"/homeList",
        component:()=>import("@/views/backstage/homeList")
      },
     {
       path:"/addProduct",
       component:()=>import("@/views/backstage/pms/add")
     },
      {
        path:"/commonProduct",
        component:()=>import("@/views/backstage/pms/commonProduct")
      },
      {
        path:"/productList",
        component:()=>import("@/views/backstage/pms/productList")
      },
      {
       path:"/orderList",
        component:()=>import("@/views/backstage/oms/orderList")
      },
      {
        path: "/killtable",
        component: () => import("@/views/backstage/oms/activityshow")
      },
      {
        path:"/marketing",
        component:()=>import("@/views/backstage/mms/Marketing")
      },
      {
        path: "/logs",
        component:()=>import("@/views/backstage/lms/Log")
      }
    ]
  },

  {
    path:"/userInfo",
    component: UserInfo,
    children: [
      {
        path:"/",
        redirect: "/baseInfo"
      },
      {
        path: "/baseInfo",
        component: ()=>import("@/views/frontdesk/userinfo/MyInfo")
      },
      {
        path: "/updatePass",
        component: () => import("@/views/frontdesk/userinfo/MyPass")
      },
      {
        path:"/order",
        component:Order
      },
      {
        path:"/releaseGoods",
        component:ReleaseGoods
      },
      {
        path:"/OrderDetails",
        component:OrderDetails
      },
      {
        path:"/ReleaseMember",
        component:ReleaseMember
      }
    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
//挂载路由导航守卫
// router.beforeEach((to,from,next)=>{
//   if(to.path==="/login") return next()
//   //获取token
//   const tokenStr=window.sessionStorage.getItem("token")
//   if(!tokenStr) return  next("/login")
//   next()
// })

export default router
