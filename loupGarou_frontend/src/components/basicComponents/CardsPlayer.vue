<template>
  <div class="card">
    <img :src="props.image" :alt="props.name" style="width: 100%" />
    <div class="container">
      <h4>
        <b>{{ props.name }}</b>
      </h4>
      <p>{{ props.role }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  name: string
  role: string
  image: string
}>()
</script>

<style>
.card {
  /* Add shadows to create the "card" effect */
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  transition: 0.3s;
}

/* On mouse-over, add a deeper shadow */
.card:hover {
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);
}

/* Add some padding inside the card container */
.container {
  padding: 2px 16px;
}

.card img {
  width: 100px;
  height: 200px;
}
</style>
