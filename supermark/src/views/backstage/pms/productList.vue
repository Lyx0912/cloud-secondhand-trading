<template>
<div class="product_box">
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>商品</el-breadcrumb-item>
    <el-breadcrumb-item>商品列表</el-breadcrumb-item>
  </el-breadcrumb>
  <el-card class="box-card" shadow="never">
    <div slot="header" class="clearfix">
      <i class="el-icon-search"></i>
       <span>筛选条件</span>
    </div>

    <el-row :gutter="20">
      <el-form :inline="true" size="small" :model="ruleForm" ref="ruleForm" label-width="140px" class="demo-form-inline">
      <el-col :span="24">
        <el-form-item label="商品编号" prop="goods_id">
          <el-input v-model="ruleForm.goods_id" placeholder="货号"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goods_name">
          <el-input v-model="ruleForm.goods_name" placeholder="商品名称"></el-input>
        </el-form-item>
        <el-form-item style="margin-left: 30px">
          <el-button type="primary" @click="getsearchgoodsList">查询</el-button>
          <el-button @click="resetForm()">重置</el-button>
        </el-form-item>
      </el-col>

      </el-form>
    </el-row>

  </el-card>

  <el-card class="operate-container" shadow="never">
    <i class="el-icon-tickets"></i>
    <span>数据列表</span>
    <el-button
        class="btn-add"
        @click="handleAddProduct()"
        size="mini"
        style="float: right"
    >
      添加
    </el-button>
  </el-card>
 <div class="table-container">
  <el-table
      :data="goodsList"
      border
      style="width: 100%"
      :default-sort="{ prop: 'commodityId', order: 'descending'}"
  >
    <el-table-column
        type="selection"
        width="60"
        align="center"
    ></el-table-column>
    <el-table-column
        label="商品ID"
        width="120"
        align="center"
    >
      <template slot-scope="scope">{{scope.row.commodityId}}</template>
    </el-table-column>

    <el-table-column label="商品图片" width="120" align="center">
      <template slot-scope="scope"><img style="height: 80px; width: 70px"  :src="scope.row.commodityImage"></template>
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
        label="价格"
        align="center"
        width="140"
    >
      <template slot-scope="scope">
        <p>￥{{scope.row.commodityPrice}}</p>
      </template>
    </el-table-column>

    <el-table-column
    label="库存"
    align="center"
    width="140"
    >
      <template slot-scope="scope">{{scope.row.stock}}</template>
    </el-table-column>

    <el-table-column
        label="在售状态"
        align="center"
        width="120"
    >
      <template slot-scope="scope">{{scope.row.commodityState}}</template>
    </el-table-column>

    <el-table-column
        label="操作"
        align="center"
    >
      <template slot-scope="scope">
        <p>
          <el-button
              size="mini"
              @click="handleUpdateProduct(scope.$index, scope.row);dialogFormVisible = true">编辑
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row.commodityId)">删除
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
        :page-size="listQuery.pageSize"
        :page-sizes="[5,10,15]"
        :current-page.sync="listQuery.pageNum"
        :total="total">
    </el-pagination>
  </div>

  <el-dialog :visible.sync="dialogFormVisible" v-if="dialogFormVisible">
    <el-form :model="updateGoods">
      <el-form-item label="商品名称" :label-width="formLabelWidth">
        <el-input v-model="updateGoods.goodsName" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="商品价格" :label-width="formLabelWidth" type="number">
        <el-input v-model="updateGoods.goodsPrice" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="商品详情" :label-width="formLabelWidth">
        <el-input v-model="updateGoods.goodsDetail" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="商品库存" :label-width="formLabelWidth" type="number">
        <el-input v-model="updateGoods.goodsStock" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="商品抢购时间:" :label-width="formLabelWidth">

        <el-date-picker
            v-model="value"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>

      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button  type="warning" @click="handleUpdateData">确定修改</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
export default {
name: "productList",
  data(){
  return{
    // 定义数据源
    goodsList:[],
    ruleForm: {
      goods_name:"",
      goods_id:"",
    },
    value:[],
    formLabelWidth: '120px',
    updateGoods:{
      goodsId:"",
      goodsName:"",
      goodsPrice:"",
      goodsDetail:"",
      goodsStock:"",
      goodsStartKill:"",
      goodsEndKill:""
    },
    total:0,
    listQuery:{
      pageNum:1,
      pageSize:5
    },
    dialogFormVisible:false
  }
  },
  created() {
    this.master = JSON.parse(sessionStorage.getItem("user")).username;
    this.getMygoodsList();
  },
  methods:{
    //ajax获取商品信息
    getMygoodsList(){
      this.$http.post("/api/getCommodityList",{
         pageNum:this.listQuery.pageNum,
         pageSize:this.listQuery.pageSize,
      }).then(res=>{
        this.goodsList=res.data.commodityList;
        this.total=res.data.commodityCount;
      })
    },
    handleChange(){
      if(this.ruleForm.goods_cat.length!==2){
        this.ruleForm.goods_cat=[];
      }
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    },
    handleAddProduct(){
      this.$router.push("/addProduct");
    },
    handleSizeChange(newSize){
      this.listQuery.pageSize=newSize;
      //重新调用商品列表
      this.getMygoodsList();
    },
    handleCurrentChange(pageNum){
      this.listQuery.pageNum=pageNum;
      //重新调用商品列表
      this.getMygoodsList();
    },
    getsearchgoodsList(){
      this.$http.post("/api/getCommoditysByKeyWord",{
        commodityName:this.ruleForm.goods_name,
        commodityId:this.ruleForm.goods_id,
        pageNum:this.listQuery.pageNum,
        pageSize:this.listQuery.pageSize,
      }).then(res=>{
        this.goodsList=res.data.commodityList;
        this.total=res.data.commodityCount;
      })
    },
    handleUpdateProduct(index,row){
      this.updateGoods.goodsName=row.commodityName;
    this.updateGoods.goodsDetail=row.commodityDescribe;
    this.updateGoods.goodsPrice=row.commodityPrice;
    this.updateGoods.goodsStock=row.stock;
    this.value[0]=row.startTime;
    this.value[1]=row.endTime;
    this.updateGoods.goodsId=row.commodityId;
    },
    handleUpdateData(){
      //发起Ajax请求
      const that=this;
      this.$http.post("/api/updateCommodity",{
        commodityName:this.updateGoods.goodsName,
        commodityDescribe:this.updateGoods.goodsDetail,
        commodityPrice:this.updateGoods.goodsPrice,
        stock:this.updateGoods.goodsStock,
        startTime:this.value[0],
        endTime:this.value[1],
        commodityId:this.updateGoods.goodsId
      }).then(res=>{
        if(res.data.code===214){
          that.dialogFormVisible=false;
          return this.$message.success(res.data.message);
        }
        this.$message.error(res.data.message);
      })
    },
    async handleDelete(goodsId) {

      const confirmResult= await this.$confirm("您确认要删除该商品吗？",`${this.master}`,{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"warning"
      }).catch((err)=>err)
      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消删除')
      }
      const { data: res } = await this.$http.delete('/api/deleteCommodity/'+goodsId)
      if (res.code===505) return this.$message.error(res.message)
      this.$message.success('删除商品成功！')
      this.getMygoodsList();
    }
  }
}
</script>

<style scoped>
.product-box{
  background: #b0b0b0;
}
.el-card{
  margin-top: 30px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>