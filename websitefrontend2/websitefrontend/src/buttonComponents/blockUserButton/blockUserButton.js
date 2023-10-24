import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';

const BlockUserButton = ({ blockId }) => {
  const { user } = useUserContext();
  const [blocked, setBlocked] = useState(false);

  const handleBlockClick = () => {
    fetch(`/api/user/${user.appUserID}/${blockId}`, {
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