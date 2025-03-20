const API_URL = import.meta.env.VITE_BASE_URL;

const BASE_URL = API_URL + "/heroes";
const BASE_URL_CAMPAIGNS = API_URL + "/campaigns";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token
    ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
    : { "Content-Type": "application/json" };
};

export const listUserHeroes = async () => {
  const res = await fetch(`${BASE_URL}/user`, {
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

export const getHeroesByUserId = async (userId) => {
  const res = await fetch(`${BASE_URL}/user/${userId}`, {
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

export const searchHeroes = async (name, heroClass) => {
  const params = new URLSearchParams();
  if (name) params.append("name", name);
  if (heroClass) params.append("heroClass", heroClass);

  const res = await fetch(`${BASE_URL}/search?${params.toString()}`, {
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

export const findHeroById = async (heroId) => {
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

export const createHero = async (payload) => {
  const res = await fetch(`${BASE_URL}/create`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }
  return res.text();
};

export const updateHero = async (heroId, payload) => {
  const res = await fetch(`${BASE_URL}/${heroId}`, {
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

export const deleteHero = async (heroId) => {
  const res = await fetch(`${BASE_URL}/${heroId}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text.message);
  }
  return res.text();
};

export const getAssignedHeroes = async (campaignId) => {
  const res = await fetch(`${BASE_URL_CAMPAIGNS}/${campaignId}/heroes`, {
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
    `${BASE_URL_CAMPAIGNS}/${campaignId}/update-hero?heroId=${heroId}`,
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
    `${BASE_URL_CAMPAIGNS}/${campaignId}/remove-hero?heroId=${heroId}`,
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