import Vue from 'vue'
import Router from 'vue-router'
import Session from '@/components/Session'
import Report from '@/components/Report'

Vue.use(Router)

export default new Router({
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
    }
  ]
})
