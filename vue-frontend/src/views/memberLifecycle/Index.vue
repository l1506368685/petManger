<template>
  <div class="page">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="等级规则" name="levelRule">
          <div class="toolbar">
            <el-button type="primary" size="small" @click="openLevelEdit()">新增等级规则</el-button>
            <el-button size="small" @click="runLevelCalc">立即执行等级计算</el-button>
          </div>
          <el-table v-loading="levelLoading" :data="levelList" border size="small" style="margin-top:12px">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="levelName" label="等级名称" />
            <el-table-column prop="minAmount" label="消费下限(元)" width="120" />
            <el-table-column prop="maxAmount" label="消费上限(元)" width="120" />
            <el-table-column prop="sortOrder" label="排序" width="80" />
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template>
            </el-table-column>
            <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
              <template slot-scope="scope">
                <table-action-cell :row="scope.row" :actions="[{ label: '编辑', handler: openLevelEdit }, { label: '删除', handler: handleDeleteLevel, danger: true }]" />
              </template>
            </el-table-column>
          </el-table>
          <p class="tip">说明：每月1日凌晨按近12个月订单消费总额自动升降级。消费金额落在区间[minAmount, maxAmount]内则对应该等级。</p>
        </el-tab-pane>

        <el-tab-pane label="流失与沉睡预警" name="warning">
          <div class="toolbar">
            <el-form :inline="true" :model="warningQuery" size="small">
              <el-form-item label="预警类型">
                <el-select v-model="warningQuery.warningType" placeholder="全部" clearable style="width:120px">
                  <el-option label="流失预警" value="CHURN" />
                  <el-option label="沉睡会员" value="SLEEPING" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="loadWarnings">搜索</el-button>
                <el-button @click="warningQuery.warningType = ''; loadWarnings()">重置</el-button>
              </el-form-item>
            </el-form>
            <el-button size="small" @click="runChurnScan">执行流失扫描</el-button>
            <el-button size="small" @click="runSleepingScan">执行沉睡扫描</el-button>
          </div>
          <el-table v-loading="warningLoading" :data="warningList" border size="small" style="margin-top:12px">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="memberName" label="会员姓名" />
            <el-table-column prop="phone" label="手机号" />
            <el-table-column prop="warningType" label="类型" width="100">
              <template slot-scope="scope">{{ scope.row.warningType === 'CHURN' ? '流失预警' : '沉睡' }}</template>
            </el-table-column>
            <el-table-column prop="lastConsumeTime" label="最后消费时间" width="160" />
            <el-table-column prop="registerDays" label="注册天数" width="90" />
            <el-table-column prop="createTime" label="生成时间" width="160" />
          </el-table>
          <el-pagination
            style="margin-top:12px"
            :current-page="warningQuery.current"
            :page-size="warningQuery.size"
            :total="warningTotal"
            layout="total, prev, pager, next"
            @current-change="p => { warningQuery.current = p; loadWarnings() }"
          />
          <p class="tip">流失：最后消费超过配置天数的会员；沉睡：注册超过配置天数且从未消费的会员。每日凌晨自动扫描，也可手动执行。</p>
        </el-tab-pane>

        <el-tab-pane label="生命周期配置" name="config">
          <el-table :data="configList" border size="small" style="max-width:600px">
            <el-table-column prop="configKey" label="配置键" width="200" />
            <el-table-column prop="remark" label="说明" />
            <el-table-column prop="configValue" label="值" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.configValue" size="small" style="width:100px" @change="saveConfig(scope.row)" />
              </template>
            </el-table-column>
          </el-table>
          <p class="tip">流失预警天数：最后消费超过该天数的会员进入流失预警；沉睡注册天数：注册超过该天数且从未消费的会员标记为沉睡。</p>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog :title="(levelForm.id ? '编辑' : '新增') + '等级规则'" :visible.sync="levelDialogVisible" width="500px" @close="levelForm = {}">
      <el-form ref="levelFormRef" :model="levelForm" label-width="120px">
        <el-form-item label="等级名称" prop="levelName"><el-input v-model="levelForm.levelName" placeholder="如：普通会员" /></el-form-item>
        <el-form-item label="消费金额下限(元)" prop="minAmount"><el-input-number v-model="levelForm.minAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="消费金额上限(元)" prop="maxAmount"><el-input-number v-model="levelForm.maxAmount" :min="0" :precision="2" style="width:100%" placeholder="999999表示无上限" /></el-form-item>
        <el-form-item label="排序" prop="sortOrder"><el-input-number v-model="levelForm.sortOrder" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="levelForm.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="levelDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitLevel">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/memberLifecycle'

export default {
  name: 'MemberLifecycle',
  data() {
    return {
      activeTab: 'levelRule',
      levelList: [],
      levelLoading: false,
      levelDialogVisible: false,
      levelForm: { levelName: '', minAmount: 0, maxAmount: 999999, sortOrder: 0, status: 1 },
      warningQuery: { current: 1, size: 10, warningType: '' },
      warningList: [],
      warningTotal: 0,
      warningLoading: false,
      configList: []
    }
  },
  watch: {
    activeTab(v) {
      if (v === 'levelRule') this.loadLevelList()
      if (v === 'warning') this.loadWarnings()
      if (v === 'config') this.loadConfig()
    }
  },
  created() {
    this.loadLevelList()
  },
  methods: {
    loadLevelList() {
      this.levelLoading = true
      api.levelRulePageList({ current: 1, size: 100 }).then(res => {
        const page = res.data
        this.levelList = (page && page.records) ? page.records : []
        this.levelLoading = false
      }).catch(() => { this.levelLoading = false })
    },
    openLevelEdit(row) {
      this.levelForm = row ? { ...row } : { levelName: '', minAmount: 0, maxAmount: 999999, sortOrder: 0, status: 1 }
      this.levelDialogVisible = true
    },
    submitLevel() {
      if (!this.levelForm.levelName) { this.$message.warning('请填写等级名称'); return }
      if (this.levelForm.id) {
        api.levelRuleUpdate(this.levelForm).then(() => { this.$message.success('修改成功'); this.levelDialogVisible = false; this.loadLevelList() })
      } else {
        api.levelRuleAdd(this.levelForm).then(() => { this.$message.success('新增成功'); this.levelDialogVisible = false; this.loadLevelList() })
      }
    },
    handleDeleteLevel(row) {
      this.$confirm('确定删除该等级规则？', '提示', { type: 'warning' }).then(() => {
        api.levelRuleRemove(row.id).then(() => { this.$message.success('删除成功'); this.loadLevelList() })
      }).catch(() => {})
    },
    runLevelCalc() {
      api.runLevelCalc().then(() => this.$message.success('等级计算已执行'))
    },
    loadWarnings() {
      this.warningLoading = true
      api.warningPageList(this.warningQuery).then(res => {
        const page = res.data
        this.warningList = (page && page.records) ? page.records : []
        this.warningTotal = (page && page.total) ? page.total : 0
        this.warningLoading = false
      }).catch(() => { this.warningLoading = false })
    },
    runChurnScan() {
      api.runChurnScan().then(() => { this.$message.success('流失扫描已执行'); this.loadWarnings() })
    },
    runSleepingScan() {
      api.runSleepingScan().then(() => { this.$message.success('沉睡扫描已执行'); this.loadWarnings() })
    },
    loadConfig() {
      api.configList().then(res => { this.configList = res.data || [] })
    },
    saveConfig(row) {
      api.configSave(row).then(() => this.$message.success('保存成功'))
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.toolbar { margin-bottom: 8px; }
.tip { margin-top: 12px; color: #909399; font-size: 12px; }
</style>
