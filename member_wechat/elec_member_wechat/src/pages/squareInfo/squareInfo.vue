<template>
  <scroller lock-x>
    <div>
      <div class="test" style="height:200px;width:100%;background-color:#ddd;">
        <mt-swipe :auto="4000">
          <mt-swipe-item v-for="v in item.pictures" :key="v.id">
            <img :src="v.mapUrl" width="auto" height="100%" style="margin: 0 auto;">
          </mt-swipe-item>
        </mt-swipe>
      </div>

      <div style="background-color:#fff;">
        <div style="color:#13c2a1;font-size:1.2em;border-bottom:1px solid #e1e1e1;padding:10px;">{{item.mall_name}}</div>
        <div v-html="item.intro" style="border-bottom:1px solid #e1e1e1;padding:10px;"></div>

        <div style="padding:10px;font-size:0.9em;color:#797979;">
          <div style="display:flex;">
            <img src="static/img/address.png" style="width:20px;height:20px;">
            <div style="margin-left:10px;">地　　址：{{item.address}}
              <router-link :to="{ path: '/getPosition', query: { longitude: item.longitude,latitude:item.latitude }}">
                <img style="width:20px;height:20px;display:inline;"  src="static/img/gdmap.png" alt="">
              </router-link>
            </div>

          </div>
          <div style="display:flex;margin-top:10px;">
            <img src="static/img/optime.png" style="width:20px;height:20px;">
            <div style="margin-left:10px;">营业时间：{{item.business_hours}}</div>
          </div>
          <div style="display:flex;margin-top:10px;">
            <img src="static/img/phone.png" style="width:20px;height:20px;">
            <div style="margin-left:10px;">联系电话：<a :href="`tel:${item.mall_phone}`" style="color:rgba(6,193,174,50);">{{item.mall_phone}}</a></div>
          </div>
        </div>
      </div>
    </div>
  </scroller>
</template>
<script>
import global from '../../../src/components/common/Global.vue'
const { mallId, appId } = global;

import { Scroller } from 'vux'
export default {
  components: {
    Scroller
  },
  data(){
    return {
      item: {},
    }
  },
  async mounted(){
    let { data } = await this.$http.get(`/api/mall/${appId}`)
    data.pictures = data.pictures ? JSON.parse(data.pictures) : []
    this.item = data;
  },
  methods:{
  }
}
</script>
<style lang="less" scoped>

</style>
