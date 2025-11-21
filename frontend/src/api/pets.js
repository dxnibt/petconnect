// api/pets.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:9494/mascotas",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getPet = (id) => api.get(`/${id}`);
export const getPets = (page = 0, size = 10) => api.get(`/List`, { params: { page, size } });
export const createPet = (data) => api.post("/save", data);
export const updatePet = (id, data) => api.patch(`/update/${id}`, data);
export const deletePet = (id) => api.delete(`/delete/${id}`);