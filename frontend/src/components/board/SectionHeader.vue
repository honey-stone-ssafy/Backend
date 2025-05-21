<template>
  <div class="section-header">
    <Tabs :options="['최신순', '찜 많은 순']" />
    <IconButton icon="filter" />
  </div>
</template>

<script setup>
import Tabs from "@/components/common/Tabs.vue";
import IconButton from "@/components/common/IconButton.vue";
</script>

<style scoped>
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
</style>
