<template>
  <div style="height:100%;">
    <scroller lock-x height="100%">
      <div style="display:flex;flex-direction:column;min-height:100%;background-color:#fff;align-items:center;">

        <div style="display:flex;align-items:center;width:100%;">
          <div style="flex:1;border-top:1px solid #ccc;"></div>
          <div style="margin:0 0.5rem;font-size:0.4rem;color:#686868;">我的投诉建议</div>
          <div style="flex:1;border-top:1px solid #ccc;"></div>
        </div>

        <ul style="margin-top:0.25rem;width:100%;">
          <li v-if="!list.length" style="text-align:center;margin-bottom:0.5rem;font-size:0.4rem;">
            暂无投诉记录
          </li>
          <li v-for="v in list" v-else style="margin:0 8px;" @click="showDetail(v)">
            <div style="display:flex;padding:0.25rem;">
              <div style="margin:0.125rem;font-size:0.4rem;flex:1;">
                <div>商户：<span style="color:#ff8100;">{{v.shop_name}}</span></div>
                <div>状态：{{v.status ? '未处理' : '已处理'}}</div>
                <div>提交时间：{{v.action_time | unix('YYYY-MM-DD HH:mm', 'ms')}}</div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </scroller>
    <x-dialog v-model="showDetailDialog" class="dialog-demo">
      <div style="padding:15px;font-size:0.4rem;text-align:left;">
        <p>建议：{{detail.comments}}</p>
        <p style="margin-top:10px;" v-if="detail.reply">回复：{{detail.reply}}</p>
      </div>
      <div @click="showDetailDialog=false" style="padding:10px;text-align:center;font-size:0.5rem;">
        <span>确定</span>
      </div>
    </x-dialog>
  </div>
</template>
<script>
import global from '../../src/components/common/Global'
const { mallId } = global;
import moment from 'moment';
import { Scroller, XDialog } from 'vux'
import {
  mapState,
} from 'vuex';

export default {
  components: {Scroller, XDialog},
  data() {
    return {
      list: [],
      showDetailDialog: false,
      detail: {},
    }
  },
  computed: {
    ...mapState({
      member_id: state => state.member_id,
    }),
  },
  filters: {
    unix: (value, format, unit) => {
      return moment.unix((unit == 'ms') ? (value / 1000) : value).format(format)
    }
  },
  methods: {
    async showDetail(v){
      this.detail = (await this.$http.get(`/api/member/suggested/${v.assessment_id}`)).data;
      this.showDetailDialog = true;
    }
  },
  async mounted(){
    document.title = '我的投诉建议'
    const { member_id } = this;

    this.list = (await this.$http.post(`/api/member/${member_id}/suggestedList`, {
      mallId, page:1, size:200
    })).data

  }
}
</script>
<style lang="less" scoped>
</style>
