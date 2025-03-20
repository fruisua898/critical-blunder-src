import React from "react";
import LeftPanel from "../components/LeftPanel";
import CenterPanelDashboard from "../components/CenterPanelDashboard";
import RightPanel from "../components/RightPanel";
import "../styles/styles.scss";

const DashboardPage = () => {
  return (
    <section className="dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <LeftPanel />
        </div>
        <div className="dashboard-center">
          <CenterPanelDashboard />
        </div>
        <div className="dashboard-right">
          <RightPanel />
        </div>
      </div>
    </section>
  );
};

export default DashboardPage;