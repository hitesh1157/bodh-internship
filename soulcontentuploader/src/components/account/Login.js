import React, {Component} from "react";
import { Redirect } from 'react-router-dom'

import Account from '../../services/account'
import './Login.css';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      redirectToReferrer: false
    };

    this.authenticateUser = this.authenticateUser.bind(this);
  }


  authenticateUser(e) {

    Account.login(
      document.getElementById('email').value,
      document.getElementById('password').value
    ).then(user => {
      this.setState({ redirectToReferrer: true });
    }).catch(error => {
      console.error(error)
      this.setState({error: Account.getLoginError(error)})
    });
    e.preventDefault();
    
  }

  render() {

    if (Account.user || this.state.redirectToReferrer) {

      return (
        <Redirect to={{ pathname: '/home' }}/>
      )
    }

    let err;
    if (this.state.error) {
      err = <div className="error">{this.state.error}</div>
    }

    return (
      <div className="login-wrapper">
        <div className="container">
          <h1>Welcome</h1>
          <form className="form" onSubmit={this.authenticateUser}>
            <input type="text" placeholder="Email" id="email" />
            <input type="password" placeholder="Password" id="password"/>
            <button type="Submit" id="login-button" >Login</button>
            {err}
          </form>
        </div>
        <ul className="bg-bubbles">
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
          <li></li>
        </ul>
      </div>
    );
  }
}

export default Login;