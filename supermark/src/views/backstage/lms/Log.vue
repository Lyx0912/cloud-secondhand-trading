<template>
<div class="log_box">
<el-breadcrumb separator="/">
  <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
  <el-breadcrumb-item>日志</el-breadcrumb-item>
  <el-breadcrumb-item>登录日志</el-breadcrumb-item>
</el-breadcrumb>
<el-card class="box-card" shadow="never">
  <div slot="header" class="clearfix">
    <i class="el-icon-search"></i>
    <span>筛选条件</span>
  </div>

  <el-row :gutter="20">
    <el-form :inline="true" size="small" :model="ruleForm" ref="ruleForm" label-width="140px" class="demo-form-inline">
      <el-col :span="24">
        <el-form-item label="登录地址" prop="login_ip">
          <el-input v-model="ruleForm.login_ip" placeholder="请输入登录地址"></el-input>
        </el-form-item>
        <el-form-item label="用户名称" prop="username">
          <el-input v-model="ruleForm.username" placeholder="请输入用户名称"></el-input>
        </el-form-item>
        <el-form-item style="margin-left: 30px">
          <el-button type="primary" @click="getsearchLogsList">查询</el-button>
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
      :data="logList"
      border
      style="width: 100%"
      :default-sort="{ prop: 'logId', order: 'descending'}"
  >
    <el-table-column
        type="selection"
        width="60"
        align="center"
    ></el-table-column>

    <el-table-column
        label="日志编号"
        width="100"
        align="center"
    >
      <template slot-scope="scope">{{scope.row.logId}}</template>
    </el-table-column>

    <el-table-column
        label="用户名称"
        align="center"
        width="140"
    >
      <template slot-scope="scope">
        <p>{{scope.row.username}}</p>
      </template>
    </el-table-column>

    <el-table-column
        label="登录地址"
        align="center"
        width="120"
    >
      <template slot-scope="scope">
        <p>{{scope.row.ip}}</p>
      </template>
    </el-table-column>

    <el-table-column
        label="登录地点"
        align="center"
        width="140"
    >
      <template slot-scope="scope">{{scope.row.address}}</template>
    </el-table-column>

    <el-table-column
        label="客户端类型"
        align="center"
        width="120"
    >
      <template slot-scope="scope">{{scope.row.deviceType}}</template>
    </el-table-column>

    <el-table-column
        label="浏览器类型"
        align="center"
        width="120"
    >
      <template slot-scope="scope">{{scope.row.browser}}</template>
    </el-table-column>

    <el-table-column
        label="操作系统"
        align="center"
    >
      <template slot-scope="scope">{{scope.row.opSystem}}</template>
    </el-table-column>

    <el-table-column
        label="登录状态"
        align="center"
    >
      <template slot-scope="scope">
        <span v-if="scope.row.loginState=='登录成功'"><el-tag type="success">{{scope.row.loginState}}</el-tag></span>
        <span v-if="scope.row.loginState=='密码错误'"><el-tag type="danger">{{scope.row.loginState}}</el-tag></span>
        <span v-if="scope.row.loginState=='系统异常'"><el-tag type="warning">{{scope.row.loginState}}</el-tag></span>
      </template>
    </el-table-column>

    <el-table-column
        label="访问日期"
        align="center"
    >
      <template slot-scope="scope">{{scope.row.loginDate|dateFormat}}</template>
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
</div>
</template>

<script>
export default {
name: "Log",
  data(){
  return{
    logList:[],
    ruleForm:{
      login_ip:"",
      username:""
    },
    listQuery:{
      pageNum:1,
      pageSize:5
    },
    total:0,
    state:"",
  }
  },
  watch:{
  state(val){
    this.state=val;
  }
  },
  created() {
    this.getLogsList();
  },
  methods:{
    handleSizeChange(newSize){
      this.listQuery.pageSize=newSize;
      //重新调用商品列表
      this.getLogsList();
    },
    handleCurrentChange(pageNum){
      this.listQuery.pageNum=pageNum;
      //重新调用商品列表
      this.getLogsList();
    },
    getLogsList(){
      const that=this;
      this.$http.post("/api/getAllLoginLogs",{
        pageNum:this.listQuery.pageNum,
        pageSize:this.listQuery.pageSize,
      }).then(res=>{
        this.logList=res.data.loginLogs;
        this.total=res.data.logCounts;
        console.log(this.logList[1].loginState);

      })
    },
    getsearchLogsList(){
      this.$http.post("/api/getLoginLogByUsernameOrIp",{
        username:this.ruleForm.username,
        ip:this.ruleForm.login_ip,
        pageNum:this.listQuery.pageNum,
        pageSize:this.listQuery.pageSize,
      }).then(res=>{
        this.logList=res.data.loginLogs;
        this.total=res.data.logCounts;
      })
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    },
  }
}
</script>

<style scoped>

</style>