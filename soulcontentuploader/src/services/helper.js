import 'whatwg-fetch'
import Account from './account'

class ServiceHelper {

  checkCookie(name) {
    let regExp = new RegExp( '(?:^' + name + '|;\\s*' + name + ')=(.*?)(?:;|$)', 'g' );
    return regExp.test(window.document.cookie);
  }

  getCookie(name) {
    if (typeof window.document === "undefined") return '';
    name = encodeURIComponent(name);
    if (this.checkCookie(name)) {
      let regexp = new RegExp('(?:^' + name + '|;\\s*' + name + ')=(.*?)(?:;|$)', 'g');
      let result = regexp.exec(window.document.cookie);
      return decodeURIComponent(result[1]);
    } else {
      return '';
    }
	}

  doCall(url, options) {
    if (!options.noauth) {
      options.headers = options.headers || {};
      options.headers.Authorization = 'Bearer ' + Account.token;
    }
    return fetch(url, options).then(response => response.json());
  }

  doGetCall(url, options = {}) {
    return this.doCall(url, options);
  }

  doPostCall(url, options) {
    let callOptions = {
      method: 'POST'
    }
    if (options.type === 'json') {
      callOptions.headers = {
        'Content-Type': 'application/json'
      }
      callOptions.body = JSON.stringify(options.body)
    } else {
      callOptions.body = options.body
    }
    return this.doCall(url, callOptions)
  }

  doPutCall(url, options) {
    let callOptions = {
      method: 'PUT'
    }
    if (options.type === 'json') {
      callOptions.headers = {
        'Content-Type': 'application/json'
      }
      callOptions.body = JSON.stringify(options.body)
    } else {
      callOptions.body = options.body
    }
    return this.doCall(url, callOptions)
  }

  doDeleteCall(url, options) {

  }


}

export default new ServiceHelper();
