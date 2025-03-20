import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import CurrentCampaignEntities from "../components/CurrentCampaignEntities";
import RightPanel from "../components/RightPanel";
import EditHeroModal from "../components/EditHeroModal";
import {
  findCampaignById,
  getAssignedHeroes,
  removeHeroFromCampaign,
  updateHeroInCampaign,
} from "../services/campaignService";
import {
  listCampaignNotes,
  deleteCampaignNote,
} from "../services/campaignNoteService";
import { listEventsByCampaign, deleteEvent } from "../services/eventService";
import "../styles/styles.scss";

const CampaignDetailPage = () => {
  const { id } = useParams();
  const [campaign, setCampaign] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");
  const [activeTab, setActiveTab] = useState("heroes");
  const [notes, setNotes] = useState([]);
  const [events, setEvents] = useState([]);
  const [loadingNotes, setLoadingNotes] = useState(false);
  const [loadingEvents, setLoadingEvents] = useState(false);
  const [assignedHeroes, setAssignedHeroes] = useState([]);
  const [loadingHeroes, setLoadingHeroes] = useState(false);
  const [selectedHero, setSelectedHero] = useState(null);

  useEffect(() => {
    if (id) {
      findCampaignById(id)
        .then((data) => setCampaign(data))
        .catch((err) => setErrorMsg(err.message));
    }
  }, [id]);

  useEffect(() => {
    if (activeTab === "notes" && id) {
      setLoadingNotes(true);
      listCampaignNotes(id)
        .then((data) => {
          setNotes(data);
          setLoadingNotes(false);
        })
        .catch((err) => {
          setErrorMsg(err.message);
          setLoadingNotes(false);
        });
    }
  }, [activeTab, id]);

  useEffect(() => {
    if (activeTab === "events" && id) {
      setLoadingEvents(true);
      listEventsByCampaign(id)
        .then((data) => {
          setEvents(data);
          setLoadingEvents(false);
        })
        .catch((err) => {
          setErrorMsg(err.message);
          setLoadingEvents(false);
        });
    }
  }, [activeTab, id]);

  useEffect(() => {
    if (activeTab === "heroes" && id) {
      setLoadingHeroes(true);
      getAssignedHeroes(id)
        .then((data) => {
          setAssignedHeroes(data);
          setLoadingHeroes(false);
        })
        .catch((err) => {
          setErrorMsg(err.message);
          setLoadingHeroes(false);
        });
    }
  }, [activeTab, id]);

  const handleRemoveHero = async (heroId) => {
    try {
      await removeHeroFromCampaign(id, heroId);
      setAssignedHeroes((prev) =>
        prev.filter((hero) => String(hero.heroId) !== String(heroId))
      );
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  const openEditModal = (heroCampaign) => {
    setSelectedHero(heroCampaign);
  };

  const closeEditModal = () => {
    setSelectedHero(null);
  };

  const handleSaveHero = async (updatedData) => {
    try {
      await updateHeroInCampaign(id, selectedHero.heroId, updatedData);
      const updatedHeroes = await getAssignedHeroes(id);
      setAssignedHeroes(updatedHeroes);
      closeEditModal();
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  const handleDeleteNote = async (noteId) => {
    try {
      await deleteCampaignNote(noteId);
      setNotes((prev) => prev.filter((note) => note.id !== noteId));
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  const handleDeleteEvent = async (eventId) => {
    try {
      await deleteEvent(eventId);
      setEvents((prev) => prev.filter((event) => event.id !== eventId));
    } catch (err) {
      setErrorMsg(err.message);
    }
  };

  return (
    <section className="campaign-detail dashboard">
      <div className="dashboard-container">
        <div className="dashboard-left">
          <CurrentCampaignEntities />
        </div>
        <div className="dashboard-center">
          {campaign ? (
            <div className="detail-container">
              <h2>{campaign.name}</h2>
              <p>{campaign.description}</p>
              <div className="tabs">
                <button
                  className={activeTab === "heroes" ? "active" : ""}
                  onClick={() => setActiveTab("heroes")}
                >
                  Heroes
                </button>
                <button
                  className={activeTab === "notes" ? "active" : ""}
                  onClick={() => setActiveTab("notes")}
                >
                  Notas
                </button>
                <button
                  className={activeTab === "events" ? "active" : ""}
                  onClick={() => setActiveTab("events")}
                >
                  Eventos
                </button>
              </div>
              <div className="tab-content">
                {activeTab === "heroes" && (
                  <div>
                    {loadingHeroes ? (
                      <p>Cargando héroes...</p>
                    ) : assignedHeroes.length > 0 ? (
                      <table className="entity-table">
                        <thead>
                          <tr>
                            <th>Nombre</th>
                            <th>Nivel</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                          </tr>
                        </thead>
                        <tbody>
                          {assignedHeroes.map((heroCampaign) => (
                            <tr key={heroCampaign.heroId}>
                              <td>{heroCampaign.heroName}</td>
                              <td>{heroCampaign.level}</td>
                              <td>{heroCampaign.status}</td>
                              <td>
                                <button
                                  onClick={() => openEditModal(heroCampaign)}
                                >
                                  Editar
                                </button>
                                <button
                                  onClick={() =>
                                    handleRemoveHero(heroCampaign.heroId)
                                  }
                                >
                                  Expulsar
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    ) : (
                      <p>No hay héroes asignados a esta campaña.</p>
                    )}
                  </div>
                )}
                {activeTab === "notes" && (
                  <div>
                    {loadingNotes ? (
                      <p>Cargando notas...</p>
                    ) : notes.length > 0 ? (
                      <table className="entity-table">
                        <thead>
                          <tr>
                            <th>Título</th>
                            <th>Contenido</th>
                            <th>Acción</th>
                          </tr>
                        </thead>
                        <tbody>
                          {notes.map((note) => (
                            <tr key={note.id}>
                              <td>{note.title}</td>
                              <td>{note.content}</td>
                              <td>
                                <button
                                  onClick={() => handleDeleteNote(note.id)}
                                >
                                  Borrar
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    ) : (
                      <p>No hay notas para esta campaña.</p>
                    )}
                  </div>
                )}
                {activeTab === "events" && (
                  <div>
                    {loadingEvents ? (
                      <p>Cargando eventos...</p>
                    ) : events.length > 0 ? (
                      <table className="entity-table">
                        <thead>
                          <tr>
                            <th>Título</th>
                            <th>Descripción</th>
                            <th>Acción</th>
                          </tr>
                        </thead>
                        <tbody>
                          {events.map((event) => (
                            <tr key={event.id}>
                              <td>{event.title}</td>
                              <td>{event.description}</td>
                              <td>
                                <button
                                  onClick={() => handleDeleteEvent(event.id)}
                                >
                                  Borrar
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    ) : (
                      <p>No hay eventos para esta campaña.</p>
                    )}
                  </div>
                )}
              </div>
            </div>
          ) : errorMsg ? (
            <div className="error">{errorMsg}</div>
          ) : (
            <p>Cargando campaña...</p>
          )}
        </div>
        <div className="dashboard-right">
          <RightPanel />
        </div>
      </div>
      {selectedHero && (
        <EditHeroModal
          hero={selectedHero}
          campaignId={id}
          onClose={closeEditModal}
          onSave={handleSaveHero}
        />
      )}
    </section>
  );
};

export default CampaignDetailPage;