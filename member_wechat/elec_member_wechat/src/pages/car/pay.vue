<template>
<button type="button" class="btn" onclick="goPay('<%= appId %>','<%= timeStamp %>', '<%= nonceStr %>', '<%= package %>', '<%= signType %>', '<%= paySign %>');">微信支付</button>
</template>
<script>


export default {
  components: {
  },
  data() {
    return {
    }
  },
  methods: {
     goPay(appId, timeStamp, nonceStr, packages, signType, paySign) {
         WeixinJSBridge.invoke('getBrandWCPayRequest', {
             "appId" : appId,
             "timeStamp":timeStamp,
             "nonceStr" : nonceStr,
             "package" : packages,
             "signType" : signType,
             "paySign" : paySign
         }, function(res){
             if(res.err_msg == "get_brand_wcpay_request:ok"){
                 alert("支付成功");

             }else{
                 alert("支付失败，请重试");
             }
         });
     }
  }
}
</script>

<style lang="less" scoped>
.char-line {
    display: flex;
    justify-content: space-around;
}
.char-line > div {
    width: 1rem;
    font-size: 0.5rem;
    padding: 0.3rem;
    text-align: center;
}
</style>
