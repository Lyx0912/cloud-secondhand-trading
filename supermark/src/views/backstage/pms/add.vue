<template>
<div>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>商品</el-breadcrumb-item>
    <el-breadcrumb-item>添加抢购商品</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card class="box-card">
<!--    提示区域-->
    <el-alert title="添加抢购商品信息" type="info" center show-icon :closable="false"></el-alert>
<!--    提示区域end-->
<!--    步骤条区域-->
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="填写商品信息"></el-step>
      <el-step title="填写商品促销活动"></el-step>
      <el-step title="上架商品"></el-step>
    </el-steps>
<!--    步骤条区域end-->
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="300px" class="demo-ruleForm" size="small" >

     <div class="basicInfo" v-if="active===0">
       <el-form-item label="商品名称 : " prop="goods_name">
         <el-input v-model="ruleForm.goods_name" ></el-input>
       </el-form-item>

       <el-form-item label="商品价格 : " prop="goods_price">
         <el-input v-model="ruleForm.goods_price" type="number" ></el-input>
       </el-form-item>

       <el-form-item label="商品库存 : " prop="goods_number">
         <el-input v-model="ruleForm.goods_number" type="number" ></el-input>
       </el-form-item>

       <el-form-item label="商品分类 : " prop="goods_cat">
         <el-cascader
             v-model="ruleForm.goods_cat"
             :options="cateList"
             :props="cateProps"
             @change="handleChange">
         </el-cascader>
       </el-form-item>

       <el-form-item style="text-align: center;">
         <el-button  type="primary" size="medium" @click="next" style="margin-left:-300px">下一步填写商品抢购时间</el-button>
       </el-form-item>
     </div>

      <div class="activity" v-if="active===1">
        <el-form-item label="商品抢购时间:" style="text-align: center">

          <el-date-picker
              style="margin-left:-300px"
              v-model="value"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>

        </el-form-item>

        <el-form-item style="margin:50px">
          <el-button  type="primary" size="medium" @click="prev" >上一步修改商品基本信息</el-button>
          <el-button  type="primary" size="medium" @click="next"  style="margin-left:200px">下一步填写上架商品</el-button>
        </el-form-item>
      </div>

      <div class="salegoods" v-if="active===2">
        <el-form-item label="商品详情" prop="goods_detail">
          <el-input v-model="ruleForm.goods_detail" ></el-input>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <input type="file" name="file">-->
<!--        </el-form-item>-->
        <el-form-item label="商品图片">
          <el-upload
              action=""
              :http-request="uploadSectionFile"
              accept="image/gif,image/jpeg,image/jpg,image/png,image/svg"
              :auto-upload="false"
              :on-change="handleChange2"
              :file-list="fileList"
              list-type="picture-card"
              :data="ruleForm"
              multiple
          >
            <i class="el-icon-plus"></i>
          </el-upload>

        </el-form-item>

        <el-form-item style="margin:50px">
          <el-button  type="primary" size="medium" @click="prev" >上一步修改商品抢购时间</el-button>
          <el-button  type="primary" size="medium" @click="handleForm" >添加商品</el-button>
        </el-form-item>
      </div>

    </el-form>
  </el-card>
</div>
</template>

<script>
var formData="";
export default {

  name: "add",
  data(){
    return{
      active:0,
      value:[],
      fileList:[],
      uploadURL:"/api/addCommodity",//上传图片的url地址
      //图片上传组件的headers请求头对象
      // headerObj:{
      //   Authorization:window.sessionStorage.getItem("token")
      // },
      // 图片的数组
      // previewPath:"",
      // previewVisible:false,
      ruleForm:{
        goods_name:"",
        goods_price:"",
        goods_number:"",
        goods_detail:"",
        goods_cat:[],
      },
      cateList:[
        {
          "id": 0,
          "name": "数码类",
          "children": [
            {
              "id": 1,
              "name": "手机"
            },
            {
              "id": 2,
              "name": "电脑",
            },
            {
              "id": 8,
              "name": "电视",
            }
          ]
        },
        {
          "id": 1,
          "name": "生活用品",
          "children": [
            {
              "id": 3,
              "name": "洗衣粉",
            },
            {
              "id": 4,
              "name": "洗洁精",
            },
            {
              "id": 7,
              "name": "洗碗机",
            }


          ]
        },
        {
          "id": 2,
          "name": "服装类",
          "children": [
            {
              "id": 5,
              "name": "衣服",
            },
            {
              "id": 6,
              "name": "鞋子",
            },
          ]
        },
        {
          "id": 3,
          "name": "生鲜类",
          "children": [
            {
              "id": 9,
              "name": "香蕉"
            }
          ]
        }
      ],
      cateProps:{
        label:"name",//一级名
        value:"id",//选中谁的值
        children:'children'//指定哪个属性实现父子节点的嵌套
      },
      rules:{
        goods_name:[
          {required:true,message:"请输入商品名称",trigger:'blur'}
        ],
        goods_price:[
          {required:true,message:"请输入商品价格",trigger:'blur'}
        ],
        goods_number:[
          {required:true,message:"请输入商品数量",trigger:'blur'}
        ],
        goods_cat: [
          {required:true,message:"请选择商品分类",trigger:'blur'}
        ],
        goods_detail:[
          {required:true,message:"请输入商品详情",trigger:'blur'}
        ]
      }
    }
  },
 mounted() {
    // this.getProductList();
 },
  methods:{
    next(){
      if(this.ruleForm.goods_name==""||this.ruleForm.goods_price==0||this.ruleForm.goods_number==0||this.ruleForm.goods_cat.length<=0){
        this.$message.error("验证失败")
      }else {
        if(this.active++>2){
          this.active=0;
        }
      }
    },
    prev(){
      if(this.active>0)
      this.active--;
      },
    handleChange(){

      if(this.ruleForm.goods_cat.length!==2){
        this.ruleForm.goods_cat=[];
      }
    },
    handleChange2(file,fileList){
      this.fileList=fileList;
    },
    // handleRemove(file) {
    //   //1.获取将要删除的图片的临时路径
    //   const filePath=file.response.data.tmp_path;
    //   //2.从pics数组中，找到这个图片对应的索引值
    //   const i=this.ruleForm.pics.findIndex(x=>x.pic===filePath)
    //   //3.调用数组的splice方法，把图片信息对象，从pics数值中移除
    //   this.ruleForm.pics.splice(i,1)
    //   // console.log(file, fileList);
    // },
    // handlePreview(file) {
    //   console.log(file);
    //   this.previewPath=file.response.data.url
    //   this.previewVisible=true;
    // },
    // //监听图片上传成功的事件
    // handleSuccess(response){
    //   console.log(response);
    //   //1.将拼接得到一个图片信息对象
    //   const piceInfo={pic:response.data.tmp_path}
    //   //2.将图片信息对象push到pics数组中
    //   this.ruleForm.pics.push(piceInfo)
    // },
    uploadSectionFile: function (param) {
      // 接收上传文件的后台地址
      // var FileController =this.uploadURL;
    },
    handleForm(){
      this.$refs.ruleForm.validate(valid=>{
        if(!valid){
          return this.$message.error("请填写必要的表单项!")
        }
        // const form=_.cloneDeep(this.ruleForm)
        // FormData 对象
        formData= new FormData();
        formData.append("commodityName",this.ruleForm.goods_name);
        formData.append("stock",this.ruleForm.goods_number);
        formData.append("commodityPrice ",this.ruleForm.goods_price);
        formData.append("commodityDescribe",this.ruleForm.goods_detail,);
        formData.append("startTime",this.value[0]);
        formData.append("endTime",this.value[1]);
        formData.append("commodityImage","http://192.168.43.174:8080/22.jpg");
        formData.append("classificationId",this.ruleForm.goods_cat[1])
        this.fileList.forEach(function (file) {
          //fileList里的file对象就是on-change钩子中的对象。后台是接收不到的
          //能接收的是file.raw对象
          formData.append("file",file.raw);
        })

        // this.goods_cat=this.goods_cat.join(",");

        this.uploadSectionFile();
        this.$http.post("/api/addCommodity",
          // commodityName:this.ruleForm.goods_name,
          // stock:this.ruleForm.goods_number,
          // commodityPrice:this.ruleForm.goods_price,
          // commodityImage:"http://192.168.43.174:8080/22.jpg",
          // commodityDescribe:this.ruleForm.goods_detail,
          // startTime:this.value[0],
          // endTime:this.value[1],
          // classificationId:,
          formData).then(res=>{
          if(res.data.code!==210){
            return this.$message.error("添加商品失败")
          }
          this.$message.success(res.data.message)
        })
      })
    }
  }

}
</script>

<style scoped>
.el-form .el-form-item .el-input{
  width: 75%;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>