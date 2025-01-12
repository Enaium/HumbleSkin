import { defineComponent } from 'vue'
import Logo from '@/assets/logo.png'

const Home = defineComponent(() => {
  return () => (
    <>
      <div class={'flex flex-col items-center justify-center h-full space-y-4 mt-10'}>
        <img src={Logo} alt={'Humble Skin'} />
        <div class={'text-4xl'}>Humble Skin</div>
        <div class={'text-2xl'}>Humble Skin is a humble Minecraft skin hosting server. </div>
      </div>
    </>
  )
})

export default Home
