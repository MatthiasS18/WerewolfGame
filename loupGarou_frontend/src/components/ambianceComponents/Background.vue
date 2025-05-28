<template>
  <div class="background" :class="phaseClass"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useGameStore } from '@/stores/gameStore'

const game = useGameStore()
const phaseClass = computed(() => `bg-${game.phase}`)
</script>

<style scoped>
.background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: background-image 0.6s ease-in-out;
  z-index: -1;
}

.bg-day {
  background-image: url('@/assets/bg-day.jpg');
}
.bg-night {
  background-image: url('@/assets/bg-night.png');
}
.bg-voting {
  background-image: url('@/assets/bg-vote.jpg');
}
.bg-end {
  background-image: url('@/assets/bg-end.jpg');
}
.bg-waiting {
  background-color: #222;
}
</style>
