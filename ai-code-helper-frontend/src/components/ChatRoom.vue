<script setup>
import { ref, nextTick, onBeforeUnmount } from 'vue'
import { getMemoryId } from '../utils/session.js'
import { chatStream } from '../api/chat.js'
import ChatMessage from './ChatMessage.vue'
import ChatInput from './ChatInput.vue'

const memoryId = getMemoryId()
const messages = ref([])
const isStreaming = ref(false)
const listRef = ref(null)
let cancelStream = null

function generateId() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2, 8)
}

async function scrollToBottom() {
  await nextTick()
  if (listRef.value) {
    listRef.value.scrollTop = listRef.value.scrollHeight
  }
}

function handleSend(text) {
  const userMsg = {
    id: generateId(),
    role: 'user',
    content: text,
    status: 'complete',
    timestamp: Date.now()
  }
  messages.value.push(userMsg)

  const aiMsg = {
    id: generateId(),
    role: 'ai',
    content: '',
    status: 'streaming',
    timestamp: Date.now()
  }
  messages.value.push(aiMsg)
  isStreaming.value = true

  scrollToBottom()

  const aiIndex = messages.value.length - 1

  cancelStream = chatStream(memoryId, text, {
    onChunk(chunk) {
      messages.value[aiIndex].content += chunk
      scrollToBottom()
    },
    onDone() {
      messages.value[aiIndex].status = 'complete'
      isStreaming.value = false
      cancelStream = null
    },
    onError(err) {
      console.error(err)
      messages.value[aiIndex].status = 'error'
      isStreaming.value = false
      cancelStream = null
    }
  })
}

onBeforeUnmount(() => {
  if (cancelStream) cancelStream()
})
</script>

<template>
  <div class="chat-room">
    <header class="chat-header">
      <h1>AI 编程小助手</h1>
      <span class="session-badge">会话: {{ memoryId.slice(0, 8) }}</span>
    </header>

    <div ref="listRef" class="message-list">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">💡</div>
        <p>有问题随时问，编程学习和求职面试都可以</p>
      </div>
      <ChatMessage
        v-for="msg in messages"
        :key="msg.id"
        :message="msg"
      />
    </div>

    <ChatInput :disabled="isStreaming" @send="handleSend" />
  </div>
</template>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 860px;
  margin: 0 auto;
  background: #fff;
  box-shadow: 0 0 40px rgba(0, 0, 0, 0.08);
}
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid #e9ecef;
  background: #fff;
  flex-shrink: 0;
}
.chat-header h1 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a2e;
}
.session-badge {
  font-size: 12px;
  color: #868e96;
  background: #f1f3f5;
  padding: 4px 10px;
  border-radius: 12px;
}
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #adb5bd;
  gap: 12px;
}
.empty-icon {
  font-size: 48px;
}
.empty-state p {
  margin: 0;
  font-size: 14px;
}
</style>
