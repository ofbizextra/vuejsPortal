import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'mdi',
  },
  theme: {
    dark: true,
    themes: {
      light: {
        primary: '#507088',
        secondary: '#588098',
        accent: '#e05d30',
        success: '#64dd17',
        error: '#f44336'
      },
      dark: {
        primary: '#507088',
        secondary: '#588098',
        accent: '#e05d30',
        success: '#64dd17',
        error: '#f44336'
      }
    }
  }
});
