<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="商品名称"><el-input v-model="query.goodsName" placeholder="商品名称" clearable /></el-form-item>
        <el-form-item label="商品编码"><el-input v-model="query.goodsCode" placeholder="商品编码" clearable /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.category" placeholder="全部" clearable><el-option v-for="c in categoryOptions" :key="c.itemValue" :label="c.itemLabel" :value="c.itemValue" /></el-select>
        </el-form-item>
        <el-form-item label="品牌"><el-input v-model="query.brand" placeholder="品牌" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="goodsCode" label="商品编码" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="brand" label="品牌" />
        <el-table-column prop="price" label="售价" width="80" />
        <el-table-column prop="originalPrice" label="原价" width="80" />
        <el-table-column prop="stockLower" label="库存下限" width="90" />
        <el-table-column prop="stockUpper" label="库存上限" width="90" />
        <el-table-column prop="intro" label="商品简介" show-overflow-tooltip />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '商品'" :visible.sync="dialogVisible" width="600px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="商品名称" prop="goodsName"><el-input v-model="form.goodsName" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类（在数据字典中维护）" clearable style="width:100%">
            <el-option v-for="c in categoryOptions" :key="c.itemValue" :label="c.itemLabel" :value="c.itemValue" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品编码"><el-input v-model="form.goodsCode" /></el-form-item>
        <el-form-item label="品牌"><el-input v-model="form.brand" /></el-form-item>
        <el-form-item label="售价" prop="price"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="成本价"><el-input-number v-model="form.costPrice" :min="0" :precision="2" style="width:100%" placeholder="用于毛利分析" /></el-form-item>
        <el-form-item label="原价"><el-input-number v-model="form.originalPrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="库存下限"><el-input-number v-model="form.stockLower" :min="0" style="width:100%" placeholder="低于时采购预警" /></el-form-item>
        <el-form-item label="库存上限"><el-input-number v-model="form.stockUpper" :min="0" style="width:100%" placeholder="高于时积压预警" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit" placeholder="件" /></el-form-item>
        <el-form-item label="商品简介"><el-input v-model="form.intro" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="商品详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="商品名称">{{ detail.goodsName }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ detail.category }}</el-descriptions-item>
        <el-descriptions-item label="商品编码">{{ detail.goodsCode }}</el-descriptions-item>
        <el-descriptions-item label="品牌">{{ detail.brand }}</el-descriptions-item>
        <el-descriptions-item label="售价">{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="成本价">{{ detail.costPrice }}</el-descriptions-item>
        <el-descriptions-item label="库存下限">{{ detail.stockLower }}</el-descriptions-item>
        <el-descriptions-item label="库存上限">{{ detail.stockUpper }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ detail.intro }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/goods'
import { items as dictItems } from '@/api/dict'

export default {
  name: 'GoodsList',
  data() {
    return {
      query: { current: 1, size: 10, goodsName: '', goodsCode: '', brand: '', category: '' },
      categoryOptions: [],
      list: [], total: 0, loading: false,
      dialogVisible: false, detailVisible: false, form: {}, detail: {},
      formRules: { goodsName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }], price: [{ required: true, message: '请输入售价', trigger: 'blur' }] }
    }
  },
  created() { this.load(); dictItems('goods_category').then(res => { this.categoryOptions = res.data || [] }).catch(() => {}) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, goodsName: '', goodsCode: '', brand: '', category: '' }; this.load() },
    openEdit(row) { this.form = row ? { ...row } : { status: 1, unit: '件' }; this.dialogVisible = true },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() { this.$refs.formRef.validate(valid => { if (!valid) return; if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() }); else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() }) }) },
    handleDelete(row) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
