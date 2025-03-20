import extractErrorMessage from "../utils/extractErrorMessage";

const API_URL = import.meta.env.VITE_BASE_URL;
const BASE_URL = API_URL + "/campaigns";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token
    ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
    : { "Content-Type": "application/json" };
};

export const listCampaignsOwner = async () => {
  const res = await fetch(`${BASE_URL}/owner`, {
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

export const listCampaignsParticipant = async () => {
  const res = await fetch(`${BASE_URL}/participant`, {
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

export const findCampaignById = async (id) => {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "GET",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.json();
};

export const createCampaign = async (payload) => {
  const res = await fetch(`${BASE_URL}/create`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const text = await res.text();
    const errorObj = { message: text };
    throw new Error(extractErrorMessage(errorObj));
  }
  return res.text();
};

export const updateCampaignDetails = async (campaignId, payload) => {
  const res = await fetch(`${BASE_URL}/${campaignId}/update`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const updateCampaignStatus = async (campaignId, newStatus) => {
  const res = await fetch(
    `${BASE_URL}/${campaignId}/status?newStatus=${encodeURIComponent(
      newStatus
    )}`,
    {
      method: "PUT",
      headers: getAuthHeaders(),
    }
  );
  if (!res.ok) {
    const text = await res.text();
    const errorObj = { message: text };
    throw new Error(extractErrorMessage(errorObj));
  }
  return res.text();
};

export const deleteCampaign = async (campaignId) => {
  const res = await fetch(`${BASE_URL}/${campaignId}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const assignHeroToCampaign = async (campaignId, heroId) => {
  const res = await fetch(
    `${BASE_URL}/${campaignId}/assign-hero?heroId=${heroId}`,
    {
      method: "POST",
      headers: getAuthHeaders(),
    }
  );
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const getAssignedHeroes = async (campaignId) => {
  const res = await fetch(`${BASE_URL}/${campaignId}/heroes`, {
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

export const updateHeroInCampaign = async (campaignId, heroId, payload) => {
  const res = await fetch(
    `${BASE_URL}/${campaignId}/update-hero?heroId=${heroId}`,
    {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(payload),
    }
  );
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const removeHeroFromCampaign = async (campaignId, heroId) => {
  const res = await fetch(
    `${BASE_URL}/${campaignId}/remove-hero?heroId=${heroId}`,
    {
      method: "POST",
      headers: getAuthHeaders(),
    }
  );
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};