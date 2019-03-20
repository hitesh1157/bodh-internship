import React, {Component} from 'react';
import Autosuggest from 'react-autosuggest';

import MenuItem from 'material-ui/MenuItem';

import './theme.css';



// https://developer.mozilla.org/en/docs/Web/JavaScript/Guide/Regular_Expressions#Using_Special_Characters
function escapeRegexCharacters(str) {
  return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function getSuggestions(value, data) {
  const escapedValue = escapeRegexCharacters(value.trim());

  if (escapedValue === '') {
    return [];
  }

  const regex = new RegExp('^' + escapedValue, 'i');

  return data.filter(language => regex.test(language.name));
}

function getSuggestionValue(suggestion) {
  return suggestion.name;
}

function renderSuggestion(suggestion) {


  return (

    <MenuItem value={suggestion.name} primaryText={suggestion.name} />

  );
}

class NameAutosuggest extends Component {
  constructor() {
    super();

    this.state = {
      value: '',
      suggestions: []
    };
  }

  onChange = (_, { newValue }) => {
    const { id, onChange } = this.props;

    onChange(id, newValue);
  };

  onSuggestionsFetchRequested = ({ value }) => {
    this.setState({
      suggestions: getSuggestions(value, this.props.data)
    });
  };

  onSuggestionsClearRequested = () => {
    this.setState({
      suggestions: []
    });
  };

  render() {
    
    const { id, placeholder, value } = this.props;
    const { suggestions } = this.state;
    const inputProps = {
      placeholder: placeholder,
      value,
      onChange: this.onChange,
      required: true
    };



    return (
      
      <Autosuggest
        id={id}
        suggestions={suggestions}
        onSuggestionsFetchRequested={this.onSuggestionsFetchRequested}
        onSuggestionsClearRequested={this.onSuggestionsClearRequested}
        getSuggestionValue={getSuggestionValue}
        renderSuggestion={renderSuggestion}
        inputProps={inputProps}
      />

    );
  }
}

export default NameAutosuggest;
