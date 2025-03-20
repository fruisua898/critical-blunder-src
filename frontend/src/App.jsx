import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import DashboardPage from "./pages/DashboardPage";
import CampaignDashboardPage from "./pages/CampaignDashboardPage";
import CampaignListPage from "./pages/CampaignListPage";
import CampaignCreatePage from "./pages/CampaignCreatePage";
import AssignHeroToCampaign from "./pages/AssignHeroToCampaign";
import CampaignEditPage from "./pages/CampaignEditPage";
import CampaignDetailPage from "./pages/CampaignDetailPage";
import CampaignNoteCreatePage from "./pages/CampaignNoteCreatePage";
import EventCreatePage from "./pages/EventCreatePage";
import HeroesPage from "./pages/HeroesPage";
import CharacterPage from "./pages/CharacterPage";
import Header from "./components/Header";
import Footer from "./components/Footer";

const PrivateRoute = ({ children }) => {
  const isAuthenticated = !!localStorage.getItem("token");
  return isAuthenticated ? children : <Navigate to="/login" />;
};

function Layout({ children }) {
  return (
    <div className="app-container">
      <Header />
      <main>{children}</main>
      <Footer />
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            <Layout>
              <AuthPage />
            </Layout>
          }
        />
        <Route
          path="/register"
          element={
            <Layout>
              <AuthPage />
            </Layout>
          }
        />

        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Layout>
                <DashboardPage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/campaigns"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignDashboardPage />
              </Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/campaigns/:mode"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignListPage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/campaign/create"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignCreatePage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/campaign/edit/:campaignId"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignEditPage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/campaigns/detail/:id"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignDetailPage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/campaign/:campaignId/create-note"
          element={
            <PrivateRoute>
              <Layout>
                <CampaignNoteCreatePage />
              </Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/campaign/:campaignId/create-event"
          element={
            <PrivateRoute>
              <Layout>
                <EventCreatePage />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route
          path="/heroes"
          element={
            <PrivateRoute>
              <Layout>
                <HeroesPage />
              </Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/character"
          element={
            <PrivateRoute>
              <Layout>
                <CharacterPage />
              </Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/character/:heroId"
          element={
            <PrivateRoute>
              <Layout>
                <CharacterPage />
              </Layout>
            </PrivateRoute>
          }
        />
        <Route
          path="/campaign/:campaignId/assign-hero"
          element={
            <PrivateRoute>
              <Layout>
                <AssignHeroToCampaign />
              </Layout>
            </PrivateRoute>
          }
        />

        <Route path="/" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;