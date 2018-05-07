<template>
   <div >
           <figure>
                  <router-link to="/userInfo"><img src="static/img/userinfo.png"/>
                  <figcaption>个人信息</figcaption></router-link>
           </figure>

           <figure>
                 <router-link to="/memberLevel"><img src="static/img/level.png"/>
                   <figcaption>我的等级</figcaption></router-link>
                 <!--<img class="active" src="static/img/point.png" alt="">-->
           </figure>

           <figure>
             <router-link to="/memActivities">
                 <img src="static/img/activity.png"/>
                 <figcaption>我的活动</figcaption></router-link>
           </figure>
           <figure>
             <router-link to="/memVoucher">
             <img src="static/img/certificate.png"/>  
             <figcaption>我的券</figcaption></router-link>
           </figure>
   </div>
</template>
<script>


</script>
<style lang="less" scoped>
   div{
      display: flex;
      height:0.55rem;
      padding: 0.05rem;
      margin-bottom:0.12rem;
     background-color: #fff;
   }
   figure{
      flex-grow: 1;
      flex-shrink:1;
      text-align:center;
      border-right:1px solid #e1e1e1;
   }
   figure:nth-child(4){
     border-right: none;
    //margin-top: 0.02rem;
   }
   figure:nth-child(4) img{
     top:-.55rem;
     left:18%;

   }
   figure:nth-child(3) img{
     top:-.55rem;
     left:8.8%;
   }
   figure:nth-child(2) img{
     top:-.55rem;
     left:12%;
   }

   figure img{
     height: 0.25rem;
     width:  0.25rem;
     display:inline ;
   }
   figcaption{
      font-size: 0.12rem;
   }
   .active{
      width: 0.05rem;
      height: 0.05rem;
      position: relative;
      top:-.52rem;
      left:17%;
   }
</style>
