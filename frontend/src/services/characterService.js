const API_URL = import.meta.env.VITE_BASE_URL;
const BASE_URL = API_URL + "/heroes";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token
    ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
    : { "Content-Type": "application/json" };
};

export const getHero = async (heroId) => {
  const res = await fetch(`${BASE_URL}/${heroId}`, {
    method: "GET",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const createHero = async (heroData) => {
  const res = await fetch(`${BASE_URL}/create`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(heroData),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const updateHero = async (heroId, heroData) => {
  const res = await fetch(`${BASE_URL}/${heroId}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(heroData),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const getUserHeroes = async () => {
  const res = await fetch(`${BASE_URL}/user`, {
    method: "GET",
    headers: getAuthHeaders(),
  });
  if (res.status === 204) {
    return [];
  }
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const deleteHero = async (heroId) => {
  const res = await fetch(`${BASE_URL}/${heroId}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};