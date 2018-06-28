<template>
<div>
  <div style="background-color:#fff;padding-bottom:15px;">
    <msg :title="statusName" :icon="statusIcon"></msg>
  </div>
</div>
</template>
<script>
import { TransferDom, Scroller, Msg } from 'vux'
export default {

  data() {
    return {
        statusName: '支付成功',
        statusIcon: 'success',
    }
  },
  components: {
    Scroller, Msg
  },
   mounted() {
    // const { transaction_id } = this.$route.query
    // this.statusName = transaction_id
    // debugger
  },
  computed: {

  },
  methods: {

  }
}
</script>

<style scoped>
</style>
