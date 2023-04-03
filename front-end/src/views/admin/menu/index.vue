<template>
  <div class="app-container">
    <el-dialog :title="menuFormTitle" :visible.sync="menuFormShow" width="500px">
      <el-form :model="menuForm" label-width="80px">
        <el-form-item label="菜单名称">
          <el-input v-model="menuForm.name" wu></el-input>
        </el-form-item>
        <el-form-item label="父级菜单">
          <el-cascader
            v-model="menuForm.parentId"
            :options="menuSelectOptions"
            :show-all-levels="false"
            :props="{ checkStrictly: true,label:'label',value:'id',expandTrigger: 'hover'}"
            clearable></el-cascader>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="menuForm.path"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="menuForm.icon"></el-input>
        </el-form-item>
        <el-form-item label="组件">
          <el-input v-model="menuForm.component"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="menuForm.sort" oninput="value=value.replace(/[^\d.]/g,'')"></el-input>
        </el-form-item>
        <el-form-item label="跳转">
          <el-input v-model="menuForm.redirect"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="menuForm.visible"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuFormShow = false">取 消</el-button>
        <el-button type="primary" @click="saveMenuInfo()">提 交</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="permissionFormTitle" :visible.sync="permissionFormShow" width="500px">
      <el-form :model="permissionForm" label-width="80px">
        <el-form-item label="权限名称">
          <el-input v-model="permissionForm.name" wu></el-input>
        </el-form-item>
        <el-form-item label="服务名">
          <el-select v-model="permissionForm.serviceName" placeholder="请选择">
            <el-option
              v-for="item in serviceOptions"
              :key="item.value"
              :label="item.serviceName"
              :value="item.serviceCode">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请求方式">
          <el-select v-model="permissionForm.method" placeholder="请选择">
            <el-option
              v-for="item in methodOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="permissionForm.url"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="permissionFormShow = false">取 消</el-button>
        <el-button type="primary" @click="savePermissionInfo()">提 交</el-button>
      </div>
    </el-dialog>

    <el-row :gutter="8">
      <el-col :span="9">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>菜单管理</span>
            <!--            <el-button style="float: right; padding: 1px 0" type="text">操作按钮</el-button>-->
          </div>
          <el-table
            :data="list"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            border
            row-key="id"
            style="width: 100%;margin-bottom: 20px;"
            @row-click="handleRowClick">
            <el-table-column
              label="菜单名称"
              prop="name"
              sortable
              width="150">
            </el-table-column>
            <el-table-column
              label="组件"
              prop="component"
              sortable
              width="197">
            </el-table-column>
            <el-table-column
              label="状态"
              width="64"
              prop="visible">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.visible"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  @change="handleChangeStatus(scope.row)">
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" >
              <template slot-scope="scope">
                <div class="tag-group">
                  <el-tag size="small" style="cursor: pointer" type="success" @click="handleAdd(scope.row)" >新增</el-tag>&nbsp;&nbsp;
                  <el-tag size="small" style="cursor: pointer" type="" @click="handleEdit(scope.row.id)">编辑</el-tag>&nbsp;&nbsp;
                  <el-tag size="small" style="cursor: pointer" type="danger" @click="handleDelete(scope.row)">删除</el-tag>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

      </el-col>
      <el-col :span="15">
        <el-card>
          <div slot="header" class="clearfix">
            <span>权限管理{{ chosedMenuName ? '(当前菜单:' + chosedMenuName + ')' : '' }}</span>
            <el-tag size="small" style="cursor: pointer;float: right" type="danger" @click="permissionAdd()">新增
            </el-tag>
          </div>
          <el-table
            :data="permissionList"
            border
            row-key="id"
            style="width: 100%;margin-bottom: 20px;">
            <el-table-column
              label="权限名称"
              prop="name"
              sortable
              width="120">
            </el-table-column>
            <el-table-column
              label="服务名称"
              prop="urlPerm"
              sortable
              width="150">
              <template slot-scope="scope">
                {{ getServiceName(handleServiceName(scope.row.urlPerm)) }}
              </template>
            </el-table-column>
            <el-table-column
              label="请求方式"
              prop="urlPerm"
              sortable
              width="120">
              <template slot-scope="scope">
                {{ scope.row.urlPerm.split(":")[0] }}
              </template>
            </el-table-column>
            <el-table-column
              label="请求路径"
              prop="urlPerm"
              sortable
              width="180">
              <template slot-scope="scope">
                {{ handleUrl(scope.row.urlPerm) }}
              </template>
            </el-table-column>
            <el-table-column
              label="按钮标识"
              prop="btnSign"
              sortable>
              <template slot-scope="scope">
                {{scope.row.btnSign}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <div class="tag-group">
                  <el-tag size="small" style="cursor: pointer" type="" @click="handlePermissionEdit(scope.row)">编辑
                  </el-tag>&nbsp;&nbsp;
                  <el-tag size="small" style="cursor: pointer" type="danger" @click="handlePermissionDelete(scope.row)">
                    删除
                  </el-tag>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {list, menuDetail, menuSelect, updateMenu, createMenu, deleteMenu, updateMenuStatus} from '@/api/admin/menu'
import {
  permissionList,
  updatePermission,
  deletePermissions,
  servicesSelect,
  createPermission
} from '@/api/admin/permission'

export default {
  components: {},
  created() {
    this.fetchMenuSelect()
    this.fetchData()
    this.fetchServicesSelect()
  },
  data() {
    return {
      permissionFormShow: false,
      permissionFormTitle: '',
      permissionForm: {
        id: '',
        name: '',
        menuId: '',
        method: '',
        serviceName: '',
        url: ''
      },
      methodOptions: [
        {
          label: 'POST',
          value: 'POST'
        },
        {
          label: 'GET',
          value: 'GET'
        },
        {
          label: 'PUT',
          value: 'PUT'
        },
        {
          label: 'DELETE',
          value: 'DELETE'
        },
        {
          label: 'PATCH',
          value: 'PATCH'
        }
      ],
      serviceOptions: [],
      chosedMenuName: '',
      permissionList: [],
      menuId: '',
      menuSelectOptions: [
        { id: -1, label: '根目录', children: [] }
      ],
      menuFormShow: false,
      menuFormTitle: '',
      menuForm: {
        id: '',
        name: '',
        parentId: '',
        path: '',
        component: '',
        icon: '',
        sort: '',
        visible: 1,
        redirect: ''
      },
      list: []
    }
  },
  methods: {
    handleChangeStatus(row) {
      const statusText = row.status === 1 ? '启用' : '禁用'
      this.$confirm(`此操作将${statusText}该菜单`, '是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateMenuStatus(row.id, row.visible).then(res => {
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
        row.visible = row.visible === 1 ? 0 : 1
      })
    },
    savePermissionInfo() {
      if (this.permissionForm.url && !this.permissionForm.url.startsWith('/')) {
        this.$message({
          type: 'warning',
          message: 'url必须以/开头!'
        })
        return
      }
      if (this.permissionForm.id) {
        updatePermission(this.permissionForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
          this.fetchPermissionList(this.permissionForm.menuId)
          this.permissionFormShow = false
        })
      } else {
        createPermission(this.permissionForm).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.fetchPermissionList(this.permissionForm.menuId)
          this.permissionFormShow = false
        })
      }
    },
    saveMenuInfo() {
      if (this.menuForm.parentId instanceof Array) {
        const parentId = this.menuForm.parentId
        this.menuForm.parentId = parentId.length === 1 ? parentId[0] : parentId[parentId.length-1]
      }
      if (!this.menuForm.parentId) {
        this.menuForm.parentId = -1
      }
      if (this.menuForm.parentId === this.menuForm.id){
        this.$message({
          type: 'warning',
          message: '不能将自己设置为父级菜单!'
        })
        return
      }
      if (this.menuForm.id) {
        updateMenu(this.menuForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
          this.fetchData()
          this.fetchMenuSelect()
          this.menuFormShow = false
        })
      } else {
        createMenu(this.menuForm).then(res => {
          this.$message({
            type: 'success',
            message: '菜单添加成功!'
          })
          this.fetchData()
          this.fetchMenuSelect()
          this.menuFormShow = false
        })
      }
    },
    fetchData() {
      list().then(res => {
        this.list = res.data
      })
    },
    handleEdit(id) {
      this.$common.clear(this.menuForm)
      this.menuFormTitle = '编辑菜单'
      menuDetail(id).then(res => {
        this.menuForm = res.data
        this.menuFormShow = true
      })
    },
    handleUrl(value) {
      const startIndex = this.$common.indexOf(value, '/', 1)
      const requestUrl = value.substring(startIndex)
      return requestUrl
    },
    handleServiceName(value) {
      const startIndex = this.$common.indexOf(value, '/', 0)
      const endIndex = this.$common.indexOf(value, '/', 1)
      const serviceName = value.substring(startIndex + 1, endIndex)
      // 获取服务显示名称
      return serviceName
    },
    getServiceName(serviceCode) {
      let serviceName = serviceCode
      this.serviceOptions.forEach((value) => {
        if (value.serviceCode === serviceCode) {
          serviceName = value.serviceName
        }
      })
      return serviceName
    },
    fetchServicesSelect() {
      servicesSelect().then(res => {
        this.serviceOptions = res.data
        this.fetchData()
      })
    },
    handleRowClick(row) {
      this.chosedMenuName = row.name
      this.menuId = row.id
      this.fetchPermissionList(row.id)
    },
    fetchPermissionList(menuId) {
      permissionList(menuId).then(res => {
        this.permissionList = res.data
      })
    },
    handlePermissionDelete(row) {
      this.$confirm(`确定删除此权限吗？`, '是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePermissions(row.id).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.fetchPermissionList(this.menuId)
        })
      })
    },
    permissionAdd() {
      if (!this.menuId) {
        this.$message({
          type: 'warning',
          message: '请选择菜单!'
        })
        return
      }
      this.$common.clear(this.permissionForm)
      this.permissionForm.menuId = this.menuId
      this.permissionFormShow = true
      this.permissionFormTitle = '添加权限'
    },
    handleAdd(row) {
      this.$common.clear(this.menuForm)
      this.menuForm.visible = 1
      this.menuFormTitle = '新增菜单'
      this.menuFormShow = true
      this.menuForm.parentId = row.id
    },
    handleDelete(row) {
      this.$confirm(`确定删除此菜单吗？`, '是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(res => {
          this.$message({
            type: 'success',
            message: '操作成功!'
          })
          this.fetchData()
          this.fetchMenuSelect()
        })
      })
    },
    handlePermissionEdit(row) {
      this.$common.clear(this.permissionForm)
      this.permissionForm.id = row.id
      this.permissionForm.name = row.name
      this.permissionForm.menuId = row.menuId
      this.permissionForm.method = row.urlPerm.split(':')[0]
      this.permissionForm.serviceName = this.handleServiceName(row.urlPerm)
      this.permissionForm.url = this.handleUrl(row.urlPerm)
      this.permissionFormShow = true
      this.permissionFormTitle = '编辑权限'
    },
    fetchMenuSelect() {
      menuSelect().then(res => {
        this.menuSelectOptions[0].children = res.data
        this.menuSelectOptions = this.deleteChildren(this.menuSelectOptions)
      })
    },
    // 删除空children
    deleteChildren(arr) {
      let childs = arr
      for (let i = childs.length; i--; i > 0) {
        if (childs[i].children) {
          if (childs[i].children.length) {
            this.deleteChildren(childs[i].children)
          } else {
            delete childs[i].children
          }
        }
      }
      return arr
    }
  }

}
</script>

<style scoped>

</style>
