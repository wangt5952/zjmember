<template>
  <div>
      <!--  <mt-header>
          <router-link to="/" slot="left">
            <mt-button icon="back">返回</mt-button>
              <mt-button  icon="close">关闭</mt-button>
          </router-link>
        </mt-header>-->
    <r-success>注册成功</r-success>
  </div>
</template>

<script>
  import  registerSucc from '../../../src/components/common/success'
  export default {
    components:{
      'r-success':registerSucc,
    }
  }
</script>

<style lang="less" scoped>



</style>
