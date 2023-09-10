<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出</el-button>
        <el-button icon="el-icon-plus" size="mini" type="primary" @click="handleAdd()">新增</el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
      </el-col>
      <el-col :span="19">
        <el-form ref="queryForm" :model="queryParams" size="small" style="float: right" :inline="true">
          <el-form-item label="标题" prop="deptName">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入标题"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="描述" prop="status">
            <el-input
              v-model="queryParams.description"
              placeholder="请输入轮播图描述"
              clearable
              @keyup.enter.native="handleQuery"
            />
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
      <el-table-column align="center" label="编号" prop="id" width="100" />
      <el-table-column align="center" label="标题" prop="title" width="200" />
      <el-table-column align="center" label="描述" prop="description" />
      <el-table-column label="激活状态" align="center" width="120">
        <template v-slot="scope">
          <el-switch
            v-model="scope.row.isActive"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="图片" align="center" width="220">
        <template v-slot="scope">
          <el-image :src="scope.row.imageUrl" style="width:192px;height:72px;margin: 3px" :preview-src-list="[scope.row.imageUrl]" />
        </template>
      </el-table-column>
      <el-table-column label="目标地址" align="center" prop="targetUrl" width="250" :show-overflow-tooltip="true" />
      <el-table-column align="center" prop="created_at" label="操作" width="250">
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="bannerFormTitle" :visible.sync="bannerFormShow">
        <el-form :rules="rules"  :model="bannerForm" label-width="80px">
          <el-form-item prop="title" label="标题">
            <el-input v-model="bannerForm.title" />
          </el-form-item>
          <el-form-item prop="description" label="描述">
            <el-input v-model="bannerForm.description" />
          </el-form-item>
          <el-form-item prop="isOnSell" label="上架状态">
            <el-switch
              v-model="bannerForm.isActive"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
          </el-form-item>
          <el-form-item  label="图片">
            <el-upload
              :data="dataObj"
              action="http://cloud-secondhand-trading.oss-cn-shanghai.aliyuncs.com"
              accept="jpg,jpeg,png,PNG"
              list-type="picture-card"
              :on-success="handleImgUploadSuccess"
              :show-file-list="false"
              :before-upload="beforeUpload">
              <el-image v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" class="avatar" />
              <i v-else class="el-icon-plus" />
            </el-upload>
          </el-form-item>
          <el-form-item prop="targetUrl" label="跳转地址">
            <el-input v-model="bannerForm.targetUrl" />
          </el-form-item>
          <el-form-item prop="sort" label="排序">
            <el-input v-model="bannerForm.sort" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="bannerFormShow = false">关 闭</el-button>
          <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, info, update, deletes, save, changeStatus } from '@/api/goods/banner'
import pagination from '@/components/Pagination'
import { exportFile } from '@/utils/request'
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
      dataObj: {},
      bannerForm: {
        imageUrl: '',
        isActive: 0
      },
      checkedIds: [],
      bannerFormTitle: '编辑轮播图',
      bannerFormShow: false,
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      list: null,
      listLoading: true,
      // 表单校验规则
      rules: {
        title: [{ required: true, trigger: 'blur', message: '请先输入标题' }],
        description: [{ required: true, trigger: 'blur', message: '请输入描述' }],
        imageUrl: [{ required: true, trigger: 'blur', message: '请先上传图片' }],
        sort: [{ required: true, trigger: 'blur', message: '请输入排序值' }, { type: 'number', message: '排序必须为数字值' }],
        targetUrl: [{ required: true, trigger: 'blur', message: '请输入跳转地址' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleSelectionChange(values) {
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
    },
    /** 导出excel文件 */
    handleExport() {
      exportFile('/cloud-goods/banner/export', '轮播图')
    },
    handleAdd() {
      this.bannerForm = {
        isActive: 0
      }
      this.bannerFormShow = true
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 10
      }
      this.handleQuery()
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
    handleStatusChange(row) {
      changeStatus(row.id, row.isActive).then(res => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.getList()
      })
    },
    getList() {
      this.listLoading = true
      list(this.queryParams).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleEdit(id) {
      info(id).then(res => {
        this.bannerForm = res.data
        this.bannerFormShow = true
      })
    },
    saveOrUpdate() {
      if (this.bannerForm.id) {
        update(this.bannerForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
        })
      } else {
        //  添加轮播图
        save(this.bannerForm).then(res => {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
        })
      }
      this.bannerFormShow = false
      this.getList()
    },
    handleDelete(row) {
      this.$confirm(`确定删除该轮播图吗？`, '是否继续?', '提示', {
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
    },
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
            console.log('出错了..', err)
            reject(false)
          })
      })
    },
    handleImgUploadSuccess(res, file) {
      this.bannerForm.imageUrl = this.dataObj.host + '/' + this.dataObj.key
      this.$forceUpdate()
    }
  }
}
</script>
<style scoped>
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>
