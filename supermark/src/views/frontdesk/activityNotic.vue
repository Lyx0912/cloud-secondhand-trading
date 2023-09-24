<template>
<div class="active-notic">
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>活动公告</el-breadcrumb-item>
  </el-breadcrumb>
  <el-card class="box-card">
    <div slot="header" class="title">
      <span>小米K40新品活动</span>
    </div>
    <div class="content-box">
      <div><img src="@/assets/img/xiaomi.png" alt=""></div>
      <div class="content">1、活动时间:<span>{{y}}年{{m}}月{{d}}日--{{yy}}年{{mm}}月{{dd}}日</span></div>
      <div class="content">2、活动规则:<span>在商品抢购活动期间，凡在商城抢购了“小米k40”的用户，根据订购顺序，将对第1-100名抢购用户以1折的价格销售该商品，第101-500名用户5折销售，第501-1000名用户8折销售，第1001名后商品将以原价销售。</span></div>
    </div>
  </el-card>
</div>
</template>

<script>
export default {
name: "activityNotic",
  data(){
  return{
    product_id:1,
    startTime:"",
    endTime:"",
    y:"",
    m:"",
    d:"",
    yy:"",
    mm:"",
    dd:""
  }
  },
  created() {
   this.getActiveTimer();
  },
  methods:{
  getActiveTimer(){
    this.$http.post("/api/getCommodityById/"+this.product_id).then(res=>{
      this.startTime=res.data.startTime;
       this.endTime=res.data.endTime;

      var startTimer=new Date(this.startTime);
      var endTimer=new Date(this.endTime);
      this.y=startTimer.getFullYear();
      this.m=(startTimer.getMonth()+1+"").padStart(2,"0");
      this.d= (startTimer.getDate() + '').padStart(2, '0');
      this.yy=endTimer.getFullYear();
      this.mm=(endTimer.getMonth()+1+"").padStart(2,"0");
      this.dd= (endTimer.getDate() + '').padStart(2, '0');
    })
  }
  }
}
</script>

<style scoped>
.el-card{
  width:500px;
  height: 600px;
  border:1px solid #a8a8a8;
  margin: 0 auto;
}
.box-card .title span{
  font-weight:bold;
  text-align: center;
}
.box-card .content-box img{
  width: 464px;
  height: 200px;
  cursor: pointer;
}
.box-card .content-box .content{
  font-weight: bold;
  line-height: 35px;
}
.box-card .content-box .content span{
  font-weight: normal;
}
</style>