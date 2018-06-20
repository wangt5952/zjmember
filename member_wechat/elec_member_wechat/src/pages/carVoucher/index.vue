<template>
  <div>
    <scroller lock-x @on-scroll="handleScroll">
      <div>
        <div style="height:5rem;background-image:url(static/img/quan.jpg);background-repeat:no-repeat;background-size: auto 100%;background-position:center;background-color:#64d9a6;position:relative;font-size:0.4rem;">
          <router-link :to="{ path: member_id ? '/memCarVoucher':'/register' }" style="position:absolute;bottom:0;right:0;padding:0.25rem;color:#ffed00;">
            {{member_id?'我的停车券':'立即注册 》'}}
          </router-link>
        </div>
        <div style="margin-top:0.5rem;">
          <router-link :to="{path:'/carVoucherDetail', query:{id:o.coupon_id}}" style="display:flex;background-color:#fff;margin-top:1px;" key="coupon_id" v-for="o in list">
            <div style="width:2.5rem;height:2.5rem;display:flex;align-items:center;justify-content:center;">
              <img :src="o.picture || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:2rem;">
            </div>
            <div style="flex:1;">
              <div style="font-size:0.4rem;margin-left:0.125rem;margin-top:0.25rem;">{{o.coupon_name}}</div>
              <div style="display:flex;font-size:0.3rem;margin-left:0.125rem;" v-if="o.receive_method==0">
                <div style="flex:1;">
                  <div style="color:#78797a;">市场价：{{o.price}} 元</div>
                  <div style="color:#00cfaf;">兑换积分：{{o.required_points}}</div>
                </div>
                <div style="display:flex;align-items:center;margin-right:0.375rem;">
                  <div style="border:1px solid #ff9100;color:#ff9100;border-radius:3px;padding:0.06rem 0.125rem;">
                    积分兑换
                  </div>
                </div>
              </div>
              <div style="display:flex;font-size:0.3rem;margin-left:0.125rem;" v-else-if="o.receive_method==2">
                <div style="flex:1;">
                  <div style="color:#78797a;">市场价：{{o.price}} 元</div>
                  <div style="color:#00cfaf;"></div>
                </div>
                <div style="display:flex;align-items:center;margin-right:0.375rem;">
                  <div style="border:1px solid #ff9100;color:#ff9100;border-radius:3px;padding:2px 0.125rem;">
                    免费领取
                  </div>
                </div>
              </div>
            </div>
          </router-link>
        </div>

        <div v-transfer-dom>
          <popup v-model="popup" position="top" :popup-style="{backgroundColor:'#fff', marginTop:'60px'}" :is-transparent="true">
            <div class="type-list">
              <div v-for="o in typeList" @click="changeType(o.id)" :class="{'active': o.id==type }" style="display:flex;align-items:center;height:40px;border-bottom:1px solid #ddd;padding-left:0.5rem;">{{o.name}}</div>
            </div>
          </popup>
        </div>
      </div>
    </scroller>
    <div :style="{backgroundColor:scrollTop>140?'#00bf8d':'transparent'}" style="position:fixed;top:0;left:0;right:0;display:flex;height:30px;padding-top:0.375rem;padding-right:0.375rem;padding-bottom:0.375rem;">
      <div style="font-size:0.4rem;width:100px;padding:0.125rem 0;text-align:center;line-height:0.375rem;color:#fff;" @click="popup=true">{{typeName}} <span class="iconfont icon-unfold"></span></div>
      <div style="display:flex;flex:1;background-color:#fff;border-radius:0.375rem;align-items:center;">
        <input v-model="keywords" style="font-size:0.4rem;background-color:transparent;border-style:none;outline:none;margin-left:0.375rem;flex:1;" type="text" placeholder="请输入搜索关键字">
        <div style="width:0.5rem;height:0.5rem;margin-right:0.375rem;line-height:0.5rem;">
          <span class="iconfont icon-search" style="color:#797979;"></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { TransferDom, Popup, Scroller } from 'vux'
import _ from 'lodash'
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;

import {
  mapState,
} from 'vuex';

export  default {
  directives: {
    TransferDom
  },
  components:{
    Popup, Scroller,
  },
  data(){
    return{
      keywords: '',
      scrollTop: 0,
      popup: false,
      list: [],
      type: undefined,
      typeList: [{id: undefined, name:'全部'}, {id:'0', name:'积分兑换'}, {id:'2', name:'免费领取'}],
    }
  },
   mounted(){
    const { type } = this.$route.query;
    this.type = type;
    document.title = '优惠券'

    this.reload()
  },
  computed: {
    typeName() {
      return (_.find(this.typeList, {id: this.type}) || {}).name;
    },
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods:{
    handleScroll({top}) {
      this.scrollTop = top;
    },
    changeType(id){
      this.type=id;
      this.popup=false;
      this.reload();
    },
    async reload(){
      const receiveMethod = this.type;
      const keywords = this.keywords
      try{
        this.list = (await this.$http.post(`/api/parkingCoupon/list`, {
          mallId, page:1, receiveMethod, size:200, keywords, memberId: parseInt(this.member_id)
        })).data

      }catch(e){
        this.list = [];
      }
    }
  },
  watch: {
    keywords(curVal, oldVal){
      this.reload()
    }
  }
}
</script>

<style lang="less" scoped>
.type-list div.active {
  color: #ff9a00;
}
</style>
