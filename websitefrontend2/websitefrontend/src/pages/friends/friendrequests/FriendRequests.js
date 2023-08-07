import React, { useState } from "react";
import { react } from React;
import { useUserContext } from "../../login/UserContext";
const ViewAllFriendRequests = () => {
const { user } = useUserContext();
const [friendReqData, setfriendReqData] = useState(false);
}