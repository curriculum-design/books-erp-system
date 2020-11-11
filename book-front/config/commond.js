const {args, config} = require('../build/command-params.js');
module.exports = {
  projectDir: 'project',
  projectName: args[0],
  config: config
}
