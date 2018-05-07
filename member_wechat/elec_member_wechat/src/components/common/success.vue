<template>
  <div>
      <figure>
          <img src="static/img/smile.png" alt=""/>
          <figcaption>
               <slot></slot>
          </figcaption>
      </figure>
      <slot name="staff"></slot>
  </div>
</template>

<style lang="less" scoped>
  div{
    margin-top: 0.12rem;
    border-top: 0.5px solid #e1e1e1;
    border-bottom: 0.5px solid #e1e1e1;
    line-height: 1.2rem;
    background-color: #fff;
    text-align: center;
  }
  figure{

  }
  figure figcaption{
    color:#06c1ae;
    text-align: center;
    font-size: 0.4rem;
  }
  img{
    height: 3rem;
    width: 3rem;
    margin: 10px auto;
  }
  .staff{
    color: #333;
    font-size: 0.3rem;
    position: relative;
  }

</style>

<script>



</script>
