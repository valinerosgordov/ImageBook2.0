<template>
  <div id="app">
    <b-navbar variant="light" class="mb-4" toggleable="sm">
      <b-navbar-brand href="#">Imagebook</b-navbar-brand>
      <b-navbar-nav>
        <b-nav-item to="/flyleafs">Цветные форзацы</b-nav-item>
        <b-nav-item to="/vellums">Калька</b-nav-item>
      </b-navbar-nav>
    </b-navbar>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  h1 {
    font-size: 2rem;
  }
</style>
