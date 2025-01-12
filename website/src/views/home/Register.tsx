import type { ApiErrors } from '@/__generated'
import type { RegisterInput, SessionView } from '@/__generated/model/static'
import { api } from '@/common/Api'
import { sessionStore } from '@/stores/session'
import { NButton, NCard, NForm, NFormItem, NInput, useMessage, type FormInst } from 'naive-ui'
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'

const Register = defineComponent(() => {
  const session = sessionStore()
  const router = useRouter()
  const message = useMessage()

  const form = ref<RegisterInput>({
    email: '',
    password: '',
    confirmPassword: '',
  })

  const formRef = ref<FormInst | null>(null)

  const register = async () => {
    formRef.value?.validate(async (errors) => {
      if (errors) {
        return
      }

      api.account
        .register({
          body: form.value,
        })
        .then(() => {
          message.success('Register successful')
          router.push('/login')
        })
        .catch((error: ApiErrors['account']['register']) => {
          switch (error.code) {
            case 'CONFIRM_PASSWORD_ERROR':
              message.error('Password and Confirm Password do not match')
              break
            case 'EMAIL_EXIST':
              message.error('Email already exists')
              break
            default:
              message.error('Please check your input')
              break
          }
        })
    })
  }

  return () => (
    <>
      <div class={'flex justify-center items-center h-screen'}>
        <div class={'w-96 h-96'}>
          <NCard title={'Register'}>
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
              <NFormItem
                label="Confirm Password"
                path="confirmPassword"
                rule={[
                  {
                    required: true,
                    message: 'Confirm Password is required',
                    trigger: ['input', 'blur'],
                  },
                ]}
              >
                <NInput v-model:value={form.value.confirmPassword} type={'password'} />
              </NFormItem>
              <div class={'flex justify-end'}>
                <NFormItem>
                  <NButton type="primary" onClick={register}>
                    Register
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

export default Register
