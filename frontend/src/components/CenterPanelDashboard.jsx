import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/styles.scss";

const CenterPanelDashboard = () => {
  const navigate = useNavigate();
  return (
    <div className="center-panel-dashboard">
      <button onClick={() => navigate("/campaigns")}>Campañas</button>
      <button onClick={() => navigate("/heroes")}>Héroes</button>
    </div>
  );
};

export default CenterPanelDashboard;