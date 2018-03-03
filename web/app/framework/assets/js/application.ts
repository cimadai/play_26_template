const Vue = require("vue").default;
const TopVue = require("./components/Top.vue").default;

require("./components/NotificationMixin");

new Vue({
    render: h => h(TopVue)
}).$mount("#top");
