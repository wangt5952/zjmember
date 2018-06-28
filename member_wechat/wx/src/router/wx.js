import express from 'express'

import _ from 'lodash';
import moment from 'moment';
moment.locale('zh-cn');
import request from 'request';

// 新增
// var ejs = require('ejs');
// var fs = require('fs');
var url = require('url');
var queryString = require('querystring');
var crypto = require('crypto');
var xml2jsparseString = require('xml2js').parseString;
// 引入项目的配置信息
var config = require('../config/index.js');
config = config.default
// var messageTpl = fs.readFileSync(__dirname +'/message.ejs', 'utf-8');

const router = express.Router()

const httpGet = (...args) => new Promise((resolve, reject) => {
  request.get(...args, (err, response, body) => {
    if (err) {
      reject(err)
    } else {
      resolve({
        err,
        response,
        body
      })
    };
  })
})




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
      '<appid>' + config.WX.wxappid + '</appid> ' +
      '<attach>' + obj.attach + '</attach> ' +
      '<body>' + obj.body + '</body> ' +
      '<mch_id>' + config.WX.mch_id + '</mch_id> ' +
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
      appid: config.WX.wxappid,
      attach: obj.attach,
      body: obj.body,
      mch_id: config.WX.mch_id,
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
            console.log(body)
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
      .catch(error => console.log('caught', error))
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
    var string = queryString.stringify(newArgs) + '&key=' + config.WX.wxpaykey;
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
        "appId": config.WX.wxappid, //公众号名称，由商户传入
        "timeStamp": parseInt(new Date().getTime() / 1000).toString(), //时间戳，自1970年以来的秒数
        "nonceStr": that.createNonceStr(), //随机串
        // 通过统一下单接口获取
        "package": "prepay_id=" + prepay_id,
        "signType": "MD5", //微信签名方式：
      };
      wcPayParams.paySign = that.getSign(wcPayParams); //微信支付签名
      console.log(wcPayParams)
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
    console.log(obj)
    var that = this;
    var getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + config.WX.wxappid + "&secret=" + config.WX.wxappsecret + "&code=" + obj.code + "&grant_type=authorization_code";
    request.post({
      url: getAccessTokenUrl
    }, function(error, response, body) {
      if (!error && response.statusCode == 200) {
        if (40029 == body.errcode) {
          cb(error, body);
        } else {
          body = JSON.parse(body);
          let info = {}
          info.access_token = body.access_token;
          info.expires_in = body.expires_in;
          info.refresh_token = body.refresh_token;
          info.openid = body.openid;
          info.scope = body.scope;
          that.userInfo = info
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

var WechatPays = new WechatPay();

const map = {
    'appid.get': async (req, res, next) => {
      const {
        wx_app_id,
        wx_app_secret
      } = req.headers
      return res.json({
        wx_app_id
      })

    },
    'code2openid.get': async (req, res, next) => {
      let {
        code
      } = req.query;

      const {
        wx_app_id,
        wx_app_secret
      } = req.headers
      if (!code) {
        return res.sendStatus(404);
      }

      let {
        body
      } = await httpGet(`https://api.weixin.qq.com/sns/oauth2/access_token?appid=${wx_app_id}&secret=${wx_app_secret}&code=${code}&grant_type=authorization_code`)

      let wx_nickname;
      let {
        errcode: wx_errno,
        openid: wx_openid,
        access_token
      } = JSON.parse(body);
      if (wx_errno) {
        return res.sendStatus(404);
      }
      return res.redirect(`/register?wx_openid=${wx_openid}`)
    },
    'wxpay.get': async (req, res, next) => {
      let {
        code,
        feeT
      } = req.query;
      if (!code) {
        return res.sendStatus(404);
      } else {
        if (feeT) {
          feeT = feeT.replace(/[\-\_\!\|\~\(\)\#\$\%\^\&\*\{\}\:\;\"\L\<\>\?]/g, '');
          var arr = feeT.split(',');
        }
        WechatPays.getAccessToken({
          notify_url: 'http://jiayuanmember.dorm9tech.com/wx/notify', //微信支付完成后的回调
          out_trade_no: new Date().getTime(), //订单号
          attach: feeT,
          body: '上海紫金广场停车系统',
          total_fee: arr[3],
          spbill_create_ip: '121.196.208.176',
          code: code
        }, function(error, responseData) {
          res.render('pay', {
            title: '微信支付',
            wxPayParams: JSON.stringify(responseData),
            //userInfo : userInfo
          });
        })
      }
      // return res.json({
      //   'mes': req.query.id
      // })
    },
    'notify.post': async (req, res, next) => {
        console.log(req.body)
        xml2jsparseString(req.body, {
            async: true
          }, function(error, result) {
            if (result.xml) {
              let transaction_id = result.xml.transaction_id[0]
              let attach = result.xml.attach[0]
              var arr = attach.split(',');
              request({
                  url: 'http://121.196.208.176:9001/api/parkingCoupon/pay/',
                  method: "POST",
                  json: true,
                  headers: {
                    "content-type": "application/json",
                  },
                  body: {
                    'member_id': arr[0],
                    'car_number': arr[1],
                    'in_date': arr[2],
                    'price': arr[3],
                    'ticket_no': transaction_id
                  }
                }, function(error, response, body) {
                  console.log(body) // 请求成功的处理逻辑
                  if (!error && response.statusCode == 200) {
                    console.log(body) // 请求成功的处理逻辑
                  }
                })

                // request.post({
                //   url: 'http://121.196.208.176:9001/api/parkingCoupon/pay/',
                //   body: JSON.stringify({
                //     'member_id': arr[0],
                //     'car_number': arr[1],
                //     'in_date':arr[2],
                //     'price': arr[3],
                //     'ticket_no': transaction_id
                //   })
                // }, function(error, response, body) {
                //         console.log(body)
                // });



                  res.writeHead(200, {
                    'Content-Type': 'application/xml'
                  });
                  res.end('<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>');
                }
                // 放回数组的第一个元素
              });

          },
          'wx_login.get': async (req, res, next) => {
            const {
              wx_app_id,
              wx_app_secret
            } = req.headers //对于支付来说已经多余，登录已经获取
            const redirectUri = `http://121.196.208.176/wx/wxpay`
            res.redirect(`http://open.weixin.qq.com/connect/oauth2/authorize?appid=${wx_app_id}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`)
          },
        }



        _.forEach(map, (o, k) => {
          const [path, method = 'use'] = k.split('.')
          console.log(`/${path}/${method} `)
          router[method](`/${path}`, async (req, res, next) => {
            try {
              await o(req, res, next)
            } catch (e) {
              next(e)
            }
          })
        })

        module.exports = router
