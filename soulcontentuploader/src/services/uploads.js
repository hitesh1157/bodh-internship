import * as constants from '../config/Constants';
import ServiceHelper from './helper';

class Uploads {
  addThumbnail(details, thumb) {

    var url = constants.UPLOAD_THUMBNAIL;
    var form_data = new FormData();

    form_data.append("file", thumb);
    form_data.append("name", details.name.en);
  
    var options = {
      body: form_data,
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
  }

  addAudio(details) {
    var url = constants.UPLOAD_AUDIO
    var form_data = new FormData()
    form_data.append("file", details.content.file);
    form_data.append("name", details.name.en);
    var options = {
      body: form_data,
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
  }

  copyAudioFromUrl(details) {
    var url = constants.COPY_AUDIO_FROM_URL
    var info = {
      "url": details.content.fromUrl,
      "filename": details.name.en
    }
    
    var options = {
      body: info,
      type: 'json',
      timeout: 3000 * 60 * 60
    };

    return ServiceHelper.doPostCall(url, options);
  }
}

export default new Uploads();
