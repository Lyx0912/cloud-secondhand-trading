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
          <el-form-item label="菜单名称" prop="name">
            <el-input
              v-model="queryParams.memberId"
              placeholder="请输入菜单名称"
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
      :data="categoryList"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
        prop="id"
        label="编号"
        sortable
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="菜单名"
        sortable
        width="180">
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.showStatus"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="productCount"
        label="商品数量"
        sortable
        width="180">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { list } from '@/api/goods/category'
export default {
  data() {
    return {
      queryParams: {
        name: ''
      },
      categoryList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      list(this.queryParams).then(res => {
        this.categoryList = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>
