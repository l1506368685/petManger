<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="商品名称"><el-input v-model="query.goodsName" placeholder="商品名称" clearable /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="query.flowType" placeholder="全部" clearable style="width:100px">
            <el-option label="入库" value="入库" /><el-option label="出库" value="出库" /><el-option label="退库" value="退库" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="query.bizType" placeholder="全部" clearable style="width:120px">
            <el-option v-for="c in bizTypeOptions" :key="c.itemValue" :label="c.itemLabel" :value="c.itemValue" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="list" border size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="flowNo" label="流水号" width="160" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="flowType" label="类型" width="80" />
        <el-table-column prop="bizType" label="业务类型" width="100" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="afterQuantity" label="变更后库存" width="100" />
        <el-table-column prop="refNo" label="关联单号" width="140" />
        <el-table-column prop="flowTime" label="业务时间" width="160" />
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
  </div>
</template>

<script>
import { flowPageList } from '@/api/inventory'
import { items as dictItems } from '@/api/dict'

export default {
  name: 'InventoryFlowList',
  data() {
    return {
      query: { current: 1, size: 10, goodsName: '', flowType: '', bizType: '' },
      list: [], total: 0, loading: false,
      bizTypeOptions: []
    }
  },
  created() {
    this.load()
    dictItems('inventory_biz_type').then(res => { this.bizTypeOptions = res.data || [] }).catch(() => {})
  },
  methods: {
    load() { this.loading = true; flowPageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, goodsName: '', flowType: '', bizType: '' }; this.load() }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
