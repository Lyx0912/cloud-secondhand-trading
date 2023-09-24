<template>
<div class="box">

<div class="register-container">
  <div class="register-box">
  <h2>用户注册</h2>
  <el-form ref="rulForm" label-width="100px" status-icon :model="ruleForm" class="demo-ruleForm" :rules="rules">
    <el-form-item label="用户名:"  prop="username">
      <el-input  v-model="ruleForm.username" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="密码:" prop="pass">
      <el-input type="password" v-model="ruleForm.pass" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="确认密码:" prop="checkPass">
      <el-input type="password"  v-model="ruleForm.checkPass" auto-complete="off"></el-input>
    </el-form-item>
    <p class="register" @click="login">已有账号？去登录</p>
    <el-form-item class="btn">
      <el-button type="success" @click="submitForm()" >注册</el-button>
      <el-button type="info" @click="resetForm()">重置</el-button>
    </el-form-item>
  </el-form>
  </div>
</div>
</div>
</template>

<script>
export default {
  name: "Regiter",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.rulForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    var validateUser = async (rule, value, callback) => {
      //判断用户是否为空，如果为空返回提示信息，否则就进行服务器请求，并把当前用户名携带给服务器
      if (value=="") {
        callback(new Error("用户名不能为空"))
      }else{
        if(value){
          const {data:res}=await this.$http.post('/api/cloud-member/MemberLogin/getUserByUserName',{
            name:this.ruleForm.username
          })

          //如果用户名已被注册返回提示信息给用户，否则不做任何处理
          if(res.code=='A00104'){
            return callback(new Error("该用户名已被注册"))
          }else{
            callback();
          }
        }
      }

    };
    return {
      ruleForm: {
        username: '',
        pass: '',
        checkPass: '',
      },
      rules: {
        username: [
          {required: true, validator: validateUser, trigger: "blur"},
          {min: 3, max: 18, message: "长度为3-5个字符", trigger: "blur"},
        ],
        pass: [
          {required: true,validator: validatePass, trigger: 'blur'},
          {min: 6, max: 18, message: "长度为6-18个字符", trigger: "blur"},
        ],
        checkPass: [
          {required: true,validator: validatePass2, trigger: 'blur'},
          {min: 6, max: 18, message: "长度为6-18个字符", trigger: "blur"},
        ]
      }

    }
  },
  methods: {
    submitForm() {
      //对整个表单进行预验证，如果验证成功的话发起网络请求，携带注册用户名和密码给服务器进行处理，并给用户提示信息，后跳转至登陆页面进行登陆
      this.$refs.rulForm.validate((valid) => {
        if(!valid){
          return
        }
        if(valid){
          this.$http.post("/api/cloud-member/MemberLogin/register",{name:this.ruleForm.username,
          password:this.ruleForm.pass}).then(res=>{
            if(res.data.code===503){
              this.$message.error("抱歉,账号注册失败");
            }else{
              this.$message.success("恭喜,账号注册成功");
              this.$router.push("/login");
            }
          })

        }
    })
    },
    login(){
       this.$router.push("/login");
    },
    resetForm(){
      this.$refs.rulForm.resetFields();
    }
  }

}
</script>

<style scoped>
html,body{
  background: #f9f9f9;
  width: 100%;
  height: 100%;
}
.box{
  height: 100%;
  position: relative;

}
.register-box{
  margin:100px auto;
}
.register-container{
  width:1054px;
  height: 600px;
  margin:10px auto;
  display: flex;
  align-items: center;
  flex-direction: column;
  background-color: white;
  box-shadow: 0px 0px 10px #ddd;
}
.register-container h2{
padding-left: 61px;
  margin:30px 0;
  text-align: center;
  text-transform: uppercase;
}

.demo-ruleForm{
  width: 500px;
  padding: 0 20px;
  box-sizing: border-box;
}
 .register-container .btn{
  margin-left:77px;
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

</style>