<template>
  <scroller lock-x>
    <div>
      <ul style="background-color:#fff;font-size:0.4rem;">
        <router-link v-for="o in list" key="id" :to="{path:'/activeDetail', query:{id:o.activity_id}}" tag="li" style="padding:0.25rem;border-bottom:1px solid #ddd;">
          <figure :style="{backgroundImage:`url(${o.picture})`}" style="height:5rem;background-size:cover;background-repeat:no-repeat;background-position:center;">
            <figcaption>
              <div style="flex:1;"></div>
              <div style="background-color: rgba(0,0,0,0.6);color:#FFF; padding: 0.125rem;">{{o.title}}</div>
            </figcaption>
          </figure>
          <div class="time" style="border:1px solied;background;">
            <div style="color:#999;">{{o.activity_time_start | unix('YYYY-MM-DD', 'ms')}} ~ {{o.activity_time_end | unix('YYYY-MM-DD', 'ms')}}</div>
            <div :style="{color:o.sign_type == '0' ? '#ff9100' : '#059cff'}">{{signTitle[o.sign_type]}}</div>
          </div>
        </router-link>
      </ul>
      <div style="text-align:center;width:100%">更多活动敬请期待...</div>
    </div>
  </scroller>
</template>

<script>
import global from '../../../src/components/common/Global.vue'
const { mallId } = global;
import { Scroller } from 'vux'
import moment from 'moment';
import {
  mapState,
} from 'vuex';
export  default {
  components: {
    Scroller,
  },
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
    limitPromptCode(){
      return this.limitPromptCodeMap[this.item.limitPromptCode] || this.item.limitPromptCode
    },
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  async mounted(){
    document.title = '我的活动'

    this.list = (await this.$http.post('/api/activity', {
      mallId, memberId: this.member_id, page:1, size:200
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
    justify-content: space-between;
    margin-top: 0.1rem;
  }
  figure img{
    width: 100%;
    height: 4rem;
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
