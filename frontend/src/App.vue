<script setup>
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
  <div id="app">
    <!-- 헤더 -->
    <header>
      <nav>
        <RouterLink to="/">main</RouterLink>
      </nav>
    </header>

    <!-- 렌더링 할 뷰 -->
    <main>
      <RouterView />
    </main>

    <!-- 푸터 -->
    <footer></footer>
  </div>
</template>

<style scoped></style>
