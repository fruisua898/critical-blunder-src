const API_URL = import.meta.env.VITE_BASE_URL;
const BASE_URL = API_URL + "/events";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token
    ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
    : { "Content-Type": "application/json" };
};

export const createEvent = async (campaignId, payload) => {
  const res = await fetch(`${BASE_URL}/${campaignId}/create`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const listEventsByCampaign = async (campaignId) => {
  const res = await fetch(`${BASE_URL}/${campaignId}`, {
    method: "GET",
    headers: getAuthHeaders(),
  });
  if (res.status === 204) return [];
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const updateEvent = async (eventId, payload) => {
  const res = await fetch(`${BASE_URL}/${eventId}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const deleteEvent = async (eventId) => {
  const res = await fetch(`${BASE_URL}/${eventId}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};