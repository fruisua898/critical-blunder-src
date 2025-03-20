import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import LeftPanel from "../components/LeftPanel";
import RightPanel from "../components/RightPanel";
import {
  findCampaignById,
  createCampaign,
  updateCampaignDetails,
} from "../services/campaignService";
import "../styles/styles.scss";

const CampaignPage = () => {
  const navigate = useNavigate();
  const { campaignId } = useParams();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const mode = campaignId ? "update" : "create";

  useEffect(() => {
    if (campaignId) {
      findCampaignById(campaignId)
        .then((data) => {
          setName(data.name || "");
          setDescription(data.description || "");
        })
        .catch((err) => {
          setErrorMsg(err.message);
        });
    }
  }, [campaignId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");
    if (name.trim().length < 3) {
      setErrorMsg("El nombre debe tener al menos 3 caracteres.");
      return;
    }
    if (mode === "create") {
      try {
        const message = await createCampaign({ name, description });
        setSuccessMsg(message);
        navigate("/campaigns");
      } catch (err) {
        setErrorMsg(err.message);
      }
    } else {
      try {
        const message = await updateCampaignDetails(campaignId, {
          name,
          description,
        });
        setSuccessMsg(message);
        navigate("/campaigns");
      } catch (err) {
        setErrorMsg(err.message);
      }
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
            <h2>{mode === "create" ? "Crear Campaña" : "Editar Campaña"}</h2>
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
                <button type="submit">
                  {mode === "create" ? "Crear" : "Actualizar"}
                </button>
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

export default CampaignPage;