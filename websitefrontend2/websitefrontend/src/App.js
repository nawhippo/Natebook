import React, { Component, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/usercontext/UserContext';
import { useUserContext } from './pages/usercontext/UserContext';
import FeedPage from './pages/feedPage/feedPage';
import AccountPage from './pages/accountPage/accountPage';
import FriendsPage from "./pages/friendsPage/friendsPage";
import AddressBookPage from './pages/addressbookPage/AddressBookPage';
import MessagesPage from './pages/messagePage/MessagesPage';
import ProfilePage from './pages/profilePage/profilePage';
import Banner from "./banners/websiteBanner"
class App extends Component {
  render() {
    return (
      <UserProvider>
        <Router>
          <div className="App">
            <Banner />
            <header className="App-header">
              <Switch>
                <Route path="/Account" exact component={AccountPage} />
                <Route path="/Feed" exact component={FeedPage} />
                <Route path ="/AddressBook" exact component={AddressBookPage} />
                <Route path="/Friends" component={FriendsPage} />
                <Route path="/UserProfile/:username" component={ProfilePage}/>
                <Route path="/Messages" component={MessagesPage}/>
              </Switch>    
            </header>
          </div>
        </Router>
      </UserProvider>
    );
  }
}
export default App;