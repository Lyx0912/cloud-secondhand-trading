<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出
        </el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
        <el-button icon="el-icon-delete" size="mini" type="success" @click="handlePass()">通过</el-button>
      </el-col>
      <el-col :span="19">
        <el-form ref="queryForm" :model="queryParams" size="small" style="float: right" :inline="true">
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入商品名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="卖家">
            <el-input
              v-model="queryParams.seller"
              placeholder="请输入卖家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="上架状态">
            <el-select v-model="queryParams.state" placeholder="请选择上架状态">
              <el-option label="已通过" :value="1" />
              <el-option label="待审核" :value="0" />
              <el-option label="未通过" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-table v-loading="listLoading" :row-style="{height:'45px'}" :cell-style="{padding:'0px'}" :data="list" element-loading-text="Loading" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column align="center" label="商品名称" prop="name" width="150 " :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="categoryName" width="130" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="卖家" prop="seller" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" width="150">
        <template v-slot="scope">
          <el-image :src="scope.row.defaultImg" style="width:64px;height:64px;" :preview-src-list="[scope.row.defaultImg]" />
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="价格" align="center" prop="price" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="审核状态" align="center" width="100">
        <template  v-slot="scope">
          <el-tag type="success" effect="dark" v-if="scope.row.state==1">已通过</el-tag>
          <el-tag type="warning" effect="dark" v-if="scope.row.state==0">待审核</el-tag>
          <el-tooltip class="item" effect="dark" :content="scope.row.mark" placement="top">
            <el-tag type="danger" effect="dark" v-if="scope.row.state==2">未通过</el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
<!--      <el-table-column label="浏览量" align="center" prop="viewCount" width="100" :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="发布时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />-->
      <el-table-column label="提交时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column align="center" prop="created_at" label="操作">
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" v-if="scope.row.state==0"
                     @click="handleEdit(scope.row.id)">审核</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" v-if="scope.row.state!=0"
                     @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="goodsFormTitle" :visible.sync="goodsFormShow">
      <el-form :rules="rules" :model="goodsForm" label-width="80px">
        <el-form-item prop="cid" label="所属分类">
          <el-cascader
            :props="props"
            v-model="goodsForm.categoryPath"
            placeholder="试试搜索：指南"
            :options="categoryList"
            filterable
            disabled
          style="width: 300px;"></el-cascader>
          <!--          <el-input v-model="goodsForm.cid" />-->
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="goodsForm.name" disabled="disabled" />
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input v-model="goodsForm.description" disabled="disabled" />
        </el-form-item>
        <el-form-item prop="price" label="价格">
          <el-input v-model="goodsForm.price" disabled="disabled" />
        </el-form-item>
        <el-form-item prop="defaultImg" label="展示图">
            <el-image :src="goodsForm.defaultImg" class="avatar" />
        </el-form-item>
        <el-form-item prop="images" label="图片集">
          <template v-for="scope in goodsForm.images">
            <el-image :src="scope.url" style="width:64px;height:64px;" :preview-src-list="[scope.url]" />
          </template>
        </el-form-item>
        <el-form-item prop="details.content" label="详情">
          <we-editor :toolbar-option="toolbar" style="width: 100%;height: 400px;border: #DCDFE6 1px solid;border-radius: 4px" :editable-option="editable" :mode="mode" :html.sync="goodsForm.details.content" />
        </el-form-item>
        <el-form-item prop="details.content" label="备注">
          <el-input v-model="audit.mark" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="goodsFormShow = false">关 闭</el-button>
        <el-button type="success" @click="handleSave(1)">通 过</el-button>
        <el-button type="warning" @click="handleSave(2)">打 回</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { list as getCategory } from '@/api/goods/category'
import pagination from '@/components/Pagination'
import { exportFile } from '@/utils/request'
import { list as getGoods, remove as deletes, update } from '@/api/goods/audit'
import { info } from '@/api/goods/goods'
import { useWangEditor } from 'wangeditor5-for-vue2'
import { policy } from '@/api/oss/policy'
import { getUUID } from '@/utils'

export default {
  components: { pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      audit: {},
      images: '',
      idialogVisible: false,
      dialogImageUrl: '',
      dialogVisible: false,
      formLabelWidth: '120px',
      mode: 'simple',
      ...useWangEditor({
        config: {
          MENU_CONF: {
            uploadImage: {
              readOnly: true,
              // 后端上传地址，必填
              server: '/api/upload/image',
              // form-data fieldName，后端接口参数名称，默认值wangeditor-uploaded-image
              fieldName: 'file',
              // 1M，单个文件的最大体积限制，默认为 2M
              maxFileSize: 1 * 1024 * 1024,
              // 最多可上传几个文件，默认为 100
              maxNumberOfFiles: 10,
              // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
              allowedFileTypes: ['image/*'],
              // 15 秒，超时时间，默认为 10 秒
              timeout: 15 * 1000
              // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
              // meta: {
              //     token: 'xxx',
              //     otherKey: 'yyy'
              // },
              // 将 meta 拼接到 url 参数中，默认 false
              // metaWithUrl: false,
              // 自定义增加 http  header
              // headers: {
              //     Accept: 'text/x-json',
              //     otherKey: 'xxx'
              // },
              // 跨域是否传递 cookie ，默认为 false
              // withCredentials: false,
            },
            uploadVideo: {
              // 后端上传地址，必填
              server: '/api/upload/video',
              // form-data fieldName，后端接口参数名称，默认值wangeditor-uploaded-video
              fieldName: 'file',
              // 5M，文件大小限制，默认10M
              maxFileSize: 5 * 1024 * 1024,
              // 最多可上传几个文件，默认为 5
              maxNumberOfFiles: 3,
              // 选择文件时的类型限制，默认为 ['video/*'] 。如不想限制，则设置为 []
              allowedFileTypes: ['video/*'],
              // 15 秒，超时时间，默认为 30 秒
              timeout: 15 * 1000
              // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
              // meta: {
              //     token: 'xxx',
              //     otherKey: 'yyy'
              // },
              // 将 meta 拼接到 url 参数中，默认 false
              // metaWithUrl: false,
              // 自定义增加 http  header
              // headers: {
              //     Accept: 'text/x-json',
              //     otherKey: 'xxx'
              // },
              // 跨域是否传递 cookie ，默认为 false
              // withCredentials: false,
            }
          }
          // onCreated: (inst) => {
          // }
        }
      }),
      data: {
        json: '',
        html: ''
      },
      dataObj: {},
      goodsFormTitle: '编辑商品',
      goodsFormShow: false,
      goodsForm: {
        details: {
          content: ''
        }
      },
      // 表单校验规则
      rules: {
        cid: [{ required: true, trigger: 'blur', message: '请先选择所属分类' }],
        name: [{ required: true, trigger: 'blur', message: '名称不能为空' }],
        description: [{ required: true, trigger: 'blur', message: '描述不能为空' }],
        price: [{ required: true, trigger: 'blur', message: '价格不能为空' }, { type: 'number', message: '价格必须为数字值' }],
        images: [{ required: true, trigger: 'blur', message: '请上传图片集' }]
      },
      roleSelect: [],
      checkedIds: [],
      audits: [],
      addrForm: {
        memberId: '',
        mobile: '',
        isDefault: 0,
        consignee: 0,
        provinceId: 0,
        province: '',
        cityId: 0,
        city: '',
        areaId: 0,
        area: '',
        postCode: '',
        addr: '',
        lng: 0.00,
        lat: 0.00
      },
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      list: null,
      listLoading: true,
      categoryList: [],
      props: {
        label: 'name',
        value: 'id',
        children: 'childrens',
        expandTrigger: 'hover'
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryList()
  },
  methods: {
    beforeUpload(file) {
      const _self = this
      return new Promise((resolve, reject) => {
        policy()
          .then(response => {
            _self.dataObj.policy = response.data.policy
            _self.dataObj.signature = response.data.signature
            _self.dataObj.ossaccessKeyId = response.data.accessid
            _self.dataObj.key = response.data.dir + getUUID() + file.name
            _self.dataObj.dir = response.data.dir
            _self.dataObj.host = response.data.host
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
    handleDefaultImgUploadSuccess(res, file) {
      this.goodsForm.defaultImg = this.dataObj.host + '/' + this.dataObj.key
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleEdit(id) {
      this.goodsFormShow = true
      info(id).then(res => {
        this.goodsForm = res.data
        console.log(this.goodsForm)
      })
    },
    handleSelectionChange(values) {
      this.audits = []
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
      values.map(res => {
        this.audits.push({ 'goodsId': res.id, 'state': 1, 'mark': '成功' })
      })
    },
    /** 导出excel文件 */
    handleExport() {
      exportFile('/cloud-goods/goods/export', '商品列表')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    handleRemove(file, fileList) {
      this.goodsForm.images = fileList
    },
    /** 通过按钮 */
    handleSave(state) {
      this.audit.goodsId = this.goodsForm.id
      this.audit.state = state
      this.audits.push(this.audit)
      /** 保存商品信息 */
      update(this.audits).then(res => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.audit = {}
        this.getList()
        this.goodsFormShow = false
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 10
      },
      this.handleQuery()
    },
    handlePass() {
      if (this.checkedIds.length === 0) {
        this.$message({
          type: 'warning',
          message: '请勾选需要通过的物品!'
        })
        return
      }
      update(this.audits).then(res => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.getList()
      })
    },
    handleDeletes() {
      if (this.checkedIds.length === 0) {
        this.$message({
          type: 'warning',
          message: '请勾选需要删除的记录!'
        })
        return
      }
      this.$confirm(`确定执行删除操作吗？`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        deletes(this.checkedIds).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        })
      })
    },
    getList() {
      this.listLoading = true
      getGoods(this.queryParams).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getCategoryList() {
      getCategory(this.queryParams).then(res => {
        this.categoryList = res.data
        console.log(this.categoryList)
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除卖家为<${row.seller}的${row.name}>吗？`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        deletes([row.id]).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        })
      })
    }
  }
}
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
<style scoped>
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>
