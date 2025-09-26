<template>
  <div id="app">
    <NavBar />

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2025 My App. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import NavBar from "@/components/NavBar.vue";
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  width: 100%;
  padding: var(--spacing-xl) 0;
}

.footer {
  background-color: var(--secondary-color);
  color: var(--text-white);
  padding: var(--spacing-lg) 0;
  margin-top: auto;
}

.footer-content {
  max-width: var(--max-width-xl);
  margin: 0 auto;
  padding: 0 var(--spacing-md);
  text-align: center;
  font-size: var(--font-sm);
  color: var(--text-light);
}
</style>
