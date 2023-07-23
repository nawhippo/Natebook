// UserController.js
import React from 'react';

const UserControllerComp = () => {
  //function to call the API and fetch data for a specific endpoint
  const fetchData = async (endpoint) => {
    try {
      const response = await fetch(`/api${endpoint}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  const renderEndpointData = (data) => {
    return <pre>{JSON.stringify(data, null, 2)}</pre>;
  };

  // Endpoint paths
  const endpoints = [
    '/',
    '/{userId}/friends',
    '/{userId}',
    '/{userId}/messages',
    '/{userId}/{messageId}',
    '/{userId}/{postId}',
    '/createAccount',
  ];

  return (
    <div>
      <h1>User Controller Endpoints</h1>
      {endpoints.map((endpoint, index) => (
        <div key={index}><h2>Endpoint: {endpoint}</h2>
          <button
            onClick={async () => {
              const data = await fetchData(endpoint);
              console.log(data);
            }}
          >
            Fetch Data for Endpoint
          </button>
        </div>
      ))}
    </div>
  );
};

export default UserControllerComp;