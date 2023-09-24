<template>
<div class="market-box">
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>营销</el-breadcrumb-item>
    <el-breadcrumb-item>营销策略</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card class="operate-container" shadow="never">
    <i class="el-icon-tickets"></i>
    <span>营销方案</span>
  </el-card>

  <div class="table-container">
    <el-table
        :data="setData"
        border
        style="width: 100%"
        :default-sort="{ prop: 'mid', order: 'descending'}"
    >
      <el-table-column
          type="index"
          width="60"
          align="center"
      ></el-table-column>

      <el-table-column
          label="营销方案"
          width="820"
          align="center"
      >
        <template slot-scope="scope">{{scope.row.programme}}</template>
      </el-table-column>

      <el-table-column
          label="活动对象"
          width="120"
          align="center"
      >
        <template slot-scope="scope">{{scope.row.Object}}</template>
      </el-table-column>

      <el-table-column
          label="是否启用"
          align="center"
      >
        <template slot-scope="scope">
          <p>
            <el-switch
                v-model="scope.row.value1"
                >
            </el-switch>
          </p>
        </template>
      </el-table-column>
    </el-table>
  </div>
</div>
</template>

<script>
export default {
name: "Marketing",
  data(){
  return{


    setData:[
      {
        mid:"1",
        Object:"小米k40",
        programme:"根据抢购顺序，第1-100名用户商品1折销售，第101-500名5折，第501-1000名8折，第1001名之后商品原价销售",
        value1:true,

      },
      {
        mid:"2",
        Object: "暂无",
        programme:"随机抽取参加订购该商品的100名用户进行商品的免单发放，对100名之后的用户商品8折销售",
        value1:false,
      }
    ]
  }
  }
}
</script>

<style scoped>

</style>