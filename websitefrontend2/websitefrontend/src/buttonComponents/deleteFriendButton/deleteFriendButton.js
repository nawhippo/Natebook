import {useUserContext} from "../../pages/usercontext/UserContext";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import RemoveIcon from "@mui/icons-material/Remove";

const DeleteFriendButton = ({ removeFriend }) => {
  const { user } = useUserContext();



  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '3px solid black',
    height: '70px',
    width: '125px',
    borderRadius: '30px'
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
  
    return <RemoveIcon style={buttonStyle} onClick={handleClick}></RemoveIcon>;
  };
  
  export default DeleteFriendButton;