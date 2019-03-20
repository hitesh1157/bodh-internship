import * as firebase from 'firebase/app';
import * as config from '../config/firebase-prod.json';
import 'firebase/auth';
import 'firebase/database';

class Account {
  constructor() {
    this._type = 'Account';
    this._token = '';
    firebase.initializeApp(config);
  }

  get user() {
    return firebase.auth().currentUser;
  }

  get token() {
    return this._token;
  }

  set token(token) {
    this._token = token;
  }

  onAuthStateChanged(cb) {
    return firebase.auth().onAuthStateChanged(cb);
  }

  loggedIn() {
    let isAuthPresent = false;
    Object.keys(window.localStorage).forEach(function(key) {
      if (key.indexOf('firebase:authUser') !== -1) {
        isAuthPresent = true
      }
    })
    if (!isAuthPresent) {
      window.localStorage.removeItem('isUserLoggedIn')
    }
    return !!window.localStorage.getItem('isUserLoggedIn');
  }

  login(email, password) {
    return new Promise((resolve, reject) => {
      if (email.length < 4) {
        reject('Please enter an email address.');
        return;
      }
      if (password.length < 4) {
        reject('Please enter a password.');
        return;
      }
      firebase.auth().signInWithEmailAndPassword(email, password).then(user => {
        window.localStorage.setItem('isUserLoggedIn', true);
        resolve(user);
      }).catch(error => {
        reject(error);
      });
    });
  }

  getLoginError(error) {
    const errorCode = error.code;
    if (errorCode === 'auth/wrong-password')
      return 'Wrong password.'
    if (errorCode === 'auth/invalid-email')
      return 'The email address is badly formatted.'
    if (errorCode === 'auth/user-not-found')
      return 'User not found. Please contact reachbodh@gmail.com'
    return 'Unexpected error occured.'
  }

  logout() {
    return new Promise((resolve, reject) => {
      firebase.auth().signOut().then(() => {
        window.localStorage.removeItem('isUserLoggedIn');
        resolve("success");
      }).catch(function(error) {
        reject(error);
      })
    })
  }

  getUserInfo() {
    return new Promise((resolve, reject) => {
      firebase.database().ref('/users/' + this.user.uid).once('value').then((snapshot) => {
          resolve(snapshot.val());
        }).catch(err => {
          reject(err);
        });
    });
  }

  get type() {
    return this._type;
  }

  set type(value) {
    this._type = value;
  }
}

export default new Account();
