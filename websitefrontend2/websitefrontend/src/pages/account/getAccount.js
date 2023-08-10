import { useEffect, useState } from "react";
import {react} from React;
import { useUserContext } from "../login/UserContext";

const getAccount =() =>{
    const { user } = useUserContext();
    const [accountData, setaccountData] = useState(null);
    cosnt [Error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
    console.log(user);
    setaccountData(user);
    if (user){
    fetch(`/api/${user.appUserId}}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        setUsers(data);
        setIsLoading(false);
      })
      .catch(error => {
        setError(error.message);
        setIsLoading(false);
      });
  } 
}, [user]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }


  return (
    <div>Account Details
    <p>id : ${user.appUserId}</p>
    <p>First Name : ${user.firstname}</p>
    <p>Last Name : ${user.lastname}</p>
    <p>Email : ${user.email}</p>
    </div>
  );
} 
export default getAccount;