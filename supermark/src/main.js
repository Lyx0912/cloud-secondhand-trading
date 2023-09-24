import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'assets/css/globle.css'
import axios from 'axios'

import { WeToolbar, WeEditable, WeEditor } from 'wangeditor5-for-vue2'

Vue.prototype.$http = axios
Vue.prototype.$http.defaults.baseURL = '' // `baseURL` 将自动加在 `url` 前面，除非 `url` 是一个绝对 URL
Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(WeToolbar)
Vue.use(WeEditable)
Vue.use(WeEditor)
Vue.directive('copy', {
  bind: function(el, binding) {
    el.$copy = function() {
      const textarea = document.createElement('textarea');
      textarea.value = binding.value;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('Copy');
      document.body.removeChild(textarea);
    }
    el.addEventListener('click', el.$copy);
  },
  unbind: function(el) {
    el.removeEventListener('click', el.$copy);
  }
});
//配置请求的根路径
axios.interceptors.request.use(config=>{
  // config.headers.Authorization=window.sessionStorage.getItem("token")
  // 在发送请求之前做些什么
  // 判断是否存在token,如果存在将每个页面header添加token
  var url = config.url
  if(!(url.substring(0,24)!=="/api/cloud-member/MemberLogin")){
    console.log(url)
    if (url.substring(0,16)==="/api/cloud-order"||url.substring(0,17)==="/api/cloud-member") {
    
      
      if(sessionStorage.getItem("token")==null){
        router.push('/login')
      }
      if(config.headers.common['token'] == sessionStorage.getItem("token")){
        router.push('/login')
      }
    //   config.headers.common['token'] = localStorage.getItem("token");
    // alert("存在token加入请求头，请求头为"+JSON.stringify(config.headers))
    }
  }
  return config
},error => {
  router.push('/login')
  return Promise.reject(error);
})
axios.interceptors.response.use(config=>{
  return config
},error => {
  return Promise.reject(error);
})
Vue.filter('dateFormat', function(originVal) {
  const dt = new Date(originVal)
  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')
  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')
  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
