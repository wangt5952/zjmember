
<script type="text/javascript">
/*
 * @Author: anziguoer
 * @Email: anziguoer@sina.com
 * @Date:   2016-09-14 11:40:12
 * @Last Modified by:   anziguoer
 * @Last Modified time: 2016-09-14 11:40:31
 * @Descrition : wechat 微信支付功能
 */

var url = require('url');
var queryString = require('querystring');
var crypto = require('crypto');
var xml2jsparseString = require('xml2js').parseString;
// 引入项目的配置信息
// var config = require('../config/index.js');
var config = {
  wxappid: 'wx3041b222eaad5c8a',
  wxappsecret: 'fda2fbf4fdec7a43b178f12d4bf36414',
  wxpaykey: 'x0jhJ4r5NMAKei7ktvysNhYxjFsV63uR',
  mch_id: '1505462971'
}
// wechat 支付类 (使用 es6 的语法)
class WechatPay {
  /**
   * 构造函数
   * @param params 传递进来的方法
   */
  constructor(userInfo) {
    this.userInfo = userInfo;
  }

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
  }

  /**
   * 获取微信统一下单的接口数据
   */
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
      this.$http.post({
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
  }

  /**
   * 获取微信支付的签名
   * @param payParams
   */
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
  }

  /**
   * 微信支付的所有参数
   * @param req 请求的资源, 获取必要的数据
   * @returns {{appId: string, timeStamp: Number, nonceStr: *, package: string, signType: string, paySign: *}}
   */
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
  }

  /**
   * 获取随机的NonceStr
   */
  createNonceStr() {
    return Math.random().toString(36).substr(2, 15);
  };

  /**
   * 获取微信的 AccessToken
   */
  getAccessToken(obj, cb) {
    var that = this;
    that.userInfo.code = obj.wx_code
    var getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + config.wxappid + "&secret=" + config.wxappsecret + "&code=" + that.userInfo.code + "&grant_type=authorization_code";
    this.$http.post({
      url: getAccessTokenUrl
    }, function(error, response, body) {
      if (!error && response.statusCode == 200) {
        if (40029 == body.errcode) {
          cb(error, body);
        } else {
          body = JSON.parse(body);
          that.userInfo.access_token = body.access_token;
          that.userInfo.expires_in = body.expires_in;
          that.userInfo.refresh_token = body.refresh_token;
          that.userInfo.openid = body.openid;
          that.userInfo.scope = body.scope;
          // console.log(that.userInfo);
          // 拼接微信的支付的参数
          that.getBrandWCPayParams(obj, function(error, responseData) {
            if (error) {
              cb(error);
            } else {
              cb(null, responseData);
            }
          });
        }
      } else {
        cb(error);
      }
    });
  }
}
var WxPay = new WechatPay;
export default {
  WxPay
}
</script>
