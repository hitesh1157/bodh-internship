import * as constants from '../config/Constants';
import ServiceHelper from './helper'

class Speakers {
  add(details) {
    var url = constants.CREATE_SPEAKER
    var info = {
      "name": details.name,
      "speakerType": details.speakerType,
      "thumbnail": details.thumbnail.url,
      "website": details.website,
      "androidApp": details.androidApp,
      "iOSApp": details.iOSApp,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json',
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
	}

  getAll() {
    return ServiceHelper.doGetCall(constants.GET_ALL_SPEAKERS);
  }

  get(id) {
    var url = constants.GET_SPEAKER + id
    return ServiceHelper.doGetCall(url);
  }

  update(details){
    var url = constants.UPDATE_SPEAKER + details.id;
    var info = {
      "name": details.name,
      "speakerType": details.speakerType,
      "thumbnail": details.thumbnail.url,
      "website": details.website,
      "androidApp": details.androidApp,
      "iOSApp": details.iOSApp,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json'
    }

    return ServiceHelper.doPutCall(url, options);
  }
}

export default new Speakers();
