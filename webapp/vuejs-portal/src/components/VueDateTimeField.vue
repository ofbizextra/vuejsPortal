<template>
  <v-row id="vue-date-time-field">
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
        // dateTime: '',
        date: '',
        time: '',
        onDate: false,
        onTime: false,
        dateMenu: false,
        timeMenu: false
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      storeForm() {
        return {
          formId: this.formName,
          key: this.name,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      // valueStore: {
      //   get() {
      //     return this.getDataFromForm(this.storeForm)
      //   },
      //   set(value) {
      //
      //   }
      // },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
      datetime() {
        return this.date + ' ' + this.time
      },
      action() {
        return this.data.hasOwnProperty('action') ? this.data.action : null
      },
      alert() {
        return this.data.hasOwnProperty('alert') ? this.data.alert : null
      },
      amSelected() {
        return this.data.hasOwnProperty('amSelected') ? this.data.amSelected : ''
      },
      ampmName() {
        return this.data.hasOwnProperty('ampmName') ? this.data.ampmName : ''
      },
      class() {
        return this.data.hasOwnProperty('class') ? this.data.class : ''
      },
      className() {
        return this.data.hasOwnProperty('className') ? this.data.className : ''
      },
      classString() {
        return this.data.hasOwnProperty('classString') ? this.data.classString : ''
      },
      compositeType() {
        return this.data.hasOwnProperty('compositeType') ? this.data.compositeType : ''
      },
      dateType() {
        return this.data.hasOwnProperty('datetype') ? this.data.datetype : 'default'
      },
      defaultDateTimeString() {
        return this.data.hasOwnProperty('defaultDateTimeString') ? this.data.defaultDateTimeString : ''
      },
      event() {
        return this.data.hasOwnProperty('event') ? this.data.event : ''
      },
      formName() {
        return this.data.hasOwnProperty('formName') ? this.data.formName : ''
      },
      hour1() {
        return this.data.hasOwnProperty('hour1') ? this.data.hour1 : ''
      },
      hour2() {
        return this.data.hasOwnProperty('hour2') ? this.data.hour2 : ''
      },
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      isTwelveHour() {
        return this.data.hasOwnProperty('isTwelveHour') && this.data.isTwelveHour === 'Y' ? this.data.isTwelveHour : false
      },
      localizedIconTitle() {
        return this.data.hasOwnProperty('localizedIconTitle') ? this.data.localizedIconTitle : ''
      },
      mask() {
        return this.data.hasOwnProperty('mask') ? this.data.mask : ''
      },
      maxlength() {
        return this.data.hasOwnProperty('maxlength') ? this.data.maxlength : ''
      },
      minutes() {
        return this.data.hasOwnProperty('minutes') ? this.data.minutes : ''
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
      },
      pmSelected() {
        return this.data.hasOwnProperty('pmSelected') ? this.data.pmSelected : ''
      },
      required() {
        return this.data.hasOwnProperty('required') ? this.data.required : {}
      },
      shortDateInput() {
        return this.data.hasOwnProperty('shortDateInput') ? this.data.shortDateInput : ''
      },
      size() {
        return this.data.hasOwnProperty('size') ? this.data.size : ''
      },
      step() {
        return this.data.hasOwnProperty('step') ? this.data.step : ''
      },
      tabindex() {
        return this.data.hasOwnProperty('tabindex') ? this.data.tabindex : ''
      },
      timeDropdown() {
        return this.data.hasOwnProperty('timeDropdown') ? this.data.timeDropdown : ''
      },
      timeDropdownParamName() {
        return this.data.hasOwnProperty('timeDropdownParamName') ? this.data.timeDropdownParamName : ''
      },
      timeHourName() {
        return this.data.hasOwnProperty('timeHourName') ? this.data.timeHourName : ''
      },
      timeMinutesName() {
        return this.data.hasOwnProperty('timeMinutesName') ? this.data.timeMinutesName : ''
      },
      timeValues() {
        return this.data.hasOwnProperty('timeValues') ? this.data.timeValues : ''
      },
      title() {
        return this.data.hasOwnProperty('title') ? this.data.title : ''
      },
      value() {
        return this.data.hasOwnProperty('value') ? this.data.value.split('.')[0] : ''
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
          required: this.data.hasOwnProperty('required') && this.data.required.hasOwnProperty('requiredField') && this.data.required.requiredField === "true"
        }
      },
      noRules() {
        return this.controls.required === false
      },
      rules() {
        let rules = []
        if (this.controls.required) {
          rules.push((v) => !!v || 'This field is required')
        }
        return rules
      }
    }
    ,
    watch: {
      data: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
        if (this.props.attributes.hasOwnProperty('value') && this.props.attributes.value !== '') {
          this.date = this.props.attributes.value.split(' ')[0]
          this.time = this.props.attributes.value.split(' ')[1].split('.')[0]
        }
      },
      datetime: function (newValue) {
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.formName,
          key: this.name,
          value: newValue
        })
      }
    }
    ,
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    },
    mounted() {
      if (this.props.attributes.hasOwnProperty('value') && this.props.attributes.value !== '') {
        this.date = this.props.attributes.value.split(' ')[0]
        this.time = this.props.attributes.value.split(' ')[1].split('.')[0]
      }
    }
  }
</script>

<style scoped>

</style>
