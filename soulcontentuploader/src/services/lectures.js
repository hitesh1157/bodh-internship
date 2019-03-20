import * as constants from '../config/Constants';
import ServiceHelper from './helper'
import $ from 'jquery';

class Lectures {
  add(details) {
    var url = ""
    if (details.content.type === "audio_url") {
      url = constants.CREATE_AUDIO_LECTURE
    } else if (details.content.type === "video_url") {
      url = constants.CREATE_VIDEO_LECTURE
    }

    var form_data = new FormData()
    form_data.append("name", details.name.en);
    form_data.append("name-en", details.name.en);
    form_data.append("name-hi", details.name.hi);
    form_data.append("speakerId", details.speaker);
    form_data.append("publishType", details.publishType);
    form_data.append("sect", details.sect);
    form_data.append("url", details.content.fromUrl);

    var options = {
      body: form_data,
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
	}

  addLectureWithAudio(details, updateUploadProgressPercent) {
    var url = constants.CREATE_LECTURE_WITH_AUDIO

    var form_data = new FormData()
    form_data.append("name", details.name.en);
    form_data.append("name-en", details.name.en);
    form_data.append("name-hi", details.name.hi);
    form_data.append("speakerId", details.speaker);
    form_data.append("audio", details.content.file);
    form_data.append("publishType", details.publishType);
    form_data.append("sect", details.sect);
    $.ajax({
      type: "POST",
      url: url,
      dataType: 'json',
      contentType: false,
      data: form_data,
      processData: false,
      timeout: 3000*60*60,
      xhr: function() {
        var xhr = new XMLHttpRequest();;
        xhr.upload.addEventListener( 'progress', function( e ) {
          if( e.lengthComputable ) {
            var loaded = e.loaded;
            var total = e.total;
            var progressValue = Math.round( ( loaded / total ) * 100 );
            updateUploadProgressPercent(progressValue)
          }
        }, false );
        xhr.upload.addEventListener( 'load', function( e ) {
          updateUploadProgressPercent(100)
        }, false );
        return xhr;
      },
      success: function(data) {
        updateUploadProgressPercent(0)
        alert("Lecture: " + details.name.en + " uploaded successfully!")
      },
      error: function(xhr, status, err) {
        alert(err);
        updateUploadProgressPercent(0)
      }
    });
  }

  get(id) {
    var url = constants.GET_LECTURE + id
    return ServiceHelper.doGetCall(url);
  }

  getAll(){
    var url =  constants.GET_ALL_LECTURES
    return ServiceHelper.doGetCall(url);
  }

  getAllUnassigned(){
    var url =  constants.GET_ALL_UNASSIGNED_LECTURES
    return ServiceHelper.doGetCall(url);
  }

  getAllEligibleForPlaylist(playlist_id){
    var url =  constants.GET_ALL_LECTIRES_ELIGIBLE_FOR_PLAYLIST + playlist_id
    return ServiceHelper.doGetCall(url);
  }

  update(details){
    var url = constants.UPDATE_LECTURE + details.id;
    var info = {
      "name": details.name,
      "speaker": {"id": details.speaker},
      "thumbnail": details.thumbnail.url,
      "audio": details.content.url,
      "publishType": details.publishType,
      "sect": details.sect
    }
    var options = {
      body: info,
      type: 'json'
    }
    return ServiceHelper.doPutCall(url, options);
  }
}

export default new Lectures();
