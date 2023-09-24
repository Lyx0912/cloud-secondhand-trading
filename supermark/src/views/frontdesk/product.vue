<template>
  <div class="product">
    <div class="breadcrumb">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item to="/">首页</el-breadcrumb-item>
        <el-breadcrumb-item>全部结果</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==1">手机</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==2">电脑</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==8">电视</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==3">洗衣粉</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==4">洗洁精</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==7">洗碗机</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==5">衣服</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==6">鞋子</el-breadcrumb-item>
        <el-breadcrumb-item v-if="pid==9">香蕉</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 主要内容区 -->
    <div class="main">
      <div class="list">
        <MyList :list="product" v-if="product.length>0"></MyList>
        <div v-else class="none-product">抱歉没有找到相关的商品，请看看其他的商品</div>
      </div>
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :page-size="pageSize"
            :total="total">
        </el-pagination>
      </div>
      <!-- 分页END -->
    </div>
    <!-- 主要内容区END -->
  </div>
</template>

<script>
import MyList from "@/views/frontdesk/MyList";
export default {
  name: "product",
  data(){
    return{
      pid:this.$route.params.pid,
      productName:"",
      product:"",
      pageNum:1,
      pageSize:15,
      total:0
    }
  },
  components:{
    MyList
  },
  activated() {
    this.total=0;
    this.pageNum=1;
    if(this.$route.params.pid!==undefined){
      this.pid=this.$route.params.pid;
    }
  },
  created() {
    this.getData();
  },
  watch:{
    pid:function () {
      this.getData();
    },
  },
  methods:{
    backtop() {
      const timer = setInterval(function() {
        const scrollTop = window.pageYOffset||document.documentElement.scrollTop || document.body.scrollTop;
        const ispeed = Math.floor(-scrollTop / 5);
        document.documentElement.scrollTop = document.body.scrollTop = scrollTop + ispeed;
        if (scrollTop === 0) {
          clearInterval(timer);
        }
      }, 22);
    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum;
      this.getData();
      this.backtop();
    },

    getData(){
      const api="/api/getCommodityListByParentId";
      this.$http.post(api,{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        parentId: this.pid
      }).then(res=>{
        this.product = res.data.commodityList;
        this.total=res.data.commodityCount;
      })
    }
  }
}
</script>

<style scoped>
.product{
  background-color: #f5f5f5;
}

.el-tabs--card .el-tabs__header {
  border-bottom: none;
}
.product .breadcrumb {
  height: 50px;
  background-color: white;
}
.product .breadcrumb .el-breadcrumb {
  width: 1225px;
  line-height: 30px;
  font-size: 16px;
  margin: 0 auto;
}



.product .main {
  margin: 0 auto;
  max-width: 1225px;
}
.product .main .list {
  min-height: 650px;
  padding-top: 14.5px;
  margin-left: -13.7px;
  overflow: auto;
}
.product .main .pagination {
  height: 50px;
  text-align: center;
}
.product .main .none-product {
  color: #333;
  margin-left: 13.7px;
}
</style>