// src/pages/Home/Home.js
import React, { useEffect, useState } from "react";
import axios from "axios"; // Import Axios directly

const Home = () => {
  const [homeData, setHomeData] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Make the API request when the component mounts
    axios.get("home") // Use the absolute URL
      .then(response => {
        console.log("API Response: ", response.data);
        setHomeData(response.data);
        setIsLoading(false);
      })
      .catch(error => {
        console.error("Error fetching home data:", error);
        setIsLoading(false);
      });
  }, []);

  return (
    <div>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <h2>Welcome to My Homepage</h2>
          <p>{homeData}</p>
        </div>
      )}
    </div>
  );
};

export default Home;