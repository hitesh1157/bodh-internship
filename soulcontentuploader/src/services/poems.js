import * as constants from '../config/Constants';
import ServiceHelper from './helper'

class Poems {
  add(details) {
    var url = constants.CREATE_POEM
    var info = {
      "name": details.name,
      "poemType": details.poemType,
      "text": details.contentText,
      "publishType": details.publishType
    }

    if (details.audio !== ""){
      info["audio"] = details.audio
    }
    var options = {
      body: info,
      type: 'json',
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
	}

  getAll() {
    return ServiceHelper.doGetCall(constants.GET_ALL_POEMS);
  }

  get(id) {
    var url = constants.GET_POEM + id
    return ServiceHelper.doGetCall(url);
  }

  update(details){

    var url = constants.UPDATE_POEM + details.id;
    var info = {
      "name": details.name,
      "poemType": details.poemType,
      "text": details.contentText,
      "publishType": details.publishType
    }
    
    if (details.audio !== ""){
      info["audio"] = details.audio
    }

    var options = {
      body: info,
      type: 'json'
    }

    return ServiceHelper.doPutCall(url, options);
  }
}

export default new Poems();
