import Vue from 'vue'
import Router from 'vue-router'
const member = r => require.ensure([], () => r(require('../pages/member/member')), 'member')
const userInfo = r => require.ensure([], () => r(require('../pages/userInfo/userInfo')), 'userInfo')
const changePhone = r => require.ensure([], () => r(require('../pages/changPhone/changePhone')), 'changePhone')
const changeSuccess = r => require.ensure([], () => r(require('../pages/success/success')), 'changeSuccess')
const registerInfo = r => require.ensure([], () => r(require('../pages/userBasicInfo/userBasicInfo')), 'registerInfo')
const registerSucc = r => require.ensure([], () => r(require('../pages/registerSuccess/success')), 'registerSucc')
const staRegSuccess =  r => require.ensure([], () => r(require('../pages/staRegSuccess/success')), 'staRegSuccess')
const register = r => require.ensure([], () => r(require('../pages/register/register')), 'register')
const staffRegister = r => require.ensure([], () => r(require('../pages/staffRegister/staffRegister')), 'staffRegister')
const squareInfo = r => require.ensure([], () => r(require('../pages/squareInfo/squareInfo')), 'squareInfo')
const getPosition = r => require.ensure([], () => r(require('../pages/position/position')), 'getPosition')
const sellers = r => require.ensure([], () => r(require('../pages/sellers/sellers')), 'sellers')
const sell = r => require.ensure([], () => r(require('../pages/sell/sell')), 'sell')
const ticketsUpload = r => require.ensure([], () => r(require('../pages/ticketsUpload/ticketsUpload')), 'sell')
const memberLevel = r => require.ensure([], () => r(require('../pages/memberLevel/memberLevel')), 'memberLevel')
const voucher = r => require.ensure([], () => r(require('../pages/voucher/voucher')), 'voucher')
const voucherDetail = r => require.ensure([], () => r(require('../pages/voucherDetail/voucherDetail')), 'voucherDetail')
const memVoucher = r => require.ensure([], () => r(require('../pages/memVoucher/memVoucher')), 'memVoucher')
const activities = r => require.ensure([], () => r(require('../pages/activities/activities')), 'activities')
const activeDetail = r => require.ensure([], () => r(require('../pages/activeDetail/activeDetail')), 'activeDetail')
const memActivities = r => require.ensure([], () => r(require('../pages/memActivities/memActivities')), 'memActivities')
const memVouDetail = r => require.ensure([], () => r(require('../pages/memVouDetail/memVouDetail')), 'memVouDetail')
const login = r => require.ensure([], () => r(require('../pages/login/login')), '/login')
const test = r => require.ensure([], () => r(require('../pages/test/test')), 'test')
const car = r => require.ensure([], () => r(require('../pages/car/car')), 'car')
const carDetail = r => require.ensure([], () => r(require('../pages/car/carDetail')), 'carDetail')

const staffRegister0 = r => require.ensure([], () => r(require('../pages/staffRegister/staffRegister0')), 'staffRegister0')
const staffRegister1 = r => require.ensure([], () => r(require('../pages/staffRegister/staffRegister1')), 'staffRegister1')

Vue.use(Router)
export default new Router({
  mode:'history',//更改模式，默认为hash
  routes: [
    //地址为空时跳转member页面
    { path: '', redirect: '/member'},
    { path: '/member', component: member},
    { path: '/userInfo', component: userInfo},
    { path: '/changePhone', component:changePhone},
    { path: '/changeSuccess', component:changeSuccess, },
    { path:'/staffRegister', component:staffRegister, }, //核销人员注册
    { path:'/staffRegister0', component:staffRegister0, }, //核销人员注册
    { path:'/staffRegister1', component:staffRegister1, }, //核销人员注册
    { path:'/staRegSuccess', component:staRegSuccess, }, //注册成功
    { path: '/register', component:register, }, //会员注册
    { path: '/registerInfo', component:registerInfo, }, //注册资料填写
    { path:'/registerSucc', component:registerSucc }, //注册成功
    { path:'/squareInfo', component:squareInfo, }, //商场简介
    { path:'/getPosition', component:getPosition, }, //定位
    { path:'/sellers', component:sellers, }, //入驻商户(列表)
    { path:'/sell', component:sell, },
    { path:'/ticketsUpload', component:ticketsUpload, }, //小票上传
    { path:'/memberLevel', component:memberLevel, }, //我的等级
    { path:'/voucher', component:voucher }, //优惠券
    { path:'/memVoucher', component:memVoucher }, //优惠券(会员)
    { path:'/voucherDetail', component:voucherDetail }, //券详情
    { path:'/activities', component:activities }, //最新活动
    { path:'/memActivities', component:memActivities }, //我的活动
    { path:'/activeDetail', component:activeDetail }, //活动详情
    { path:'/memVouDetail', component:memVouDetail }, //活动详情
    { path:'/login', component:login }, //登陆接口
    { path:'/test', component:test, }, //测试
    { path:'/car', component:car }, //停车缴费
    { path:'/carDetail', component:carDetail }, //停车缴费

    { path: '/shopSelect', component: ()=>import('@/pages/shopSelect')},
    { path: '/verify', component: ()=>import('@/pages/verify')},
    { path: '/activity_sign_in', component: ()=>import('@/pages/ActivitySignIn')},
    { path: '/zhuanpan', component: ()=>import('@/pages/zhuanpan')},
    { path: '/suggest', component: ()=>import('@/pages/suggest')},
    { path: '/suggest_list', component: () => import('@/pages/suggestList')},
    { path: '/myreward', component: () => import('@/pages/memVoucher/myReward')},

    { path:'/gift_voucher', component: () =>import('@/pages/gift_voucher/voucher') }, //优惠券
    { path:'/gift_memVoucher', component: () =>import('@/pages/gift_memVoucher/memVoucher') }, //优惠券(会员)
    { path:'/gift_voucherDetail', component: () =>import('@/pages/gift_voucherDetail/voucherDetail') }, //券详情
    { path:'/gift_memVouDetail', component: () =>import('@/pages/gift_memVouDetail/memVouDetail') }, //活动详情
    { path:'/gift_verify', component: ()=>import('@/pages/gift_verify')},


  ]
})
