import React, { Component } from 'react';
import RaisedButton from 'material-ui/RaisedButton'
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import _ from "lodash";
import IconButton from 'material-ui/IconButton';

import SelectSpeakers from '../shared/SelectSpeakers';
import SelectPublishType from '../shared/SelectPublishType';
import SelectSect from '../shared/SelectSect';
import Lectures from '../../services/lectures'
import NameAutosuggest from '../shared/NameAutosuggest'
import Quillpad from '../../services/quillpad'

import './lecture.css';

class UpdateLectureForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      id: this.props.match.params.id,
      name: {
        "en": "",
        "hi": ""
      },
      speaker: "",
      thumbnail: {
        "uploaded": "0",
        "url": ""
      },
      content: {
        "uploaded": "0",
        "url": "",
        "fromUrl": ""
      },
      publishType: "",
      enLectures: [],
      hiLectures: [],
      lectures: [],
      sect: ''
    };

    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSpeakerChange = this.handleSpeakerChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
  }

  componentDidMount() {
    Lectures.get(this.state.id)
      .then((response) => {
        let old_data = response.data;
        let thumbnail = old_data.thumbnail
        let uploaded;
        if(thumbnail === undefined) {
          uploaded = 0
        } else {
          uploaded = 2
        }
        this.setState({
          "name": old_data.name,
          "speaker": old_data.speaker.id,
          "thumbnail": {
            "uploaded": uploaded,
            "url": thumbnail
          },
          "content": {
            "uploaded": "2",
            "url": old_data.audio
          },
          "publishType": old_data.publishType,
          "sect": old_data.sect
        })
      })
      .catch(err => {
        alert(err);
      })

      console.log(this.state.speaker);

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
        alert("This is a checkpoint.");
        alert(err)
      })
  }

  handlePublishTypeChange(event, index, value) {
    this.setState({ publishType: value });
    console.log(value)
  }

  handleSpeakerChange(event, index, value) {
    this.setState({speaker: value})
  }

  handleSectChange(event, index, value) {
    this.setState({sect: value})
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

  handleSubmit(event) {
    Lectures.update(this.state)
      .then((response) => {
        alert("Lecture: " + this.state.name.en + " uploaded successfully!")
      })
      .catch(err => {
        alert(err);
      })
    event.preventDefault();
  }

  render() {
    console.log(this.state)
    return (
      <div className="more-padding">
        <h2> Update Lecture </h2>
        <br></br>
        <form  onSubmit={this.handleSubmit}>
          <label> Name(English):
          </label>
          <div>
            <NameAutosuggest
                id="en"
                placeholder="Lecture name"
                onChange={this.handleNameChange}
                value={this.state.name.en}
                data={this.state.enLectures}
              />
          </div>
          <br></br>

          <div className='float-left'>
          <label> Name(Hindi):
          </label>
            <NameAutosuggest
                id="hi"
                placeholder="Lecture name"
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

          <br></br>

          <div>
          <SelectSpeakers  name="Speaker" value={this.state.speaker} onChange={this.handleSpeakerChange} />
          </div>
          <br></br>

          <div>
          <SelectPublishType  name="Publish Type" value={this.state.publishType} onChange={this.handlePublishTypeChange} />
          </div>
          <br></br>
          <RaisedButton type="Submit" primary={true} label='Submit'/>
        </form>
      </div>
      );
  }
}

export default UpdateLectureForm;
