const STORAGE_KEY = 'ai-chat-memory-id'

function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0
    const v = c === 'x' ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}

export function getMemoryId() {
  let id = sessionStorage.getItem(STORAGE_KEY)
  if (!id) {
    id = generateUUID()
    sessionStorage.setItem(STORAGE_KEY, id)
  }
  return id
}
