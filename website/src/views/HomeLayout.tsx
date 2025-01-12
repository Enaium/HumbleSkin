import { defineComponent } from 'vue'
import { NLayout, NLayoutContent, NLayoutHeader } from 'naive-ui'
import TopBar from '@/components/TopBar'
import { RouterView } from 'vue-router'

const Home = defineComponent(() => () => (
  <>
    <NLayout>
      <NLayoutHeader bordered>
        <TopBar />
      </NLayoutHeader>
      <NLayoutContent>
        <RouterView />
      </NLayoutContent>
    </NLayout>
  </>
))
export default Home
