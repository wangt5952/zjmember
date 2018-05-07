<template>
  <div>
    <scroller lock-x>
      <div>
        <template v-for="(o, idx) in list">
          <router-link :to="`/memVouDetail?id=${o.reward_value}`" v-if="o.reward_type === 0" :key="idx" style="display:flex;background-color:#fff;margin-top:1px;">
            <div style="width:2.5rem;height:2.5rem;display:flex;align-items:center;justify-content:center;">
              <img :src="o.reward_picture  || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:2rem;">
            </div>
            <div style="flex:1;">
              <div style="font-size:1em;margin-left:5px;margin-top:10px;">{{o.coupon_name}}</div>
              <div style="display:flex;font-size:0.9em;margin-left:5px;">
                <div style="flex:1;">
                  <div style="color:#78797a;">活动时间：{{o.action_time | unix('YYYY-MM-DD', 'ms')}}</div>
                </div>
              </div>
            </div>
          </router-link>
          <div v-else :key="idx" style="display:flex;background-color:#fff;margin-top:1px;">
            <div style="width:2.5rem;height:2.5rem;display:flex;align-items:center;justify-content:center;">
              <img :src="o.reward_picture  || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:2rem;">
            </div>
            <div style="flex:1;">
              <div style="font-size:1em;margin-left:5px;margin-top:10px;">{{o.coupon_name}}</div>
              <div style="display:flex;font-size:0.9em;margin-left:5px;">
                <div style="flex:1;">
                  <div style="color:#78797a;">活动时间：{{o.action_time | unix('YYYY-MM-DD', 'ms')}}</div>
                </div>
              </div>
            </div>
          </div>

        </template>

        <div style="padding-bottom:100px;"></div>
      </div>
    </scroller>
  </div>
</template>
<script>

import { Scroller } from 'vux'
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import moment from 'moment';
import {
  mapState,
} from 'vuex';
import _ from 'lodash'
export default {
  components:{
    Scroller,
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  data(){
    return{
      list: [],
    }
  },
  async mounted(){
    document.title = '我的奖品'

    try{
      let list = (await this.$http.post('/api/raffle/myReward', {
        memberId: this.member_id, page:1, size:200
      })).data
      this.list = list;
    }catch(e){
      this.list = [];
    }
  },
}
</script>
<style lang="less" scoped>
.mint-navbar .mint-tab-item.is-selected{
  color: #07C0AE;
  margin-bottom: 0;
  border-bottom: 3px solid #07C0AE;
}

.gray {
  -webkit-filter:grayscale() opacity(0.5);
}
</style>
