<template>
  <div class="seckill-info">
    <div class="left">
      <div class="left-box">
        <div class="price-box">
          <span class="name">秒杀价:</span>
          <span class="price">
            <em>￥</em>{{price}}
          </span>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="time-box">
        <span class="tips" v-if="state==0">
          距离开始还有:
        </span>
        <span class="tips" v-if="state==1">
          距离结束:
        </span>
        <span class="tips" v-if="state==2">
          该商品已抢购结束
        </span>
        <span class="time" v-if="state==0">
          <span class="hour">{{h}}</span>:
          <span class="min">{{m}}</span>:
          <span class="sec">{{s}}</span>
        </span>
        <span class="time"v-if="state==1">
          <span class="hour">{{h}}</span>:
          <span class="min">{{m}}</span>:
          <span class="sec">{{s}}</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
var now;
var begin;
export default {
  name: "Countdown",
  props:{
    gid:{
      type:String
    },
    overTime:{
      type:Number
    },
    startFunc:{
      type:Function
    },
    overFunc:{
      type:Function
    },
    showFunc:{
      type:Function
    }
  },
  data(){
    return{
      h:"00",
      m:"00",
      s:"00",
      state:0,
      price:0
    }
  },
  methods:{
    // 开始倒计时
    countdown(){
      var that=this;
      // 结束时间
      // var over=new Date(this.overTime);

      //  转换成毫秒值
      // var end=over.getTime();

      var end=this.overTime;
      //获取当前时间戳
      var currentTime=new Date().getTime();

      if(currentTime>end && currentTime>begin){
        this.state=2;
        // this.$message.success("抢购结束")
      }else if(currentTime<begin) {
        this.state=0;
        var times=begin-currentTime;
        // this.$message.success("即将开始")
      }else {
        // 中间差距的毫秒
        this.state=1;
        var times=end-currentTime;

        // this.$message.success("抢购中")
      }
      if(this.state==1){
        //倒计时结束
        //调用父组件的方法
        this.startFunc();
      }else if(this.state==0){
         this.overFunc();
      }else{
        this.overFunc();
        this.showFunc();
      }

      var hour=parseInt(times/1000/60/60);
      var min=parseInt(times/1000/60%60);
      var sec=parseInt(times/1000%60);

      this.h=this.formateTime(hour);
      this.m=this.formateTime(min);
      this.s=this.formateTime(sec);

      // 每隔1s执行,更新倒计时
      setTimeout(function () {
        // begin+=1000;
        that.countdown();
      },1000)
    },
    // 格式化时间格式
    formateTime(s){
      return s<10?("0"+s):s;
    },
    // 获取服务器的当前时间
    getServeTime(){
      var that=this;
      //ajax请求
      this.$http.post("/api/getCommodityById/"+this.gid).then(res=>{

        begin=res.data.startTime;
        that.price=res.data.commodityPrice,
        that.countdown();
      })
    }
  },
  created() {
    this.getServeTime();
  },
  watch:{
    overTime(v){
      this.getServeTime();
    }
  }
}
</script>

<style scoped>
.seckill-info{
  position: relative;
  height: 52px;
  background: url(../../assets/img/booking.png) 0 0 no-repeat;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  color: #fff;
  margin-top: 15px;
}
.seckill-info .left{
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;
  height: 100%;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
}
.seckill-info .left .price-box{
  margin-bottom: 2px;
  line-height: 16px;
}
.seckill-info .left .left-box  .name{
  text-align: right;
  width: 55px;
  font-size: 14px;
  display: inline-block;
}
.seckill-info .left .left-box .price{
  font-size: 24px;
  margin-left: 8px;
}
.seckill-info .left .left-box .price em{
  font-style: normal;
  font-size: 14px;
}
.seckill-info .right{
  max-width: 200px;
  /*display: inline-block;*/
  flex: 1;
  margin-right: 20px;
  height: 100%;
  font-size: 12px;
}
.seckill-info .right .time-box{
  height: 30px;
  margin-top: 11px;
  line-height: 30px;
}
.seckill-info .right .tips{
  /*margin-left: 20px;*/
  /*margin-right: 2px;*/
}
.seckill-info .right span{
  /*display:inline-block;*/
}
.seckill-info .right .hour,
.seckill-info .right .min,
.seckill-info .right .sec{
  text-align: center;
  vertical-align: middle;
  width: 18px;
  height: 18px;
  background: #000;
  border-radius: 2px;
  margin: 0 6px;
  line-height: 16px;
}
</style>