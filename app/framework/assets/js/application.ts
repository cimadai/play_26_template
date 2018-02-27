const Vue = require("vue").default;
const TopVue = require("./components/Top.vue").default;

$(function () {
    console.log("hello");
    new Vue({
        render: h => h(TopVue)
    }).$mount("#top");
});
