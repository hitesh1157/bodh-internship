import React, { Component } from 'react';
// import Select from 'react-select';
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import Speakers from '../../services/speakers'

class SelectSpeakers extends Component {
  constructor(props) {
    super(props)
    this.state = { data: null }
  }

  componentDidMount() {
    Speakers.getAll()
      .then((response) => {
        var speakers = response.data;
        var items = [<MenuItem value="" key="" primaryText='Select One Speaker' disabled />];
        speakers.forEach(function(speaker,index) {
          items.push(<MenuItem value={speaker.id} key={index} primaryText={ speaker.name.en } />);
        })
        this.setState({
          data: items
        });
      })
      .catch(err => {
        alert(err);
      })
  }

  render() {
    let select;
    let textStyle = {
      width: '100%'
    }  
    if (this.state && this.state.data) {
      
    select = <SelectField style={textStyle}
            floatingLabelText='' 
            value={this.props.value}
            onChange={this.props.onChange}
            required
            >{this.state.data}</SelectField>
    }
    return (
      <div>
      {select}
      </div>
    )
  }
}

export default SelectSpeakers;
