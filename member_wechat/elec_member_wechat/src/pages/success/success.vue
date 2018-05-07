<template>
  <div>
        <mt-header title="会员中心">
          <router-link to="/" slot="left">
            <mt-button icon="back">返回</mt-button>
              <mt-button  icon="close">关闭</mt-button>
          </router-link>
        </mt-header>
    <m-success>绑定成功</m-success>
  </div>
</template>

<script>
  import  changPhone from '../../../src/components/common/success'
  export default {
    components:{
      'm-success':changPhone,
    }
  }
</script>

<style lang="less" scoped>



</style>
