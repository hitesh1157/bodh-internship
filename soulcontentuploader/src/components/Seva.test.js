import React from 'react';
import ReactDOM from 'react-dom';
import Seva from './Seva';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<Seva />, div);
});
