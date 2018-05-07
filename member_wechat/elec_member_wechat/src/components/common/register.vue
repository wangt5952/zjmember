<template>
  <div style="position:fixed;left:0;right:0;" >
         <mt-field label="手机号"  type="tel"  :state="state"  v-model="formData.phone" :placeholder="placeholder" ></mt-field>
         <mt-field label="验证码"   :placeholder="formData.v_code"  v-model="formData.code"   :state="c_state" >
             <button v-show="show" @click="getCode(formData)" class="code">获取验证码</button>
             <button v-show="!show" class="code">{{count}} s</button>
         </mt-field>
           <mt-button size="large" @click="valid" type="primary"><slot>下一步</slot></mt-button>

        <!-- <router-link to="/registerInfo"></router-link>-->

  </div>
</template>
<script>
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
      this.$emit('title',this.title,this.closeButton);
    },
   // const TIME_COUNT = 60;
  methods:{
    handleFocus(){

       alert('123');

    },
    //发送请求
    valid(){
      if(this.checkMobile(this.formData.phone)){
            if(this.formData.code==''){
                  this.formData.v_code= '请先获取验证码';
                  this.c_state = 'error';
            }else if(this.formData.code.length<6){
                   this.formData.v_code= '请输入正确的验证码';
                  this.c_state = 'error';
            }else{
                //  手机号验证码都验证通过,验证验证码是否正确
                this.$http.post('/api/member/vcodeCheck',{'mobile':this.formData.phone,'vcode':this.formData.code }).then(data =>{
                    //  this.$store.commit('login', this.formData.phone)
                     //验证成功
                    let defautlHref = '/registerInfo?phone='+this.formData.phone;
                    // let { redirect=defautlHref } = this.$route.query
                    this.$router.push(defautlHref)
              },err=>{
                  this.formData.code='';
                  this.formData.v_code= '验证失败';
                  this.c_state = 'error';
                  window.location.href='/register';
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
    async getCode(formData){
      if (!this.timer) {
        this.count = 60;
        this.show = false;
         if(this.checkMobile(this.formData.phone)){
             this.state='success';
             try {
               var data=await this.$http.get(`/api/member/m?mobile=${formData.phone}`);
                alert('该手机已注册');
                location.reload()
                return
             } catch (error) {
             }
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
             return;
         }
        this.$http.post('/api/member/vcode',{'mallId':10,'mobile': formData.phone}).then(data =>{
             console.log(data);
        });
      }
    }
   }
  }
</script>


<style lang="less" scoped>
    div{
       margin-top:0.13rem;
    }
   .code{
      height: 0.35rem;
      width:0.9rem ;
      background-color: #7DD8CF;
      border-radius:0.04rem;
      border:0;
      outline: 0;
      overflow:hidden;
      text-align: center;
      color: #fff;
      opacity:.8;
      margin-right: 0.07rem;
   }
  .mint-cell{
    color: #06c1ae;
  }
   .mint-button--primary{
      width: 90%;
      margin:0.13rem auto;
      background-color:#06c1ae ;
   }
   .mint-field-core{
     width: 1.5rem !important
   }
   .mint-field-core{
     width: 0.8rem !important
   }
</style>
