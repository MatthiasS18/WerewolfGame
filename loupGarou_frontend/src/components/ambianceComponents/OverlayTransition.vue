<template>
  <div class="transition-overlay" v-if="showOverlay"></div>
</template>

<style scoped>
.transition-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: black;
  opacity: 0.7;
  animation: fadeOverlay 1s ease-in-out forwards;
  z-index: 1000;
}

@keyframes fadeOverlay {
  0% {
    opacity: 0.7;
  }
  100% {
    opacity: 0;
  }
}
</style>
