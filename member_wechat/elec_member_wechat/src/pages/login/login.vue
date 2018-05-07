<template>
    <div>
          <mt-field label="手机号"  type="tel"  :state="state"  v-model="formData.phone" :placeholder="placeholder"></mt-field>

          <mt-field label="验证码"   :placeholder="formData.v_code"  v-model="formData.code"   :state="c_state">
            <button v-show="show" @click="getCode(formData)" class="code">获取验证码</button>
            <button v-show="!show" class="code">{{count}} s</button>
          </mt-field>
          <div class="buttonGroup">
                <mt-button size="large" @click="valid" type="primary">登陆</mt-button>
                <mt-button size="large" @click="regis" type="primary">注册</mt-button>
          </div>
    </div>

</template>

<script>
  import  {setCookie} from '../../../src/util/util'
  export default {
    data(){
      return {
        formData: {
          phone:'',
          code:'',
          v_code:'请输入验证码',
        },
        placeholder:'请输入手机号',
        state:'',
        c_state:'',
        show: true,
        count: '',
        timer: null,
        title:'绑定',
        closeButton:false
      }
    },
    watch:{
      phone(curVal,oldVal){
        console.log(curVal,oldVal);
      }
    },
    mounted:function () {
      let { wx_openid } = this.$route.query
      this.$emit('title',this.title,this.closeButton);
    },
    // const TIME_COUNT = 60;
    methods:{
      handleFocus(){

        alert('123');

      },
      regis(){
           this.$router.push('/register');
      },
      //发送请求
      valid(){
         if(this.checkMobile(this.formData.phone)){
             console.log('123');
          if(this.formData.code==''){
            console.log('123');
            this.formData.v_code= '请先获取验证码';
            this.c_state = 'error';
          }else{
            //手机号验证码都验证通过,验证验证码是否正确
            alert(123)
            this.$http.post('/api/member/vcodeCheck',{'mobile':this.formData.phone,'vcode':this.formData.code }).then(data =>{
                  alert(4)
                  //验证成功(查询用户信息) //window.location.href='/registerInfo?phone='+this.formData.phone;
                  this.$http.get(`/api/member/m?mobile=${this.formData.phone}`).then(resp=>{
                      //设置cookie
                      alert(1234)
                      let data = resp.data;
                      setCookie('member_id',data.member_id,365);
                      this.$router.push('/member');
                  },err=>{

                  });
            },err=>{
                  this.formData.code='';
                  this.formData.v_code= '验证失败';
                  this.c_state = 'error';
            });
          }
        }else{
                this.show = true;
                clearInterval(this.timer);
                this.timer = null;
                this.state='error';
                this.placeholder='请输入正确的手机号';
        }
      },
      //验证手机号
      checkMobile(s){
        let length = s.length;
        if(length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1})|)+\d{8})$/.test(s) )
        {
          return true;
        }else{
          return false;
        }
      },
      getCode(formData){
        if (!this.timer) {
          this.count = 60;
          this.show = false;
          if(this.checkMobile(this.formData.phone)){
            this.state='success';
            this.$http.post('/api/member/vcode',{'mallId':1,'mobile': formData.phone}).then(data =>{
              console.log(data);
            });
            this.timer = setInterval(() => {
              if (this.count > 0 && this.count <= 60) {
                this.count--;
              } else {
                this.show = true;
                clearInterval(this.timer);
                this.timer = null;
              }
            }, 1000);
          }else{
            this.show = true;
            clearInterval(this.timer);
            this.timer = null;
            this.state='error';
            this.placeholder='请输入正确的手机号';
          }
          /*   this.$http.post('/api/member/vcode',{'mallId':10,'mobile': formData.phone}).then(data =>{
                  console.log(data);
             });*/
        }
      }
    }
  }
</script>
<style lang="less" scoped>
    div{
      margin-top:0.5rem;
    }
    .code{
      height: 0.8rem;
      width:3rem ;
      background-color: #7DD8CF;
      border-radius:0.04rem;
      border:0;
      outline: 0;
      overflow:hidden;
      text-align: center;
      color: #fff;
      opacity:.8;
    }
    .mint-cell{
      color: #06c1ae;
    }
    .buttonGroup{
       display: flex;
    }
    .mint-button--primary{
      width: 40%;
      margin:0.13rem auto;
      background-color:#06c1ae ;
    }
</style>
