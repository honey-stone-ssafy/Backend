<template>
  <div class="board-card">
    <img :src="board.image" :alt="board.title" class="thumbnail" />
    <h3 class="title">{{ board.title }}</h3>
    <p class="subtitle">{{ board.subtitle }}</p>
    <div class="tags">
      <span v-for="tag in board.tags" :key="tag" class="tag">{{ tag }}</span>
    </div>
    <IconButton icon="heart" :active="board.liked" />
  </div>
</template>

<script setup>
import { defineProps } from "vue";
import IconButton from "@/components/common/IconButton.vue";

const { board } = defineProps({
  board: {
    type: Object,
    required: true,
  },
});
</script>

<style scoped>
.board-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.thumbnail {
  width: 100%;
  object-fit: cover;
}
.title {
  font-size: 1rem;
  margin: 8px 12px 4px;
}
.subtitle {
  font-size: 0.875rem;
  color: #555;
  margin: 0 12px 8px;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 0 12px 8px;
}
.tag {
  background: #f0f0f0;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.75rem;
}
</style>
