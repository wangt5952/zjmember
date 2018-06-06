<template>
    <div>

      <div style="background-color:#fff;margin-top:0.5rem;padding:0.25rem;border-top:1px solid #e1e1e1;">
        <div style="display:flex;flex:1;border:1px solid #804c19;border-radius:10px;box-shadow: 5px 5px 5px #888888;">
          <div style="width:24vw;height:50vw;background-image:url(/static/img/member_card_left.png);background-size:auto 100%;background-repeat:no-repeat;">
          </div>
          <div style="flex:1;display:flex;flex-direction:column;justify-content:center;height:50vw;background-image:url(/static/img/member_card_right.png);background-size:auto 100%;background-repeat:no-repeat;background-position:center right;padding-left:0.5rem;">
            <div style="font-size:0.5rem;">{{user.name}} <span v-if="user.level" style="background-color:#56ded3;color:#fff;padding:2px 5px;font-size:0.9em;border-radius:3px;">{{user.level}}</span></div>
            <div style="font-size:0.4rem;color:#7f8081;">累计积分<span style="margin-left:0.3rem;">{{user.cumulate_points}}</span></div>
            <div style="font-size:0.4rem;color:#7f8081;">可用积分<span style="margin-left:0.3rem;">{{user.usable_points}}</span></div>
          </div>
        </div>
      </div>

      <div style="display:flex;background-color:#fff;font-size:0.4rem;text-align:center;padding:0.25rem 0;border-top:1px solid #e1e1e1;border-bottom:1px solid #e1e1e1;">
        <router-link to="/userInfo" style="flex:1;display:flex;flex-direction:column;align-items:center;">
          <img src="static/img/userinfo.png" style="width:0.7rem;height:0.7rem;"/>
          <div style="margin-top:0.2rem;">个人信息</div>
        </router-link>
        <div style="border-left:1px solid #e1e1e1;"></div>
        <router-link to="/memberLevel" style="flex:1;display:flex;flex-direction:column;align-items:center;">
          <div><img src="static/img/level.png" style="width:0.7rem;height:0.7rem;"/></div>
          <div style="margin-top:0.2rem;">我的等级</div>
        </router-link>
        <div style="border-left:1px solid #e1e1e1;"></div>
        <router-link to="/memActivities" style="flex:1;display:flex;flex-direction:column;align-items:center;">
          <div><img src="static/img/activity.png" style="width:0.7rem;height:0.7rem;"/></div>
          <div style="margin-top:0.2rem;">我的活动</div>
        </router-link>
        <div style="border-left:1px solid #e1e1e1;"></div>
        <router-link to="/memVoucher" style="flex:1;display:flex;flex-direction:column;align-items:center;">
          <div><img src="static/img/certificate.png" style="width:0.7rem;height:0.7rem;"/></div>
          <div style="margin-top:0.2rem;">我的券</div>
        </router-link>
      </div>

      <div style="background-color:#fff;margin-top:0.5rem;font-size:0.4rem;border-top:1px solid #e1e1e1;border-bottom:1px solid #e1e1e1;">
        <router-link to="/voucher" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/voucher.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;align-items:center;padding-right:0.25rem;">
            <div style="flex:1;margin:0.25rem;">优惠券</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link to="/gift_voucher" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/certificate.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:0.25rem;">
            <div style="flex:1;margin:0.25rem;">积分兑礼</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link to="/activities" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/latestactive.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:0.25rem;">
            <div style="flex:1;margin:0.25rem;">最新活动</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link  to="/car" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/stopcar.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:0.25rem;">
            <div style="flex:1;margin:0.25rem;">停车缴费</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link to="/myreward" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/cash.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:0.25rem;">
            <div style="flex:1;margin:0.25rem;">我的奖品</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link to="/ticketsUpload" style="display:flex;">
          <div style="display:flex;align-items:center;">
            <img src="static/img/upload.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:10px;">
            <div style="flex:1;margin:0.25rem;">小票上传</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <router-link to="/suggest_list" style="display:flex;" >
          <div style="display:flex;align-items:center;">
            <img src="static/img/record.png" style="width:0.7rem;height:0.7rem;margin:0 0.5rem;">
          </div>
          <div style="flex:1;display:flex;border-top:1px solid #e1e1e1;align-items:center;padding-right:10px;">
            <div style="flex:1;margin:0.25rem;">投诉建议</div>
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>
      </div>
    </div>
</template>
<script>
import  global from '../../../src/components/common/Global.vue'
import {
  mapState,
} from 'vuex';
export  default {
  components:{
  },
  data() {
    return {
      user: {},
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  async mounted(){
    document.title = '会员中心'
    let member_id = this.member_id;
    try{
      let { data } = await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)
      this.user=data;
    }catch(e){
      await this.$store.commit('logout')
      location.reload();
    }

  }
}
</script>
<style  lang="less" scoped>
</style>
