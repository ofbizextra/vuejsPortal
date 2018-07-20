export default {
  apiUrl: 'https://localhost:8443/exampleapi/control',
  formApiUrl: 'https://localhost:8443/example/control',
  listForDropDown: {path: '/listForDropDown', tokenRequired: false},
  main: {path: '/main', tokenRequired: false},
  editExampleLayer: {path: '/EditExampleLayer', tokenRequired: false},
  authentication: {path: '/authentification', tokenRequired: false},
  checkAuthToken: {path: '/checkAuthToken', tokenRequired: true},
  login: {path: '/login', tokenRequired: false}
}
