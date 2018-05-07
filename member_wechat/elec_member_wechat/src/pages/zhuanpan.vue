<template>
  <div style="background-color:#fff;padding-bottom:15px;">


    <div style="background-image:url(/static/zhuanpan/pan-bg-red.png);height:30px;"></div>
  </div>
</template>

<script>
import _ from 'lodash'
export default {
  data() {
    return {
      popup: false,
    }
  },
  methods: {
  },
  async mounted(){
    document.title = 'VUE大转盘'
  }
}
</script>

<style lang="less" scoped>
</style>
