import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import MobileBackButton from "../components/MobileBackButton";
import LeftPanel from "../components/LeftPanel";
import RightPanel from "../components/RightPanel";
import {
  listCampaignsOwner,
  listCampaignsParticipant,
  deleteCampaign,
} from "../services/campaignService";
import "../styles/styles.scss";
const CampaignListPage = () => {
  const { mode } = useParams();
  const [campaigns, setCampaigns] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (mode === "owner") {
      listCampaignsOwner()
        .then((data) => setCampaigns(data))
        .catch((err) => setErrorMsg(err.message));
    } else if (mode === "participant") {
      listCampaignsParticipant()
        .then((data) => setCampaigns(data))
        .catch((err) => setErrorMsg(err.message));
    }
  }, [mode]);

  const handleDelete = async (campaignId) => {
    try {
      await deleteCampaign(campaignId);
      setCampaigns((prev) => prev.filter((c) => c.id !== campaignId));
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <LeftPanel />
        </div>
        <div className="dashboard-center">
          <div className="campaign-list-container">
            <h2>
              {mode === "owner"
                ? "Mis Campañas (como Game Master)"
                : "Campañas donde participo"}
            </h2>
            {errorMsg && <div className="error">{errorMsg}</div>}
            <div className="campaign-list">
              {campaigns.map((camp) => (
                <div className="campaign-item" key={camp.id}>
                  <div className="campaign-name">{camp.name}</div>
                  <div className="campaign-status">
                    Estado: {camp.status || "Desconocido"}
                  </div>
                  <div className="campaign-gm">GM: {camp.gameMasterName}</div>
                  {mode === "owner" ? (
                    <div className="campaign-actions">
                      <button
                        onClick={() => navigate(`/campaigns/detail/${camp.id}`)}
                      >
                        Entrar
                      </button>
                      <button
                        onClick={() => navigate(`/campaign/edit/${camp.id}`)}
                      >
                        Editar
                      </button>
                      <button onClick={() => handleDelete(camp.id)}>
                        Borrar
                      </button>
                    </div>
                  ) : (
                    <div className="campaign-actions">
                      <button
                        onClick={() => navigate(`/campaigns/detail/${camp.id}`)}
                      >
                        Entrar
                      </button>
                    </div>
                  )}
                </div>
              ))}
              {campaigns.length === 0 && !errorMsg && (
                <p>No se encontraron campañas.</p>
              )}
            </div>
          </div>
        </div>
        <div className="dashboard-right">
          <RightPanel />
        </div>
      </div>
      <MobileBackButton />
    </section>
  );
};

export default CampaignListPage;