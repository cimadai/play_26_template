const webpack = require('webpack');
const PRODUCTION = process.env.BUILD_ENV === 'production';

module.exports = {
    entry: './app/framework/assets/js/application.ts',
    output: {
        filename: (PRODUCTION ? "./app/framework/assets/js/application-all.min.js" : "./app/framework/assets/js/application-all.js"),
        path: __dirname
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.vue'],
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        },
        modules: ["node_modules"]
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: {
                    loaders: {
                        // Since sass-loader (weirdly) has SCSS as its default parse mode, we map
                        // the "scss" and "sass" values for the lang attribute to the right configs here.
                        // other preprocessors should work out of the box, no loader config like this necessary.
                        'scss': 'vue-style-loader!css-loader!sass-loader',
                        'sass': 'vue-style-loader!css-loader!sass-loader?indentedSyntax',
                    }
                    // other vue-loader options go here
                }
            },
            {
                test: /\.tsx?$/,
                loader: 'ts-loader',
                exclude: /node_modules/,
                options: {
                    appendTsSuffixTo: [/\.vue$/]
                }
            }
        ]
    },

    plugins:
        (PRODUCTION ?
                [
                    new webpack.DefinePlugin({PRODUCTION: JSON.stringify(true)}),
                    new webpack.DefinePlugin({'process.env': {NODE_ENV: JSON.stringify(process.env.BUILD_ENV)}}),
                    new webpack.optimize.UglifyJsPlugin(),
                    new webpack.optimize.OccurrenceOrderPlugin()
                ] :
                [
                    new webpack.DefinePlugin({PRODUCTION: JSON.stringify(true)}),
                    new webpack.DefinePlugin({'process.env': {NODE_ENV: JSON.stringify(process.env.BUILD_ENV)}})

                ]
        ),
    cache: true
};

