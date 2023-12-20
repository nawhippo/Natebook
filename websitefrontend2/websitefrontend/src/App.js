import React, { Component, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import { UserProvider } from './pages/usercontext/UserContext';
import { useUserContext } from './pages/usercontext/UserContext';
import SnowFlake from './FunSFX/snowflake';
import FeedPage from './pages/feedPage/feedPage';
import AccountPage from './pages/accountPage/accountPage';
import FriendsPage from "./pages/friendsPage/friendsPage";
import AllUserPage from './pages/allUsersPage/allUsersPage';
import MessagesPage from './pages/messagePage/MessagesPage';
import ProfilePage from './pages/profilePage/profilePage';
import LoginPage from './pages/loginPage/loginPage';
import Banner from "./banners/websiteBanner"
import styles from "./global.css";
import "./FunSFX/snowflake.css"
class App extends Component {
  render() {

    const snowflakes = Array.from({ length: 50 }).map((_, index) => (
        <SnowFlake key={index} />
    ));

    return (
      <UserProvider>
        <Router>
          <div className="App">
            <div className="snowflake-container">{snowflakes}</div>
            <Banner />
            <header className="App-header">
              <Switch>
                <Route path="/Account" exact component={AccountPage} />
                <Route path="/Feed" exact component={FeedPage} />
                <Route path ="/AllUsersPage" exact component={AllUserPage} />
                <Route path="/Friends" component={FriendsPage} />
                <Route path="/UserProfile/:userid" component={ProfilePage}/>
                <Route path="/Messages" component={MessagesPage}/>
                <Route path="/Login" component={LoginPage}/>
              </Switch>    
            </header>
          </div>
        </Router>
      </UserProvider>
    );
  }
}
export default App;