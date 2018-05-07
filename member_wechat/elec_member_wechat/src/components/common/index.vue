<template>
  <div>
         <mt-field label="新手机号" placeholder="请输入手机号"></mt-field>

         <mt-field label="验证码"   placeholder="请输入验证码">
             <button v-show="show" @click="getCode(formData)" class="code">获取验证码</button>
             <button v-show="!show" class="code">{{count}} s</button>
         </mt-field>
    <router-link to="/changeSuccess"><mt-button size="large" type="primary">绑定</mt-button></router-link>

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
          show: true,
          count: '',
          timer: null,
          title:'绑定',
          closeButton:false
        }
      },
    mounted:function () {
      this.$emit('title',this.title,this.closeButton);
    },
   // const TIME_COUNT = 60;
  methods:{
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
</style>
