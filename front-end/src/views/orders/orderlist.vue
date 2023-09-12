<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出
        </el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeletes()">删除</el-button>
        <el-button icon="el-icon-delete" size="mini" type="success" @click="handlePass()">通过</el-button>
      </el-col>
      <el-col :span="19">
        <el-form ref="queryForm" :model="queryParams" size="small" style="float: right" :inline="true">
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入商品名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="卖家">
            <el-input
              v-model="queryParams.seller"
              placeholder="请输入卖家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="queryParams.state" placeholder="请选择订单状态">
              <el-option label="已发货" :value="1" />
              <el-option label="待发货" :value="0" />
              <el-option label="以完成" :value="2" />
            </el-select>
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
      <el-table-column align="center" label="商品名称" prop="goodsName" width="150 " :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="categoryName" width="100" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="卖家" prop="seller" width="100" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="买家" prop="buyer" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" width="150" align="center" prop="orderSn" />
      <el-table-column label="价格" align="center" prop="payAmount" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="订单状态" align="center" width="100">
        <template  v-slot="scope">
          <el-tag type="success" effect="state" v-if="scope.row.state==1">已发货</el-tag>
          <el-tag type="warning" effect="dark" v-if="scope.row.state==0">待发货</el-tag>
          <el-tooltip class="item" effect="dark" :content="scope.row.mark" placement="top">
            <el-tag type="danger" effect="dark" v-if="scope.row.state==2">已完成</el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="付款时间" align="center" prop="payTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column label="发货时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column label="成交时间" align="center" prop="finallyTime" width="164" :show-overflow-tooltip="true" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { list } from '@/api/orders/oderlist'

export default {
  data() {
    return {
      list: null,
      listLoading: true,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      total: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleSelectionChange(values) {
      this.audits = []
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
      values.map(res => {
        this.audits.push({ 'goodsId': res.id, 'state': 1, 'mark': '成功' })
      })
    },
    getList() {
      this.listLoading = true
      list(this.queryParams).then(response => {
        this.list = response.data
        // this.total = response.data.total
        console.log(response.data)
        this.listLoading = false
      })
    },
    // 删除订单
    handleDelete(){
    },
    handleEdit(){}
  }
}
</script>
