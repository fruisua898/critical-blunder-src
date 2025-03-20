import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import LeftPanel from "../components/LeftPanel";
import RightPanel from "../components/RightPanel";
import { createCampaign } from "../services/campaignService";
import "../styles/styles.scss";

const CampaignCreatePage = () => {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

    if (name.trim().length < 3) {
      setErrorMsg("El nombre debe tener al menos 3 caracteres.");
      return;
    }

    try {
      const message = await createCampaign({ name, description });
      setSuccessMsg(message);
      navigate("/campaigns");
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="campaign-page dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <LeftPanel />
        </div>
        <div className="dashboard-center">
          <div className="campaign-form-container">
            <h2>Crear Campaña</h2>
            <form onSubmit={handleSubmit}>
              <label>Nombre</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Nombre de la campaña"
              />
              <label>Descripción</label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Descripción de la campaña"
              ></textarea>
              <div className="button-row">
                <button type="button" onClick={() => navigate(-1)}>
                  Volver
                </button>
                <button type="submit">Crear</button>
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

export default CampaignCreatePage;