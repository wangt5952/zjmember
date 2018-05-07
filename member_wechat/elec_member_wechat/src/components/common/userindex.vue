<template>
    <div>
        <mt-cell title="姓名" style="right:2%;margin-left:0.03rem">
          <!-- :value="this.user.name"  ↓ -->
          <input id="name" type="text" v-model="user.name" placeholder="请输入真实姓名"
            style="position:relative;outline: none;border: medium;
                text-align:right;left:-17%;height:0.4rem" @change="save">
        </mt-cell>


        <mt-cell title="性别" is-link style="right:2%;margin-left:0.03rem;">
          <select style="position:relative;right:-3%;height:0.2rem;width:2.4rem;padding-left: 2.1rem;height:0.4rem" v-model="user.sex" @change="save">
            <option value="0">男</option>
            <option value="1">女</option>
          </select>
        </mt-cell>
        <mt-cell title="生日"  style="right:2%;margin-left:0.03rem">
           <input id="name" type="text" v-model="this.value1"
            style="position:relative;outline: none;border: medium;
                text-align:right;left:-17%;height:0.4rem" readonly="readonly">
          <!-- <datetime  v-model="this.value1" :start-date="startDate"  @on-change="save"  style="padding-right: 15px;color:#656B79;border-right-width: 23px;">

        </datetime>
         <div style="">
          <img src="static/img/rightLogo.png" alt="" style="padding-bottom: 17px;padding-left: 0px;padding-right: 4px;">
        </div> -->
       </mt-cell>


      <mt-cell title="职业" is-link style="right:2%;margin-left:0.03rem">
        <select style="position:relative;right:-1%;height:0.2rem;width:2.4rem;padding-left:1.8rem;height:0.4rem" v-model="user.occupation"  @change="save">
          <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</option>
          <option value="1">工程师</option>
          <option value="2">&nbsp;&nbsp;&nbsp;&nbsp;医生</option>
        </select>
      </mt-cell>

      <mt-cell title="地址" style="right:3%;margin-left:0.07rem">
        <input  @change="save" id="address" class="addre" type="text" v-model="user.address" placeholder="请输入地址"
           style="position:relative;outline: none;border: medium;
           text-align:right;color:#656B79;left:-11%;height:0.4rem">
      </mt-cell>
      <mt-cell title="教育程度" is-link style="right:2%;margin-left:0.03rem">
        <select style="position:relative;right:4%;height:0.4rem;width:2rem;padding-left:1.5rem" v-model="user.degree_of_education" @change="save">
          <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</option>
          <option value="1">&nbsp;&nbsp;&nbsp;&nbsp;博士</option>
          <option value="2">&nbsp;&nbsp;&nbsp;&nbsp;硕士</option>
          <option value="3">&nbsp;&nbsp;&nbsp;&nbsp;本科</option>
          <option value="4">&nbsp;&nbsp;&nbsp;&nbsp;大专</option>
          <option value="5">&nbsp;&nbsp;&nbsp;&nbsp;高中</option>
        </select>
      </mt-cell>

      <mt-cell title="收入范围" is-link style="right:2%;margin-left:0.03rem">
        <select style="position:relative;right:4%;height:0.4rem;width:2.08rem;padding-left:0.95rem" v-model="user.income_range"  @change="save">
           <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</option>
           <option value="1">&nbsp;&nbsp;&nbsp;&nbsp;10000-50000</option>
           <option value="2">&nbsp;&nbsp;50000-100000</option>
        </select>
      </mt-cell>
     <mt-cell title="兴趣爱好" is-link style="right:2%;margin-left:0.03rem">
        <select style="position:relative;right:4%;height:0.4rem;width:2rem;padding-left:1.5rem" v-model="user.interest"  @change="save">
          <option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</option>
          <option value="1">&nbsp;&nbsp;&nbsp;&nbsp;篮球</option>
          <option value="2">&nbsp;&nbsp;&nbsp;&nbsp;游泳</option>
        </select>
      </mt-cell>
      <mt-cell title="手机" style="right:3%;margin-left:0.07rem">
        <input  type="text" :value="this.user.mobile" readonly="readonly" style="position:relative;left:20%;outline: none;border: medium;width:1.1rem;">
        <!-- <router-link to="/changePhone"><mt-button size="small" style="background-color:#F2F2F2">修改</mt-button></router-link> -->
        <div style="margin-top:-2%;margin-left:20%;">
          <router-link to="/changePhone">
            <img src="static/img/updatephone.png" style="width:0.5rem;height:0.28rem" alt="">
          </router-link>
        </div>
      </mt-cell>

      <mt-cell title="微信号" style="right:2%;margin-left:0.03rem">
        <input type="text" :value="this.user.wechat_account"   style="position:relative;outline: none;border: medium;
           text-align:right;color:#656B79;left:-18%;height:0.4rem"  @change="save">
        <!-- <input  @change="save" class="addre" type="text" v-model="user.wechat_account" placeholder="请输入微信号"
           style="position:relative;outline: none;border: medium;
           text-align:right;color:#656B79;left:-7%;height:0.4rem"> -->
      </mt-cell>

      <mt-cell title="是否公开微信号" style="padding-bottom:0.2rem;right:3%;margin-left:0.06rem">
        <mt-switch v-model="user.is_public_wx" @change="save"></mt-switch>
      </mt-cell>

    </div>
</template>

<script>
import  global from '../../../src/components/common/Global.vue'
import calendar from '../common/calendar.vue'
import { Datetime, Group, XButton } from 'vux'
import {
  mapState,
} from 'vuex';

  export default {
      components: {
        calendar,
        Datetime,
        Group,
        XButton
    },
    computed: {
      ...mapState({
        member_id: state => state.member_id,
      }),
    },
    data () {
      return {
         user: {
          },
         value: null,
        value1: '',
        //show: true,
         startDate: '1970-01-01',
        brithday:'9月29日',
        title:'会员中心',
        closeButton:false,

         calendar3:{
                display:'',
                show:false,
                zero:true,
                value:[1990,11,2], //默认日期
                lunar:true, //显示农历
                select:(value)=>{
                    this.calendar3.show=false;
                    this.calendar3.value=value;
                    this.calendar3.display=value.join("/");
                }
            },
      }
    },
     async mounted () {
       let { data } = await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)
        this.user = data;

      var unixTimestamp = new Date(this.user.birthday)
      var year = unixTimestamp.getFullYear();
      var month = unixTimestamp.getMonth() + 1;
      var date = unixTimestamp.getDate();
      this.value1 = year+"-"+month+"-"+date
      this.$emit('title',this.title,this.closeButton);
      if(!this.user.interest){
          this.user.interest =0;
      }
      },
    methods: {
      showPlugin () {
        this.$vux.datetime.show({
          placeholder:'请选择生日',
          cancelText: '取消',
          confirmText: '确定',
          format: 'YYYY-MM-DD',
          value: '2017-05-20',
          onConfirm (val) {
            console.log('plugin confirm', val)
          },
          onShow () {
            console.log('plugin show')
          },
          onHide () {
            console.log('plugin hide')
          }
        })
      },
      change (value) {
        console.log('change', value)
      },
       save(value){
        let is_public_wx = this.user.is_public_wx ? true: false
        var time=new Date(value);
        this.$http.put(`/api/member`,{
          'address':document.getElementById("address").value,
          'birthday':time.getTime(),
          'name':document.getElementById("name").value,
          'degreeOfEducation':this.user.degree_of_education,
          'enablePublicWa':is_public_wx,
          'incomeRange':this.user.income_range,
          'interest':this.user.interest,
          'memberId':this.user.member_id,
          'occupation':this.user.occupation,
          'sex':this.user.sex,

        }).then(data =>{
                //保存cookie(设置1年有效期);
                 setCookie('member_id',this.user.memberId,365);
                this.$toast({
                    message:'更新成功',
                    possition:'top',
                 });
                 setTimeout(() => {
                     this.$router.push('/member');
                  },2000)
        }
        ,err=>{
                // this.$toast({
                //     message:'更新失败',
                //     possition:'top',
                //  });
                // setTimeout(() => {
                //   this.$router.push('/member');
                // }, 2000);
            });
       },
      openByDrop(e){
            this.calendar3.show=true;
            this.calendar3.left=e.target.offsetLeft+19;
            this.calendar3.top=e.target.offsetTop+70;

            e.stopPropagation();
            window.setTimeout(()=>{
                document.addEventListener("click",(e)=>{
                    this.calendar3.show=false;
                    document.removeEventListener("click",()=>{},false);
                },false);
            },1000)
        },
        openByDialog(){
            this.calendar4.show=true;
        },
        closeByDialog(){
            this.calendar4.show=false;
        },
        changeEvents(){
            this.calendar1.events={
                '2017-7-20':'$'+(Math.random()*1000>>0),
                '2017-7-21':'$'+(Math.random()*1000>>0),
                '2017-7-22':'$'+(Math.random()*1000>>0),
            }
        },
      open(picker) {
        this.$refs[picker].open();
      },
      handleChange(value) {
        this.brithday=`${value.getMonth()+1}月${value.getDate()}日`;
      },
    }
  }
</script>

<style lang="less" scoped>

  div{
      margin-top: 0.17rem;
      background-color: #fff;

  }
 /* border:none;
  outline:medium;*/
  input{
   font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif;
    font-size: 0.16rem;
    color: #656B79;
  }
  .addre:-ms-input-placeholder{
     color: #656B79;
  }
  select{
   font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif;
    font-size: 0.16rem;
    border: medium;
    background-color: #fff;
    appearance:none;
    -moz-appearance:none;
    -webkit-appearance:none;
    outline: none;
    color: #656B79;
  }
  button{
    font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif;
    font-size: 0.16rem;
    background-color: #fff;
    box-shadow: none;
  }
  .mint-cell-wrapper:first{
    background-image: none;
  }
</style>
