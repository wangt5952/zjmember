<template>
<div>
  <div style="background-color:white">
    <div style="height:1rem;border-bottom:0.02rem solid #E2E2E2;padding-top:0.2rem">
      <label style="margin-top:0.3rem;margin-left:0.3rem;color:#777777;font-size:0.5rem;">剩余车位</label>
      <p style="float:right;padding-right:0.6rem;color:#FB9D0A;font-size:0.5rem;"><strong>102</strong></p>
    </div>

    <div v-if='oldlicence' style="display:flex;padding:0.2rem;margin:0.2rem;">
      <div @click="showKeyboard(0)" :style="{borderColor:inputIndex==0 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[0]}}
      </div>
      <div @click="showKeyboard(1)" :style="{borderColor:inputIndex==1 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.5em;background:#f1eded">
        {{car_number[1]}}
      </div>
      <div @click="showKeyboard(2)" :style="{borderColor:inputIndex==2 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[2]}}
      </div>
      <div @click="showKeyboard(3)" :style="{borderColor:inputIndex==3 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[3]}}
      </div>
      <div @click="showKeyboard(4)" :style="{borderColor:inputIndex==4 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[4]}}
      </div>
      <div @click="showKeyboard(5)" :style="{borderColor:inputIndex==5 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[5]}}
      </div>
      <div @click="showKeyboard(6)" :style="{borderColor:inputIndex==6 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[6]}}
      </div>
      <div @click="showKeyboard(7)" :style="{borderColor:inputIndex==7 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[7]}}
      </div>
    </div>
    <div v-if='newlicence' style="display:flex;padding:0.2rem;margin:0.2rem;">
      <div @click="showKeyboard(0)" :style="{borderColor:inputIndex==0 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[0]}}
      </div>
      <div @click="showKeyboard(1)" :style="{borderColor:inputIndex==1 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.5em;background:#f1eded">
        {{car_number[1]}}
      </div>
      <div @click="showKeyboard(2)" :style="{borderColor:inputIndex==2 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[2]}}
      </div>
      <div @click="showKeyboard(3)" :style="{borderColor:inputIndex==3 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[3]}}
      </div>
      <div @click="showKeyboard(4)" :style="{borderColor:inputIndex==4 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[4]}}
      </div>
      <div @click="showKeyboard(5)" :style="{borderColor:inputIndex==5 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[5]}}
      </div>
      <div @click="showKeyboard(6)" :style="{borderColor:inputIndex==6 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[6]}}
      </div>
      <div @click="showKeyboard(7)" :style="{borderColor:inputIndex==7 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[7]}}
      </div>
      <div @click="showKeyboard(7)" :style="{borderColor:inputIndex==7 && popupKeyboard ? 'red':'#E2E2E2'}" style="border:1px solid #E2E2E2;width:1rem;height:1rem;font-size:0.4rem;display:flex;align-items:center;justify-content:center;margin-right:.1em;background:#f1eded">
        {{car_number[7]}}
      </div>
    </div>
    <div style="display:flex;float:right;padding-right:0.6rem;">
      <p style="height:1rem;line-height: 1rem;padding:.1rem;color:#777777;">是否新能源</p>
      <x-switch title="" @on-change='neworold' v-model="switchvalue"></x-switch>
    </div>

    <div style="display:flex;align-items:center;margin-top:1.5rem;">
      <div style="flex:1;border-bottom:1px solid #E2E2E2;"></div>
      <div style="margin:0 1rem;font-size:0.4rem;color:#777777;">注意事项</div>
      <div style="flex:1;border-bottom:1px solid #E2E2E2;"></div>
    </div>

    <div style="color:#767676;width:9rem;margin:0 auto;font-size:0.4rem">
      <label>1.缴费成功后，请于30分钟内离场，超时需补交停车费<br /></label>
      <div style="padding-top:0.1rem;padding-bottom:0.1rem;">
        <label>2.若车牌识别有误，请到中央收费处缴纳</label>
      </div>
    </div>
  </div>
  <div style="background-color:#FFFFFF;margin-top:20px;text-align">
    <div style="padding-bottom:1px;">
      <div style="display:flex;align-items:center;padding-top:10px;text-align:center;">
        <div style="flex:1;"></div>
        <div style="margin:0 1rem;font-size:0.4rem;color:#777777;">收费标准</div>
        <div style="flex:1;"></div>
      </div>


      <table style="margin-top:10px;border:1px solid #E2E2E2;width:9rem;height:2rem;color:#767676;font-size:0.4rem;margin:0 auto;text-align:center;" cellpadding="0" cellspacing="0">
        <tr>
          <td style="border-bottom:1px solid #E2E2E2;padding:0.2rem;">时间</td>
          <td style="border-left:1px solid #E2E2E2; border-bottom:1px solid #E2E2E2;">收费</td>
        </tr>
        <tr>
          <td>停车不足15分钟</td>
          <td style="border-left:1px solid #E2E2E2;padding:0.1rem;">免费</td>
        </tr>
        <tr>
          <td>15分钟至1小时</td>
          <td style="border-left:1px solid #E2E2E2;padding:0.1rem;">5元/台</td>
        </tr>
        <tr>
          <td>超1小时后每30分钟</td>
          <td style="border-left:1px solid #E2E2E2;padding:0.1rem;">2元/台</td>
        </tr>
        <tr>
          <td>24小时</td>
          <td style="border-left:1px solid #E2E2E2;padding:0.1rem;">最高50元/台</td>
        </tr>
      </table>
      <div style="width:9rem;margin:10px auto;">
        <label style="color:#767676;">注：超过一小时后，不足30分钟按30分钟收费。</label>
      </div>
    </div>
  </div>
  <!-- <router-link :to="/carDetail/'+id"> -->
    <router-link :to="{path:'/carDetail',query: {car_number:car_number}}">
    <div style="background-color:#46D0C3;text-align:center;position:fixed;right:0;left:0;bottom:0;">
      <div style="color:#fff;padding:0.3rem;font-size:0.5rem">缴 费</div>
    </div>
  </router-link>

  <div v-transfer-dom>
    <popup v-model="popupKeyboard" position="top" :popup-style="{backgroundColor:'#fff', marginTop:'3rem'}" :is-transparent="true">
      <div class="char-line" v-for="o in keyboard[car_number_type[inputIndex]]">
        <div v-for="p in o" @click="touchKeyboard">{{p}}</div>
      </div>
    </popup>
  </div>
</div>
</template>
<script>
import _ from 'lodash';
import {
  TransferDom,
  Popup,
  Scroller,
  XSwitch
} from 'vux';

export default {
  directives: {
    TransferDom
  },
  components: {
    Popup,
    Scroller,
    XSwitch
  },
  data() {
    return {
      oldlicence: true,
      newlicence: false,
      switchvalue: '',
      car_number: [],
      car_number_type: ['1', '2', '3', '3', '3', '3', '3', '4'],
      keyboard: {
        '1': [
          ['冀', '豫', '云', '辽'],
          ['黑', '湘', '皖', '鲁'],
          ['新', '苏', '浙', '赣'],
          ['鄂', '桂', '甘', '晋'],
          ['蒙', '陕', '吉', '闽'],
          ['贵', '粤', '川', '青'],
          ['藏', '琼', '宁', '渝'],
          ['京', '津', '沪'],
        ],
        '2': [
          ['A', 'B', 'C', 'D', 'E'],
          ['F', 'G', 'H', 'I', 'J'],
          ['K', 'L', 'M', 'N', 'O'],
          ['P', 'Q', 'R', 'S', 'T'],
          ['U', 'V', 'W', 'X', 'Y'],
          ['Z', '后退'],
        ],
        '3': [
          ['1', '2', '3', '4', '5'],
          ['6', '7', '8', '9', '0'],
          ['A', 'B', 'C', 'D', 'E'],
          ['F', 'G', 'H', 'I', 'J'],
          ['K', 'L', 'M', 'N', 'O'],
          ['P', 'Q', 'R', 'S', 'T'],
          ['U', 'V', 'W', 'X', 'Y'],
          ['Z', '后退', '关闭'],
        ],
        '4': [
          ['1', '2', '3', '4', '5'],
          ['6', '7', '8', '9', '0'],
          ['A', 'B', 'C', 'D', 'E'],
          ['F', 'G', 'H', 'I', 'J'],
          ['K', 'L', 'M', 'N', 'O'],
          ['P', 'Q', 'R', 'S', 'T'],
          ['U', 'V', 'W', 'X', 'Y'],
          ['Z', '后退', '删除', '关闭'],
        ]
      },
      popupKeyboard: false,
      inputIndex: 0,
    }
  },
  methods: {
    neworold(value) {
      if (value) {
        this.oldlicence = false
        this.newlicence = true
      } else {
        this.oldlicence = true
        this.newlicence = false
      }
    },
    showKeyboard(index) {
      this.inputIndex = index;
      this.popupKeyboard = true;
    },
    touchKeyboard({
      target
    }) {
      const value = _.trim(target.innerText);
      let car_number = [
        ...this.car_number
      ];
      if (value == '删除') {
        car_number[this.inputIndex] = '';
        this.popupKeyboard = false;
      } else if (value == '后退') {
        car_number[this.inputIndex] = '';
        this.inputIndex--;
      } else if (value == '关闭') {
        this.popupKeyboard = false;
      } else {
        car_number[this.inputIndex] = value;
        this.inputIndex++;
        if (this.inputIndex > 7) {
          this.popupKeyboard = false;
        }
      }
      this.car_number = car_number;
    },
  }
}
</script>

<style lang="less" scoped>
.char-line {
    display: flex;
    justify-content: space-around;
}
.char-line > div {
    width: 1rem;
    font-size: 0.5rem;
    padding: 0.3rem;
    text-align: center;
}
</style>
