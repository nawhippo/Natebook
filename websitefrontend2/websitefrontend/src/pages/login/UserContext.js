import React, { createContext, useState, useContext, useEffect } from 'react';

const UserContext = createContext();

export function useUserContext() {
  return useContext(UserContext);
}

export function UserProvider({ children }) {
  const [user, setUser] = useState(null);

  const clearUserContext = () => {
    setUser(null);
  };

  useEffect(() => {
    console.log("User Data:", user);
  }, []);

  return (
    <UserContext.Provider value={{ user, setUser, clearUserContext }}>
      {children}
    </UserContext.Provider>
  );
}