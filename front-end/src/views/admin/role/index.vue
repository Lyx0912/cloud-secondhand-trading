<template>
  <div class="dashboard-container">
    <el-row :gutter="8">
      <el-col :span="11">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>角色管理</span>
            <el-button style="float: right; padding: 1px 0" type="text" @click="handleAdd">新增</el-button>
          </div>
          <el-dialog :title="roleFormTitle" :visible.sync="roleFormShow" width="500px">
            <el-form :model="roleForm" label-width="80px">
              <el-form-item label="角色名称">
                <el-input v-model="roleForm.name" wu></el-input>
              </el-form-item>
              <el-form-item label="角色编码">
                <el-input v-model="roleForm.code"></el-input>
              </el-form-item>
              <el-form-item label="排序">
                <el-input v-model="roleForm.sort"></el-input>
              </el-form-item>
              <el-form-item label="状态">
                <el-switch
                  v-model="roleForm.status"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
                </el-switch>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="roleFormShow = false">取 消</el-button>
              <el-button type="primary" @click="saveRoleInfo()">提 交</el-button>
            </div>
          </el-dialog>
          <el-table
            v-loading="listLoading"
            :data="roleList"
            border
            row-key="id"
            style="width: 100%;margin-bottom: 20px;"
            @row-click="handleRoleRowClick">
            <el-table-column
              label="角色名称"
              prop="name">
            </el-table-column>
            <el-table-column
              label="角色编码"
              prop="code"
              width="150">
            </el-table-column>
            <el-table-column
              label="显示顺序"
              prop="sort"
              width="80">
            </el-table-column>
            <el-table-column
              label="状态"
              prop="visible" width="90">
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
            <el-table-column align="center" label="操作" >
              <template slot-scope="scope">
                <div class="tag-group">
                  <el-tag size="small" style="cursor: pointer" type="" @click="handleEdit(scope.row)">编辑</el-tag>&nbsp;&nbsp;
                  <el-tag size="small" style="cursor: pointer" type="danger" @click="handleDelete(scope.row.id)">删除
                  </el-tag>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :limit.sync="pageParam.pageSize" :page.sync="pageParam.pageNo" :total="total" @pagination="fetchRoleData"/>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>菜单选择</span>
            <el-button style="float: right; padding: 1px 0" type="text" @click="saveRoleMenu">保存</el-button>
          </div>
          <el-tree
            ref="tree"
            :data="menus"
            :default-checked-keys="[]"
            node-key="id"
            show-checkbox
            @node-click="nodeClick">
          </el-tree>
        </el-card>
      </el-col>
      <el-col :span="7">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>权限选择</span>
            <el-button style="float: right; padding: 1px 0" type="text" @click="saveRolePermission">保存</el-button>
          </div>

          <el-checkbox-group v-model="rolePermissions">
            <el-checkbox v-for="item in permissions" :key="item.id" :label="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { createRole, deletes, listPage, updateRole, updateStatus } from '@/api/admin/role'
import { mapGetters } from 'vuex'
import { menuSelect, roleMenus, saveRoleMenus } from '@/api/admin/menu'
import { permissionList, rolePermissions, saveRolePermissions } from '@/api/admin/permission'
import Pagination from '@/components/Pagination'

export default {
  name: 'Dashboard',
  components: { Pagination },
  data() {
    return {
      roleId: '',
      listLoading: false,
      rolePermissions: [],
      permissions: [],
      roleFormTitle: '',
      menus: [],
      roleFormShow: false,
      roleForm: {
        id: '',
        name: '',
        code: '',
        sort: '',
        status: 1
      },
      roleList: [],
      ruleListLoading: true,
      total: 0,
      pageParam: {
        pageNo: 1,
        pageSize: 10,
        keyword: ''
      }
    }
  },
  created() {
    this.fetchRoleData()
    this.loadMenuSelect()
  },
  methods: {
    saveRolePermission() {
      if (!this.roleId) {
        this.$message({
          type: 'error',
          message: '请先选择角色!'
        })
        return
      }
      saveRolePermissions(this.roleId, {ids: this.rolePermissions}).then(res => {
        this.$message({
          type: 'success',
          message: '更新成功!'
        })
      })
    },
    saveRoleMenu() {
      if (!this.roleId) {
        this.$message({
          type: 'error',
          message: '请先选择角色!'
        })
        return
      }
      var arr = [];
      this.$refs.tree.getCheckedNodes(false, true).forEach(item=>{
        arr.push(item.id)
      })
      saveRoleMenus(this.roleId, { ids: arr }).then(res => {
        this.$message({
          type: 'success',
          message: '更新成功!'
        })
      })
    },
    nodeClick(menu, nodeInfo) {
      permissionList(menu.id).then(res => {
        this.permissions = res.data
      })
    },
    handleRoleRowClick(role) {
      this.roleId = role.id
      this.permissions = []
      rolePermissions(role.id).then(res => {
        this.rolePermissions = res.data
      })
      roleMenus(role.id).then(res => {
        this.$nextTick(() => {
          res.data.forEach((element) => {
            var node = this.$refs.tree.getNode(element)
            if (node.isLeaf) {
              this.$refs.tree.setChecked(node, true)
            }
          })
        })
        // this.$refs.tree.setCheckedKeys(res.data)
      })
    },
    loadMenuSelect() {
      menuSelect(1).then(res => {
        this.menus = res.data
      })
    },
    handleStatusChange(row) {
      const statusText = row.status === 1 ? '启用' : '禁用'
      this.$confirm(`此操作将${statusText}该角色`, '是否继续?', '提示', {
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
    saveRoleInfo() {
      if (this.roleForm.id) {
        updateRole(this.roleForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
          this.roleFormShow = false
          this.fetchRoleData()
        })
      } else {
        createRole(this.roleForm).then(res => {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          this.roleFormShow = false
          this.fetchRoleData()
        })
      }
    },
    handleDelete(id) {
      this.$confirm(`确定删除此角色吗？`, '是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletes(id).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.fetchRoleData()
        })
      })
    },
    handleAdd() {
      this.$common.clear(this.roleForm)
      this.roleFormShow = true
      this.roleForm.status = 1
      this.roleFormTitle = '新增角色'
    },
    handleEdit(row) {
      this.$common.clear(this.roleForm)
      this.roleForm.id = row.id
      this.roleForm.name = row.name
      this.roleForm.code = row.code
      this.roleForm.sort = row.sort
      this.roleForm.status = row.status
      this.roleFormShow = true
      this.roleFormTitle = '编辑角色'
    },
    fetchRoleData() {
      listPage(this.pageParam).then(res => {
        this.roleList = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
