<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="盘点单号"><el-input v-model="query.checkNo" clearable /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="草稿" value="草稿" /><el-option label="已确认" value="已确认" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openAdd">新建盘点单</el-button>
      <el-table v-loading="loading" :data="list" border size="small" style="margin-top:12px">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="checkNo" label="盘点单号" width="160" />
        <el-table-column prop="checkDate" label="盘点日期" width="120" />
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已确认' ? 'success' : 'info'" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="getStockCheckActions(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog title="新建盘点单" :visible.sync="addVisible" width="400px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="盘点日期"><el-date-picker v-model="addForm.checkDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="addForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <p style="color:#909399;font-size:12px">新建后将按当前各商品账面库存生成盘点明细，请到「录入实盘」中填写实盘数量后确认。</p>
      <span slot="footer"><el-button @click="addVisible = false">取消</el-button><el-button type="primary" @click="submitAdd">确定</el-button></span>
    </el-dialog>
    <el-dialog title="盘点详情" :visible.sync="detailVisible" width="90%" max-width="800px">
      <div v-if="checkDetail.main">
        <p><strong>盘点单号：</strong>{{ checkDetail.main.checkNo }} &nbsp; <strong>状态：</strong>{{ checkDetail.main.status }} &nbsp; <strong>盘点日期：</strong>{{ checkDetail.main.checkDate }}</p>
        <el-table :data="checkDetail.items || []" border size="small" style="margin-top:12px">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="goodsName" label="商品名称" />
          <el-table-column prop="goodsCode" label="商品编码" width="120" />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
          <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="right" />
          <el-table-column prop="diffQuantity" label="盈亏" width="90" align="right">
            <template slot-scope="scope">
              <span :style="{ color: (scope.row.diffQuantity || 0) > 0 ? '#67c23a' : (scope.row.diffQuantity || 0) < 0 ? '#f56c6c' : '' }">{{ scope.row.diffQuantity != null ? (scope.row.diffQuantity > 0 ? '+' + scope.row.diffQuantity : scope.row.diffQuantity) : '-' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
    <el-dialog :title="editForm.main ? '录入实盘 - ' + editForm.main.checkNo : '录入实盘'" :visible.sync="editVisible" width="90%" max-width="800px">
      <div v-if="editForm.main">
        <el-table :data="editForm.items || []" border size="small">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="goodsName" label="商品名称" />
          <el-table-column prop="goodsCode" label="商品编码" width="120" />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
          <el-table-column label="实盘数量" width="140">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.actualQuantity" :min="0" size="mini" style="width:110px" @change="calcDiff(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="diffQuantity" label="盈亏" width="90" align="right">
            <template slot-scope="scope">{{ scope.row.diffQuantity != null ? (scope.row.diffQuantity > 0 ? '+' + scope.row.diffQuantity : scope.row.diffQuantity) : '' }}</template>
          </el-table-column>
        </el-table>
        <span slot="footer"><el-button @click="editVisible = false">取消</el-button><el-button type="primary" @click="submitEdit">保存</el-button></span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/stockCheck'

export default {
  name: 'StockCheckList',
  data() {
    return {
      query: { current: 1, size: 10, checkNo: '', status: '' },
      list: [], total: 0, loading: false,
      addVisible: false, detailVisible: false, editVisible: false,
      addForm: { checkDate: new Date().toISOString().slice(0, 10), remark: '' },
      checkDetail: {},
      editForm: { main: null, items: [] }
    }
  },
  created() { this.load() },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, checkNo: '', status: '' }; this.load() },
    openAdd() { this.addForm = { checkDate: new Date().toISOString().slice(0, 10), remark: '' }; this.addVisible = true },
    submitAdd() {
      api.add(this.addForm).then(() => { this.$message.success('新建成功'); this.addVisible = false; this.load() })
    },
    openDetail(row) { api.detail(row.id).then(res => { this.checkDetail = res.data; this.detailVisible = true }) },
    openEdit(row) {
      api.detail(row.id).then(res => {
        this.editForm = { main: res.data.main, items: (res.data.items || []).map(it => ({ ...it })) }
        this.editForm.items.forEach(this.calcDiff)
        this.editVisible = true
      })
    },
    calcDiff(row) { row.diffQuantity = (row.actualQuantity != null ? row.actualQuantity : row.bookQuantity) - (row.bookQuantity || 0) },
    submitEdit() {
      api.update({ main: this.editForm.main, items: this.editForm.items }).then(() => { this.$message.success('保存成功'); this.editVisible = false; this.load() })
    },
    handleConfirm(row) {
      this.$confirm('确认后将按实盘数量调整库存并生成流水，是否继续？', '确认盘点', { type: 'warning' })
        .then(() => api.confirm(row.id).then(() => { this.$message.success('确认成功'); this.load() }))
        .catch(() => {})
    },
    handleDelete(row) {
      this.$confirm('确定删除该盘点单？', '提示', { type: 'warning' })
        .then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() }))
        .catch(() => {})
    },
    getStockCheckActions(row) {
      const actions = [{ label: '详情', handler: this.openDetail }]
      if (row.status === '草稿') {
        actions.push({ label: '录入实盘', handler: this.openEdit }, { label: '确认盘点', handler: this.handleConfirm }, { label: '删除', handler: this.handleDelete, danger: true })
      }
      return actions
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
