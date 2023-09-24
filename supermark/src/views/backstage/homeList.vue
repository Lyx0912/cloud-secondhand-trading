<template>
  <div class="homeList">
  <div class="container">
    <el-row :gutter="20" class="panel-group">
      <el-col :span="6" :xs="12" :sm="12" :lg="6">
        <div class="card-panel">
          <div class="icon-wrapper">
            <img src="@/assets/img/group.png">
          </div>
          <div class="card-description">
            <div class="card-text">用户数量</div>
            <count-to :stardVal="0" :endVal="userCount" :duration="2600"  class="card-num"></count-to>
          </div>

        </div>
      </el-col>
      <el-col :span="6" :xs="12" :sm="12" :lg="6">
        <div class="card-panel">
          <div class="icon-wrapper">
            <img src="@/assets/img/message.png">
          </div>
          <div class="card-description">
            <div class="card-text">商品数量</div>
            <count-to :stardVal="0" :endVal="goodsCount" :duration="3000"  class="card-num"></count-to>
          </div>

        </div>
      </el-col>

      <el-col :span="6" :xs="12" :sm="12" :lg="6">
        <div class="card-panel">
          <div class="icon-wrapper">
            <img src="@/assets/img/message2.png">
          </div>
          <div class="card-description">
            <div class="card-text">订单数量</div>
            <count-to :stardVal="0" :endVal="orderCount" :duration="3200" class="card-num"></count-to>
          </div>
        </div>
      </el-col>

      <el-col :span="6" :xs="12" :sm="12" :lg="6">
        <div class="card-panel">
          <div class="icon-wrapper">
            <img src="@/assets/img/money.png">
          </div>
          <div class="card-description">
            <div class="card-text">订单总金额</div>
            <count-to :stardVal="0" :endVal="moneyCount" :duration="3600"  :decimals="2" class="card-num"></count-to>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
     <line-chart :chart-data="orderAmts"></line-chart>
    </el-row>
  </div>
  </div>
</template>

<script>
import countTo from 'vue-count-to';

import LineChart from "@/views/backstage/LineChart";
export default {
name: "homeList",
  components: {
  LineChart,
  countTo ,
  },
  data () {
    return {
      userCount:0,
      goodsCount:0,
      orderCount:0,
      moneyCount:0,
      orderAmts: {orderCntData: [] , orderCmtData: [],dayData: []}
    }
  },
  created() {
  this.getData();
  this.getOrderAmts();
  },
  mounted() {
  setInterval(this.getOrderAmts,5000);
    setInterval(this.getData,5000);
  },

  methods:{
   getData(){
     this.$http.post("/api/getCountAllUser").then(res=>{
      this.userCount=res.data;
     });
     this.$http.post("/api/getCountAllCommodity").then(res=>{
       this.goodsCount=res.data;
     });
     this.$http.post("/api/getCountAllOrder").then(res=>{
       this.orderCount=res.data;
     });
     this.$http.post("/api/getCountMoney").then(res=>{
       this.moneyCount=res.data;
     });
   },
    getOrderAmts(){
     this.$http.post("/api/getStatistics").then(res=>{
       const timer=res.data.timeArray;
       for(var i =0;i<timer.length;i++){
         const date = new Date(timer[i])

         const hh = (date.getHours() + '').padStart(2, '0')
         const mm = (date.getMinutes() + '').padStart(2, '0')
         const ss = (date.getSeconds() + '').padStart(2, '0')
         timer[i] = hh+":"+mm+":"+ss;
       }
       this.orderAmts.dayData=timer;
       this.orderAmts.orderCmtData=res.data.countMoneyArray;
       this.orderAmts.orderCntData=res.data.orderCountArray;
     })
    }
  }

}

</script>

<style scoped>
.container{
  padding:42px;
  background-color: rgb(240, 242, 245);
}
.panel-group{
  margin-top: 18px;
}
.el-col{
  margin-bottom: 32px;
}
.homeList .el-row{
  display: flex;
  align-items: center;
  justify-content:space-between;
}
.card-panel{
  height: 108px;
  cursor: pointer;
  font-size: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  overflow: hidden;
  color: #666;
  background: #fff;
  border-radius:5%;
  box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
  border-color: rgba(0, 0, 0, .05);
}

.homeList img{
  width: 72px;
  height:80px;
}
.icon-wrapper{
  margin-left: 20px;
}
.card-description{
  margin-right: 20px;
}
.card-text{
 font-size:15px;
  font-weight:bold;
  color: rgba(0, 0, 0, 0.45);
}

.card-num{
 display: inline-block;
  font-size: 20px;
  font-weight:bold;
  margin-top: 10px;
}
</style>