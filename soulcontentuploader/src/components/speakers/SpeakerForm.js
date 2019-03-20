import React, { Component } from 'react';
import TextField from 'material-ui/TextField'
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import RaisedButton from 'material-ui/RaisedButton'
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import IconButton from 'material-ui/IconButton';
import CropperJS from 'react-cropperjs';
import Dialog from 'material-ui/Dialog';

import Quillpad from '../../services/quillpad';
import DropzoneFiles from '../shared/DropzoneFiles';
import Uploads from '../../services/uploads';
import Speakers from '../../services/speakers';
import * as constants from '../../config/Constants';
import ImageTool from '../../services/ImageTool';
import SelectSect from '../shared/SelectSect';

import './speaker.css';

class SpeakerForm extends Component {
   constructor(props) {
    super(props);
    console.log(this.props.match.params.id);
    this.state = {
      id: this.props.match.params.id,
      name: {
        "en": "",
        "hi": ""
      },
      speakerType: '',
      thumbnail: {
        "uploaded": "0",
        "url": ""
      },
      open: false,
      imageURI: "",
      website: "",
      sect: ""
    };

    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSpeakerTypeChange = this.handleSpeakerTypeChange.bind(this);
    this.handleThumbnailChange = this.handleThumbnailChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
  }

  componentDidMount() {
    if (this.state.id) {
      Speakers.get(this.state.id)
      .then((response) => {
        var old_data = response.data;
        var old_data_state = {
          "name": old_data.name,
          "speakerType": old_data.speakerType,
          "thumbnail": {"uploaded": "2", "url": old_data.thumbnail},
          "website": old_data.website,
          "androidApp": old_data.androidApp,
          "iOSApp": old_data.iOSApp,
          "sect": old_data.sect
        }
        this.setState(old_data_state);
      })
      .catch(err => {
        alert(err);
      })
    }
  }

  handleSpeakerTypeChange(event, index, value) {
    this.setState({speakerType: value})
  }

  handleSectChange(event, index, value) {
    this.setState({sect: value})
  }

  handleNameChange(event, val) {
    let currentName = this.state.name;
    currentName[event.target.name] = val;
    this.setState({
      name: currentName
    })
  }

  handleChange(event, val) {
    var name = event.target.name
    var value = event.target.value;
    this.setState({
      [name]: value
    })
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

  handleThumbnailChange(event) {
    var curr_thumbnail =  this.state.thumbnail
    curr_thumbnail.uploaded = "1";
    this.setState({
      "thumbnail": curr_thumbnail,
      "image": event[0],
      "imageSRC": String(event[0].preview),
      "open":true
    })
  }

  _crop(){
    // image in dataUrl
    var imageURI = this.refs.cropper.getCroppedCanvas().toDataURL();
    this.setState({
      "imageURI": imageURI,
    })
  }

  handleClose(event) {
    var curr_thumbnail =  this.state.thumbnail;
    let newFile = ImageTool._toCroppedFile(this.state.imageURI, this.state.image);
    Uploads.addThumbnail(this.state, newFile)
    .then((response) => {
        curr_thumbnail =  this.state.thumbnail
        curr_thumbnail.uploaded = "2"
        curr_thumbnail.url = response.data
        this.setState({
          "thumbnail": curr_thumbnail
        })

    })
    .catch(err => {
        alert(err);
    })

    this.setState({
      "open": false,
    })
  }

  handleSubmit(event) {
    if (this.state.id) {
      if(this.state.thumbnail.uploaded==="2"){
        Speakers.update(this.state)
          .then((response) => {
            alert("Speaker: " + this.state.name.en + " updated successfully!")
          })
          .catch(err => {
            alert(err);
          })
      } else {
        alert("Please upload a thumbnail.");
      }
    } else {
      if(this.state.thumbnail.uploaded==="2"){
        Speakers.add(this.state)
          .then((response) => {
            alert("Speaker: " + this.state.name.en + " created successfully!")
            this.setState({
              name: {"en": "", "hi": ""},
              speakerType: '',
              thumbnail: {"uploaded": "0", "url": ""},
              website: '',
              androidApp: '',
              iOSApp: '',
              sect: ''
            })
          })
          .catch(err => {
            alert(err);
          })
      } else {
        alert("Please upload a thumbnail.");
      }
    }
    event.preventDefault();
  }

  render() {
    let speakers = constants.SPEAKER_TYPES;
    let speakerList = [];
    speakers.forEach(function(speaker, index) {
      speakerList.push(<MenuItem value={speaker} key={index} primaryText={speaker} />);
    })

    let sects = constants.SECTS;
    let sectList = [];
    sects.forEach(function(sect, index) {
      sectList.push(<MenuItem value={sect} key={index} primaryText={sect} />);
    })

    let textStyle = {
      width: '100%'
    }

    const actions = [
      <RaisedButton
        label="DONE"
        primary={true}
        keyboardFocused={false}
        onClick={this.handleClose}
      />,
    ];

    return (
      <div className="more-padding">
        <h2> {this.state.id ? 'Edit' : 'Add'} Speaker: </h2>
        <form onSubmit={this.handleSubmit}>
          <TextField
            style={textStyle} required name='en'
            value={this.state.name.en}
            onChange={this.handleNameChange}
            floatingLabelText="Name(English)"
          />
          <br />
          <div className='float-left'>
            <TextField
              style={textStyle} required name='hi'
              value={this.state.name.hi}
              onChange={this.handleNameChange}
              floatingLabelText="Name(Hindi)"
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
          <SelectField style={textStyle}
            floatingLabelText='Speaker Type'
            value={this.state.speakerType}
            onChange={this.handleSpeakerTypeChange}
            required
          >{speakerList}</SelectField>
          <br />
          <TextField
            style={textStyle} name='website'
            value={this.state.website}
            onChange={this.handleChange}
            floatingLabelText="Website"
          />
          <br />
          <TextField
            style={textStyle} name='androidApp'
            value={this.state.androidApp}
            onChange={this.handleChange}
            floatingLabelText="Android App"
          />
          <br />
          <TextField
            style={textStyle} name='iOSApp'
            value={this.state.iOSApp}
            onChange={this.handleChange}
            floatingLabelText="iOS App"
          />
          <br /><br />
          <label> Thumbnail:</label>
          <DropzoneFiles
            name="thumbnail" fileTypes="image/*" onChange={this.handleThumbnailChange}
            uploadStatus={this.state.thumbnail.uploaded}  uploadUrl={this.state.thumbnail.url} />
          <br />
          <Dialog
            title="Crop Speaker image"
            actions={actions}
            modal={false}
            open={this.state.open}
            onRequestClose={this.handleClose}
          >
              Please crop the selected image appropriately.
              <br/>
              <CropperJS
                  ref='cropper'
                  src={this.state.imageSRC}
                  style={{height: 400, width: '100%'}}
                  // Cropper.js options
                  aspectRatio={1 / 1}
                  guides={false}
                  crop={this._crop.bind(this)} />
          </Dialog>

          <RaisedButton type="Submit" primary={true} label='Submit' />
        </form>
      </div>
      )
  }
}

export default SpeakerForm;
