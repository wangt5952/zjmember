<template>
<div>
  <div style="background-color:white;padding:0.5rem;padding-left:0.2rem;display;flex">
    <div style="font-size:0.5rem;">
      你的车牌<label style="color:#A8A8A8;padding-left:0.2rem"><span v-for='item of licenceNum'>{{item}}</span></label>
    </div>
  </div>
  <div style="width:100%">
    <table style="margin-top:0.5rem;margin-left:0.2rem;width:90%">
      <tr>
        <td style="align:center;font-size:0.8rem;color:#00C4AB;padding-right:0.1rem">
          ¥20.00
        </td>
        <td style="color:#B7B7B7">
          入场时间:09月09日 12:30<br/>
          <br /> 已停时长：2小时
          <br />
          <br /> 停车位置：B区108
        </td>
      </tr>
    </table>
  </div>
  <div style="background-color:white;height:0.5rem;margin-top:0.3rem;font-size:0.2rem">
    <div style="padding-left:0.04rem;padding-right:0.1rem">
      <group>
        <popup-radio title="优惠券" :options="options1" v-model="option1" placeholder="请选择优惠券"></popup-radio>
      </group>
    </div>
  </div>
  <div style="background-color:white;height:0.5rem;font-size:0.2rem;">
    <div style="padding-left:0.2rem;padding-right:0.1rem">积分
      <input type="text" style="float:right;height:100%;border: medium;text-align:right;height:0.42rem;width:73%;padding-right:0.15rem" placeholder="请输入可用积分">
    </div>


  </div>
  <div style="margin-bottom:0.8rem;padding-right:10%">
    <div style="color:#F79E07;padding-top:0.3rem;padding-left:0.2rem;">温馨提醒</div>
    <div style="color:#979797;padding-top:0.3rem;padding-left:0.2rem;">1 积分抵现金比例： 100积分=1元人民币；</div>
    <div style="color:#979797;padding-top:0.3rem;padding-left:0.2rem;">2 积分抵现条件： 100积分起抵，100积分一档。即：只能100，200,300...依此类推</div>
  </div>
  <router-link to="/carDetail">
    <div style="position:absolute;right:0;left:0;bottom:0;padding-top:0.2rem;display:flex;position:fixed;right:0;left:0;bottom:0;">

      <div style="width:70%;height:100%;font-size:0.5rem;text-align:center;background-color:#E6E6E6">
        <div style="padding:0.3rem;">
          还需支付
          <label style="color:#00C4AB;">¥20.00</label>
        </div>
      </div>
      <div style="color:white;font-size:0.5rem;background-color:#46D0C3;width:30%;height:100%;">
        <div style="padding:0.3rem;" @click="wxpayClik">立即支付</div>
      </div>
    </div>
  </router-link>
</div>
</template>
<script>
import {
  Group,
  PopupRadio
} from 'vux'


export default {
  components: {
    Group,
    PopupRadio
  },
  data() {
    return {
      options1: ['A', 'B', 'C'],
      licenceNum: [],
      wx_openid: '',
      access_token: '',
      config: {
        wxappid: 'wx3041b222eaad5c8a',
        wxappsecret: 'fda2fbf4fdec7a43b178f12d4bf36414',
        wxpaykey: 'x0jhJ4r5NMAKei7ktvysNhYxjFsV63uR',
        mch_id: '1505462971'
      }
    }
  },
  mounted() {
    let arr = this.$route.query.car_number
    this.licenceNum = arr

    let {
      wx_openid,
      access_token
    } = this.$route.query

    if (wx_openid) {
      this.wx_openid = wx_openid
      this.access_token = access_token
      let obj = {
        notify_url: 'http://demo.com/', //微信支付完成后的回调
        out_trade_no: new Date().getTime(), //订单号
        attach: '名称',
        body: '购买信息',
        total_fee: '1', // 此处的额度为分
        spbill_create_ip: '121.196.208.176'
      }
      this.loadgetBrandWCPayParams(obj, function(error, responseData) {
        res.render('payment', {
          title: '微信支付',
          wxPayParams: JSON.stringify(responseData),
          //userInfo : userInfo
        });
      })

    }
  },

  methods: {
    loadgetBrandWCPayParams(obj, cb) {
      this.getBrandWCPayParams(obj, function(error, responseData) {
        if (error) {
          cb(error);
        } else {
          cb(null, responseData);
        }
      });
    },
    /**
     * 获取微信统一下单参数
     */
    getUnifiedorderXmlParams(obj) {
      var body = '<xml> ' +
        '<appid>' + config.wxappid + '</appid> ' +
        '<attach>' + obj.attach + '</attach> ' +
        '<body>' + obj.body + '</body> ' +
        '<mch_id>' + config.mch_id + '</mch_id> ' +
        '<nonce_str>' + obj.nonce_str + '</nonce_str> ' +
        '<notify_url>' + obj.notify_url + '</notify_url>' +
        '<openid>' + obj.openid + '</openid> ' +
        '<out_trade_no>' + obj.out_trade_no + '</out_trade_no>' +
        '<spbill_create_ip>' + obj.spbill_create_ip + '</spbill_create_ip> ' +
        '<total_fee>' + obj.total_fee + '</total_fee> ' +
        '<trade_type>' + obj.trade_type + '</trade_type> ' +
        '<sign>' + obj.sign + '</sign> ' +
        '</xml>';
      return body;
    },
    getPrepayId(obj) {
      var that = this;
      // 生成统一下单接口参数
      var UnifiedorderParams = {
        appid: config.wxappid,
        attach: obj.attach,
        body: obj.body,
        mch_id: config.mch_id,
        nonce_str: this.createNonceStr(),
        notify_url: obj.notify_url, // 微信付款后的回调地址
        openid: this.userInfo.openid,
        out_trade_no: obj.out_trade_no, //new Date().getTime(), //订单号
        spbill_create_ip: obj.spbill_create_ip,
        total_fee: obj.total_fee,
        trade_type: 'JSAPI',
        // sign : getSign(),
      };
      // 返回 promise 对象
      return new Promise(function(resolve, reject) {
        // 获取 sign 参数
        UnifiedorderParams.sign = that.getSign(UnifiedorderParams);
        var url = 'https://api.mch.weixin.qq.com/pay/unifiedorder';
        request.post({
          url: url,
          body: JSON.stringify(that.getUnifiedorderXmlParams(UnifiedorderParams))
        }, function(error, response, body) {
          var prepay_id = '';
          if (!error && response.statusCode == 200) {
            // 微信返回的数据为 xml 格式， 需要装换为 json 数据， 便于使用
            xml2jsparseString(body, {
              async: true
            }, function(error, result) {
              prepay_id = result.xml.prepay_id[0];
              // 放回数组的第一个元素
              resolve(prepay_id);
            });
          } else {
            reject(body);
          }
        });
      })
    },
    getSign(signParams) {
      // 按 key 值的ascll 排序
      var keys = Object.keys(signParams);
      keys = keys.sort();
      var newArgs = {};
      keys.forEach(function(val, key) {
        if (signParams[val]) {
          newArgs[val] = signParams[val];
        }
      })
      var string = queryString.stringify(newArgs) + '&key=' + config.wxpaykey;
      // 生成签名
      return crypto.createHash('md5').update(queryString.unescape(string), 'utf8').digest("hex").toUpperCase();
    },
    getBrandWCPayParams(obj, callback) {
      var that = this;
      var prepay_id_promise = that.getPrepayId(obj);
      prepay_id_promise.then(function(prepay_id) {
        var prepay_id = prepay_id;
        var wcPayParams = {
          "appId": config.wxappid, //公众号名称，由商户传入
          "timeStamp": parseInt(new Date().getTime() / 1000).toString(), //时间戳，自1970年以来的秒数
          "nonceStr": that.createNonceStr(), //随机串
          // 通过统一下单接口获取
          "package": "prepay_id=" + prepay_id,
          "signType": "MD5", //微信签名方式：
        };
        wcPayParams.paySign = that.getSign(wcPayParams); //微信支付签名
        callback(null, wcPayParams);
      }, function(error) {
        callback(error);
      });
    },
    /**
     * 获取随机的NonceStr
     */
    createNonceStr() {
      return Math.random().toString(36).substr(2, 15);
    },
    async wxpayClik() {
      let wx_app_id
      try {
        wx_app_id = (await this.$http.get('/wx/appid')).data.wx_app_id //在线获取
      } catch (e) {

      }
      const redirectUri = `http://${location.hostname}/wx/wxpay`
      location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`

      // if(!wx_openid){
      //   const { redirect } = this.$route.query
      //   if(redirect){
      //     localStorage.setItem('redirect', redirect)
      //   }
      //   const redirectUri = `http://${location.hostname}/wx/code2openid`
      //   location.href = `http://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`
      //   return;
      // }else{
      //   WxPay.getAccessToken({
      //     notify_url: 'http://demo.com/', //微信支付完成后的回调
      //     out_trade_no: new Date().getTime(), //订单号
      //     attach: '名称',
      //     body: '购买信息',
      //     total_fee: '1', // 此处的额度为分
      //     spbill_create_ip: '192',
      //     wx_code: wx_openid,
      //   }, function(error, responseData) {
      //     res.render('payment', {
      //       title: '微信支付',
      //       wxPayParams: JSON.stringify('123'),
      //       //userInfo : userInfo
      //     });
      //   })
      // }

    }
  }
}
</script>

<style scoped>
.demo3-slot {
  text-align: center;
  padding: 8px 0;
  color: #888;
}
</style>
