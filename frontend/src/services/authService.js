const API_URL = import.meta.env.VITE_BASE_URL;
const BASE_URL = API_URL + "/auth";
const handleErrorResponse = async (response) => {
  const responseText = await response.text();
  try {
    const responseData = JSON.parse(responseText);
    if (typeof responseData === "object" && responseData !== null) {
      const errorMessages = Object.values(responseData).join(" ");
      throw new Error(errorMessages);
    }
  } catch {
    throw new Error(responseText || "Error en la petición.");
  }
};

export const login = async (email, password) => {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      await handleErrorResponse(response);
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);
    if (data.name) {
      localStorage.setItem("name", data.name);
    }
    return data;
  } catch (error) {
    throw error;
  }
};

export const register = async (email, name, password) => {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, name, password }),
    });

    if (!response.ok) {
      await handleErrorResponse(response);
    }

    return "Usuario registrado con éxito.";
  } catch (error) {
    throw error;
  }
};