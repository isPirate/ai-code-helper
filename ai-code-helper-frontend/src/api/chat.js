const BASE = 'http://localhost:8081/api/ai'

export function chatStream(memoryId, message, { onChunk, onDone, onError }) {
  const url = `${BASE}/chat?memoryId=${encodeURIComponent(memoryId)}&message=${encodeURIComponent(message)}`
  const controller = new AbortController()
  let aborted = false

  fetch(url, { signal: controller.signal })
    .then(async (response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`)
      }
      const reader = response.body.getReader()
      const decoder = new TextDecoder()
      let buffer = ''

      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer += decoder.decode(value, { stream: true })
        const parts = buffer.split('\n\n')
        buffer = parts.pop()

        for (const part of parts) {
          const lines = part.split('\n')
          const dataLines = []
          for (const line of lines) {
            if (line.startsWith('data:')) {
              let value = line.slice(5)
              if (value.startsWith(' ')) value = value.slice(1)
              dataLines.push(value)
            }
          }
          if (dataLines.length > 0) {
            onChunk(dataLines.join('\n'))
          }
        }
      }

      // flush remaining buffer
      if (buffer.trim()) {
        const lines = buffer.split('\n')
        const dataLines = []
        for (const line of lines) {
          if (line.startsWith('data:')) {
            let value = line.slice(5)
            if (value.startsWith(' ')) value = value.slice(1)
            dataLines.push(value)
          }
        }
        if (dataLines.length > 0) {
          onChunk(dataLines.join('\n'))
        }
      }

      onDone()
    })
    .catch((err) => {
      if (aborted) return
      onError(err)
    })

  return () => {
    aborted = true
    controller.abort()
  }
}
