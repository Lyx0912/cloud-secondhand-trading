<template>
<div class="box">
  
 <img style="float: left;" src="~assets/img/a.png" class="wave">
 <router-link :to="{path:'/index'}">
    <p style="float: right;margin-top: 3rem;font-size: 2rem;margin-right: 3rem;" class="register">回到商城</p>
  </router-link>
  <div class="login-container">
    <img style="float: left;" src="~assets/img/bei.svg" class="img">
    <div class="login-box">
      <div class="avater-box">
        <img src="~assets/img/tou.svg" alt="">
      </div>
<!--      登陆表单-->
      <el-form label-width="70px"  ref="loginFromRef" class="login-form" :model="form" :rules="rules">
        <el-form-item label="用户:" prop="username">
          <el-input prefix-icon="el-icon-user"  v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="密码:"  prop="password">
          <el-input @keyup.enter.native="login" prefix-icon="el-icon-lock"  type="password" v-model="form.password"></el-input>
        </el-form-item>
        <p class="register" @click="register">免费注册</p>
        
        <el-form-item class="btn">
          <el-button type="primary" round class="login-btn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</div>
</template>

<script>
import {mapActions}from "vuex";
import jsonp from "jsonp";
export default {
name: "Login",
  data() {
    var validateUser = async (rule, value, callback) => {
      //判断用户是否为空，如果为空返回提示信息，否则就进行服务器请求，并把当前用户名携带给服务器
      if (value=="") {
        callback(new Error("请输入登录名称"))
      }else{
        if(value){
          const {data:res}=await this.$http.post('/api/cloud-member/member/getName/'+this.form.username,{
            username:this.form.username
          })
          console.log(res);
          if(res.code==='A00101'){
            return callback(new Error("该用户名不存在"))
          }else{
            callback();
          }
        }
      }
    };
    return {
      form: {
        username: "",
        password: "",
        address: "",

      },
      rules: {
        username: [
          {required: true,validator:validateUser, trigger: "blur"},
          {min: 3, max: 18, message: "长度为3-18个字符", trigger: "blur"},
        ],
        password: [
          {required: true, message: "请输入登录密码", trigger: "blur"},
          {min: 6, max: 18, message: "长度为6-18个字符", trigger: "blur"},
        ]
      },

    }
  },
  created() {
    this.getAddress();
  },
  methods:{
  getAddress(){
    jsonp(
        'https://api.map.baidu.com/location/ip?ak=KPOPrTmKSUQSuPCUpu6TpSD5KgOjcUvh&&coor=bd09ll',
        null,
        (err, data) => {
          if (err) {
            console.log(err);
          } else {
           this.form.address=data.content.address;
          }
        }
    );
  },
    login(){
      //对整个表单进行验证
      this.$refs.loginFromRef.validate(vali=>{
        if(!vali){
          return
        }
        //将promise对象进行数据解构
         this.$http.post("/api/cloud-member/member/login",this.form)
             .then(res=>{
               // 状态不等于201表示登陆失败，其余的为成功
              if(res.data.code=='000000'){
                this.$notify.success({
                   title:"登录成功",
                   message:`欢迎回来~ 亲爱的${res.data.data.member.nickname==undefined?res.data.data.member.username:res.data.data.member.nickname}`
                 });
                //把登陆信息存储到本地
                sessionStorage.setItem("user",JSON.stringify(res.data.data.member));
                sessionStorage.setItem("token",JSON.stringify(res.data.data.token));
                sessionStorage.setItem("nickname",res.data.data.member.nickname);
                sessionStorage.setItem("user_id",res.data.data.id);
                this.$store.commit("saveUserName",res.data.data.member.username);
                //登陆信息保存到vuex里

                  //进行跳转至首页
                 if(res.data.data.member.username=="admin"){
                   this.$router.push("/homepage");
                 }else{
                   this.$router.push("/home");
                 }
              }else{
                return this.$notify({
                   title:res.data.msg,
                   message:"登录失败",
                   type:"error"
                 });
           }
         }).catch(err=>{
           return Promise.reject(err);
         })
      })
    },
    register(){
      this.$router.push("/register")
    }
  }
}
</script>

<style scoped>
*{
  box-sizing: border-box;
}
.wave{
  position: fixed;
  height: 100%;
  left: 0;
  bottom: 0;
  z-index: -1;
}
.login-container{
  width: 100vw;
  height: 70vh;
  display: flex;
  align-items: center;
}
.login-container .img{
  width: 600px;
}
.login-container .login-box{
  width: 450px;
  height: 300px;
  background-color: white;
  border-radius: 3px;
  position: absolute;
  left:60%;
  top:30%;
  box-shadow: 0px 0px 10px #ddd;
}
.avater-box{
  width: 100px;
  height: 100px;
  position: absolute;
  left: 50%;
  transform: translate(-40%,-50%);
}
.avater-box img{
  width: 100%;
  height: 100%;
}
.login-form{
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
p{
  text-align: right;
  color:#999;
  font-size:10px;
  cursor: pointer;
  transition: .3s;
}
p:hover{
  color: #38d39f;
}
.btn{
  display: flex;
  justify-content: center;
  margin-right: 50px;
}
.el-button.is-round {
  border-radius: 20px;
  padding: 12px 50px;
}
.el-button+.el-button {
  margin-left: 30px;
}
</style>