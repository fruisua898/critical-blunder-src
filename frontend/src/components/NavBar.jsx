import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/styles.scss";

const NavBar = () => {
  const navigate = useNavigate();

  return (
    <div className="new-entity">
      <button onClick={() => navigate("/dashboard")}>Inicio</button>
      <button onClick={() => navigate("/campaigns")}>Campañas</button>
      <button onClick={() => navigate("/heroes")}>Héroes</button>
    </div>
  );
};

export default NavBar;