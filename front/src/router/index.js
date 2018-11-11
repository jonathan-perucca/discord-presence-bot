import Vue from 'vue'
import Router from 'vue-router'
import Session from '@/components/Session'
import Report from '@/components/Report'
import Config from '@/components/Config'

Vue.use(Router)

export default new Router({
  linkExactActiveClass: 'active',
  routes: [
    {
      path: '/',
      name: 'Session',
      component: Session
    },
    {
      path: '/reports',
      name: 'Report',
      component: Report
    },
    {
      path: '/config',
      name: 'Config',
      component: Config
    }
  ]
})
