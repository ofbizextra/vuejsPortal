<template>
  <div id="list">
    <h2>List</h2>
    <table>
      <thead>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
      </thead>
      <tr v-for="entry in all">
        <td>{{entry.id}}</td>
        <td>{{entry.name}}</td>
        <td>{{entry.description}}</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'list',
  data () {
    return {
      all: {
        itemA: { id: 1, name: 'item A', description: "item portant la lettre A ..." },
        itemB: { id: 2, name: 'item B', description: "item portant la lettre B ..." },
        itemC: { id: 3, name: 'item C', description: "item portant la lettre C ..." }
      }
    }
  }
}
</script>

<style>

</style>
