const Vue = require("vue").default;
const TopVue = require("./components/Top.vue").default;

new Vue({
    render: h => h(TopVue)
}).$mount("#top");
