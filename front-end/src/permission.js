import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import Layout from '@/layout'
import {indexOf} from "core-js/internals/array-includes";
import {Logger} from "runjs/lib/common";

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasGetUserInfo = store.getters.name
      if (hasGetUserInfo) {
        next()
      } else {
        try {
          // get user info
          const userInfo = await store.dispatch('user/getInfo')
          await store.dispatch('user/serPermissions', userInfo.permissions)
          const allRoutes = await store.dispatch('user/listMenuTree')
          const newRoutes = handleMenus(allRoutes.data, userInfo.menuIds)
          newRoutes.push(
            { path: '*', redirect: '/404', hidden: true }
          )
          router.addRoutes(newRoutes)
          store.dispatch('user/setRoutes', newRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

function handleMenus(routes, currentUserMenus) {
  routes.forEach(route => {
    if (route.children.length > 0) {
      const newChildern = handleMenus(route.children, currentUserMenus)
      route.children = newChildern
    }
    if (currentUserMenus.indexOf(route.id) === -1 && route.children.length === 0) {
      route.hidden = true
    } else {
      if(currentUserMenus.indexOf(route.id) === -1) {
        route.hidden = true
      } else {
        route.hidden = false
      }
    }
    route.meta = {
      title: route.name,
      icon: route.icon,
      noCache: false
    }

    if (route.component === 'Layout') {
      route.component = Layout
    } else {
      if (route.parentId === -1) {
        const component = route.component + ''
        route.children = [{
          path: '/manager',
          name: route.name,
          component: (resolve) => require(['@/views' + component], resolve),
          meta: { title: route.name, icon: route.icon }
        }]
        route.name = ''
        route.path = '/user'
        route.redirect = '/user/manager'
        route.component = Layout
      } else {
        const component = route.component + ''
        route.component = (resolve) => require(['@/views' + component], resolve)
      }
    }
  })
  return routes
}
