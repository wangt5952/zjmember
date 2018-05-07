<template>
  <div>
    <div style="text-align:center;margin:20px 0;font-size:0.4rem;">{{item.coupon_name}}</div>
    <div style="border:1px solid #ddd;background-color:#fff;margin:0 10px;">
      <div style="display:flex;align-items:center;">
        <div style="border-left:2px solid #000;height:0.5rem;"> </div>
        <div style="flex:1;margin:5px 10px;font-size:0.4rem;">券详情</div>
      </div>

      <div style="display:flex;">
        <div style="width:2.5rem;height:2.5rem;display:flex;align-items:center;justify-content:center;">
          <img :src="item.picture || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:2rem;">
        </div>
        <div style="display:flex;color:#777879;font-size:0.35rem;flex:1;">
          <div style="display:flex;flex-direction:column;">
            <div style="flex:1;border-bottom:1px solid #ddd;display:flex;align-items:center;">活动时间</div>
            <!-- <div style="flex:1;border-bottom:1px solid #ddd;display:flex;align-items:center;">已领取</div> -->
            <div style="flex:1;display:flex;align-items:center;">领取条件</div>
          </div>
          <div style="flex:1;display:flex;flex-direction:column;">
            <div style="flex:1;border-bottom:1px solid #ddd;display:flex;align-items:center;padding-left:0.5em;">{{item.expiry_date_start | unix('YYYY-MM-DD', 'ms')}} ~ {{item.expiry_date_end | unix('YYYY-MM-DD', 'ms')}}</div>
            <!-- <div style="flex:1;border-bottom:1px solid #ddd;display:flex;align-items:center;padding-left:0.5em;color:#ff8b00;">{{item.receivedTotal}}</div> -->
            <div style="flex:1;display:flex;align-items:center;padding-left:0.5em;color:#ff8b00;"> {{item.required_points}} 积分</div>
          </div>
        </div>

      </div>
    </div>

    <div v-html="item.intro" style="color:#777879;font-size:0.4rem;padding:15px;line-height:2em;">
    </div>

    <div :class="{active:item.limitPromptCode==6}" @click="item.limitPromptCode==6 && receiveCoupon()" style="font-size:0.4rem;position:fixed;bottom:0;left:0;right:0;background-color:#939393;color:#fff;text-align:center;padding:0.2rem 0;">
      {{limitPromptCode}}
    </div>
  </div>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import moment from 'moment'
import {
  mapState,
} from 'vuex';

export default {
  components:{
  },
  data(){
    return{
      item: {},
      limitPromptCodeMap: {
        '0': '已经终止发行',
        '1': '库存不足',
        '2': '当天已不能领取',
        '3': '已领取券组中的其它券',
        '4': '积分不足',
        '5': '已领取',
        '6': '领取'
      }
    }
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  computed: {
    limitPromptCode(){
      return this.limitPromptCodeMap[this.item.limitPromptCode] || this.item.limitPromptCode
    },
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods:{
    async receiveCoupon() {
      const { id } = this.$route.query;
      const { coupon_type: couponType, receive_method: receivedMethod, required_points: requiredPoints} = this.item;

      const doReceive = async () => {
        const { data } = await this.$http.get(`/api/member/${this.member_id}/receiveCoupon/${id}`)

        const resultMap = {
          '7': '领取失败',
          '8': '领取成功'
        }
        this.$vux.toast.text(resultMap[data] || data)
        if(data == 8){
          this.$router.back()
        }
      }

      let needConfirm = this.item.required_points > 0;
      if(needConfirm){
        this.$vux.confirm.show({
          content: `确认使用${this.item.required_points}积分领取吗？`,
          confirmText: '确定领取',
          cancelText: '暂不领取',
          onConfirm: doReceive
        })
      }else{
        doReceive()
      }



    }
  },
  async mounted() {
    document.title = '优惠券详情'
    const { id } = this.$route.query;

    this.item = (await this.$http.get(`/api/coupon/${id}?memberId=${this.member_id}`)).data
  }
}
</script>

<style lang="less" scoped>
.active {
  background-color: #00d6c6 !important;
}
</style>
