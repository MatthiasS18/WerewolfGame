<template>
  <div ref="pixiContainer" class="pixi-container">
    <!-- PixiJS canvas -->
    <!-- ... -->

    <!-- Cartes alignées horizontalement -->
    <div class="cards-row">
      <CardsPlayerVue
        v-for="(card, index) in cards"
        :key="'card-' + index"
        :name="card.name"
        :role="card.role"
        :image="card.image"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Application, Container } from 'pixi.js'
import SpriteObjectVue from '../SpriteObject.vue'
import CardsPlayerVue from './CardsPlayer.vue'

const pixiContainer = ref<HTMLDivElement | null>(null)
const world = new Container()
const staticSprites = ref([])

onMounted(async () => {
  const app = new Application()
  await app.init({
    resizeTo: window,
    backgroundColor: 0x06402b,
  })

  pixiContainer.value?.appendChild(app.canvas)
  app.stage.addChild(world)
})

const cards = ref([
  { name: 'Jean', role: 'Voyante', image: '/images/villagers.png' },
  { name: 'Léa', role: 'Loup-Garou', image: '/images/villagers.png' },
  { name: 'Tariq', role: 'Villageois', image: '/images/villagers.png' },
])
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.pixi-container {
  width: 100%;
  height: 100%;
  background: none;
}

.pixi-container canvas {
  display: block;
  width: 100%;
  height: 100%;
}

.cards-row {
  position: absolute;
  top: 200px; /* tu peux ajuster */
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 20px;
  z-index: 10;
}
</style>
