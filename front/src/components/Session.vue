<template>
  <div class="row">
    <div class="col-lg-8 p-2">
      <h1>{{ msg }}</h1>
    </div>
    <div class="col-lg-4 p-2">

      <div @click="startSession()" :class="{'d-none': activeSession}" class="btn btn-primary btn-lg">Start</div>
      <div @click="stopSession()" :class="{'d-none': !activeSession}" class="btn btn-danger btn-lg">Stop</div>
    </div>

    <div v-if="hasCurrentSession" class="col-lg-8 p-0">
      <session-details :recordDTOs="sessionRecords"></session-details>
    </div>
  </div>
</template>

<script>
import SessionDetails from "./SessionDetails.vue";

export default {
  components: {SessionDetails},
  name: 'Session',
  data () {
    return {
      msg: 'Sessions',
      activeSession: false,
      sessionRecords: {},
      recordUpdateInterval: ''
    }
  },
  computed: {
    hasCurrentSession() {
      return this.recordUpdateInterval !== '';
    }
  },
  methods: {
    startSession() {
      fetch("http://localhost:8080/sessions", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({command: 'start'}) })
        .then(response => response.json())
        .then(body => console.log(body))
        .then(body => this.getCurrentSessionRecords());
      this.activeSession = true;
    },
    stopSession() {
      fetch("http://localhost:8080/sessions", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({command: 'stop'}) })
        .then(response => response.json())
        .then(body => console.log(body))
        .then(body => { clearInterval(this.recordUpdateInterval); this.recordUpdateInterval = ''; });

      this.activeSession = false;
    },
    getCurrentSessionRecords() {
      let getCurrentSessionRecordsFn = () =>
        fetch("http://localhost:8080/sessions/current", { method: 'GET', headers: { 'Content-Type': 'application/json' } })
        .then(response => response.json())
        .then(body => {console.log(body); this.sessionRecords = body;});

      this.recordUpdateInterval = this.createRecordUpdater(getCurrentSessionRecordsFn, 5000);
    },
    createRecordUpdater(fn, time) {
      fn();
      return setInterval(() => fn(), time)
    }
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
