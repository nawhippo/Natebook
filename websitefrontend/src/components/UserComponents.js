import React, { useEffect, useState } from 'react';
import userService from '../services/users/userService';

const UserComponent = () => {
  const [friends, setFriends] = useState([]);
  const [user, setUser] = useState(null);
  const [messages, setMessages] = useState([]);
  const [selectedMessage, setSelectedMessage] = useState(null);
  const [selectedPost, setSelectedPost] = useState(null);
  const [newUser, setNewUser] = useState({});

  useEffect(() => {
    const fetchData = async () => {
        //user id is used as an argument for all of these.
      try {
        const friendsData = await userService.getAllFriends(1); 
        setFriends(friendsData);

        const userData = await userService.getUserById(1); 
        setUser(userData);

        const messagesData = await userService.getAllMessages(1); 
        setMessages(messagesData);

        const messageData = await userService.getMessageById(1, 1); 
        setSelectedMessage(messageData);

        const postData = await userService.getPostById(1, 1); 
        setSelectedPost(postData);
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    fetchData();
  }, []);

  const handleCreateAccount = async () => {
    await userService.createAccount(newUser);
    //perform any additional logic after creating the account
  };

  return (
    <div>
      <h1>User Component</h1>
      {/* Display user-related data */}
    </div>
  );
};

export default UserComponent;