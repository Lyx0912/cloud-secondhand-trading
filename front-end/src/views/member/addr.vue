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
      <el-table-column align="center" label="会员编号" prop="memberId" width="100" />
      <el-table-column align="center" label="收货人" prop="consignee" width="100" />
      <el-table-column label="手机号" align="center" prop="mobile" width="120" />
      <el-table-column label="省编号" align="center" prop="provinceId" width="60" :show-overflow-tooltip="true" />
      <el-table-column label="省份" align="center" prop="province" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="市编号" align="center" prop="cityId" width="60" :show-overflow-tooltip="true" />
      <el-table-column label="城市" align="center" prop="city" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="区" align="center" prop="area" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="详细地址" align="center" prop="addr" :show-overflow-tooltip="true" />
      <el-table-column label="邮编" align="center" prop="postCode" width="120" />
      <el-table-column label="默认" align="center" width="60">
        <template v-slot="scope">
          <el-tag>{{ scope.row.isDefault == 1?'是':'否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" width="150">
        <template v-slot="scope">
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { queryMemberAddr, deletes } from '@/api/member/addr'
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
        memberId: '',
        pageNo: 1,
        pageSize: 10,
        mobile: '',
        consignee: ''
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
        memberId: '',
        pageNo: 1,
        pageSize: 10,
        mobile: '',
        consignee: ''
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
      queryMemberAddr(this.queryParams).then(response => {
        this.list = response.data.list
        this.total = <response className="data total"></response>
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
