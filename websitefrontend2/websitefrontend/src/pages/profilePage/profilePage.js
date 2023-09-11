import { useUserContext } from "../usercontext/UserContext";
import { useState, useEffect } from "react";
import UserPosts from "./helperComponents/displayAllUserPosts"
import AddFriendButton from "../../buttonComponents/sendFriendRequestButton/addFriendButton"
import SendMessageButton from "../../buttonComponents/createMessageButton/createMessageButton"
const ProfilePage = ({ userId }) => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [friendList, setFriendList] = useState([]); 
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const response = await fetch(`/api/friends/${user.appUserID}/getAllFriends`);
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setAccountData(data);

        
        const friendListResponse = await fetch(`/api/friends/${user.appUserID}`);
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
  }, [userId, user.appUserID]);

  const removeFriend = (userId) => {
    setFriendList(friendList.filter(friendId => friendId !== userId));
  };

  const isFriend = friendList.includes(accountData.appUserID); 

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h2>Account Details</h2>
      <p>ID: {accountData.appUserID}</p>
      <p>First Name: {accountData.firstname}</p>
      <p>Last Name: {accountData.lastname}</p>
      <p>Email: {accountData.email}</p>

      {/* Conditionally render AddFriendButton or DeleteFriendButton */}
      {isFriend ? (
        <deleteFriendButton username={accountData.username} removeFriend={() => removeFriend(accountData.appUserID)} />
      ) : (
        <AddFriendButton username={accountData.username} isLoading={isLoading} error={error} />
      )}

      <SendMessageButton recipient={accountData.username} />
      <UserPosts userid={userId} profileUserId={accountData.appUserID} />
    </div>
  );
};

export default ProfilePage;