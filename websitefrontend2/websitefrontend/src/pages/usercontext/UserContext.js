import React, { createContext, useState, useContext, useEffect } from 'react';
import Cookies from 'js-cookie';

const UserContext = createContext();

export function useUserContext() {
  return useContext(UserContext);
}

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    console.log("User Data set:", user);
    const userData = Cookies.get('userData');
    if(userData && !user){
      setUser(JSON.parse(userData));
    }
  }, []);

  const clearUserContext = () => {
    setUser(null);
    Cookies.remove('userData');
  };



  return (
    <UserContext.Provider value={{ user, setUser, clearUserContext }}>
      {children}
    </UserContext.Provider>
  );
}
