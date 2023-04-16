<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出</el-button>
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
    <el-table v-loading="listLoading" :row-style="{height:'45px'}" :cell-style="{padding:'0px'}" :data="list" element-loading-text="Loading"  fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column align="center" label="编号" prop="id" width="100" />
      <el-table-column align="center" label="物品编号" prop="goodsId" width="100" />
      <el-table-column align="center" label="物品名称" prop="goodsName" />
      <el-table-column align="center" label="价格" prop="goodsPrice" width="100"/>
      <el-table-column label="图片" align="center" width="100">
        <template v-slot="scope">
          <el-image :src="scope.row.goodsImage" style="width:64px;height:64px;" :preview-src-list="[scope.row.goodsImage]" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="描述" prop="goodsPrice" />
      <el-table-column align="center" prop="created_at" label="操作" width="250">
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="handleCorret(scope.row.id)">同步</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { list, corret, deletes } from '@/api/goods/recommend'
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
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      list: null,
      listLoading: true,
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
      exportFile('/cloud-goods/recommend/export', '推荐商品列表')
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
    handleCorret(id) {
      corret(id).then(res => {
        this.$message({
          type: 'success',
          message: '数据同步成功!'
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
      this.$confirm(`确定删除吗？`, '是否继续?', '提示', {
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
            console.log('出错了...', err)
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
