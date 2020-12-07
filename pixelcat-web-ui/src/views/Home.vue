<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <FastSearch/>
      </v-col>
      <v-col cols="12">

      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// @ is an alias to /src
import FastSearch from '@/components/FastSearch'

export default {
  name: 'Home',
  components: {
    FastSearch
  }
}
</script>
