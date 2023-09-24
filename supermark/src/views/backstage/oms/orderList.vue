<template>
  <div class="order_box">

    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单</el-breadcrumb-item>
      <el-breadcrumb-item>订单列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>

      <el-row :gutter="20">
        <el-form :inline="true" size="small" :model="ruleForm" ref="ruleForm" label-width="140px" class="demo-form-inline">
          <el-col :span="24">

            <el-form-item label="输入搜索" prop="order_id">
              <el-input v-model="ruleForm.order_id" placeholder="订单编号"></el-input>
            </el-form-item>

            <el-form-item label="手机号" prop="user_phone">
              <el-input v-model="ruleForm.user_phone" placeholder="用户电话"></el-input>
            </el-form-item>

            <el-form-item style="margin-left: 30px">
              <el-button type="primary" @click="getMysearchorderList">查询</el-button>
              <el-button @click="resetForm()">重置</el-button>
            </el-form-item>

          </el-col>
        </el-form>
      </el-row>

    </el-card>

    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
    </el-card>

    <div class="table-container">
      <el-table
          :data="orderList"
          border
          style="width: 100%"

      >
        <el-table-column
            type="index"
            width="60"
            align="center"
        ></el-table-column>

        <el-table-column
            label="订单ID"
            width="160"
            align="center"
        >
          <template slot-scope="scope">{{scope.row.orderId}}</template>
        </el-table-column>

        <el-table-column
            label="下单时间"
            align="center"
            width="160"
        >
          <template slot-scope="scope">{{scope.row.orderTime | dateFormat}}</template>
        </el-table-column>

        <el-table-column
            label="客户名称"
            align="center"
            width="140"
        >
          <template slot-scope="scope">
            <p>{{scope.row.username}}</p>
          </template>
        </el-table-column>


        <el-table-column
            label="商品名称"
            align="center"
            width="140"
        >
          <template slot-scope="scope">
            <p>{{scope.row.commodityName}}</p>
          </template>
        </el-table-column>


        <el-table-column
            label="订单金额"
            align="center"
            width="120"
        >
          <template slot-scope="scope">￥{{scope.row.money}}</template>
        </el-table-column>


        <el-table-column
            label="订单状态"
            align="center"
            width="120"
           >
          <template slot-scope="scope">{{scope.row.orderState}}</template>
        </el-table-column>

        <el-table-column
            label="物流单号"
            align="center"
            width="140">
          <template slot-scope="scope">{{scope.row.courierNumber}}</template>
        </el-table-column>

        <el-table-column
            label="操作"
            align="center"
        >
          <template slot-scope="scope">
            <p>
              <el-button
                  size="mini"
                  @click="handleUpdateProduct(scope.$index, scope.row);dialogVisible=true">订单详情
              </el-button>
              <el-button
                  size="mini"
                  type="danger"
                  @click="deleteById(scope.row.orderId)">删除订单
              </el-button>
            </p>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination-container">
      <el-pagination
          background
          style="float:right; margin-top: 20px"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes,prev, pager, next,jumper"
          :page-size="queryInfo.pageSize"
          :page-sizes="[5,10,15]"
          :current-page="queryInfo.pageNum"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog
        title="订单详情"
        :visible.sync="dialogVisible"
        >
      <el-row :gutter="20">
        <el-col :span="3">订单编号</el-col>
        <el-col :span="21">{{this.orderDetail.orderId}}</el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="3">订单状态</el-col>
        <el-col :span="21"><el-tag>{{this.orderDetail.orderState}}</el-tag></el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="3">订单用户</el-col>
        <el-col :span="21">{{this.orderDetail.orderUser}}</el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="3">收货信息</el-col>
        <el-col :span="21">(收货人)  {{this.orderDetail.orderUser}} (手机号) {{this.orderDetail.phone}}</el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">商品信息</el-col>
        <el-col :span="24">
          <el-table
              :data="tableData"
              border
              :header-cell-style="{background:'#f8f8f9'}"
              style="width: 100%;">

            <el-table-column
                label="商品名称"
                align="center"
                width="140"
            >
              <template slot-scope="scope">
                <p>{{scope.row.goodName}}</p>
              </template>
            </el-table-column>

            <el-table-column
                label="商品ID"
                width="120"
                align="center"
            >
              <template slot-scope="scope">{{scope.row.goodId}}</template>
            </el-table-column>

            <el-table-column
                label="价格"
                align="center"
                width="140"
            >
              <template slot-scope="scope">
                <p>￥{{scope.row.goodPrice}}</p>
              </template>
            </el-table-column>

            <el-table-column
                label="商品数量"
                align="center"
                width="140"
            >
              <template slot-scope="scope">{{scope.row.goodStock}}</template>
            </el-table-column>

          </el-table>
        </el-col>
      </el-row>

  <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
name: "orderList",
  data(){
  return{
    // 定义数据源
    orderList:[],
    ruleForm:{
      order_id:"",
      user_name:"",
      user_phone:""
    },
    tableData:[],
    orderDetail:{
      orderId:"",
      orderState:"",
      orderUser:"",
      orderAddr:"",
      phone:"",
      goodInfo:{
        goodName:"",
        goodId:"",
        goodPrice:"",
        goodStock:"",
        goodImg:"",
      }
    },
    dialogVisible:false,
    //查询分页信息
    queryInfo:{
      uid:JSON.parse(sessionStorage.getItem("user")).uid,
      pageNum:1,
      pageSize:5,
    },
    //订单总页数
    total:0
  }
  },
  created() {
  this.getMyorderList();
  },
  methods:{
    //ajax获取订单信息
    getMyorderList() {
      this.$http.post("/api/getAllOrder",{
        pageNum:this.queryInfo.pageNum,
        pageSize:this.queryInfo.pageSize,
      }).then(res=>{
   
        this.orderList=res.data.orderList;
        this.total=res.data.orderCount;
      })
    },
    getMysearchorderList(){
      this.$http.post("/api/getOrderByPhoneOrOrderId",{
        phone:this.ruleForm.user_phone,
        orderId:this.ruleForm.order_id,
        pageNum:this.queryInfo.pageNum,
        pageSize:this.queryInfo.pageSize,
      }).then(res=>{
        this.orderList=res.data.order;
        this.total=res.data.pageCount;
      })
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize//更新每页的显示数据的条数
      this.getMyorderList()//重新发起网络请求
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage//更新当前页码的数据
      this.getMyorderList()
    },
    handleUpdateProduct(index,row){
      this.orderDetail.orderId=row.orderId;
      this.orderDetail.orderState=row.orderState;
      this.orderDetail.orderUser=row.username;
      this.orderDetail.goodInfo.goodName=row.commodityName;
      this.orderDetail.goodInfo.goodId=row.commodityId;
      this.orderDetail.goodInfo.goodPrice=row.money;
      this.orderDetail.goodInfo.goodStock=row.commodityMuch;
      this.orderDetail.phone=row.phone;
      this.tableData[0]=this.orderDetail.goodInfo;
    },
    async deleteById(orderId) {

      const confirmResult= await this.$confirm("您确认要删除该订单吗？",`${this.$store.getters.getUser}`,{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"warning"
      }).catch((err)=>err)

      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消删除')
      }
      const { data: res } = await this.$http.delete('/api/deleteOrderByOrderId/'+orderId)

      if (res.code===505) return this.$message.error(res.message)
      this.$message.success('删除订单成功！')
      this.getMyorderList()
    }
  }
}
</script>

<style scoped>
.el-card{
  margin-top: 30px;
}
.el-row{
  margin-bottom: 30px;
}
.el-row .el-col:nth-child(1){
  font-weight: bold;
}
</style>