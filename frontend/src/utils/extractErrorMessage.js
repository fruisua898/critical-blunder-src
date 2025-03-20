const extractErrorMessage = (err) => {
  if (err.response && err.response.data) {
    if (typeof err.response.data === "object") {
      if (err.response.data.message) {
        return err.response.data.message;
      } else {
        return Object.values(err.response.data).join(" ");
      }
    }
    if (typeof err.response.data === "string") {
      try {
        const parsed = JSON.parse(err.response.data);
        if (parsed.message) {
          return parsed.message;
        } else if (typeof parsed === "object") {
          return Object.values(parsed).join(" ");
        } else {
          return err.response.data;
        }
      } catch (err) {
        return err.response.data;
      }
    }
  }
  if (
    err.message &&
    typeof err.message === "string" &&
    err.message.trim().startsWith("{")
  ) {
    try {
      const parsed = JSON.parse(err.message);
      if (parsed.message) {
        return parsed.message;
      } else if (typeof parsed === "object") {
        return Object.values(parsed).join(" ");
      } else {
        return err.message;
      }
    } catch (err) {
      return err.message;
    }
  }
  return err.message || "Error desconocido";
};

export default extractErrorMessage;