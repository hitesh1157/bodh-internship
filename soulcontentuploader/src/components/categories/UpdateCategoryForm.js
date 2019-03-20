import React, { Component } from 'react';
import TextField from 'material-ui/TextField'
import Search from '../shared/Search';
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import IconButton from 'material-ui/IconButton';
import RaisedButton from 'material-ui/RaisedButton'

import Quillpad from '../../services/quillpad'
import SelectPublishType from '../shared/SelectPublishType';
import SelectSect from '../shared/SelectSect';
import Playlists from '../../services/playlists'
import Categories from '../../services/categories'
import './categories.css';

class CategoryUpdateForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      id: this.props.match.params.id,
      name: {
        "en": "",
        "hi": ""
      },
      selectedPlaylists: [],
      allPlaylists: [],
      publishType: '',
      introduction: '',
      position: '',
      sect: ''
    };

    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSelectedPlaylistsChange = this.handleSelectedPlaylistsChange.bind(this);
    this.getCategory = this.getCategory.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.handleIntroductionChange = this.handleIntroductionChange.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
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

  componentWillMount() {
    Playlists.getAll()
      .then((response) => {
        var playlists = response.data;
        var items = [];
        playlists.forEach(function(playlist, index) {
          items.push({
            id: playlist.id,
            value: playlist.name.en
          });
        })
        this.getCategory(items)
      })
      .catch(err => {
        alert(err);
      })
  }

  getCategory(items) {
    Categories.get(this.state.id)
      .then((response) => {
        var old_data = response.data;
        var selected_playlist_ids_names;
        var playlist_id_name_map = {};
        items.forEach((item) => {
          playlist_id_name_map[item.id] = item.value
        })
        selected_playlist_ids_names = old_data.playlistIds.map((playlistId) => {
          return {
            id: playlistId,
            value: playlist_id_name_map[playlistId]
          }
        })

        this.setState({
          "name": old_data.name,
          "selectedPlaylists": selected_playlist_ids_names,
          "allPlaylists": items,
          "publishType": old_data.publishType,
          "introduction": old_data.introduction,
          "position": old_data.position,
          "sect": old_data.sect
        })
      })
      .catch((err) => {
        alert(err);
      })
  }

  handleIntroductionChange(event) {
    const target = event.target;
    var value = target.value;
    var name = target.name;
    this.setState({
      [name]: value
    });
  }

  handlePublishTypeChange(event, index, value) {
    this.setState({publishType: value})
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

  handleSelectedPlaylistsChange(items) {
    console.log(items);
    var selected_playlist_ids_names = [];
    items.forEach(function(playlist, index) {
      selected_playlist_ids_names.push({id: playlist.id, value: playlist.value});
    })

    this.setState({
      "selectedPlaylists": selected_playlist_ids_names
    });
  }

  handleSubmit(event) {
    Categories.update(this.state)
      .then((response) => {
        alert("Category " + this.state.name.en + " created successfully!")
        this.setState({
          name: {
            "en": "",
            "hi": ""
          },
          selectedPlaylists: [],
          allPlaylists: [],
          publishType: '',
          introduction: '',
          sect: ''
        })
      })
      .catch(err => {
        alert(err);
      })
    event.preventDefault();
  }

  render() {
    let textStyle = {
      width: '100%'
    }
    return (
      <div className="more-padding">
      <h2> Update Category: </h2>
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

        <br />
        <br />

        <label> Add Playlists:
        </label>
        <div>
        <Search items={this.state.allPlaylists}
                placeholder='Pick playlists'
                multiple={true}
                onItemsChanged={this.handleSelectedPlaylistsChange}
                initialSelected={this.state.selectedPlaylists}
                autoCompleteVisibility={true}
                 />
        </div>
        <br></br>


        <div>
        <SelectPublishType  name="Publish Type" value={this.state.publishType} onChange={this.handlePublishTypeChange} />
        </div>
        <br></br>


        <div>
        <TextField floatingLabelText="Introduction" style={textStyle} multiLine="true" name="introduction" value={this.state.introduction} onChange={this.handleIntroductionChange} />
        </div>
        <br></br>

        <RaisedButton type="Submit" primary={true} label='Submit' />
      </form>
      </div>
      )
  }
}


export default CategoryUpdateForm;
