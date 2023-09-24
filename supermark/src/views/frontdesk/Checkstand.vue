<template>
  <div class="app">
    <!--      顶部导航栏-->
    <div class="nav-topbar">
      <div class="container">
        <div></div>
        <div class="topbar-user">
          <el-dropdown trigger="click" v-if="username" style="margin-right: 20px" @command="handleCommand">
            <span class="el-dropdown-link">
              欢迎！{{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="b">个人中心</el-dropdown-item>
              <el-dropdown-item command="a">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <a href="javascript:;" v-if="!username" @click="login()">登录</a>
          <a href="javascript:;" @click="reg()">注册</a>
          <a href="/order" v-if="username">我的订单</a>
          <a href="/homepage" v-if="username == 'admin'">返回后台</a>
        </div>
      </div>
    </div>
    <!--  顶部导航栏end-->
    <div style="background-color: white;height: 100%;">

      <div style="height: 60px;background-color: white;">


        <h3 style="margin-top: 30px;margin-left: 170px;color: #656565;">收银台</h3>
      </div>
      <div style="height: 20px;"></div>
      <div style="height: 4px;width: 85%;background-color: #32b934;margin-left: 7%;"></div>
      <!-- 订单超时 -->
      <div v-if="overtime" v-loading="listLoading">
        <div style="display: flex;justify-content:center;margin-top: 100px;">
          <div style="float: left;border-radius: 50%;width: 5rem;height: 5rem;background: #cecece;">
            <div style="height: 7px;"></div>
            <span style="font-size: 40px;margin: 33px;">!</span>
          </div>
          <div style="float: left;margin-left: 30px;">
            <div style="font-size: 20px;">抱歉，无法完成付款。</div>
            <div style="height: 40px;"></div>
            您的访问已超时，请关闭收银台并重新支付。(CAS01LT004)
          </div>
        </div>
        <div style="display: flex;justify-content:center;margin-top: 50px;">
          <button @click="backToTheMall" id="button" plain
            style="border-radius: 15px;background-color: #ffffff;border: #cecece solid 1px;" class="el-dropdown-link">
            <div style="font-size: 18px;margin: 18px;width: 150px;">回到商城</div>
          </button>
        </div>
      </div>
      <div style="height: 500px;" v-loading="listLoading" v-if="!overtime">
        <!--第一层-->
        <div style="background-color: white;width: 88%;margin-left: 6%;">
          <div style="height: 30px;"></div>
          <div style="margin-left: 4%;width: 100%;">
            <div style="float: left;width: 10%;">
              <img style="width: 60%;height: 5%;"
                src="https://res.vmallres.com/ips/pc/20230711/img/success_icon.png?20230711">
            </div>
            <div style="float: left;">
              <span style="font-size: 20px;color: #32b934;">订单提交成功，只差付款了~</span>
              <span style="margin-left: 15px;" v-if="!display">订单号：<span style="color: #e01515;">{{ id }}</span></span>
              <div style="margin-top: 8%;">
                请您在 <span style="color: #da2525;">30分00秒</span> 内完成支付，否则订单将自动取消。
              </div>
              <div style="height: 20px;"></div>
              <div v-if="display">
                <div style="font-size: 13px;color: #414141;">订单号：<span style="color: #e01515;">{{ id }}</span></div>
                <div style="font-size: 13px;color: #414141;">收货信息：<span
                    style="font-size: 12px;color: #414141;">{{ addr.consignee }} </span><span
                    style="font-size: 12px;color: #414141;"> {{ addr.mobile }}</span></div>
                <div style="font-size: 13px;color: #414141;">商品名称：<span style="font-size: 12px;color: #414141;">{{
                  goods.name }}</span></div>
                <div style="font-size: 13px;color: #414141;">购买时间：<span
                    style="font-size: 12px;color: #414141;">{{ addr.createTime }}</span></div>
              </div>

            </div>

            <div style="float: right;margin-right: 8%;">
              <span style="margin-left: 15px;">订单金额：</span><span
                style="color: #e01515;font-size: 18px;">￥{{ goods.price }}</span>
              <div style="margin-top: 19%;">
                <span style="float: right;" class="el-dropdown-link" @click="bdisplayNone">
                  订单详情<i v-if="!display" class="el-icon-caret-bottom el-icon--right"></i>
                  <i v-if="display" class="el-icon-caret-top el-icon--right"></i>
                </span>
              </div>
            </div>
          </div>

        </div>
        <div style="height: 40px;"></div>
        <!--第二层-->
        <div style="background-color: white;width: 85%;text-align: center;margin:auto;margin-left: 7%;float: left;">
          <div style="height: 30px;"></div>
          <el-tabs type="border-card">
            <el-tab-pane>
              <span slot="label">
                <div style="font-size: 15px;width: 150px;height: 60px;">常用</div>
              </span>
              <div style="height: 30px;"></div>
              <el-tooltip style="float: left;" class="item" effect="dark" content="支付宝支付" placement="top">
                <div @click="updatesty1('z1')" id="z1" :style="[updatesty]">
                  <div style="float: left;text-align:center;width: 220px;margin-top: 14px;"><span
                      style="margin-left: 20px;">支付宝支付</span></div>
                  <div v-if="xianshi1" style="float: right;margin-top: 21px;">
                    <div
                      style="width: 0px;height: 0px;border-bottom: 30px solid red;border-left: 30px solid transparent;bottom: -13px;">
                      <p style="position: relative;left: -13px;top: 10px;color: #f9f9f9;">√</p>
                    </div>
                  </div>

                </div>
              </el-tooltip>
              <el-tooltip style="float: left;" class="item" effect="dark" content="微信支付" placement="top">
                <div @click="updatesty1('z2')" id="z2" :style="[updatesty]">
                  <div style="float: left;text-align:center;width: 220px;margin-top: 14px;"><span
                      style="margin-left: 20px;">微信支付</span></div>
                  <div style="float: right;margin-top: 21px;">
                    <div v-if="xianshi2"
                      style="width: 0px;height: 0px;border-bottom: 30px solid red;border-left: 30px solid transparent;bottom: -13px;">
                      <p style="position: relative;left: -13px;top: 10px;color: #f9f9f9;">√</p>
                    </div>
                  </div>

                </div>
              </el-tooltip>
              <el-tooltip style="float: left;" class="item" effect="dark" content="花呗支付" placement="top">
                <div @click="updatesty1('z3')" id="z3" :style="[updatesty]">
                  <div style="float: left;text-align:center;width: 220px;margin-top: 14px;"><span
                      style="margin-left: 20px;">花呗支付</span></div>
                  <div style="float: right;margin-top: 21px;">
                    <div v-if="xianshi3"
                      style="width: 0px;height: 0px;border-bottom: 30px solid red;border-left: 30px solid transparent;bottom: -13px;">
                      <p style="position: relative;left: -13px;top: 10px;color: #f9f9f9;">√</p>
                    </div>
                  </div>

                </div>
              </el-tooltip>
            </el-tab-pane>
            <el-tab-pane>
              <span slot="label">
                <div style="font-size: 15px;width: 150px;height: 160px;">平台支付</div>
              </span>
              <div>暂未开通</div>

            </el-tab-pane>
            <el-tab-pane>
              <span slot="label">
                <div style="font-size: 15px;width: 150px;height: 60px;">网银支付</div>
              </span>
              <div>暂未开通</div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- 第三层 -->
        <div>
          <el-button @click="orderPayment()" type="danger" style="float: right;margin-right: 10%;margin-top: 30px;">
            <div style="width: 180px;font-size: 20px;">确认付款</div>
          </el-button>
        </div>
      </div>

    </div>

  </div>
</template>
    
<script>

export default {
  name: "nowhome",

  beforeUpdate() {
    this.activeIndex = this.$route.path;
  },
  data() {
    return {
      overtime: true,
      listLoading: true,
      listLoadingz: true,
      style1: { display: 'none' },
      displaybianji: false,
      displaybianji1: false,
      display: false,
      id: '',
      activeIndex: "",
      search: {
        query: ""
      },
      username: "",
      goods: {},
      addrs: {},
      addr: {},
      addrss: {},
      updatesty: "",
      xianshi1: true,
      xianshi2: false,
      xianshi3: false,
      zhifu: 1
    };
  },
  watch: {
    id: function (val) {
      this.listLoading = true;
      this.getOrderId();
    }
  },
  activated() {
    if (sessionStorage.getItem("token")) {
      this.username = this.$store.getters.getUser;
    }
    if (this.$route.query.orderSn != undefined) {
      this.id = this.$route.query.orderSn;
    }
  },
  created() {
    this.getOrderId();
  },
  methods: {
    orderPayment() {
      this.listLoading = true;
      this.$http.post("/api/cloud-order/order/payment",
        { orderSn: this.id, payType: this.zhifu, memberId: JSON.parse(sessionStorage.getItem("user_id")) }
      ).then(res => {
        console.log(res.data)
        if (res.data.data == 1) {
          this.overtime = false;
          this.$notify.success({
            title: "付款成功",
          });
          this.$router.push("/order")
        } else {
          this.overtime = true;
          this.listLoading = false;
        }


      })

    },
    backToTheMall() {
      this.$router.push("/index")
    },
    transpositionaddr(index) {
      this.addrss = this.addrs[index];
      this.addrs.splice(index, 1);
      this.addrs.push(this.addr);
      this.addr = this.addrss;
    },
    updatesty1(id) {
      var z = document.getElementById("z1");
      var z2 = document.getElementById("z2");
      var z3 = document.getElementById("z3");
      if (id == 'z1') {
        z.style.border = "red solid 1px";
        z2.style.border = "#656565 solid 1px";
        z3.style.border = "#656565 solid 1px";
        this.xianshi1 = true;
        this.xianshi2 = false;
        this.xianshi3 = false;
        this.zhifu = 1
      }
      if (id == 'z2') {
        z2.style.border = "red solid 1px";
        z.style.border = "#656565 solid 1px";
        z3.style.border = "#656565 solid 1px";
        this.xianshi2 = true;
        this.xianshi1 = false;
        this.xianshi3 = false;
        this.zhifu = 2
      }
      if (id == 'z3') {
        z3.style.border = "red solid 1px";
        z.style.border = "#656565 solid 1px";
        z2.style.border = "#656565 solid 1px";
        this.xianshi3 = true;
        this.xianshi2 = false;
        this.xianshi1 = false;
        this.zhifu = 0
      }
      console.log(this.zhifu)

    },
    bdisplaybianji() {
      this.displaybianji = !this.displaybianji;
    },
    bdisplaybianji1(index) {
      var id = document.getElementById("id" + index);
      if (this.displaybianji1) {
        // 修改索引为index的样式
        id.style.display = "none";
      } else {
        // 修改索引为index的样式
        id.style.display = "";
      }
      this.displaybianji1 = !this.displaybianji1;
      this.displaybianji = false;
    },
    bdisplayNone() {
      this.display = !this.display;
    },
    getOrderId() {
      var id = this.$route.query.orderSn;
      console.log(id)
      if (id != null) {
        var memberId = JSON.parse(sessionStorage.getItem("user_id"))
        this.$http.get("/api/cloud-order/order/" + id + "/" + memberId).then(res => {
          if (res.data.data != null) {
            this.overtime = false;
            this.goods = res.data.data.goodsDTO
            this.addr = res.data.data.orderAddr[0]
            console.log(this.goods)
            console.log(this.addr)
            this.listLoading = false;
          } else {
            this.overtime = true;
            this.listLoading = false;
          }
        });

      }

    },
    login() {
      this.$router.push("/login")
    },
    handleCommand(command) {
      if (command == 'b') {
        this.$router.push('/userInfo')
      }
      if (command == 'a') {
        sessionStorage.clear()
        this.$message.success("账号退出成功")
        this.$router.push("/login")
        // 删除vuex中的数据,让当前的界面刷新
        // window.location.reload();
      }
    },
    reg() {
      this.$router.push('/register')

    },
    searchGoods() {
      if (this.search.query != "") {
        this.$router.push({ path: "/goods", query: { search: this.search.query } });
        this.search.query = "";
      }
    },

  }
}
</script>
    
<style>
#button:hover {
  border: #8bf39b solid 1px;
}

#z3,
#z2 {
  border: #656565 solid 1px;
  height: 50px;
  width: 250px;
  background-color: rgb(255, 255, 255);
  margin-left: 20px;
}

#z1 {
  border: red solid 1px;
  height: 50px;
  width: 250px;
  background-color: rgb(255, 255, 255);
  margin-left: 20px;
}

#z3:hover,
#z2:hover,
#z1:hover {
  color: #B0B0B0;
}

.app .nav-topbar {
  height: 39px;
  line-height: 39px;
  background: #333333;
  color: #B0B0B0;
}

#adddiv:hover {
  color: #fb1b1b;
}

.addrdiv:hover {
  color: #acacac;
}

.app .nav-topbar .container {
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.app .nav-topbar .container a {
  /*display: inline-block;*/
  color: #B0B0B0;
  margin-right: 17px;
  font-weight: normal;
  text-decoration: none;
}

.nav-header .container {
  height: 112px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #B0B0B0;
}

.header-search {
  width: 319px;
  margin-left: 60px;
}</style>