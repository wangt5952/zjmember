
<template>
  <div>
    <mt-field label="姓名" :placeholder="namePl" :state="nameSta" v-model="formData.name"></mt-field>
       <!--  <label class="radio radio1"><input name="radio" type="radio"><span></span>男</label>
         <label class="radio radio2"><input name="radio" type="radio"><span></span>女</label>-->
    <mt-cell title="性别">
                <label class="radio radio3" v-if="android==true"><input name="radio" value="0"  v-model="formData.sex"   type="radio">男</label>
                <label class="radio radio1" v-else><input name="radio" v-model="formData.sex"  type="radio" value="0">男</label>
                <label class="radio radio4" v-if="android==true"><input v-model="formData.sex"  name="radio" value="1" type="radio">女</label>
                <label class="radio radio2" v-else><input name="radio" v-model="formData.sex"  type="radio" value="1">女</label>
    </mt-cell>
    <mt-cell title="生日">
            <input type="text" @click="openByDrop($event)" v-model="calendar3.display" readonly style='position:absolute;left:26%;overflow:auto; background-attachment: fixed; background-repeat: no-repeat; border-style: solid;
border-color: #FFFFFF;color:#888888'>
    </mt-cell>
     <transition name="fade">
    <div class="calendar-dropdown"  v-if="calendar3.show" style="float:left;position:absolute;z-index:100;width:100%">
        <calendar :zero="calendar3.zero" :lunar="calendar3.lunar" :value="calendar3.value" :begin="calendar3.begin" :end="calendar3.end" @select="calendar3.select"></calendar>
    </div>
    </transition>
   <!-- <router-link to="/registerSucc"></router-link>-->
    <mt-button size="large" type="primary" @click="subInfo">绑定</mt-button>

  </div>
</template>
<script>
  import calendar from '../common/calendar.vue'
  import  {setCookie} from '../../../src/util/util'
  import  {getCookie} from '../../../src/util/util'
  export default {
     name: 'app',
    components: {
        calendar
    },
    data(){

        return {
            namePl:'请输入姓名',
            nameSta:'',
            birthdatySta:'',
            mall_id:1,
            formData: {
              phone:'',
              code:'',
              name:'',
              sex:'',
              birthday:'',
              openId:12345678,
            },
            calendar3:{
                display:"请选择日期",
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
            value1: null,
            show: true,
            count: '',
            timer: null,
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
          let { phone } = this.$route.query
          this.formData.phone = phone;
     },
      methods:{
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
        getParam(name) {
             let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
             let r = window.location.search.substr(1).match(reg);
             if(r != null) return unescape(r[2]);
             return null;
        },
        open(picker) {
             this.$refs[picker].open();
        },
        handleChange(value) {

          this.formData.birthday=Date.parse(value)/1000;
          this.brithday=`${value.getFullYear()}年${value.getMonth()+1}月${value.getDate()}日`;
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

          //提交注册资料
          async subInfo(){
            if(this.formData.name==''){
              this.namePl='姓名不能为空';
              this.nameSta='error';
              return false;
            }else {
              this.nameSta='success';
            }
            if(this.formData.sex==''){
              this.$toast({
                message:'必须选择性别',
                possition:'top',
              });
              return false;
            }
            if(this.calendar3.display=="请选择日期" || !this.calendar3.display || this.calendar3.display=="生日不能为空" ){
              this.birthdatySta='error';
              this.calendar3.display='生日不能为空';
              return false;
            }else{
              this.birthdatySta='success';
            }
            var a = new Date(this.calendar3.display);

            this.$http.post('/api/member',{'birthday':a.getTime(),'mallId':this.mall_id,'mobile':this.formData.phone,'name':this.formData.name,'sex':this.formData.sex,'openId':this.formData.openId}).then(data =>{
              const { member_id } = data.data;
              if(member_id){
                //保存cookie(设置1年有效期)

                this.$store.commit('login', member_id)
                this.$router.push('/registerSucc');
                 setTimeout(() => {
                     this.$router.push('/member');
                  },2000)
              }
            },err=>{
                this.$toast({
                      message:err.response.data.content,
                      possition:'middle'
                 });
                setTimeout(() => {
                  this.$router.push('/register');
                }, 2000);
            });
          }
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
    left: 42%;
    top:0.1rem;
  }
  .radio3{
    position: absolute;
    left: 26%;
    top:0.1rem;
  }
  .radio4{
    position: absolute;
    left: 45%;
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

/*弹出框*/
.calendar-dialog{
    position: absolute;
    left:0;
    top:0;
    right:0;
    bottom:0;
}
.flex{
    box-sizing: border-box;

    display: -webkit-box;
    -webkit-box-pack: start;
    -webkit-box-align: start;

    display: -webkit-flex;
    -webkit-justify-content: space-between;
    -webkit-align-items: top;

    display: flex;
    justify-content: space-between;
    align-items: top;
    flex-flow:row wrap
}
.flex>div{
    margin:10px;
    padding:20px;
    width:25%;
    min-width:300px;
    border: 1px solid #eee;
    border-radius: 2px;
    position: relative;
}
.flex>div>span{
    position: absolute;
    left:0px;
    top:0px;
    padding:5px 10px;
    font-family: "PingFang SC","Hiragino Sans GB","STHeiti","Microsoft YaHei","WenQuanYi Micro Hei",sans-serif;
    font-size:10px;
    border-radius:0 0 2px 0;
    background:#ea6151;
    color:#fff;
}
.flex>div>input{
    box-sizing: border-box;
    width:100%;
    margin-top:20px;
    border-radius: 2px;
    border:1px solid #dedede;
    padding:10px;
    font-size: 16px;
    background:url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/PjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48ZGVmcz48c3R5bGUgdHlwZT0idGV4dC9jc3MiPjwhW0NEQVRBWwpAZm9udC1mYWNlIHsgZm9udC1mYW1pbHk6IGlmb250OyBzcmM6IHVybCgiLy9hdC5hbGljZG4uY29tL3QvZm9udF8xNDQyMzczODk2XzQ3NTQ0NTUuZW90PyNpZWZpeCIpIGZvcm1hdCgiZW1iZWRkZWQtb3BlbnR5cGUiKSwgdXJsKCIvL2F0LmFsaWNkbi5jb20vdC9mb250XzE0NDIzNzM4OTZfNDc1NDQ1NS53b2ZmIikgZm9ybWF0KCJ3b2ZmIiksIHVybCgiLy9hdC5hbGljZG4uY29tL3QvZm9udF8xNDQyMzczODk2XzQ3NTQ0NTUudHRmIikgZm9ybWF0KCJ0cnVldHlwZSIpLCB1cmwoIi8vYXQuYWxpY2RuLmNvbS90L2ZvbnRfMTQ0MjM3Mzg5Nl80NzU0NDU1LnN2ZyNpZm9udCIpIGZvcm1hdCgic3ZnIik7IH0KCl1dPjwvc3R5bGU+PC9kZWZzPjxnIGNsYXNzPSJ0cmFuc2Zvcm0tZ3JvdXAiPjxnIHRyYW5zZm9ybT0ic2NhbGUoMC4wMTU2MjUsIDAuMDE1NjI1KSI+PHBhdGggZD0iTTcxMS4zMDYyIDI5MC42OTcyYzI0LjI4MjEgMCA0NS4zNzY1LTE5LjcwNjkgNDUuMzc2NS00NC4wMzJWNDYuNTYwMjU1OTk5OTk5OTk1YzAtMjQuMzI1MS0yMS4wOTU0LTQ0LjA1MzUtNDUuMzc2NS00NC4wNTM1LTI0LjMwMjYgMC00My45ODggMTkuNzI4NC00My45ODggNDQuMDUzNXYyMDAuMTA0OTZDNjY3LjMxODMgMjcwLjk5MDMgNjg3LjAwMzYgMjkwLjY5NzIgNzExLjMwNjIgMjkwLjY5NzJ6TTYyMy40ODA4IDExMy40MjAzSDQwMC43NjQ5Mjh2NjYuNTEzOTJoMjIyLjcxNTkwNDAwMDAwMDAyVjExMy40MjAyODh6TTg4NC4wNTMgMTEzLjQyMDNoLTgyLjc3NDAxNnY2Ni4xNDUyOGg4NS45NDAyMjRjMjUuMjc4NSAwIDQ2LjYxMTUgMjEuMzc2IDQ2LjYxMTUgNDYuNjc3djE1My45Mjc2OEg5MC40Mzg2NTYwMDAwMDAwMXYtMTUzLjkyNzY4YzAtMjUuMyAyMS4zMzMtNDYuNjc3IDQ2LjYxMTUtNDYuNjc3aDg2LjUwMzQyNFYxMTMuNDIwMjg4aC04Mi42NDI5NDRjLTY0LjA4NiAwLTExNi41MDc2IDUyLjUwODctMTE2LjUwNzYgMTE2LjcwMzJ2Njc2LjgwMTUzNTk5OTk5OTljMCA2NC4xNzQxIDUwLjQ5MTQgMTE2LjY4MDcgMTE0LjU3NzQgMTE2LjY4MDdIODg0LjA1Mjk5MmM2NC4wNjI1IDAgMTE2LjUwNjYtNTIuNTA2NiAxMTYuNTA2Ni0xMTYuNjgwN1YyMzAuMTIzNTE5OTk5OTk5OThDMTAwMC41NTk2IDE2NS45MjkgOTQ4LjExNDQgMTEzLjQyMDMgODg0LjA1MyAxMTMuNDIwM3pNOTMzLjgyOTYgOTEwLjM1MTRjMCAyNS4zLTIxLjMzMyA0Ni42NzYtNDYuNjExNSA0Ni42NzZIMTM3LjA1MDExMTk5OTk5OTk4Yy0yNS4yNzg1IDAtNDYuNjExNS0yMS4zNzYtNDYuNjExNS00Ni42NzZWNDQ2LjQ0NTU2OEg5MzMuODI5NjMyVjkxMC4zNTEzNnpNMjY3LjEwODQgNjQ2LjE4MTljMzYuODc3MyAwIDY2Ljc1MjUtMjkuOTM5NyA2Ni43NTI1LTY2Ljg4MTUgMC0zNi45MjI0LTI5Ljg3NTItNjYuODYxMS02Ni43NTI1LTY2Ljg2MTEtMzYuODU0OCAwLTY2Ljc1MjUgMjkuOTM5Ny02Ni43NTI1IDY2Ljg2MTFDMjAwLjM1NTggNjE2LjI0MjIgMjMwLjI1MjUgNjQ2LjE4MTkgMjY3LjEwODQgNjQ2LjE4MTl6TTUxMS41NDg0IDY0Ni4xODE5YzM2Ljg1NTggMCA2Ni43NTI1LTI5LjkzOTcgNjYuNzUyNS02Ni44ODE1IDAtMzYuOTIyNC0yOS44OTU3LTY2Ljg2MTEtNjYuNzUyNS02Ni44NjExLTM2Ljg3NzMgMC02Ni43NTI1IDI5LjkzOTctNjYuNzUyNSA2Ni44NjExQzQ0NC43OTU5IDYxNi4yNDIyIDQ3NC42NzExIDY0Ni4xODE5IDUxMS41NDg0IDY0Ni4xODE5ek0yNjUuOTE2NCA4OTAuNzA5YzM2Ljg3NzMgMCA2Ni43NTE1LTI5LjkzOTcgNjYuNzUxNS02Ni44NjExIDAtMzYuOTQyOC0yOS44NzQyLTY2Ljg4MjYtNjYuNzUxNS02Ni44ODI2LTM2Ljg1NTggMC02Ni43NTI1IDI5LjkzOTctNjYuNzUyNSA2Ni44ODI2QzE5OS4xNjM5IDg2MC43NjkzIDIyOS4wNTk2IDg5MC43MDkgMjY1LjkxNjQgODkwLjcwOXpNNTExLjU0ODQgODkwLjcwOWMzNi44NTU4IDAgNjYuNzUyNS0yOS45Mzk3IDY2Ljc1MjUtNjYuODYxMSAwLTM2Ljk0MjgtMjkuODk1Ny02Ni44ODI2LTY2Ljc1MjUtNjYuODgyNi0zNi44NzczIDAtNjYuNzUyNSAyOS45Mzk3LTY2Ljc1MjUgNjYuODgyNkM0NDQuNzk1OSA4NjAuNzY5MyA0NzQuNjcxMSA4OTAuNzA5IDUxMS41NDg0IDg5MC43MDl6TTc1NS42NDEzIDY0Ni4xODE5YzM2Ljg1NjggMCA2Ni43NTM1LTI5LjkzOTcgNjYuNzUzNS02Ni44ODE1IDAtMzYuOTIyNC0yOS44OTY3LTY2Ljg2MTEtNjYuNzUzNS02Ni44NjExLTM2Ljg3NzMgMC02Ni43NTI1IDI5LjkzOTctNjYuNzUyNSA2Ni44NjExQzY4OC44ODk5IDYxNi4yNDIyIDcxOC43NjQgNjQ2LjE4MTkgNzU1LjY0MTMgNjQ2LjE4MTl6TTMxMS43MDM2IDI5MC42OTcyYzI0LjI4MTEgMCA0NS4zNzY1LTE5LjcwNjkgNDUuMzc2NS00NC4wMzJWNDYuNTYwMjU1OTk5OTk5OTk1YzAtMjQuMzI1MS0yMS4wOTQ0LTQ0LjA1MzUtNDUuMzc2NS00NC4wNTM1LTI0LjMwMTYgMC00My45ODkgMTkuNzI4NC00My45ODkgNDQuMDUzNXYyMDAuMTA0OTZDMjY3LjcxNDYgMjcwLjk5MDMgMjg3LjQwMiAyOTAuNjk3MiAzMTEuNzAzNiAyOTAuNjk3MnoiIGZpbGw9IiM1ZTdhODgiPjwvcGF0aD48L2c+PC9nPjwvc3ZnPg==) no-repeat 8px 10px;
        padding-left: 36px;
        color:#666;
}/*transition*/
.fade-enter-active,
.fade-leave-active {
    transition: all .5s ease-in-out;
}
.fade-enter,.fade-leave-active{
    opacity: 0;
    transform: translateY(-10px);

}
#time{
  font-family: "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei UI", "Microsoft YaHei", SimHei, "\5B8B\4F53", simsun, sans-serif
}


</style>

<style lang="less">
input.mint-field-core{
  position: absolute;
  left: 27%;
}
</style>
