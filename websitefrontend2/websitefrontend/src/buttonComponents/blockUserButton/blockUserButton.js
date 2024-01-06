import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const BlockUserButton = ({ blockId }) => {
  const { user } = useUserContext();
  const [blocked, setBlocked] = useState(false);

  const handleBlockClick = () => {
    fetchWithJWT(`/api/user/${user.appUserID}/${blockId}`, {
      method: 'PUT'
    }).then(() => setBlocked(true));
  };

  return (
    <>
      {blocked ? 'User blocked!' : <button onClick={handleBlockClick}>Block User</button>}
    </>
  );
};

export default BlockUserButton;