<template>
<div class="activityshow">
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>订单</el-breadcrumb-item>
    <el-breadcrumb-item>活动公示</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card class="box-card" shadow="never">
    <div slot="header" class="clearfix">
      <i class="el-icon-search"></i>
      <span>筛选条件</span>
    </div>

    <el-row :gutter="20">
      <el-col :span="4">
        <el-select v-model="value" placeholder="请选择">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
        </el-select>
      </el-col>
     <el-col :span="4">
       <el-button type="primary" @click="getsearchdiscount">查询</el-button>
       <el-button @click="resetForm()">重置</el-button>
     </el-col>

    </el-row>
  </el-card>

  <el-card class="operate-container" shadow="never">
    <i class="el-icon-tickets"></i>
    <span>数据列表</span>
  </el-card>

  <el-row :gutter="20">
    <el-col :span="13.2" :offset="6">
<!--      <div class="table-container">-->
      <div v-if="value==0">
        <el-table
          :data="showkillList"
          border
          style="width: 100%"
          :default-sort="{ prop: 'orderId', order: 'descending'}"
      >
        <el-table-column
            type="index"
            width="60"
            align="center"
        >
        </el-table-column>

        <el-table-column
            label="客户ID"
            align="center"
            width="200"
        >
          <template slot-scope="scope">
            <p>{{scope.row.uid}}</p>
          </template>
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
            label="手机号码"
            align="center"
            width="160"
        >
          <template slot-scope="scope">{{scope.row.phone}}</template>
        </el-table-column>

        <el-table-column
            label="商品折扣"
            align="center"
            width="160"
        >
          <template slot-scope="scope">
            <span v-if="scope.$index<100"><el-tag>一折</el-tag></span>
            <span v-else-if="scope.$index>=100 && scope.$index<500"><el-tag type="success">五折</el-tag></span>
            <span v-else><el-tag type="warning">八折</el-tag></span>
          </template>


        </el-table-column>
      </el-table>
      </div>

      <div v-if="value==1">
        <el-table
          :data="showkillList"
          border
          style="width: 100%"
          :default-sort="{ prop: 'orderId', order: 'descending'}"
      >
        <el-table-column
            type="index"
            width="60"
            align="center"
        >
        </el-table-column>

        <el-table-column
            label="客户ID"
            align="center"
            width="200"
        >
          <template slot-scope="scope">
            <p>{{scope.row.uid}}</p>
          </template>
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
            label="手机号码"
            align="center"
            width="160"
        >
          <template slot-scope="scope">{{scope.row.phone}}</template>
        </el-table-column>

        <el-table-column
            label="商品折扣"
            align="center"
            width="160"
        >
          <template slot-scope="scope">
            <span><el-tag>一折</el-tag></span>
          </template>


        </el-table-column>

      </el-table>
      </div>
      <div v-if="value==2">
        <el-table
          :data="showkillList"
          border
          style="width: 100%"
          :default-sort="{ prop: 'orderId', order: 'descending'}"
      >
        <el-table-column
            type="index"
            width="60"
            align="center"
        >
        </el-table-column>

        <el-table-column
            label="客户ID"
            align="center"
            width="200"
        >
          <template slot-scope="scope">
            <p>{{scope.row.uid}}</p>
          </template>
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
            label="手机号码"
            align="center"
            width="160"
        >
          <template slot-scope="scope">{{scope.row.phone}}</template>
        </el-table-column>

        <el-table-column
            label="商品折扣"
            align="center"
            width="160"
        >
          <template slot-scope="scope">
            <span><el-tag type="success">五折</el-tag></span>
          </template>


        </el-table-column>

      </el-table>
      </div>

      <div v-if="value==3">
        <el-table
            :data="showkillList"
            border
            style="width: 100%"
            :default-sort="{ prop: 'orderId', order: 'descending'}"
        >
          <el-table-column
              type="index"
              width="60"
              align="center"
          >
          </el-table-column>

          <el-table-column
              label="客户ID"
              align="center"
              width="200"
          >
            <template slot-scope="scope">
              <p>{{scope.row.uid}}</p>
            </template>
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
              label="手机号码"
              align="center"
              width="160"
          >
            <template slot-scope="scope">{{scope.row.phone}}</template>
          </el-table-column>

          <el-table-column
              label="商品折扣"
              align="center"
              width="160"
          >
            <template slot-scope="scope">
              <span ><el-tag type="warning">八折</el-tag></span>
            </template>


          </el-table-column>

        </el-table>
      </div>
<!--      </div>-->
    </el-col>
  </el-row>
</div>
</template>

<script>
export default {
name: "activityshow",
  data(){
  return{
    options: [
      {
        value:0,
        label: "全部"
      },
      {
        value: 1,
        label: '一折'
      }, {
        value: 2,
        label: '五折'
      }, {
        value: 3,
        label: '八折'
      }
    ],
    value:0,
    ruleForm:{
      user_name:"",
      user_id:"",
      user_phone:""
    },
   showkillList:[
   ],
    // showkillList2:[],
    // showkillList3:[],
    queryInfo:{
      pagenum:1,
      pagesize:100
    },
    total:0
  }
  },
  created() {
  this.getMysearchkillList();
  },
  methods:{
    resetForm(){
     this.value=0;
     this.getsearchdiscount();
    },
    getsearchdiscount(){
     this.$http.post("/api/getLuck/"+this.value).then(res=>{
       this.showkillList=res.data
     })
    },
    getMysearchkillList(){
       this.$http.post("/api/getLuck/0").then(res=>{

         this.showkillList=res.data
       })

    }
    // handleSizeChange(newSize){
    //   this.getMysearchkillList();
    // },
    // handleCurrentChange(newPage){
    //   console.log(newPage);
    // }
  }
}
</script>

<style scoped>
.el-card{
  margin-top: 30px;
}
/*.el-table{*/
/*  margin-left:1.2rem;*/
/*}*/

</style>