import {useUserContext} from "../../pages/usercontext/UserContext";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const DeleteFriendButton = ({ removeFriend }) => {
  const { user } = useUserContext();



  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '4px solid black',
  };
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
  
    return <button style={buttonStyle} onClick={handleClick}>Remove Friend</button>;
  };
  
  export default DeleteFriendButton;