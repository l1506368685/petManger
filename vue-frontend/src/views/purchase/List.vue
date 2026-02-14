<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query">
        <el-form-item label="采购单号"><el-input v-model="query.purchaseNo" clearable /></el-form-item>
        <el-form-item label="商品名称"><el-input v-model="query.goodsName" clearable /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="query.supplier" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="purchaseNo" label="采购单号" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="spec" label="规格" width="80" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="price" label="单价" width="80" />
        <el-table-column prop="amount" label="金额" width="90" />
        <el-table-column prop="supplier" label="供应商" />
        <el-table-column prop="purchaseDate" label="采购日期" width="110" />
        <el-table-column label="操作" :width="actionColumnWidth">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '采购'" :visible.sync="dialogVisible" width="560px">
      <el-form ref="formRef" :model="form" label-width="100px">
        <el-form-item label="选择商品">
          <el-select v-model="form.goodsId" filterable clearable placeholder="选择后自动带出名称并用于入库增加库存" style="width:100%" @change="onGoodsSelect">
            <el-option v-for="g in goodsList" :key="g.id" :label="g.goodsName" :value="g.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称"><el-input v-model="form.goodsName" placeholder="可手填或从上方选择商品" /></el-form-item>
        <el-form-item label="规格"><el-input v-model="form.spec" /></el-form-item>
        <el-form-item label="采购数量"><el-input-number v-model="form.quantity" :min="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="form.supplier" filterable allow-create default-first-option placeholder="选择或输入供应商" style="width:100%">
            <el-option v-for="s in supplierList" :key="s.id" :label="s.name" :value="s.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="采购日期"><el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" style="width:100%" placeholder="不选则默认今天" /></el-form-item>
        <el-form-item label="批次号"><el-input v-model="form.batchNo" placeholder="选填，用于保质期追溯" /></el-form-item>
        <el-form-item label="生产日期"><el-date-picker v-model="form.productionDate" type="date" value-format="yyyy-MM-dd" style="width:100%" clearable /></el-form-item>
        <el-form-item label="到期日"><el-date-picker v-model="form.expiryDate" type="date" value-format="yyyy-MM-dd" style="width:100%" placeholder="用于保质期预警" clearable /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="采购详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="采购单号">{{ detail.purchaseNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ detail.goodsName }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ detail.quantity }}</el-descriptions-item>
        <el-descriptions-item label="单价">{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="金额">{{ detail.amount }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ detail.supplier }}</el-descriptions-item>
        <el-descriptions-item label="采购日期">{{ detail.purchaseDate }}</el-descriptions-item>
        <el-descriptions-item label="批次号">{{ detail.batchNo }}</el-descriptions-item>
        <el-descriptions-item label="生产日期">{{ detail.productionDate }}</el-descriptions-item>
        <el-descriptions-item label="到期日">{{ detail.expiryDate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/purchase'
import { listAll as goodsListAll } from '@/api/goods'
import { listAll as supplierListAll } from '@/api/supplier'

export default {
  name: 'PurchaseList',
  data() {
    return {
      query: { current: 1, size: 10, purchaseNo: '', goodsName: '', supplier: '' },
      list: [], total: 0, loading: false,
      goodsList: [],
      supplierList: [],
      dialogVisible: false, detailVisible: false, form: {}, detail: {}
    }
  },
  created() {
    this.load()
    goodsListAll().then(res => { this.goodsList = res.data || [] }).catch(() => {})
    supplierListAll({ status: 1 }).then(res => { this.supplierList = res.data || [] }).catch(() => {})
  },
  methods: {
    onGoodsSelect(id) { const g = this.goodsList.find(x => x.id === id); if (g) this.form.goodsName = g.goodsName },
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, purchaseNo: '', goodsName: '', supplier: '' }; this.load() },
    openEdit(row) {
      this.form = row ? { ...row } : {};
      if (!this.form.purchaseDate && !row) this.form.purchaseDate = new Date().toISOString().slice(0, 10);
      this.dialogVisible = true;
    },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() {
      if (!this.form.goodsName) { this.$message.warning('请填写商品名称'); return }
      if (!this.form.quantity || !this.form.price) { this.$message.warning('请填写数量和单价'); return }
      if (!this.form.purchaseDate) this.form.purchaseDate = new Date().toISOString().slice(0, 10);
      if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() });
      else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() });
    },
    handleDelete(row) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>.page { padding: 0; }</style>
