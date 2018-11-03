<template>
  <div :class="classProp">
    <span class="badge badge-info m-3">{{ recordDTOs.numberOfUsers }} members</span>
    <span class="badge badge-info">{{ recordDTOs.sessionDate }}</span>
    <ul class="list-group">
      <li v-for="sessionRecord in recordDTOs.records" class="list-group-item d-flex justify-content-between align-items-center">
        {{ sessionRecord.user }}
        <span class="badge badge-primary">{{ sessionRecord.timeSpent }} seconds</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'SessionDetails',
  props : {
    recordDTOs: {},
    classProp: ''
  },
  data () {
    return {
      recordUpdateInterval: ''
    }
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
a {
  color: #42b983;
}
</style>
