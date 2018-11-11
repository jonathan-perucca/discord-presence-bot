<template>
  <div class="row">
    <div class="col-lg-12 p-2">
      <h1>{{ msg }}</h1>
    </div>

  </div>
</template>

<script>

export default {
  name: 'Config',
  data () {
    return {
      msg: 'Configuration',
    }
  },
  computed: {
  },
  methods: {
  }
}
</script>

<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
</style>
