<script setup lang="ts">
import DeathPopupVue from '../components/basicComponents/DeathPopup.vue'
import RoleModal from '../components/basicComponents/RoleModal.vue'
import ButtonVue from '../components/interactifComponents/Button.vue'
import BackgroundVue from '../components/ambianceComponents/Background.vue'
import PixiContainerVue from '../components/basicComponents/pixi-container.vue'
import CardsPlayerVue from '../components/basicComponents/CardsPlayer.vue'
</script>

<template>
  <!-- <BackgroundVue /> -->
  <PixiContainerVue />
  <!-- <DeathPopupVue />
  <role-modal />
  <ButtonVue /> -->
</template>

<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
</style>
