<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="19">
        <el-button icon="el-icon-plus" size="mini" type="warning" @click="handleAdd()" v-permission="'admin:user:add'">新增</el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
      </el-col>
      <el-col :span="5">
        <el-row style="display: flex">
          <el-input v-model="queryParams.keyword" placeholder="请输入内容" size="small" style="margin-right: 20px"></el-input>
          <el-button icon="el-icon-search" size="mini" type="primary" @click="getList">查询</el-button>
        </el-row>
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
        width="55">
      </el-table-column>
      <el-table-column align="center" label="用户名" prop="username" />
      <el-table-column label="用户昵称" align="center" prop="nickname" v-permission="'admin:user:add'"/>
      <el-table-column label="手机号" align="center" prop="mobile" width="150"/>
      <el-table-column label="邮箱"  align="center" prop="email" />
      <el-table-column class-name="status-col" label="性别" width="110" align="center">
        <template v-slot="scope">
          <el-tag :type="scope.row.gender===1?'primary':'success'">{{scope.row.gender===1?'男':'女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="账号状态" width="150">
        <template slot-scope="scope">
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
      <el-table-column align="center" prop="created_at" label="操作" >
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="userFormTitle" :visible.sync="userFormShow" width="500px">
      <el-form :model="userForm" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="userForm.username" wu></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="userForm.nickname"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <template>
            <el-radio v-model="userForm.gender" :label="0">男</el-radio>
            <el-radio v-model="userForm.gender" :label="1">女</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userForm.mobile"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="userForm.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="userForm.roleIds">
            <el-checkbox v-for="role in roleSelect" :key="role.id" :label="role.id">{{ role.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userFormShow = false">取 消</el-button>
        <el-button type="primary" @click="saveUserInfo()">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, updateStatus, userDetail, updateUserInfo, deletes, createUserInfo } from '@/api/admin/user'
import { roleSelect } from '@/api/admin/role'
import pagination from '@/components/Pagination'

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
        roleIds: []
      },
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        keyword: ''
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
    handleSelectionChange(values) {
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
    },
    handleAdd() {
      this.$common.clear(this.userForm)
      this.userForm.status = 1
      this.userFormTitle = '添加用户信息'
      this.userFormShow = true
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
    saveUserInfo() {
      if (this.userForm.id) {
        updateUserInfo(this.userForm.id, this.userForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
          this.userFormShow = false
          this.getList()
        })
      } else {
        createUserInfo(this.userForm).then(res => {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          this.userFormShow = false
          this.getList()
        })
      }
    },
    getList() {
      this.listLoading = true
      getList(this.queryParams).then(response => {
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
      this.$confirm(`此操作将${statusText}该用户`, '是否继续?', '提示', {
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
      this.userFormTitle = '编辑用户信息'
      userDetail(id).then(res => {
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
