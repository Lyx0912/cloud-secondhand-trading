<template>
  <el-container class="userInfo">
    <el-header>
      <!--      顶部导航栏-->
        <div class="header">
          <div>
            <span>
              <img src="" alt="">
            </span>
          </div>
          <div class="topbar-user">
            <a href="javascript:;" @click="backhome()">返回商城</a>
            <a href="javascript:;"  @click="reg()">退出登陆</a>
          </div>
        </div>
      <!--  顶部导航栏end-->
    </el-header>
    <el-container>
      <el-aside width="200px"  style="background-color:#e9eef3">
        <!--  侧边导航栏-->
                  <el-menu
                      class="el-menu-vertical-demo"
                      @open="handleOpen"
                      @close="handleClose"
                      unique-opened
                      router
                      :default-active="activePath"
                  >


                    <el-submenu index="2">
                      <template slot="title"><i class="el-icon-edit"></i>个人中心</template>
                      <el-menu-item-group>
                        <el-menu-item index="/baseInfo" @click="saveNavState('/baseInfo')">基本信息</el-menu-item>
                        <el-menu-item index="/updatePass">安全设置</el-menu-item>
                      </el-menu-item-group>
                    </el-submenu>

                    <el-submenu index="3">
                      <template slot="title"><i class="el-icon-collection-tag"></i>我的交易</template>
                      <el-menu-item-group>
                        <el-menu-item index="/order">我的订单</el-menu-item>
                        <el-menu-item index="/releaseGoods">我的发布</el-menu-item>
                      </el-menu-item-group>
                    </el-submenu>

                  </el-menu>
                </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
name: "userInfo",
  data(){
     return{
       // 被激活的链接地址
       activePath:""
     }
  },
  created() {
  // this.activePath=window.sessionStorage.getItem("activePath")
  },
  methods: {
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    },
    // 保存链接的激活状态
    saveNavState(val){
     // window.sessionStorage.setItem("activePath",val);
     this.activePath=val
    },
    reg(){
      sessionStorage.clear()
      this.$message.success("账号退出成功")
      this.$router.push("/login")
    },
    backhome(){
      this.$router.push("/home")
    }
  },
  mounted() {

  }
}
</script>

<style scoped>
.userInfo{
  height: 100%;
}
.el-header{
  background:#333333;
}
.el-header .header{
  display: flex;
  justify-content:space-between;
  padding-left: 0;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0,0,0,.1);

}
.el-header .header .topbar-user  a{
  display: inline-block;
  color: #B0B0B0;
  margin-right:30px;
  font-weight: normal;
  text-decoration: none;
  margin-top:20px;
}

</style>