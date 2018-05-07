<template>
  <scroller lock-x>
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

      <div>
        <div style="margin-top:0.25rem;background:#fff;border-top:1px solid #e1e1e1;font-size:0.4rem;">
          <div style="display:flex;border-bottom:1px solid #e1e1e1;">
            <div style="width:2rem;padding:0.25rem;margin-left:0.25rem;color:#00c9b2;">新手机号</div>
            <div style="flex:1;padding:0.25rem;">
              <input style="font-size:0.4rem;" type="tel" placeholder="请输入新手机号" v-model="mobile" />
            </div>
          </div>

          <div style="display:flex;border-bottom:1px solid #e1e1e1;">
            <div style="width:2rem;padding:0.25rem;margin-left:0.25rem;color:#00c9b2;">验证码</div>
            <div style="flex:1;padding:0.25rem;">
              <input style="font-size:0.4rem;" type="number" placeholder="请输入验证码" v-model="vcode" />
            </div>
            <button :disabled="cd > 0" ref="code" @click="getCode" class="btn-code" style="padding:0.125rem 0.25rem;margin:0.125rem;min-width:2rem;">
              <div style="text-align:center;width:100%;">{{cd > 0 ? `${cd} 秒` : '获取验证码'}}</div>
            </button>
          </div>
        </div>
      </div>

      <countdown v-model="cd" :start="cd>0" v-show="false"></countdown>
      <div @click="handleSubmit" style="font-size:0.4rem;background-color:#00c9b2;color:#fff;padding:0.25rem;text-align:center;margin-top:0.5rem;margin: 0.5rem;border-radius:5px;">提交修改</div>
    </div>
  </scroller>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;

import { TransferDom, Scroller, Countdown } from 'vux'

import _ from 'lodash';
import moment from 'moment';
import {
  mapState,
} from 'vuex';
export default {
  directives: {
    TransferDom
  },
  components: {
    Scroller, Countdown
  },
  data() {
    return {
      mobile: '',
      vcode: '',
      user: {},
      cd: 0,
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods: {
    async getCode(){
      const { mobile } = this;

      const [errno, errmsg] = (!mobile && [1, '请输入新手机号'])
        || (!/1\d{10}/.test(mobile) && [2, '请输入正确的手机号'])
        || [];
      if(errno){
        this.$vux.toast.text(errmsg)
        return
      }

      try{
        await this.$http.post(`/api/member/vcode`,{mallId, mobile})
        this.cd = 30;
      }catch(e){
        this.$vux.toast.text(`该手机号已注册会员`)
      }
    },

    async handleSubmit() {
      const { member_id, mobile, vcode } = this

      const [errno, errmsg] = (!mobile && [1, '请输入手机号'])
                            || (!/1\d{10}/.test(mobile) && [2, '请输入正确的手机号'])
                            || (!vcode && [3, '请输入验证码'])
                            || [];
      if(errno){
        this.$vux.toast.text(errmsg)
        return
      }
      try{
        await this.$http.post(`/api/member/vcodeCheck`,{mobile,vcode})

        await this.$http.put(`/api/member`, {
          memberId: member_id, mobile
        })

        this.$vux.toast.text('手机号修改成功')
        this.$router.back();

      }catch(e){
        if(e.response){
          this.$vux.toast.text(e.response.data.content)
        }else{
          this.$vux.toast.text(e.message)
        }
      }
    },
  },
  async mounted(){
    document.title = '修改手机号'
    let member_id = this.member_id;
    let { data } = await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)
    this.user=data;
  }
}
</script>
<style lang="less" scoped>
input {
  background-color:transparent;
  border-style:none;
  outline:none;
  width: 100%;
}
button.btn-code {
  border: 0;
  background-color:#00c9b2;
  color:#fff;
  font-size:0.4rem;;
  display:flex;
  align-items:center;
  border-radius:5px;
  outline:none;
}
button.btn-code:disabled {
  background-color:#7DD8CF !important;
}
</style>
