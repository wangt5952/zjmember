<template>
  <div>
    <div>
      <div style="margin-top:10px;background:#fff;border-top:1px solid #e1e1e1;font-size:0.4rem;">
        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">姓名</div>
          <div style="flex:1;padding:10px;">
            <input style="font-size:0.4rem;" type="text" placeholder="请输入姓名" v-model="user.name" />
          </div>
        </div>

        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">性别</div>
          <div style="flex:1;padding:10px;">
            <checker v-model="user.sex" default-item-class="demo5-item" selected-item-class="demo5-item-selected" :radio-required="true">
              <checker-item key="id" v-for="o in popupList.sex" :value="o.id">
                <span v-if="o.icon" class="iconfont" :class="o.icon" style="color:#797979;"></span>
                {{o.name}}
              </checker-item>
            </checker>
          </div>
        </div>

        <div @click="openDatetime('birthday', '请选择生日', 'YYYY-MM-DD', 'ms')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">生日</div>
          <div style="flex:1;padding:10px;color:#7f8081;">{{user.birthday | unix('YYYY-MM-DD', 'ms')}}</div>
          <div style="padding:10px;padding-left:0;width:16px;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>
      </div>
    </div>

    <div @click="handleSubmit" style="background-color:#00c9b2;color:#fff;padding:10px;text-align:center;margin-top:20px;margin: 20px;border-radius:5px;">绑定</div>
    <datetime :show="valueFalse"></datetime>
  </div>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
import { TransferDom, Popup, Checker, CheckerItem, Datetime } from 'vux'
import moment from 'moment';

const { mallId } = global;

export default {
  directives: {
    TransferDom
  },
  components:{
    Popup,
    Datetime, Checker, CheckerItem,
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  data(){
    return {
      valueFalse: false,
      popupList: {
        sex:[
          {id:0, name:'男', icon:'icon-male'},
          {id:1, name:'女', icon:'icon-female'},
        ]
      },
      user: {
        birthday:moment().subtract(30, 'y').unix() * 1000,
        name:'',
        sex:0,
      },
    }
  },
  methods: {
    async handleSubmit() {
      const { user } = this
      const { mobile, openId } = this.$route.query

      const [errno, errmsg] = (!user.name && [1, '请输入姓名'])
        || [];
      if(errno){
        this.$vux.toast.text(errmsg)
        return
      }

      try{
        const { member_id } = (await this.$http.post(`/api/member`, {
          ...user,
          mallId, mobile, openId
        })).data;

        if(member_id){
          this.$store.commit('login', {member_id})
          this.$router.push('/registerSucc');
          setTimeout(() => {
            const redirect = localStorage.getItem('redirect')
            localStorage.removeItem('redirect');
            this.$router.push(redirect || '/member');
          },2000)
        }
      }catch(e){
        if(e.response){
          this.$vux.toast.text(e.response.data.content)
        }else{
          this.$vux.toast.text(e.message)
        }
      }
    },

    openDatetime(key, title, format, unit){
      const scale = (unit == 'ms') ? 1000 : 1;
      this.$vux.datetime.show({
        startDate: '1900-01-01',
        endDate: moment().format(format),
        cancelText: '取消',
        confirmText: '确定',
        format,
        value: moment.unix(this.user[key] / scale).format(format),
        onConfirm: val => {
          this.user[key] = moment(val).unix() * scale;
        }
      })
    },
  },
  mounted(){
    document.title = '会员信息'
  },
}

</script>

<style lang="less" scoped>
input {
  background-color:transparent;
  border-style:none;
  outline:none;
  width: 100%;
}

.demo5-item {
  width: 80px;
  height: 26px;
  line-height: 26px;
  text-align: center;
  border-radius: 3px;
  border: 1px solid #ccc;
  background-color: #fff;
  margin-right: 6px;
  color:#7f8081;
}
.demo5-item-selected {
  border-color: #00c9b2;
}
</style>
