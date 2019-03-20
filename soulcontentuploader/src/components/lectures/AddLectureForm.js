import React, {Component} from 'react';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import _ from "lodash";
import Progress from 'react-progressbar';
import TextField from 'material-ui/TextField'
import { BarLoader } from 'react-spinners';
import IconButton from 'material-ui/IconButton';
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import RaisedButton from 'material-ui/RaisedButton'

import SelectSpeakers from '../shared/SelectSpeakers';
import SelectPublishType from '../shared/SelectPublishType';
import SelectSect from '../shared/SelectSect';
import Lectures from '../../services/lectures'
import Quillpad from '../../services/quillpad'
import NameAutosuggest from '../shared/NameAutosuggest'
import DropzoneFiles from '../shared/DropzoneFiles';
import './lecture.css';

class AddLectureForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: {
        "en": "",
        "hi": ""
      },
      speaker: '',
      content: {
        "uploaded": "0",
        "file": {
          "name" : ""
        },
        "type": "audio_url",
        "fromUrl": ""
      },
      publishType: "PUBLISHED_UNVERIFIED",
      value: "",
      enLectures: [],
      hiLectures: [],
      lectures: [],
      uploadProgressPercent: 0,
      sect: ''
    };

    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSpeakerChange = this.handleSpeakerChange.bind(this);
    this.handleContentTypeChange = this.handleContentTypeChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleContentFromUrlChange = this.handleContentFromUrlChange.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.isValidAudioUrl = this.isValidAudioUrl.bind(this);
    this.isValidVideoUrl = this.isValidVideoUrl.bind(this);
    this.isContentTypeAudioUrl = this.isContentTypeAudioUrl.bind(this);
    this.isContentTypeVideoUrl = this.isContentTypeVideoUrl.bind(this);
    this.handleContentChange = this.handleContentChange.bind(this);
    this.updateUploadProgressPercent = this.updateUploadProgressPercent.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
  }

  componentDidMount() {
    Lectures.getAll()
      .then((response) => {
        var enLectures = []
        var hiLectures = []
        var lectures = []
        response.data.forEach((lecture) => {
          enLectures.push({name: lecture.name.en})
          hiLectures.push({name: lecture.name.hi})
          lectures.push({
            en: lecture.name.en,
            hi: lecture.name.hi
          })
        })
        this.setState({
          enLectures: _.uniqBy(enLectures, 'name'),
          hiLectures: _.uniqBy(hiLectures, 'name'),
          lectures: lectures
        })
      })
      .catch((err) => {
        alert(err)
      })
  }

  handlePublishTypeChange(event, index, value) {
    this.setState({publishType: value})
  }

  handleSpeakerChange(event, index, value) {
    this.setState({speaker: value})
  }

  handleSectChange(event, index, value) {
    this.setState({sect: value})
  }

  handleNameChange(id, newValue) {
    var name = 'name';
    var lang = id
    var curr_name = this.state.name
    curr_name[lang] = newValue
    if (lang === "en"){
      curr_name["hi"] = _.get(_.find(this.state.lectures, {'en': newValue}), "hi", curr_name["hi"])
    }
    this.setState({
      [name]: curr_name
    });
  }

  convertInHindi() {
    Quillpad.convertWords(this.state.name.en, "hindi", (hindiName) => {
      var curr_name = this.state.name
      curr_name['hi'] = hindiName
      this.setState({
        name: curr_name
      })
    })
  }

  handleContentTypeChange(event, val) {
    var curr_content = this.state.content
    var value = val;
    curr_content.type = value
    this.setState({
      "content": curr_content
    })
  }

  handleContentFromUrlChange(event) {
    var curr_content = this.state.content
    const target = event.target;
    var value = target.value;
    curr_content.fromUrl = value
    console.log(curr_content);
    this.setState({
      "content": curr_content
    })
  }

  handleContentChange(event) {
    var curr_content =  this.state.content
    curr_content.file = event[0]
    curr_content.uploaded = "2"
    this.setState({
      "content": curr_content
    })
  }

  matchYoutubeUrl(url) {
    var p = /^(?:https?:\/\/)?(?:m\.|www\.)?(?:youtu\.be\/|youtube\.com\/(?:embed\/|v\/|watch\?v=|watch\?.+&v=))((\w|-){11})(?:\S+)?$/;
    if (url.match(p)) {
      return url.match(p)[1];
    }
    return false;
  }

  isContentTypeAudioUrl(){
    return (this.state.content.type === "audio_url");
  }

  isContentTypeVideoUrl(){
    return (this.state.content.type === "video_url");
  }

  isContentTypeAudioUpload(){
    return (this.state.content.type === "audio_upload");
  }

  isValidAudioUrl() {
    return (this.isContentTypeAudioUrl())
  }

  isValidVideoUrl() {
    return (this.isContentTypeVideoUrl() && this.matchYoutubeUrl(this.state.content.fromUrl) !== false)
  }

  isValidAudioUpload(){
    return (this.isContentTypeAudioUpload() && !_.isEmpty(this.state.content.file))
  }

  updateUploadProgressPercent(percent) {
    this.setState({
      "uploadProgressPercent": percent
    });
  }

  handleSubmit(event) {
    if (this.isValidAudioUrl() || this.isValidVideoUrl()) {
      this.updateUploadProgressPercent(100)

      Lectures.add(this.state)
        .then((response) => {
          this.updateUploadProgressPercent(0)
          if(response["error"]) {
            alert(response["error"] + " Please retry.");
          } else {
            alert("Lecture: " + this.state.name.en + " uploaded successfully!");
          }
        })
        .catch(err => {
          this.updateUploadProgressPercent(0)
          alert(err + ". This simply means that things didn't go well. We'll debug soon for a better experience.");
        })
    } else if (this.isValidAudioUpload()){
      Lectures.addLectureWithAudio(this.state, this.updateUploadProgressPercent)
    } else {
      if (this.isContentTypeAudioUrl()) {
        alert("Please paste valid Url");
      } else if (this.isContentTypeVideoUrl()) {
        alert("Please paste youtube url");
      } else {
        alert("Please upload an audio file");
      }
    }
    event.preventDefault();
  }

  render() {
    let upload_status_text = "Upload"
    let button_status = false
    if (this.state.uploadProgressPercent > 0) {
      upload_status_text = "Uploading..."
      button_status = true
    }

    let placeholder_text = "Paste .mp3 url here";
    if (this.state.content.type === "video_url") {
      placeholder_text = "Paste youtube url here"
    }

    const styles = {
      block: {
        maxWidth: 250,
      },
      radioButton: {
        marginBottom: 16,
      },
      input: {
        width: 400,
      },
    };


    return (
      <div className="more-padding">
        <h2> Add Lecture </h2>
        <br />
        <form onSubmit={this.handleSubmit}>


          <div >
            <label>
              Name(English):
            </label>
            <NameAutosuggest
              id="en"
              placeholder=""
              onChange={this.handleNameChange}
              value={this.state.name.en}
              data={this.state.enLectures}
            />
          </div>
          <br />

          <div className='float-left'>
          <label>
            Name(Hindi):
          </label>

            <NameAutosuggest
              id="hi"
              placeholder=""
              onChange={this.handleNameChange}
              value={this.state.name.hi}
              data={this.state.hiLectures}
            />
          </div>

          <div >
            <IconButton onTouchTap={this.convertInHindi} tooltip="Translate from English to Hindi">
              <GTranslate />
            </IconButton>
          </div>

          <div>
          <SelectSect  name="Sect" value={this.state.sect} onChange={this.handleSectChange} />
          </div>

          <br />

          <div>
            <SelectSpeakers name="Speaker" value={this.state.speaker}  onChange={this.handleSpeakerChange} />
          </div>
          <br />

          <label >
            Content Type:
          </label>
          <RadioButtonGroup name="content_type" valueSelected={this.state.content.type} onChange={this.handleContentTypeChange}>

              <RadioButton value="audio_url" label="Audio URL" style={styles.radioButton} />
              <RadioButton value="video_url" label="Video URL" style={styles.radioButton} />
              <RadioButton value="audio_upload" label="Audio Upload" style={styles.radioButton} />

          </RadioButtonGroup>

          { (this.state.content.type === "audio_url" || this.state.content.type === "video_url") &&
            <div>
              <TextField style={styles.input} floatingLabelText="URL" name="from_url" placeholder={placeholder_text} value={this.state.content.fromUrl} onChange={this.handleContentFromUrlChange} required/>
            </div>
          }
          {
            this.state.content.type === "audio_upload" &&
            <div>
              <DropzoneFiles name="content" fileTypes="audio/*" onChange={this.handleContentChange} uploadStatus={this.state.content.uploaded} uploadFileName={this.state.content.file.name} />
            </div>
          }
          <br />


          <div>
            <SelectPublishType name="Publish Type" value="PUBLISHED_UNVERIFIED" onChange={this.handlePublishTypeChange}/>
          </div>
          <br />

          { (this.state.uploadProgressPercent > 0 && this.state.uploadProgressPercent < 100) &&
            <div>
              <p> Step 1/2 : Uploading File {this.state.uploadProgressPercent}% </p>
              <Progress completed={this.state.uploadProgressPercent} />
              <br />
            </div>
          }
          { (this.state.uploadProgressPercent === 100) &&
            <div>
              <p> Step 2/2 : Converting file format </p>
              <BarLoader
                color={'#7ED321'}
              />
              <br />
            </div>
          }
          <RaisedButton type="Submit" primary={true} label={upload_status_text} disabled={button_status} />
        </form>
      </div>
    );
  }
}

export default AddLectureForm;
