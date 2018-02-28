const webpack = require('webpack');
const PRODUCTION = process.env.BUILD_ENV === 'production';

module.exports = {
    entry: './app/framework/assets/js/application.ts',
    output: {
        filename: (PRODUCTION ? "./app/framework/assets/js/application-all.min.js" : "./app/framework/assets/js/application-all.js"),
        path: __dirname
    },
    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        }

    },
    module: {
        loaders: [
            {test: /\.vue$/, loader: 'vue-loader'},
            {test: /\.ts$/, loader: 'ts-loader'}
        ]
    },

    plugins:
        (PRODUCTION ?
                [
                    new webpack.DefinePlugin({PRODUCTION: JSON.stringify(true)}),
                    new webpack.DefinePlugin({'process.env': {NODE_ENV: JSON.stringify(process.env.BUILD_ENV)}}),
                    new webpack.optimize.UglifyJsPlugin(),
                    new webpack.optimize.OccurrenceOrderPlugin(),
                    new webpack.ProvidePlugin({$: 'jquery', jQuery: 'jquery', Vue: "vue"})
                ] :
                [
                    new webpack.DefinePlugin({PRODUCTION: JSON.stringify(true)}),
                    new webpack.DefinePlugin({'process.env': {NODE_ENV: JSON.stringify(process.env.BUILD_ENV)}}),
                    new webpack.ProvidePlugin({$: 'jquery', jQuery: 'jquery', Vue: "vue"}),

                ]
        ),
    cache: true
};

