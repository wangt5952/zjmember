import express from 'express'

import _ from 'lodash';
import moment from 'moment';
moment.locale('zh-cn');
import request from 'request';

const router = express.Router()

const httpGet = (...args) => new Promise((resolve, reject) => {
  request.get(...args, (err, response, body) => {
    if(err){
      reject(err)
    } else {
      resolve({response, body})
    };
  })
})

const map = {
  'appid.get': async(req, res, next) => {
    const { wx_app_id, wx_app_secret } = req.headers
    return res.json({
      wx_app_id
    })
  },
  'code2openid.get': async (req, res, next) => {
    let { code } = req.query;

    const { wx_app_id, wx_app_secret } = req.headers
    if(!code){
      return res.sendStatus(404);
    }

    let { body } = await httpGet(`https://api.weixin.qq.com/sns/oauth2/access_token?appid=${wx_app_id}&secret=${wx_app_secret}&code=${code}&grant_type=authorization_code`)

    let wx_nickname;
    let { errcode: wx_errno, openid: wx_openid, access_token } = JSON.parse(body);
    console.log(wx_openid)
    if(wx_errno){
      return res.sendStatus(404);
    }
    return res.redirect(`/register?wx_openid=${wx_openid}`)
  },
}

_.forEach(map, (o, k)=>{
  const [ path, method = 'use' ] = k.split('.')
  router[method](`/${path}`, async (req, res, next) => {
    try{
      await o(req, res, next)
    }catch(e){
      next(e)
    }
  })
})

module.exports = router
