import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../styles/styles.scss";

const CurrentCampaignEntities = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  return (
    <div className="current-campaign-entities">
      <button className="back-btn" onClick={() => navigate(-1)}>
        Volver
      </button>
      <button onClick={() => navigate(`/campaign/${id}/assign-hero`)}>
        Asignar Personaje
      </button>
      <button onClick={() => navigate(`/campaign/${id}/create-note`)}>
        Nueva Nota
      </button>
      <button onClick={() => navigate(`/campaign/${id}/create-event`)}>
        Nuevo Evento
      </button>
    </div>
  );
};

export default CurrentCampaignEntities;