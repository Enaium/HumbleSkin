import { ModelType_CONSTANTS, TextureType_CONSTANTS } from '@/__generated/model/enums'
import type {
  AccountView_TargetOf_characters,
  AccountView_TargetOf_characters_TargetOf_textures,
  CharacterInput,
  TextureInput,
} from '@/__generated/model/static'
import { api, BASE_URL } from '@/common/Api'
import { sessionStore } from '@/stores/session'
import { useQuery } from '@tanstack/vue-query'
import {
  type FormInst,
  NButton,
  NCard,
  NForm,
  NFormItem,
  NImage,
  NInput,
  NModal,
  NPopconfirm,
  NRadioButton,
  NRadioGroup,
  NSpin,
  NUpload,
  type UploadFileInfo,
} from 'naive-ui'
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'

const CreateCharacter = defineComponent(
  (props: { accountId: string; onFinished: () => void }) => {
    const form = ref<CharacterInput>({
      name: '',
      model: 'STEVE',
      accountId: props.accountId,
    })

    const formRef = ref<FormInst | null>(null)

    const createCharacter = async () => {
      formRef.value?.validate(async (errors) => {
        if (errors) {
          return
        }

        await api.character
          .save({
            body: form.value,
          })
          .then(() => {
            props.onFinished()
          })
      })
    }

    return () => (
      <>
        <NForm ref={formRef} model={form.value} label-placement={'left'} label-width={'auto'}>
          <NFormItem
            label={'Name'}
            path={'name'}
            rule={[{ required: true, message: 'Name is required', trigger: ['input', 'blur'] }]}
          >
            <NInput v-model:value={form.value.name} />
          </NFormItem>
          <NFormItem label={'Model'} path={'model'} rule={[{ required: true }]}>
            <NRadioGroup v-model:value={form.value.model}>
              {ModelType_CONSTANTS.map((model) => (
                <NRadioButton key={model} value={model}>
                  {model}
                </NRadioButton>
              ))}
            </NRadioGroup>
          </NFormItem>
          <div class={'flex justify-end'}>
            <NFormItem>
              <NButton type={'primary'} onClick={createCharacter}>
                Create
              </NButton>
            </NFormItem>
          </div>
        </NForm>
      </>
    )
  },
  {
    props: ['accountId', 'onFinished'],
  },
)

const UploadTexture = defineComponent(
  (props: { characterId: string; onFinished: () => void }) => {
    const form = ref<TextureInput>({
      type: 'SKIN',
      characterId: props.characterId,
      content: '',
    })

    const formRef = ref<FormInst | null>(null)

    const uploadTexture = async () => {
      formRef.value?.validate(async (errors) => {
        if (errors) {
          return
        }

        await api.texture
          .save({
            body: form.value,
          })
          .then(() => {
            props.onFinished()
          })
      })
    }

    const onBeforeUpload = (options: { file: UploadFileInfo; fileList: UploadFileInfo[] }) => {
      const file = options.file.file

      if (file == null) {
        return false
      }

      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => {
        form.value.content = reader.result as string
      }
    }

    return () => (
      <>
        <NForm ref={formRef} model={form.value} label-placement={'left'} label-width={'auto'}>
          <NFormItem label={'Type'} path={'type'} rule={[{ required: true }]}>
            <NRadioGroup v-model:value={form.value.type}>
              {TextureType_CONSTANTS.map((type) => (
                <NRadioButton key={type} value={type}>
                  {type}
                </NRadioButton>
              ))}
            </NRadioGroup>
          </NFormItem>
          <NFormItem
            label={'Content'}
            path={'content'}
            rule={[{ required: true, message: 'Content is required', trigger: ['input', 'blur'] }]}
          >
            <NUpload
              max={1}
              accept=".png"
              onBeforeUpload={onBeforeUpload}
              onRemove={() => (form.value.content = '')}
            >
              <NButton>Upload</NButton>
            </NUpload>
          </NFormItem>
          <div class={'flex justify-end'}>
            <NFormItem>
              <NButton type={'primary'} onClick={uploadTexture}>
                Upload
              </NButton>
            </NFormItem>
          </div>
        </NForm>
      </>
    )
  },
  {
    props: ['characterId', 'onFinished'],
  },
)

const Account = defineComponent(() => {
  const router = useRouter()
  const session = sessionStore()

  const createCharacterShowed = ref(false)
  const uploadTextureShowed = ref(false)

  const { data, refetch } = useQuery({
    queryKey: ['account'],
    queryFn: async () => api.account.get(),
  })

  const logout = async () => {
    await api.session.logout()
    session.set(undefined)
    router.push('/')
  }

  return () => (
    <>
      <div class={'flex justify-center h-screen'}>
        <div class={'w-2/3 mt-10'}>
          <NCard
            v-slots={{
              header: () => (
                <div class={'flex justify-between'}>
                  <div class={'text-2xl'}>Account</div>
                  <div>
                    {data.value == undefined ? (
                      <NSpin />
                    ) : (
                      <>
                        {data.value.characters.length > 0 ? (
                          <>
                            <NButton
                              type={'primary'}
                              onClick={() => (uploadTextureShowed.value = true)}
                            >
                              Upload Texture
                            </NButton>
                            <NModal
                              class={'w-1/2'}
                              v-model:show={uploadTextureShowed.value}
                              title={'Upload Texture'}
                              preset={'card'}
                              size={'huge'}
                              onClose={() => (uploadTextureShowed.value = false)}
                            >
                              <UploadTexture
                                characterId={data.value.characters[0].id}
                                onFinished={() => {
                                  uploadTextureShowed.value = false
                                  refetch()
                                }}
                              />
                            </NModal>
                          </>
                        ) : (
                          <>
                            <NButton
                              type={'primary'}
                              onClick={() => (createCharacterShowed.value = true)}
                            >
                              Create Character
                            </NButton>
                            <NModal
                              class={'w-1/2'}
                              v-model:show={createCharacterShowed.value}
                              title={'Create Character'}
                              preset={'card'}
                              size={'huge'}
                              onClose={() => (createCharacterShowed.value = false)}
                            >
                              <CreateCharacter
                                accountId={data.value.id}
                                onFinished={() => {
                                  createCharacterShowed.value = false
                                  refetch()
                                }}
                              />
                            </NModal>
                          </>
                        )}
                      </>
                    )}
                  </div>
                </div>
              ),
            }}
          >
            {data.value == undefined ? (
              <>
                <NSpin />
              </>
            ) : (
              <>
                <div class={'flex justify-between'}>
                  <div>Email:</div>
                  <div>{data.value.email}</div>
                </div>
                {data.value.characters.map((character: AccountView_TargetOf_characters) => (
                  <div key={character.name} class={'mt-4'}>
                    <div class={'flex justify-between'}>
                      <div>Name:</div>
                      <div>{character.name}</div>
                    </div>
                    <div class={'flex flex-col gap-5'}>
                      {character.textures.map(
                        (texture: AccountView_TargetOf_characters_TargetOf_textures) => (
                          <div class={'flex justify-between'} key={texture.type}>
                            <div>{texture.type}:</div>
                            <NImage
                              src={`${BASE_URL}/textures/${texture.hash}`}
                              alt={texture.type}
                            />
                          </div>
                        ),
                      )}
                    </div>
                  </div>
                ))}
              </>
            )}
            <div class={'flex justify-end mt-5'}>
              <NPopconfirm
                onPositiveClick={logout}
                v-slots={{
                  default: () => 'Are you sure you want to logout?',
                  trigger: () => <NButton type={'warning'}>Logout</NButton>,
                }}
              />
            </div>
          </NCard>
        </div>
      </div>
    </>
  )
})

export default Account
