<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleRecommed()">发布公告</el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
      </el-col>
      <el-col :span="19">
        <el-form ref="queryForm" :model="queryParams" size="small" style="float: right" :inline="true">
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入标题"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="发布者">
            <el-input
              v-model="queryParams.creater"
              placeholder="请输入卖家名称"
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
      <el-table-column align="center" label="编号" prop="id" width="100 " :show-overflow-tooltip="true" />
      <el-table-column align="center" label="标题" prop="title" width="150 " :show-overflow-tooltip="true" />
      <el-table-column label="内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="发布者" prop="creater" width="200" :show-overflow-tooltip="true" />
      <el-table-column label="发布时间" align="center" prop="createTime"  width="200"/>
      <el-table-column label="上架状态" align="center" width="100">
        <template v-slot="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="280" >
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { exportFile } from '@/utils/request'
import { list } from '@/api/message/systemMessage'
import pagination from '@/components/Pagination'

export default {
  name: 'SysMessage',
  components: { pagination },
  data() {
    return {
      checkedIds: [],
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
      exportFile('/cloud-goods/goods/export', '商品列表')
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
    getList() {
      this.listLoading = true
      list(this.queryParams).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
