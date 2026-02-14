<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="订单号"><el-input v-model="query.orderNo" placeholder="订单号" clearable /></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="query.memberName" placeholder="会员姓名" clearable /></el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="query.status" placeholder="全部" clearable><el-option label="待付款" value="待付款" /><el-option label="已付款" value="已付款" /><el-option label="已发货" value="已发货" /><el-option label="已完成" value="已完成" /></el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openAdd">新增订单</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="memberName" label="会员姓名" />
        <el-table-column prop="petName" label="宠物名称" />
        <el-table-column prop="totalQuantity" label="数量" width="80" align="center" />
        <el-table-column prop="totalAmount" label="订单金额" width="100" />
        <el-table-column prop="orderTime" label="下单时间" width="160" />
        <el-table-column prop="status" label="订单状态" width="100" />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '修改状态', handler: openStatus }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog title="订单详情" :visible.sync="detailVisible" width="600px">
      <div v-if="orderDetail.order">
        <p><strong>订单号：</strong>{{ orderDetail.order.orderNo }}</p>
        <p><strong>会员：</strong>{{ orderDetail.order.memberName }}</p>
        <p><strong>总金额：</strong>{{ orderDetail.order.totalAmount }}</p>
        <p><strong>状态：</strong>{{ orderDetail.order.status }}</p>
        <el-table :data="orderDetail.items || []" border size="small" style="margin-top:12px">
          <el-table-column prop="goodsName" label="商品" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="price" label="单价" width="80" />
          <el-table-column prop="amount" label="小计" width="80" />
        </el-table>
      </div>
    </el-dialog>
    <el-dialog :title="(orderForm.order && orderForm.order.id) ? '编辑订单' : '新增订单'" :visible.sync="orderDialogVisible" width="700px">
      <el-form label-width="100px">
        <el-form-item label="支付方式">
          <el-select v-model="orderForm.order.payMethod" placeholder="默认现金" clearable style="width:100%">
            <el-option label="现金" value="现金" /><el-option label="微信" value="微信" /><el-option label="支付宝" value="支付宝" /><el-option label="转账" value="转账" />
          </el-select>
        </el-form-item>
        <el-form-item label="会员">
          <el-select v-model="orderForm.order.memberId" filterable placeholder="请选择会员" style="width:100%" @change="onMemberChange">
            <el-option v-for="m in memberList" :key="m.id" :label="m.name + ' ' + m.phone" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单明细">
          <el-button type="primary" size="mini" @click="addOrderItem">添加商品</el-button>
          <el-table :data="orderForm.items" border size="small" style="margin-top:8px">
            <el-table-column label="商品" width="200">
              <template slot-scope="scope">
                <el-select v-model="scope.row.goodsId" filterable placeholder="选择商品" style="width:100%" @change="v => onGoodsChange(scope.row, v)">
                  <el-option v-for="g in goodsList" :key="g.id" :label="g.goodsName" :value="g.id" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="数量" width="100">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.quantity" :min="1" size="mini" @change="calcItemAmount(scope.row)" />
              </template>
            </el-table-column>
            <el-table-column label="单价" width="100">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.price" :min="0" :precision="2" size="mini" @change="calcItemAmount(scope.row)" />
              </template>
            </el-table-column>
            <el-table-column label="小计" width="90">
              <template slot-scope="scope">{{ scope.row.amount }}</template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope"><el-button type="text" size="small" @click="removeOrderItem(scope.$index)">删除</el-button></template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="总金额"><strong>{{ totalAmount }}</strong></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="orderDialogVisible = false">取消</el-button><el-button type="primary" @click="submitOrder">确定</el-button></span>
    </el-dialog>
    <el-dialog title="修改订单状态" :visible.sync="statusVisible" width="400px">
      <el-form label-width="100px">
        <el-form-item label="当前状态">{{ statusRow.status }}</el-form-item>
        <el-form-item label="新状态">
          <el-select v-model="newStatus" placeholder="请选择" style="width:100%">
            <el-option label="待付款" value="待付款" /><el-option label="已付款" value="已付款" /><el-option label="已发货" value="已发货" /><el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="statusVisible = false">取消</el-button><el-button type="primary" @click="submitStatus">确定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/order'
import { pageList as memberPageList } from '@/api/member'
import { listAll as goodsListAll } from '@/api/goods'

export default {
  name: 'OrderList',
  data() {
    return {
      query: { current: 1, size: 10, orderNo: '', memberName: '', status: '' },
      list: [], total: 0, loading: false,
      detailVisible: false, statusVisible: false, orderDialogVisible: false,
      orderDetail: {}, statusRow: {}, newStatus: '',
      memberList: [], goodsList: [],
      orderForm: { order: { memberId: null, memberName: '', status: '待付款', payMethod: '现金' }, items: [] }
    }
  },
  computed: {
    totalAmount() {
      const items = this.orderForm.items || []
      return items.reduce((sum, it) => sum + (it.amount || 0), 0).toFixed(2)
    }
  },
  created() { this.load(); memberPageList({ current: 1, size: 500 }).then(res => { this.memberList = res.data.records || [] }); goodsListAll().then(res => { this.goodsList = res.data || [] }) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, orderNo: '', memberName: '', status: '' }; this.load() },
    openAdd() { this.orderForm = { order: { memberId: null, memberName: '', status: '待付款', payMethod: '现金' }, items: [{ goodsId: null, goodsName: '', quantity: 1, price: 0, amount: 0 }] }; this.orderDialogVisible = true },
    openEdit(row) {
      api.detail(row.id).then(res => {
        const o = res.data.order || {}
        const items = (res.data.items || []).map(it => ({ ...it }))
        this.orderForm = { order: { ...o }, items: items.length ? items : [{ goodsId: null, goodsName: '', quantity: 1, price: 0, amount: 0 }] }
        this.orderDialogVisible = true
      })
    },
    onMemberChange(id) { const m = this.memberList.find(x => x.id === id); if (m) this.orderForm.order.memberName = m.name },
    addOrderItem() { this.orderForm.items.push({ goodsId: null, goodsName: '', quantity: 1, price: 0, amount: 0 }) },
    removeOrderItem(i) { this.orderForm.items.splice(i, 1) },
    onGoodsChange(row, goodsId) { const g = this.goodsList.find(x => x.id === goodsId); if (g) { row.goodsName = g.goodsName; row.price = g.price || 0; this.calcItemAmount(row) } },
    calcItemAmount(row) { row.amount = (row.quantity || 0) * (row.price || 0) },
    submitOrder() {
      const items = this.orderForm.items.filter(it => it.goodsId && it.quantity > 0)
      if (!items.length) { this.$message.warning('请至少添加一条商品明细'); return }
      if (this.orderForm.order.id) api.update(this.orderForm).then(() => { this.$message.success('修改成功'); this.orderDialogVisible = false; this.load() })
      else api.add(this.orderForm).then(() => { this.$message.success('新增成功'); this.orderDialogVisible = false; this.load() })
    },
    openDetail(row) { api.detail(row.id).then(res => { this.orderDetail = res.data; this.detailVisible = true }) },
    openStatus(row) { this.statusRow = row; this.newStatus = row.status; this.statusVisible = true },
    submitStatus() { api.updateStatus(this.statusRow.id, this.newStatus).then(() => { this.$message.success('修改成功'); this.statusVisible = false; this.load() }) },
    handleDelete(row) { this.$confirm('确定删除该订单？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
