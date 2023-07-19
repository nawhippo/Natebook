import React, { useState } from 'react';

const UserForm = () => {
    const[formData, setFormData] = useState({
        name: '',
        email: '',
        password: ''
    });
const handleChange = (e) => {
    setFormData({
        ...formData,
        [e.target.name]: e.target.value
    });
};

const handleSubmit = async (e) => {
    e.preventDefault();

    try{
        await axios.post('http://localhost:8080/users/createAccount', formData);
        console.log('Account created!');
    } catch(error) {
    console.error('Error creating account:', error);
    }
};

return (
    <div>
        <h2>Customized User Form</h2>
        <form onSubmit={handleSubmit}>
            <div>
                <label>Name:</label>
                <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                />
            </div>
            <div>
                <label>Email:</label>
                <input 
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                />
            </div>
            <div>
                <label>Password:</label>
                <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                />
            </div>
            <button type="submit">Submit</button>
        </form>
    </div>
);
};

export default UserForm;