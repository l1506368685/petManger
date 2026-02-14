<template>
  <div class="finance-report">
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="onTabClick">
        <el-tab-pane label="日报" name="daily">
          <div class="report-toolbar">
            <el-date-picker v-model="dailyDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" size="small" />
            <el-button type="primary" size="small" @click="loadDaily">查询</el-button>
          </div>
          <div v-if="dailyData.date" class="report-content">
            <el-row :gutter="16" class="summary-cards">
              <el-col :span="6"><div class="card"><div class="value">{{ dailyData.totalIncome }}</div><div class="label">当日收入（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ dailyData.totalExpense }}</div><div class="label">当日支出（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ dailyData.grossProfit }}</div><div class="label">毛利（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ dailyData.transactionCount }}</div><div class="label">交易笔数</div></div></el-col>
            </el-row>
            <h4>收入按支付方式</h4>
            <el-table :data="incomeByPayMethodRows" border size="small" style="max-width:400px">
              <el-table-column prop="payMethod" label="支付方式" />
              <el-table-column prop="amount" label="金额（元）" />
            </el-table>
            <h4 style="margin-top:16px">支出明细</h4>
            <el-table :data="dailyExpenseRows" border size="small" style="max-width:400px">
              <el-table-column prop="name" label="项目" />
              <el-table-column prop="amount" label="金额（元）" />
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="月报" name="monthly">
          <div class="report-toolbar">
            <el-date-picker v-model="monthlyYearMonth" type="month" value-format="yyyy-MM" placeholder="选择月份" size="small" />
            <el-button type="primary" size="small" @click="loadMonthly">查询</el-button>
          </div>
          <div v-if="monthlyData.year" class="report-content">
            <el-row :gutter="16" class="summary-cards">
              <el-col :span="6"><div class="card"><div class="value">{{ monthlyData.totalIncome }}</div><div class="label">当月收入（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ monthlyData.totalExpense }}</div><div class="label">当月支出（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ monthlyData.grossProfit }}</div><div class="label">毛利（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ monthlyData.grossMarginRate }}%</div><div class="label">毛利率</div></div></el-col>
            </el-row>
            <h4>收入构成</h4>
            <el-table :data="incomeBySubjectRows" border size="small" style="max-width:400px">
              <el-table-column prop="subject" label="科目" />
              <el-table-column prop="amount" label="金额（元）" />
            </el-table>
            <h4 style="margin-top:16px">同比/环比</h4>
            <el-table :data="monthlyCompareRows" border size="small" style="max-width:500px">
              <el-table-column prop="name" label="对比项" />
              <el-table-column prop="value" label="数值" />
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="季报" name="quarterly">
          <div class="report-toolbar report-toolbar-quarterly">
            <span class="toolbar-label">年份</span>
            <el-input-number v-model="quarterlyYear" :min="2020" :max="2030" size="small" placeholder="年份" controls-position="right" class="quarterly-year-input" />
            <span class="toolbar-label">季度</span>
            <el-select v-model="quarterlyQuarter" placeholder="请选择季度" size="small" class="quarterly-quarter-select">
              <el-option label="Q1（1-3月）" :value="1" /><el-option label="Q2（4-6月）" :value="2" />
              <el-option label="Q3（7-9月）" :value="3" /><el-option label="Q4（10-12月）" :value="4" />
            </el-select>
            <el-button type="primary" size="small" @click="loadQuarterly">查询</el-button>
          </div>
          <div v-if="quarterlyData.label" class="report-content">
            <el-row :gutter="16" class="summary-cards">
              <el-col :span="8"><div class="card"><div class="value">{{ quarterlyData.totalIncome }}</div><div class="label">本季收入（元）</div></div></el-col>
              <el-col :span="8"><div class="card"><div class="value">{{ quarterlyData.totalExpense }}</div><div class="label">本季支出（元）</div></div></el-col>
              <el-col :span="8"><div class="card"><div class="value">{{ quarterlyData.grossProfit }}</div><div class="label">毛利（元）</div></div></el-col>
            </el-row>
            <div ref="quarterlyChart" class="chart" style="height:280px" />
            <h4 style="margin-top:16px">商品销售 TOP10</h4>
            <el-table :data="quarterlyTopGoodsRows" border size="small" empty-text="该季度暂无订单商品数据">
              <el-table-column type="index" label="排名" width="60" />
              <el-table-column prop="goodsName" label="商品名称" min-width="120" show-overflow-tooltip />
              <el-table-column prop="amountText" label="销售额（元）" width="130" />
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="年报" name="yearly">
          <div class="report-toolbar">
            <el-input-number v-model="yearlyYear" :min="2020" :max="2030" size="small" placeholder="年" style="width:120px" />
            <el-button type="primary" size="small" @click="loadYearly">查询</el-button>
          </div>
          <div v-if="yearlyData.label" class="report-content">
            <el-row :gutter="16" class="summary-cards">
              <el-col :span="8"><div class="card"><div class="value">{{ yearlyData.totalIncome }}</div><div class="label">年度收入（元）</div></div></el-col>
              <el-col :span="8"><div class="card"><div class="value">{{ yearlyData.totalExpense }}</div><div class="label">年度支出（元）</div></div></el-col>
              <el-col :span="8"><div class="card"><div class="value">{{ yearlyData.grossProfit }}</div><div class="label">毛利（元）</div></div></el-col>
            </el-row>
            <div ref="yearlyChart" class="chart" style="height:280px" />
            <h4 style="margin-top:16px">商品销售 TOP10</h4>
            <el-table :data="yearlyTopGoodsRows" border size="small" empty-text="该年度暂无订单商品数据">
              <el-table-column type="index" label="排名" width="60" />
              <el-table-column prop="goodsName" label="商品名称" min-width="120" show-overflow-tooltip />
              <el-table-column prop="amountText" label="销售额（元）" width="130" />
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="自定义" name="custom">
          <div class="report-toolbar">
            <el-date-picker v-model="customRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" size="small" />
            <el-button type="primary" size="small" @click="loadCustom">查询</el-button>
          </div>
          <div v-if="customData.startDate" class="report-content">
            <el-row :gutter="16" class="summary-cards">
              <el-col :span="6"><div class="card"><div class="value">{{ customData.totalIncome }}</div><div class="label">收入（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ customData.totalExpense }}</div><div class="label">支出（元）</div></div></el-col>
              <el-col :span="6"><div class="card"><div class="value">{{ customData.grossProfit }}</div><div class="label">毛利（元）</div></div></el-col>
              <el-col :span="6"><div class="card" v-if="customData.incomeChangeRatio != null"><div class="value">{{ customData.incomeChangeRatio }}%</div><div class="label">较对比期收入变化</div></div></el-col>
            </el-row>
            <div ref="customChart" class="chart" style="height:280px" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import * as financeApi from '@/api/finance'
import * as echarts from 'echarts'

export default {
  name: 'FinanceReport',
  data() {
    const today = new Date()
    const y = today.getFullYear()
    const m = String(today.getMonth() + 1).padStart(2, '0')
    const d = String(today.getDate()).padStart(2, '0')
    return {
      activeTab: 'daily',
      dailyDate: `${y}-${m}-${d}`,
      dailyData: {},
      monthlyYearMonth: `${y}-${m}`,
      monthlyData: {},
      quarterlyYear: y,
      quarterlyQuarter: Math.ceil((today.getMonth() + 1) / 3),
      quarterlyData: {},
      yearlyYear: y,
      yearlyData: {},
      customRange: null,
      customData: {},
      chartInstances: {}
    }
  },
  computed: {
    incomeByPayMethodRows() {
      const m = this.dailyData.incomeByPayMethod || {}
      return Object.keys(m).map(k => ({ payMethod: k, amount: m[k] }))
    },
    dailyExpenseRows() {
      const d = this.dailyData
      if (!d.totalExpense && d.totalExpense !== 0) return []
      return [
        { name: '采购支出', amount: d.purchaseExpense },
        { name: '索赔支出', amount: d.claimExpense }
      ]
    },
    incomeBySubjectRows() {
      const m = this.monthlyData.incomeBySubject || {}
      return Object.keys(m).map(k => ({ subject: k, amount: m[k] }))
    },
    monthlyCompareRows() {
      const d = this.monthlyData
      const rows = []
      if (d.incomeLastMonth != null) rows.push({ name: '上月收入（元）', value: d.incomeLastMonth })
      if (d.chainRatio != null) rows.push({ name: '环比（%）', value: d.chainRatio })
      if (d.incomeLastYearSameMonth != null) rows.push({ name: '去年同月收入（元）', value: d.incomeLastYearSameMonth })
      if (d.yearOnYearRatio != null) rows.push({ name: '同比（%）', value: d.yearOnYearRatio })
      return rows
    },
    quarterlyTopGoodsRows() {
      const list = this.quarterlyData.topGoods || []
      return list.map(item => ({
        ...item,
        goodsName: item.goodsName != null ? String(item.goodsName) : '',
        amountText: this.formatAmount(item.amount)
      }))
    },
    yearlyTopGoodsRows() {
      const list = this.yearlyData.topGoods || []
      return list.map(item => ({
        ...item,
        goodsName: item.goodsName != null ? String(item.goodsName) : '',
        amountText: this.formatAmount(item.amount)
      }))
    }
  },
  methods: {
    formatAmount(val) {
      if (val == null) return '0.00'
      const n = Number(val)
      return isNaN(n) ? String(val) : n.toFixed(2)
    },
    onTabClick() {
      this.$nextTick(() => {
        if (this.activeTab === 'daily') this.loadDaily()
        else if (this.activeTab === 'monthly') this.loadMonthly()
        else if (this.activeTab === 'quarterly') this.loadQuarterly()
        else if (this.activeTab === 'yearly') this.loadYearly()
        else if (this.activeTab === 'custom' && this.customRange && this.customRange.length === 2) this.loadCustom()
      })
    },
    loadDaily() {
      if (!this.dailyDate) return
      financeApi.getDailyReport(this.dailyDate).then(res => {
        this.dailyData = res.data || {}
      })
    },
    loadMonthly() {
      if (!this.monthlyYearMonth) return
      const [year, month] = this.monthlyYearMonth.split('-').map(Number)
      financeApi.getMonthlyReport(year, month).then(res => {
        this.monthlyData = res.data || {}
      })
    },
    loadQuarterly() {
      financeApi.getQuarterlyReport(this.quarterlyYear, this.quarterlyQuarter).then(res => {
        this.quarterlyData = res.data || {}
        this.$nextTick(() => this.renderTrendChart('quarterlyChart', this.quarterlyData.incomeTrendByDay))
      })
    },
    loadYearly() {
      financeApi.getYearlyReport(this.yearlyYear).then(res => {
        this.yearlyData = res.data || {}
        this.$nextTick(() => this.renderTrendChart('yearlyChart', this.yearlyData.incomeTrendByDay))
      })
    },
    loadCustom() {
      if (!this.customRange || this.customRange.length !== 2) {
        this.$message.warning('请选择日期范围')
        return
      }
      financeApi.getCustomReport(this.customRange[0], this.customRange[1]).then(res => {
        this.customData = res.data || {}
        this.$nextTick(() => this.renderTrendChart('customChart', this.customData.incomeTrendByDay))
      })
    },
    renderTrendChart(refName, trendData) {
      if (!trendData || !trendData.length) return
      const el = this.$refs[refName]
      if (!el) return
      if (this.chartInstances[refName]) this.chartInstances[refName].dispose()
      const chart = echarts.init(el)
      this.chartInstances[refName] = chart
      const dates = trendData.map(t => t.reportDate)
      const amounts = trendData.map(t => Number(t.amount))
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: '收入（元）' },
        series: [{ name: '日收入', type: 'line', data: amounts, smooth: true }]
      })
    }
  },
  mounted() {
    this.loadDaily()
  },
  beforeDestroy() {
    Object.values(this.chartInstances).forEach(c => c && c.dispose())
  }
}
</script>

<style scoped>
.finance-report { padding: 0; }
.report-toolbar { margin-bottom: 16px; display: flex; align-items: center; gap: 12px; }
.report-toolbar-quarterly { flex-wrap: wrap; }
.report-toolbar-quarterly .toolbar-label { font-size: 14px; color: #606266; margin-right: 6px; min-width: 36px; }
.report-toolbar-quarterly .quarterly-year-input { width: 140px; }
.report-toolbar-quarterly .quarterly-quarter-select { width: 180px; min-width: 180px; }
.summary-cards { margin-bottom: 20px; }
.summary-cards .card { background: #f5f7fa; padding: 16px; border-radius: 4px; text-align: center; }
.summary-cards .value { font-size: 22px; font-weight: bold; color: #303133; }
.summary-cards .label { font-size: 12px; color: #909399; margin-top: 4px; }
.report-content h4 { margin: 0 0 8px 0; font-size: 14px; color: #606266; }
.chart { width: 100%; min-height: 200px; }
</style>
