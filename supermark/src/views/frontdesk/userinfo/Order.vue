<template>
  <div>
    <template>
      <el-table v-loading="listLoading" :row-style="{height:'45px'}" :cell-style="{padding:'0px'}" :data="orderList" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column align="center" label="商品名称" prop="goodsName" width="150 " :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="categoryName" width="130" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="卖家" prop="seller" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" width="150">
        <template v-slot="scope">
          <el-image :src="scope.row.defaultImg" style="width:64px;height:64px;" :preview-src-list="[scope.row.defaultImg]" />
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="orderSn" />
      <el-table-column label="价格" align="center" prop="payAmount" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="订单状态" align="center" width="100">
        <template  v-slot="scope">
          <el-tag type="success" effect="dark" v-if="scope.row.state==1">待发货</el-tag>
          <el-tag type="warning" effect="dark" v-if="scope.row.state==0||scope.row.state==null">待付款</el-tag>
          <el-tooltip class="item" effect="dark" :content="scope.row.mark" placement="top">
            <el-tag type="danger" effect="dark" v-if="scope.row.state==2">已发货</el-tag>
          </el-tooltip>
          <div v-if="scope.row.state==4">已取消</div>
        </template>
      </el-table-column>
      <el-table-column label="购买时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column align="center" prop="created_at" label="操作">
        <template  v-slot="scope">
          
        <!-- <router-link :to="{path:'/checkstand',query: {orderSn: scope.row.orderSn}}"> -->
          <el-button v-if="scope.row.state==0||scope.row.state==null" type="primary" icon="el-icon-edit" size="mini" @click="payment(scope.row.orderSn)"
                     >付款</el-button>
                     <!-- </router-link> -->
                     
          <el-button 
          type="primary" icon="el-icon-edit" size="mini" @click="getOrder(scope.row.orderSn,scope.row.id)"
                     >详情</el-button>
          <el-button v-if="scope.row.state==1||scope.row.state==0||scope.row.state==null" 
          type="danger" icon="el-icon-delete" size="mini" @click="deleteById(scope.row.orderSn,scope.row.id)"
                     >取消</el-button>
        </template>
      </el-table-column>
    </el-table>

      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pagenum"
          :page-sizes="[5, 10, 15]"
          :page-size="queryInfo.pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      >
      </el-pagination>
    </template>
  </div>
</template>

<script>
export default{
  data() {
    return {
      listLoading: true,
      // 定义数据源
      orderList:[],
      //查询分页信息
      queryInfo:{
        uid:JSON.parse(sessionStorage.getItem("user_id")),
        pagenum:1,
        pagesize:5,
      },
      //订单总页数
      total:0
    }
  },
  created() {
    this.master = JSON.parse(sessionStorage.getItem("user")).username;
    this.getMyorders();
  },
  methods:{
    getOrder(orderSn,orderId){
      this.$router.push({path:"/OrderDetails",query:{orderId:orderId}})
    },
    //ajax获取订单信息
    getMyorders() {
      this.listLoading = true;
      this.$http.post("/api/cloud-order/order/list",{
        pageNo:this.queryInfo.pagenum,
        pageSize:this.queryInfo.pagesize,
        uid:this.queryInfo.uid
      }).then(res=>{
        console.log(res.data.data)
        this.orderList=res.data.data.list;
        this.total=res.data.data.total;
        this.listLoading = false
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize//更新每页的显示数据的条数
      this.getMyorders()//重新发起网络请求
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage//更新当前页码的数据
      this.getMyorders()
    },
    async deleteById(orderSn,goodsId) {
      console.log(orderSn)
      const confirmResult= await this.$confirm("您确认要取消该订单吗？",`${this.master}`,{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"warning"
      }).catch((err)=>err)

      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消操作')
      }
      const { data: res } = await this.$http.delete('/api/cloud-order/order/deleteOrderByOrderId',
      {params: {orderSn:orderSn,memberId:this.queryInfo.uid,goodsId:goodsId}})

      if (res.code===505) return this.$message.error(res.message)
      this.$message.success('取消订单成功！')
      this.getMyorders()
    },
    payment(orderSn){
      // window.location.href("/checkstand/"+orderSn)
      this.$router.push({path:"/checkstand",query:{orderSn:orderSn}});
    }
  }
}
</script>
<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>