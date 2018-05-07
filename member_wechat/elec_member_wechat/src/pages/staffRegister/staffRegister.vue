<template>
  <div>
    <div v-if="showForm">
      <div style="margin-top:10px;background:#fff;border-top:1px solid #e1e1e1;">
        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">姓名</div>
          <div style="flex:1;padding:10px;">
            <input type="text" placeholder="请输入姓名" v-model="form.name" />
          </div>
        </div>

        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">联系电话</div>
          <div style="flex:1;padding:10px;">
            <input type="number" placeholder="请输入联系电话" v-model="form.phone" />
          </div>
        </div>

        <div style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">类别</div>
          <div style="flex:1;padding:10px;">
            <checker v-model="form.leibie" default-item-class="demo5-item" selected-item-class="demo5-item-selected" :radio-required="true">
              <checker-item v-for="o in popupList.leibie" :value="o.id">{{o.name}}</checker-item>
            </checker>
          </div>
        </div>

        <div v-if="form.leibie==1" @click="shopSelect()" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">商户</div>
          <div style="flex:1;padding:10px;">{{form.shopName}}</div>
          <div style="padding:10px;padding-left:0;width:16px;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>

        <div v-else-if="form.leibie==0" @click="openPopup('department', '请选择部门')" style="display:flex;border-bottom:1px solid #e1e1e1;">
          <div style="width:70px;padding:10px;margin-left:10px;color:#00c9b2;">部门</div>
          <div style="flex:1;padding:10px;">{{popupName('department')}}</div>
          <div style="padding:10px;padding-left:0;width:16px;">
            <span class="iconfont icon-right" style="color:#797979;"></span>
          </div>
        </div>
      </div>

      <div @click="handleSubmit" style="background-color:#00c9b2;color:#fff;padding:10px;text-align:center;margin-top:20px;margin: 20px;border-radius:5px;">注册</div>

      <div v-transfer-dom>
        <popup v-model="popup" position="bottom" style="background-color:#fff;">
          <div style="padding:10px;text-align:center;position:relative;">
            <div>{{popupTitle}}</div>
            <div v-if="popupSelected instanceof Array" @click="closePopup" style="position:absolute;right:0;bottom:5px;top:5px;display:flex;align-items:center;padding:0 15px;color:#04BE02;">确定</div>
            <div @click="closePopup" style="position:absolute;left:0;bottom:5px;top:5px;display:flex;align-items:center;padding:0 15px;color:#828282;">取消</div>
          </div>

          <div @click="choosePopup(o.id)" v-for="o in popupList[popupKey]" style="display:flex;border-top:1px solid #e1e1e1;">
            <div style="flex:1;padding:10px;">
              {{o.name}}
            </div>
            <div style="display:flex;align-items: center;margin-right:10px;">
              <span v-show="(popupSelected instanceof Array) ? ~popupSelected.indexOf(o.id) :o.id==popupSelected" class="iconfont icon-check" style="color:#797979;"></span>
            </div>
          </div>
        </popup>
      </div>
    </div>
    <div v-else style="background-color:#fff;padding-bottom:15px;">
      <msg :title="statusName" :icon="statusIcon"></msg>
    </div>
  </div>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import { Checker, CheckerItem, TransferDom, Popup, Scroller, Msg } from 'vux'
import _ from 'lodash'
import {
  mapState,
} from 'vuex';

export default {
  directives: {
    TransferDom
  },
  components: {
    Popup, Scroller,Checker, CheckerItem, Msg
  },
  computed: {
    statusName(){
      const m = {
        '0':'审核通过', '1':'未审核', '2':'审核未通过'
      }

      return m[this.status] || this.status;
    },
    statusIcon(){
      const m = {
        '0':'success', '1':'waiting', '2':'warn'
      }

      return m[this.status] || this.status;
    },
    ...mapState({
      member_id: state => parseInt(state.member_id),
    }),
  },
  data() {
    return {
      popup: false,
      popupTitle: '',
      popupKey: null,
      popupSelected: null,
      popupList: {
        'leibie': [
          {id:0, name:'工作人员'},
          {id:1, name:'商户'},
        ],
        'department': [
          {id:0, name:'财务部'},
          {id:1, name:'招商部'},
          {id:2, name:'运营部'},
          {id:3, name:'客服中心'},
        ],
      },

      form: {
        name:'',
        phone: '',
        leibie: 1,
        department:0,
        shopId:null,
        shopName:null,
      },
      showForm: false,
      status: null,
    }
  },
  methods: {
    popupName(key){
      const id = this.form[key];
      if(id instanceof Array){
        const list = _.map(_.filter(this.popupList[key], o=>~id.indexOf(o.id)), 'name')
        return list.join(',')
      }else{
        return (_.find(this.popupList[key], {id}) || {}).name
      }
    },
    openPopup(key, title) {
      this.popupSelected = this.form[key];
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
        if(this.form[this.popupKey] != id){
          this.form[this.popupKey] = id
        }
        this.popup = false;
      }
    },
    closePopup(){
      if(this.popupSelected instanceof Array){
        this.form[this.popupKey] = this.popupSelected;
        this.popup = false;
      }else{
        this.popup = false;
      }
    },
    shopSelect(){
      this.$store.commit('staffshop', this.form)
      this.$router.push('/shopSelect')
    },
    async handleSubmit(){
      const { shopName, department, shopId, leibie, ...form } = this.form;
      const { member_id: memberId } = this
      form.mallId = mallId;
      form.memberId = memberId;
      let url;
      if(leibie === 1){
        form.shopId = shopId;
        url = '/api/member/registClerk';
      } else if(leibie === 0){
        form.department = department;
        url = '/api/member/registServices'
      }



      const [ errno, errmsg ] =
                              (!form.name && [1, '请输入姓名'])
                              || (!form.phone && [2, '请输入联系电话'])
                              || ((leibie === 1 && !form.shopId) && [ 1, '请选择商户'])
                              || [];

      if(errno && errmsg) {
        this.$vux.toast.text(errmsg)
        return
      }

      try{
        await this.$http.post(url, form)
        this.$vux.toast.text('注册申请已提交')
        this.$router.push('/member')
      }catch(e){
        if(e.response){
          this.$vux.toast.text(e.response.data)
        }else{
          this.$vux.toast.text(e.message)
        }

      }

    }
  },
  async mounted(){
    document.title = '核销注册'
    const { staffreg } = this.$store.state


    try{
      const { data } = await this.$http.get(`/api/member/verification/${this.member_id}`)
      const { status } = data;
      this.status = data.status
    }catch(e){
      this.showForm = true
    }
    this.form = {
      ...this.form,
      ...staffreg,
    }
  }
}
</script>

<style lang="less" scoped>
input {
  background-color:transparent;
  border-style:none;
  outline:none;
}

.box {
  padding: 0 15px;
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
}
.demo5-item-selected {
  border-color: #00c9b2;
}
</style>
