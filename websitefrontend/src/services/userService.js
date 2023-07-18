import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Replace with your backend URL

const userService = {
  getAllFriends: async (userId) => {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/friends`);
    return response.data;
  },
  getUserById: async (userId) => {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}`);
    return response.data;
  },
  getAllMessages: async (userId) => {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/messages`);
    return response.data;
  },
  getMessageById: async (userId, messageId) => {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/${messageId}`);
    return response.data;
  },
  getPostById: async (userId, postId) => {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/${postId}`);
    return response.data;
  },
  createAccount: async (user) => {
    await axios.post(`${API_BASE_URL}/users/createAccount`, user);
  },
};

export default userService;