<template>
<div>
  <div style="background-color:white;padding:0.5rem;padding-left:0.2rem;display;flex">
    <div style="font-size:0.5rem;">
      你的车牌<label style="color:#A8A8A8;padding-left:0.2rem"><span>{{licenceNum}}</span></label>
    </div>
  </div>
  <div style="width:100%">
    <table style="margin-top:0.5rem;margin-left:0.2rem;width:90%">
      <tr>
        <td style="align:center;font-size:0.8rem;color:#00C4AB;padding-right:0.1rem">
          ¥{{fee}}.00
        </td>
        <td style="color:#B7B7B7">
          入场时间:{{in_date}}<br/>
          <br /> 已停时长：{{stay_time}}
          <br />
          <br /> 停车位置：{{parking_position}}
        </td>
      </tr>
    </table>
  </div>
  <div style="background-color:white;height:0.5rem;margin-top:0.3rem;font-size:0.2rem">
    <div style="padding-left:0.04rem;padding-right:0.1rem">
      <group>
        <popup-radio title=""  :options="options1" v-model="voucherPrice" :placeholder="valueVoucher"></popup-radio>
      </group>
    </div>
  </div>
  <div style="background-color:white;height:0.5rem;font-size:0.2rem;">
    <div style="padding-left:0.2rem;padding-right:0.1rem">积分
      <input type="text" style="float:right;height:100%;border: medium;text-align:right;height:0.42rem;width:73%;padding-right:0.15rem" placeholder="请输入可用积分">
    </div>


  </div>
  <!-- <div style="margin-bottom:0.8rem;padding-right:10%">
    <div style="color:#F79E07;padding-top:0.3rem;padding-left:0.2rem;">温馨提醒{{title}}</div>
    <div style="color:#979797;padding-top:0.3rem;padding-left:0.2rem;">1 积分抵现金比例： 100积分=1元人民币；</div>
    <div style="color:#979797;padding-top:0.3rem;padding-left:0.2rem;">2 积分抵现条件： 100积分起抵，100积分一档。即：只能100，200,300...依此类推</div>
  </div> -->
  <router-link to="/carDetail">
    <div style="position:absolute;right:0;left:0;bottom:0;padding-top:0.2rem;display:flex;position:fixed;right:0;left:0;bottom:0;">

      <div style="width:70%;height:100%;font-size:0.5rem;text-align:center;background-color:#E6E6E6">
        <div style="padding:0.3rem;">
          还需支付
          <label style="color:#00C4AB;">¥{{feeTotal}}.00</label>
        </div>
      </div>
      <div style="color:white;font-size:0.5rem;background-color:#46D0C3;width:30%;height:100%;">
        <div style="padding:0.3rem;" @click="wxpayClik">立即支付{{wxPayParams}}</div>
      </div>
    </div>
  </router-link>
</div>
</template>
<script>
import {
  Group,
  PopupRadio
} from 'vux'

import global from '../../../src/components/common/Global.vue'
const {
  mallId
} = global;
import {
  mapState,
} from 'vuex';

import _ from 'lodash'
export default {
  components: {
    Group,
    PopupRadio
  },
  data() {
    return {
      options1: [{
        key: 'A',
        value: 'label A'
      }, {
        key: 'B',
        value: 'label B'
      }],
      licenceNum: [],
      title: '',
      wxPayParams: '',
      parking_position: '',
      in_date: '',
      in_datey: '',
      stay_time: '',
      voucherPrice: '',
      valueVoucher: '请选择优惠券',
      mes: '',
      fee: '20',
      feeTotal :'20'
    }
  },
  async mounted() {
    let arr = this.$route.query.car_number
    let carNumber = arr.join('');
    let notify = (await this.$http.get('/api/parkingCoupon/getCarPayInfo?memberId=' + this.member_id + '&carNumber=' + carNumber))
    if (notify.data) {
      this.parking_position = notify.data.parking_position
      let in_time = this.timestampToTime(notify.data.in_date);
      this.in_date = in_time
      this.in_datey = notify.data.in_date

      let out_time = new Date()
      out_time = out_time.getTime();
      var nTime = out_time - notify.data.in_date

      //计算出相差天数
      var days = Math.floor(nTime / (24 * 3600 * 1000))

      //计算出小时数

      var leave1 = nTime % (24 * 3600 * 1000) //计算天数后剩余的毫秒数
      var hours = Math.floor(leave1 / (3600 * 1000))
      //计算相差分钟数
      var leave2 = leave1 % (3600 * 1000) //计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000))
      //计算相差秒数
      var leave3 = leave2 % (60 * 1000) //计算分钟数后剩余的毫秒数
      var seconds = Math.round(leave3 / 1000)
      days > 0 ? days = days + '天' : days = ''
      hours > 0 ? hours = hours + '小时' : hours = ''
      minutes > 0 ? minutes = minutes + '分钟' : minutes = ''
      seconds > 0 ? seconds = seconds + '秒' : seconds = ''
      this.stay_time = days + hours + minutes + seconds

    }

    this.licenceNum = carNumber


    let list = (await this.$http.post(`/api/parkingCoupon/member/${this.member_id}/couponList`, {
      couponStatus: 1,
      mallId,
      page: 1,
      size: 200
    })).data
    let aaa = new Date(new Date().setHours(0, 0, 0, 0)) / 1000;
    // list = _.filter(list, o => o.expiry_date_end >= moment().format('YYYY-MM-DD').unix() * 1000);
    list = _.filter(list, o => o.expiry_date_end >= aaa * 1000);

    list = _.sortBy(list, 'past')

    let arrLsit = []
    debugger
    for (var x in list) {
      let obj = {}
      obj.key = list[x]
      obj.value = list[x].coupon_name
      arrLsit.push(obj)
    }
    // this.options1 = arrLsit;
    // arrLsit.length > 0 ? this.voucherPrice = arrLsit[0].key.price : this.voucherPrice =  0
    // arrLsit.length > 0 ? this.valueVoucher = arrLsit[0].value : this.valueVoucher = '暂无优惠券'
    arrLsit.length > 0 ? this.options1 = arrLsit : this.options1 = []

  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  watch:{
        voucherPrice(val, oldVal){
           this.feeTotal = '20'
          let voucherPrice
          if (this.voucherPrice.price) {
            voucherPrice = this.voucherPrice.price
          } else {
            voucherPrice = 0
          }
          let feeTotal = parseInt(this.fee - voucherPrice)
          debugger
          feeTotal > 0 ? this.feeTotal = feeTotal : this.feeTotal = 0
        },
    },
  methods: {
    timestampToTime(timestamp) {
      let date = new Date(timestamp); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
      let Y = date.getFullYear() + '-';
      let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
      let D = date.getDate() + ' ';
      let h = date.getHours() + ':';
      let m = date.getMinutes() + ':';
      let s = date.getSeconds();
      return Y + M + D + h + m + s;
    },
    async wxpayClik() {
      let wx_app_id

      try {
        wx_app_id = (await this.$http.get('/wx/appid')).data.wx_app_id //在线获取
      } catch (e) {}
      this.wxpayTOjava()
      const redirectUri = `http://${location.hostname}/wx/wxpay?fee=` + this.feeTotal * 100
      location.href = `http://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`

      this.mes = (await this.$http.get(`/wx/wxpay?id=` + 1)).data
    },
    async wxpayTOjava() {
      let list = (await this.$http.post(`/api/parkingCoupon/pay/`, {
        'member_id': this.member_id,
        'car_number': this.licenceNum,
        'in_date': this.in_datey,
        'price': this.feeTotal,
        'ticket_no': 123
      })).data

    }
  }
}
</script>

<style scoped>
.demo3-slot {
  text-align: center;
  padding: 8px 0;
  color: #888;
}
</style>
