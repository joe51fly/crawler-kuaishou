// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import $ from 'jquery';
import {VueJsonp} from 'vue-jsonp';
import vueVideoPlayer from 'vue-video-player';
import videoJsContribHls from 'videojs-contrib-hls';

Vue.use(ElementUI);
Vue.use(VueJsonp);
Vue.use(vueVideoPlayer);
Vue.use(videoJsContribHls);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
