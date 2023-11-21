import React, { createContext, useState, useContext, useEffect } from 'react';
import Cookies from 'js-cookie';

const UserContext = createContext();

export function useUserContext() {
  return useContext(UserContext);
}

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const cookieUserData = Cookies.get('userData');
    if (cookieUserData) {
      setUser(JSON.parse(cookieUserData)); // Set user from cookie data
    } else {
      setUser(null); // Set user to null if no cookie data
    }
  }, []); // Empty dependency array, runs only on mount

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