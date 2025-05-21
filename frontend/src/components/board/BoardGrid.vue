<template>
  <div class="board-grid">
    <BoardCard v-for="board in boards" :key="board.id" :board="board" />
  </div>
</template>

<script setup>
import { defineProps } from "vue";
import BoardCard from "@/components/board/BoardCard.vue";

const { boards } = defineProps({
  boards: {
    type: Array,
    required: true,
  },
});
</script>

<style scoped>
.board-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}
</style>
