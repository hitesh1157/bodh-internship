import React, { Component } from 'react';
// import Select from 'react-select';
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import * as constants from '../../config/Constants';

class SelectPublishType extends Component{
  constructor(props) {
    super(props)
    this.state = {
      data: null
    }
  }

  componentDidMount() {
    var publish_types = constants.PUBLISH_TYPES;
    var items = [];
    publish_types.forEach(function(publish_type,index) {
      items.push(<MenuItem value={publish_type} key={index} primaryText={ publish_type } />);
    })
    this.setState({
      data: items
    });
  }

  render() {
    let select;
    let textStyle = {
      width: '100%'
    }
    if (this.state && this.state.data) {
      
      select = <SelectField style={textStyle}
            floatingLabelText={this.props.name} 
            value={this.props.value}
            onChange={this.props.onChange}
            required
            >{this.state.data}</SelectField>
    }
    return (
      <div>
      {select}
      </div>
     )}
  }

export default SelectPublishType;
