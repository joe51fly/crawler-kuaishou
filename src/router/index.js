import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/login'
import Home from '../views/Home'
// import Header from "../views/Header";
import LiveList from "../views/LiveList";

Vue.use(Router)

export default new Router({
  /** 添加以下代码 */
  base: "/ks-vue/", //项目名称 访问路由页面都需要加上这个,访问的根路径为http://ip:port/my_app
  /** 添加上述代码 结束 */
  routes: [
    {
      path: '/login',
      component: Login
    },
    {
      path: '/home',
      component: Home,
      children: [
        {
          path: 'live-list',
          name:'live-list',
          component: LiveList,
        },{
          path: '',
          redirect: '/home/live-list'
        },
      ]
    },
    {
      path: '/',
      redirect: '/login'
    },
  ]
})
