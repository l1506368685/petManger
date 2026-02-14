<template>
  <div class="page">
    <el-card>
      <h3 style="margin-top:0;margin-bottom:12px">会员储值卡/套餐卡</h3>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="卡号"><el-input v-model="query.cardNo" placeholder="卡号" clearable /></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="query.memberName" placeholder="会员姓名" clearable /></el-form-item>
        <el-form-item label="卡种">
          <el-select v-model="query.cardKind" placeholder="全部" clearable style="width:100px">
            <el-option label="储值卡" :value="1" />
            <el-option label="次卡" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:90px">
            <el-option label="正常" :value="1" />
            <el-option label="已过期" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openBuy">购卡/开卡</el-button>
      <el-button size="small" @click="openRecharge" :disabled="!selectedRow || selectedRow.cardKind !== 1">储值充值</el-button>
      <el-button size="small" @click="openConsume">消费扣款</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small" @current-change="selectedRow = $event">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="cardNo" label="卡号" width="180" />
        <el-table-column prop="memberName" label="会员" width="100" />
        <el-table-column prop="cardTypeName" label="卡类型" width="120" />
        <el-table-column prop="cardKind" label="卡种" width="70">
          <template slot-scope="scope">{{ scope.row.cardKind === 2 ? '次卡' : '储值卡' }}</template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="90" />
        <el-table-column prop="remainTimes" label="剩余次数" width="90" v-if="false" />
        <el-table-column label="剩余次数/余额" width="110">
          <template slot-scope="scope">{{ scope.row.cardKind === 2 ? (scope.row.remainTimes + '次') : ('¥' + scope.row.balance) }}</template>
        </el-table-column>
        <el-table-column prop="expireTime" label="到期时间" width="160" />
        <el-table-column prop="status" label="状态" width="70">
          <template slot-scope="scope"><el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">{{ scope.row.status === 1 ? '正常' : '已过期' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog title="购卡/开卡" :visible.sync="buyVisible" width="480px">
      <el-form ref="buyForm" :model="buyForm" :rules="buyRules" label-width="100px">
        <el-form-item label="会员" prop="memberId">
          <el-select v-model="buyForm.memberId" filterable placeholder="请选择会员" style="width:100%">
            <el-option v-for="m in memberList" :key="m.id" :label="m.name + ' ' + m.phone" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="卡类型" prop="cardTypeId">
          <el-select v-model="buyForm.cardTypeId" placeholder="请选择卡类型" style="width:100%" @change="onCardTypeChange">
            <el-option v-for="c in cardTypeList" :key="c.id" :label="c.typeName + ' ¥' + c.price + (c.cardKind === 2 ? ' (' + c.totalTimes + '次)' : '')" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="实付金额" prop="payAmount"><el-input-number v-model="buyForm.payAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="buyForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="buyVisible = false">取消</el-button><el-button type="primary" @click="submitBuy">确定</el-button></span>
    </el-dialog>
    <el-dialog title="储值充值" :visible.sync="rechargeVisible" width="440px">
      <el-form ref="rechargeForm" :model="rechargeForm" :rules="rechargeRules" label-width="100px">
        <el-form-item label="当前卡">{{ selectedRow ? selectedRow.cardNo + ' 余额¥' + selectedRow.balance : '' }}</el-form-item>
        <el-form-item label="充值金额" prop="amount"><el-input-number v-model="rechargeForm.amount" :min="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="实付金额" prop="payAmount"><el-input-number v-model="rechargeForm.payAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="rechargeForm.remark" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="rechargeVisible = false">取消</el-button><el-button type="primary" @click="submitRecharge">确定</el-button></span>
    </el-dialog>
    <el-dialog title="消费扣款" :visible.sync="consumeVisible" width="480px">
      <el-form ref="consumeForm" :model="consumeForm" :rules="consumeRules" label-width="100px">
        <el-form-item label="会员" prop="memberId">
          <el-select v-model="consumeForm.memberId" filterable placeholder="请选择会员" style="width:100%">
            <el-option v-for="m in memberList" :key="m.id" :label="m.name + ' ' + m.phone" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="消费金额" prop="amount"><el-input-number v-model="consumeForm.amount" :min="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="使用次卡次数"><el-input-number v-model="consumeForm.useTimes" :min="0" style="width:100%" placeholder="0表示系统自动优先扣次卡" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="consumeForm.remark" /></el-form-item>
        <el-form-item label="业务单号"><el-input v-model="consumeForm.bizOrderNo" placeholder="可选" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="consumeVisible = false">取消</el-button><el-button type="primary" @click="submitConsume">确定扣款</el-button></span>
    </el-dialog>
    <el-dialog title="会员卡详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="卡号">{{ detail.cardNo }}</el-descriptions-item>
        <el-descriptions-item label="会员">{{ detail.memberName }}</el-descriptions-item>
        <el-descriptions-item label="卡类型">{{ detail.cardTypeName }}</el-descriptions-item>
        <el-descriptions-item label="卡种">{{ detail.cardKind === 2 ? '次卡' : '储值卡' }}</el-descriptions-item>
        <el-descriptions-item label="余额">{{ detail.balance }}</el-descriptions-item>
        <el-descriptions-item label="剩余次数">{{ detail.remainTimes }}</el-descriptions-item>
        <el-descriptions-item label="到期时间">{{ detail.expireTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status === 1 ? '正常' : '已过期' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/memberCard'
import { pageList as memberPageList } from '@/api/member'
import { listAll as cardTypeListAll } from '@/api/cardType'

export default {
  name: 'MemberCardList',
  data() {
    return {
      query: { current: 1, size: 10, cardNo: '', memberName: '', cardKind: null, status: null },
      list: [], total: 0, loading: false,
      selectedRow: null,
      buyVisible: false, rechargeVisible: false, consumeVisible: false, detailVisible: false,
      buyForm: { memberId: null, cardTypeId: null, payAmount: 0, remark: '' },
      buyRules: { memberId: [{ required: true, message: '请选择会员', trigger: 'change' }], cardTypeId: [{ required: true, message: '请选择卡类型', trigger: 'change' }] },
      rechargeForm: { amount: 0, payAmount: 0, remark: '' },
      rechargeRules: { amount: [{ required: true, message: '请输入充值金额', trigger: 'blur' }] },
      consumeForm: { memberId: null, amount: 0, useTimes: 0, remark: '', bizOrderNo: '' },
      consumeRules: { memberId: [{ required: true }], amount: [{ required: true, message: '请输入消费金额', trigger: 'blur' }] },
      memberList: [], cardTypeList: [], detail: {}
    }
  },
  created() {
    this.load()
    memberPageList({ current: 1, size: 500 }).then(res => { this.memberList = res.data.records || [] })
    cardTypeListAll({ status: 1 }).then(res => { this.cardTypeList = res.data || [] })
  },
  methods: {
    load() {
      this.loading = true
      api.pageList(this.query).then(res => {
        this.list = res.data.records || []
        this.total = res.data.total || 0
      }).finally(() => { this.loading = false })
    },
    resetQuery() { this.query = { current: 1, size: 10, cardNo: '', memberName: '', cardKind: null, status: null }; this.load() },
    onCardTypeChange(id) {
      const ct = this.cardTypeList.find(c => c.id === id)
      if (ct) this.buyForm.payAmount = ct.price
    },
    openBuy() { this.buyForm = { memberId: null, cardTypeId: null, payAmount: 0, remark: '' }; this.buyVisible = true },
    submitBuy() {
      this.$refs.buyForm.validate(valid => {
        if (!valid) return
        api.buy(this.buyForm).then(() => { this.$message.success('购卡成功'); this.buyVisible = false; this.load() })
      })
    },
    openRecharge() {
      if (!this.selectedRow) { this.$message.warning('请先选择一张储值卡'); return }
      this.rechargeForm = { amount: 0, payAmount: 0, remark: '' }; this.rechargeVisible = true
    },
    submitRecharge() {
      this.$refs.rechargeForm.validate(valid => {
        if (!valid) return
        api.recharge({ cardId: this.selectedRow.id, ...this.rechargeForm }).then(() => { this.$message.success('充值成功'); this.rechargeVisible = false; this.load() })
      })
    },
    openConsume() { this.consumeForm = { memberId: null, amount: 0, useTimes: 0, remark: '', bizOrderNo: '' }; this.consumeVisible = true },
    submitConsume() {
      this.$refs.consumeForm.validate(valid => {
        if (!valid) return
        api.consume(this.consumeForm).then(res => {
          const d = res.data
          this.$message.success('扣款完成：次卡扣' + (d.deductedByTimes || 0) + '次，储值扣¥' + (d.deductedByBalance || 0) + '，需现金¥' + (d.needCash || 0))
          this.consumeVisible = false
          this.load()
        })
      })
    },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    handleDelete(row) {
      this.$confirm('确定删除该会员卡？', '提示', { type: 'warning' }).then(() => {
        api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
