<template>
  <div>

    <div style="background-color:#fff;margin:10px;">
      <div style="display:flex;align-items:center;">
        <div style="border-left:2px solid #000;height:0.5rem;"> </div>
        <div style="flex:1;margin:5px 10px;font-size:0.4rem;">券详情</div>
      </div>

      <div style="display:flex;">
        <div style="width:2.5rem;height:2.5rem;display:flex;align-items:center;justify-content:center;">
          <img :src="item.picture  || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:2rem;">
        </div>
        <div style="flex:1;display:flex;flex-direction:column;">
          <div style="margin-left:5px;margin-top:10px;font-size:0.4rem;">{{item.coupon_name}}</div>
          <div style="flex:1;display:flex;font-size:0.3rem;margin-left:5px;min-height:55px;">
            <div style="flex:1;display:flex;flex-direction:column;font-size:0.3rem;">
              <div style="flex:1;color:#78797a;border-bottom:1px solid #e1e1e1;display:flex;align-items:center;"><span style="width:45px;display:inline-block;">有效期</span>{{item.expiry_date_start | unix('YYYY-MM-DD', 'ms')}} ~ {{item.expiry_date_end | unix('YYYY-MM-DD', 'ms')}}</div>
              <div style="flex:1;color:#78797a;display:flex;align-items:center;"><span style="width:45px;display:inline-block;">状态</span>{{coupon_status}}</div>
            </div>
          </div>
        </div>
      </div>

      <div style="display:flex;justify-content:center;">
        <vue-qr :dotScale="1" :text="qr" height="200" width="200"></vue-qr>
      </div>
    </div>


    <div v-html="item.intro" style="margin:15px;font-size:0.4rem;color:#707172;"></div>
  </div>
</template>

<script>
import VueQr from 'vue-qr'
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import moment from 'moment';
import {
  mapState,
} from 'vuex';

export default {
  components:{
    VueQr
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  computed: {
    coupon_status(){

      const { past, coupon_status } = this.item;

      if(coupon_status == 2) return '已核销'
      if(past == 1) return '已过期'

      const m = {
        '0': '未激活', '1':'已激活', '2':'已核销'
      }
      return m[coupon_status] || coupon_status;
    },
    qr(){
      const { id } = this.$route.query;
      const { past, coupon_status } = this.item;
      if(coupon_status != 0 || past == 1 )
        return `${location.origin}/gift_verify?id=${id}`

      return `${location.origin}/gift_verify?id=${id}&type=activate`;
    },
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  data(){
    return{
      item: {},
    }
  },
  async mounted(){
    document.title = '积分兑礼详情'
    const { id } = this.$route.query;
    this.item = (await this.$http.get(`/api/giftCoupon/coupon/${id}`)).data
  },
  methods:{

  },
}
</script>

<style lang="less" scoped>

</style>
