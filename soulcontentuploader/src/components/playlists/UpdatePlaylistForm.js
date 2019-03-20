import React, { Component } from 'react';
import TextField from 'material-ui/TextField'
import RaisedButton from 'material-ui/RaisedButton'
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import IconButton from 'material-ui/IconButton';
import Quillpad from '../../services/quillpad'
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'

import Search from '../shared/Search';
import SelectSpeakers from '../shared/SelectSpeakers';
import sortBy from "lodash/sortBy";
import SelectPublishType from '../shared/SelectPublishType';
import SelectSect from '../shared/SelectSect';
import Lectures from '../../services/lectures'
import Playlists from '../../services/playlists'
import * as constants from '../../config/Constants';

class UpdatePlaylistForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      id: this.props.match.params.id,
      name: {
        "en": "",
        "hi": ""
      },
      speaker: '',
      selectedLectures: [],
      allLectures: [],
      publishType: '',
      playlistType: '',
      sect: ''
    };
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSpeakerChange = this.handleSpeakerChange.bind(this);
    this.handleSelectedLecturesChange = this.handleSelectedLecturesChange.bind(this);
    this.getPlaylist = this.getPlaylist.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handlePlaylistTypeChange = this.handlePlaylistTypeChange.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
  }

  componentDidMount() {
    Lectures.getAllEligibleForPlaylist(this.state.id)
      .then((response) => {
        var lectures = response.data;
        var sortedLectures = sortBy(lectures, "createdDate").reverse()
        var items = [];
        sortedLectures.forEach(function(lecture, index) {
          items.push({id: lecture.id, value: lecture.name.en});
        })
        this.getPlaylist(items)
      })
      .catch((err) => {
        alert(err);
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

  getPlaylist(items) {
    Playlists.get(this.state.id)
      .then((response) => {
        var old_data = response.data;
        var selected_lecture_ids_names;
        var lecture_id_name_map = {};
        items.forEach((item) => {
          lecture_id_name_map[item.id] = item.value
        })
        selected_lecture_ids_names = old_data.lectureIds.map((lectureId)=> {
          return {
            id: lectureId,
            value: lecture_id_name_map[lectureId]
          }
        })
        this.setState({
          "name": old_data.name,
          "selectedLectures": selected_lecture_ids_names,
          "allLectures": items,
          "speaker": old_data.speaker.id,
          "publishType": old_data.publishType,
          "playlistType": old_data.playlistType,
          "sect": old_data.sect
        })
      })
  }

  handlePublishTypeChange(event, index, value) {
    this.setState({"publishType": value});
  }

  handleSpeakerChange(event, index, value) {
    this.setState({"speaker": value});
  }

  handlePlaylistTypeChange(event, index, value) {
    this.setState({"playlistType": value});
  }

  handleSectChange(event, index, value) {
    this.setState({sect: value})
  }

  handleNameChange(event) {
    const target = event.target;
    var value = target.value;
    var name = 'name';
    var lang = target.name
    var curr_name = this.state.name
    curr_name[lang] = value
    this.setState({
      [name]: curr_name
    });
  }

  handleSelectedLecturesChange(items) {
    var selected_lecture_ids_names = []
    items.forEach(function(item, index){
      selected_lecture_ids_names.push({id: item.id, value: item.value})
    })
    console.log(selected_lecture_ids_names);
    this.setState({
      "selectedLectures": selected_lecture_ids_names
    });
  }

  handleSubmit(event) {
    Playlists.update(this.state)
      .then((response) => {
        alert("Playlist: " + this.state.name.en + " created successfully!")
        this.setState({
          name: {"en": "", "hi": ""},
          speaker: '',
          selectedLectures: [],
          allLectures: [],
          publishType: '',
          playlistType: '',
          sect: ''
        })
      })
      .catch(err => {
        alert(err);
      })
    event.preventDefault();
  }

  render() {
    let playlistTypes = [];
    constants.PLAYLIST_TYPES.forEach(function(playlistType, index) {
      playlistTypes.push(<MenuItem value={playlistType} key={index} primaryText={playlistType} />);
    })

    let textStyle = {
      width: '100%'
    }
    return (
      <div className="more-padding">
        <h2> Update Playlist: </h2>
        <br></br>
        <form  onSubmit={this.handleSubmit}>

          <div>
          <TextField style={textStyle} name="en" value={this.state.name.en} onChange={this.handleNameChange} floatingLabelText="Name(English)" required/>
          </div>

          <div className='float-left'>
          <TextField style={textStyle} name="hi" value={this.state.name.hi} onChange={this.handleNameChange} floatingLabelText="Name(Hindi)" required/>
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
          <Search items={this.state.allLectures}
                  placeholder='Pick lectures'
                  multiple={true}
                  onItemsChanged={this.handleSelectedLecturesChange}
                  initialSelected={this.state.selectedLectures}
                  autoCompleteVisibility={true} />
          </div>
          <br></br>

          <div>
          <SelectPublishType  name="Publish Type" value={this.state.publishType} onChange={this.handlePublishTypeChange} />
          </div>

          <SelectField style={textStyle}
            floatingLabelText='Playlist Type'
            value={this.state.playlistType}
            onChange={this.handlePlaylistTypeChange}
            required
          >{playlistTypes}</SelectField>
          <br></br>
          <RaisedButton type="Submit" primary={true} label='Submit' />
        </form>
      </div>
      )
  }
}


export default UpdatePlaylistForm;
