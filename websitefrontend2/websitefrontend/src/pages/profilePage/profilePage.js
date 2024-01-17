import { useUserContext } from "../usercontext/UserContext";
import React, { useEffect, useState } from "react";
import UserPosts from "./helperComponents/displayAllUserPosts";
import AddFriendButton from "../../buttonComponents/sendFriendRequestButton/sendFriendRequestButton";
import DeleteFriendButton from "../../buttonComponents/deleteFriendButton/deleteFriendButton";
import { useHistory, useParams } from 'react-router-dom';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { getRandomColor } from "../../FunSFX/randomColorGenerator";
import UserStatus from "../../buttonComponents/getStatus/getStatus";
import getAllFollowers from "../../buttonComponents/getAllFollowers/getAllFollowers";
import getAllFollowing from "../../buttonComponents/getAllFollowing/getAllFollowing";
import FollowButton from "../../buttonComponents/followButton/followButton";
import UnfollowButton from "../../buttonComponents/unfollowButton/unfollowButton";
import GetAllFollowing from "../../buttonComponents/getAllFollowing/getAllFollowing";
import GetAllFollowers from "../../buttonComponents/getAllFollowers/getAllFollowers";
const ProfilePage = () => {
  const {userid} = useParams();
  const {user} = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [friendList, setFriendList] = useState([]);
  const [followingList, setFollowingList] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const history = useHistory();
  const [followersCount, setFollowersCount] = useState(0);
  const [followingCount, setFollowingCount] = useState(0);
  const [postsCount, setPostsCount] = useState(0);
  const [visibleSection, setVisibleSection] = useState(null);


  const togglePosts = () => setVisibleSection(visibleSection !== 'posts' ? 'posts' : null);
  const toggleFollowers = () => setVisibleSection(visibleSection !== 'followers' ? 'followers' : null);
  const toggleFollowing = () => setVisibleSection(visibleSection !== 'following' ? 'following' : null);


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
          const followingResponse = await fetchWithJWT(`/api/${user.appUserID}/following`);
          if (!followingResponse.ok) {
            throw new Error("Failed to fetch following list");
          }
          if (!friendListResponse.ok) {
            throw new Error("Failed to fetch friends");
          }
          const followingData = await followingResponse.json();
          const friendData = await friendListResponse.json();
          setFriendList(friendData.friends);
          setFollowingList(followingData.following);
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
  const isFollowing = accountData && followingList ? followingList.includes(accountData.appUserID) : false;
  const isFriend = accountData && friendList ? friendList.includes(accountData.appUserID) : false;
  const isCurrentUser = user && user.appUserID === parseInt(userid, 10);
  const SendMessageButtonClick = (recipientUsername) => {
    console.log("Recipient name at the time of send button click" + recipientUsername);
    history.push({
      pathname: '/Messages',
      state: {recipientUsername}
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
    transform: "TranslateY(-10px)",
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

  const toggleSectionStyle = {
    boxSizing: 'border-box',
    width: '33.33%',
    padding: '10px',
    display: 'inline-block',
    verticalAlign: 'top',
    overflow: 'hidden',
    visibility: 'hidden',
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
              <div style={{transform: 'translateX(30px)'}}>
                <ProfilePictureComponent style={customStyle} userid={accountData.appUserID}/>
              </div>
              <div style={profileInfoStyle}>
                <div style={nameAndStatusContainerStyle}>
                  <p>{accountData.firstname} {accountData.lastname}</p>
                </div>
                <p style={{fontSize: '30px'}}> Friends: {accountData.friendCount} Followers: {followersCount} Following: {followingCount} </p>
                <UserStatus appUserId={accountData.appUserID}/>
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
              {isFollowing ? (
                  <UnfollowButton followedId={accountData.appUserID}/>
              ) : (
                  <FollowButton followedId={accountData.appUserID}/>
              )}
              <button style={buttonStyle} onClick={() => SendMessageButtonClick(accountData.username)}>
                Send Message
              </button>
            </>
        )}
        {accountData && (
            <div style={{width: '100%'}}>
              <div style={{display: 'flex', justifyContent: 'center', margin: '10px 0'}}>
                <button style={buttonStyle} onClick={togglePosts}>
                  {visibleSection === 'posts' ? 'Hide Posts' : 'Show Posts'}
                </button>
                <button style={buttonStyle} onClick={toggleFollowers}>
                  {visibleSection === 'followers' ? 'Hide Followers' : 'Show Followers'}
                </button>
                <button style={buttonStyle} onClick={toggleFollowing}>
                  {visibleSection === 'following' ? 'Hide Following' : 'Show Following'}
                </button>
              </div>

              {visibleSection === 'posts' && (
                  <UserPosts
                      userid={accountData.appUserID}
                      profileUserId={accountData ? accountData.appUserID : null}
                  />
              )}
              {visibleSection === 'followers' && (
                  <GetAllFollowers
                      userId={accountData.appUserID}
                      setFollowersCount={setFollowersCount}
                  />
              )}
              {visibleSection === 'following' && (
                  <GetAllFollowing
                      userId={accountData.appUserID}
                      setFollowingCount={setFollowingCount}
                  />
              )}
            </div>
        )}
      </div>
  )
}
export default ProfilePage;