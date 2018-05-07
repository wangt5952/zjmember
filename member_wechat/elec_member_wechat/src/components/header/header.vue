<template>
    <div >
      <div style="width:100%; border-bottom:1px solid #D6D6D6">
        <div style="width:30%;margin-left:0.3rem ;border-bottom:1px solid #D6D6D6">
          <img src="static/img/member.png" alt="">
         </div>
         <div style="width:70% ;border-bottom:1px solid #D6D6D6">
          <ul>
            <li><span> {{this.user.name}}</span><h3>{{this.user.level}}</h3></li>
            <li>累计积分:{{this.user.cumulate_points}}</li>
            <li>可用积分:{{this.user.usable_points}}</li>
          </ul>
         </div>
      </div>
    </div>
</template>
<script>
import  global from '../../../src/components/common/Global.vue'
import {
  mapState,
} from 'vuex';

   export  default {
     data(){
        return {
          user: {
          }
        }
      },
      computed: {
        ...mapState({
          member_id: state => state.member_id,
        }),
      },
      async mounted () {

        let { data } = await this.$http.get(`/api/member/${this.member_id}?mallId=${global.mallId}`)
        this.user=data;
        console.log(this.user)
      },
   }

</script>
<style lang="less" scoped>
     div{
        border-top: 0.5px solid #e1e1e1;
        height: 120px;
        display: flex;
        background-color: #fff;
        z-index: 3;
     }
     img{
       border-radius: 50%;
       height: 0.8rem;
       width: 0.8rem;
       margin:17px 0 0 5%;
     }
    ul{
        margin:8% 0 0 3%;
    }
    ul li{
        color:#999;
        font-size: 0.12rem;

    }
    li:nth-child(2),li:nth-child(3){
      margin-left:0.01rem;

    }
     li:nth-child(3){
       margin-top: 3px;

     }
   ul li span{
        font-size: 0.22rem;
        font-weight:600;
        margin-right: 5px;
        color: #333;
   }
   ul li h3{
        display:inline-block;
        background-color: #76dace;
        font-size:0.1rem ;
        padding: 0.01rem 0.03rem 0.01rem 0.03rem;
        border-radius:0.02rem;
        color:#fff;
        position: relative;
        top:-.035rem;
   }
   .rank{
     height: 0.2rem;
     background-color: #E6FAF4;
     display:block;
     width: 0.75rem;
     border:none;
     border-top-left-radius: 0.1rem;
     border-bottom-left-radius: 0.1rem;
   }
     .rank img:nth-child(1){
       height: 0.2rem;
       width: 0.2rem;
       float: left;
       margin:0;
     }

     .rank img:nth-child(3){
       display: inline-block;
       height: 0.1rem;
       width: 0.1rem;
       position: relative;
       bottom:0.7em;
    }
  .rank a{
    display: inline-block;
    font-size: 0.1rem;
    position: relative;
    left:0.02rem;
    bottom:0.12rem;
    color:rgba(6,193,174,50);
  }
</style>
