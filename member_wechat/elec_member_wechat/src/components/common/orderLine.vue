<template>
    <div class="order">
        <span class="line"></span>
        <span class="txt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<slot></slot>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
        <span class="line"></span>
    </div>
</template>
<script>



</script>

<style lang="less" scoped>
  .order {
    height: 0.3rem;
    line-height: 0.3rem;
  }
  .order .line {
    display: inline-block;
    width: 28.1%;
    border-top: 1px solid #ccc ;
  }
  .order .txt {
    color: #686868;
    vertical-align: middle;
    font-size: 0.14rem;
  }
</style>
