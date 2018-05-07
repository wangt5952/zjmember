<template>

    <div>
      <img :src="item.picture" style="height:250px;width:100%;">
      <h1 style="font-size:0.2rem;text-align:center;margin: 10px 0;">{{item.title}}</h1>

      <div style="background-color:#FFFFFF;margin-left:0.1rem;margin-right:0.1rem;padding: 5px 15px;">
          <div class="bbody" style="width:100%">
          <div style="width: 6px; height: 17px; position: relative; margin-left: -4%; top: 0.09rem;">
            <img src="static/img/logo002.png" alt="" style="width:4.5px;height:15px;">
          </div>
              <div style="font-size: 0.2rem;color: black;margin-top: -0.17rem;">活动详情</div>
          </div>
          <div class="bbody" style="border-bottom:1px solid #E3E3E3;color:#767676;padding: 5px;">
              活动时间<label style="padding-left:15px;">{{item.activity_time_start | unix('YYYY-MM-DD', 'ms')}} ~ {{item.activity_time_end | unix('YYYY-MM-DD', 'ms')}}</label>
          </div>
          <div class="bbody" style="border-bottom:1px solid #E3E3E3;color:#767676;padding: 5px;">
              活动地点<label style="padding-left:15px;">{{item.address}}</label>
          </div>
          <div class="bbody" style="border-bottom:1px solid #E3E3E3;color:#767676;padding: 5px;">
              剩余人数<label style="padding-left: 16px;color: rgb(255, 136, 23);">{{item.sign_up_residue}}</label>
          </div>
          <div class="bbody" style="border-bottom:1px solid #E3E3E3;color:#767676;padding: 5px;">
              报名条件<label style="padding-left: 16px;color: rgb(255, 136, 23);">{{item.sign_up_points}}积分</label>
          </div>
          <div class="bbody" style="border-bottom: 1px ; color: rgb(118, 118, 118); padding: 5px;">
              活动奖励<label style="padding-left: 16px;">签到获得{{item.incentive_points}} 积分</label>
          </div>
      </div>

      <div v-html="item.intro" style="color:#777879;font-size:0.8em;padding:15px;line-height:2em;">
      </div>

      <div :class="{active:enableSignInCode==8}" @click="enableSignInCode==8 && signin()" style="position:fixed;bottom:0;left:0;right:0;background-color:#939393;color:#fff;text-align:center;padding:15px 0;">
        {{enableSignIn}}
      </div>
    </div>
</template>

<script>
import { Scroller } from 'vux'
import global from '@/components/common/Global.vue'
const { mallId } = global;
import moment from 'moment'
import {
  mapState,
} from 'vuex';

export default {
  components:{
    Scroller
  },
  data(){
    return{
      item: {},
      enableSignInCode: 0,
      enableSignInMap: {
        '0': '会员不存在',
        '1': '活动不存在',
        '2': '未报名',
        '3': '已签到',
        '8': '签到'
      },
      signInResultMap: {
        '0': '会员不存在',
        '1': '活动不存在',
        '2': '未报名',
        '3': '重复签到',
        '4': '签到失败',
        '5': '签到成功', // 没有奖励
        '6': '签到成功', // 送券
        '7': '签到成功', // 送积分
      }
    }
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  computed: {
    enableSignIn(){
      return this.enableSignInMap[this.enableSignInCode] || this.enableSignInCode
    },
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods:{
    async signin() {
      const { activityId } = this.$route.query;

      let data = {}
      try {
        data = (await this.$http.put(`/api/member/${this.member_id}/activitySignIn/${activityId}?mallId=${mallId}`)).data
      }catch (e){
        if(e.response){
          data = e.response.data
        }
      }

      this.$vux.toast.text(this.signInResultMap[data.content] || data.content)
      this.$router.back()
    }

  },
  async mounted(){
    document.title = '活动签到'
    const { activityId } = this.$route.query;
    try {
      this.enableSignInCode = (await this.$http.get(`/api/member/${this.member_id}/enableSignIn/${activityId}?mallId=${mallId}`)).data
    }catch (e){
      if(e.response){
        this.enableSignInCode = e.response.data
      }
    }
    console.log(this.enableSignInCode)
    this.item = (await this.$http.get(`/api/activity/${activityId}?memberId=${this.member_id}`)).data
  }
}
</script>

<style lang="less" scoped>
.active {
  background-color: #00d6c6 !important;
}
</style>
