import React from "react";
import LeftPanel from "../components/LeftPanel";
import RightPanel from "../components/RightPanel";
import MobileBackButton from "../components/MobileBackButton";
import CenterPanelCampaign from "../components/CenterPanelCampaign";
import "../styles/styles.scss";

const CampaignDashboardPage = () => {
  return (
    <section className="dashboard">
      <div className="dashboard-container">
        <aside className="dashboard-left">
          <LeftPanel />
        </aside>
        <section className="dashboard-center">
          <CenterPanelCampaign />
        </section>
        <aside className="dashboard-right">
          <RightPanel />
        </aside>
      </div>
      <MobileBackButton />
    </section>
  );
};

export default CampaignDashboardPage;