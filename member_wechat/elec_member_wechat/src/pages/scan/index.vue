<template>
<div>
  <h1></h1>
</div>
</template>

<script>
import {
  mapState,
} from 'vuex';
import {
  Indicator,
  MessageBox
} from 'mint-ui';
export default {
  data() {
    return {
      jifen: '0'
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods: {

  },
  async mounted() {
    Indicator.open({
      text: '积分中...',
      spinnerType: 'fading-circle'
    });
    localStorage.setItem('redirect', 'scan')
    if (!this.member_id || this.member_id == null) {
      debugger
      let wx_app_id = (await this.$http.get('/wx/appid')).data.wx_app_id //在线获取
      const redirectUri = `http://${location.hostname}/wx/code2openid`
      location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`
    } else {
      let {
        qrCode
      } = this.$route.query
      if (qrCode) {
        let data = await this.$http.post(`/api/gag/uploadBill/${this.member_id}?qrCode=${qrCode}`)
        debugger
        if (data.data > 0) {
          MessageBox('兑换成功', '');
            this.$router.push('/memberLevel');
        } else if (data.data = -1) {
          MessageBox('兑换失败', '失败原因：已兑换！');
            this.$router.push('/member');
        }else  if (data.data = 0) {
          MessageBox('兑换失败', '失败原因：输入信息有错误！');
              this.$router.push('/member');
        }
       Indicator.close();
      } else {
        location.href = `http://jiayuanmember.dorm9tech.com/scan?qrCode=http://f.test.goago.cn/001/B107GGGGHkiikGkjklstljljsikqhllql`
        return;
      }

    }

  }
}
</script>
