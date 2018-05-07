<template>
  <div id="app">
   <!-- <m-header></m-header>
    <m-info></m-info>-->
   <!-- <m-nav></m-nav>-->
 <!-- <m-user @title="getTitle"></m-user>-->
  <!-- <r-index @title="getTitle"></r-index>-->
 <!--  <r-success></r-success>-->
  <!-- <r-data @title="getTitle"></r-data>-->
    <transition name="router-fade">
        <router-view></router-view>
    </transition>
  </div>
</template>

<script>
import animate from 'animate.css';
import global from '@/components/common/Global.vue'
const { mallId } = global;
import {
  mapState,
} from 'vuex';

export default {
  data(){
     return {
       title:'',
       closeButton:false,
     }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  async mounted() {
    if(this.member_id){
      this.$http.post('/api/member/visit', { mallId, memberId: this.member_id})
    }
  }
}
</script>
<style lang="less">
  .mint-header{
    background-color: #1C1B21;
  }
@import '~vux/src/styles/reset.less';
@import '~vux/src/styles/1px.less';
@import '~vux/src/styles/tap.less';
body {
  background-color: #F2F2F2;
}
html, body {
  height: 100%;
  min-width: 320px;
  max-width: 640px;
  margin: 0 auto;
  overflow-x: hidden;
}
</style>
