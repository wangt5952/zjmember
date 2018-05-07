<template>
   <div>
         <p>温馨提示:&nbsp;&nbsp;1&nbsp;&nbsp;小票有效期有三个月,请及时上传<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2&nbsp;&nbsp;后台人员尽快为您录入,如有问题<br>&nbsp;&nbsp;&nbsp;可电话:<a @click="phone">{{tel}}</a></p>
   </div>
</template>


<script>
   export default {
         data(){
             return{
               tel:'021-2768658',
             }
         },
         methods:{
           phone(){
             let telPhone = this.tel;
             window.location.href=`tel:${telPhone}`
           }
         }
   }
</script>

<style lang="less" scoped>
  div{
    text-align: center;
  }
  div p {
    color: #666;
    font-size: 0.14rem;
    line-height: 0.25rem;

  }
  div a{
    color:rgba(6,193,174,50);

  }

</style>
