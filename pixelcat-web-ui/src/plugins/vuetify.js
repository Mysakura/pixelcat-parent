import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import 'font-awesome/css/font-awesome.min.css'; // 确保您正在使用 css-loader

Vue.use(Vuetify);

export default new Vuetify({
    icons: {
        iconfont: 'fa4', // 'mdi' || 'mdiSvg' || 'md' || 'fa' || 'fa4' || 'faSvg'
    },
});
