<template>
  <div>
    <ul style="background-color:#fff;">
      <router-link v-for="o in list" key="id" :to="{path:'/activeDetail', query:{id:o.activity_id}}" tag="li" style="padding-bottom:10px;border-bottom:1px solid #ddd;">
        <figure :style="{backgroundImage:`url(${o.picture})`}" style="height:2rem;background-size:cover;background-repeat:no-repeat;background-position:center;">
          <figcaption>
            <div style="flex:1;"></div>
            <div style="font-size:0.8em;background-color: rgba(0,0,0,0.6);color:#FFF; padding: 5px;">{{o.title}}</div>
          </figcaption>
        </figure>
        <div class="time" style="border:1px solied;background;">
          <div style="font-size:0.4rem;color:#999;">{{o.activity_time_start | unix('YYYY-MM-DD', 'ms')}} ~ {{o.activity_time_end | unix('YYYY-MM-DD', 'ms')}}</div>
          <div style="font-size:0.4rem;color:#059CFF;">{{signTitle[o.sign_type]}}</div>
        </div>
      </router-link>
    </ul>
    <div style="text-align:center;width:100%">更多活动敬请期待...</div>
  </div>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import moment from 'moment';
import {
  mapState,
} from 'vuex';
export  default {
  data() {
    return {
      signTitle: {
        '0': '未报名', '1': '已报名', '2': '已签到'
      },
      list: []
    }
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  async mounted(){
    document.title = '我的活动'

    this.list = (await this.$http.post(`/api/member/${this.member_id}/activityList`, {
      mallId,
    })).data

    // const list = await this.$http.post('/api/activity')
    // console.log(list)
  }
}
</script>
<style lang="less" scoped>
  .act{
      font-family:-apple-system-font, "Helvetica Neue", sans-serif

  }
  div ul li{
   margin-bottom: 0.1rem;


  }
  .time{
    display: flex;
    width: 90%;
    margin-left: 5%;
    justify-content: space-between;
    margin-top: 0.1rem;


  }
  li div h1{
    font-weight: normal;
    color:#999;
    font-size: 0.14rem;



  }
  figure img{
    width: 100%;
    height: 2rem;
  }
  figure figcaption{
    display: flex;
    flex-direction: column;
    text-align: justify;
    color: #333;
    height: 100%;
  }
  button{
    border-radius: 0.03rem;
    background-color: #fff;
    border: 0;
    outline: none;
    color: #999;
    padding: 0.02rem 0.05rem;
  }

</style>
