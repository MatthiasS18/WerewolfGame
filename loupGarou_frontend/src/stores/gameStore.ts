import { defineStore } from 'pinia'

export type Phase = 'waiting' | 'day' | 'night' | 'voting' | 'end'

export const useGameStore = defineStore('game', {
  state: () => ({
    phase: 'night' as Phase, // valeur initiale
  }),

  actions: {
    setPhase(newPhase: Phase) {
      this.phase = newPhase
    },
  },
})
