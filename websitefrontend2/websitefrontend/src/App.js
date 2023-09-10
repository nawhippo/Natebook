import React, { Component, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/usercontext/UserContext';
import { useUserContext } from './pages/usercontext/UserContext';
import Login from './pages/usercontext/Login';
import Home from './pages/home/Home';
import createAccount from './pages/account/createAccount/createAccount';
import getAllPostByUsername from './pages/posts/allposts/getAllPostsByUsername';
import specFriend from './pages/friends/specfriend/specFriend';
import GetAllFriends from './pages/friends/allfriends/getAllFriends';
import createMessage from './pages/buttonComponents/sendMessageButton/createMessageButton';
import createPost from './pages/posts/createPost/createPost';
import ProtectedRoute from './pages/usercontext/ProtectedRoute';
import Banner from './banners/banner';
import './universal.css';
import Logout from './pages/usercontext/Logout';
import SendFriendRequestByUsername from './pages/friendrequests/SendFriendRequest/SendFriendRequestByUsername';
import GetAllFriendRequests from './pages/friendrequests/ViewAllFriendRequests/ViewAllFriendRequests';
import GetAccount from './pages/account/getAccount/getAccount';
import GetAllMessages from './pages/message/getAllMessages/MessagesPage';
import CommentForm from './pages/posts/comment/createComment/createComment';
import UpdateAccount from './pages/account/updateAccount/updateAccount';
import PostsPage from './pages/posts/allposts/getAllPostsByUsername';
import DeleteAccount from './pages/account/deleteAccount/deleteAccount';
import GetUser from './pages/profile/profilePage';
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
                <Route path ="/accountDetails" exact component={GetAccount} />
                <Route path="/getAllFriends" component={GetAllFriends} />
                <Route path="/specFriend/:userId" component={specFriend} /> 
                <Route path="/createMessage" component={createMessage} />
                <Route path="/login" component={Login} />
                <Route path="/getAllPosts" component={PostsPage} />
                <Route path="/createPost" component={createPost} />
                <Route path="/sendFriendRequestByUsername" component={SendFriendRequestByUsername} />
                <Route path="/getFriendRequests" component={GetAllFriendRequests} />
                <Route path="/SendFriendRequest" component={SendFriendRequestByUsername} />
                <Route path="/logout" component={Logout} />
                <Route path="/getAllMessages" component={GetAllMessages} />
                <Route path="/createComment" component={CommentForm}/>
                <Route path="/updateAccount" component={UpdateAccount}/>
                <Route path="/deleteAccount" component={DeleteAccount}/>
                <Route path="/userProfile/:username" component={GetUser}/>
              </Switch>    
            </header>
          </div>
        </Router>
      </UserProvider>
    );
  }
}
export default App;