// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router/router'
import axios from 'axios'
import Mint from 'mint-ui'
import AMap from 'vue-amap'
import Vuex from 'vuex'
import UAParser from 'ua-parser-js'
import 'mint-ui/lib/style.css'

import 'lib-flexible/flexible';

import { ConfirmPlugin, DatetimePlugin, ToastPlugin, LoadingPlugin, AlertPlugin } from 'vux'
Vue.use(ConfirmPlugin)
Vue.use(DatetimePlugin)
Vue.use(ToastPlugin)
Vue.use(LoadingPlugin)
Vue.use(AlertPlugin)

Vue.prototype.$http = axios
Vue.use(Mint);
//高德地图
Vue.use(AMap)
AMap.initAMapApiLoader({
   /*key: '639296d0ac073b20f28b281f1110fa8c',*/
   key:'b348b6304b328c5961c8d6bd8b95cb5c',
   plugin: ['AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale', 'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType', 'AMap.PolyEditor', 'AMap.CircleEditor','MarkerClusterer']


})
Vue.use(Vuex)
Vue.config.productionTip = false

let member_id = null;
// let ua = new UAParser().getResult()
// if (ua.browser.name !== 'WeChat') {
//   member_id = localStorage.getItem('member_id')
// }

member_id = localStorage.getItem('member_id')
const store = new Vuex.Store({
  state: {
    member_id: member_id,
    staffreg: {},
  },
  mutations: {
    login (state, { member_id }) {
      localStorage.setItem('member_id', member_id);
      state.member_id = member_id
    },
    logout (state) {
      state.member_id = null
      localStorage.removeItem('member_id');
    },
    staffshop(state, o){
      const { staffreg } = state
      state.staffreg = {
        ...staffreg,
        ...o
      }
    }
  }
})



router.beforeEach(async (to, from, next) => {
  const goLogin = () => {
    next({
      path: '/register',
      query: {redirect: to.fullPath}
    })
  }

  const goNext = (user) => {
    // user.sign = buildSign(user)
    // store.commit('login', user)
    next()
  }

  console.log(store.state.member_id)
  console.log(to)
  if (store.state.member_id || ~['/getPosition','/registerInfo','/register','/squareInfo', '/sellers','/voucher','/sell','/getPosition','/car','/carDetail','/activities'].indexOf(to.path)) {
    next()
  } else {
    goLogin()
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
