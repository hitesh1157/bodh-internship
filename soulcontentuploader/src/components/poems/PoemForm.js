import React, { Component } from 'react';
import TextField from 'material-ui/TextField'
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import RaisedButton from 'material-ui/RaisedButton'
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import _ from "lodash";
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import IconButton from 'material-ui/IconButton';

import Quillpad from '../../services/quillpad'
import DropzoneFiles from '../shared/DropzoneFiles';
import Poems from '../../services/poems';
import * as constants from '../../config/Constants';
import SelectPublishType from '../shared/SelectPublishType';
import Uploads from '../../services/uploads';

// import './poem.css'

class PoemForm extends Component {
   constructor(props) {
    super(props);
    this.state = {
      id: this.props.match.params.id,
      name: {
        "en": "",
        "hi": ""
      },
      poemType: '',
      contentText: "",
      content: {
        uploaded: "0",
        file: {
          name : ""
        },
        type: "none",
        fromUrl: ""
      },
      audio: "",
      publishType: "PUBLISHED_UNVERIFIED",
      uploadProgressPercent: 0
    };

    this.handleNameChange = this.handleNameChange.bind(this);
    this.handlePoemTypeChange = this.handlePoemTypeChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleContentTextChange = this.handleContentTextChange.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.isValidAudioUrl = this.isValidAudioUrl.bind(this);
    this.isContentTypeAudioUrl = this.isContentTypeAudioUrl.bind(this);
    this.handleContentChange = this.handleContentChange.bind(this);
    this.handleContentTypeChange = this.handleContentTypeChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleContentFromUrlChange = this.handleContentFromUrlChange.bind(this);
    this.updateUploadProgressPercent = this.updateUploadProgressPercent.bind(this);
    this.updatePoem = this.updatePoem.bind(this);
    this.addPoem = this.addPoem.bind(this);
    this.addOrUpdatePoem = this.addOrUpdatePoem.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
  }

  componentDidMount() {
    if (this.state.id) {
      Poems.get(this.state.id)
      .then((response) => {
        var old_data = response.data;
        var old_data_state = {
          "name": old_data.name,
          "poemType": old_data.poemType,
          "contentText": old_data.text,
          "publishType": old_data.publishType,
          "audio": old_data.audio
        }
        this.setState(old_data_state);
      })
      .catch(err => {
        alert(err);
      })
    }
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

  handlePoemTypeChange(event, index, value) {
    this.setState({poemType: value})
  }

  handleNameChange(event, val) {
    let currentName = this.state.name;
    currentName[event.target.name] = val;
    this.setState({
      name: currentName
    })
  }

  handleContentTextChange(event, val){
    this.setState({
      contentText: val
    })
  }

  handleContentFromUrlChange(event) {
    var curr_content = this.state.content
    const target = event.target;
    var value = target.value;
    curr_content.fromUrl = value
    this.setState({
      "content": curr_content
    })
  }

  handlePublishTypeChange(event, index, value) {
    this.setState({"publishType": value});
  }

  handleContentTypeChange(event, val) {
    var curr_content = this.state.content
    var value = val;
    curr_content.type = value
    this.setState({
      content: curr_content
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

  isContentTypeAudioUrl(){
    return (this.state.content.type === "audio_url");
  }

  isContentTypeAudioUpload(){
    return (this.state.content.type === "audio_upload");
  }

  isValidAudioUrl() {
    return (this.isContentTypeAudioUrl() && ['.mp3', '.wma', '.m4a', '.wav'].includes(this.state.content.fromUrl.substr(-4).toLowerCase()))
  }

  isValidAudioUpload(){
    return (this.isContentTypeAudioUpload() && !_.isEmpty(this.state.content.file))
  }

  updateUploadProgressPercent(percent) {
    this.setState({
      "uploadProgressPercent": percent
    });
  }

  updatePoem() {
    Poems.update(this.state)
    .then((response) => {
      this.updateUploadProgressPercent(0)
      alert("Poem: " + this.state.name.en + " updated successfully!")
    })
    .catch(err => {
      alert(err);
    })
  }

  addPoem() {
    Poems.add(this.state)
    .then((response) => {
      this.updateUploadProgressPercent(0)
      alert("Poem: " + this.state.name.en + " created successfully!")
    })
    .catch(err => {
      alert(err);
    })
  }

  addOrUpdatePoem(addOrUpdateMethod) {
    if (this.state.content.type === "none"){
      addOrUpdateMethod()
    } else {
      if(this.isValidAudioUrl()){
        Uploads.copyAudioFromUrl(this.state)
        .then((response) => {
          this.setState({
            audio: response.data.location
          })
        })
        .then( addOrUpdateMethod )
      } else if (this.isValidAudioUpload()){
        Uploads.addAudio(this.state)
        .then((response) => {
          this.setState({
            audio: response.data.location
          })
        })
        .then( addOrUpdateMethod )
      } else {
        this.updateUploadProgressPercent(0)
        if (this.isContentTypeAudioUrl()) {
          alert("Please paste url of a mp3 file, having .mp3 extension");
        } else {
          alert("Please upload an audio file");
        }
      }
    }
  }

  handleSubmit(event) {
    this.updateUploadProgressPercent(100)
    if (this.state.id) {
      this.addOrUpdatePoem(this.updatePoem)
    } else {
      this.addOrUpdatePoem(this.addPoem)
    }
    event.preventDefault();
  }

  render() {
    let poemTypes = constants.POEM_TYPES;
    let poemTypesList = [];
    poemTypes.forEach(function(poemType, index) {
      poemTypesList.push(<MenuItem value={poemType} key={index} primaryText={poemType} />);
    })

    const textStyle = {
      width: '100%'
    }

    const styles = {
      block: {
        maxWidth: 250,
      },
      radioButton: {
        marginBottom: 16,
      },
    };

    let placeholder_text = "Paste .mp3 url here";
    let upload_status_text = "Upload"
    let button_status = false
    if (this.state.uploadProgressPercent > 0) {
      upload_status_text = "Uploading..."
      button_status = true
    }

    return (
      <div className="more-padding">
        <h2> {this.state.id ? 'Edit' : 'Add'} Poem: </h2>
        <form onSubmit={this.handleSubmit}>
          <TextField
            style={textStyle}
            name='en'
            value={this.state.name.en}
            onChange={this.handleNameChange}
            floatingLabelText="Name(English)"
            required
          />
          <div className='float-left'>
          <TextField
            style={textStyle}
            name='hi'
            value={this.state.name.hi}
            onChange={this.handleNameChange}
            floatingLabelText="Name(Hindi)"
            required
          />
          </div>

          <div >
            <IconButton onTouchTap={this.convertInHindi} tooltip="Translate from English to Hindi">
              <GTranslate />
            </IconButton>
          </div>

          <br />
          <SelectField
            style={textStyle}
            floatingLabelText='Poem Type' 
            value={this.state.poemType}
            onChange={this.handlePoemTypeChange}
            required
          >{poemTypesList}</SelectField>
          <br />
          <div>
          <TextField floatingLabelText="ContentText" style={textStyle} multiLine={true} name="contentText" value={this.state.contentText} onChange={this.handleContentTextChange} />
          </div>
          <br></br>
          <label >
            Content Type:
          </label>
          <RadioButtonGroup name="content_type" valueSelected={this.state.content.type} onChange={this.handleContentTypeChange}>
            <RadioButton value="none" label="None" style={styles.radioButton} />
            <RadioButton value="audio_url" label="Audio URL" style={styles.radioButton} />
            <RadioButton value="audio_upload" label="Audio Upload" style={styles.radioButton} />
          </RadioButtonGroup>

          { (this.state.content.type === "audio_url") && 
            <div>
              <TextField floatingLabelText="URL" name="from_url" placeholder={placeholder_text} value={this.state.content.fromUrl} onChange={this.handleContentFromUrlChange} required/>
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
          <br />
          <RaisedButton type="Submit" primary={true} label={upload_status_text} disabled={button_status}  />
        </form>
      </div>
      )
  }
}

export default PoemForm;
