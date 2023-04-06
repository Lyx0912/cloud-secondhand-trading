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
          <el-form-item label="商品名称" >
            <el-input
              v-model="queryParams.name"
              placeholder="请输入商品名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="卖家" >
            <el-input
              v-model="queryParams.seller"
              placeholder="请输入卖家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="上架状态">
            <el-input
              v-model="queryParams.isOnSell"
              placeholder="请输入上架状态"
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
      <el-table-column align="center" label="商品名称" prop="name" width="150 " :show-overflow-tooltip="true" />
      <el-table-column align="center" label="卖家" prop="seller" width="100"  :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" width="150">
        <template v-slot="scope">
          <el-image :src="scope.row.url" style="width:64px;height:64px;"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description"  />
      <el-table-column label="价格" align="center" prop="price" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="上架状态" align="center" width="100" >
        <template v-slot="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" align="center" prop="viewCount" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="发布时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column label="更新时间" align="center" prop="updateTime"  width="164" :show-overflow-tooltip="true" />
      <el-table-column align="center" prop="created_at" label="操作" >
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleDelete(scope.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { list } from '@/api/goods/goods'
import pagination from '@/components/Pagination'
import { exportFile } from '@/utils/request'

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
      roleSelect: [],
      checkedIds: [],
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
      },
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
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
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
    }
  }
}
</script>
