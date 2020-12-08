import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import Axios from 'axios'   // 引入Axios

Axios.defaults.baseURL = 'http://127.0.0.1:8888';
// Axios.defaults.headers.common['token'] = localStorage.getItem("token");  登录之后自动设置
Vue.prototype.$axios = Axios;    // 全局变量

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app');
