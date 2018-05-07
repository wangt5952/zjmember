<template>
  <div>
    <mt-field label="姓名" placeholder="请输入姓名"></mt-field>
    <mt-field label="紧急电话" placeholder="请输入联系电话"></mt-field>
    <mt-cell title="类别">
            <label class="radio radio3" v-if="android==true"><input  v-model="selected"    value="1" name="radio"  type="radio">工作人员</label>
            <label class="radio radio1" v-else><input name="radio"   v-model="selected"   value="1"  type="radio">工作人员</label>
            <label class="radio radio4" v-if="android==true"><input  v-model="selected"   value="2" name="radio" type="radio">商户</label>
            <label class="radio radio2" v-else><input name="radio"   v-model="selected"   value="2" type="radio">商户</label>
    </mt-cell>
      <mt-cell title="部门" is-link>
        <select>
            <option value="1">招商部</option>
            <option value="2">客服中心</option>
            <option value="3">营运部</option>
            <option value="4">企划部</option>
        </select>
      </mt-cell>

      <div v-show="selected==2">
           <mt-field label="用户名" placeholder="请输入用户名"></mt-field>
           <mt-field label="密码" placeholder="请输入密码"></mt-field>
      </div>

    <router-link to="/staRegSuccess"><mt-button size="large" type="primary">注册</mt-button></router-link>
  </div>
</template>
<script>
  export default {
    data(){
        return {
            formData: {
              phone:'',
              code:'',
            },
           selected:[],
           value1: null,
            show: true,
            count: '',
            timer: null,
            startDate: new Date('1970-1-1'),
            endDate: new Date(),
            brithday:'请选择日期',
            android:false,
        }
    },
     mounted:function () {
        let u = navigator.userAgent;
        let isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
          if(isAndroid){
              this.android=true;
          }

     },
      methods:{
        open(picker) {
          this.$refs[picker].open();
        },
        handleChange(value) {
          this.brithday=`${value.getFullYear()+1}年${value.getMonth()+1}月${value.getDate()}日`;
        },
        getCode(formData){
          if (!this.timer) {
            this.count = 60;
            this.show = false;
            this.timer = setInterval(() => {
              if (this.count > 0 && this.count <= 60) {
                this.count--;
              } else {
                this.show = true;
                clearInterval(this.timer);
                this.timer = null;
              }
            }, 1000)
          }
        },
      }
  }
</script>
<style lang="less" scoped>
  div{
    max-width: 640px;
    margin-top: 0.12rem;
  }
  mint-cell{
    color: #06c1ae;
    display: inline-block;
  }
  .radio{
     cursor: pointer;


  }
  .radio input{
    width: 0.2rem;
    height: 0.2rem;

  }
  .radio1{
     position: absolute;
     left: 27%;
     top:0.1rem;
  }
  .radio2{
    position: absolute;
    left: 51%;
    top:0.1rem;
  }
  .radio3{
    position: absolute;
    left: 31%;
    top:0.1rem;
  }
  .radio4{
    position: absolute;
    left: 55%;
    top:0.1rem;

  }

  .mint-button--primary{
    width: 90%;
    margin:0.13rem auto;
    background-color:#06c1ae ;
  }
  .mint-cell{
    color: #06c1ae;
  }
  .mint-field-core{
      margin-right: 20px;
  }
  .mint-cell-value{
    margin-right: 100px;
  }
  .mint-button--small{
   background-color: #fff;
    box-shadow: none;
  }
  input{
    font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif;
    font-size: 0.16rem;
    color: #656B79;
  }

  select{
    font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif;
    font-size: 0.16rem;
    border: medium;
    background-color: #fff;
    -moz-appearance:none;
    -webkit-appearance:none;
    outline: none;
    color: #656B79;
    position: absolute;
    left:1.2rem;
    top :0.1rem;

  }


</style>
