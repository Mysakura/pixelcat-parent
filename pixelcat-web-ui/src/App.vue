<template>
  <v-app>
    <v-app-bar
      app
      clipped-left
      elevate-on-scroll
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        <v-avatar>
          <v-img
                  class="mr-2"
                  contain
                  src="./assets/cat.gif"
          />
        </v-avatar>
      </div>
      <v-spacer></v-spacer>
      <div class="text-h5">PixelCat Configuration Center</div>
      <v-spacer></v-spacer>

      <v-btn
        href="https://github.com/vuetifyjs/vuetify/releases/latest"
        target="_blank"
        text
      >
        <span class="mr-2">退出</span>
        <v-icon>fa-sign-out</v-icon>
      </v-btn>
    </v-app-bar>

    <nav-bar></nav-bar>
    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>

import NavBar from "./components/NavBar";
export default {
  name: 'App',
  components: {NavBar},

  data: () => ({
    //
  }),
};
</script>
