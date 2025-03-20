import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/styles.scss";

const NewEntity = () => {
  const navigate = useNavigate();

  return (
    <div className="new-entity">
      <button onClick={() => navigate("/character")}>Personaje Nuevo</button>
      <button onClick={() => navigate("/campaign/create")}>
        Campaña Nueva
      </button>
    </div>
  );
};

export default NewEntity;