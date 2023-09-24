<template>
<div id="mypass">
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>修改密码</span>
    </div>

    <el-form ref="rulForm" label-width="400px" status-icon :model="ruleForm" class="demo-ruleForm" :rules="rules">
      <el-form-item>
        <span><i class="el-icon-lock">注意:</i>请保护好你的隐私</span>
      </el-form-item>
      <el-form-item label="原始密码:"  prop="oldPass">
        <el-input type="password"  v-model="ruleForm.oldPass" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="新密码:" prop="pass">
        <el-input type="password" v-model="ruleForm.pass" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码:" prop="checkPass">
        <el-input type="password"  v-model="ruleForm.checkPass" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item class="btn">
        <el-button type="warning" @click="submitForm()" >保存修改</el-button>
        <el-button type="info" @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</div>
</template>

<script>
export default {
name: "MyPass",
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
    var validateOldPass = async (rule, value, callback) => {
      //判断用户是否为空，如果为空返回提示信息，否则就进行服务器请求，并把当前用户名携带给服务器
      if (value=="") {
        callback(new Error("原始密码不能为空"))
      }else{
        //请求数据库，把当前用户的用户密码与输入的密码做比对
        if(value){
          const {data:res}=await this.$http.post('/api/cloud-member/member/getPassword',{
            uid:sessionStorage.getItem("user_id"),
            password:this.ruleForm.oldPass
          })

          //如果密码是否错误返回提示信息给用户，否则不做任何处理
          if(res.code=='A00100'){
            return callback(new Error("原始密码错误"))
          }else{
            callback();
          }
        }
      }

    };
    var userInfo=JSON.parse(sessionStorage.getItem("user"));
    return {
      ruleForm: {
        uid:userInfo.uid,
        oldPass:"",
        pass: '',
        checkPass: '',
      },
      rules: {
        oldPass: [
          {required: true, validator: validateOldPass, trigger: "blur"},
          {min:6, max: 11, message: "长度为6-11个字符", trigger: "blur"},
        ],
        pass: [
          {required: true,validator: validatePass, trigger: 'blur'},
          {min: 6, max: 11, message: "长度为6-11个字符", trigger: "blur"},
        ],
        checkPass: [
          {required: true,validator: validatePass2, trigger: 'blur'},
          {min: 6, max: 11, message: "长度为6-11个字符", trigger: "blur"},
        ]
      }

    }
  },
  methods: {
    submitForm() {
      //对整个表单进行预验证，如果验证成功的话发起网络请求，携带注册用户名和密码给服务器进行处理，并给用户提示信息，后跳转至登陆页面进行登陆
      this.$refs.rulForm.validate(async (valid) => {
        if(!valid){
          return
        }
        if(valid){
          const {data:res}=await this.$http.post("/api/cloud-member/MemberLogin/updatePassword",{
            password:this.ruleForm.pass,
            uid:sessionStorage.getItem("user_id")})
            console.log(res)
          if(res.code=="A00100"){
            this.$message.error(res.msg)
          }else{
            this.$message.success("修改成功")
            this.ruleForm.oldPass= ''
            this.ruleForm.checkPass= ''
            this.ruleForm.pass= ''
          }
        }
      })
    },
    resetForm(){
      this.$refs.rulForm.resetFields();
    }
  }
}
</script>

<style scoped>
.box-card .el-input{
  width: 45%;
}
</style>