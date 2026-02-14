<template>
  <div class="page">
    <el-card>
      <h3 style="margin-top:0;margin-bottom:12px">充值流水</h3>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="订单号"><el-input v-model="query.orderNo" placeholder="订单号" clearable /></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="query.memberName" placeholder="会员姓名" clearable /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="query.phone" placeholder="手机号" clearable /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="query.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="开始时间" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="query.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openAdd">新增充值</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="memberName" label="会员姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="amount" label="充值金额" />
        <el-table-column prop="payAmount" label="实付金额" /><el-table-column prop="payMethod" label="支付方式" width="90" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" :width="actionColumnWidth"><template slot-scope="scope"><table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }]" /></template></el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog title="新增充值" :visible.sync="addVisible" width="500px">
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-form-item label="会员" prop="memberId">
          <el-select v-model="addForm.memberId" filterable placeholder="请选择会员" style="width:100%" @change="onMemberChange">
            <el-option v-for="m in memberList" :key="m.id" :label="m.name + ' ' + m.phone" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="充值金额" prop="amount"><el-input-number v-model="addForm.amount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="实付金额" prop="payAmount"><el-input-number v-model="addForm.payAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="addForm.payMethod" placeholder="默认现金" clearable style="width:100%">
            <el-option label="现金" value="现金" /><el-option label="微信" value="微信" /><el-option label="支付宝" value="支付宝" /><el-option label="转账" value="转账" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model="addForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="addVisible = false">取消</el-button><el-button type="primary" @click="submitAdd">确定</el-button></span>
    </el-dialog>
    <el-dialog title="充值详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="会员姓名">{{ detail.memberName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="充值金额">{{ detail.amount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">{{ detail.payAmount }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ detail.payMethod || '现金' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/recharge'
import { pageList as memberPageList } from '@/api/member'

export default {
  name: 'RechargeList',
  data() {
    return {
      query: { current: 1, size: 10, orderNo: '', memberName: '', phone: '', startTime: '', endTime: '' },
      list: [], total: 0, loading: false,
      addVisible: false, detailVisible: false,
      addForm: { memberId: null, amount: 0, payAmount: 0, payMethod: '现金', remark: '' },
      addRules: { amount: [{ required: true, message: '请输入充值金额', trigger: 'blur' }], payAmount: [{ required: true, message: '请输入实付金额', trigger: 'blur' }] },
      memberList: [], detail: {}
    }
  },
  created() { this.load(); memberPageList({ current: 1, size: 500 }).then(res => { this.memberList = res.data.records || [] }) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, orderNo: '', memberName: '', phone: '', startTime: '', endTime: '' }; this.load() },
    openAdd() { this.addForm = { memberId: null, amount: 0, payAmount: 0, payMethod: '现金', remark: '' }; this.addVisible = true },
    onMemberChange() {},
    submitAdd() {
      this.$refs.addForm.validate(valid => { if (!valid) return; api.add(this.addForm).then(() => { this.$message.success('新增成功'); this.addVisible = false; this.load() }) })
    },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
