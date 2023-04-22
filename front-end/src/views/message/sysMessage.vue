<template>
  <div class="app-container">
    <el-row style="margin-bottom: 12px">
      <el-col :span="5">
        <el-button icon="el-icon-download" size="mini" type="warning" @click="handleExport()">导出</el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd()">发布公告</el-button>
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
      <el-table-column align="center" label="标题" prop="title" width="250 " :show-overflow-tooltip="true" />
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
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="systemMessageFormTitle" :visible.sync="systemMessageFormShow">
      <el-form :rules="rules"  :model="systemMessageForm" label-width="80px">
        <el-form-item prop="title" label="标题">
          <el-input v-model="systemMessageForm.title" />
        </el-form-item>
        <el-form-item prop="isOnSell" label="状态">
          <el-switch
            v-model="systemMessageForm.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
        <el-form-item prop="content" label="详情">
          <we-editor :toolbar-option="toolbar" style="width: 100%;height: 400px;border: #DCDFE6 1px solid;border-radius: 4px" :editable-option="editable" :mode="mode" v-bind:html.sync="systemMessageForm.content" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">关 闭</el-button>
        <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { exportFile } from '@/utils/request'
import { info, list, update, save, deletes } from '@/api/message/systemMessage'
import pagination from '@/components/Pagination'
import { useWangEditor } from 'wangeditor5-for-vue2'

export default {
  name: 'SysMessage',
  components: { pagination },
  data() {
    return {
      mode: 'simple',
      ...useWangEditor({
        config: {
          extendCache: false,
          MENU_CONF: {
            uploadImage: {
              // 后端上传地址，必填
              server: '/api/upload/image',
              // form-data fieldName，后端接口参数名称，默认值wangeditor-uploaded-image
              fieldName: 'file',
              // 1M，单个文件的最大体积限制，默认为 2M
              maxFileSize: 1 * 1024 * 1024,
              // 最多可上传几个文件，默认为 100
              maxNumberOfFiles: 10,
              // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
              allowedFileTypes: ['image/*'],
              // 15 秒，超时时间，默认为 10 秒
              timeout: 15 * 1000
              // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
              // meta: {
              //     token: 'xxx',
              //     otherKey: 'yyy'
              // },
              // 将 meta 拼接到 url 参数中，默认 false
              // metaWithUrl: false,
              // 自定义增加 http  header
              // headers: {
              //     Accept: 'text/x-json',
              //     otherKey: 'xxx'
              // },
              // 跨域是否传递 cookie ，默认为 false
              // withCredentials: false,
            },
            uploadVideo: {
              // 后端上传地址，必填
              server: '/api/upload/video',
              // form-data fieldName，后端接口参数名称，默认值wangeditor-uploaded-video
              fieldName: 'file',
              // 5M，文件大小限制，默认10M
              maxFileSize: 5 * 1024 * 1024,
              // 最多可上传几个文件，默认为 5
              maxNumberOfFiles: 3,
              // 选择文件时的类型限制，默认为 ['video/*'] 。如不想限制，则设置为 []
              allowedFileTypes: ['video/*'],
              // 15 秒，超时时间，默认为 30 秒
              timeout: 15 * 1000
              // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
              // meta: {
              //     token: 'xxx',
              //     otherKey: 'yyy'
              // },
              // 将 meta 拼接到 url 参数中，默认 false
              // metaWithUrl: false,
              // 自定义增加 http  header
              // headers: {
              //     Accept: 'text/x-json',
              //     otherKey: 'xxx'
              // },
              // 跨域是否传递 cookie ，默认为 false
              // withCredentials: false,
            }
          }
          // onCreated: (inst) => {
          // }
        }
      }),
      systemMessageFormShow: false,
      systemMessageFormTitle: '编辑公告',
      systemMessageForm: {},
      checkedIds: [],
      total: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      list: null,
      listLoading: true,
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    closeDialog() {
      this.reloadEditor()
      this.systemMessageFormShow = false
    },
    handleEdit(id) {
      info(id).then(res => {
        this.systemMessageForm = res.data
        this.systemMessageFormShow = true
      })
    },
    handleAdd() {
      this.systemMessageForm = {
        content: '请输入公告内容'
      }
      this.systemMessageFormShow = true
    },
    handleSelectionChange(values) {
      this.checkedIds = []
      values.map(res => this.checkedIds.push(res.id))
    },
    /** 导出excel文件 */
    handleExport() {
      exportFile('/cloud-message/systemMessage/export', '系统公告列表')
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
    },
    saveOrUpdate() {
      // 强制同步 v-model 数据
      this.syncContent()
      if (this.systemMessageForm.id) {
        update(this.systemMessageForm).then(res => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          })
        })
      } else {
        save(this.systemMessageForm).then(res => {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
        })
      }
      this.reloadEditor()
      this.systemMessageFormShow = false
      this.getList()
    },
    handleDelete(id) {
      this.$confirm(`确定执行删除操作吗？`, '是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '算了吧',
        type: 'warning'
      }).then(() => {
        deletes([id]).then(res => {
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
    }
  }
}
</script>

<style src="@wangeditor/editor/dist/css/style.css"></style>
<style scoped>

</style>
