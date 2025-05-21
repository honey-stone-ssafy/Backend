<!-- src/components/board/BoardSection.vue -->
<template>
  <section class="board-section">
    <SectionHeader />
    <BoardGrid :boards="boards" />
  </section>
</template>
<script setup>
import { defineProps } from "vue";
import SectionHeader from "@/components/board/SectionHeader.vue";
import BoardGrid from "@/components/board/BoardGrid.vue";

const { boards } = defineProps({
  boards: {
    type: Array,
    required: true,
  },
});
</script>
