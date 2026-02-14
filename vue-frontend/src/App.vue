<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; }
html { -webkit-text-size-adjust: 100%; }
body { -webkit-tap-highlight-color: transparent; }

/* 移动端全局适配 */
@media (max-width: 768px) {
  .el-message-box { max-width: 90vw; }
  .el-dialog { width: 95% !important; max-width: 95vw; margin-top: 5vh !important; }
  .el-dialog__body { max-height: 65vh; overflow-y: auto; -webkit-overflow-scrolling: touch; }
  .el-form--inline .el-form-item { margin-right: 8px; margin-bottom: 8px; }
  .el-form-item__label { min-width: 70px; }
  .el-input, .el-select, .el-date-editor { width: 100% !important; min-width: 0 !important; }
  .el-pagination { flex-wrap: wrap; justify-content: center; }
  .el-pagination .el-pager li { min-width: 28px; }
  .page .el-card { margin: 0; }
  .page .el-card__body { padding: 12px; }
  .query-form { flex-wrap: wrap; }
  .el-table { font-size: 12px; }
  .el-table .el-button--text { padding: 4px 6px; }
}
</style>
