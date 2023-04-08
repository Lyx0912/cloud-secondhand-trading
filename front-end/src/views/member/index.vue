<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()"
                   v-permission="'admin:user:add'"
        >导出
        </el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
      </el-col>
      <el-col :span="19">
        <el-form :model="queryParams" ref="queryForm" size="small" style="float: right" :inline="true">
          <el-form-item label="用户名" prop="deptName">
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="昵称" prop="status">
            <el-input
              v-model="queryParams.nickname"
              placeholder="请输入用户昵称"
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
          <el-form-item label="邮箱" prop="status">
            <el-input
              v-model="queryParams.email"
              placeholder="请输入邮箱"
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
    <el-table
      :row-style="{height:'45px'}"
      :cell-style="{padding:'0px'}"
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      @selection-change="handleSelectionChange"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="55"
      >
      </el-table-column>
      <el-table-column align="center" label="用户名" prop="username"/>
      <el-table-column label="昵称" align="center" prop="nickname" v-permission="'admin:user:add'"/>
      <el-table-column label="手机号" align="center" prop="mobile" width="150"/>
      <el-table-column label="邮箱" align="center" prop="email"/>
      <el-table-column class-name="status-col" label="性别" width="110" align="center">
        <template v-slot="scope">
          <el-tag :type="scope.row.gender===1?'primary':'success'">{{ scope.row.gender === 1 ? '男' : '女' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="账号状态" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="createTime" width="180"/>
      <el-table-column align="center" prop="created_at" label="操作">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row.id)" icon="el-icon-search">详情</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)" icon="el-icon-delete">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <el-dialog :title="userFormTitle" :visible.sync="userFormShow">
      <div style="text-align: center;margin-bottom: 20px">
        <el-image
          :preview-src-list="['https://ichef.bbci.co.uk/news/1024/branded_zhongwen/17DE8/production/_102686779_momo.jpg']"
          src="https://ichef.bbci.co.uk/news/1024/branded_zhongwen/17DE8/production/_102686779_momo.jpg"
          style="width: 120px;height: 120px;border-radius: 100px"
        ></el-image>
      </div>
      <el-descriptions class="margin-top" :column="3" :size="15" border>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          {{ userForm.username }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            昵称
          </template>
          {{ userForm.nickname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-male"></i>
            性别
          </template>
          <el-tag size="small">{{ userForm.gender == 1 ? '男' : '女' }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          {{ userForm.mobile }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          {{ userForm.email }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-date"></i>
            生日
          </template>
          {{ userForm.birth }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-bank-card"></i>
            注册时间
          </template>
          {{ userForm.createTime }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-edit"></i>
            修改时间
          </template>
          {{ userForm.updateTime }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-promotion"></i>
            最近登陆时间
          </template>
          {{ userForm.updateTime }}
        </el-descriptions-item>
        <el-descriptions-item :span="2">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            默认地址
          </template>
          {{
            userForm.memberAddr ? userForm.memberAddr.province + '-' + userForm.memberAddr.city + '-' + userForm.memberAddr.area + '-' + userForm.memberAddr.addr : '暂无'
          }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            其他地址
          </template>
          <el-link type="primary" @click="handleQueryAddr(userForm.id)" >查看</el-link>
        </el-descriptions-item>
      </el-descriptions>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="userFormShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="收货地址列表"
      :visible.sync="addrVisible"
    >
      <el-table
        :data="addrList"
        border>
        <el-table-column  label="收件人" width="100">
          <template v-slot="scope">
            {{scope.row.consignee}}<el-tag v-if="scope.row.isDefault" size="mini" effect="plain">默认</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="mobile"
          label="手机号"
          width="100">
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="province"
          label="省份"
          width="100">
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="city"
          label="城市"
          width="100">
        </el-table-column>
        <el-table-column
          prop="area"
          label="地区"
          width="100">
        </el-table-column>
        <el-table-column
          :show-overflow-tooltip="true"
          prop="addr"
          label="地址">
        </el-table-column>
        <el-table-column
          prop="postCode"
          label="邮编"
          width="80">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addrVisible = false">取 消</el-button>
  </span>
      <pagination v-show="addrTotal>0" :total="addrTotal" :page.sync="addrQueryParams.pageNo" :limit.sync="addrQueryParams.pageSize" @pagination="handleQueryAddr"/>
    </el-dialog>

  </div>
</template>

<script>
import { list, remove, memberDetail } from '@/api/member/member'
import { queryMemberAddr } from '@/api/member/addr'

import { roleSelect } from '@/api/admin/role'
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
      addrTotal: 0,
      addrList: [],
      addrVisible: false,
      roleSelect: [],
      checkedIds: [],
      userFormTitle: '',
      userFormShow: false,
      userForm: {
        id: '',
        username: '',
        nickname: '',
        gender: 0,
        mobile: '',
        status: 1,
        email: '',
        memberAddr: {}
      },
      total: 0,
      queryParams: {
        username: '',
        nickname: '',
        mobile: '',
        email: '',
        pageNo: 1,
        pageSize: 15
      },
      addrQueryParams: {
        pageNo: 1,
        pageSize: 10,
        memberId: ''
      },
      list: null,
      listLoading: true
    }
  },
  created() {
    this.getList()
    this.loadRoleIds()
  },
  methods: {
    handleQueryAddr(memberId) {
      this.addrQueryParams.memberId = memberId
      // 查询收获地址
      queryMemberAddr(this.addrQueryParams).then(res => {
        this.addrList = res.data.list
        this.addrTotal = res.data.total
        this.addrVisible = true
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        username: '',
        nickname: '',
        mobile: '',
        email: '',
        pageNo: 1,
        pageSize: 15
      }
      this.handleQuery()
    },
    handleSelectionChange(values) {
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
    },
    handleExport() {
      exportFile('/cloud-member/member/export', '会员列表')
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
        remove(this.checkedIds).then(res => {
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
    loadRoleIds() {
      roleSelect().then(res => {
        this.roleSelect = res.data
      })
    },
    handleStatusChange(row) {
      const statusText = row.status === 1 ? '启用' : '禁用'
      this.$confirm(`此操作将${statusText}该会员`, '是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(row.id, row.status).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消操作'
        })
        row.status = row.status === 1 ? 0 : 1
      })
    },
    handleEdit(id) {
      this.$common.clear(this.userForm)
      this.userFormTitle = '用户详情'
      memberDetail(id).then(res => {
        this.userForm = res.data
        this.userFormShow = true
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除<${row.username}>用户吗？`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        remove([row.id]).then(res => {
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

<style scoped>

</style>
