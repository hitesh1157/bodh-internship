import * as constants from '../config/Constants';
import ServiceHelper from './helper'

class Categories {

  add(details) {
    var url = constants.CREATE_CATEGORY
    var selected_playlist_ids = []
    details.selectedPlaylists.forEach(function(item, index){
      selected_playlist_ids.push(item.id)
    })
    var info = {
      "name": details.name,
      "playlistIds": selected_playlist_ids,
      "publishType": details.publishType,
      "introduction": details.introduction,
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
    var url = constants.GET_CATEGORY + id
    return ServiceHelper.doGetCall(url);
  }

  getAll(){
    var url =  constants.GET_ALL_CATEGORIES
    return ServiceHelper.doGetCall(url);
  }

  update(details){
    var url = constants.UPDATE_CATEGORY + details.id;
    var selected_playlist_ids = []
    details.selectedPlaylists.forEach(function(item, index){
      selected_playlist_ids.push(item.id)
    })
    var info = {
      "name": details.name,
      "playlistIds": selected_playlist_ids,
      "publishType": details.publishType,
      "introduction": details.introduction,
      "position": details.position,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json'
    }

    return ServiceHelper.doPutCall(url, options);
  }


  updateOrder(details){
    var url = constants.UPDATE_CATEGORY_REORDER;
    var selected_category_ids = []
    details.selectedCategories.forEach(function(item, index){
      selected_category_ids.push(item.id)
    })
    var info = {
      "Ids": selected_category_ids
    }
    var options = {
      body: info,
      type: 'json'
    }
    return ServiceHelper.doPutCall(url, options);

  }

}

export default new Categories();
