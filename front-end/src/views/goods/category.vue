<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出
        </el-button>
      </el-col>
    </el-row>
    <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="margin: 15px 0px"></el-input>
    <el-tree
      :data="categoryList"
      :props="defaultProps"
      :filter-node-method="filterNode"
      show-checkbox
      node-key="id"
      ref="tree"
      :expand-on-click-node="false">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span style="margin-right: 40px">{{ data.name }}</span>
        <span>
          <el-button
            type="text"
            size="mini"
            v-if="!node.isLeaf"
            @click="() => appendNode(data)">
            添加
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => removeNode(node, data)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { list } from '@/api/goods/category'
export default {
  data() {
    return {
      filterText: '',
      queryParams: {
        name: ''
      },
      categoryList: [],
      // 树形控件配置项，为对象，label代表节点的title标签，children代表节点的子树（孩子）
      defaultProps: {
        label: 'name',
        children: 'childrens'
      },
      // 默认展开的数组
      expandKeys: []
    }
  },
  created() {
    this.getList()
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    getList() {
      list(this.queryParams).then(res => {
        this.categoryList = res.data
        console.log(this.categoryList)
      })
    },
    removeNode(node, data) {
      console.log(node)
    }
  }
}
</script>

<style scoped>

</style>
