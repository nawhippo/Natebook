import React, { useEffect, useState } from "react";
import { useUserContext } from "../login/UserContext";
import { Link, useHistory } from "react-router-dom";

const GetUser = () => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const history = useHistory();
  const location = useLocation(); 
  const profileUsername = location.state?.profileUsername;




  const handleLinkClick = (url) => {
    history.push(url);
  };

  const handleAddFriendClick = (friendUsername) => {
    fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${friendUsername}`, {
      method: 'PUT',
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to send friend request');
      }
      return response.json();
    })
    .catch(error => {
      console.error("Error sending friend request:", error);
    });
  };


  
  const handleButtonClick = (recipientUsername) =>{
    history.push({
      pathname: '/createMessage',
      state: { recipient: recipientUsername }
    });
  };


  useEffect(() => {
    if (profileUsername) {
      fetch(`/api/user/${profileUsername}/`)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          setAccountData(data);
          setIsLoading(false);
        })
        .catch(error => {
          setError(error.message);
          setIsLoading(false);
        });
    }
  }, [profileUsername]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  const isFriend = accountData.friends.includes(user.appUserID);

  return (
    <div>
      <h2>Account Details</h2>
      <p>ID: {accountData.appUserID}</p>
      <p>First Name: {accountData.firstname}</p>
      <p>Last Name: {accountData.lastname}</p>
      <p>Email: {accountData.email}</p>
      {!isFriend && accountData.appUserID !== user.appUserID ? (
        <button onClick={() => handleAddFriendClick(accountData.username)}>Add Friend</button>
      ) : null}
        <button onClick={() => handleButtonClick(accountData.username)}>Send Message</button>
      
    </div>
  );
};

export default GetUser;