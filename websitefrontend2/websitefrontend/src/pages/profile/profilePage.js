const GetUserProfile = ({ userId }) => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [friendList, setFriendList] = useState([]); // list of Longs
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

        // Fetch your friend list
        const friendListResponse = await fetch(`/api/friends/${user.appUserID}`);
        if (!friendListResponse.ok) {
          throw new Error("Failed to fetch friends");
        }
        const friendData = await friendListResponse.json(); // assuming this returns a list of Longs
        setFriendList(friendData.friends); // adapt as needed

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

  const isFriend = friendList.includes(accountData.appUserID); // change to friendList.some() if needed

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
        <DeleteFriendButton username={accountData.username} removeFriend={() => removeFriend(accountData.appUserID)} />
      ) : (
        <AddFriendButton username={accountData.username} isLoading={isLoading} error={error} />
      )}

      <SendMessageButton recipient={accountData.username} />
      <UserPosts userid={userId} profileUserId={accountData.appUserID} />
    </div>
  );
};

export default GetUserProfile;