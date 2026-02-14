<template>
  <div class="table-action-cell">
    <template v-if="isMobile">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="action-trigger"><i class="el-icon-more-outline"></i> 操作</span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item
            v-for="(a, i) in actions"
            :key="i"
            :command="i"
            :class="{ 'is-danger': a.danger }"
          >{{ a.label }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>
    <template v-else>
      <template v-for="(a, i) in actions">
        <el-button
          :key="i"
          type="text"
          size="small"
          :style="a.danger ? { color: '#f56c6c' } : {}"
          @click="a.handler(row)"
        >{{ a.label }}</el-button>
      </template>
    </template>
  </div>
</template>

<script>
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'TableActionCell',
  mixins: [mobileMixin],
  props: {
    row: { type: Object, required: true },
    actions: {
      type: Array,
      required: true
      // [{ label: '详情', handler: (row)=>{} }, { label: '删除', handler: (row)=>{}, danger: true }]
    }
  },
  methods: {
    handleCommand(index) {
      const a = this.actions[Number(index)]
      if (a && a.handler) a.handler(this.row)
    }
  }
}
</script>

<style scoped>
.table-action-cell { white-space: nowrap; }
.action-trigger { cursor: pointer; color: #409EFF; font-size: 12px; padding: 0 4px; }
.action-trigger i { margin-right: 2px; }
@media (max-width: 768px) {
  .table-action-cell >>> .el-button--text { padding: 2px 4px; }
}
</style>
<style>
.el-dropdown-menu__item.is-danger { color: #f56c6c; }
</style>
