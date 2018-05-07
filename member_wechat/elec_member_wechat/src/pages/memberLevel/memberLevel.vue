<template>
  <div>
    <div style="display:flex;background-color:#fff;margin-top:0.5rem;border-top:1px solid #e1e1e1;border-bottom:1px solid #e1e1e1;">
      <div>
        <img src="static/img/member.png" style="border-radius:50%;width:2rem;height:2rem;margin:0.5rem;">
      </div>
      <div>
        <div style="margin-top:0.5rem;font-size:0.5rem;">{{user.name}} <span v-if="user.level" style="background-color:#56ded3;color:#fff;padding:2px 5px;font-size:0.5rem;border-radius:3px;">{{user.level}}</span></div>
        <div style="font-size:0.4rem;color:#7f8081;">累计积分<span style="margin-left:0.25rem;">{{user.cumulate_points}}</span></div>
        <div style="font-size:0.4rem;color:#7f8081;">可用积分<span style="margin-left:0.25rem;">{{user.usable_points}}</span></div>
      </div>
    </div>

    <div style="display:flex;background-color:#fff;text-align:center;font-size:0.4rem;">
      <div style="flex:1;padding:0.25rem 0;border-bottom:3px solid #00c9b2;color:#00c9b2;">积分记录</div>
      <a href="https://mp.weixin.qq.com/s/N40si0A7iUNUXRK85sxjZQ" target="_blank" style="flex:1;padding:0.25rem 0;">会员权益</a>
    </div>

    <div style="background-color:#fff;margin-top:0.5rem;padding:0 0.25rem;">
      <table style="width:100%;table-layout:fixed;font-size:0.4rem;" border="0" cellspacing="0" cellpadding="0">
        <tr style="height:1rem;">
          <td style="border-bottom:1px solid #e1e1e1;padding-left:0.25rem;width:35%;">时间</td>
          <td style="border-bottom:1px solid #e1e1e1;width:20%;">积分</td>
          <td style="border-bottom:1px solid #e1e1e1;text-align:center;">商户</td>
        </tr>
        <tbody>
          <template v-for="o in list">
            <tr @click="detailId=((detailId==o.mplog_id) ? null : o.mplog_id)" :class="{active:detailId==o.mplog_id}" style="height:1rem;" >
              <td style="padding-left:0.25rem;">{{o.shopping_date | unix('YYYY-MM-DD', 'ms')}}</td>
              <td style="color:#00c9b2;">{{o.points}}</td>
              <td style="white-space:nowrap;overflow:hidden;word-break:keep-all;text-overflow:ellipsis;">{{o.shop_name}}</td>
            </tr>
            <tr v-if="detailId==o.mplog_id" :class="{active:detailId==o.mplog_id}" >
              <td colspan="3" style="padding-left:0.25rem;color:#7f8081;font-size:0.4rem;">
                <template v-if="o.points>0">
                  <p>消费时间: {{o.shopping_date | unix('YYYY-MM-DD', 'ms')}}</p>
                  <p>消费商户: {{o.shop_name}}</p>
                  <p>消费金额: {{o.amount}}</p>
                  <p>获得积分: {{o.points}}</p>
                </template>
                <template v-else>
                  <p>抵扣时间: {{o.shopping_date | unix('YYYY-MM-DD', 'ms')}}</p>
                  <p>抵扣商户: {{o.shop_name}}</p>
                  <p>抵扣金额: {{o.amount}}</p>
                  <p>扣减积分: {{o.points}}</p>
                </template>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
   </div>
</template>
<script>
import global from '../../../src/components/common/Global'
import moment from 'moment'
import {
  mapState,
} from 'vuex';
export default {
  components:{
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  filters:{
    time(value){
      let date =  new Date(value);
      let y = 1900+date.getYear();
      let m = "0"+(date.getMonth()+1);
      let d = "0"+date.getDate();
      let H = date.getHours();
      let minu= date.getMinutes();
      return y+"/"+m.substring(m.length-2,m.length)+"/"+d.substring(d.length-2,d.length)+' '+H+':'+minu;
    },
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  data(){
   /* this.$http.get('http://192.168.1.160/test').then(resp=>{
        console.log(resp);}*/
    return{
      user: {},
      list: [],
      detailId: null
    }
  },
  async mounted(){
    document.title = '我的等级'
    let member_id = this.member_id;
    this.user = (await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)).data

    this.list = (await this.$http.post(`/api/member/${this.member_id}/pointsList`, {
      mallId: global.mallId, page: 1, size: 200
    })).data
  },
  methods:{
  },
}
</script>
<style lang="less" scoped>
tr.active {
  background-color: #fafbfc;
}
</style>
