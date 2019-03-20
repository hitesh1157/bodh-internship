import React, { Component } from 'react';
import GTranslate from 'material-ui/svg-icons/action/g-translate';
import IconButton from 'material-ui/IconButton';
import TextField from 'material-ui/TextField'
import RaisedButton from 'material-ui/RaisedButton'

import Quillpad from '../../services/quillpad'
import Search from '../shared/Search';
import sortBy from "lodash/sortBy";
import SelectPublishType from '../shared/SelectPublishType';
import SelectSect from '../shared/SelectSect';
import Categories from '../../services/categories'
import Playlists from '../../services/playlists'

class AddCategoryForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: {"en": "", "hi": ""},
      selectedPlaylists: [],
      allPlaylists: [],
      publishType: 'PUBLISHED_UNVERIFIED',
      introduction: "",
      sect: ""
    };
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSelectedPlaylistsChange = this.handleSelectedPlaylistsChange.bind(this);
    this.handlePublishTypeChange = this.handlePublishTypeChange.bind(this);
    this.handleIntroductionChange = this.handleIntroductionChange.bind(this);
    this.convertInHindi = this.convertInHindi.bind(this);
    this.handleSectChange = this.handleSectChange.bind(this);
  }

  componentWillMount() {
    Playlists.getAll()
      .then((response) => {
        var playlists = response.data;
        var sortedPlaylists = sortBy(playlists, "modifiedDate").reverse();
        var items = [];
        sortedPlaylists.forEach(function(playlist, index) {
          items.push({id: playlist.id, value: playlist.name.en});
        })
        this.setState({
          allPlaylists: items
        });
      })
      .catch(err => {
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

  handlePublishTypeChange(event, index, value) {
    this.setState({publishType: value})
  }

  handleSectChange(event, index, value) {
    this.setState({sect: value})
  }

  handleIntroductionChange(event) {
    const target = event.target;
    var value = target.value;
    var name = target.name;
    this.setState({
      [name]: value
    });
    console.log(this.state)
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
    console.log(this.state)
  }

  handleSelectedPlaylistsChange(items) {
    console.log(items);
    var selected_playlist_ids_names = [];
    items.forEach(function(playlist, index) {
      selected_playlist_ids_names.push({id: playlist.id, value: playlist.value});
    })

    this.setState({"selectedPlaylists": selected_playlist_ids_names});

  }

  handleSubmit(event) {
    Categories.add(this.state)
      .then((response) => {
        alert("Category " + this.state.name.en + " created successfully!")
        this.setState({
          name: {"en": "", "hi": ""},
          selectedPlaylists: [],
          allPlaylists: [],
          publishType: '',
          sect: ''
        })
      })
      .then(err => {
        //alert(err);
      })
    event.preventDefault();
  }

  render() {

    let textStyle = {
      width: '100%'
    }

    return (
      <div className="more-padding">
        <h2> Add Category: </h2>
        <form  onSubmit={this.handleSubmit}>

          <div >
          <TextField style={textStyle} name="en" value={this.state.name.en} onChange={this.handleNameChange} floatingLabelText="Name(English)" required/>
          </div>

          <div className='float-left'>
          <TextField style={textStyle} name="hi" value={this.state.name.hi} onChange={this.handleNameChange} floatingLabelText="Name(Hindi)" required/>
          </div>

          <div >
            <IconButton onTouchTap={this.convertInHindi} tooltip="Translate from English to Hindi" >
              <GTranslate/>
            </IconButton>
          </div>

          <div>
          <SelectSect  name="Sect" value={this.state.sect} onChange={this.handleSectChange} />
          </div>

          <br></br>
          <br></br>

          <label> Add Playlists:
          </label>
          <div>
          <Search
            items={this.state.allPlaylists}
            placeholder='Pick playlists'
            multiple={true}
            onItemsChanged={this.handleSelectedPlaylistsChange}
            initialSelected={this.state.selectedPlaylists}
            autoCompleteVisibility={true} />
          </div>
          <br></br>


          <div>
          <SelectPublishType  name="Publish Type" value="PUBLISHED_UNVERIFIED" onChange={this.handlePublishTypeChange} />
          </div>
          <br></br>

          <div>
          <TextField floatingLabelText="Introduction" style={textStyle} multiLine={true} name="introduction" value={this.state.introduction} onChange={this.handleIntroductionChange} />
          </div>
          <br></br>

          <RaisedButton type="Submit" primary={true} label='Submit' />
        </form>
      </div>
      )
  }
}


export default AddCategoryForm;
