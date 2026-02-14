import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import mobileMixin from './mixins/mobile'
import TableActionCell from './components/TableActionCell.vue'

Vue.use(ElementUI)
Vue.mixin(mobileMixin)
Vue.component('TableActionCell', TableActionCell)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
