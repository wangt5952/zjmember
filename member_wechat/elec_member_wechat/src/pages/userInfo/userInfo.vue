<template>
  <scroller lock-x>
    <div>
      <div style="display:flex;background-color:#fff;margin-top:0.5rem;border-top:1px solid #e1e1e1;border-bottom:1px solid #e1e1e1;">
        <div>
          <img src="static/img/member.png" style="border-radius:50%;width:2rem;height:2rem;margin:0.5rem;">
        </div>
        <div style="margin:0.4rem;">
          <div style="font-size:0.5rem;">{{user.name}} <span v-if="user.level" style="background-color:#56ded3;color:#fff;padding:2px 5px;font-size:0.9em;border-radius:3px;">{{user.level}}</span></div>
          <div style="font-size:0.4rem;color:#7f8081;">累计积分<span style="margin-left:0.25rem;">{{user.cumulate_points}}</span></div>
          <div style="font-size:0.4rem;color:#7f8081;">可用积分<span style="margin-left:0.25rem;">{{user.usable_points}}</span></div>
        </div>
      </div>

      <div style="margin-top:0.25rem;background:#fff;border-top:1px solid #e1e1e1;font-size:0.4rem;">
        <div @click="openEdit('name', '请输入姓名')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">姓名</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{user.name}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openPopup('sex', '请选择性别')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">性别</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{popupName('sex')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="!user.birthday_modified && openDatetime('birthday', '请选择生日', 'YYYY-MM-DD', 'ms')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">生日</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{user.birthday | unix('YYYY-MM-DD', 'ms')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span v-if="!user.birthday_modified" class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openPopup('occupation', '请选择职业')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">职业</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{popupName('occupation')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openEdit('address', '请输入地址')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">地址</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;">{{user.address}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openPopup('degree_of_education', '请选择教育程度')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">教育程度</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{popupName('degree_of_education')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openPopup('income_range', '请选择收入范围')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">收入范围</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{popupName('income_range')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div @click="openPopup('interest', '请选择兴趣爱好')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">兴趣爱好</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;">{{popupName('interest')}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <router-link to="/changePhone" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">手机</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{user.mobile}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </router-link>

        <div @click="openEdit('wechat_account', '请输入微信号')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">微信号</div>
          <div style="flex:1;text-align:right;padding:0.25rem;color:#7f8081;">{{user.wechat_account}}</div>
          <div style="padding:0.25rem;padding-left:0;width:0.4rem;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="padding:0.25rem;margin-left:0.25rem;">是否公开微信号</div>
          <div style="flex:1;">

          </div>
          <div style="padding:0.25rem;padding-left:0;">
            <mt-switch v-model="user.enable_public_wa" @change="save('enable_public_wa')"></mt-switch>
          </div>
        </div>


      </div>

      <div v-transfer-dom>
        <popup v-model="popup" position="bottom" style="background-color:#fff;">
          <div style="padding:0.25rem;text-align:center;position:relative;">
            <div>{{popupTitle}}</div>
            <div v-if="popupSelected instanceof Array" @click="closePopup" style="position:absolute;right:0;bottom:5px;top:5px;display:flex;align-items:center;padding:0 15px;color:#04BE02;">确定</div>
            <div @click="popup=false" style="position:absolute;left:0;bottom:5px;top:5px;display:flex;align-items:center;padding:0 15px;color:#828282;">取消</div>
          </div>

          <div @click="choosePopup(o.id)" v-for="o in popupList[popupKey]" style="display:flex;border-top:1px solid #e1e1e1;">
            <div style="flex:1;padding:0.25rem;">
              {{o.name}}
            </div>
            <div style="display:flex;align-items: center;margin-right:0.25rem;">
              <span v-show="(popupSelected instanceof Array) ? ~popupSelected.indexOf(o.id) :o.id==popupSelected" class="iconfont icon-check" style="color:#797979;"></span>
            </div>
          </div>
        </popup>
      </div>
      <div style="padding-bottom:100px;"></div>
      <datetime :show="false"></datetime>
    </div>
  </scroller>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
import { TransferDom, Popup, Datetime, Scroller } from 'vux'
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
    Popup,
    Datetime, Scroller,
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  data() {
    return {
      popup: false,
      popupTitle: '',
      popupKey: null,
      popupSelected: null,
      popupList: {
        'sex': [{id:0,name:'男'},{id:1,name:'女'}],
        'occupation': [
          {id:0,name:'教师'},
          {id:1,name:'会计'},
          {id:2,name:'IT'},
          {id:3,name:'金融'},
          {id:4,name:'销售'},
          {id:5,name:'营业员'},
          {id:6,name:'公务员'},
          {id:7,name:'自由职业'}
        ],
        'degree_of_education': [
          {id:0, name:'小学'},
          {id:1, name:'初中'},
          {id:2, name:'高中/中专/技校/职高'},
          {id:3, name:'大专'},
          {id:4, name:'本科'},
          {id:5, name:'硕士'},
          {id:6, name:'博士'},
        ],
        'income_range': [
          {id:0, name:'2000以下'},
          {id:1, name:'2000-4000'},
          {id:2, name:'4000-6000'},
          {id:3, name:'6000-10000'},
          {id:4, name:'10000以上'},
        ],
        'interest': [
          {id:0, name:'看书'},
          {id:1, name:'音乐'},
          {id:2, name:'上网'},
          {id:3, name:'游戏'},
          {id:4, name:'旅游'},
          {id:5, name:'汽车'},
          {id:6, name:'购物'},
        ]
      },
      user: {},

      key4put: {
        'degree_of_education': 'degreeOfEducation',
        'income_range': 'incomeRange',
        'wechat_account': 'wechatAccount',
        'enable_public_wa': 'enablePublicWa'
      }
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  methods: {
    popupName(key){
      const id = this.user[key];
      if(id instanceof Array){
        const list = _.map(_.filter(this.popupList[key], o=>~id.indexOf(o.id)), 'name')
        return list.join(',')
      }else{
        return (_.find(this.popupList[key], {id}) || {}).name
      }
    },
    openPopup(key, title) {
      this.popupSelected = this.user[key];
      if(this.popupSelected instanceof Array){
        this.popupSelected = _.intersection(this.popupSelected, _.map(this.popupList[key], 'id'));
      }
      this.popupKey = key;
      this.popupTitle = title;
      this.popup = true;
    },
    choosePopup(id){
      if(this.popupSelected instanceof Array){
        if(~this.popupSelected.indexOf(id)){
          this.popupSelected = _.without(this.popupSelected, id)
        }else{
          this.popupSelected = _.concat(this.popupSelected, id)
        }
      }else{
        if(this.user[this.popupKey] != id){
          this.user[this.popupKey] = id
        }
        this.popup = false;
        this.save(this.popupKey)
      }
    },
    closePopup(){
      if(this.popupSelected instanceof Array){
        this.user[this.popupKey] = this.popupSelected;
        this.popup = false;
        this.save(this.popupKey)
      }else{
        this.popup = false;
      }
    },
    openEdit(key, title){
      this.$vux.confirm.prompt('', { title,
        onShow: () => {
          this.$vux.confirm.setInputValue(this.user[key])
        },onConfirm: (input) => {
          this.user[key] = input;
          this.save(key)
        }});
    },
    openDatetime(key, title, format, unit){
      this.$vux.datetime.show({
        startDate: '1900-01-01',
        endDate: moment().format(format),
        cancelText: '取消',
        confirmText: '确定',
        format,
        value: moment.unix((unit == 'ms') ? (this.user[key] / 1000) : this.user[key]).format(format),
        onConfirm: val => {
          this.user[key] = moment(val).unix() * 1000;

          if(key == 'birthday'){
            this.birthday_modified = 1;
          }
          this.save(key)
        }
      })
    },

    async save(key){
      let value = this.user[key];
      if(value instanceof Array) value = value.join(',')
      const data = {
        memberId: this.member_id,
        [this.key4put[key] || key]: value
      }
      let res = await this.$http.put(`/api/member`, data)
      console.log(res);
    }
  },
  async mounted(){
    document.title = '个人信息'
    let member_id = this.member_id;
    let { data } = await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)
    data.interest = data.interest ? _.map(data.interest.split(','), _.parseInt) : [];
    this.user=data;
  }
}
</script>
<style lang="less" scoped>
</style>
