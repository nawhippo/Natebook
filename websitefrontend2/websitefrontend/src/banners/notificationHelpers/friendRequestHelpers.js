// import React, { useState, useEffect } from 'react';
// import { useUserContext } from '../../pages/usercontext/UserContext';
// import { debounce } from 'lodash';
//
// const FriendReqCounter = () => {
//   const { user } = useUserContext();
//   const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
//   const [isLoading, setIsLoading] = useState(true);
//   const [error, setError] = useState(null);
//
//   const fetchwithCsrfFriendRequests = () => {
//     if (user) {
//       fetchwithCsrf(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
//           .then(response => response.json())
//           .then(data => setAllFriendRequestsData(data))
//           .catch(error => setError(error.message))
//           .finally(() => setIsLoading(false));
//     }
//   };
//
//   // Initial fetchwithCsrf
//   fetchwithCsrfFriendRequests();
//
//   // Clear any existing intervals
//   const intervalId = setInterval(fetchwithCsrfFriendRequests, 120000);
//
//   // Clean up previous intervals
//   useEffect(() => {
//     return () => clearInterval(intervalId);
//   }, [intervalId]);
//
//   return (
//       <div>
//         {allFriendRequestsData.length > 0 && <p>Number of Friend Requests: {allFriendRequestsData.length}</p>}
//       </div>
//   );
// };
//
// export default FriendReqCounter;