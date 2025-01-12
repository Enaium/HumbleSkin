import { Api } from '@/__generated'
import { sessionStore } from '@/stores/session'
import { useRouter } from 'vue-router'

export const BASE_URL = 'http://localhost:8080'

export const api = new Api(async ({ uri, method, body }) => {
  const session = sessionStore()
  const router = useRouter()

  const response = await fetch(`${BASE_URL}${uri}`, {
    method,
    body: body !== undefined ? JSON.stringify(body) : undefined,
    headers: {
      'Content-Type': 'application/json',
      ...(session.get ? { Authorization: session.get.authorization } : {}),
    },
  })

  if (response.status === 401) {
    session.set(undefined)
    router.push('/login')
    return
  }

  if (response.status === 400) {
    throw 'Input error'
  }

  if (!response.ok) {
    throw JSON.parse(await response.text())
  }

  const text = await response.text()

  if (text.length === 0) {
    return undefined
  }

  return JSON.parse(text)
})
