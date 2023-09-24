<template>
<div class="app">
<!--      顶部导航栏-->
      <div class="nav-topbar">
        <div class="container">
          <div></div>
          <div class="topbar-user">
            <el-dropdown
                trigger="click"
                v-if="nickname"
                style="margin-right: 20px"
                @command="handleCommand"
            >
             <span class="el-dropdown-link">
              欢迎！{{nickname}}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="b">个人中心</el-dropdown-item>
                <el-dropdown-item command="a">退出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <a href="javascript:;" v-if="!username" @click="login()">登录</a>
            <a href="javascript:;"  @click="reg()">注册</a>
            <a href="/order" v-if="username">我的订单</a>
            <a href="/homepage" v-if="username=='admin'">返回后台</a>
          </div>
        </div>
      </div>
<!--  顶部导航栏end-->
<!--  头部导航栏-->
  <div class="nav-header">
    
    
    <div class="container">
      
      <div class="header-menu">
        <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            active-text-color="#409eff"
            router
        >
          <div></div>
          <el-menu-item index="/index">首页</el-menu-item>
          <el-menu-item index="/goods">全部商品</el-menu-item>
<!--          <el-menu-item index="/miao">抢购商品</el-menu-item>-->
        </el-menu>
      </div>
      <!--   搜索框-->
      <div class="header-search">
      <div class="search">
        <el-input
            placeholder="请输入你想要的商品"
            class="input-with-select"
            v-model="search.query"
            @keyup.enter.native="searchGoods"
            suffix-icon="el-icon-search"
        >
        </el-input>
      </div>
      </div>
      <!--    搜索框end-->
    </div>
  </div>
<!--头部导航栏end-->

    <div style="top:50px; position: relative">
      <keep-alive>
          <router-view />
      </keep-alive>
    </div>
</div>
</template>

<script>

export default {
name: "home",

beforeUpdate() {
  this.activeIndex=this.$route.path;
},
  data(){
  return{
    fanhui: false,
    activeIndex:"",
    search:{
      query:""
    },
    username:"",
    nickname:""
  };
  },
  activated() {
  if(sessionStorage.getItem("token")){
    this.username=sessionStorage.getItem('username');
    this.nickname =sessionStorage.getItem('nickname')=="null"?this.$store.getters.getUser:sessionStorage.getItem('nickname')
    // console.log(this.nickname+""==null)
    // console.log(this.nickname=="null")
  }
  },
  created(){
    this.getfanhui();
  },
  methods: {
    getfanhui(){
      this.fanhui = window.location.pathname!="/index";
      console.log(this.fanhui)
    },

    login(){
      this.$router.push("/login")
    },
    handleCommand(command) {
    if(command=='b'){
      this.$router.push('/userInfo')
    }
    if(command=='a'){
      sessionStorage.clear()
      this.$message.success("账号退出成功")
      this.$router.push("/login")
      // 删除vuex中的数据,让当前的界面刷新
      // window.location.reload();
    }
    },
    reg(){
     this.$router.push('/register')

    },
    searchGoods(){
    if(this.search.query!=""){
      this.$router.push({path:"/goods",query:{search:this.search.query}});
      this.$http.get("/api/cloud-goods/goods/es/list?pageNo="+this.pageNo+"&pageSize="+this.pageSize+"&category_id="+this.category_id).
      then(res=>{
        console.log(res.data.data.list);
        this.product = res.data.data.list;
        this.total=res.data.data.total;
        this.search.query="";
      })
    }
    },

  }
}
</script>

<style>
*{
  margin: 0;
  padding:0;
  list-style: none;
  border:0;
}
.app .nav-topbar{
  height: 39px;
  line-height: 39px;
  background:#333333;
  color:#B0B0B0;
}
.app .nav-topbar .container{
  width: 80%;
  display: flex;
  align-items:center;
  justify-content:space-between;
}
.app .nav-topbar .container a{
  /*display: inline-block;*/
  color: #B0B0B0;
  margin-right: 17px;
  font-weight: normal;
  text-decoration: none;
}
.nav-header .container{
  margin: 0 auto -50px auto;
  height: 112px;
  display: flex;
  align-items:center;
  justify-content:center;
}
.el-dropdown-link {
  cursor: pointer;
  color: #B0B0B0;
}
.header-search{
  width: 319px;
  margin-left: 60px;
}
</style>