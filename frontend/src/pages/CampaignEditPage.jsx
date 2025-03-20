import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import LeftPanel from "../components/LeftPanel";
import RightPanel from "../components/RightPanel";
import EditCampaignModal from "../components/EditCampaignModal";
import {
  findCampaignById,
  updateCampaignDetails,
  updateCampaignStatus,
} from "../services/campaignService";
import "../styles/styles.scss";

const CampaignEditPage = () => {
  const { campaignId } = useParams();
  const navigate = useNavigate();
  const [campaign, setCampaign] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (campaignId) {
      findCampaignById(campaignId)
        .then((data) => setCampaign(data))
        .catch((err) => {
          setErrorMsg(err.message);
        });
    }
  }, [campaignId]);

  const handleModalSave = async (updatedData) => {
    try {
      const messageDetails = await updateCampaignDetails(campaignId, {
        name: updatedData.name,
        description: updatedData.description,
      });
      const messageStatus = await updateCampaignStatus(
        campaignId,
        updatedData.status
      );
      setSuccessMsg(`${messageDetails} ${messageStatus}`);
      setCampaign({ ...campaign, ...updatedData });
      setIsModalOpen(false);
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="campaign-edit-page dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <LeftPanel />
        </div>
        <div className="dashboard-center-edit">
          <h2>Editar Campaña</h2>
          {errorMsg && <div className="error">{errorMsg}</div>}
          {successMsg && <div className="success">{successMsg}</div>}
          <button onClick={() => setIsModalOpen(true)}>Editar Campaña</button>
          <button className="back-button" onClick={() => navigate(-1)}>
            Volver
          </button>
        </div>
        <div className="dashboard-right">
          <RightPanel />
        </div>
      </div>
      {isModalOpen && campaign && (
        <EditCampaignModal
          campaign={campaign}
          onClose={() => setIsModalOpen(false)}
          onSave={handleModalSave}
        />
      )}
    </section>
  );
};

export default CampaignEditPage;