import Vue from 'vue'
import Router from 'vue-router'
import Flyleafs from '@/components/Flyleafs'
import Vellums from '@/components/Vellums'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '*',
      redirect: '/flyleafs'
    },
    {
      path: '/flyleafs',
      name: 'Flyleafs',
      component: Flyleafs
    },
    {
      path: '/vellums',
      name: 'Vellums',
      component: Vellums
    }
  ]
})
