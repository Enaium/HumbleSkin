import { dateZhCN, NConfigProvider, NMessageProvider, zhCN } from 'naive-ui'
import { defineComponent } from 'vue'
import { RouterView } from 'vue-router'

const App = defineComponent(() => () => (
  <>
    <NConfigProvider locale={zhCN} dateLocale={dateZhCN}>
      <NMessageProvider>
        <RouterView />
      </NMessageProvider>
    </NConfigProvider>
  </>
))
export default App
