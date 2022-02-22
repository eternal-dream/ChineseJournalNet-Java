import Vue from 'vue'
import Vuex from 'vuex'
import getters from './modules/d2admin/getters'

import d2admin from './modules/d2admin'
import front from './modules/front'
import common from './modules/common'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    d2admin,
    front,
    common
  },
  getters
})

export default store
