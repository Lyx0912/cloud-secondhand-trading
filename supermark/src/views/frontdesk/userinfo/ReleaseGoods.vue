<template>
  <div>
    <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(1)">发布</el-button>
    <template>

      <el-table v-loading="listLoading" :row-style="{ height: '45px' }" :cell-style="{ padding: '0px' }" :data="List"
        element-loading-text="Loading" border fit highlight-current-row>
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="商品名称" prop="name" width="150 " :show-overflow-tooltip="true" />
        <el-table-column label="分类" align="center" prop="categoryName" width="130" :show-overflow-tooltip="true" />
        <el-table-column align="center" label="数量" prop="total" width="100" :show-overflow-tooltip="true" />
        <el-table-column label="图片" align="center" width="150">
          <template v-slot="scope">
            <el-image :src="scope.row.defaultImg" style="width:64px;height:64px;"
              :preview-src-list="[scope.row.defaultImg]" />
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="description" />
        <el-table-column label="价格" align="center" prop="price" width="100" :show-overflow-tooltip="true" />
        <el-table-column label="审核状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag type="success" effect="dark" v-if="scope.row.audit.state == 1">已通过</el-tag>
            <el-tag type="warning" effect="dark" v-if="scope.row.audit.state == 0">待审核</el-tag>
            <el-tooltip class="item" effect="dark" :content="scope.row.audit.mark" placement="top">
              <el-tag type="danger" effect="dark" v-if="scope.row.audit.state == 2">未通过</el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
        <el-table-column align="center" prop="created_at" label="操作">
          <template v-slot="scope">
            <el-button v-if="scope.row.audit.state != 2" type="primary" icon="el-icon-edit" size="mini" @click="ReleaseMember(scope.row.audit.goodsId)">详情</el-button>
            <el-button @click="edit(scope.row.audit.goodsId)" v-if="scope.row.audit.state == 2" type="primary"
              icon="el-icon-edit" size="mini">重新编辑</el-button>
            <el-button @click="deleteid(scope.row.audit.goodsId, scope.row.isOnSell)" v-if="scope.row.audit.state == 1&&scope.row.isOnSell==1"
              type="danger" icon="el-icon-delete" size="mini">下架</el-button>
              <el-button @click="push(scope.row.audit.goodsId)" v-if="scope.row.audit.state == 1&&scope.row.isOnSell==0"
              type="danger" icon="el-icon-delete" size="mini">上架</el-button>
              <!-- <el-button @click="deletequxiao(scope.row.audit.goodsId)" v-if="scope.row.audit.state == 0&&scope.row.isOnSell==0"
              type="danger" icon="el-icon-delete" size="mini">取消</el-button> -->
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum" :page-sizes="[5, 10, 15]" :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </template>
    <el-dialog :title="goodsFormTitle" :visible.sync="goodsFormShow">
      <el-form :rules="rules" :model="goodsForm" label-width="80px">
        <el-form-item prop="cid" label="所属分类">
          <el-cascader :props="props" v-model="goodsForm.categoryPath" placeholder="试试搜索：指南" :options="categoryList"
            filterable></el-cascader>
          <!--          <el-input v-model="goodsForm.cid" />-->
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="goodsForm.name" />
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="goodsForm.description" />
        </el-form-item>
        <el-form-item prop="price" label="价格">
          <el-input v-model="goodsForm.price" />
        </el-form-item>
        <el-form-item prop="details.content" label="数量">
          <el-input v-model="goodsForm.total" />
        </el-form-item>
        <!-- <el-form-item prop="isOnSell" label="上架状态">
          <el-switch
            v-model="goodsForm.isOnSell"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item> -->
        <el-form-item prop="defaultImg" label="展示图">
          <el-upload :data="dataObj" action="http://cloud-secondhand-trading.oss-cn-shanghai.aliyuncs.com" multiple
            accept="jpg,jpeg,png,PNG" list-type="picture-card" :on-success="handleDefaultImgUploadSuccess"
            :show-file-list="false" :before-upload="beforeUpload">
            <img v-if="goodsForm.defaultImg" :src="goodsForm.defaultImg" v-bind="goodsForm.defaultImg" class="avatar"
              style="width: 100%;border: #DCDFE6 1px solid;border-radius: 4px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item prop="images" label="图片集">
          <el-upload :data="dataObj" action="http://cloud-secondhand-trading.oss-cn-shanghai.aliyuncs.com" multiple
            accept="jpg,jpeg,png,PNG" list-type="picture-card" :on-success="handleUploadSuccess"
            :file-list="goodsForm.images" :before-upload="beforeUpload" :on-remove="handleRemove">
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <!-- <el-form-item prop="details.content" label="详情">
          <we-editor :toolbar-option="toolbar" style="width: 100%;height: 400px;border: #DCDFE6 1px solid;border-radius: 4px" :editable-option="editable" :mode="mode" :html.sync="goodsForm.details.content" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">关 闭</el-button>
        <el-button type="primary" @click="handleSave()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { getUUID } from '@/utils/index.js'
import { useWangEditor } from 'wangeditor5-for-vue2'
export default {
  data() {
    return {
      state: 0,
      listLoading: true,
      props: {
        label: 'name',
        value: 'id',
        children: 'childrens',
        expandTrigger: 'hover'
      },
      mode: 'simple',
      categoryList: [],
      dataObj: {},
      goodsFormTitle: '发布商品',
      goodsFormShow: false,
      goodsForm: {
        details: {
          content: ''
        },
        images: []
      },
      // 定义数据源
      List: [],
      //查询分页信息
      queryInfo: {
        uid: JSON.parse(sessionStorage.getItem("user")).uid,
        pagenum: 1,
        pagesize: 5,
      },
      // 表单校验规则
      rules: {
        cid: [{ required: true, trigger: 'blur', message: '请先选择所属分类' }],
        name: [{ required: true, trigger: 'blur', message: '名称不能为空' }],
        description: [{ required: true, trigger: 'blur', message: '描述不能为空' }],
        price: [{ required: true, trigger: 'blur', message: '价格不能为空' }, { type: 'number', message: '价格必须为数字值' }],
        images: [{ required: true, trigger: 'blur', message: '请上传图片集' }]
      },
      //订单总页数
      total: 0
    }
  },
  created() {
    this.master = JSON.parse(sessionStorage.getItem("user")).username;
    this.getMyGoods();
    this.getCategoryList()
  },
  methods: {
    ReleaseMember(goodsId){
      this.$router.push({path:"/ReleaseMember",query:{goodsId:goodsId}});
    },
    deleteid(goodsId, isOnSell) {
      isOnSell = isOnSell==1?0:1
      this.state = 1
      this.$http.patch("/api/cloud-goods/goods/" + isOnSell+"/"+goodsId ).then(res => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.getMyGoods();
      })
    },
    push(goodsId) {
      this.$http.put("/api/cloud-goods/goods/memberUpdate", {id:goodsId}).then(res => {
          console.log(res.data)
          if (res.data.code == '000000') {
            this.$message({
              type: 'success',
              message: '操作成功!'
            })
            this.goodsFormShow = false
          } else {
            this.$message({
              type: 'error',
              message: res.data.msg
            })
          }
          this.getMyGoods();
          // this.getList()
        })
    },
    edit(goodsId) {
      this.state = 1
      this.$http.get("/api/cloud-goods/goods/" + goodsId).then(res => {
        this.goodsForm = res.data.data;
        console.log(this.goodsForm)
      })
      this.goodsFormShow = true
    },
    handleSave() {
      this.goodsForm.sellerId = sessionStorage.getItem("user_id");
      this.goodsForm.seller = sessionStorage.getItem("username");
      // 保存商品信息
      if (this.state == 0) {
        this.$http.put("/api/cloud-goods/goods/save", this.goodsForm).then(res => {
          console.log(res.data)
          if (res.data.code == '000000') {
            this.$message({
              type: 'success',
              message: '操作成功!'
            })
            this.goodsFormShow = false
          } else {
            this.$message({
              type: 'error',
              message: res.data.msg
            })
          }
          this.getMyGoods();
          // this.getList()
        })
      }else{
        this.$http.put("/api/cloud-goods/goods/update", this.goodsForm).then(res => {
          console.log(res.data)
          if (res.data.code == '000000') {
            this.$message({
              type: 'success',
              message: '操作成功!'
            })
            this.goodsFormShow = false
          } else {
            this.$message({
              type: 'error',
              message: res.data.msg
            })
          }
          this.getMyGoods();
          // this.getList()
        })
      }

    },
    closeDialog() {
      this.getMyGoods()
      this.goodsFormShow = false
    },
    getCategoryList() {
      this.$http.get("/api/cloud-goods/category/list?pageNo=1&pageSize=10").then(res => {
        this.categoryList = res.data.data;
        console.log(this.categoryList)
      })
    },
    beforeUpload(file) {
      const _self = this
      return new Promise((resolve, reject) => {
        this.$http.get("/api/cloud-oss/oss/policy")
          .then(response => {
            console.log(response)
            _self.dataObj.policy = response.data.data.policy
            _self.dataObj.signature = response.data.data.signature
            _self.dataObj.ossaccessKeyId = response.data.data.accessid
            _self.dataObj.key = response.data.data.dir + getUUID() + file.name
            _self.dataObj.dir = response.data.data.dir
            _self.dataObj.host = response.data.data.host
            resolve(true)
          })
          .catch(err => {
            console.log('出错了...', err)
            reject(false)
          })
      })
    },
    handleUploadSuccess(res, file) {
      var imageObj = {
        // url: this.dataObj.host + "/" + this.dataObj.dir + "/" + file.name； 替换${filename}为真正的文件名
        url: this.dataObj.host + '/' + this.dataObj.key,
        goodsId: this.goodsForm.id
      }
      // 上传成功后加入images集合，暂时不保存到db，方便后续更新
      this.goodsForm.images.push(imageObj)
    },
    handleRemove(file, fileList) {
      this.goodsForm.images = fileList
    },
    // 
    handleDefaultImgUploadSuccess(res, file) {
      this.goodsForm.defaultImg = this.dataObj.host + '/' + this.dataObj.key;
    },
    handleEdit(id) {
      this.state = 0
      this.goodsForm = {}
      // this.goodsForm = res.data
      this.goodsFormShow = true
    },
    addRelease() {

    },
    //ajax获取发布信息
    getMyGoods() {
      this.$http.post("/api/cloud-goods/goods/releaseGoodslist", {
        pageNo: this.queryInfo.pagenum,
        pageSize: this.queryInfo.pagesize,
        seller_id: sessionStorage.getItem("user_id")
      }).then(res => {
        this.List = res.data.data.list;
        this.total = res.data.data.total;
        this.listLoading = false
        console.log(window.location.pathname);
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize//更新每页的显示数据的条数
      this.getMyorders()//重新发起网络请求
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage//更新当前页码的数据
      this.getMyorders()
    },
    async deleteById(orderId) {

      const confirmResult = await this.$confirm("您确认要删除该订单吗？", `${this.master}`, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).catch((err) => err)

      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消删除')
      }
      const { data: res } = await this.$http.delete('/api/deleteOrderByOrderId/' + orderId)

      if (res.code === 505) return this.$message.error(res.message)
      this.$message.success('删除订单成功！')
      this.getMyorders()
    }
  }
}
</script>
<style>
.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>