<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出</el-button>
        <el-button icon="el-icon-plus" size="mini" type="primary" @click="handleAdd()">添加目录</el-button>
        <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleBatchDelete()">批量删除</el-button>
      </el-col>
    </el-row>
    <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="margin: 15px 0px;"></el-input>
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
            v-if="node.level !=3"
            @click="appendNode(node)">
            添加
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="removeNode(node)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog :title="CategoryFormTitle" :visible.sync="CategoryFormShow" width="500px">
      <el-form :model="categoryForm" label-width="80px" :rules="rules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="categoryForm.name" ></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="categoryForm.sort"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="CategoryFormShow = false">取 消</el-button>
        <el-button type="primary" @click="saveCategory()">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, remove, save, batchDelete } from '@/api/goods/category'
import { exportFile } from '@/utils/request'
export default {
  data() {
    return {
      categoryForm: {

      },
      CategoryFormShow: false,
      CategoryFormTitle: '添加分类',
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
      expandKeys: [],
      rules: {
        name: [{ required: true, trigger: 'blur', message: '分类名称不能为空' }],
      }
    }
  },
  created() {
    this.getList()
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    handleExport() {
      exportFile('/cloud-goods/category/export', '分类列表')
    },
    handleBatchDelete() {
      this.$confirm(`确定删除该分类及子分类吗`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        batchDelete(this.$refs.tree.getCheckedKeys()).then(res => {
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
    handleAdd() {
      this.CategoryFormShow = true
      // 设置分类级别和父分类
      this.categoryForm.parentCid = 0
      this.categoryForm.catLevel = 1
    },
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1;
    },
    getList() {
      list(this.queryParams).then(res => {
        this.categoryList = res.data
      })
    },
    appendNode(node) {
      console.log(node)
      this.CategoryFormShow = true
      // 设置分类级别和父分类
      this.categoryForm.parentCid = node.data.id
      this.categoryForm.catLevel = node.level + 1
    },
    removeNode(node, data) {
      if (!node.isLeaf) {
        this.$message({
          type: 'error',
          message: '请先删除子分类'
        })
        return
      }
      this.$confirm(`确定删除该分类及子分类吗`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        remove([node.data.id]).then(res => {
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
    saveCategory() {
      save(this.categoryForm).then(res => {
        this.getList()
        this.categoryForm = {}
        this.CategoryFormShow = false
        this.$refs.tree.setCheckedKeys([])
      })
    }
  }
}
</script>

<style scoped>

</style>
