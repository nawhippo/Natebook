import React, { useState } from "react";

const MyForm = ({ userId }) => {
  const [formData, setFormData] = useState({
    recipients: [],
    content: "",
  });


  const handleSubmit = (event) => {
    event.preventDefault();


    const { recipients, content } = formData;
    fetch(`/api/users/${userId}/sendMessage`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ recipients, content }),
    })
      .then((response) => {
        if (response.ok) {
          // Handle success
          console.log("Message created successfully!");
        } else {
          // Handle error
          console.error("Failed to send message.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    // Clear the form fields after submission
    setFormData({
      recipients: [],
      content: "",
    });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            Recipients (comma-separated):
            <input
              type="text"
              name="recipients"
              value={formData.recipients}
              onChange={handleChange}
            />
          </label>
          <label>
            Content:
            <textarea
              name="content"
              value={formData.content}
              onChange={handleChange}
            />
          </label>
        </div>
        <button type="submit">Send Message</button>
      </form>
    </div>
  );
};

export default MyForm;