import { NButton, NIcon, NMenu } from 'naive-ui'
import { defineComponent } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import Logo from '@/assets/logo.png'
import { Archive16Regular, ArrowEnterLeft20Regular, Person12Regular } from '@vicons/fluent'
import { sessionStore } from '@/stores/session'

const TopBar = defineComponent(() => {
  const menuOptions = [
    {
      label: () => <RouterLink to="/">Home</RouterLink>,
      key: 'home',
    },
    {
      label: () => <RouterLink to="/about">About</RouterLink>,
      key: 'about',
    },
  ]

  const router = useRouter()
  const session = sessionStore()

  return () => (
    <>
      <div class={'flex justify-around items-center'}>
        <RouterLink to="/">
          <img src={Logo} alt={'Humble Skin'} class={'w-24'} />
        </RouterLink>
        <div>
          <NMenu options={menuOptions} mode={'horizontal'} />
        </div>
        <div>
          {session.get !== undefined ? (
            <>
              <NButton
                text
                onClick={() => router.push('/account')}
                v-slots={{
                  default: () => (
                    <>
                      <NIcon size={24}>
                        <Archive16Regular />
                      </NIcon>
                    </>
                  ),
                }}
              ></NButton>
            </>
          ) : (
            <div class={'flex space-x-4'}>
              <NButton
                text
                onClick={() => router.push('/register')}
                v-slots={{
                  default: () => (
                    <>
                      <NIcon size={24}>
                        <Person12Regular />
                      </NIcon>
                      Register
                    </>
                  ),
                }}
              />
              <NButton
                text
                onClick={() => router.push('/login')}
                v-slots={{
                  default: () => (
                    <>
                      <NIcon size={24}>
                        <ArrowEnterLeft20Regular />
                      </NIcon>
                      Login
                    </>
                  ),
                }}
              />
            </div>
          )}
        </div>
      </div>
    </>
  )
})

export default TopBar
