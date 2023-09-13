import { useUserContext } from "../usercontext/UserContext";
import { useState, useEffect } from "react";
import UserPosts from "./helperComponents/displayAllUserPosts"
import AddFriendButton from "../../buttonComponents/sendFriendRequestButton/sendFriendRequestButton"
import SendMessageButton from "../../buttonComponents/createMessageButton/createMessageButton"
import { useParams } from 'react-router-dom';


const ProfilePage = () => {
  const { userid } = useParams();
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [friendList, setFriendList] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);

        
        const accountDataResponse = await fetch(`/api/user/${userid}`);
        if (!accountDataResponse.ok) {
          throw new Error("Failed to fetch account data");
        }
        const accountDataJson = await accountDataResponse.json();
        setAccountData(accountDataJson);

        
        const friendListResponse = await fetch(`/api/friends/${user.appUserID}/getAllFriends`);
        if (!friendListResponse.ok) {
          throw new Error("Failed to fetch friends");
        }
        const friendData = await friendListResponse.json();
        setFriendList(friendData.friends);

        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userid, user.appUserID]);

  const removeFriend = (userid) => {
    setFriendList(friendList.filter(friendId => friendId !== userid));
  };

  const isFriend = accountData && friendList ? friendList.includes(accountData.appUserID) : false;
  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h2>Account Details</h2>
      { accountData && (
        <>
          <p>ID: {accountData.appUserID}</p>
          <p>First Name: {accountData.firstname}</p>
          <p>Last Name: {accountData.lastname}</p>
          <p>Email: {accountData.email}</p>
        </>
      )}

      {isFriend ? (
        <deleteFriendButton username={accountData.username} removeFriend={() => removeFriend(accountData.appUserID)} />
      ) : (
        <AddFriendButton username={accountData.username} isLoading={isLoading} error={error} />
      )}

      <SendMessageButton defaultRecipientName={accountData.username} />
      <UserPosts userid={userid} profileUserId={accountData ? accountData.appUserID : null} />
    </div>
  );
};

export default ProfilePage;