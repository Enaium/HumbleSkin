import type { ApiErrors } from '@/__generated'
import type { LoginInput, SessionView } from '@/__generated/model/static'
import { api } from '@/common/Api'
import { sessionStore } from '@/stores/session'
import { NButton, NCard, NForm, NFormItem, NInput, useMessage, type FormInst } from 'naive-ui'
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'

const Login = defineComponent(() => {
  const session = sessionStore()
  const router = useRouter()
  const message = useMessage()

  const form = ref<LoginInput>({
    email: '',
    password: '',
  })

  const formRef = ref<FormInst | null>(null)

  const login = async () => {
    formRef.value?.validate(async (errors) => {
      if (errors) {
        return
      }

      api.session
        .login({
          body: form.value,
        })
        .then((view: SessionView) => {
          session.set(view)
          router.push('/')
        })
        .catch((error: ApiErrors['session']['login']) => {
          switch (error.code) {
            case 'USERNAME_OR_PASSWORD_ERROR':
              message.error('Username or password is incorrect')
              break
          }
        })
    })
  }

  return () => (
    <>
      <div class={'flex justify-center items-center h-screen'}>
        <div class={'w-96 h-96'}>
          <NCard title={'Login'}>
            <NForm label-placement={'left'} label-width={'auto'} model={form.value} ref={formRef}>
              <NFormItem
                label="Email"
                path="email"
                rule={[
                  { required: true, message: 'Email is required', trigger: ['input', 'blur'] },
                ]}
              >
                <NInput v-model:value={form.value.email} />
              </NFormItem>
              <NFormItem
                label="Password"
                path="password"
                rule={[
                  { required: true, message: 'Password is required', trigger: ['input', 'blur'] },
                ]}
              >
                <NInput v-model:value={form.value.password} type={'password'} />
              </NFormItem>
              <div class={'flex justify-end'}>
                <NFormItem>
                  <NButton type="primary" onClick={login}>
                    Login
                  </NButton>
                </NFormItem>
              </div>
            </NForm>
          </NCard>
        </div>
      </div>
    </>
  )
})

export default Login
