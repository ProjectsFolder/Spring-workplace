const path = require('path');
const MinifyPlugin = require ('babel-minify-webpack-plugin');
const MiniCssExtractPlugin = require ('mini-css-extract-plugin');

module.exports = {
      mode: 'development',
      entry: ['./assets/main.js', './assets/main.scss'],
      output: {
            path: path.resolve(__dirname + '/src/main/resources', 'build')
      },
      module: {
          rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            },
            {
                test: /\.(scss|css)$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: MiniCssExtractPlugin.loader
                    },
                    'css-loader',
                    'postcss-loader',
                    'sass-loader'
                ]
            }
        ]
    },
    plugins: [
        new MinifyPlugin({}, { comments: false }),
        new MiniCssExtractPlugin({ filename: '[name].css' }),
    ]
};
