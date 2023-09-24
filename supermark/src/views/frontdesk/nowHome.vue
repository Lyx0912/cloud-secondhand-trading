<template>
    <div class="app">
    <!--      顶部导航栏-->
          <div class="nav-topbar">
            <div class="container">
              <div></div>
              <div class="topbar-user">
                <el-dropdown
                    trigger="click"
                    v-if="username"
                    style="margin-right: 20px"
                    @command="handleCommand"
                >
                 <span class="el-dropdown-link">
                  欢迎！{{username}}<i class="el-icon-arrow-down el-icon--right"></i>
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
    </div>
</template>
    
    <script>
    
    export default {
    name: "nowhome",
    
    beforeUpdate() {
      this.activeIndex=this.$route.path;
    },
      data(){
      return{
        activeIndex:"",
        search:{
          query:""
        },
        username:""
      };
      },
      activated() {
      if(sessionStorage.getItem("token")){
        this.username=this.$store.getters.getUser;
      }
      },
      methods: {
    
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
          this.search.query="";
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