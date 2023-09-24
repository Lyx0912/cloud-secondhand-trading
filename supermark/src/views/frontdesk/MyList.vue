<template>
  <div style="background-color: #f7f7f7db;">
    <div v-for="item in list" :key="item.id">
          <div class="photo-list" style="float: left;background-color: white;border: 1px;border-radius: 30px;width: 22%;margin-right: 30px;height: 27rem;
            margin-top: 2%;">
              
                <h3 style="margin-top: 8%;margin-left: 8%;margin-bottom: 2%;width: 15rem;height: 2rem;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">{{item.name}}</h3>
                <router-link :to="{path:'/detail/'+item.id}">
                <img :src="item.defaultImg" alt="" style="height: 60%;width: 75%;margin-left: 2.5rem;">
                </router-link>
                <div style="margin-left: 8%;margin-top: 2%;size: 15px;font-size: 20px;">￥{{item.price}} 
                  <span style="border: solid crimson 1px;border-radius: 12px;color: brown;font-size: 2%;"><span style="margin: 2%;">优惠后￥{{item.price}}</span></span></div>
              <router-link :to="{path:'/nowConfirmcart/'+[[item.id]]}">
              <button class="el-dropdown-link" v-if="xianshi" style="border-radius: 30px;background-color: white;width: 80%;margin-left: 10%;
                border: solid 1px;height: 40px;margin-top: 5%;">
                  <h2 style="color: #575757;">立即购买</h2>
              </button>
              </router-link>
              <router-link :to="{path:'/login/'}">
              <button class="el-dropdown-link" v-if="!xianshi" style="border-radius: 30px;background-color: white;width: 80%;margin-left: 10%;
                border: solid 1px;height: 40px;margin-top: 5%;">
                  <h2 style="color: #575757;">立即登录</h2>
              </button>
              </router-link>
          </div>
        </div>
  </div>
</template>

<script>
export default {
name: "MyList",
  props:["list"],
  data:{
    return(){
      xianshi:sessionStorage.getItem('token')
    }
  },
  created(){
    this.xianshi= sessionStorage.getItem('token')
  },
  // methods:{
  //   // 跳转到商品详情的页面，根据id
  //   showGoods(id){
  //     // 跳转到详情页
  //     this.$router.push({
  //       path:"/detail/"+id
  //     })
  //   },
  // }
}
</script>
<style scoped>
*{
  margin: 0;
  padding: 0;
  list-style: none;
}

.myList ul li{
  width: 224px;
  height: 280px;
  float: left;
  z-index: 1;
  background: white;
  margin: 10px;
  transition: all .3s linear;
  position: relative;
}
.myList ul li a{
  text-decoration: none;
}
.myList ul li:hover{
  z-index: 2;
  box-shadow: 0 15px 30px rgba(0,0,0,.1);
  transform: translate3d(0,-2px,0);
}
.myList ul li img{
  display: block;
  width:160px;
  height: 160px;
  margin: 0 auto;
}
.myList ul li h2{
  margin: 25px 10px 0;
  font-size: 14px;
  font-weight: 400;
  color: #333;
  line-height: 19px;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul li h3{
  margin: 5px 10px;
  height: 18px;
  font-size: 12px;
  font-weight: 400;
  color: #b0b0b0;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul .price span{
  display: block;
   margin:10px auto;
  text-align: center;
  color: #FF6700;
}
.product-box .box-head .photo-list{
  float: left;
  height: 615px;
  width: 234px;
}
</style>