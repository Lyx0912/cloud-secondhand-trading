<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="23">
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
          <el-form-item label="买家">
            <el-input
              v-model="queryParams.buyer"
              placeholder="请输入买家名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="queryParams.state" placeholder="请选择订单状态">
              <el-option label="待发货" :value="1" />
              <el-option label="待付款" :value="0" />
              <el-option label="已发货" :value="2" />
              <el-option label="已完成" :value="3" />
              <el-option label="已取消" :value="4" />
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
<!--      <el-table-column-->
<!--        type="selection"-->
<!--        width="55"-->
<!--      />-->
      <el-table-column align="center" label="商品名称" prop="goodsName" width="160 " :show-overflow-tooltip="true" />
      <el-table-column align="center" label="卖家" prop="sellerName" width="130" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="买家" prop="buyerName" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" width="190" align="center" prop="orderSn" />
      <el-table-column label="支付流水号" width="160" align="center" prop="bizPayNo" />
      <el-table-column label="价格" align="center" prop="payAmount" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="支付方式" align="center" width="100">
        <template  v-slot="scope">
          <span type="danger" effect="dark" v-if="scope.row.payId==1">支付宝支付</span>
          <span type="success" effect="dark" v-if="scope.row.payId==2">微信支付</span>
          <span type="success" effect="dark" v-if="scope.row.payId==3">花呗支付</span>
        </template>
      </el-table-column>
      <el-table-column label="付款时间" align="center" prop="createTime" width="164" :show-overflow-tooltip="true" />
      <el-table-column label="支付状态" align="center" width="100">
        <template  v-slot="scope">
          <el-tag type="danger" effect="dark" v-if="scope.row.payStatus==0">已取消</el-tag>
          <el-tag type="success" effect="dark" v-if="scope.row.payStatus==1">已支付</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />

  </div>
</template>
<script>
import { list } from '@/api/orders/pay'
import pagination from '@/components/Pagination'

export default {
  components: {pagination},
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
        this.list = response.data.list
        this.total = response.data.total
        console.log(response.data.total)
        this.listLoading = false
      })
    }
    // 删除订单
    // handleDelete(){
    // },
    // handleEdit(){}
  }
}
</script>
