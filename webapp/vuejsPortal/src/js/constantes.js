export default {
  apiUrl: 'https://localhost:8443/exampleapi/control',
  formApiUrl: 'https://localhost:8443/example/control',
  hostUrl: 'https://localhost:8443',
  listForDropDown: {path: '/listForDropDown', tokenRequired: false},
  main: {path: '/main', tokenRequired: false},
  editExampleLayer: {path: '/EditExampleLayer', tokenRequired: false},
  authentication: {path: '/authentification', tokenRequired: false},
  checkAuthToken: {path: '/checkAuthToken', tokenRequired: true},
  login: {path: '/login', tokenRequired: false},
  ping: {path: '/ping', tokenRequired: false},
  ajaxCheckLogin: {path: '/ajaxCheckLogin', tokenRequired: false},
  findExampleLayer: {path: '/FindExampleLayer', tokenRequired: false},
  listExampleLayer: {path: '/ListExampleLayer', tokenRequired: false},
  portalPageDetail: {path: '/portalPageDetail', tokenRequired: false}
}
