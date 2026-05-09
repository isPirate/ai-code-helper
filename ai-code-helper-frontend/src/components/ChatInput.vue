<script setup>
import { ref } from 'vue'

const props = defineProps({
  disabled: Boolean
})

const emit = defineEmits(['send'])

const text = ref('')

function handleSend() {
  const trimmed = text.value.trim()
  if (!trimmed || props.disabled) return
  emit('send', trimmed)
  text.value = ''
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSend()
  }
}
</script>

<template>
  <div class="input-area">
    <div class="input-wrapper">
      <textarea
        v-model="text"
        class="input-box"
        placeholder="输入你的问题，Enter 发送，Shift+Enter 换行"
        rows="1"
        :disabled="disabled"
        @keydown="handleKeydown"
      />
      <button
        class="send-btn"
        :disabled="disabled || !text.trim()"
        @click="handleSend"
      >
        发送
      </button>
    </div>
  </div>
</template>

<style scoped>
.input-area {
  padding: 16px 20px;
  border-top: 1px solid #e9ecef;
  background: #fff;
}
.input-wrapper {
  display: flex;
  gap: 10px;
  align-items: flex-end;
}
.input-box {
  flex: 1;
  resize: none;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  padding: 10px 14px;
  font-size: 14px;
  line-height: 1.5;
  outline: none;
  font-family: inherit;
  max-height: 120px;
}
.input-box:focus {
  border-color: #4f6ef7;
}
.send-btn {
  padding: 10px 22px;
  border: none;
  border-radius: 8px;
  background: #4f6ef7;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.2s;
}
.send-btn:hover:not(:disabled) {
  background: #3b5de7;
}
.send-btn:disabled {
  background: #adb5bd;
  cursor: not-allowed;
}
</style>
