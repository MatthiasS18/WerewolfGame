<template>
  <div></div>
  <!-- Le rendu est géré par Pixi.js, donc vide ici -->
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { Sprite, Assets } from 'pixi.js'

const props = defineProps<{
  world: any
  src: string
  playerName: number
  scale?: number
}>()

onMounted(async () => {
  const texture = await Assets.load(props.src)
  const sprite = new Sprite(texture)

  sprite.anchor.set(0.5)
  sprite.playerName = props.playerName

  sprite.scale.set(props.scale ?? 2)

  props.world.addChild(sprite)
})
</script>
