<template>
  <div id="vue-next-prev" :class="paginateStyle">
    <ul>
      <li :class="paginateFirstStyle">
        <a href="" v-on:click.prevent="first">{{paginateFirstLabel}}</a>
      </li>
      <li :class="paginatePreviousStyle">
        <a href="" v-on:click.prevent="previous">{{paginatePreviousLabel}}</a>
      </li>
      <li>
        {{pageLabel}}
        <select v-model="viewIndex">
          <option v-for="index in Array.from(Array(numberOfPages).keys())" :key="index" :value="index">
            {{index + 1}}
          </option>
        </select>
      </li>
      <li :class="paginateNextStyle">
        <a href="" v-on:click.prevent="next">{{paginateNextLabel}}</a>
      </li>
      <li :class="paginateLastStyle">
        <a href="" v-on:click.prevent="last">{{paginateLastLabel}}</a>
      </li>
      <li class="nav-pagesize">
        <select v-model="viewSize">
          <option :value="20">
            20
          </option>
          <option :value="30">
            30
          </option>
          <option :value="50">
            50
          </option>
          <option :value="100">
            100
          </option>
          <option :value="200">
            200
          </option>
        </select>
        Items per Pages
      </li>
      <li class="nav-displaying">
        {{commonDisplaying}}
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueNextPrev",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['currentValue']
        Object.keys(data).map(it => {
          if (data[it] === ''){
            delete data[it]
          }
        })
        return data
      },
      commonDisplaying() {
        return this.data.commonDisplaying
      },
      highIndex() {
        return this.data.highIndex
      },
      listSize() {
        return this.data.listSize
      },
      pageLabel() {
        return this.data.pageLabel
      },
      paginateFirstLabel() {
        return this.data.paginateFirstLabel
      },
      paginateFirstStyle() {
        return this.data.paginateFirstStyle
      },
      paginateLastLabel() {
        return this.data.paginateLastLabel
      },
      paginateLastStyle() {
        return this.data.paginateLastStyle
      },
      paginateNextLabel() {
        return this.data.paginateNextLabel
      },
      paginateNextStyle() {
        return this.data.paginateNextStyle
      },
      paginatePreviousLabel() {
        return this.data.paginatePreviousLabel
      },
      paginatePreviousStyle() {
        return this.data.paginatePreviousStyle
      },
      paginateStyle() {
        return this.data.paginateStyle
      },
      paginateTarget() {
        return this.data.paginateTarget
      },
      paginateViewSizeLabel() {
        return this.data.paginateViewSizeLabel
      },
      viewIndex: {
        get() {
          return this.data.viewIndex
        },
        set(viewIndex) {
          console.log('ViewIndex set to ' + viewIndex)
          this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_INDEX_1: viewIndex}})
        }
      },
      viewSize: {
        get() {
          return this.data.viewSize
        },
        set(viewSize) {
          console.log('ViewSize set to ' + viewSize)
          this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_SIZE_1: viewSize}})
        }
      },
      numberOfPages() {
        return Math.trunc(this.listSize % this.viewSize > 0 ? this.listSize / this.viewSize + 1 : this.listSize / this.viewSize)
      }
    },
    methods: {
      first() {
        console.log('First')
        this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_INDEX_1: 0}})
      },
      previous() {
        console.log('Previous')
        this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_INDEX_1: this.viewIndex - 1}})
      },
      next() {
        console.log('Next')
        this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_INDEX_1: this.viewIndex + 1}})
      },
      last() {
        console.log('Last')
        this.$store.dispatch('data/setWatcherAttributes', {watcherName: this.paginateTarget, params: {VIEW_INDEX_1: this.numberOfPages - 1}})
      }
    },
    watch: {

    }
  }
</script>

<style scoped>

</style>
