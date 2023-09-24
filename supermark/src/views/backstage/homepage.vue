<template>
  <el-container class="home-container">
      <!--  侧边导航栏-->
            <el-container>
<!--              侧边栏-->
              <el-aside :width="isCollapse?'64px':'200px'" style="background-color:#304156">
<!--                侧边栏菜单区域-->
                <el-menu
                    class="el-menu-vertical-demo"
                    unique-opened
                    background-color="#304156"
                    show-timeout="200"
                    text-color="#bfcbd9"
                    active-text-color="#409EFF"
                    router
                    :collapse="isCollapse"
                    :default-active="activePath"
                    :collapse-transition="false"
                >

                  <el-menu-item class="homelist" index="/homeList" @click="saveNavState('/homeList')">
                    <template slot="title">
                      <i class="el-icon-s-home"></i>
                      <span>首页</span>
                    </template>
                  </el-menu-item>

                  <el-submenu index="1">
<!--                    一级菜单-->
                    <template slot="title">
                      <i class="el-icon-s-goods"></i>
                      <span>商品</span>
                    </template>
<!--                    一级菜单end-->

<!--                    二级菜单-->
                    <el-menu-item index="/productList" @click="saveNavState('/productList')">
                      <template slot="title">
                        <i class="el-icon-s-unfold"></i>
                        <span>商品列表</span>
                      </template>
                    </el-menu-item>

                    <el-menu-item index="/addProduct" @click="saveNavState('/addProduct')">
                      <template slot="title">
                        <i class="el-icon-sell"></i>
                        <span>添加抢购商品</span>
                      </template>
                    </el-menu-item>

                    <el-menu-item  index="/commonProduct" @click="saveNavState('/commonProduct')">
                      <template slot="title">
                        <i class="el-icon-set-up"></i>
                        <span>添加普通商品</span>
                      </template>
                    </el-menu-item>
<!--                    二级菜单end-->
                  </el-submenu>

                  <el-submenu index="2">
                    <!--                    一级菜单-->
                    <template slot="title">
                      <i class="el-icon-s-order"></i>
                      <span>订单</span>
                    </template>
                    <!--                    一级菜单end-->

                    <!--                    二级菜单-->
                    <el-menu-item index="/orderList" @click="saveNavState('/orderList')">
                      <template slot="title">
                        <i class="el-icon-date"></i>
                        <span>订单列表</span>
                      </template>
                    </el-menu-item>

                    <el-menu-item index="/killtable" @click="saveNavState('/killtable')">
                      <template slot="title">
                        <i class="el-icon-chat-dot-square"></i>
                        <span>活动公示</span>
                      </template>
                    </el-menu-item>
                    <!--                    二级菜单end-->
                  </el-submenu>
                  <el-submenu index="3">
                    <!--                    一级菜单-->
                    <template slot="title">
                      <i class="el-icon-s-order"></i>
                      <span>营销</span>
                    </template>
                    <!--                    一级菜单end-->

                    <!--                    二级菜单-->
                    <el-menu-item index="/marketing" @click="saveNavState('/marketing')">
                      <template slot="title">
                        <i class="el-icon-date"></i>
                        <span>营销策略</span>
                      </template>
                    </el-menu-item>
                    <!--                    二级菜单end-->
                  </el-submenu>

                  <el-submenu index="4">
                    <!--                    一级菜单-->
                    <template slot="title">
                      <i class="el-icon-s-order"></i>
                      <span>日志</span>
                    </template>
                    <!--                    一级菜单end-->

                    <!--                    二级菜单-->
                    <el-menu-item index="/logs" @click="saveNavState('/logs')">
                      <template slot="title">
                        <i class="el-icon-date"></i>
                        <span>登录日志</span>
                      </template>
                    </el-menu-item>
                    <!--                    二级菜单end-->
                  </el-submenu>
                </el-menu>
                <!--                侧边栏菜单区域end-->
              </el-aside>
<!--              侧边栏end-->
              <el-main>
                <div class="header">
                  <div>
                    <span class="toggle-button" @click="toggleCollapse">|||</span>
                  </div>
                  <div class="topbar-user">
                    <a href="javascript:;" @click="backhome()">返回商城</a>
                    <a href="javascript:;"  @click="reg()">退出登陆</a>
                  </div>
                </div>
                  <!--  顶部导航栏end-->
                <router-view></router-view>
              </el-main>
            </el-container>
      <!--  侧边导航栏end-->
  </el-container>
</template>

<script>
export default {
name: "homepage",
  data(){
  return{
    isCollapse:false,
    activePath:""
  }
  },
  created() {
  this.activePath=window.sessionStorage.getItem("activePath");
  this.a();
  },
  methods: {
   a(){
     const name=this.$store.getters.getUser;
     if(name!=="admin"){
       this.$router.push("/home");
     }
   },
    backhome(){
      this.$router.push("/home");
    },
    reg(){
      sessionStorage.clear();
      this.$message.success("账号退出成功");
      this.$router.push("/login");
    },
    toggleCollapse(){
      this.isCollapse=!this.isCollapse;
    },

    saveNavState(val){
      this.activePath=val
      window.sessionStorage.setItem("activePath",val);
    }
  }
}
</script>

<style scoped>
.home-container{
  height: 100%;
}
.el-aside .el-menu{
  border-right: 0;
}
.home-container .header{
  /*background:#333333;*/
  display: flex;
  justify-content:space-between;
  padding-left: 0;
  align-items: center;
  color:#B0B0B0;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0,0,0,.1);
}
.home-container .header .toggle-button{
  margin-right: 20px;
  color:#000;
  font-size:15px;
  line-height: 24px;
  font-weight:bolder;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
.home-container .topbar-user  a{
  /*display: inline-block;*/
  color: #B0B0B0;
  margin-right:30px;
  font-weight: normal;
  text-decoration: none;
}

.home-container .el-menu .el-menu-item{
  background-color: #1f2d3d!important;
}
.home-container .el-menu .homelist{
  background-color:#304156!important;
}
.home-container .el-menu .el-menu-item:hover{
  background-color: #001528!important
}
.home-container .el-menu .homelist:hover{

}
</style>

