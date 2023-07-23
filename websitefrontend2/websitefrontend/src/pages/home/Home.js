import React, { useState, useEffect } from 'react';

const Home = () => {
  const [homeData, setHomeData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/home');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setHomeData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            FirstName: 
            <input type="text" name="firstName" value={FormData.firstNane} onChange={handleChange} />
          </label>
          <label>
            LastName: 
            <input type="text" name="lastName" value={FormData.firstNane} onChange={handleChange} />
          </label>
        </div>
      </form>
    </div>
  );
};

export default Home;