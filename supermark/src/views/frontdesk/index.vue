<template>
  <div class="index" id="index">
  <!--  主题区域侧边栏-->
  <div class="main">
    <div class="container">
      <div class="swiper-box">

        <div class="nav-menu">
          <!--          获取一级菜单-->
          <ul class="menu-wrap" v-for="(kind,i) in cateList" :key="i">
            <li class="menu-item">
              <a href="javascript:;">{{kind.parentClassification.parentClassificationName}}</a>
              <div class="children">
                <ul v-for="(item,j) in kind.classification" :key="j">
                  <li>
                    <router-link :to="{ path: '/product/' +item.classificationId}">
                      {{item.classificationName}}
                    </router-link>
                  </li>
                </ul>
              </div>
            </li>
          </ul>
        </div>

        <div class="block" style="width: 1228px;">
          <el-carousel trigger="click" height="451px" arrow="never">
            <el-carousel-item v-for="(item,index) in imageArr" :key="index">
              <img :src="item.imageUrl" @click="showGoods(item.targetUrl)" style="width: 1228px; height: 451px"/>
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="activity">
        </div>
      </div>
    </div>
  </div>

  <!--  主题区域侧边栏end-->
  <!--  商品展示区域-->
  <div class="product-box">
    <div class="container">
    <div class="layout">
    <div class="log">
      <img src="@/assets/img/active.jpg" alt="">
    </div>
      <div class="active-right">
       <p class="active-font"><img src="@/assets/img/shopActive.png" alt=""></p>
       <router-link to="/activityNotic" class="active-link">
         闲置大甩卖
       </router-link>
      </div>
    </div>

      <!-- <div class="box-head">
        <div class="title">手机</div>
      </div> -->
      <div style=""></div>

    <!-- 主要内容区 -->
    <div class="box-head" style="margin-left: 7%;margin-right: 2%;height: 100%;">
        
        
        <MyIndexList :list="product" v-if="product.length>0"></MyIndexList>
        <div v-else class="none-product">抱歉没有找到相关的商品，请看看其他的商品</div>
      
    </div>
    </div>
  </div>

 <div class="footer" style="clear: left;">
   <p class="footer-con">
     <router-link to="/home">
       首页
     </router-link>|
     <router-link to="/goods">
       全部商品
     </router-link>|
     <router-link to="/userInfo">
       个人中心
     </router-link>
   </p>
 </div>
  </div>
</template>

<script>
import MyList from "@/views/frontdesk/MyList";
import xiaomiK40 from "@/assets/img/phone.png";
import television from "@/assets/img/television.png";
import cloth from "@/assets/img/cloth.jpg";
import shoe from "@/assets/img/shoe.jpg";
import kitchen from "@/assets/img/kitchen2.gif";
import MyIndexList from "@/views/frontdesk/MyIndexList";
export default {
  name: "home",
  components: {
    MyList,MyIndexList
  },
  beforeUpdate() {
    this.activeIndex = this.$route.path;
  },
  data() {
    return {
      fanhui: false,
      commodityIdArr:[1,2,3,4,5],
      imageArr:[{url:xiaomiK40},{url:kitchen},{url: television},{url:cloth},{url:shoe}],
      activeIndex:"",
      cateList: [],//侧边栏商品列表
      phoneList: [],//手机商品列表
      category_id:1,
      pageNum:1,
      pageSize:10,
      product: "", // 商品列表
    };
  },
  created() {
    this.getProductList();
    this.getBannerList();
  },
  methods:{
    getBannerList(){
      this.fanhui = false;
      this.$http.get("/api/cloud-goods/banner/bannerList").then(res => {
        console.log(res.data.data);
        this.imageArr = res.data.data
      });
    },
    // 跳转到商品详情的页面，根据id
    showGoods(path){
      // 跳转到详情页
      this.$router.push({
        path: path
      })
    },
    getProductList() {
      //获取侧边栏列表
      this.$http.post("/api/getAllClassification").then(res => {
        this.cateList = res.data
      });

      // 获取手机商品列表
      this.$http.get("/api/cloud-goods/recommend/list?pageNo=1&pageSize=8").then(res => {
        console.log(res.data.data.list);
        this.product = res.data.data.list;

      });
    }
  }
}
</script>

<style scoped>
.index .main{
  display: flex;
  justify-content: center;
}
.index .main .nav-menu{
  position: absolute;
  width: 264px;
  height: 451px;
  z-index: 5;
  padding: 23px 0;
  background: #55585a7a;
  box-sizing: border-box;
}
.index .menu-wrap .menu-item{
  height: 43px;
  line-height: 40px;
}

.menu-item a{
  position: relative;
  display: block;
  text-decoration: none;
  font-size: 16px;
  color: #fff;
  padding-left: 30px;
}
.menu-item a:after{
  position: absolute;
  right: 30px;
  top: 17px;
  content: '';

}
.block img{
  cursor: pointer;
}
.menu-item:hover{
  background: #FF6600;
}
.menu-item:hover .children {
  display: block;
}
.menu-item .children{
  display: none;
  width: 926px;
  height: 451px;
  background:#fff;
  position: absolute;
  top:0;
  left: 264px;
  border: 1px solid #e5e5e5;
}
.menu-item .children ul{
  display: flex;
  justify-content: space-between;
  height: 75px;
}
.menu-item .children ul li{
  height: 75px;
  line-height: 75px;
  flex: 1;
  padding-left: 23px;
}
.menu-item .children ul li a{
  color: #333;
  font-size: 14px;
}


.product-box{
  background: #F5F5F5;
  padding: 30px 0 50px;
  height:789px;
}
.product-box .box-head{
  height: 58px;
  margin: 20px 0 0 0;
}
.product-box .box-head .title{
  float: left;
  font-size: 22px;
  font-weight: 200;
  line-height: 58px;
  color:#333;
}
.product-box .box-head .more{
  float: right;
}
.product-box .box-head .more a{
  font-size: 16px;
  line-height: 58px;
  color: #424242;
  text-decoration: none;
}
.product-box .box-head .more a:hover{
  color:#FF6600;
}
/*.product-box .box-head{*/
/*  height: 615px;*/
/*}*/
.product-box .box-head .photo-list{
  float: left;
  height: 615px;
  width: 234px;
}
.product-box .box-head .photo-list li{
  z-index: 1;
  width: 234px;
  height: 300px;
  margin-bottom: 14px;
  transition: all 0.2s linear;
}
.product-box .box-head .photo-list li:hover{
  z-index: 2;
  box-shadow: 0 15px 30px rgba(0,0,0,0.1);
  transform: translate3d(0,-2px,0);
}
.product-box .box-head .photo-list li img{
  width: 234px;
  height: 300px;
}
.product-box .box-head .photo-list img{
  width: 234px;
}
.product-box .container .layout{
   width: 1400px;
   height: 120px;
   background: #fff;
  border-radius: 10px;
  overflow: hidden;
  margin-left: 6rem;
}
.log{
  float: left;
  width: 1200px;
  height: 120px;
}
.active-right{
  width: 200px;
  height: 120px;
  float: right;
}
.active-font{
  height: 27px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 1px 1px rgba(0,0,0,0.1);
  font-weight: bold;
  color:#b0b0b0;
  padding:10px;
}
.active-font img{
  position: absolute;
  top:-94px;
}
.active-link{
  padding: 10px;
  height: 10px;
  display: inline-block;
  line-height: 10px;
  text-decoration: none;
}
.active-link:hover{
  color: #FF6600;
}
.footer{
  height: 200px;
  background: #2f2f2f;

}
.footer p,.footer a{
  padding: 80px 20px;
  margin-top: 20px;
  text-align: center;
  color:#b0b0b0;
}
.footer .footer-con a{
  text-decoration: none;
}
.footer .footer-con a:hover{
  color:#fff;
}
</style>