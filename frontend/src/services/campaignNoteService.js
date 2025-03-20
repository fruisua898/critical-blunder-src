const API_URL = import.meta.env.VITE_BASE_URL;
const BASE_URL = API_URL + "/notes";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token
    ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
    : { "Content-Type": "application/json" };
};

export const createCampaignNote = async (campaignId, payload) => {
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

export const listCampaignNotes = async (campaignId) => {
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

export const updateCampaignNote = async (noteId, payload) => {
  const res = await fetch(`${BASE_URL}/${noteId}`, {
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

export const deleteCampaignNote = async (noteId) => {
  const res = await fetch(`${BASE_URL}/${noteId}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};