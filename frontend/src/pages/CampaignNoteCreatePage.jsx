import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CurrentCampaignEntities from "../components/CurrentCampaignEntities";
import RightPanel from "../components/RightPanel";
import { createCampaignNote } from "../services/campaignNoteService";
import "../styles/styles.scss";

const CampaignNoteCreatePage = () => {
  const { campaignId } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

    if (title.trim().length < 3) {
      setErrorMsg("El título debe tener al menos 3 caracteres.");
      return;
    }
    try {
      const payload = { title, content };
      const message = await createCampaignNote(campaignId, payload);
      setSuccessMsg(message);
      navigate(`/campaigns/detail/${campaignId}`);
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="campaign-note-create-page dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <CurrentCampaignEntities />
        </div>
        <div className="dashboard-center">
          <div className="note-form-container">
            <h2>Nueva Nota</h2>
            <form onSubmit={handleSubmit}>
              <label>Título</label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Título de la nota"
              />
              <label>Contenido</label>
              <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Contenido de la nota"
              ></textarea>
              <div className="button-row">
                <button type="button" onClick={() => navigate(-1)}>
                  Volver
                </button>
                <button type="submit">Crear Nota</button>
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

export default CampaignNoteCreatePage;