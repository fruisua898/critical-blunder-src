import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/styles.scss";

const CenterPanelCampaign = () => {
  const navigate = useNavigate();
  return (
    <div className="center-panel-campaign">
      <button onClick={() => navigate("/campaigns/owner")}>
        Campañas Creadas
      </button>
      <button
        onClick={() =>
          navigate("/campaigns/participant", { state: { notOwner: true } })
        }
      >
        Campañas Participante
      </button>
    </div>
  );
};

export default CenterPanelCampaign;