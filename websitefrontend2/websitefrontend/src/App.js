import React, { Component, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/login/UserContext';
import { useUserContext } from './pages/login/UserContext';
import Login from './pages/login/Login';
import Home from './pages/home/Home';
import createAccount from './pages/account/createAccount';
import getAllPosts from './pages/posts/allposts/getAllPosts';
import specPost from './pages/posts/specpost/specPost';
import specFriend from './pages/friends/specfriend/specFriend';
import getAllFriends from './pages/friends/allfriends/getAllFriends';
import createMessage from './pages/message/createMessage/createMessage';
import getAllMessages from './pages/message/getAllMessages/getAllMessages';
import createPost from './pages/posts/createPost/createPost';
import ProtectedRoute from './pages/login/ProtectedRoute';
import Banner from './banners/banner';
import './universal.css';
import SendFriendRequestByUsername from './pages/friends/friendrequests/SendFriendRequestByUsername';
import GetAllFriendRequests from './pages/friends/friendrequests/ViewAllFriendRequests';
import Logout from './pages/login/Logout';

class App extends Component {
  render() {
    return (
      <UserProvider>
        <Router>
          <div className="App">
            <Banner />
            <header className="App-header">
              <Switch>
                {/* Routes that don't require authentication */}
                <Route path="/about" component={About} />
                <Route path="/createAccount" component={createAccount} />

                {/* Routes that require authentication */}
                <Route path="/home" exact component={Home} />
                <Route path="/getAllFriends" component={getAllFriends} />
                <Route path="/specFriend/:userId" component={specFriend} /> 
                <Route path="/createMessage" component={createMessage} />
                <Route path="/getAllMessages" component={getAllMessages} />
                <Route path="/login" component={Login} />
                <Route path="/specPost/:userId" component={specPost} /> 
                <Route path="/getAllPosts" component={getAllPosts} />
                <Route path="/createPost" component={createPost} />
                <Route path="/sendFriendRequestByUsername" component={SendFriendRequestByUsername} />
                <Route path="/getFriendRequests" component={GetAllFriendRequests} />
                <Route path="/SendFriendRequest" component={SendFriendRequestByUsername} />
                <Route path="/logout" component={Logout} />
              </Switch>    
            </header>
          </div>
        </Router>
      </UserProvider>
    );
  }
}
export default App;