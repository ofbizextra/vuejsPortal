import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'mdiSvg',
  },
  theme: {
    dark: false,
    themes: {
      light: {
        primary: '#cdcfd0',
        secondary: '#999',
        accent: '#020617',
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
