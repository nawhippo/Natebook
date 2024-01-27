import React, {useState} from "react";
import {useUserContext} from "../../pages/usercontext/UserContext";
import {useHistory} from "react-router-dom";
import Cookies from 'js-cookie';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
const DeleteAccountButton = () => {
  const { user, setUser } = useUserContext();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isDeleted, setIsDeleted] = useState(false); 
  const history = useHistory();

  const handleDeleteAccount = async () => {
    setIsLoading(true);
    try {
      const response = await fetchWithJWT(`/api/account/${user.appUserID}/deleteAccount`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      console.log("Account Deleted!");
      history.push("/Feed")
      Cookies.remove('userData');
      setIsDeleted(true);  
    } catch (error) {
      console.error("Error:", error);
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  if (isDeleted) { 
    return <p>Account Deleted</p>;
  }

  const buttonStyle = {
    backgroundColor: 'Red',
    color: '#FFFFFF',
    border: '4px solid black',
  };

  return (
      <div>
        {error && <p>{error}</p>}
        <button  className='button-common'
            onClick={handleDeleteAccount}
            disabled={isLoading}
            style={buttonStyle}
        >
          {isLoading ? "Deleting..." : "Delete"}
        </button>
      </div>
  );
};
export default DeleteAccountButton;