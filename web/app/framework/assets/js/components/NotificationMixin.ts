/**
 * Usage:
 * require("./components/NotificationMixin");
 * require once in top level script.
 */
import Vue from "vue";
import Notification from "vue-notification";
Vue.use(Notification);

// vueクラスに読み込ませるインターフェース
declare module 'vue/types/vue' {
    export interface Vue {
        // 以下のVue.mixinで実装している。
        error: (options) => void;
        // "vue-notification/src/index.js" で `Vue.prototype.$notify = (params) => { ... }` してる。
        $notify: (options) => void;
    }
}

Vue.mixin({
    methods: {
        error: function (options: {title: string, text: string}) {
            let opt = {
                type: "error",
                title: options.title,
                text: options.text
            };
            this.$notify(opt);
        }
    }
});
