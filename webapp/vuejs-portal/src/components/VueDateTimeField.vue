<template>
  <v-row id="vue-date-time-field">
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <label class="font-weight-medium ma-2" v-on="fieldHelpText ? on : null">{{fieldTitle}}</label>
      </template>
      <span>{{fieldHelpText}}</span>
    </v-tooltip>
    <v-menu
        ref="dateMenu"
        v-model="dateMenu"
        :close-on-content-click="false"
        :return-value.sync="date"
        transition="scale-transition"
        offset-overflow
        offset-y
        min-width="290px"
    >
      <template v-slot:activator="{ on }">
        <v-text-field
            v-model="date"
            label="date"
            prepend-icon="mdi-event"
            :rules="rules"
            :hide-details="noRules"
            v-on="on"
        ></v-text-field>
      </template>
      <v-date-picker v-model="date" scrollable @change="$refs.dateMenu.save(date)">
      </v-date-picker>
    </v-menu>
    <v-menu
        ref="timeMenu"
        v-model="timeMenu"
        :close-on-content-click="false"
        :return-value.sync="time"
        transition="scale-transition"
        offset-overflow
        offset-y
        min-width="290px"
    >
      <template v-slot:activator="{ on }">
        <v-text-field
            v-model="time"
            label="time"
            prepend-icon="mdi-event"
            :rules="rules"
            :hide-details="noRules"
            v-on="on"
        ></v-text-field>
      </template>
      <v-time-picker v-model="time" :format="config.timeFormat" scrollable use-seconds @change="$refs.timeMenu.save(time)" >
      </v-time-picker>
    </v-menu>
  </v-row>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDateTimeField",
    props: ['props', 'updateStore'],
    data() {
      return {
        date: '',
        time: '',
        onDate: false,
        onTime: false,
        dateMenu: false,
        timeMenu: false
      }
    },
    computed: {
      action() {
        return this.props.attributes.hasOwnProperty('action') ? this.props.attributes.action : null
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
      alert() {
        return this.props.attributes.hasOwnProperty('alert') ? this.props.attributes.alert : null
      },
      amSelected() {
        return this.props.attributes.hasOwnProperty('amSelected') ? this.props.attributes.amSelected : ''
      },
      ampmName() {
        return this.props.attributes.hasOwnProperty('ampmName') ? this.props.attributes.ampmName : ''
      },
      class() {
        return this.props.attributes.hasOwnProperty('class') ? this.props.attributes.class : ''
      },
      className() {
        return this.props.attributes.hasOwnProperty('className') ? this.props.attributes.className : ''
      },
      classString() {
        return this.props.attributes.hasOwnProperty('classString') ? this.props.attributes.classString : ''
      },
      compositeType() {
        return this.props.attributes.hasOwnProperty('compositeType') ? this.props.attributes.compositeType : ''
      },
      config() {
        return {
          allowInput: true,
          enableTime: true,
          enableSeconds: true,
          timeFormat: this.isTwelveHour ? 'ampm' : '24hr'
        }
      },
      controls() {
        return {
          required: this.props.attributes.hasOwnProperty('required') && this.props.attributes.required.hasOwnProperty('requiredField') && this.props.attributes.required.requiredField === "true"
        }
      },
      datetime() {
        return this.date + ' ' + this.time
      },
      dateType() {
        return this.props.attributes.hasOwnProperty('datetype') ? this.props.attributes.datetype : 'default'
      },
      defaultDateTimeString() {
        return this.props.attributes.hasOwnProperty('defaultDateTimeString') ? this.props.attributes.defaultDateTimeString : ''
      },
      event() {
        return this.props.attributes.hasOwnProperty('event') ? this.props.attributes.event : ''
      },
      fieldTitle() {
        return this.props.attributes.hasOwnProperty('fieldTitle') ? this.props.attributes.fieldTitle : ''
      },
      fieldHelpText() {
        return this.props.attributes.hasOwnProperty('fieldHelpText') ? this.props.attributes.fieldHelpText : ''
      },
      formName() {
        return this.props.attributes.hasOwnProperty('formName') ? this.props.attributes.formName : ''
      },
      hour1() {
        return this.props.attributes.hasOwnProperty('hour1') ? this.props.attributes.hour1 : ''
      },
      hour2() {
        return this.props.attributes.hasOwnProperty('hour2') ? this.props.attributes.hour2 : ''
      },
      id() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : ''
      },
      isTwelveHour() {
        return this.props.attributes.hasOwnProperty('isTwelveHour') && this.props.attributes.isTwelveHour === 'Y' ? this.props.attributes.isTwelveHour : false
      },
      localizedIconTitle() {
        return this.props.attributes.hasOwnProperty('localizedIconTitle') ? this.props.attributes.localizedIconTitle : ''
      },
      mask() {
        return this.props.attributes.hasOwnProperty('mask') ? this.props.attributes.mask : ''
      },
      maxlength() {
        return this.props.attributes.hasOwnProperty('maxlength') ? this.props.attributes.maxlength : ''
      },
      minutes() {
        return this.props.attributes.hasOwnProperty('minutes') ? this.props.attributes.minutes : ''
      },
      name() {
        return this.props.attributes.hasOwnProperty('name') ? this.props.attributes.name : ''
      },
      noRules() {
        return this.controls.required === false
      },
      pmSelected() {
        return this.props.attributes.hasOwnProperty('pmSelected') ? this.props.attributes.pmSelected : ''
      },
      required() {
        return this.props.attributes.hasOwnProperty('required') ? this.props.attributes.required : {}
      },
      rules() {
        let rules = []
        if (this.controls.required) {
          rules.push((v) => !!v || 'This field is required')
        }
        return rules
      },
      shortDateInput() {
        return this.props.attributes.hasOwnProperty('shortDateInput') ? this.props.attributes.shortDateInput : ''
      },
      size() {
        return this.props.attributes.hasOwnProperty('size') ? this.props.attributes.size : ''
      },
      step() {
        return this.props.attributes.hasOwnProperty('step') ? this.props.attributes.step : ''
      },
      storeForm() {
        return {
          formId: this.formName,
          key: this.name,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      tabindex() {
        return this.props.attributes.hasOwnProperty('tabindex') ? this.props.attributes.tabindex : ''
      },
      timeDropdown() {
        return this.props.attributes.hasOwnProperty('timeDropdown') ? this.props.attributes.timeDropdown : ''
      },
      timeDropdownParamName() {
        return this.props.attributes.hasOwnProperty('timeDropdownParamName') ? this.props.attributes.timeDropdownParamName : ''
      },
      timeHourName() {
        return this.props.attributes.hasOwnProperty('timeHourName') ? this.props.attributes.timeHourName : ''
      },
      timeMinutesName() {
        return this.props.attributes.hasOwnProperty('timeMinutesName') ? this.props.attributes.timeMinutesName : ''
      },
      timeValues() {
        return this.props.attributes.hasOwnProperty('timeValues') ? this.props.attributes.timeValues : ''
      },
      title() {
        return this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      value() {
        return this.props.attributes.hasOwnProperty('value') ? this.props.attributes.value.split('.')[0] : ''
      }
    }
    ,
    mounted() {
      if (this.props.attributes.hasOwnProperty('value') && this.props.attributes.value !== '') {
        this.date = this.props.attributes.value.split(' ')[0]
        this.time = this.props.attributes.value.split(' ')[1].split('.')[0]
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    },
    watch: {
      datetime: function (newValue) {
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.formName,
          key: this.name,
          value: newValue
        })
      },
      props: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
        if (this.props.attributes.hasOwnProperty('value') && this.props.attributes.value !== '') {
          this.date = this.props.attributes.value.split(' ')[0]
          this.time = this.props.attributes.value.split(' ')[1].split('.')[0]
        }
      }
    }
  }
</script>

<style scoped>

</style>
