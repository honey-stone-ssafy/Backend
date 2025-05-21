<template>
  <div>
    <WeeklyRanking />
    <BoardSection :boards="boards" />
  </div>
</template>

<script setup>
import BoardSection from "@/components/board/BoardSection.vue";
import WeeklyRanking from "@/components/ranking/WeeklyRanking.vue";
import { ref, onMounted } from "vue";

const boards = ref([]);

onMounted(async () => {
  boards.value = [
    {
      id: 1,
      image: "..",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
    {
      id: 2,
      image: "@/public/sample-thumbnail.png",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
    {
      id: 3,
      image: "@/public/sample-thumbnail.png",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
    {
      id: 4,
      image: "@/public/sample-thumbnail.png",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
    {
      id: 5,
      image: "@/public/sample-thumbnail.png",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
    {
      id: 6,
      image: "@/public/sample-thumbnail.png",
      title: "Test 입니다",
      subtitle: "happy~",
      tags: ["Test", "happy"],
      liked: true,
    },
  ];
});
</script>

<style scoped></style>
