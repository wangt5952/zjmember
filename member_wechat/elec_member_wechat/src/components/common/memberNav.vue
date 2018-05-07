<template>
  <div style="margin-top:0.1rem">
    <ul style="padding-bottom:0.2rem">
       <li>
            <router-link to="/voucher" >
             <img src="static/img/voucher.png" alt="">
               <a>优惠券</a>
                <img class="right" src="static/img/nr.png" alt="">
                </router-link>
       </li>

        <li>
             <router-link to="/activities">
              <img src="static/img/latestactive.png" alt="">
              <a>最新活动</a>
              <img class="right" src="static/img/nr.png" alt="">
              </router-link>
        </li>

      <li>
        <router-link to="/car">
        <img src="static/img/stopcar.png" alt="">
        <a>停车缴费</a>
        <img class="right" src="static/img/nr.png" alt="">
        </router-link>
      </li>

      <li>
         <router-link to="">
        <img src="static/img/cash.png" alt="">
        <a>积分抵现</a>
        <img class="right" src="static/img/nr.png" alt="">
        </router-link>
      </li>

      <li>
        <router-link to="/ticketsUpload">
        <img src="static/img/upload.png" alt="">
        <a>小票上传</a>
        <img class="right" src="static/img/nr.png" alt="">
        </router-link>
      </li>

      <li>
         <router-link to="">
        <img src="static/img/record.png" alt="">
        <a>商户记录</a>
        <img class="right" src="static/img/nr.png" alt="">
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>



</script>


<style lang="less" scoped>
  div{
    background-color: #fff;
  }
  ul li{
    height: 0.5rem;
    line-height: 0.5rem;
    border-bottom: 1px solid #e1e1e1;
    display: flex
  }
  li img{
     height:0.25rem;
     width:0.25rem;
     float: left;
     margin:0.14rem 0 0  0;
     position: relative

  }
  li a{
      font-size: 0.14rem;
      position: relative;
      width: 90%;
      margin-left: 20px;
  }
  .right{
    float: right;
    width: 0.08rem;
    height: 0.16rem;
    margin:0.16rem 0 0 0;
  }
</style>
