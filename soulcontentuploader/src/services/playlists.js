import * as constants from '../config/Constants';
import ServiceHelper from './helper'

class Playlists {
  add(details) {
    var url = constants.CREATE_PLAYLIST
    var selected_lecture_ids = []
    details.selectedLectures.forEach(function(item, index){
      selected_lecture_ids.push(item.id)
    })
    var info = {
      "name": details.name,
      "speakerId": details.speaker,
      "lectureIds": selected_lecture_ids,
      "publishType": details.publishType,
      "playlistType": details.playlistType,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json',
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
	}

  get(id) {
    var url = constants.GET_PLAYLIST + id
    return ServiceHelper.doGetCall(url);
  }

  getAll(){
    var url =  constants.GET_ALL_PLAYLISTS
    return ServiceHelper.doGetCall(url);
  }

  update(details){
    var selected_lecture_ids = []
    details.selectedLectures.forEach(function(item, index){
      selected_lecture_ids.push(item.id)
    })
    var url = constants.UPDATE_PLAYLIST + details.id;
    var info = {
      "name": details.name,
      "speakerId": details.speaker,
      "lectureIds": selected_lecture_ids,
      "publishType": details.publishType,
      "playlistType": details.playlistType,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json'
    }

    return ServiceHelper.doPutCall(url, options);
  }
}

export default new Playlists();
