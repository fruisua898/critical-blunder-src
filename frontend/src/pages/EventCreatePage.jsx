import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CurrentCampaignEntities from "../components/CurrentCampaignEntities";
import RightPanel from "../components/RightPanel";
import { createEvent } from "../services/eventService";
import "../styles/styles.scss";

const EventCreatePage = () => {
  const { campaignId } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

    if (title.trim().length < 3) {
      setErrorMsg("El nombre del evento debe tener al menos 3 caracteres.");
      return;
    }
    try {
      const payload = { title, description };
      const message = await createEvent(campaignId, payload);
      setSuccessMsg(message);
      navigate(`/campaigns/detail/${campaignId}`);
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="event-create-page dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <CurrentCampaignEntities />
        </div>
        <div className="dashboard-center">
          <div className="event-form-container">
            <h2>Nuevo Evento</h2>
            <form onSubmit={handleSubmit}>
              <label>Nombre del Evento</label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Nombre del evento"
              />
              <label>Descripción</label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Descripción del evento"
              ></textarea>
              <div className="button-row">
                <button type="button" onClick={() => navigate(-1)}>
                  Volver
                </button>
                <button type="submit">Crear Evento</button>
              </div>
            </form>
            {errorMsg && <div className="error">{errorMsg}</div>}
            {successMsg && <div className="success">{successMsg}</div>}
          </div>
        </div>
        <div className="dashboard-right">
          <RightPanel />
        </div>
      </div>
    </section>
  );
};

export default EventCreatePage;