<template>
  <div id="vue-date-time-field">
    <flat-pickr v-model="valueStore" :config="config"></flat-pickr>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FlatPickr from 'vue-flatpickr-component/src/component'

  export default {
    name: "VueDateTimeField",
    components: {FlatPickr},
    props: ['props', 'updateStore'],
    data() {
      return {
        dateTime: ''
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
      valueStore: {
        get() {
          return this.getDataFromForm(this.storeForm)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.formName,
            key: this.name,
            value: value
          })
        }
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
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
        return this.data.hasOwnProperty('isTwelveHour') ? this.data.isTwelveHour : false
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
          dateFormat: 'Y-m-d H:i:S',
          enableTime: true,
          enableSeconds: true,
          minuteIncrement: this.step,
          'time_24hr': !this.isTwelveHour,
        }
      }
    }
    ,
    watch: {
      data: function (from, to) {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    }
    ,
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
