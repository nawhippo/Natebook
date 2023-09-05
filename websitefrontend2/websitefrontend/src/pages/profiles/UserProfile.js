import React, { useEffect, useState } from "react";
import { useUserContext } from "../usercontext/UserContext";
import { Link, useHistory, useLocation } from "react-router-dom";

const GetUser = ({username}) => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const history = useHistory();

  const handleLinkClick = (url) => {
    history.push(url);
  };

  const handleAddFriendClick = (username) => {
    fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${username}`, {
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


  
  const handleButtonClick = (username) =>{
    history.push({
      pathname: '/createMessage',
      state: { recipient: username }
    });
  };


  useEffect(() => {
    if (username) {
      fetch(`/api/user/${username}/`)
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
  }, [username]);

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