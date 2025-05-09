const axios = require('axios')

import config from '@/config/config'

const api = axios.create({
  baseURL: config.apiUrl,
  timeout: config.baseTimeout
})

api.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    return Promise.reject(error);
  });

api.setAuthToken = function (token) {
  this.token = token
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

api.getImageUrl = function (url) {
  return config.apiUrl + url + "?token=" + this.token
}

// TODO do we need this everywhere or in sheets only?
api.getUniqueImageUrl = function (url) {
  return api.getImageUrl(url) + "&" + uuidv4()
}

api.getBase64 = async function (url) {
  const response = await api.get(url, { responseType: 'arraybuffer' })
  return new Buffer(response, 'binary').toString('base64')
}

api.upload = async function (url, file, uploadProgress) {
  const formData = new FormData()
  formData.append("file", file, file.name)
  return await api.post(url, formData, {
    timeout: config.uploadTimeout,
    onUploadProgress: uploadProgress
  })
}

api.postMultipart = async function (url, data, files, uploadProgress) {
  const formData = new FormData()

  for (let [key, value] of Object.entries(data)) {
    const json = JSON.stringify(value);
    const blob = new Blob([json], { type: 'application/json' });
    formData.append(key, blob)
  }

  for (let [key, file] of Object.entries(files)) {
    if (!file) continue

    formData.append(key, file, file.name)
  }

  return await api.post(url, formData, {
    timeout: config.uploadTimeout,
    onUploadProgress: uploadProgress
  })
}

export default api
