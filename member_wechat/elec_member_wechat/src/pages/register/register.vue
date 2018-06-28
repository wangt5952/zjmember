<template>
  <div v-show="showLoginForm">

    <div v-if="closeUse">
      <div style="font-size:1.2em;text-align:center;color:red;margin-top:20px;">请使用微信扫一扫</div>
    </div>
    <div v-else>
      <div style="margin-top:10px;background:#fff;border-top:1px solid #e1e1e1;font-size:0.4rem;">
        <div style="display:flex;border-bottom:1px solid #e1e1e1;align-items: center;">
          <div style="width:2rem;margin-left:10px;color:#00c9b2;text-align:center;">手机号</div>
          <div style="flex:1;padding:0.25rem;">
            <input style="font-size:0.4rem;" type="tel" placeholder="请输入手机号" v-model="mobile" />
          </div>
        </div>

        <div style="display:flex;border-bottom:1px solid #e1e1e1;align-items: center;">
          <div style="width:2rem;margin-left:10px;color:#00c9b2;text-align:center;">验证码</div>
          <div style="flex:1;padding:10px;">
            <input style="font-size:0.4rem;" type="number" placeholder="请输入验证码" v-model="vcode" />
          </div>
          <button :disabled="cd > 0" ref="code" @click="getCode" class="btn-code" style="padding:0.1rem 0.2rem;margin:5px;min-width:2rem;">
            <div style="text-align:center;width:100%;font-size:0.3rem;">{{cd > 0 ? `${cd} 秒` : '获取验证码'}}</div>
          </button>
        </div>
      </div>
      <countdown v-model="cd" :start="cd>0" v-show="false"></countdown>
      <div @click="handleSubmit" style="font-size:0.4rem;background-color:#00c9b2;color:#fff;padding:10px;text-align:center;margin-top:20px;margin: 20px;border-radius:5px;">下一步</div>

    </div>

  </div>
</template>

<script>
import { Countdown } from 'vux'
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import UAParser from 'ua-parser-js'
export default {
  components:{
    Countdown,
  },
  data(){
    return {
      showLoginForm: false,
      mobile: '',
      member_id: '',
      vcode: '',
      cd: 0,
      closeUse: false
    }
  },
  methods: {
    async getCode(){
      const { mobile } = this;

      const [errno, errmsg] = (!mobile && [1, '请输入手机号'])
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
        this.$vux.toast.text('该手机号已注册')
        this.cd = 30;
        this.member_id = (await this.$http.get(`/api/member/m?mobile=${mobile}`)).data.member_id
        this.vcode = '888888'
        this.$store.commit('login', member_id)
      }
    },

    async handleSubmit() {
      const { member_id, mobile, vcode } = this
      const { wx_openid } = this.$route.query
      const { redirect } = this.$route.query
      if(member_id){
        this.$store.commit('login', {member_id})
        this.$router.push(redirect || '/member');
        return
      }

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
        this.$router.push(`/registerInfo?mobile=${mobile}&openId=${wx_openid}`)
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
    // localStorage.setItem('member_id', '14811');
    document.title = '会员注册'
    let ua = new UAParser().getResult()
    let { wx_openid , code} = this.$route.query
    if (ua.browser.name === 'WeChat') {

      let wx_app_id
      try{
        wx_app_id = (await this.$http.get('/wx/appid')).data.wx_app_id//在线获取
      }catch(e){

      }

      if(!wx_openid){
        const { redirect } = this.$route.query
        if(redirect){
          localStorage.setItem('redirect', redirect)
        }
        const redirectUri =`http://${location.hostname}/wx/code2openid`
        location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`

        return;
      }else{


        try{
          let { member_id } = (await this.$http.get(`/api/member?appId=${wx_app_id}&openId=${wx_openid}`)).data
          this.$store.commit('login', {member_id})
          const redirect = localStorage.getItem('redirect')
          localStorage.removeItem('redirect');
          this.$router.push(redirect || '/member');
        }catch(e){
          this.showLoginForm = true
        }
      }
    }else{
      this.showLoginForm = true
      this.closeUse = true
    }
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
  font-size:0.8em;
  display:flex;
  align-items:center;
  border-radius:5px;
  outline:none;
}
button.btn-code:disabled {
  background-color:#7DD8CF !important;
}
</style>
