<template>
  <div class="page">
    <el-card>
      <h3 style="margin-top:0;margin-bottom:12px">卡交易流水</h3>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="流水类型">
          <el-select v-model="query.transType" placeholder="全部" clearable style="width:110px">
            <el-option label="购卡/充值" value="recharge" />
            <el-option label="消费" value="consume" />
            <el-option label="赠送" value="gift" />
            <el-option label="过期" value="expire" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="query.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="开始时间" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="query.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="memberName" label="会员" width="100" />
        <el-table-column prop="transType" label="类型" width="90">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.transType === 'recharge'" type="success" size="small">充值</el-tag>
            <el-tag v-else-if="scope.row.transType === 'consume'" type="warning" size="small">消费</el-tag>
            <el-tag v-else size="small">{{ scope.row.transType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100" />
        <el-table-column label="次数变动" width="90">
          <template slot-scope="scope">{{ scope.row.timesChange != null && scope.row.timesChange !== 0 ? scope.row.timesChange : '-' }}</template>
        </el-table-column>
        <el-table-column prop="balanceAfter" label="变动后余额" width="100" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="bizOrderNo" label="业务单号" width="140" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" :width="actionColumnWidth"><template slot-scope="scope"><table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }]" /></template></el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog title="流水详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="会员">{{ detail.memberName }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ detail.transType }}</el-descriptions-item>
        <el-descriptions-item label="金额">{{ detail.amount }}</el-descriptions-item>
        <el-descriptions-item label="次数变动">{{ detail.timesChange }}</el-descriptions-item>
        <el-descriptions-item label="变动后余额">{{ detail.balanceAfter }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
        <el-descriptions-item label="业务单号">{{ detail.bizOrderNo }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/cardTransaction'

export default {
  name: 'CardTransactionList',
  data() {
    return {
      query: { current: 1, size: 10, transType: '', startTime: '', endTime: '' },
      list: [], total: 0, loading: false,
      detailVisible: false, detail: {}
    }
  },
  created() { this.load() },
  methods: {
    load() {
      this.loading = true
      api.pageList(this.query).then(res => {
        this.list = res.data.records || []
        this.total = res.data.total || 0
      }).finally(() => { this.loading = false })
    },
    resetQuery() { this.query = { current: 1, size: 10, transType: '', startTime: '', endTime: '' }; this.load() },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
