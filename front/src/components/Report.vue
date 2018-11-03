<template>
  <div class="row">
    <div class="col-lg-12 p-2">
      <h1>{{ msg }}</h1>
    </div>

    <div v-for="session in sessions" class="col-lg-3 p-2">
      <button @click="selectSession(session)" class="btn btn-block btn-outline-primary font-weight-bold">
            {{ session.sessionDate }}
      </button>
    </div>

    <div v-if="hasSelectedSession" class="col-lg-12 p-2">
      <session-details :recordDTOs="sessionSelected"></session-details>
    </div>
  </div>
</template>

<script>
import SessionDetails from "./SessionDetails.vue";

export default {
  components: {SessionDetails},
  name: 'Report',
  data () {
    return {
      msg: 'Reports',
      sessions: [],
      sessionSelected: null
    }
  },
  computed: {
    hasSelectedSession() {
      return this.sessionSelected !== null;
    }
  },
  mounted() {
    this.loadSessions();
  },
  methods: {
    loadSessions() {
      fetch("http://localhost:8080/reports", { method: 'GET', headers: { 'Content-Type': 'application/json' }})
        .then(response => response.json())
        .then(body => this.sessions = body);
    },
    selectSession(session) {
      this.sessionSelected = session;
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
</style>
