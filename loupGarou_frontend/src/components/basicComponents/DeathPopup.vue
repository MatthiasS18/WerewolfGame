<template>
  <transition name="slide-down">
    <div v-if="visible" class="alert-modal">
      <div class="alert-box">
        <span class="closebtn" @click="visible = false">&times;</span>
        DEAD 💀 !!!
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const visible = ref(true) // équivalent au mutableState

onMounted(() => {
  // Disparaît automatiquement après 4 secondes
  setTimeout(() => {
    visible.value = false
  }, 6000)
})
</script>

<style scoped>
.alert-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.alert-box {
  background-color: #f44336;
  color: white;
  padding: 30px 40px;
  border-radius: 10px;
  font-size: 28px;
  font-weight: bold;
  position: relative;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
}

/* Bouton fermer */
.closebtn {
  position: absolute;
  top: 8px;
  right: 12px;
  color: white;
  font-size: 24px;
  font-weight: bold;
  cursor: pointer;
}

.closebtn:hover {
  color: black;
}

/* Transition uniquement pour l’entrée */
.slide-down-enter-active {
  animation: slideInDown 0.6s ease-out forwards;
}
.slide-down-leave-active {
  /* Pas d'animation de sortie, juste disparition */
  display: none;
}

@keyframes slideInDown {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
