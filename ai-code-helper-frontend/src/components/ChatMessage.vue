<script setup>
import { computed } from 'vue'
import { marked } from 'marked'

const props = defineProps({
  message: {
    type: Object,
    required: true
  }
})

const renderedContent = computed(() => {
  if (props.message.role === 'user') return null
  return marked.parse(props.message.content)
})
</script>

<template>
  <div class="message-row" :class="message.role === 'user' ? 'user' : 'ai'">
    <div class="avatar">
      {{ message.role === 'user' ? '👤' : '🤖' }}
    </div>
    <div class="bubble" :class="message.role">
      <template v-if="message.role === 'user'">
        <div class="content">{{ message.content }}</div>
      </template>
      <template v-else>
        <div class="content markdown-body" v-html="renderedContent" />
      </template>
      <span v-if="message.status === 'streaming'" class="cursor">|</span>
      <div v-if="message.status === 'error'" class="error-tip">连接中断，请重试</div>
    </div>
  </div>
</template>

<style scoped>
.message-row {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: flex-start;
}
.message-row.user {
  flex-direction: row-reverse;
}
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  user-select: none;
}
.bubble {
  max-width: 72%;
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
  word-break: break-word;
}
.bubble.ai {
  background: #f1f3f5;
  color: #1a1a2e;
  border-bottom-left-radius: 4px;
}
.bubble.user {
  background: #4f6ef7;
  color: #fff;
  border-bottom-right-radius: 4px;
  white-space: pre-wrap;
}

/* markdown — :deep() penetrates v-html children */
.markdown-body :deep(h1), .markdown-body :deep(h2), .markdown-body :deep(h3) {
  margin: 16px 0 8px;
  font-weight: 600;
  line-height: 1.4;
}
.markdown-body :deep(h1) { font-size: 1.4em; }
.markdown-body :deep(h2) { font-size: 1.2em; }
.markdown-body :deep(h3) { font-size: 1.05em; }
.markdown-body :deep(h1:first-child), .markdown-body :deep(h2:first-child), .markdown-body :deep(h3:first-child) {
  margin-top: 0;
}

.markdown-body :deep(p) {
  margin: 4px 0;
}

.markdown-body :deep(ul), .markdown-body :deep(ol) {
  padding-left: 20px;
  margin: 4px 0;
}
.markdown-body :deep(li) {
  margin: 2px 0;
}

.markdown-body :deep(code) {
  background: #e9ecef;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.9em;
  font-family: 'Fira Code', 'Consolas', monospace;
}
.markdown-body :deep(pre) {
  background: #1e1e2e;
  color: #cdd6f4;
  padding: 12px 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 8px 0;
}
.markdown-body :deep(pre) code {
  background: none;
  padding: 0;
  color: inherit;
  font-size: 0.85em;
}

.markdown-body :deep(blockquote) {
  border-left: 3px solid #4f6ef7;
  padding-left: 12px;
  margin: 8px 0;
  color: #495057;
}

.markdown-body :deep(table) {
  border-collapse: collapse;
  margin: 8px 0;
  width: 100%;
}
.markdown-body :deep(th), .markdown-body :deep(td) {
  border: 1px solid #dee2e6;
  padding: 6px 12px;
  text-align: left;
  font-size: 0.9em;
}
.markdown-body :deep(th) {
  background: #f1f3f5;
  font-weight: 600;
}

.markdown-body :deep(strong) {
  font-weight: 600;
}

.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid #dee2e6;
  margin: 12px 0;
}

.markdown-body :deep(a) {
  color: #4f6ef7;
  text-decoration: none;
}
.markdown-body :deep(a):hover {
  text-decoration: underline;
}

.cursor {
  display: inline-block;
  animation: blink 0.8s infinite;
  color: #4f6ef7;
  font-weight: bold;
}
@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}
.error-tip {
  margin-top: 6px;
  font-size: 12px;
  color: #e03131;
}
</style>
