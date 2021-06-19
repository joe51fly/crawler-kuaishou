import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/login'
import Home from '../views/Home'
// import Header from "../views/Header";
import List from "../views/List";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      component: Login
    },
    {
      path: '/home',
      component: Home,
      default:List,
      children: [
        {
          path: 'list',
          name:'list',
          component: List,
        },{
          path: '',
          redirect: '/home/list'
        },
      ]
    },
    {
      path: '/',
      redirect: '/login'
    },
  ]
})
