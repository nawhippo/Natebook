// src/pages/About/About.js
import React, { useEffect, useState } from "react";
import axios from "axios"; // Import Axios directly

const About = () => {
  const [aboutData, setAboutData] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Make the API request when the component mounts
    axios.get("about") // Use the absolute URL
      .then(response => {
        console.log("API Response: ", response.data);
        setAboutData(response.data);
        setIsLoading(false);
      })
      .catch(error => {
        console.error("Error fetching about data:", error);
        setIsLoading(false);
      });
  }, []);

  return (
    <div>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <p style={{ color: "red", fontSize: 25 }}>{aboutData || "No data available"}</p>
        </div>
      )}
    </div>
  );
};

export default About;