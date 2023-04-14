<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出
        </el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
      </el-col>
      <el-col :span="19">
        <el-form ref="queryForm" :model="queryParams" size="small" style="float: right" :inline="true">
          <el-form-item label="用户号" prop="deptName">
            <el-input
              v-model="queryParams.memberId"
              placeholder="请输入会员编号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="收货人" prop="status">
            <el-input
              v-model="queryParams.consignee"
              placeholder="请输入收货人"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号" prop="status">
            <el-input
              v-model="queryParams.mobile"
              placeholder="请输入手机号"
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
      <el-form :rules="rules" :model="bannerForm" label-width="80px">
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
        <el-form-item prop="defaultImg" label="展示图">
          <el-upload
            :data="dataObj"
            action="http://cloud-secondhand-trading.oss-cn-shanghai.aliyuncs.com"
            multiple
            accept="jpg,jpeg,png,PNG"
            list-type="picture-card"
            :on-success="handleDefaultImgUploadSuccess"
            :show-file-list="false"
            :before-upload="beforeUpload"
          >
            <img v-if="bannerForm.imageUrl" :src="bannerForm.imageUrl" class="avatar">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item prop="targetUrl" label="跳转地址">
          <el-input v-model="bannerForm.targetUrl" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="goodsFormShow = false">关 闭</el-button>
        <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, info, update } from '@/api/goods/banner'
import pagination from '@/components/Pagination'
import { exportFile } from '@/utils/request'
import { policy } from '@/api/oss/policy'
import { getUUID } from '@/utils'
import {createUserInfo, updateUserInfo} from "@/api/admin/user";

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
      bannerForm: {},
      checkedIds: [],
      bannerFormTitle: '编辑轮播图',
      bannerFormShow: false,
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      list: null,
      listLoading: true
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
      exportFile('/cloud-member/memberAddr/export', '会员收获地址')
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
    getList() {
      this.listLoading = true
      list(this.queryParams).then(response => {
        console.log(response)
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
    saveOrpdate() {
      if (this.bannerForm.id) {
        update(this.userForm.id, this.userForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
          this.userFormShow = false
          this.getList()
        })
      } else {
        // todo 添加輪播圖
      }
      update(id, this.bannerForm).then(res => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.bannerFormShow = false
        this.getList()
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除收货人<${row.consignee}>吗？`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        deletes([row.addrId]).then(res => {
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
    handleDefaultImgUploadSuccess(res, file) {
      this.bannerForm.imageUrl = this.dataObj.host + '/' + this.dataObj.key
    }
  }
}
</script>
<style scoped>
.avatar {
  width: 384px;
  height: 144px;
  display: block;
}
</style>
