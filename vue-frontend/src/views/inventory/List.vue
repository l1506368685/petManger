<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="商品名称"><el-input v-model="query.goodsName" placeholder="商品名称" clearable /></el-form-item>
        <el-form-item label="商品编码"><el-input v-model="query.goodsCode" placeholder="商品编码" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <p style="margin-bottom:12px">当前各商品库存：采购入库增加、订单出库减少</p>
      <el-table v-loading="loading" :data="list" border size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="goodsCode" label="商品编码" />
        <el-table-column prop="stock" label="当前库存" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog title="调整库存" :visible.sync="dialogVisible" width="400px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="商品名称">{{ form.goodsName }}</el-form-item>
        <el-form-item label="当前库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" style="width:100%" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { list, updateStock, remove } from '@/api/inventory'

export default {
  name: 'InventoryList',
  data() {
    return {
      query: { goodsName: '', goodsCode: '' },
      list: [],
      loading: false,
      dialogVisible: false,
      form: {},
      formRules: { stock: [{ required: true, message: '请输入库存', trigger: 'blur' }] }
    }
  },
  created() { this.load() },
  methods: {
    load() {
      this.loading = true
      list(this.query).then(res => { this.list = res.data || [] }).finally(() => { this.loading = false })
    },
    resetQuery() { this.query = { goodsName: '', goodsCode: '' }; this.load() },
    openEdit(row) {
      this.form = { goodsId: row.goodsId, goodsName: row.goodsName, stock: row.stock }
      this.dialogVisible = true
    },
    submitEdit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        updateStock({ id: this.form.goodsId, stock: this.form.stock }).then(() => {
          this.$message.success('调整成功')
          this.dialogVisible = false
          this.load()
        })
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该商品库存记录？删除后商品将在商品管理中逻辑删除。', '提示', { type: 'warning' })
        .then(() => remove(row.goodsId).then(() => { this.$message.success('删除成功'); this.load() }))
        .catch(() => {})
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
