import {useUserContext} from "../../pages/usercontext/UserContext";

const DeleteFriendButton = ({ removeFriend }) => {
  const { user } = useUserContext();
    const handleClick = () => {
      fetch(`/api/friends/${user.appUserID}/removeFriend/${removeFriend}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        removeFriend();
      })
      .catch(error => {
        console.error("Error:", error);
      });
    };
  
    return <button onClick={handleClick}>Remove Friend</button>;
  };
  
  export default DeleteFriendButton;