import { useUserContext } from "../usercontext/UserContext";
import React, { useEffect, useState } from "react";
import UserPosts from "./helperComponents/displayAllUserPosts";
import AddFriendButton from "../../buttonComponents/sendFriendRequestButton/sendFriendRequestButton";
import SendMessageButton from "../../buttonComponents/createMessageButton/createMessageButton";
import DeleteFriendButton from "../../buttonComponents/deleteFriendButton/deleteFriendButton";
import { useHistory, useParams } from 'react-router-dom';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { getRandomColor } from "../../FunSFX/randomColorGenerator";
import UserStatus from "../../buttonComponents/getStatus/getStatus";

const ProfilePage = () => {
  const { userid } = useParams();
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [friendList, setFriendList] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const history = useHistory();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);

        const accountDataResponse = await fetchWithJWT(`/api/user/${userid}`);
        if (!accountDataResponse.ok) {
          throw new Error("Failed to fetch account data");
        }
        const accountDataJson = await accountDataResponse.json();
        setAccountData(accountDataJson);

        if (user) {
          const friendListResponse = await fetchWithJWT(`/api/friends/${user.appUserID}/getAllFriends`);
          if (!friendListResponse.ok) {
            throw new Error("Failed to fetch friends");
          }
          const friendData = await friendListResponse.json();
          setFriendList(friendData.friends);
        }

        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userid, user]);

  const customStyle = {
    width: '300px',
    height: '300px',
  };
  const removeFriend = (userid) => {
    setFriendList(friendList.filter(friendId => friendId !== userid));
  };

  const isFriend = accountData && friendList ? friendList.includes(accountData.appUserID) : false;
  const isCurrentUser = user && user.appUserID === parseInt(userid, 10);

  const SendMessageButtonClick = (recipientUsername) => {
    console.log("Recipient name at the time of send button click" + recipientUsername);
    history.push({
      pathname: '/Messages',
      state: { recipientUsername }
    });
  };

  const containerStyle = {
    display: 'flex',
    alignItems: 'center',
    flexWrap: 'wrap',
  };

  const profileInfoStyle = {
    flex: '1',
    margin: '10px',
    marginLeft: '50px',
  };

  const nameAndStatusContainerStyle = {
    display: 'flex',
    alignItems: 'center',
    fontSize: '50px',
    position: 'relative',
    transform: "TranslateY(50px)"
  };

  const onlineStatusStyle = {
    position: 'absolute',
    top: '100%',
    left: '-22%',
    width: '80px',
    height: '80px',
    borderRadius: '50%',
    backgroundColor: accountData && accountData.online ? 'green' : 'red',
  };


  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '4px solid black',
    height: '50px',
    width: '250px',
    margin: '10px',
  };


  if (error) {
    return <div>Error: {error}</div>;
  }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
      <div>
        {accountData && (
            <div style={containerStyle}>
              <div style={{transform:'translateX(30px)'}}>
                <ProfilePictureComponent style={customStyle} userid={accountData.appUserID} />
              </div>
              <div style={profileInfoStyle}>
                <div style={nameAndStatusContainerStyle}>
                  <p>{accountData.firstname} {accountData.lastname}</p>
                  <div style={onlineStatusStyle} />
                </div>
                <p style={{fontSize:'30px'}}>@{accountData.username}</p>
                <p style={{fontSize:'30px'}}>{accountData.occupation}</p>
                <p style={{fontSize:'30px'}}>{accountData.biography}</p>
                <p style={{fontSize: '30px'}}>{accountData.friendCount}</p>
                <UserStatus appUserId={accountData.appUserID} />
              </div>
            </div>
        )}

        {user && !isCurrentUser && (
            <>
              {isFriend ? (
                  <DeleteFriendButton
                      username={accountData.username}
                      removeFriend={() => removeFriend(accountData.appUserID)}
                  />
              ) : (
                  <AddFriendButton
                      username={accountData.username}
                      isLoading={isLoading}
                      error={error}
                  />
              )}
              <button style={buttonStyle} onClick={() => SendMessageButtonClick(accountData.username)}>
                Send Message
              </button>
            </>
        )}
        {accountData && (
            <UserPosts
                userid={accountData.appUserID}
                profileUserId={accountData ? accountData.appUserID : null}
            />
        )}
      </div>
  );
}

export default ProfilePage;