<template>
   <div>
     <scroller lock-x ref="scroller">
       <div style="margin-top:2.5rem;">
         <router-link :to="{path:'/sell',query:{sell_id:o.shop_id}}" style="display:flex;background-color:#fff;margin-top:1px;" key="shop_id" v-for="o in list">
           <div style="width:2.2rem;height:2rem;display:flex;align-items:center;justify-content:center;">
             <img :src="o.logo || '/static/img/default-icon.jpg'" style="max-width:2rem;max-height:1.8rem;">
           </div>
           <div style="flex:1;margin-right:0.25rem;padding:0.2rem;">
             <div style="display:flex;font-size:0.4rem;margin-left:0.2rem;">
               <div style="flex:1">{{o.shop_name}}</div>
               <!-- <div style="color:#78797a;"><span class="iconfont icon-favorfill" style="color:#ffd656;margin-right:5px;"></span>{{o.points}}</div> -->
             </div>
             <div style="display:flex;font-size:0.3rem;margin-left:0.2rem;">
               <div style="flex:1;">
                 <div style="color:#ff4352;"><span class="iconfont icon-location"></span>{{o.berth_number}}</div>
                 <div style="color:#78797a;">{{o.industry_name}}</div>
               </div>
             </div>
           </div>
         </router-link>

         <p v-if="loading" style="text-align:center;">
           <inline-loading></inline-loading><span style="vertical-align:middle;display:inline-block;font-size:0.4rem;">&nbsp;&nbsp;商户接入中...</span>
         </p>
         <div v-else style="text-align:center;color:#7D7D7D;padding-top:1rem;padding-bottom:3rem;">更多商家接入中，敬请期待...</div>
       </div>
     </scroller>

     <div style="position:fixed;top:0;right:0;left:0;">
       <div>
         <search v-model="searchParams.keywords" @on-change="reload" @on-submit="reload" placeholder="请输入关键词，例如：肯德基" position="static"></search>
       </div>

       <div style="display:flex;text-align:center;background-color:#fff;padding:0.25rem 0;border-bottom:1px solid #e1e1e1;font-size:0.4rem;">
         <div @click="openPopup('map_id')" style="flex:1">{{popupName('map_id')}}</div>
         <div style="border-left:1px solid #e1e1e1;"></div>
         <div @click="openPopup('industry_id')" style="flex:1">{{popupName('industry_id')}}</div>
         <div style="border-left:1px solid #e1e1e1;"></div>
         <div @click="openPopup('sort')" style="flex:1">{{popupName('sort')}}</div>
       </div>
     </div>

     <div v-transfer-dom>
       <popup v-model="popup" position="top" style="margin-top:2rem;background-color:#fff;font-size:0.4rem;">
         <div @click="choosePopup(o.id)" v-for="o in popupList[popupKey]" style="width:50%;display:inline-block;border-top:1px solid #e1e1e1;">
           <div style="display:flex;">
             <div style="flex:1;padding:0.25rem;">
               {{o.name}}
             </div>
             <div style="display:flex;align-items: center;margin-right:0.25rem;">
               <span v-show="(popupSelected instanceof Array) ? ~popupSelected.indexOf(o.id) :o.id==popupSelected" class="iconfont icon-check" style="color:#797979;"></span>
             </div>
           </div>
         </div>
       </popup>
     </div>
   </div>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
import { Search, TransferDom, Popup, Datetime, Scroller, InlineLoading } from 'vux'
import _ from 'lodash';
export default {
  directives: {
    TransferDom
  },
  components: {
    Popup, Scroller, Search, InlineLoading
  },
  data(){
    return {
      loading: false,
      list: [],
      popup: false,
      popupTitle: '',
      popupKey: null,
      popupSelected: null,
      popupList: {
        map_id: [
          {id:undefined, name:'全部', selectedName:'楼层'},
        ],
        industry_id: [
          {id:undefined, name:'全部', selectedName:'分类'},
        ],
        sort: [
          {id:0, name:'默认', selectedName:'排序'},
          {id:1, name:'积分热度'},
          {id:2, name:'首拼'}
        ],
      },
      searchParams: {
        map_id: undefined,
        industry_id: undefined,
        sort: 0,
        keywords: ''
      },
    }
  },
  async mounted(){
    document.title = '商户详情'
    await this.reload();

    this.popupList.map_id = [
      {id:undefined, name:'全部', selectedName:'楼层'},
      ..._.map((await this.$http.get(`/api/mall/${global.mallId}/planeMaps`)).data, o=>({
        id: o.map_id,
        name: o.map_name
      }))
    ]

    this.popupList.industry_id = [
      {id:undefined, name:'全部', selectedName:'分类'},
      ..._.map((await this.$http.get(`/api/mall/${global.mallId}/industries`)).data, o=>({
        id: o.industry_id,
        name: o.industry_name
      }))
    ]

    return;
  },
  methods:{
    popupName(key){
      const id = this.searchParams[key];
      if(id instanceof Array){
        const list = _.map(_.filter(this.popupList[key], o=>~id.indexOf(o.id)), 'name')
        return list.join(',')
      }else{
        const item = _.find(this.popupList[key], {id}) || {}
        return item.selectedName || item.name
      }
    },
    openPopup(key) {
      this.popupSelected = this.searchParams[key];
      this.popupKey = key;
      this.popup = true;
    },
    closePopup() {
      this.popup = false;
    },
    async choosePopup(id) {

      this.searchParams[this.popupKey] = id;
      this.popup = false;
      await this.reload()
    },

    async reload() {
      this.loading = true;
      this.list = []
      try{
        let list = (await this.$http.post(`/api/shop`, {
          mall_id: global.mallId,
          page:1,
          size:200,
          ...this.searchParams,
        })).data
        list = _.map(list, o=>({
          ...o,
          logo: o.logo ? o.logo.replace(/^http:\/\/.+?\//,'/') : ''
        }))
        this.list = list;
      }catch(e){
      }
      this.loading = false;

      this.$nextTick(() => {
        this.$refs.scroller.reset({
          top: 0
        })
      })

    },
  }
}
</script>
<style lang="less" scoped>
</style>
