
import express from 'express'

import _ from 'lodash';
import moment from 'moment';
moment.locale('zh-cn');

const router = express.Router()

const map = {
  'do.get': async (req, res, next) => {
    res.send('eee')
  },
  'hello.get': async (req, res, next) => {
    res.json('hello')
  },

  'github.post': async (req, res, next) => {
    const { ref, repository = {} } = req.body

    if(ref == 'refs/heads/zhao_branch' && repository.name == 'member_wechat'){
      const { spawn } = require('child_process');
      spawn('../update.sh');
    }
    res.send('ok')
  },
  'coding.post': async (req, res, next) => {
    const codingEvent = req.headers['x-coding-event'];
    const { token } = req.body;
    if(codingEvent == 'push' && token == 'f9XOP9CMC7e9b2aL'){
      const { spawn } = require('child_process');
      spawn('../update.sh');
    }
    res.send('post');
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
