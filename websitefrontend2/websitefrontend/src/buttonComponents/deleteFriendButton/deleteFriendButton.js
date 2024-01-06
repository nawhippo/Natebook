import {useUserContext} from "../../pages/usercontext/UserContext";
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const DeleteFriendButton = ({ removeFriend }) => {
  const { user } = useUserContext();
    const handleClick = () => {
      fetchWithJWT(`/api/friends/${user.appUserID}/removeFriend/${removeFriend}`, {
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