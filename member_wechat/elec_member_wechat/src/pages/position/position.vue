<template>
  <div>
<!--    <mt-header>
      <router-link to="/" slot="left">
        <mt-button icon="back">返回</mt-button>
      </router-link>
    </mt-header>-->
    <div class="amap-page-container">
      <el-amap vid="amapDemo" :zoom="zoom" :center="center" class="amap-demo" :plugin="plugin">
        <el-amap-info-window  :position="mywindow.position" :content="mywindow.content" :visible="mywindow.visible" :events="mywindow.events"></el-amap-info-window>
        <el-amap-marker :position="marker.position" :events="marker.events" :visible="marker.visible" :draggable="marker.draggable"></el-amap-marker>
        <el-amap-circle :center="circle.center" :radius="circle.radius" :fillOpacity="circle.fillOpacity" :events="circle.events" :strokeColor="circle.strokeColor" :strokeStyle="circle.strokeStyle" :fillColor="circle.fillColor"></el-amap-circle>
      </el-amap>
    </div>
  </div>
</template>

<style>
  .amap-page-container {
    position: fixed;
    top:0;
    bottom: 0;
    left: 0;
    right: 0;

  }
</style>


<script>
  export default {
    data () {
      return {
        //祡荆广场坐标(121.5167,31.275101)
        zoom: 15,
        center: [],
        circle: {
          clickable: true,
          center: [],
          radius: 50,
          fillOpacity: 0.3,
          strokeStyle: 'dashed',
          fillColor: '#FFFF00',
          strokeColor: '#00BFFF'
        },
        marker: {
          position: [],
          events: {
            click: () => {
              if (this.mywindow.visible === true) {
               /* this.mywindow.visible = false*/
              } else {
                this.mywindow.visible = true
              }
            },
            dragend: (e) => {
              this.markers[0].position = [e.lnglat.lng, e.lnglat.lat]
            }
          },
          visible: true,
          draggable: false
        },
        mywindow: {
          position: [],
          /*content: '<div class="text item">控江路1628号</div>',*/
          visible: true,
          events: {
            close () {
              this.mywindow.visible = false
            }
          }
        },
        plugin: {
          pName: 'Scale',
          events: {
            init (instance) {
              console.log(instance)
            }
          }
        }
      }
    },
    mounted(){
       this.position=[this.$route.query.longitude,this.$route.query.latitude];
       this.center=this.position;
       this.circle.center=this.position;
       this.marker.position=this.position;
       this.mywindow.position=this.position;
    }
  }
</script>
