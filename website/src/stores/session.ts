import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { SessionView } from '@/__generated/model/static'

export const sessionStore = defineStore(
  'session',
  () => {
    const session = ref<SessionView>()
    const get = computed(() => session.value)

    function set(newSession?: SessionView) {
      session.value = newSession
    }

    return { session, get, set }
  },
  { persist: true },
)
