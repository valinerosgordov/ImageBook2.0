const env = process.env.NODE_ENV

import devConfig from './dev.config'
import prodConfig from './prod.config'

let config = {
  baseTimeout: 5000,  // TODO
  uploadTimeout: 1*3600*1000 // 1h
}

if (env == 'development')
  config = { ...config, ...devConfig }
else if (env == 'production')
  config = { ...config, ...prodConfig }
else
  throw "Unknown env: " + env

export default config

